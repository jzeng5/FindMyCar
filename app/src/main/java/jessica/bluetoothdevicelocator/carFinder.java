package jessica.bluetoothdevicelocator;

import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class carFinder extends Activity {

    LocationManager locationManager;
    String provider;
    boolean bluetoothDetached = false;
    double latitude, attitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_finder);

        final Button compassButton = (Button) findViewById(R.id.compass);
        final Button googleButton = (Button) findViewById(R.id.googleNavigate);

        IntentFilter disconnectFilter =
                new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(bluetoothEventReceiver, disconnectFilter);

        setLocationProvider();
        setLocationListener();

        googleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Location currentLocation = locationManager.getLastKnownLocation(provider);
                double currentLatitude = currentLocation.getLatitude();
                double currentLongitude = currentLocation.getLongitude();
                String url = "http://maps.google.com/maps?saddr=" + currentLatitude + ","
                        + currentLongitude + "&daddr=" + latitude + "," + longitude + "&mode=walking";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        compassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(carFinder.this, compassActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("attitude", attitude);
                startActivity(intent);
            }
        });
    }

    /**
     * Check if best-available location provider is enabled,
     * enable the provider if not
     */
    private final void setLocationProvider() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GPSEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkLocationEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (GPSEnabled)
            provider = LocationManager.GPS_PROVIDER;
        else if (networkLocationEnabled)
            provider = LocationManager.NETWORK_PROVIDER;
        else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        System.out.println("@Info Location service provider:" + provider);
    }


    private final void setLocationListener() {

        locationManager.requestLocationUpdates(provider, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (bluetoothDetached) {
                    location = locationManager.getLastKnownLocation(provider);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    attitude = location.getAltitude();
                    System.out.println("@Info " + Double.toString(latitude) + " "
                            + Double.toString(longitude) + " " + Double.toString(attitude));
                    Toast.makeText(getApplicationContext(), "@Info " + Double.toString(latitude)
                                    + " " + Double.toString(longitude) + " " +
                                    Double.toString(attitude), Toast.LENGTH_LONG).show();
                    bluetoothDetached = false;
                } else if (compassActivity.compassEnable) {
                    compassActivity.currentLatitude = location.getLatitude();
                    compassActivity.currentLongitude = location.getLongitude();
                    compassActivity.currentAttitude = location.getAltitude();
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                System.out.println("@Warning Provider disabled");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

        });
    }

    private final BroadcastReceiver bluetoothEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                int deviceType = device.getBluetoothClass().getMajorDeviceClass();
                switch (deviceType) {
                    case BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED:
                        // for debug use, simulate car audio
                        System.out.println("@Info Portable audio disconnected");
                        //break;
                    case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                        //Location location = locationManager.getLastKnownLocation(provider);
                        bluetoothDetached = true;
                        System.out.println("@Info Car audio disconnected");
                        break;
                    default:
                        System.out.println("@Info Disconnected device type: " +
                                Integer.toString(deviceType));
                        break;
                }
            }
        }
    };


}

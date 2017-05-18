package jessica.bluetoothdevicelocator;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jessicazeng1127 on 2/27/16.
 */

public class compassActivity extends Activity implements SensorEventListener{
    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;
    private TextView Vertical_dire;
    private double latitude, attitude, longitude;
    public static double currentLatitude, currentAttitude, currentLongitude;
    public static boolean compassEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        Vertical_dire = (TextView) findViewById(R.id.DIRE);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Bundle bundle = getIntent().getExtras();
        latitude = bundle.getDouble("latitude");
        attitude = bundle.getDouble("attitude");
        longitude = bundle.getDouble("longitude");

        compassEnable = true;
        
    }
    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {


        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        // get the angle between the car and user
        double curr_lat =34.13773589;
        double curr_long = 118.140106;
        double dy = latitude-curr_lat;
        double dx = longitude-curr_long;
        // double dx = Math.cos(Math.PI/180*car_lat)*(car_long - curr_long);
        //double ang = Math.toDegrees(Math.atan2(dy, dx));
        double ang = Math.toDegrees((Math.tan(dx/dy)));
        float act_angle = (float) ang;

        //get current alt
        double curr_alt = 6;
        // compare and set text direction to up or down or right level
        final TextView Vd = (TextView)findViewById(R.id.DIRE);
        if (curr_alt < attitude){
            Vd.setText(R.string.UP);
        }
        else if (curr_alt > attitude){
            Vd.setText(R.string.DOWN);
        }
        else {
            Vd.setText(R.string.RIGHT);
        }
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -(degree-act_angle),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        //System.out.println("@123" + Double.toString(act_angle) + " " + Double.toString(degree));

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -(degree-act_angle);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compassEnable = false;
    }
}






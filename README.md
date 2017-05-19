# FindMyCar
A Hackthon project, android application to helper users find where they parked their cars.

# Author:
Jessica Zeng, Zephyr Yao and Edwin Angkasa.

# What it does
Our android app, Find My Car can help people find where they park their cars, no matter it is an outdoor parking lot or huge parking structure. Find My Car applies to car with bluetooth connection. User needs to turn on the bluetooth on his cell phone. When the bluetooth connects to the car device, our app will recognize the connection. When user stop the car engine, the disconnection of the bluetooth and cell phone will trigger our app to mark the altitude, latitude, longitude of that location, which is where the car parks. And when the user needs to find his car, he will turn on Find My Car. There are two selections he can choose. If he is unaware of where the parking structure is, he can select Direction, which uses google map to direct use to the parking structure/parking lot. And inside the parking space, he can switch the mode to walking route, then there will be an compass tells user which direction his car is at now using a compass. There also will be an instruction telling the user should go up or go down to reach the floor he parked.

# How we built it
1. We use android studio to develop the application.
2. We use the disconnect of the bluetooth and the car to active our program.
3. We have different options to get the parking location of the car: the top selection is GPS, if GPS is not available, we use the nearby network's physical address to determine the user. 
4. We use google map to direct the user from where he is to the parking structure/parking lot.
5. Whe the user enters the parking structure/parking lot, we will instruct the user go up or down to reach the car's parking level. 
6. When the user is in the parking structure/parking lot, we use the current location of the phone, and the car's location to determine which direction the user walk would approach the car. We calculate the angular displacement analyzing the azimuth.


# What's next for FindMyCar
1. Improve the user interface by styling the button and fonts.
2. Improve the accuracy of the car's location. 
3. Implement our own map to displace the closest route.
4. Apply the application on other platforms, such as ios and smart watch. 
5. Apply voice interaction function to our app.


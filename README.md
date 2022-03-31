# Bluedot Location Tracker

This project was developed as requires of Bluedot small task.

This project requires up-to-date versions of the Android build tools and the Android support repository.
* This peoject may require that you add your own Google Maps API key, try to run it first, if the map doesn't work, see the "How to get an Google API Key" part to get your own key.

## How to run

1. I assumed you have the copy of this project, then in the welcome screen of Android Studio, select "Open an Existing project"
2. Build and run

## How to get an Google API Key

1. Follow this guide to get your Google API Key: https://developers.google.com/maps/documentation/android-sdk/get-api-key
2. Open the project, go to ./app/src/main/AndroidManifest.xml, line 26, you will see these code:
            
            android:value="AIzaSyDtJZLIO9bMzd1gQ9EZTEjINI_dJTn1sOA"
            
3. Change the exiting value to your Key
4. Build and run

## My focus

The most important thing here is calculate distance, and I considered a more complicated function that would take into account the height of the two places while comparing latitude and longitude, but I was worried that this would instead be wrong, plus it might make things too complicated, and I ended up choosing to use an existing function to calculate distance.

It was a very simple app and I didn't think too much about it. In the end it wasn't anything over the top.

## Decisions when developing

Why Android - I had implemented something similar in the Android App and had a little memory of how it was done.

Before getting started, I searched Stack Overflow for some examples.

At first I was going to write a simple, UI-less interface that would show latitude and longtitude and calculate distance. but in the process I felt it was a bit monotonous and I recreated a new Map project to try out in Map.

In the process of writing the code, I basically didn't encounter any very difficult places. In `android.location.Location` there is the most crucial point I need: the distanceTo() function, after realising this point I just try to get and compare the old and new locations and the distance between them. If it's not more than 10m I don't update the stored location information, if it is I use Toast to show the distance moved and update the oldLocation data.

I considered some other functions such as turning off and on tracking, but since the project didn't require it, I didn't implement this part.

## Copied code, references and 3rd party libraries

I used Google API and relevant libraries.
For location authority, insted of using several lines to do so, I copied the PermissionUtils class in this project to make it more effecient: https://github.com/googlemaps/android-samples/tree/master/ApiDemos/java/app/src/gms/java/com/example/mapdemo

## Time

I spent around 10 hrs in total developing this project because I did another one without Google map first, I thought that was too straight, then decided to implement a map, then it takes a little bit long time.

## Other - when you running

If you change a more distant position in the simulator, it may take a few seconds to move the blue dot to the centre of the screen. Before the simulator completes this step, you may see the map's camera move and indicate how far you have moved, but on the screen, there is no blue dot, which is normal - well, maybe not normal, but I didn't make any effort to polishing the map, sorry ;(

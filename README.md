# ChargeMyCar
An android app to find, save and navigate to Electric Car Charge Points!

Written in Kotlin using databinding, lifecyle awareness, room-database, Retrofit REST API & MVVM Design Pattern in 36.5 hours.

## INTRO - 20 Hours To MVP
The inspiration for this project was to build an app and time myself from concept to completion. I am happy to say I have completed everything in 36.5 hours. 

I knew I wanted to practise using databases & retrieving information from the internet so browsed through [UK Government Datasets](https://data.gov.uk/) and settled on their Electric Car Charge Point Registry. Data that people need filtering, visualising, saving locally and could let me explore some extra features. These additional features being geo-permissions, google map integration, Regex pattern recognition & advertising. 

## 1 - PLANNING
I started with the flow of my app: 

                    Title Screen
                          |
    Live Points | Saved Points | Options | About
    
Checking through [National Charge Point Registry API](https://chargepoints.dft.gov.uk/api/help) I could see options for filtering by latitude & longitude, postcode, distance and total number of results. This seemed like plenty of options to include. 

Secondly each of the live and saved points fragments would be displayed in a list or map format. Since there would be one source of data that will supply all my fragments I decided to use a single shared ViewModel between all fragments.

## 2 - NAVIGATION & STRUCTURE ~ 5 hours
Knowing the basic structure I could create a navigation graph and implement the frame of the app and all the paths through it. Looking at the Android Material Design principles suggested a use for the navigation drawer which I obliged.

|**Nav Graph**|**Nav Drawer**|
| ---| ---|
|<img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/Navigation%20Graph.PNG" width="620">|<img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20Nav%20Drawer.png" width="260">|


In this phase I also designed the simple Title, About & Options fragments along with adding [Timber](https://github.com/JakeWharton/timber) to simplifiy my log messages in anticipation of bug fixing/feature finding.

| Title | About | Options |
| --- | --- | --- |
| <img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20Title%20Ad.png" width="260"> | <img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20About.png" width="260"> | <img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20Options.png" width="260"> |

## 3 - DATABASE & VIEWMODEL ~ 4 hours
Started by creating the Charge Point entitiy to represent the electric car charging points and all their data. Then creating the room + database to run Junit tests the ChargePointDatabaseDAO. Then designing the shared ViewModel to hold & interact with the room database and link it to a simple listview. Noting the use of non blocking Kotlin Coroutines for room database interactions.

## 4 - RECYCLERVIEW & INTERACTION ~ 8 hours
<img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20Saved%20List.png" width="360" align="left">
This section was reusable for both the live and saved lists, where a recyclerview & custom adapter would convert the data (be it from the database or JSON response) to custom list items with the charge point details. Including distance from user, status and location type. Buttons for adding dummy data and clearing the database are also implemented.

<br/><br/>
Finally adding the onClick behaviour for launching Google Maps intent and interacting with the database were added to complete a near MVP - just excluding live data.

<img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20Map%20Directions.png" width="200"> <img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20Directions%202.png" width="200">


## 5 - PERMISSIONS & REAL DATA ~ 9 hours
Phase 5 included adding permissions for internet access, copying as much of the saved points architecture over to the live points and setting up the [Retrofit](https://square.github.io/retrofit/) API service. Once I could collect the raw JSON data I could then parse it into the recyclerview on the frontend. The Retrofit API was called again from a Coroutine to not stop the UI thread. Then I designed the Options fragment to alter the API Query to vary the distance & search limit and finally used an enum to handle any errors from the API call and deal with them dynamically on the frontend.

| Raw Data | Live List | No Internet |
| --- | --- | --- | 
|<img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/Raw%20Data.PNG" width="300">| <img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20Live%20List%20Feature.png" width="300" >|<img align="center" src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20No%20Internet.png" width="300">|


## 6 - MAP FRAGMENTS ~ 10 hours
The final phase was adding the geo permissions and rippling through the users location to the viewmodel along with finishing off the functionality of the Title fragment. This entailed enabling the postcode button, adding postcode mode for API query and checking the users input to see if it was a true postcode. I used kotlins built in pattern checker with the aid of this [Hero](https://stackoverflow.com/questions/164979/regex-for-matching-uk-postcodes) on stackoverflow - improving on the regex pattern supplied by the UK government.

<img align="center" src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/postcode%20Regex.PNG" width="650">

I then finished the Options menu by adding a variety of options to alter the accuracy and power consumption ofthe GPS singal. After that was completed I could setup the maps SDK and cloud console, create an API key and start to design my map fragments. Observing the respective charge point lists from the viewmodel allowed me to populate the maps with custom markers displaying the key information about the charge points, along with direction intentions and an animation to zoom perfectly to fill the screen.

<img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20Live%20Map.png" width="260"> <img src="https://github.com/Kovah101/Kovah101/blob/main/CMC%20Screenshots/CMC%20Saved%20Map.png" width="260">

20 HOURS to MVP + 16 HOURS to full build

## EXTRAS
- Advertisement banners - Completed in 1h 25m
Research into enabling adaptive ad banner on the bottom of each fragment - [Google Admob Setup](https://developers.google.com/admob/android/quick-start)

- Review Design -
Rethink colours, UI, spacing, font, images, logos, buttons, navigation animations, map animations.

- Update UI using Jetpack Compose -
Implement design changes while learning a new framework

- Market Research -
Analyse competitiors to guage interest, usability, feature & design.

### LINKS & REFERENCES
-[National Charge Point Registry](https://chargepoints.dft.gov.uk/api/help)

-[UK Gov free datasets](https://data.gov.uk/search)

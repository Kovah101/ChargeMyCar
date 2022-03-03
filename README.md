# ChargeMyCar
An android app to find, save and navigate to Electric Car Charge Points!

Written in Kotlin using databinding, lifecyle awareness, room-database, Retrofit REST API & MVVM Design Pattern in 36.5 hours.

## INTRO - 36.5 HOURS DONE
The inspiration for this project was to build an app and time myself from concept to completion. I am happy to say I have completed everything in 36.5 hours. 

I knew I wanted to practise using databases & retrieving information from the internet so browsed through [UK Government Datasets](https://data.gov.uk/) and settled on their Electric Car Charge Point Registry. Data that people need filtering, visualising, saving locally and could let me explore some extra features. These additional features being geo-permissions, google map integration, Regex pattern recognition & advertising. 

## 1 - PLANNING
I started with the flow of my app: 

                    Title Screen
                          |
    Live Points | Saved Points | Options | About
    
Checking through [National Charge Point Registry API](https://chargepoints.dft.gov.uk/api/help) I could see options for filtering by latitude & longitude, postcode, distance and total number of results. This seemed like plenty of options to include. Secondly each of the live and saved points fragments would be displayed in a list or map format. Since there would be one source of data that will supply all my fragments I decided to use a single shared ViewModel between all fragments.

## 2 - NAVIGATION & STRUCTURE

## 3 - DATABASE & VIEWMODEL

## 4 - RECYCLERVIEW & INTERACTION

## 5 - PERMISSIONS & REAL DATA

## 6 - MAP FRAGMENTS

## EXTRAS
- Advertisement banners -
Research into enabling ad banner on the bottom of each fragment - [Google Admob Setup](https://developers.google.com/admob/android/quick-start)

### LINKS & REFERENCES

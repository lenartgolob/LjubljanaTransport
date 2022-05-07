# Introduction
​
__LjubljanaTransport API__ provides a REST API for Ljubljana public transport related actions. The available actions are getting a list of all public transport options in Ljubljana (/), getting a list of the taxis in Ljubljana (/taxis), getting the best path between A and B while renting a bicycle (BicikeLJ) or a car (Avant2Go).

[__OpenAPI documentation__](http://130.61.179.62:8080/api-specs/ui/?url=http://130.61.179.62:8080/api-specs/ljubljana-transport/openapi.json)
​
## Purpose
​
__LjubljanaTransport__ is primarily used on mobile apps, since it needs user's location to calculate the best path for him. 

__Typical use case__ scenario consists of:
​
1. Get a list of all public transport options with duration, distance and price
2. Check all the available taxis
3. Check the best path for renting a bicycle
4. Check the best path for renting a car  
​
## Functionality
​
LjubljanaTransport has 4 different endpoints: 
1. __(/)__: Returns a list of 6 possible public transportations (walking, bicycle, taxis, carsharing, buses, trains) between the starting point and destionation point. All of the possible public transportations include duration, distance and price (kcal expended for walking and bicycling).
2. __(/taxis)__: Returns a list of all taxis available in Ljubljana with their telephone number and email.
3. __(/bicycle)__: Returns a calculated path between the starting point and the closest BicikeLJ  station. Then it calculates the path between that said station and the station which is the closest to destination. And finally it calculates the path between the finish station and destination. The response is comprised of the duration, distance and kcal expended for all 3 parts of the journey and coordinates and address for both stations.
4. __(/carsharing)__: Returns a calculated path between the starting point and the closest Avant2Go  station. Then it calculates the path between that said station and the station which is the closest to destination. And finally it calculates the path between the finish station and destination. The response is comprised of the duration, distance and price for all 3 parts of the journey and coordinates and address for both stations.


## Other services implemented
​
__LjubljanaTransport integrates with__:
1. [__Google Maps Directions API__](https://developers.google.com/maps/documentation/directions/overview) for calculating distance and duration between 2 locations
2. [__Google Maps Geocode API__](https://developers.google.com/maps/documentation/geocoding/overview) for conversion between place_id and coordinates and vice versa
3. [__OpenData promet API__](https://github.com/zejn/prometapi) for locations of BicikeLJ stations
4. [__Open Charge Map API__](https://openchargemap.org/site/develop/api) for locations of Avant2Go stations
​

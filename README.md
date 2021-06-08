![Imgur](https://imgur.com/lXhXOim.jpg)
# AQI
AQI application shows the Air Quality Index of indian cities. The data is refreshed realtime. The application allows us to see realtime chart of the data at custom interval.
It took 2 and a half days to complete the app. The background used in the app is self designed in Adobe photoshop.

## App use case
- Connect to the websocket and fetch the updated list. The list will contain cities and the AQI value for those cities.
- Show the city name, AQI value and last updated time in a list
- On each update from socket, the cities count can be different, so if there is a city which came in one response but did not come in other, we still have to show it in the list and update whenever the data comes.
- Show color based representation based on the AQI value.
- Once clicked on any record, a new screen opens up with a live chart with a 5 seconds interval or more, the interval can be customized on screen.

## App Architecture
![Imgur](https://imgur.com/ujG5UjF.jpg)

The architecture used for this application is MVVM. The activities use viewModel functions to open a websocket channel via a repository.
The data received from the socket is sent to the viewmodel using Flows. I have used flows here because we have the data coming at a regular interval and we have to make sure that the data is updated only when the app is ready. Flows are known for handling back pressure, the producer emits the data when the collector is ready.
The data is saved to local DB when received. The list is then updated via DB. The DB helps to contain old data even when the new ones are received.
I have used Kotlin language along with Hilt for dependency injection, Room DB for local storage, Databinding with binding adapters and Coroutines.

## Third Party Libraries
I have used the following libraries:
- OKHttp: for socket handling and fetching data.
- MPChart: for showing live data in chart.
- SDP intuit: to support multiple screen avoiding the dimens files.
- TimeAgo: Tried this library for thr first time, it converts long time to time ago String. So if I store a `System.currentTimeMillis()` and check it after an hour using TimeAgo, it shows 1 ahour ago. Fun.

## UI Test
Tried to use espresso to simulate the click of a recyclerview

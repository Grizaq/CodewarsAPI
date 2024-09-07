# TuiCodewars
Project Overview:

This project is built with the API provided by CodeWars, for the position #2 on the leaderboard - jhoffner. It follows the MVVM architecture and adheres to the principles of Clean Architecture. The app leverages Jetpack Compose with Material3 design for a modern and responsive UI. Dependency injection is handled with Dagger/Hilt, and coroutines are used for asynchronous operations. The project includes robust loading state handling to ensure a smooth user experience during data fetching. A local Room database is used to cache data for offline access, and a network checker is implemented to avoid unnecessary network requests when the internet is unavailable.

Key Libraries:

Jetpack Compose: Modern UI toolkit for building native Android UIs.

Material3: Material design components for Compose.

Dagger/Hilt: Dependency injection framework.

Coroutines: For managing background threads and asynchronous tasks.

Retrofit & Gson: For networking and JSON parsing.

OkHttp: For HTTP requests and logging.

Kotlinx Coroutines: For coroutine support.

Room: For local database caching.

JUnit & MockK: For unit testing and mocking.

Compose Destinations: For navigation in Compose.



![](Screenshot_greeting_screen.png)

![](Screenshot_list_authored_screen.png)

![](Screenshot_challenge_details_screen.png)

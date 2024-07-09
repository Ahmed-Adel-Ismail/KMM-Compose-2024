This is a POC project to clarify how Hexagonal architecture combined with DDD can acheive more than 80% of the code to be KMM & Compose while platforms required code will be limited to edge cases and depending on there libraries for data-sources related code 


<img width="1265" alt="Screenshot 2024-07-10 at 1 58 11 AM" src="https://github.com/Ahmed-Adel-Ismail/KMM-Compose-2024/assets/4227222/36451433-c49f-4c65-ad56-0ceb8f47d47b">

<img width="1920" alt="Screenshot 2024-07-10 at 1 50 29 AM" src="https://github.com/Ahmed-Adel-Ismail/KMM-Compose-2024/assets/4227222/5b1f46c1-838b-4553-b8b5-f0ced21acd9b">


=========

This is a Kotlin Multiplatform project targeting Android, iOS, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…



# <img src=".github/assets/logo.png" width="35" style="vertical-align: middle;" /> SpendLess

SpendLess is a finance tracking app with a focus on security. The aim is to keep track of the 
users income and expenses while also securing the data in terms of accessing the app and storing data.

## Screenshots

<table style="border: none">
  <tr>
    <td><img src="screenshots/dashboard.jpeg" alt="Home" width="150"/></td>
    <td><img src="screenshots/add_transaction.jpeg" alt="Add Expense" width="150"/></td>
    <td><img src="screenshots/preferences.jpeg" alt="Reports" width="150"/></td>
    <td><img src="screenshots/pin.jpeg" alt="Pin" width="150"/></td>
    <td><img src="screenshots/report.jpeg" alt="Report" width="150"/></td>
  </tr>
  <tr>
    <td align="center">Dashboard</td>
    <td align="center">Add Expense</td>
    <td align="center">Preferences</td>
    <td align="center">Pin</td>
    <td align="center">Report</td>
  </tr>
</table>

## Features

- **Biometric Authentication**: Users can use their fingerprint or face to unlock the app.
- **Income and Expense Tracking**: Users can add their income and expenses with recurring support.
- **Reports**: Users can export reports in CSV or PDF format.
- **Widget**: Users can add a widget to their home screen to quickly add an expense.
- **Security Preferences**: Users can set up security preferences with session expiration and lockout duration.
- **Expense preferences**: Users can set up expense preferences to view their expenses in a certain way.

## Technical Stack

- **Language**: Kotlin
- **Architecture**: Clean Architecture with MVVM
- **Dependency Injection**: Hilt
- **Database**: Room
- **UI Framework**: Jetpack Compose
- **Background Processing**: WorkManager
- **Testing**: JUnit5

## Getting Started

### Prerequisites

- Android Studio
- JDK 17 or newer
- Android SDK 34 or newer

### Installation

1. Clone the repository:
```bash
   git clone git@github.com:rribeiro1/spendless-android.git
```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Run the app on your device or emulator

## Building the Project

The project uses Gradle for building. To build the project:

```bash
./gradlew build
```

To execute unit tests:

```bash
./gradlew app:testDebugUnitTest
```

To create the APK (build -> outputs -> apk -> release):

```bash
./gradlew assembleRelease
```

## License

See [LICENSE](LICENSE.md) file for details.

### Acknowledgements

Main contributors:
* [Rafael Ribeiro](https://github.com/rribeiro1)
* [Orcun](https://github.com/orcuns)

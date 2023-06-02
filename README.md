# StrandHogg detection POC

This project demonstrates the POC detection techniques described in the accompanying developer blog post [Protecting Against StrandHogg](https://www.guardsquare.com/blog/protecting-against-strandhogg).

These are the components of interest:

- `MinimizedActivity`: The entry point into the app
- `MyBootReceiver`: Starts `MinimizedActivity` on device boot and immediately moves it to the background
- `RestartMinimizedActivityService`: Starts `MinimizedActivity` again and moves it to the background if the app is closed
- `ForegroundDetectionService`: Responsible for checking for StrandHogg attacks if the user navigates away from the app
- `StrandhoggProtectionUtils`: Contains the actual StrandHogg checks

# Building the project

Navigate to the project root and run one of these commands:

- `./gradlew assemble`: Build both the debug and release variants
- `./gradlew assembleDebug`: Build the debug variant
- `./gradlew assembleRelease`: Build the release variant

Clean the project with `./gradlew clean`

# Installing the application on a device

After building the project, the resulting APK(s) can be found under `app/build/outputs/apk/<variant>/app-<variant>.apk`.
Assuming you are running an emulator or that you have turned on developer mode on a connected physical device,
you can install the APK with `adb install <path/to/project>/app/build/outputs/apk/<variant>/app-<variant>.apk`

<h1 align="center">Protecting Against StrandHogg</h4>

A set of techniques to protect against StrandHogg on Android

> [!IMPORTANT]  
> Be aware that StrandHogg is old and these techniques demonstrated in this sample application
> are not necessary in most cases: see the flow chart to see if you need to take action in the
>  [accompanying blog post](https://www.guardsquare.com/blog/protecting-against-strandhogg).

## What is StrandHogg?

<img src="https://www.guardsquare.com/hs-fs/hubfs/Protecting-Against-StrandHogg-1.png?width=200&name=Protecting-Against-StrandHogg-1.png" width="300" align="right"  alt="Protecting Against Strandhogg"/>

StrandHogg exploits an oversight in Android’s task management that allows a malicious application to insert a
malicious activity at the top of the task stack for a targeted application. 
In other words, it lets applications impersonate other applications and e.g. steal sensitive information.

Two variants of StrandHogg have been identified. The first variant (v1) involves setting the `android:taskAffinity`
attribute to the task affinity of the targeted application. This is easy to detect, which means that 
Google Play will reject such applications. The second variant (v2) uses somewhat more complex code 
to inject the malicious activity, and so is more difficult to detect automatically.

Further details can be found in the [accompanying blog post](https://www.guardsquare.com/blog/protecting-against-strandhogg).

<br clear=right />

## ✨ Building the application

<img align="right" src=screenshot.png width="279" />

The application can be built from the command line or within Android Studio:

```shell
$ ./gradlew assembleRelease
```

## ❓ How to use the application?

The application demonstrates techniques to protect protect against Strandhogg that
you can apply to your own application. To get the most out of the example application, the code is best read
together with the [companion blog post](https://www.guardsquare.com/blog/protecting-against-strandhogg).

These are the components of interest in the application source code:

- [MinimizedActivity](app/src/main/java/com/example/simpleapp/MinimizedActivity.java): The entry point into the app
- [MyBootReceiver](app/src/main/java/com/example/simpleapp/MyBootReceiver.java): Starts `MinimizedActivity` on device boot and immediately moves it to the background
- [RestartMinimizedActivityService](app/src/main/java/com/example/simpleapp/RestartMinimizedActivityService.java): Starts `MinimizedActivity` again and moves it to the background if the app is closed
- [ForegroundDetectionService](app/src/main/java/com/example/simpleapp/ForegroundDetectionService.java): Responsible for checking for StrandHogg attacks if the user navigates away from the app
- [StrandhoggProtectionUtils](app/src/main/java/com/example/simpleapp/StrandhoggProtectionUtils.java): Contains the actual StrandHogg checks

<br clear=right />

## 🤝 Contributing

Contributions, issues and feature requests are welcome.
Feel free to check the [issues](https://github.com/Guardsquare/strandhogg-detection/issues) page if you would like to contribute.

## 📝 License

Copyright (c) 2002-2023 [Guardsquare NV](https://www.guardsquare.com/).
This project is released under the [Apache 2 license](LICENSE).

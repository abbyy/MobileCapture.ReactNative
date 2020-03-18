# ABBYY Mobile Capture React Native Module

ABBYY Mobile Capture React Native Module allows to use the Image Capture feature of ABBYY Mobile Capture in apps based on the [React Native](https://reactnative.dev/) framework.

This plugin requires the ABBYY Mobile Capture native libraries which are available for Android and iOS. You can request ABBYY Mobile Capture trial version on the [ABBYY website](http://www.abbyy.com/mobile-capture-sdk/#request-demo).

The native libraries support the following systems:

* Android version: 5.0 or later for ARMv7 (armeabi-v7a), ARMv8 (arm64-v8a), x86 and x86_64 processors
* iOS: versions 11.x and later


## Getting started

*Note* If you are new to React Native, set up development environment first. You will need `yarn`, `npx` and `cocoapods` (for iOS) utilities. See [React Native documentation](https://reactnative.dev/docs/getting-started) for details.

1. Choose one of the following ways to add the module to your project:
   - From npm:

    ```
    yarn add react-native-abbyy-mobile-capture
    ```
   - From sources:
    ```
    yarn add file:"Path to react-native-abbyy-mobile-capture sources"
    ```
    
2. Request ABBYY Mobile Capture trial version on the [ABBYY website](http://www.abbyy.com/mobile-capture-sdk/#request-demo) and add it to your project:
    * Copy iOS `assets` directory to the project into `./assets` folder.
    * Copy your license into `./assets` with `MobileCapture.License` name
    * Copy Android `libs` directory to the project into `./android/libs` folder
    * Copy iOS `libs` directory to the project into `./ios/libs` folder
    
3. Proceed setup depending on the platform:
    * Android:
        * Add `gradle` file to the project. Usually it is located at `./android/build.gradle`:
            ```
            allprojects {
                repositories {
                    flatDir {
                    dirs "$rootDir/libs"
                    }
                }
            }
            subprojects {
                afterEvaluate {
                    dependencies {
                        // Change abbyy-mi-sdk-2.0 to abbyy-rtr-sdk-1.0 if you need to use
                        recognition API (MI+Extended or MI+ExtendedDC)
                        implementation(name: 'abbyy-mi-sdk-2.0', ext: 'aar')
                        implementation(name: 'abbyy-ui-1.0', ext: 'aar')
                    }
                }
            }
            ```
        * Add to application `gradle` file. Usually it is located at `./android/app/build.gradle`:
            ```
            android {
                defaultConfig {
                    ndk {
                        // Filter out mips
                        abiFilters 'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'
                    }
                }
                sourceSets {
                    main {
                        assets.srcDirs += [ "$rootDir/../assets" ]
                    }
                }
                packagingOptions {
                    pickFirst 'lib/x86/libc++_shared.so'
                    pickFirst 'lib/armeabi-v7a/libc++_shared.so'
                    pickFirst 'lib/arm64-v8a/libc++_shared.so'
                    pickFirst 'lib/x86_64/libc++_shared.so'
                }
            }
            ```
        * Set minimum SDK version to '21'.
        * Add `adb` utility to PATH environment variable. `adb` is located in `Android sdk directory/platform-tools directory`.
    * iOS:
        * Run `pod install` from iOS directory.
        * Select your project in the `Target` group and open the `Build Settings` tab. In the `Search Paths` section add to the `Framework Search Paths` the following path:
        ```
        ${PROJECT_DIR}/libs
        ```
        * On the `Build Options` tab set `Enable Bitcode` option to No.
        * Open the `Build Phases` tab. In the `Link Binary With Libraries` section, click "+" and add the `AbbyyRtrSDK.framework` and `AbbyyUI.framework`.
        * In `Build Phases`, add a new `Run Script phase`. Run the `copy_frameworks.sh` script that removes the frameworks for the non-active CPU architectures (the complete list depends on the project settings), and sign the resulting framework. This is a required step before uploading your application to App Store.
        ```
        /bin/sh "${SRCROOT}/libs/copy_frameworks.sh"
        ```
        * To add the resource files and set up the copying rules, in `Build Phases` add one more `Run Script` phase. Run the `copy_assets.py` script to automatically copy all resource files to corresponding destinations and add necessary dictionaries.
        ```
        python "${SRCROOT}/../assets/copy_assets.py"  
        ```
        * In `Build Phases`, go to the `Copy Bundle Resources` section and add the `MobileCapture.License` file.
        * In the `info.plist` file add permission for working with camera and gallery if necessary.
        

## ABBYY Mobile Capture React Native Module Example

### How to run samples

1. Request ABBYY Mobile Capture trial version on the [ABBYY website](http://www.abbyy.com/mobile-capture-sdk/#request-demo) and add it to your project:
    * Copy iOS `assets` directory to the project into `./assets` folder.
    * Copy your license into `./assets` with `MobileCapture.License` name
    * Copy Android `libs` directory to the project into `./android/libs` folder
    * Copy iOS `libs` directory to the project into `./ios/libs` folder

2. Run `yarn install` from the sample root

3. Run `pod install` from the `sample/ios` directory

4. To run application do the following:
    * Android:
        * From console:
            * Execute `npx react-native run-android` from the sample root
        * From Android Studio:
            * Open `sample/android` project
            * Click `Run app` button
    * iOS:
        * From console:
            * Execute `npx react-native run-ios` from the sample root
            It runs sample in the simulator
        * From XCode
            * Open `sample/ios` project
            * Click `Run` button


## Documentation

See full documentation on the [ABBYY Documentation website](https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-introduction).

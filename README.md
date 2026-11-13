# canton_info_app

A kiosk-style information display for an Android TV panel (Tecmashin LED43, Android 11).
It is a React 19 + TypeScript app built with Vite and packaged as a native Android APK with Capacitor 8.

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| Node.js | 22+ | Required by the Capacitor 8 CLI. npm is the package manager. |
| JDK | 21 | Required by Capacitor 8 / Android Gradle Plugin. Set `JAVA_HOME` if several JDKs are installed. |
| Android SDK | platform 36 + build-tools | `compileSdk`/`targetSdk` = 36, `minSdk` = 24 (see `android/variables.gradle`). The easiest way to get it is to install Android Studio. |
| adb | any | Only needed to install the APK on a device (part of Android SDK platform-tools). |

Gradle doesn't need to be installed. The project uses the wrapper (`android/gradlew`, Gradle 8.14.3).

### Point the build at the Android SDK

Use either of these:

```sh
# option A: environment variable (macOS default Android Studio location shown)
export ANDROID_HOME="$HOME/Library/Android/sdk"

# option B: local, git-ignored file
echo "sdk.dir=$HOME/Library/Android/sdk" > android/local.properties
```

Without one of them, Gradle fails with `SDK location not found`.

## Build the APK

```sh
npm ci                 # install dependencies (first time / after package.json changes)
npm run android:apk    # build web app, sync into android/, run ./gradlew assembleDebug
```

Output: `android/app/build/outputs/apk/debug/app-debug.apk`

`npm run android:apk` runs these steps:

1. `npm run build` runs `tsc -b` and `vite build`, writing the web bundle to `dist/`.
2. `cap sync android` copies `dist/` into `android/app/src/main/assets/public` and updates plugins.
3. `cd android && ./gradlew assembleDebug` builds the debug APK. It is signed with the debug key and can be sideloaded.

After changing web code (`src/`), always rebuild with `npm run android:apk` (or at least `npm run android:sync`). Otherwise the APK still contains the old bundle.

### Release build (optional)

`./gradlew assembleRelease` (run inside `android/`) produces an **unsigned** APK, because no signing config is set up.
The app is sideloaded, so the debug APK is usually enough. If you need a release build, add a `signingConfigs` block in `android/app/build.gradle` or sign the APK with `apksigner`.

## Install on the device

```sh
adb connect <device-ip>:5555        # LED43 over the network (or use USB)
adb devices                         # verify it is listed
adb install -r android/app/build/outputs/apk/debug/app-debug.apk
```

`npm run android:run` builds the app and deploys it directly to a connected device.

### One-time device setup (auto-start on boot)

`BootReceiver` starts the app on boot. Android 10+ blocks background activity starts, so allow it once:

```sh
adb shell appops set com.example.cantoninfo SYSTEM_ALERT_WINDOW allow
```

Then open the app manually once. Android doesn't send boot broadcasts to apps that have never been launched.

## Development

- `npm run dev` starts the Vite dev server for UI work in a desktop browser.
- `npm run lint` runs oxlint.
- To debug the WebView on the device, open `chrome://inspect` in desktop Chrome (works for debug builds).

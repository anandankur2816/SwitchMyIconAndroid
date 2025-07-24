# Android Dynamic App Icon Switcher

This Android project demonstrates how to dynamically switch the app's launcher icon at runtime using `activity-alias`. The main use case is to allow users to choose from different themed icons directly from the app, without reinstalling or restarting the app.

## 🚀 Features

- Multiple app icons defined using `activity-alias`
- One-click switch between icons
- Works across most Android launchers (subject to launcher behavior)
- Persistent icon state using SharedPreferences

## 🧩 How It Works

1. Define the main `MainActivity` **without** the launcher intent.
2. Create multiple `<activity-alias>` entries in the `AndroidManifest.xml`, each representing a different icon.
3. Enable one alias at a time via `PackageManager.setComponentEnabledSetting()`.
4. Store the selected alias in `SharedPreferences` to persist it across app launches.

## 📱 Demo

Check out the video demo below to see it in action:

▶️ [Watch Demo on Google Drive](https://drive.google.com/file/d/1seHxmBNfGn8_-6LXyGGTCVfH4WhkkqMu/view?usp=sharing)

## 🛠️ Tech Stack

- Java (Android)
- Android Manifest with `activity-alias`
- SharedPreferences
- View binding & event listeners

## 📝 Setup Instructions

1. Clone the repo
2. Open in Android Studio
3. Update your icon aliases in `AndroidManifest.xml` with desired icon resources
4. Build & run on a real device (preferred over emulator)

## 💡 Notes

- **Only one `activity-alias` should be enabled at a time**
- The actual `MainActivity` must **not** contain the `LAUNCHER` intent filter
- Some launchers cache the app icon — home screen icon might need to be removed and re-added to reflect changes
- Behavior may vary slightly across Android devices and launchers

## 📂 File Structure Overview

app/
├── java/com/example/webview/
│ ├── MainActivity.java
│ └── Icon Switching Logic
├── res/mipmap-*/ # Contains different icon assets
└── AndroidManifest.xml # Defines MainActivity and icon aliases
## 🙌 Acknowledgements

Inspired by Android’s flexible launcher alias mechanism.

## 📄 License

MIT License – feel free to use, modify, and share!

---

Made with ❤️ by anandankur2816


# Torque (v1) — legacy Android app

> **This is the first version of Torque. The maintained project is [Torque.v2](https://github.com/VicProgram/Torque.v2)**, which adds a FastAPI backend, a desktop client, multi-vehicle support (motorcycles and cars) and a much richer maintenance model.

Offline-first Android app to keep a motorcycle garage under control: bikes, maintenance records, photos and a health score computed locally with Room.

## What it does

- Garage list with bike details and photos.
- Maintenance history per bike: oil, filters, chain, brakes, tyres…
- Health score and upcoming-maintenance reminders based on km intervals.
- Fully offline: all data lives in a local Room database.
- Settings and rider profile.

## Stack

- Kotlin, Jetpack Compose, Material 3
- Room (entities, DAOs, repository)
- Gradle 8, Android SDK 35

## Build

```bash
./gradlew assembleDebug
# APK → app/build/outputs/apk/debug/
```

## Why v2 exists

v1 is motorcycle-only and has no sync. [Torque.v2](https://github.com/VicProgram/Torque.v2) keeps the same idea but adds a REST API, desktop workshop management, car support, appointments, invoices, inventory and CI. New work happens there.

## License

[MIT](LICENSE) © 2026 Victor Abad Romero

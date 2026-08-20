# RepSync

RepSync is a modern Android fitness tracking app built with **Kotlin** and **Jetpack Compose**. It tracks workout activity in real time and allows users to monitor their steps, calories burned, distance, and workout duration.

## Features

- Real-time step tracking using the device's step counter sensor
- Calorie and distance calculations based on steps
- Start, pause, resume, reset, and finish workout controls
- Configurable daily calorie goal
- Visual calorie-goal progress
- Goal-reached notification
- Workout history with date, time, duration, steps, calories, distance, and calorie goal
- Ability to clear saved workout history
- Persistent workout history using **Room Database**
- Dependency injection using **Hilt**
- Reactive UI using **Kotlin Coroutines and Flows**
- Screen navigation using **Navigation Compose**
- Material 3 UI with light and dark theme support

## Architecture

RepSync follows an MVVM-style architecture with a repository layer:

```text
UI (Jetpack Compose)
        ↓
   ViewModel
        ↓
   Repository
        ↓
 ┌──────┴────────┐
 │               │
Step Sensor    Room
              Database

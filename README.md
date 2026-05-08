# Smart Laundromat Concept

An Android application that enables users to book laundry machines, track live machine states, manage a queue, and view account details — built as a school project at SUTD.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Key Design Patterns](#key-design-patterns)
- [Setup & Running](#setup--running)
- [Backend (Spring Boot Server)](#backend-spring-boot-server)
- [Database (Supabase)](#database-supabase)
- [Known Design Decisions](#known-design-decisions)
- [References & Academic Integrity](#references--academic-integrity)

---

## Overview

Smart Laundromat Concept simulates a smart laundromat management system where users can:

- View the real-time status of washers and dryers at their selected location
- Join or leave the machine queue, which is managed by a Spring Boot backend
- Start a machine or collect laundry via the interact endpoint
- Track active bookings with a live countdown timer on the home screen
- Monitor wallet balance and reputation tier, both of which are synced with Supabase

The app polls Supabase every second so machine states are always up to date without requiring a manual refresh.

---

## Features

| Feature | Description |
|---|---|
| **Machine Booking** | View all 4 washers and 4 dryers per location with live status badges |
| **Queue System** | Join or leave the machine queue; the backend assigns available machines automatically |
| **Start / Collect** | Interact with an assigned machine to start a cycle or collect laundry |
| **Live Countdown** | Home screen shows a countdown timer for active bookings, fed by the polling loop |
| **Wallet** | View and top-up wallet balance; payments are deducted when a cycle starts |
| **Reputation & Tiers** | Score adjusts based on behaviour (collecting on time, etc.); displayed as Tier 1–4 |
| **Notifications** | View a history of booking, cycle, queue, and system notifications |
| **Location Selection** | Pick from multiple laundromat locations on a Google Map |
| **Authentication** | Login and sign-up with username/password, stored in Supabase |

---

## Architecture

The application follows a **layered architecture** with clear separation of concerns:

```
Android App (Java)
    │
    ├── ui/              # Activities, helpers, navigation
    ├── data/
    │   ├── model/       # POJOs: AppMachine, User, Notification, etc.
    │   ├── remote/      # Retrofit API clients (Supabase + Spring Boot)
    │   ├── repository/  # Data access layer
    │   └── session/     # Singleton session state (UserSession, LocationSession)
    └── ...

Spring Boot Server (Java)
    │
    ├── controller/      # REST endpoints (/queue, /leave, /interact)
    ├── facility/        # Facility & queue orchestration
    ├── queue/           # MachineQueue with PriorityQueue of Users
    ├── model/           # Machine (abstract), Washer, Dryer, User
    └── db/              # Query and Update helpers (JdbcTemplate → Supabase)

Supabase (PostgreSQL)
    └── Tables: users, machine, notifications
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Android client | Java (Android SDK) |
| UI layouts | ConstraintLayout, custom `LiquidGlassView` |
| HTTP client | Retrofit 2 + Gson |
| Backend middleware | Spring Boot 4 (Java 17) |
| Database | Supabase (PostgreSQL) |
| Maps | Google Maps SDK for Android |
| Build tools | Gradle (Android), Maven Wrapper (Spring Boot) |

---

## Project Structure

```
smart_laundromat_concept/
├── app/
│   └── src/main/java/com/example/smart_laundromat_concept/
│       ├── data/
│       │   ├── model/
│       │   │   ├── AppMachine.java       # Unified machine model + static in-memory store
│       │   │   ├── User.java
│       │   │   ├── Notification.java
│       │   │   ├── QueueResponse.java
│       │   │   ├── Reputation.java
│       │   │   └── Wallet.java
│       │   ├── remote/
│       │   │   ├── supabase/
│       │   │   │   ├── SupabaseClient.java   # Retrofit API interface for Supabase REST
│       │   │   │   ├── SupabaseRealtime.java # WebSocket listener (experimental)
│       │   │   │   └── SupabaseError.java
│       │   │   ├── server/
│       │   │   │   ├── BackendClient.java    # Retrofit API interface for Spring Boot
│       │   │   │   ├── QueueRepository.java
│       │   │   │   └── QueueCallback.java
│       │   │   └── repository/
│       │   │       ├── MachineRepository.java
│       │   │       ├── UserRepository.java
│       │   │       ├── AuthRepository.java
│       │   │       └── NotificationRepository.java
│       │   └── session/
│       │       ├── UserSession.java      # Singleton: logged-in user + active booking
│       │       └── LocationSession.java  # Singleton: selected laundromat location
│       └── ui/
│           ├── activities/
│           │   ├── auth/                 # LogInActivity, SignUpActivity, AuthUIHelper
│           │   ├── main/
│           │   │   ├── home/             # HomeActivity, HomeCardHelper, ReputationHelper
│           │   │   ├── booking/          # BookingActivity, WasherManager, DryerManager,
│           │   │   │                     # MachineManager, MachineStateHelper
│           │   │   └── utils/            # PollingManager
│           │   ├── notification/         # NotificationActivity, NotificationAdapter
│           │   ├── location/             # LocationActivity, LocationHelper
│           │   └── profile/              # ProfileActivity
│           ├── common/
│           │   ├── MenuBarHelper.java    # Highlights active bottom nav item
│           │   └── ButtonHelper.java     # Wires reusable layout_button includes
│           └── navigation/
│               ├── NavigationHelper.java # Central navigation hub (module chain)
│               ├── NavigationRequest.java
│               ├── NavigatorModule.java  # Interface for all navigator modules
│               ├── AuthNavigator.java
│               ├── MenuNavigator.java
│               ├── BookingNavigator.java
│               ├── SystemNavigator.java
│               └── HomeNavigator.java
└── server/
    └── src/main/java/com/laundromat/server/
        ├── ServerApplication.java
        ├── config/FacilityConfig.java
        ├── controller/FacilityController.java
        ├── facility/Facility.java
        ├── queue/MachineQueue.java
        ├── model/
        │   ├── Machine.java      # Abstract; State enum uses State pattern
        │   ├── Washer.java
        │   ├── Dryer.java
        │   └── User.java
        └── db/
            ├── Query.java
            └── Update.java
```

---

## Key Design Patterns

### Template Method — `MachineManager`
`WasherManager` and `DryerManager` share all state-tracking and UI-update logic via the abstract `MachineManager` base class. Each subclass only supplies its own view ID array via `getMachineIds()`.

### Strategy / Polymorphism — Navigation Modules
`NavigationHelper` holds a list of `NavigatorModule` implementations (`AuthNavigator`, `MenuNavigator`, `BookingNavigator`, `SystemNavigator`, `HomeNavigator`). On any button tap, each module is asked in order whether it handles the view ID. New navigation destinations can be added without modifying `NavigationHelper`.

### State Pattern — `Machine.State` (Server)
Each `Machine.State` enum value (`AVAILABLE`, `RESERVED`, `IN_USE`, `COLLECTION`, `OOS`) implements `timeOut()` and `interact()`, defining its own transition logic. This avoids large switch statements in the machine class itself.

### Singleton — `UserSession` / `LocationSession`
Both session classes use the classic singleton pattern so any Activity can access the current user and selected location without passing context around.

### Polling as Source of Truth
Rather than maintaining local boolean flags for queue or machine state, `PollingManager` fires a Supabase fetch every second. State is read directly from the in-memory `AppMachine` maps (`AppMachine.getWashers()` / `AppMachine.getDryers()`), which are updated on every poll cycle.

---

## Setup & Running

### Android App

1. Clone the repository.
2. Open the `app/` directory in Android Studio.
3. Add your Supabase API key to `local.properties` or `BuildConfig`:
   ```
   Supabase_Key=your_supabase_anon_key
   ```
4. Ensure the `BASE_URL` in `BackendClient.java` points to your running Spring Boot instance (or Railway deployment).
5. Build and run on an emulator or physical device (API 26+).

### Spring Boot Server

```bash
cd server
./mvnw spring-boot:run
```

The server starts on port `8080` by default (configurable via the `PORT` environment variable).

Configure your Supabase credentials in `server/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://<supabase-pooler-url>/postgres?prepareThreshold=0
spring.datasource.username=your_username
spring.datasource.password=your_password
```

The server is also deployed on Railway at:
```
https://laundromat-server-production.up.railway.app/
```

### REST Endpoints (Spring Boot)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/queue/washer?userId=<id>` | Join the washer queue |
| `POST` | `/queue/dryer?userId=<id>` | Join the dryer queue |
| `POST` | `/leave/washer?userId=<id>` | Leave the washer queue |
| `POST` | `/leave/dryer?userId=<id>` | Leave the dryer queue |
| `POST` | `/interact?userId=<id>` | Start machine or collect laundry |

---

## Database (Supabase)

The app reads and writes to three Supabase tables:

### `users`
| Column | Type | Notes |
|---|---|---|
| `id` | integer | Primary key |
| `username` | text | Unique |
| `password` | text | Plain text (demo only) |
| `wallet` | float | Current balance |
| `reputation` | integer | Score used for tier calculation |

### `machine`
| Column | Type | Notes |
|---|---|---|
| `id` | integer | Global primary key |
| `position` | integer | 1–4 per store — used for UI mapping |
| `store` | integer | Store number |
| `type` | text | `"washer"` or `"dryer"` |
| `status` | text | `AVAILABLE`, `RESERVED`, `IN_USE`, `COLLECTION`, `OOS` |
| `current_user` | integer | FK to `users.id`, nullable |
| `timeout_time` | timestamptz | ISO 8601; when the current state expires |

> **Important:** UI mapping always uses `position` (1–4), never the global `id`.

### `notifications`
| Column | Type | Notes |
|---|---|---|
| `id` | integer | Primary key |
| `user_id` | integer | FK to `users.id` |
| `message` | text | Notification body |
| `type` | text | `booking`, `cycle_done`, `queue`, `system` |
| `created_at` | timestamptz | Auto-set by Supabase |
| `is_read` | boolean | Read state |

---

## Known Design Decisions

- **`position` vs `id`:** Machine UI slots are indexed by `position` (1–4 within a store), not the global database `id`. All `AppMachine` static map operations use `position`.
- **ISO 8601 parsing:** `timeout_time` is parsed with `"yyyy-MM-dd'T'HH:mm:ss.SSSXXX"` and falls back to `"yyyy-MM-dd'T'HH:mm:ssXXX"` when milliseconds are absent.
- **Material Button tint:** `setBackgroundTintList(null)` must be called before applying custom drawables to Material buttons; otherwise Material silently overrides the colour.
- **Polling vs Realtime:** `PollingManager` (1-second interval) is the primary sync mechanism. `SupabaseRealtime` (WebSocket) exists as an experimental alternative but is not the active path.
- **No local queue flags:** `isInWasherQueue` / `isInDryerQueue` booleans were removed. Queue membership is derived entirely from whether the user appears as `current_user` in any machine row.

---

## References & Academic Integrity

All external resources, video tutorials, and AI tool usage (Gemini, ChatGPT, Claude) are documented in:

```
app/src/main/java/com/example/smart_laundromat_concept/ui/reference_jh.md
```

All AI-generated outputs were reviewed and edited by the team before inclusion. This repository is private and intended solely for course submission.
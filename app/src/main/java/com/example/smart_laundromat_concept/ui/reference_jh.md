## Referencing and Academic Integrity

You may use any referencing/citation style to acknowledge external resources such as:
- Websites
- AI tools
- GitHub repositories
- Video tutorials

References may also be included directly in code comments.

If AI-generated code is modified, include a remark:
“This part was written by [AI tool] and edited by Jiahao.”

---

## Code Ownership Policy

- Do NOT share your code with other groups
- Do NOT copy code from outside your group
- All submitted work must reflect your own understanding

---

## Repository Policy

- Repository must remain **private**
- Only team members have access
- Public sharing only after course completion (if allowed)

---

## Use of External Code

- Small snippets are allowed
- Large-scale copying is NOT allowed
- Project must remain primarily your own work

---

## Learning Resources

### Video Tutorials
- Android App Development in Java  
  https://youtu.be/tZvjSl9dswg
- UI/UX Design Course  
  https://youtu.be/wIuVvCuiJhU
- Login Page Tutorial  
  https://youtu.be/orXJRPNvAc4
- Android Package Structure  
  https://youtu.be/ek682t-z2gQ

### Online Resources
- Android Login Page  
  https://androidknowledge.com/login-page-in-android-studio/
- iOS UI Kits / Figma  
  https://www.figma.com/community/file/1515442422580898276  
  https://www.figma.com/community/file/1459470667046558004  
- Laundry App UI Kit  
  https://www.figma.com/community/file/1338949631631207121

---

## AI Assistance and Refactoring

### Gemini AI

- **Architecture Refactoring**
  - Issue: Activities contained mixed UI + logic
  - Solution: Introduced layered structure (`ui`, `data`, `utils`)
  - Outcome: Improved separation of concerns and maintainability

- **UI Optimization**
  - Issue: Performance inefficiencies in custom views
  - Solution: Refactored drawing logic and pre-allocated objects
  - Outcome: Improved rendering performance

- **Machine State System**
  - Issue: Repeated UI logic for washers/dryers
  - Solution: Introduced `MachineStateHelper` and manager classes
  - Outcome: Centralized and reusable UI state handling

- **Documentation System**
  - Issue: Difficult navigation across large codebase
  - Solution: Added structured Javadoc and navigation hints
  - Outcome: Improved code readability and maintainability

- **API Synchronization Fix**
  - Issue: Supabase PATCH request failures (`eq.` filter missing)
  - Solution: Corrected query filters and request format
  - Outcome: Reliable backend updates without overwriting data

 (All outputs were reviewed and edited by Jiahao)


---

### ChatGPT

- **Code Refactoring & Structure**
  - Issue: Activities and helper classes contained mixed responsibilities
  - Solution: Suggested separation into smaller methods and helper classes
  - Outcome: Improved readability, maintainability, and modular design

- **Android UI & Layout Guidance**
  - Issue: UI layout inconsistencies and difficulty structuring XML
  - Solution: Provided guidance on layout structures (ConstraintLayout, LinearLayout) and UI components
  - Outcome: Cleaner and more consistent UI design

- **Debugging & Error Resolution**
  - Issue: Build failures, runtime crashes, and incorrect UI updates
  - Solution: Identified root causes and provided step-by-step fixes
  - Outcome: Stable application behavior and successful builds

- **Documentation & Javadoc Support**
  - Issue: Codebase lacked clear explanations and documentation
  - Solution: Assisted in writing structured Javadoc and inline comments
  - Outcome: Improved code clarity and easier understanding for reviewers

- **Navigation System Design**
  - Issue: Repetitive navigation logic and duplicate activity launches
  - Solution: Designed modular navigation system (`NavigationHelper`, `NavigationRequest`)
  - Outcome: Centralized, reusable, and safer navigation flow

- **Animation Handling**
  - Issue: Duplicate or inconsistent transition animations
  - Solution: Centralized animation logic and simplified implementation
  - Outcome: Consistent and predictable UI transitions

- **Clean Code Practices**
  - Issue: Complex or hard-to-read code structures
  - Solution: Recommended simplification (clear flow, helper methods, reduced nesting)
  - Outcome: Code became easier to read, explain, and maintain

- **Frontend–Backend Integration**
  - Issue: Unclear data flow between Android app and backend (Supabase / Spring Boot)
  - Solution: Provided guidance on API usage, data handling, and architecture flow
  - Outcome: Better understanding of system integration and data consistency

- **Supabase Data Mapping & Debugging**
  - Issue: `current_user` from Supabase was not reflected correctly in the app (appearing as null)
  - Solution: Guided mapping using `@SerializedName("current_user")`, ensured correct data type (Integer), and added logging for verification
  - Outcome: Reliable retrieval of `current_user` from Supabase into the Android app

- **Machine Highlight Logic Fix**
  - Issue: Assigned machine was not consistently highlighted due to state overwrite and polling resets
  - Solution: Refactored logic to first collect matching machine (`myWasher`, `myDryer`) and apply highlight after loop completion
  - Outcome: Stable and consistent highlighting of the user’s assigned machine

- **ID vs Position Bug Resolution**
  - Issue: Machines were indexed using database `id` instead of UI `position`, causing mismatched updates
  - Solution: Identified mismatch and corrected repository logic to use `position` for UI mapping
  - Outcome: Correct machine slots updated and highlighted in UI

- **Polling & State Consistency Fix**
  - Issue: Frequent polling caused valid `current_user` values to be overwritten by null responses
  - Solution: Recommended preserving existing user values when new data is null and restructuring update flow
  - Outcome: Reduced flickering and maintained consistent UI state across refresh cycles

- **Type Handling (String vs Integer) Correction**
  - Issue: Comparison between `String` user ID and `Integer current_user` caused matching issues
  - Solution: Standardized handling and conversion between types for accurate comparison
  - Outcome: Reliable user-to-machine matching logic

- **Navigation Execution Bug Fix (Internal Animation Not Triggering)**
  - Issue: Washer/Dryer tab sliding animation stopped working after refactoring click listeners; navigation logic was executed via `BookingNavigator.handle()` directly, bypassing animation execution
  - Solution: Identified that `handle()` only returns a `NavigationRequest` but does not execute it; refactored button click handling to use `NavigationHelper.launchPage()` to restore the full navigation pipeline (handle → executeNavigation → executeInternalTransition)
  - Outcome: Internal slide animations function correctly again with proper separation between navigation decision and execution logic


(All outputs were reviewed and edited by Jiahao)

---

### Claude AI

- **Code Review & Refactoring**
  - Issue: Inconsistent code structure and formatting
  - Solution: Standardized Javadoc, sections, and naming
  - Outcome: Cleaner, more consistent codebase

- **Bug Fixes**
  - Issue: Crashes due to missing return, navigation issues
  - Solution: Fixed logic errors and back stack handling
  - Outcome: Stable and predictable app behavior

- **Session Management**
  - Issue: Limited user data handling
  - Solution: Refactored to store full User object
  - Outcome: Easier access to user-related data

- **UI Enhancements**
  - Issue: Static and less responsive UI
  - Solution: Added loading states and dynamic updates
  - Outcome: Improved user experience

- **Spring Boot Queue Integration**
  - Issue: Android app could not send queue requests to the Spring Boot server
  - Solution: Identified HTTP method mismatch (`@GET` vs `@POST`) and wrong URL paths in `BackendClient`; corrected to match `FacilityController` endpoints
  - Outcome: Queue join/cancel requests successfully reach the server

- **Machine Highlight from Supabase `current_user`**
  - Issue: App had no way to identify which machine was assigned to the logged-in user
  - Solution: Refactored `AppMachine` static maps to store full objects instead of just `State`; added `position` column mapping; `MachineRepository` now passes `current_user` and `position` on each poll; `fetchMachineStates()` compares `current_user` against session ID to find and highlight the assigned machine
  - Outcome: Assigned machine is automatically highlighted and un-highlighted based on live Supabase data

- **Machine Indexing Bug Fix**
  - Issue: Machines were stored at wrong map positions because `m.id` (global DB key) was used instead of `m.position` (1–4 per store)
  - Solution: Added `@SerializedName("position")` to `AppMachine` and switched `MachineRepository` to index by `m.position`
  - Outcome: Correct machine slots updated and highlighted

- **Local Queue State Removal**
  - Issue: `isInWasherQueue` / `isInDryerQueue` flags were redundant since Supabase `current_user` is the source of truth
  - Solution: Replaced local flags with `isAssignedToMachine()` derived directly from the in-memory machine map; queue button label and action now update automatically every 2 seconds from the polling loop
  - Outcome: Simpler, stateless queue handling with no risk of UI/data desync

- **Outline Clear on Unassign**
  - Issue: Machine outline remained highlighted after user left the queue or cycle ended
  - Solution: Added `else` branch in `fetchMachineStates()` to clear all outlines when no assigned machine is found for that type
  - Outcome: Outline is automatically removed on the next poll cycle after `current_user` is cleared in Supabase

- **WasherManager/DryerManager currentUser Overwrite Fix**
  - Issue: `setState()` was calling `AppMachine.setWasherState(..., null)`, wiping `currentUser` on every UI update
  - Solution: Removed the `AppMachine` static map call from `setState()`; `currentUser` is now only written by `MachineRepository`
  - Outcome: `currentUser` is preserved correctly between poll cycles

(All outputs were reviewed and edited by Jiahao)

Use use any referencing/citation style that you are comfortable with to reference the external
resources that you used e.g. website, AI tool, repository, video tutorial etc.

You can also make such references in comment statements to acknowledge the source of any Java code in your codebase.

If you use AI tools to write any part of the project and you made edits to it, simply add a
remark saying “This part was written by [AI tool] and edited by Jiahao”.

You may not share your code with any other group, nor may you copy any code from a student outside
 your group. This project is meant to be a project within your group that allows you to apply your
 programming skills. Thus, the code that your instructors see must be a reflection of your own skills.

Because you may not share your code with any other group, code uploaded to an online repository
must remain private only to your team until the end of the course.

Thus, the code should be largely your own work. We have mentioned above that using small bits of
code found on the internet is ok, but your project must not be based upon copying large amounts of
code from websites.

### Video Tutorials and Online Resources


**Video Tutorials:**

- **Android App Development in Java All-in-One Tutorial Series**  
  https://youtu.be/tZvjSl9dswg?si=INbh7l5dj1nuUl10

- **UI/UX Design start up course**  
  https://youtu.be/wIuVvCuiJhU?si=eisbhCdbNYtk5JO7

- **User login page tutorial**  
  https://youtu.be/orXJRPNvAc4?si=MyFEb3Gg8m7WJ94V

- **The Ultimate Package Structure Guide for Android Developers**     
  https://youtu.be/ek682t-z2gQ?si=YFxmweKi3dDAdwvq


**Online Resources:**

- **User login page**        
    https://androidknowledge.com/login-page-in-android-studio/

- **Apple IOS 26 Liquid Glass**     
    https://www.figma.com/community/file/1515442422580898276/apple-ios-26-liquid-glass

- **IOS UI kit + Icons pack**     
  https://www.figma.com/community/file/1459470667046558004

- **iOS and iPadOS 26**       
  https://www.figma.com/community/file/151544242258089827

- **Laundry Management Mobile App UI KIt**        
  https://www.figma.com/community/file/1338949631631207121

### AI Assistance and Refactoring

**Gemini AI:**
- **Architectural Refactoring**: The project was reorganized into a professional layered structure (`ui.activities`, `ui.utils`, `data.remote`, `data.model`). Business logic was extracted from Activities into specialized helpers (`NavigationHelper.java`, `LoginToggleHelper.java`, `MenuBarHelper.java`) and repositories (`AuthRepository.java`) with assistance from Gemini AI and edited by Jiahao.
- **UI Component Implementation & Optimization**: `LiquidGlassView.java` was implemented and later optimized for performance (pre-allocating objects, moving logic out of `onDraw`) with Gemini AI assistance and edited by Jiahao.
- **Machine State & Manager Pattern**: Implemented a specialized `MachineStateHelper` and manager classes (`WasherManager.java`, `DryerManager.java`) to handle complex UI states and selection outlines for multiple machine types sharing layout IDs. This logic was implemented with assistance from Gemini AI and edited by Jiahao.
- **Advanced Documentation System**: A comprehensive Javadoc system featuring interactive navigation hints ("Hold Cmd/Ctrl + Click") was implemented across all major Activities and classes with Gemini AI assistance and reviewed/edited by Jiahao.
- **Error Resolution & Stability**: Fixed critical `BuildConfig` symbol lookup issues and Retrofit constant requirement errors. Also assisted with orientation locking in `AndroidManifest.xml`. All performed with Gemini AI assistance and reviewed/edited by Jiahao.

**ChatGPT:**
- **Code Refactoring & Structure Improvements**: Assisted in refactoring several Activities and utility classes to improve readability and maintainability, including suggestions for separating logic into helper classes and improving method organization, with ChatGPT assistance and reviewed/edited by Jiahao.
- **Android UI & XML Layout Assistance**: Provided guidance on Android XML layout design, including modifying button shapes, improving layout structure, and configuring UI elements such as `LinearLayout`, `ConstraintLayout`, and reusable components, with ChatGPT assistance and reviewed/edited by Jiahao.
- **Debugging & Error Fixes**: Helped troubleshoot Android Studio build errors, Java compilation issues, and missing UI updates. Also provided explanations for issues such as incorrect view updates, layout problems, and refactoring impacts, with ChatGPT assistance and reviewed/edited by Jiahao.
- **Code Explanation & Documentation Support**: Assisted in explaining Java methods, Android components, and project code behavior to improve understanding and documentation within the project, with ChatGPT assistance and reviewed/edited by Jiahao.
- **General Development Guidance**: Provided development guidance related to Android Studio tools (e.g., Logcat usage, TODO markers, and project navigation), as well as general programming advice during the project development process, with ChatGPT assistance and reviewed/edited by Jiahao.
- **Navigation Architecture & Bug Fixing**: Assisted in designing a modular navigation system using `Navigator`, `NavigationRequest`, and `NavigationHelper`, including handling animation separation and preventing duplicate navigation issues (e.g., double-trigger login bug), with ChatGPT assistance and reviewed/edited by Jiahao.
- **UI Animation Handling**: Provided guidance on structuring animation logic centrally within helper classes to avoid duplicated animations (e.g., slide-in animation triggering twice). Suggested use of flags and clean separation of concerns, with ChatGPT assistance and reviewed/edited by Jiahao.
- **Custom UI Enhancements**: Assisted with implementing UI improvements such as text outlines, drawable styling, and reusable UI components for machine display elements, with ChatGPT assistance and reviewed/edited by Jiahao.

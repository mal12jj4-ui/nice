# MyClient (Fabric, Minecraft 1.21.4)

## Build
1. Install JDK 21.
2. Open this folder in IntelliJ IDEA (it imports the Gradle project), or from a terminal:
   `gradle wrapper --gradle-version 8.12` once, then `./gradlew build`
   (Windows: `gradlew.bat build`).
3. The jar is `build/libs/myclient-0.2.0.jar` (not the `-sources` one).
4. Install Fabric Loader 1.21.4 (fabricmc.net/use) and put the jar plus Fabric API (Modrinth) in `.minecraft/mods`.
5. Quick test without installing: `./gradlew runClient`

## Controls
- Right Shift: open/close the menu (rebindable in Minecraft Controls -> MyClient)
- V: Freecam, B: Base Finder, G: Donut Wheel (all rebindable in the menu: open a module, use the Bind row)
- Menu: click a module to open its settings, click its toggle to turn it on, Esc or Back to return
- Keybinds and settings save to `.minecraft/config/myclient.json` when you close the menu

## Modules
Visual: Freecam, Block ESP
Donut: Ancient Debris Finder, Auto Base Hunt, Auto Dig Down, Auto Elytra Fly, Auto Rtp, Auto Sell,
       Base ESP, Base Finder, Donut Wheel, Farm FPS Optimizer

## If the build fails
Version numbers are in gradle.properties (check fabricmc.net/develop for 1.21.4).
Most likely trouble spots are Minecraft method names in:
  render/Render3D.java, mixin/KeyboardInputMixin.java, module/modules/AutoElytraFly.java (isGliding)
Send the compiler error and each one is a one-line fix.

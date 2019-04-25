# VisualKeyLogger

A basic Java application that makes use of the [JNativeHook](https://github.com/kwhat/jnativehook) library to track key-presses without the Swing limitation of requiring window focus in order to register KeyEvents.

Developed with the purpose of providing visual feedback when playing fighting games (DMC, Guilty Gear, etc.). Not for sketchy spyware use.

## Deployment

After deployment (targeted for Windows), two options are provided for execution:

* Run the .jar file: Requires a JRE installation that supports Java8 at least (e.g. [this](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)) and the `%JRE_HOME%` environment variable to be [set](https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html). Run `VisualKeyLogger.exe`.

* Run the .exe bundled with a JRE installation: Self-explanatory.

## Licensing

This software is covered under the [MIT license](./LICENSE), so feel free to use it if you so desire.

## Resources

The application icon is sourced from <a href="http://cliparts.co">cliparts.co</a>, so kudos to them.

[![Code Climate](https://codeclimate.com/github/amtgard/amtgard-phpstorm-plugin/badges/gpa.svg)](https://codeclimate.com/github/amtgard/amtgard-phpstorm-plugin)


# Amtgard Builder Hints

Provides declaration/implementation completions for [Amtgard Builder Traits](https://github.com/amtgard/builder-traits).

This adds fun little autocompletes when you type the arrow operator (->) to variables identifiable as classes with 
the Amtgard Builder, Getter, Setter, or ToBuilder traits.

## Editing
This plugin was built with 

```text
IntelliJ IDEA 2024.3.2.2 (Ultimate Edition)
Build #IU-243.23654.189, built on January 29, 2025
```
And requires that or a similar editor.

### First Build
On first build, you will need to build the gradle wrapper:

`gradle wrapper`

then

`/.gradlew build`

## Building plugin

To build the plugin, run:

`./gradlew buildPlugin`

Output will be in `build/distributions`
# [Green](https://zedseven.github.io/Green/)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![# Issues](https://img.shields.io/github/issues/zedseven/Green.svg?logo=github)](https://github.com/zedseven/Green/issues)

This is a relatively simple library designed to emulate most of the features of [Greenfoot](https://www.greenfoot.org/)
in [Processing](https://processing.org/).
This was originally made for a school project and aims to enable easy 2D game creation within Processing.

If you're using this in a class or as a teaching tool, I'd love to hear about it!

## Installation
1. `Sketch > Import Library... > Add Library...`
2. Search for `Green`
3. Select the library and click `Install`

### Manual Installation
These steps are only in case the above steps don't work. You should try them first.

1. Go to the [latest GitHub release](https://github.com/zedseven/Green/releases/latest)
2. Download the `.zip` file
3. Unzip it to wherever you want to keep the library
4. Open `Green-<version>.zip/library/` and put `Green.jar` in your Processing libraries folder
5. Processing's `libraries` folder is located in `<sketchbookLocation>/libraries`
   - Its sketchbook location can be found in the editor under `File > Preferences...`

Once installed:
1. Add it to a project with `Sketch > Import Library...`
2. Select it from the popup list

## Reference
To see the Javadoc reference, please visit https://zedseven.github.io/Green/.

## The Basic Idea
The library works with two concepts: worlds and actors.

The `World` class is where all the interaction and gameplay takes place - it's where all the `Actors` live, and for most
games one `World` is enough.

The `Actor` class is where all the per-item code goes: you might have a player `Actor`, one for an item the player can
pick up, and maybe a few for obstacles.

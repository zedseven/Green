# Green
#### A library for Processing written by Zacchary Dempsey-Plante
This is a relatively simple library designed to emulate most of the features of [Greenfoot](https://www.greenfoot.org/) in [Processing](https://processing.org/).
This was done for a school project, so it will likely _not_ be maintained. 

## Installation
First, go to the Releases tab and download the .zip file, and unzip it to wherever you want to keep the library.
To install the library, simply open `Green-<version>.zip\library\` and put `Green.jar` in your Processing libraries folder.
Processing's `libraries` folder is located in `<sketchbookLocation>\libraries`, and it's sketchbook location can be found in the editor under `File > Preferences...`.

From there, select `Sketch > Import Library...` in the editor, and Green should show up for selection.

## Reference
To see the Javadoc reference, please visit [https://zedseven.github.io/Green/](https://zedseven.github.io/Green/).

## A brief run-down
The library works with two basic classes: `Worlds` and `Actors`.

The `World` class is where all the interaction and gameplay takes place - it's where all the `Actors` live, and for most games one `World` is enough.

The `Actor` class is where all the per-item code goes: you might have a player `Actor`, one for an item the player can pick up, and maybe a few ones for obstacles.
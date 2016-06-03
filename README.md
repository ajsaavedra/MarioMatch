# MarioMatch
This is a fun card-matching game that generates, shuffles and lays down a deck of cards represented by Super Mario Bros. characters.

Using a 2-dimensional array of panels, the game lays down the cards and the player must simply find all cards that match before the time runs out.

## Building

This project repo is ready to be used from Eclipse.

In Eclipse go to: File/New/Java Project

Un-check "Use default location" and then click Browse and navigate to the top level directory with contains your source, libs, configs, etc.

Eclipse will display a warning that says that your project "overlaps the location of another project".

Give your project the directory name, and now Eclipse will let you click on the "Next" button to continue configuration of your project.

# How to play

To start a new game, simply click the New Game button. You may quit the game at any time.

Click one card, and try to guess the location of its match. If the selected cards don't match, the game will flip them back, but will leave them
face-up when they do. Each match garners one point towards the player.

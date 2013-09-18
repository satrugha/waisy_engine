waisy_engine
============

Java-based modular plug &amp; play 2D game engine
This engine is written for desktops and applets using J2SE 1.7
An android core is highly tentative.


This engine is free to use for the free games and fan games communities.
This is not for commercial use or for-profit games (including games with microtransactions).
This engine is based around Super Mario-style games. No copyright infringement intended. Don't sue me. I'm poor.


If you plan on using the Waisy engine, please note that you do somewhere so that other people see what can be done with it!
I shall be adding a splash screen for free use.




The planned modules are as follows (please note this is subject to change any time):
Please follow the planned structure for any additions. Contact me (https://github.com/satrugha) with any questions.


Waisy.core - all core functionalities required for any style game (planned)
- .core - includes base functionalities, including generic game loop and state handling
- .file - xml file handling and loading
- .graphics - includes graphics loading, sprites, and rendering
- .input - interface for input system. plan to include a controller and keyboard interface
- .math - math extensions, such as refined random algorithms
- .message - a custom message pump and handler
- .physics - physics, including movements, projectiles
- .player - generic player interface
- .sound - sound engine and library
- .structures - any additional data structures
- .ui - ui interface, basic menuing system
- .worldhandler - handles loading and managing of levels and worlds
- tbd


Waisy.platformer - 2D platformer game engine (planned)
- .core - includes basic core functionalities
- .ai - basic enemy ai
- .level - level building, navigation, and collision handling
- .level.elements (tentative name) - handling specific typical elements within a level. Will include specific subpackages.
- .level.paralax - handles paralax effects, background movements, and foreground overlays
- .player - specifics for a player character, including all animations, actions, sound effects, etc
- tbd


Waisy.platformer.extendedAI - customized AI algorithms for boss battles and more complex enemies
This will be included as a separate module in case programmers wish to not include it


Waisy.overworld - a basic world map handler and editor (planned)
Includes various formats. Planned to be skinnable.


Waisy.convo - a conversation engine for scripted cutscenes and interactions


Waisy.minigames - an enveloping module set of mini games.
This will be broken into sub-modules as needed.
Planned minigames are:
- Memory
- Toad House
- Mini boss battle (Minigame from overworld map)



Waisy.match3 - a match3 engine


Waisy.rpg - a highly tentative rpg engine


Also planned:
eclipse plug-ins for editing bosses, maps, and player data

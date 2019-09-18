# OpenDyna
OpenDyna is a clone of the DOS game Dyna Blaster. It is completely written in Java using only the standard library.

## Installation
```
git clone https://github.com/danielroth1/OpenDyna.git
cd OpenDyna && mkdir build && javac src/main/Main.java -d build -sourcepath src/ && cp -r src/gui/images/ build/gui/
```

## Execution
```
cd build
java main.Main
```

Note:
After starting the game, a folder ```<home>/OpenDyna/``` is created.
Copy the maps folder to ```<home>/OpenDyna/maps``` to access the pre created maps ingame.


## Features

Summary:
- A simple AI
- Generate random or chose one of the preconstructed maps
- Create new maps in the provided level editor
- Local multiplayer at one PC for up to four players, individual keybindings are supported

Use the lobby to select the map and add human or AI players to the game. Create random maps of different sizes and block densities by a simple button press in the lobby. These maps are immediately displayed in the preview.
![Bildschirmfoto zu 2019-09-18 19-45-54](https://user-images.githubusercontent.com/34305776/65177248-a1e9af80-da56-11e9-86aa-c87b4e6b0b9f.png)

Chose between a rich set of given maps or the ones you created in the level editor. Each map is listed by name and with its own preview. Starting locations are indicated by the green fields.
![Bildschirmfoto zu 2019-09-18 20-00-17](https://user-images.githubusercontent.com/34305776/65176183-8bdaef80-da54-11e9-9ef4-3d09478b29ae.png)

Multiplayer is done locally on the same keyboard. It is also possible to use multiple keyboards as long as each person only uses his set of keys. Custom keybinding for each player can be set under "Profiles".
![Bildschirmfoto zu 2019-09-18 21-06-11](https://user-images.githubusercontent.com/34305776/65178013-2a1c8480-da58-11e9-8607-547db0df199c.png)

## Gameplay

Play a free-for-all with up to 3 friends and as many other computer enemies as you wish.
![Bildschirmfoto zu 2019-09-18 19-54-48](https://user-images.githubusercontent.com/34305776/65177169-7a92e280-da56-11e9-90c1-12adf657da76.png)

## Level Editor
Create new maps in the level editor. For each map a .map file is created and added to the other map files under ```<home>/OpenDyna/maps```. It is possible to share map files between different computers. Just place them in the maps folder to access them ingame.
![level editor_174](https://user-images.githubusercontent.com/34305776/33760880-98fed336-dc07-11e7-8a78-d3c58969e87a.png)

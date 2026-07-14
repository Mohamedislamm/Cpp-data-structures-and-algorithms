# OOP XO / Connect-4 / Pyramid Games

This workspace contains a small object-oriented C++ practice project with:
- an XO game implementation
- a separate folder for other assignment exercises

## Structure
- [include/BoardGame_Classes.hpp](include/BoardGame_Classes.hpp) — shared class declarations
- [src/xo](src/xo) — the XO game source files
- [src/assignments](src/assignments) — other exercise files that are not part of the main XO build

## Build
From the project root, run:

```bash
g++ -std=c++17 src/xo/X_O_App.cpp src/xo/Player.cpp src/xo/RandomPlayer.cpp src/xo/X_O_Board.cpp src/xo/GameManager.cpp -Iinclude -o xo_game.exe
```

Or use the VS Code build task named "build XO game".

## Run
```bash
./xo_game.exe
```

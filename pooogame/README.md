# Strategy Game (Java Terminal)

A turn-based strategy game where you control a faction, gather resources, build an army, and defeat the enemy.

## Features
- **Map Check**: 10x10 Grid with Grass, Water, and Mountains.
- **Resources**: Gold, Wood, Stone, Food.
- **Buildings**: Command Center, Training Camp, Farm, Mine.
- **Units**: Soldier, Archer, Cavalier.
- **AI**: Basic enemy AI that moves towards your base.

## How to Play

### Prerequisites
- Java 11 or higher installed.

### Compilation
Open a terminal in the project directory and run:

```bash
mkdir bin
javac -d bin -sourcepath src src/com/pooogame/Main.java
```

### Running the Game
```bash
java -cp bin com.pooogame.Main
```

### Controls (Commands)
The game uses text commands. Type the command and press Enter.

*   `select x y`: Selects a tile at coordinates (x, y). Start counts from 0,0 (Top Left).
*   `move x y`: Moves the selected unit to (x, y). (Must be adjacent).
*   `attack x y`: Attacks an enemy unit at (x, y).
*   `build [type]`: Builds a structure on the selected empty tile.
    *   Types: `center`, `camp`, `farm`, `mine`.
*   `recruit [type]`: Recruits a unit from the selected building.
    *   Types: `soldier`, `archer`, `cavalier`.
*   `next`: Ends your turn. Allows AI to move and you gain resources.
*   `exit`: Quits the game.

### Rules
1.  **Resources**: You gain resources at the end of each turn based on your buildings.
    *   Command Center: +Gold, +Wood
    *   Farm: +Food
    *   Mine: +Stone, +Gold
2.  **Combat**: Units deal damage based on Attack - Defense. Archers have range.
3.  **Winning**: Destroy all enemy units (implied goal).

## Technical Details
- **Pattern**: MVC (Model-View-Controller).
- **Structure**:
    - `model`: Entities and Logic data.
    - `view`: Console rendering.
    - `controller`: Game loop and input handling.

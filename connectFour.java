

/**
 * Supports playing Connect 4 games between two players on a fixed 6x7 board.
 * Instances represent a single game following standard Connect 4 rules. 
 * The RED player starts, and players alternate dropping pieces into columns 0-6. 
 * The first to achieve four pieces in a row horizontally or diagonally wins. 
 * All game states are maintained internally and exposed via JSON responses.
 * 
 * 
 * Game state JSON structure:
 <pre>
    {
        "cells": [null|"RED"|"BLACK"],   // 42 total cells
        "currentPlayer": "RED"|"BLACK",  // Next player to move
        "winner": "RED"|"BLACK"|null     // null = Active game
    }
 </pre>
 */
interface ConnectFour {

    /**
     * Initialize a new Connect 4 game with specified player names.
     *
     * <p>
     * Initializes an empty 6x7 board and allows for new turns to be played.
     * with RED as the current player (player1). Subsequent calls to startGame()
     * will reset the game. Resets all cells to empty board.
     * </p>
     *
     * <p>
     * <strong>Preconditions:</strong> No preconditions—you may call startGame()
     * on a fresh instance of the implementation or after endGame() is called.
     * </p>
     *
     * @param player1 First player name. This player will have RED checkers.
     *                1-20 chars, cannot be null.
     * @param player2 Second player name. This player will have BLACK checkers.
     *                1-20 chars, cannot be null.
     * @return JSON of initial empty game state
     * @throws IllegalArgumentException if player1 or player2 is null or >20 chars
     *
     *                                  <p>
     *                                  <strong>Example:</strong>
     *                                  </p>
     *
     *                                  <pre>
     * <code>
     * // Returns the initial board.
     * String result1 = game.startGame("Alice", "Bob");
     *
     * // Throws an IllegalArgumentException because Player1 is null.
     * game.startGame(null, "Bob");
     * </code>
     *                                  </pre>
     */

    String startGame(String player1, String player2);

    // End game prematurely
    String endGame(); // [Mark]

    /**
     * Conducts a game turn for the specified player and column.
     * A turn involves placing the player's checker in the specified column,
     * allowing it to fall to the lowest unoccupied space.
     * 
     * @pre
     * Preconditions:
     * - The game must be ongoing
     * 
     * - The specified column should not be full
     * 
     * @post
     * Postconditions:
     * - The player's checker is placed in the lowest available space in the specified column.
     * 
     * - The game state is updated to reflect the new move
     * 
     * @param column int [0-6] where the player wants to place their checker.
     * @throws IllegalArgumentException if the column is out of bounds (not between 0 and 6) or if the column is full.
     * @throws IllegalStateException if the game has not been started or has already ended.
     * @see ConnectFour.getGameState() for the expected return JSON format
     * @return the current game state in JSON format
     */
    String takeTurn(int column);

    /**
     * Returns the current state of the game in JSON format.
     * 
     * Example game state JSON for a game in progress during turn 4 with BLACK to move next:
    <pre>
    {
        "cells": [
        //cols: 0    1    2    3    4    5    6
                null,null,null,null,null,null,null, // row 0
                null,null,null,null,null,null,null, // row 1
                null,null,null,null,null,null,null, // row 2
                null,null,"RED",null,null,null,null, // row 3
                null,null,"BLACK",null,null,null,null, // row 4
                null,null,"RED",null,null,null,null    // row 5
        ],        
        "currentPlayer": "BLACK",
        "winner": null
    }
    </pre>
     * 
     * Where:
     * - "cells" is a flat array representing the board from top-left to bottom-right,
     * with Checker if filled or null otherwise
     * 
     * - "currentPlayer" indicates whose turn it is next, 
     * with Checker or null otherwise
     * 
     * - "winner" indicates the winning player, 
     * with Checker or null otherwise
     * 
     * @see Checker for possible Checker values.
     * @return the current game state in JSON format
     */
    String getGameState();
}

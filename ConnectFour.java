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
public interface ConnectFour {

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
     * <strong style="font-family: Arial; font-size: 0.856em">Preconditions:</strong> No preconditions—you may call startGame()
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
     * 
     * @see ConnectFour#getGameState() for the expected return JSON format
     */
    String startGame(String player1, String player2);

    /**
     * Resets the currently active game to a fresh initial state and returns the new
     * empty game state as JSON.
     * 
     * <p>
     * <strong style="font-family: Arial; font-size:
     * 0.856em">Preconditions:</strong> None—you may call resetGame()
     * on a fresh instance of the implementation or after endGame() is called.
     * </p>
     * 
     * <p>
     * This method recreates a new 6x7 empty board with the same player names as the
     * previous game,
     * sets the internal game state to Active with RED as the current player
     * (player1).
     * All cells are reset to empty. Subsequent calls to resetGame() will create
     * additional new games.
     * </p>
     *
     * <p>
     * If no previous game exists with stored player names,
     * this method returns a minimal error JSON object.
     * </p>
     *
     * <p>
     * The operation uses previously stored player names from the last valid
     * startGame() call.
     * Calling resetGame() without a prior startGame() will fail with an error.
     * </p>
     *
     * @return a JSON string containing one of the following:
     *         <ul>
     *         <li>the initial empty game state (empty 6x7 cells array,
     *         currentPlayer:"RED", winner:"none")</li>
     *         <li>{"error": "No game"} — when no previous game with player names
     *         exists</li>
     *         </ul>
     *
     * @see #startGame(String, String) for initial game creation with player names
     * @see ConnectFour#getGameState() for the expected return JSON format
     */
    String resetGame();

    /**
     * Ends the currently active game (if any) and returns its final state as a JSON string.
     * 
     * <p>
     * <strong style="font-family: Arial; font-size: 0.856em">Preconditions:</strong> None
     * </p>
     * 
     * <p>This method transitions the game to an end state, after which
     * no further moves, actions or modifications are permitted on this game instance.
     * The returned JSON reflects the game's state after the end operation has been applied.</p>
     *
     * <p>If no game is currently active,
     * this method returns a minimal error JSON object instead of attempting to end anything.</p>
     *
     * <p>The operation is effectively idempotent: calling this method multiple times
     * on an already-ended game returns the same final state without further changes.</p>
     *
     * @return a JSON string containing one of the following:
     *         <ul>
     *             <li>the final game state (typically including cells, currentPlayer,
     *                 winner if applicable.)</li>
     *             <li>{{"error": "No game"}} — when no active game exists</li>
     *         </ul>
     *
     * @see ConnectFour#getGameState() for the expected return JSON format
     */
    String endGame();

    /**
     * Conducts a game turn for the current player and column.
     * A turn involves placing the player's checker in the specified column,
     * allowing it to fall to the lowest unoccupied space.
     * 
     * <p>
     * <strong style="font-family: Arial; font-size:
     * 0.856em">Preconditions:</strong>
     * <br>
     * - The game must be ongoing
     * <br>
     * - The specified column should not be full
     * </p>
     * 
     * <p>
     * <strong style="font-family: Arial; font-size:
     * 0.856em">Postconditions:</strong>
     * <br>
     * - The player's checker is placed in the lowest available space in the
     * specified column.
     * <br>
     * - The game state is updated to reflect the new move
     * </p>
     * 
     * @param column int [0-6] where the player wants to place their checker.
     * @see ConnectFour#getGameState() for the expected return JSON format
     * @return the current game state in JSON format, or an error JSON if the
     *         move is invalid. 
     *         Error messages are {"error": "Invalid move"} or 
     *         {"error": "Game not active"} as appropriate.
     *         
     */
    String takeTurn(int column);

    /**
     * Returns the current state of the game in JSON format.
     * 
     * <p> 
     * <strong style="font-family: Arial; font-size: 0.856em">Preconditions:</strong> None
     * </p>
     * 
     * <p>Example game state JSON for a game in progress during turn 4 with BLACK to move next </p>
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

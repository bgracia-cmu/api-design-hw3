/**
 * Supports playing Connect 4 games between two players on a fixed 6x7 board.
 * Instances represent a single game following standard Connect 4 rules. 
 * The RED player starts, and players alternate dropping pieces into columns 0-6. 
 * The first to achieve four pieces in a row horizontally or diagonally wins. 
 * All game states are maintained internally and exposed via JSON responses.
 * 
 * <p><b>Note:</b> The String returned by all methods is a JSON-formatted
 * representation of the game state. Clients should parse this JSON to retrieve
 * the {@code cells}, {@code currentPlayer}, and {@code winner} status.
 * This allows language-agnostic implementations 
 * and encourages clean separation between game logic (backend) and UI (frontend). 
 * In Java, for example, JSON libraries such as 
 * <a href="https://github.com/FasterXML/jackson">Jackson</a> or 
 * <a href="https://google.github.io/gson/">Gson</a> can be used.
 * </p>
 *
 * Game state JSON structure:
 * <pre>
 *  {
 *      "cells": [null|"RED"|"BLACK"],   // 42 total cells
 *      "currentPlayerName": String|null, // null if game not active
 *      "currentPlayerChecker": "RED"|"BLACK",  // Next player to move
 *      "player1Color": "RED"|"BLACK",  // Player 1's checker color
 *      "player2Color": "RED"|"BLACK",  // Player 2's checker color
 *      "winner": "RED"|"BLACK"|"Draw"|null     // null = Active game, CHECKER = Winner, "Draw" = Draw
 *  }
 * </pre>
 * 
 * The following example demonstrates how a client should interact with this
 * interface to facilitate a full game session, including turn-taking and
 * error handling using the JSON state.
 * <pre>{@code
 * // 1. Initialize the game engine and start a session
 * // Optional: Pass "Black" to set Player 1 as Black
 * ConnectFourJSON game = new ConnectFourImpl();
 * String state = game.startGame("Player1", "Player2", "Black");
 * // Initial render
 * printState(state);
 *
 * // 2. Main Game Loop
 * while (true) {
 *  // Prompt user for input based on the current JSON state
 *  String input = promptUser(state);
 *
 *  if (input.equals("q")) {
 *  System.out.println("Game ended.");
 *  break;
 *  }
 *
 *  // Parse the column input (0-6)
 *  int col = parseColumn(input);
 *  if (col == -1) continue;
 *
 *  // 3. Update the engine state by taking a turn
 *  // The engine returns a new JSON string representing the updated state
 *  state = game.takeTurn(col);
 *
 *  // 4. Render the updated board or display errors (e.g., "Column Full")
 *  printState(state);
 *  }
 *}
 *}</pre>
 * 
 * @apiNote For player-player, player-computer, or computer-computer games, 
 * clients can implement their own logic to determine moves and call takeTurn() accordingly.
 * For example, computer players can be implemented with a simple random number generator to select columns
 */
public interface ConnectFourJSON {

    /**
     * Initialize a new Connect 4 game with specified player names.
     *
     * <p>
     * Initializes an empty 6x7 board and allows for new turns to be played.
     * with RED as the current player (player1). Subsequent calls to startGame()
     * will erase any existing game and start a new game.
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
     * @param player1CheckerPreference First player checker color preference. This
     *                                 is optional and will default to RED if an
     *                                 empty or invalid input is provided.
     *
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
     * String result1 = game.startGame("Alice", "Bob", "Red");
     *
     * // Throws an IllegalArgumentException because Player1 is null.
     * game.startGame(null, "Bob", "Reds");
     * </code>
     *                                  </pre>
     * 
     * @see ConnectFourJSON#getGameState() for the expected return JSON format
     */
    String startGame(String player1, String player2, String player1CheckerPreference);

    /**
     * Initialize a new Connect 4 game with specified player names.
     *
     * <p>
     * Initializes an empty 6x7 board and allows for new turns to be played.
     * with RED as the current player (player1). Subsequent calls to startGame()
     * will reset the game. Resets all cells to empty board. First player name will play
     * with RED checkers by default.
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
     *
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
     * @see ConnectFourJSON#getGameState() for the expected return JSON format
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
     * @see ConnectFourJSON#getGameState() for the expected return JSON format
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
     * @see ConnectFourJSON#getGameState() for the expected return JSON format
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
     * <br>
     * - The currentPlayer is automatically updated to the next player in the JSON game state
     * </p>
     * 
     * @param column int [0-6] where the player wants to place their checker.
     * @see    ConnectFourJSON#getGameState() for the expected return JSON format
     * @throws IndexOutOfBoundsException if the column is out of bounds (not between 0 and 6) or if the column is full.
     * @return the current game state in JSON format, or an error JSON if the
     *         move is invalid. 
     *         Error messages are {"error": "Invalid move"} or 
     *         {"error": "Game not active"} as appropriate.
     *         
     * <pre>
     * <code>
     * // Example of a valid move returning the updated game state.
     * String result1 = game.takeTurn(3);  
     * 
     * // Example of the client checking the valid move for an error JSON.
     * if (result1.contains("error")) {
     *     // Handle error case
     * }
     * </code>
     * </pre>
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
        "currentPlayerName": "player2",
        "currentPlayerChecker": "BLACK",
        "player1Color": "RED",
        "player2Color": "BLACK",
        "winner": null
    }
    </pre>
     * 
     * Where:
     * <p>
     * - "cells" is a flat array representing the board from top-left to bottom-right,
     * with Checker if filled or null otherwise
     * </p>
     * 
     * <p>
     * - "currentPlayerChecker" indicates whose turn it is next,
     * with Checker or null otherwise
     * </p>
     * 
     * <p>
     * - "currentPlayerName" shows the name of the current player
     * </p>
     * 
     * <p>
     * - "player1Color" shows player 1's preferred color
     * </p>
     * 
     * <p>
     * - "player2Color" shows player 2's preferred color
     * </p>
     * 
     * <p>
     * - "winner" indicates the winning player 
     * with Checker, null if the game is still active, 
     * or "Draw" if the game ended in a draw.
     * </p>
     * 
     * @apiNote This is specifically useful for UIs to query the current game state
     * and render it accordingly at any point during the game.
     * @see Checker for possible Checker values.
     * @return the current game state in JSON format
     */
    String getGameState();
}

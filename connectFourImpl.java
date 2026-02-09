
public class connectFourImpl implements connectFour {
    private Game game;
    private String player1Name;
    private String player2Name;

    /**
     * Initialize a new Connect 4 game with specified player names.
     * 
     * <p>
     * Initializes an empty 6x7 board and sets the internal game state to Active
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
     * <p>
     * <strong>Example:</strong>
     * </p>
     * 
     * <pre>
     * <code>
     * // Returns the initial board.
     * String result1 = game.startGame("Alice", "Bob");
     * 
     * // Throws an IllegalArgumentException because Player1 is null.
     * game.startGame(null, "Bob");
     * </code>
     * </pre>
     */
    @Override
    public String startGame(String player1, String player2) {
        if (player1 == null || player2 == null) {
            throw new IllegalArgumentException("Player names cannot be null");
        }
        if (player1.length() > 20 || player2.length() > 20) {
            throw new IllegalArgumentException("Player names cannot exceed 20 characters");
        }

        this.player1Name = player1;
        this.player2Name = player2;
        this.game = new Game();
        return game.getGameStateJson();
    }

    @Override
    public String endGame() {
        if (game != null) {
            game.endGame();
        }
        return game != null ? game.getGameStateJson() : "{\"error\": \"No game\"}";
    }

    @Override
    public String takeTurn(int column) {
        if (game == null) {
            return "{\"error\": \"Game not started\"}";
        }
        boolean success = game.takeTurn(column);
        return game.getGameStateJson();
    }

    @Override
    public String returnGameState() {
        if (game == null) {
            return "{\"error\": \"Game not started\"}";
        }
        return game.getGameStateJson();
    }
}


public class connectFourImpl implements connectFour {
    private Game game;
    private String player1Name;
    private String player2Name;

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
    public String takeTurn(String player, int col) {
        if (game == null) {
            return "{\"error\": \"Game not started\"}";
        }
        boolean success = game.takeTurn(col);
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

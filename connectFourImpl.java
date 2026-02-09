
public class connectFourImpl implements connectFour {
    private Game game;
    private String player1Name;
    private String player2Name;

    @Override
    public String startGame(String player1, String player2) {
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

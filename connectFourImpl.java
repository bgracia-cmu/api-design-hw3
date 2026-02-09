// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
public class connectFourImpl implements connectFour {
    private Game game;
    private String player1Name;
    private String player2Name;

    public connectFourImpl() {
    }

    public String startGame(String var1, String var2) {
        this.player1Name = var1;
        this.player2Name = var2;
        this.game = new Game();
        return this.game.getGameStateJson();
    }

    public String endGame() {
        if (this.game != null) {
            this.game.endGame();
        }

        return this.game != null ? this.game.getGameStateJson() : "{\"error\": \"No game\"}";
    }

    public String takeTurn(String var1, int var2) {
        return this.game == null ? "{\"error\": \"Game not started\"}" : this.game.getGameStateJson();
    }

    public String returnGameState() {
        return this.game == null ? "{\"error\": \"Game not started\"}" : this.game.getGameStateJson();
    }
}

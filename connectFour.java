
interface connectFour {

    String startGame(String player1, String player2);

    // End game prematurely
    String endGame(); // [Mark]

    // take turn for a particular player (returns gameState JSON including win
    // state)
    String takeTurn(String player, int col); // [Bruno]

    // Return gameState JSON
    String returnGameState(); // [Bruno]

}

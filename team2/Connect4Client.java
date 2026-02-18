import java.util.Random;

public class Connect4Client {
  public static void main(String[] args) {
    Connect4 game = new Connect4();
    System.out.println(game.showBoard());

    // BG: A round is an entire game
    // BG: isPlaying: whether rounds are ongoing
    while (game.isPlaying()) {
      while (game.isRoundInProgress()) {
        // make moves

        Random rand = new Random();
        int column = Math.round(rand.nextFloat() * 6);
        System.out.println("Moving to  " + column);
        game.makeMove(column);
        System.out.println(game.showBoard());
        // try {
        //   Thread.sleep(0);
        // }
        // catch(Exception e) {}
      }

      if (game.getWinner() == -1 && !game.isRoundInProgress()) {
        // a draw
        System.out.println(("draw"));
        game.stopPlaying();
      }
      else {
        game.resetGame();
      }
    }
  }
}

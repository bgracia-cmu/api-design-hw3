
import java.util.Scanner;

/**
* Class Connect4
*
* <p> A utility class that represents a standard Connect4 game on a 6x7 grid. </p>
*
* <p> A standard Connect4 is a two-player game where each players drops checkers
* into columns of board in turns, and try to make horizontal, vertical or diagonal
* lines of four connected checkers. </p>
* 
* <p>This class maintains the game state, including board, current player and
* game results. It supports displaying the board, making moves, and switch
* turns. </p>
*
* <p> Players are represented internally by integer: </p>
* <ul>
*  <li> 0 = empty </li>
*  <li>1 = Player 1 = 'X' when displaying </li>
*  <li>2 = Player 2 = 'O' when displaying </li>
*</ul>
*
* <p>The game continues while both isPlaying() and isRoundInProgress() return true. 
* The round will end until one of the players wins, or the board is full. Once the 
* round ends, the result can be determined by getWinner(). While isPlaying() remains 
* true, client can choose to terminate the game with stopPlaying() or start a new round 
* of play with resetGame().</p>
*
 * <p>Example usage:</p>
 *
 * <pre>{@code
 * Connect4 game = new Connect4();
 * System.out.println(game.showBoard());
 *
 * while (game.isPlaying()) {
 *     while (game.isRoundInProgress()) {
 *         // make moves
 *     }
 *
 *     if (game.getWinner() == -1 && !game.isRoundInProgress()) {
 *         // a draw
 *     }
 * }
 * }</pre>
 */

public final class Connect4{
   private final int[][] board = new int[6][7];
   private final int[] nextAvailableRow = {5, 5, 5, 5, 5, 5, 5};
   private int currentPlayer = 1; // 1 or 2
   private int winner = -1; // 1 or 2
   //true while game still playing, false when finished
   private boolean playing = true;
   private boolean roundInProgress = true;

   /**
    *
    * Initializes game session.
    *
    */
   public Connect4(){}

   /**
    * Returns current player.
    *
    * @return 1 if it is player 1's turn, 2 if it is player 2's turn.
    *
    */
   public int getCurrentPlayer(){
       return currentPlayer;
   }
  /**
    * Returns the winner of the game.
    *
    * The default winner is -1. If no one wins and the game is a draw,
    * it returns -1. Otherwise it return the 1 or 2, corresponding to players.
    *
    * @return 1 if Player 1 wins
    *         2 if Player 2 wins
    *         -1 if no one wins
    */

   public int getWinner(){
       return winner;
   }
  /**
    * Returns whether the game is still in progress.
    *
    * This method returns true while overall game session is active. 
    * It returns false once stopPlaying is called.
    *
    * @return true if game is still in progress
    *         false if the game ends
    */

   public boolean isPlaying(){
       return playing;
   }

   /**
    * Ends the current game session.
    *
    * After calling this method, no additional rounds will be played.
    *
    */
   public void stopPlaying(){
       playing = false;
   }

   /**
    * Returns whether the current round is still in progress.
    *
    * @return  true if the round is ongoing, false if it has ended.
    *
    */
   public boolean isRoundInProgress(){
       return roundInProgress;
   }

   /**
    *
    * Resets game to inital game state.
    *
    * This method resets the board state, the current player, and round state.
    *
    */
   public void resetGame(){
       for (int r = 0; r < board.length; r++) {
           for (int c = 0; c < board[r].length; c++) {
               board[r][c] = 0;
           }
       }
      
       for (int i = 0; i < nextAvailableRow.length; i++) {
           nextAvailableRow[i] = 5;
       }
      
       currentPlayer = 1;
       winner = -1;
       playing = true;
       roundInProgress = true;
   }
  /**
    * A formatted string representation of the game board.
    *
    * Cells in a row is separated by '|'. Each cell is displayed using
    * the following charactors:
    *      Blank - empty cell
    *      X - Player 1
    *      O - Player 2
    * Column number are printed at the bottom.
    *
    * This method does not print the board directly. It returns a string for
    * clients to decide how to display the output.
    *
    * @return A string of formatted game board.
    *
    */

   public String showBoard(){
       StringBuilder sb =  new StringBuilder();
       for(int r=0; r<6; r++){
           sb.append("|");
           for(int c=0; c<7; c++){
               if(board[r][c]==0){
                   sb.append("   ").append("|");
               }else if(board[r][c]==1){
                   sb.append(" X ").append("|");
               }else if(board[r][c]==2){
                   sb.append(" O ").append("|");
               }
           }
           sb.append("\n");
       }
       sb.append(" ");
       for(int c=0; c<7; c++){
           sb.append(" ").append(c).append("  ");
       }
       sb.append("\n");
       return sb.toString();
   }

   /**
    * Drops a checker for the current player into the specified column on the board.
    *
    * This method updates the board state, and switches the current player if the move is valid.
    * It also checks for a winning condition and updates the game state accordingly.
    *
    * @param column the column (0-indexed) where the player wants to drop their checker.
    * @return true if the move was valid and successfully made, false if the move was invalid
    *          (e.g., column is full or out of bounds)
    */
   public boolean makeMove(int column){
       if ((!isBoardFull()) && (isValidColumn(column))){

           int row = nextAvailableRow[column];

           if ((isValidRow(row))) {
               board[row][column] = currentPlayer;
               nextAvailableRow[column]--;
               updateCurrentPlayer();
               checkSuccess();

               if (!checkSuccess() && isBoardFull()){
                   roundInProgress = false;
               }

               return true;
           }

       }

       return false;
   }

   //==== Helper Functions ======
   /**
    * Helper function to use in makemove.
    * Check the whole board to see if any player wins
    * if so, return true, and set winner to that player,
    * and set the playing status to false.
    * return false if no one wins yet.
    */

   private boolean checkSuccess(){
       if(!playing) return (winner!=-1);
       //right, down, right-down, left-down
       int[] dr = { 0, 1, 1, 1 };
       int[] dc = { 1, 0, 1, -1 };

       for(int r=0; r<6; r++){
           for(int c=0; c<7; c++){
               int p = board[r][c];
               if(p==0) continue; //empty
               for(int i=0; i<4; i++){
                   int count = 1;
                   int nextr = r+dr[i];
                   int nextc = c+dc[i];

                   //check endbound, avoid meaningless scanning
                   int endr = r+3*dr[i];
                   int endc = c+3*dc[i];
                   if (endr<0 || endr>=6 || endc<0 || endc>=7) continue;

                   //within bound & connect
                   while(nextr>=0 && nextr<6 && nextc>=0 && nextc<7
                       && board[nextr][nextc] == p){
                       count++;
                       if(count==4){
                           winner=p;
                           roundInProgress =false;
                           return true;
                       }
                       nextr += dr[i];
                       nextc += dc[i];
                   }
               }
           }
       }

       return false;
   }

   private boolean isValidColumn(int column){
       if (column >= 0 && column < 7){
           return true;
       }
       return false;
   }

   private boolean isValidRow(int row){
       if (row >= 0 && row < 6){
           return true;
       }
       return false;
   }

   private boolean isBoardFull(){
       for (int i=0; i < nextAvailableRow.length; i++){
           if (nextAvailableRow[i] != -1){
               return false;
           }
       }
       return true;
   }

   private void updateCurrentPlayer(){
       if (currentPlayer == 1){
           currentPlayer = 2;
       } else{
           currentPlayer = 1;
       }
   }
}

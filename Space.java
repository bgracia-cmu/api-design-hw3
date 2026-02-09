
public class Space {
    private final Board board;
    private final int row;
    private final int col;
    private Checker occupiedBy;

    public Space(Board board, int row, int col) {
        this.board = board;
        this.row = row;
        this.col = col;
        this.occupiedBy = null;
    }

    public Checker getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(Checker checker) {
        this.occupiedBy = checker;
    }

    public boolean isOccupied() {
        return occupiedBy != null;
    }
}
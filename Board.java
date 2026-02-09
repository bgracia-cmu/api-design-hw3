
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int ROWS = 6;
    private final int COLS = 7;
    private final List<Space> spaces;

    public Board() {
        this.spaces = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                spaces.add(new Space(this, row, col));
            }
        }
    }

    public Space getSpace(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            throw new IndexOutOfBoundsException("Row or column out of bounds");
        }
        int index = row * COLS + col;
        return spaces.get(index);
    }
}

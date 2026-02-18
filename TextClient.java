import java.util.Scanner;

public class TextClient {
    private static ConnectFourJSON game = new ConnectFourImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Connect 4 Game Started (6x7) ===");

        String state = game.startGame("Player1", "Player2");
        printState(state);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = promptUser(state);

            if (input.equals("q")) {
                System.out.println("Game ended.");
                break;
            }

            int col = parseColumn(input);
            if (col == -1) {
                continue;
            }

            state = game.takeTurn(col);
            printState(state);
        }
        scanner.close();
    }

    private static String promptUser(String state) {
        GameInfo info = parseState(state);

        if (info.isError() || info.hasWinner()) {
            System.out.print("\nGame over! Press q to quit: ");
        } else {
            System.out.printf("\nNext: %s  Enter column (0-6) or q: ", info.currentPlayer);
        }

        return scanner.nextLine().trim().toLowerCase();
    }

    private static int parseColumn(String input) {
        try {
            int col = Integer.parseInt(input);
            if (col >= 0 && col <= 6) {
                return col;
            }
            System.out.println("Please enter 0-6");
        } catch (NumberFormatException e) {
            System.out.println("Please enter number 0-6 or q");
        }
        return -1;
    }

    private static void printState(String json) {
        GameInfo info = parseState(json);
        if (info.isError()) {
            System.out.println("Error: " + info.error);
            return;
        }

        printBoard(info.cells);
        if (info.hasWinner()) {
            System.out.println(info.winner.equals("RED") ? " RED WINS! " : " BLACK WINS! ");
        } else {
            System.out.printf("Next: %s%n%n", info.currentPlayer);
        }
    }

    private static GameInfo parseState(String json) {
        GameInfo info = new GameInfo();

        // Error check first
        if (json.contains("\"error\"")) {
            String[] parts = json.split("\"error\"\\s*:\\s*\"");
            if (parts.length > 1) {
                info.error = parts[1].replace("\"", "").replace("}", "");
            }
            return info;
        }

        // Extract cells array
        int cellsStart = json.indexOf("\"cells\":") + 8;
        int cellsEnd = json.indexOf("]", cellsStart);
        if (cellsStart < 8 || cellsEnd == -1)
            return info;

        String[] cellsRaw = json.substring(cellsStart, cellsEnd).split(",");

        // Parse cells to display format (. R B)
        for (int i = 0; i < 42 && i < cellsRaw.length; i++) {
            String cell = cellsRaw[i].replaceAll("[\"\\s]", "");
            info.cells[i] = "RED".equals(cell) ? "R" : "BLACK".equals(cell) ? "B" : ".";
        }

        // Parse currentPlayer & winner
        info.currentPlayer = json.contains("\"currentPlayer\": \"RED\"") ? "RED" : "BLACK";
        info.winner = json.contains("\"winner\": \"RED\"") ? "RED"
                : (json.contains("\"winner\": \"BLACK\"") ? "BLACK" : null);

        return info;
    }

    private static void printBoard(String[] cells) {
        System.out.println("\n     0   1   2   3   4   5   6   ← Columns");
        System.out.println("  +-----------------------------+");

        for (int row = 0; row < 6; row++) {
            System.out.print(" " + row + " |");
            for (int col = 0; col < 7; col++) {
                System.out.print(" " + cells[row * 7 + col] + "  ");
            }
            System.out.println("|");
            System.out.println("  +-----------------------------+");
        }
    }

    private static class GameInfo {
        String[] cells = new String[42];
        String currentPlayer = "RED";
        String winner = null;
        String error = null;

        boolean isError() {
            return error != null;
        }

        boolean hasWinner() {
            return winner != null;
        }
    }
}

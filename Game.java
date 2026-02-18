
import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private Checker currentPlayerChecker;
    private final String player1Name;
    private final String player2Name;
    private final Checker player1Checker;

    private Checker winner;
    private boolean gameOver;

    public Game(String player1, String player2, Checker player1CheckerPreference){
        this.board = new Board();
        this.currentPlayerChecker = player1CheckerPreference;

        this.player1Name = player1;
        this.player2Name = player2;
        this.player1Checker = player1CheckerPreference;

        this.winner = null;
        this.gameOver = false;
    }

    public boolean takeTurn(int col) {
        if (gameOver || col < 0 || col >= 7) {
            return false;
        }

        boolean placed = dropChecker(col, currentPlayerChecker);
        if (!placed) {
            return false;
        }

        if (checkWin(currentPlayerChecker)) {
            this.winner = currentPlayerChecker;
            this.gameOver = true;
            return true;
        }

        if (isBoardFull()) {
            this.gameOver = true;
            return true;
        }

        currentPlayerChecker = currentPlayerChecker.opponent();

        return true;
    }

    private boolean dropChecker(int col, Checker checker) {
        for (int row = 5; row >= 0; row--) {
            Space space = board.getSpace(row, col);
            if (!space.isOccupied()) {
                space.setOccupiedBy(checker);
                return true;
            }
        }
        return false;
    }

    private boolean checkWin(Checker checker) {
        // Horizontal
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col <= 3; col++) {
                if (checkLine(row, col, 0, 1, checker)) {
                    return true;
                }
            }
        }

        // Vertical
        for (int row = 0; row <= 2; row++) {
            for (int col = 0; col < 7; col++) {
                if (checkLine(row, col, 1, 0, checker)) {
                    return true;
                }
            }
        }

        // Diagonal /
        for (int row = 0; row <= 2; row++) {
            for (int col = 0; col <= 3; col++) {
                if (checkLine(row, col, 1, 1, checker)) {
                    return true;
                }
            }
        }

        // Diagonal \
        for (int row = 5; row >= 3; row--) {
            for (int col = 0; col <= 3; col++) {
                if (checkLine(row, col, -1, 1, checker)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkLine(int row, int col, int dRow, int dCol, Checker checker) {
        for (int i = 0; i < 4; i++) {
            Space space = board.getSpace(row + i * dRow, col + i * dCol);
            if (space.getOccupiedBy() != checker) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (!board.getSpace(row, col).isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getGameStateJson() {
        String cellsJson = getCellsJson();

        // Convert currentPlayer and winner to JSON-friendly strings (keep null value intact)
        String currentPlayerStr = currentPlayerChecker != null ? ("\"" + getCurrentPlayerName() + "\"") : "null";
        String winnerStr = winner != null ? ("\"" + winner + "\"") : "null";
        String player1Color = player1Checker.toString();
        String player2Color = player1Checker.opponent().toString();

        return String.format("""
                {
                    "cells": %s,
                    "currentPlayerName": %s,
                    "currentPlayerChecker": %s,
                    "player1Color": %s,
                    "player2Color": %s,
                    "winner": %s
                }
                """, cellsJson, currentPlayerStr, currentPlayerChecker.toString(), player1Color, player2Color,   winnerStr);
    }

    private String getCurrentPlayerName() {
        // Using the player1checker as an anchor to which player name to return
        return currentPlayerChecker == player1Checker ? player1Name : player2Name;
    }

    private String getCellsJson() {
        List<String> cells = new ArrayList<>();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Checker checker = board.getSpace(row, col).getOccupiedBy();
                String value = checker != null ? "\"" + checker + "\"" : "null";
                cells.add(value);
            }
        }

        return "[" + String.join(",", cells) + "]";
    }

    public void endGame() {
        this.gameOver = true;
    }
}

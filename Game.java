
import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private Checker currentPlayer;
    private Checker winner;
    private boolean gameOver;

    public Game() {
        this.board = new Board();
        this.currentPlayer = Checker.RED;
        this.winner = null;
        this.gameOver = false;
    }

    public boolean takeTurn(int col) {
        if (gameOver || col < 0 || col >= 7) {
            return false;
        }

        boolean placed = dropChecker(col, currentPlayer);
        if (!placed) {
            return false;
        }

        if (checkWin(currentPlayer)) {
            this.winner = currentPlayer;
            this.gameOver = true;
            return true;
        }

        if (isBoardFull()) {
            this.gameOver = true;
            return true;
        }

        currentPlayer = (currentPlayer == Checker.RED) ? Checker.BLACK : Checker.RED;
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
        String currentPlayerStr = currentPlayer != null ? ("\"" + currentPlayer.name() + "\"") : "null";
        String winnerStr = winner != null ? ("\"" + winner.name() + "\"") : "null";

        return String.format("""
                {
                    "cells": %s,
                    "currentPlayer": %s,
                    "winner": %s
                }
                """, cellsJson, currentPlayerStr, winnerStr);
    }

    private String getCellsJson() {
        List<String> cells = new ArrayList<>();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Checker checker = board.getSpace(row, col).getOccupiedBy();
                String value = checker != null ? "\"" + checker.name() + "\"" : "null";
                cells.add(value);
            }
        }

        return "[" + String.join(",", cells) + "]";
    }

    public void endGame() {
        this.gameOver = true;
    }
}

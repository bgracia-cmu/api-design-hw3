
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextClient {
    public static void main(String[] args) {
        connectFourImpl game = new connectFourImpl();

        System.out.println("=== Connect 4 Game Started (6x7) ===");
        String state = game.startGame("Player1", "Player2");
        printGameState(state);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nColumns: [0][1][2][3][4][5][6]  Enter column (0-6) or 'q': ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Game ended.");
                break;
            }

            try {
                int col = Integer.parseInt(input);
                if (col < 0 || col > 6) {
                    System.out.println("Please enter a number 0-6");
                    continue;
                }

                state = game.takeTurn("Player1", col);
                printGameState(state);

            } catch (NumberFormatException e) {
                System.out.println("Please enter a number 0-6");
            }
        }
        scanner.close();
    }

    private static void printGameState(String json) {
        String[] cells = extractCells(json);
        if (cells == null) {
            System.out.println("Error parsing game state");
            return;
        }

        printBoard(cells);
        printGameInfo(json);
    }

    private static String[] extractCells(String json) {
        // Extract cells array from JSON: "cells": ["empty","RED",...]
        Pattern pattern = Pattern.compile("\"cells\":\\s*\\[([^\\]]*)\\]");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find())
            return null;

        String cellsStr = matcher.group(1);
        String[] cells = cellsStr.split(",");
        // Clean up quotes
        for (int i = 0; i < cells.length; i++) {
            cells[i] = cells[i].replaceAll("[\"']", "").trim();
        }
        return cells;
    }

    private static void printBoard(String[] cells) {
        System.out.println("\n     0   1   2   3   4   5   6     ← Columns");
        System.out.println("  +-----------------------------+");

        for (int row = 0; row < 6; row++) {
            System.out.print(" " + row + " |");
            for (int col = 0; col < 7; col++) {
                int idx = row * 7 + col;
                String cell = cells[idx];
                if ("RED".equals(cell)) {
                    System.out.print(" R  ");
                } else if ("BLACK".equals(cell)) {
                    System.out.print(" B  ");
                } else {
                    System.out.print(" .  ");
                }
            }
            System.out.println("|");
            System.out.println("  +-----------------------------+");
        }
    }

    private static void printGameInfo(String json) {
        if (json.contains("\"winner\": \"RED\"")) {
            System.out.println(" RED WINS! ");
        } else if (json.contains("\"winner\": \"BLACK\"")) {
            System.out.println(" BLACK WINS! ");
        } else {
            String current = json.contains("\"currentPlayer\": \"RED\"") ? "RED" : "BLACK";
            System.out.println("Next: " + current);
        }
        System.out.println();
    }
}

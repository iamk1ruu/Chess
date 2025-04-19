package com.kiruu.chess.util;

public class Position {
    private final int row, col;

    public Position(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7)
            throw new IllegalArgumentException("Invalid position");
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public static Position getNotation(String alg) {
        int col = alg.charAt(0) - 'a';
        int row = 8 - Integer.parseInt(alg.substring(1));  // Fix here
        return new Position(row, col);
    }

    public static boolean equals(Position a, Position b) {
        return a.row == b.row && a.col == b.col;
    }
}

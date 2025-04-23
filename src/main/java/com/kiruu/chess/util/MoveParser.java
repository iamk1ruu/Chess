package com.kiruu.chess.util;

public class MoveParser {

    public static Move parseMove(String notation) {
        if (notation == null || notation.length() != 4) {
            throw new IllegalArgumentException("Invalid move notation: " + notation);
        }

        // files a–h → cols 0–7
        int fromCol = notation.charAt(0) - 'a';
        int toCol   = notation.charAt(2) - 'a';

        // ranks '1'–'8' → rows 7–0
        //   '8' - rankChar gives 0 for '8', 7 for '1'
        int fromRow = '8' - notation.charAt(1);
        int toRow   = '8' - notation.charAt(3);

        // sanity check
        if (fromCol < 0 || fromCol > 7 || toCol < 0 || toCol > 7 ||
                fromRow < 0 || fromRow > 7 || toRow < 0 || toRow > 7) {
            throw new IllegalArgumentException("Move out of bounds: " + notation);
        }

        Position from = new Position(fromRow, fromCol);
        Position to   = new Position(toRow,   toCol);
        return new Move(from, to);
    }
}

package com.kiruu.chess.util;

import com.kiruu.chess.model.*;
import com.kiruu.chess.model.pieces.*;
import java.awt.*;

public class FENParser {

    // Decode a FEN string into a Board object
    public static Piece[][] decode(String fen) {
        Board board = new Board();
        Piece[][] state = new Piece[8][8];
        String[] parts = fen.split(" ");
        String[] rows = parts[0].split("/");

        for (int row = 0; row < 8; row++) {
            String fenRow = rows[row];
            int col = 0;
            for (int i = 0; i < fenRow.length(); i++) {
                char c = fenRow.charAt(i);
                if (Character.isDigit(c)) {
                    col += Character.getNumericValue(c);
                } else {
                    Color color = Character.isUpperCase(c) ? Color.WHITE : Color.BLACK;
                    state[row][col] = createPiece(c, color);
                    col++;
                }
            }
        }

        return state;
    }

    // Encode a Board object into a FEN string
    public static String encode(Board board) {
        StringBuilder fen = new StringBuilder();
        Piece[][] state = board.getBoardState();

        for (int row = 0; row < 8; row++) {
            int emptyCount = 0;
            for (int col = 0; col < 8; col++) {
                Piece piece = state[row][col];
                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    String symbol = piece.getSymbol();
                    fen.append(piece.getColor() == Color.WHITE ? symbol.toUpperCase() : symbol.toLowerCase());
                }
            }
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            if (row < 7) fen.append('/');
        }
        // Side to move — defaulting to white (can add tracking later)
        String sideToMove = "w";

        // Castling rights
        StringBuilder castling = new StringBuilder();
        if (!board.hasWhiteKingMoved) {
            if (!board.hasWhiteKingsideRookMoved) castling.append("K");
            if (!board.hasWhiteQueensideRookMoved) castling.append("Q");
        }
        if (!board.hasBlackKingMoved) {
            if (!board.hasBlackKingsideRookMoved) castling.append("k");
            if (!board.hasBlackQueensideRookMoved) castling.append("q");
        }
        if (castling.length() == 0) castling.append("-");

        // En passant
        String enPassant = "-";
        if (!board.doubleForwardPawns.isEmpty()) {
            int row = board.doubleForwardPawns.get(0).getRow();
            int col = board.doubleForwardPawns.get(0).getCol();
            // en passant target is one square behind the pawn
            int epRow = (board.getPiece(board.doubleForwardPawns.get(0)).getColor() == Color.WHITE) ? row + 1 : row - 1;
            char file = (char) ('a' + col);
            enPassant = "" + file + (8 - epRow);
        }

        int halfmoveClock = 0; // You can update this logic later if needed
        int fullmoveNumber = 1;

        fen.append(" ")
                .append(sideToMove).append(" ")
                .append(castling).append(" ")
                .append(enPassant).append(" ")
                .append(halfmoveClock).append(" ")
                .append(fullmoveNumber);

        return fen.toString();
    }

    // Factory method to create pieces based on symbol
    private static Piece createPiece(char symbol, Color color) {
        switch (Character.toLowerCase(symbol)) {
            case 'p': return new Pawn(color);
            case 'r': return new Rook(color);
            case 'n': return new Knight(color);
            case 'b': return new Bishop(color);
            case 'q': return new Queen(color);
            case 'k': return new King(color);
            default: return null;
        }
    }
}

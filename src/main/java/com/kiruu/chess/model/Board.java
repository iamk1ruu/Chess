package com.kiruu.chess.model;

import com.kiruu.chess.model.pieces.Pawn;
import com.kiruu.chess.util.Move;
import com.kiruu.chess.util.Position;

import java.awt.*;
import java.util.ArrayList;
public class Board {
    private Piece[][] board = new Piece[8][8];
    private ArrayList<Position> doubleForwardPawns = new ArrayList<>();
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    public void initializeBoard() {
        System.err.println("[BUG]En passant's implementation is incorrect. Fix later.");
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row == 1)
                    board[row][col] = new Pawn(Color.BLACK);
                if (row == 6)
                    board[row][col] = new Pawn(Color.WHITE);
            }
        }
    }
    public Piece[][] getBoardState() {
        return board;
    }

    public ArrayList<Position> validMoves(Position from) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        Piece getPiece = board[from.getRow()][from.getCol()];
        try {
            Color color = getPiece.getColor();
            if (getPiece instanceof Pawn) {
                int dir = (color == Color.WHITE) ? -1 : 1;
                int startRow = (color == Color.WHITE) ? 6 : 1;
                int oneStepRow = from.getRow() + dir;
                int ldCol = (color == Color.WHITE) ? -1 : 1;
                int rdCol = (color == Color.WHITE) ? 1 : -1;
                // Normal forward position
                if (isInBounds(oneStepRow, from.getCol()) && board[oneStepRow][from.getCol()] == null) {
                    possibleMoves.add(new Position(oneStepRow, from.getCol()));
                    int twoStepRow = from.getRow() + (2 * dir);
                    if (from.getRow() == startRow && board[twoStepRow][from.getCol()] == null) {
                        possibleMoves.add(new Position(twoStepRow, from.getCol()));
                    }
                }
                // Capture left diagonal
                if (isInBounds(oneStepRow, from.getCol() + ldCol) && board[oneStepRow][from.getCol() + ldCol] != null
                        && board[oneStepRow][from.getCol() + ldCol].getColor() != getPiece.getColor()) {
                    possibleMoves.add(new Position(oneStepRow, from.getCol() + ldCol));
                }
                // Capture right diagonal
                if (isInBounds(oneStepRow, from.getCol() + rdCol) && board[oneStepRow][from.getCol() + rdCol] != null
                        && board[oneStepRow][from.getCol() + rdCol].getColor() != getPiece.getColor()) {
                    possibleMoves.add(new Position(oneStepRow, from.getCol() + rdCol));
                }

                // Check for en passant captures
                int[] adjacentCols = {from.getCol() - 1, from.getCol() + 1};
                for (int adjCol : adjacentCols) {
                    if (isInBounds(from.getRow(), adjCol)) {
                        Piece adjacentPiece = board[from.getRow()][adjCol];
                        if (adjacentPiece instanceof Pawn && adjacentPiece.getColor() != color) {
                            Position adjacentPos = new Position(from.getRow(), adjCol);
                            if (doubleForwardPawns.contains(adjacentPos)) {
                                possibleMoves.add(new Position(from.getRow() + dir, adjCol));
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            System.err.println("[GAME] Selected an empty tile.");
        }
        return possibleMoves;
    }


    public boolean validateMove(Move move) {
        ArrayList<Position> possibleMoves = validMoves(move.getFrom());
        for (Position pos : possibleMoves) {
            // =======================================
            char col = (char) ('a' + pos.getCol());
            int row = 8 - pos.getRow();
            System.out.println(String.format("[GAME] Valid Move: %c%d", col, row));

            // =======================================
            if (Position.equals(move.getTo(), pos))
                return true;
        }
        return false;
    }
    // !! May be redundant
    public Piece getPiece(Position pos) {
        return board[pos.getRow()][pos.getCol()];
    }
    public void move(Move move) {
        Position from = move.getFrom(), to = move.getTo();
        Piece movingPiece = board[from.getRow()][from.getCol()];
        try {
            // Track double pawn moves for en passant
            if (movingPiece instanceof Pawn && Math.abs(from.getRow() - to.getRow()) == 2) {
                doubleForwardPawns.clear(); // Only track the most recent double move
                doubleForwardPawns.add(to);
            } else {
                // Check for en passant capture
                if (movingPiece instanceof Pawn && from.getCol() != to.getCol() && board[to.getRow()][to.getCol()] == null) {
                    // This is an en passant capture - the captured pawn is on the same rank as the capturing pawn
                    System.out.println("[EN PASSANT EXECUTED]");
                    board[to.getRow()][to.getCol()] = movingPiece; // Move the capturing pawn
                    board[from.getRow()][to.getCol()] = null; // Remove the captured pawn (same rank as capturing pawn, same column as destination)
                    board[from.getRow()][from.getCol()] = null; // Remove the capturing pawn from its original position
                    doubleForwardPawns.clear();
                    return;
                }

                // Clear en passant opportunities after any other move
                doubleForwardPawns.clear();
            }

            // Normal move
            board[to.getRow()][to.getCol()] = movingPiece;
            board[from.getRow()][from.getCol()] = null;

            checkState(move);
        } catch (NullPointerException e) {
            System.err.println("Clicked on an empty tile.");
        }
    }

    public void checkState(Move move) {
        if (move.getTo().getRow() == 0
                && board[move.getTo().getRow()][move.getTo().getCol()] instanceof Pawn
                && board[move.getTo().getRow()][move.getTo().getCol()].getColor() == Color.WHITE) {
            System.err.println("[GAME] Promotion Possible");
        }
        if (move.getTo().getRow() == 7
                && board[move.getTo().getRow()][move.getTo().getCol()] instanceof Pawn
                && board[move.getTo().getRow()][move.getTo().getCol()].getColor() == Color.BLACK) {
            System.err.println("[GAME LOGIC]: Promotion Possible");
        }
        // For checkmate, if the king has no possible moves anymore or validMoves().isEmpty(), then we can conclude
        // a checkmate is happened.
    }
}

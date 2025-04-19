package com.kiruu.chess.model.pieces;

import com.kiruu.chess.model.Board;
import com.kiruu.chess.model.Piece;
import com.kiruu.chess.util.Position;

import java.awt.*;
import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return super.getColor() == Color.WHITE ? "P" : "p";
    }

    @Override
    public ArrayList<Position> getValidMoves(Board board, Position from) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        int dir = (getColor() == Color.WHITE) ? -1 : 1;
        int startRow = (getColor() == Color.WHITE) ? 6 : 1;
        int oneStepRow = from.getRow() + dir;

        // Forward move
        if (board.isInBounds(oneStepRow, from.getCol()) && board.getPiece(new Position(oneStepRow, from.getCol())) == null) {
            possibleMoves.add(new Position(oneStepRow, from.getCol()));

            int twoStepRow = from.getRow() + (2 * dir);
            if (from.getRow() == startRow && board.getPiece(new Position(twoStepRow, from.getCol())) == null) {
                possibleMoves.add(new Position(twoStepRow, from.getCol()));
            }
        }

        // Diagonal captures
        int[] diagCols = {from.getCol() - 1, from.getCol() + 1};
        for (int col : diagCols) {
            if (board.isInBounds(oneStepRow, col)) {
                Piece target = board.getPiece(oneStepRow, col);
                if (target != null && target.getColor() != this.getColor()) {
                    possibleMoves.add(new Position(oneStepRow, col));
                }
            }
        }

        // En Passant
        for (int adjCol : diagCols) {
            if (board.isInBounds(from.getRow(), adjCol)) {
                Piece adjacentPiece = board.getPiece(from.getRow(), adjCol);
                if (adjacentPiece instanceof Pawn && adjacentPiece.getColor() != this.getColor()) {
                    Position adjPos = new Position(from.getRow(), adjCol);
                    if (board.getDoubleForwardPawns().contains(adjPos)) {
                        possibleMoves.add(new Position(from.getRow() + dir, adjCol));
                    }
                }
            }
        }

        return possibleMoves;
    }
}

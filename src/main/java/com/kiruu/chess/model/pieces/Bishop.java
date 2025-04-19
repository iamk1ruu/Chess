package com.kiruu.chess.model.pieces;

import com.kiruu.chess.model.Board;
import com.kiruu.chess.model.Piece;
import com.kiruu.chess.util.Position;

import java.awt.*;
import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
    }

    @Override
    public String getSymbol() {
        return "";
    }

    public ArrayList<Position> getValidMoves(Board board, Position from) {
        return null;
    }
}

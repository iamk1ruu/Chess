package com.kiruu.chess.model.pieces;

import com.kiruu.chess.model.Board;
import com.kiruu.chess.model.Piece;
import com.kiruu.chess.util.Position;

import java.awt.*;
import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(Color color) {
        super(color);
    }
    public ArrayList<Position> getValidMoves(Board board, Position from) {
        return null;
    }
    @Override
    public String getSymbol() {
        return "";
    }
}
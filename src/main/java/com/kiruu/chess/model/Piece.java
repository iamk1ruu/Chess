package com.kiruu.chess.model;

import com.kiruu.chess.util.Position;
import java.util.ArrayList;
import java.awt.*;

public abstract class Piece {
    private Color color;
    public Piece(Color color) {this.color = color;}
    public Color getColor() {return color;}
    public abstract String getSymbol();
    public abstract ArrayList<Position> getValidMoves(Board board, Position from);
}

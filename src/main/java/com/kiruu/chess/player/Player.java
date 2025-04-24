package com.kiruu.chess.player;

import com.kiruu.chess.model.Board;
import com.kiruu.chess.util.Move;
import com.kiruu.chess.util.Position;

import java.awt.*;
import java.io.IOException;

public abstract class Player {
    private Color color;
    private String name;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }
    public String getName() {return name;}
    public Color getColor() {return color;}
    public abstract Move makeMove(String fen) throws IOException;
}

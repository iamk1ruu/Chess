package com.kiruu.chess.player.types;

import com.kiruu.chess.model.Board;
import com.kiruu.chess.player.Player;
import com.kiruu.chess.util.Move;

import java.awt.*;

public class HumanPlayer extends Player {
    public HumanPlayer(String name, Color color) {
        super(name, color);
    }
    @Override
    public Move makeMove(String fen) {
        return null;
    }
}

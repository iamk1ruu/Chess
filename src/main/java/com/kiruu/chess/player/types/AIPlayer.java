package com.kiruu.chess.player.types;

import com.kiruu.chess.model.Board;
import com.kiruu.chess.player.Player;
import com.kiruu.chess.util.Move;

import java.awt.*;

public class AIPlayer extends Player {
    private DIFFICULTY difficulty;
    public enum DIFFICULTY {EASY, HARD}

    public AIPlayer(String name, Color color, DIFFICULTY difficulty) {
        super(name, color);
        this.difficulty = difficulty;
    }

    // ==== IMPLEMENT LATER =========
    @Override
    public Move makeMove(Board board) {
        return null;
    }
}

package com.kiruu.chess.player.types;

import com.kiruu.chess.ai.StockfishClient;
import com.kiruu.chess.ai.StockfishDifficulty;
import com.kiruu.chess.model.Board;
import com.kiruu.chess.player.Player;
import com.kiruu.chess.util.FENParser;
import com.kiruu.chess.util.Move;
import com.kiruu.chess.util.MoveParser;

import java.awt.*;
import java.io.IOException;

public class AIPlayer extends Player {
    public enum DIFFICULTY {EASY, HARD}

    private DIFFICULTY difficulty;
    private StockfishClient client;

    public AIPlayer(String name, Color color, DIFFICULTY difficulty) {
        super(name, color);
        this.difficulty = difficulty;
        this.client = new StockfishClient();
    }

    private StockfishDifficulty mapDifficulty() {
        return switch (this.difficulty) {
            case EASY -> StockfishDifficulty.EASY;
            case HARD -> StockfishDifficulty.HARD;
        };
    }

    @Override
    public Move makeMove(String fen) throws IOException {
        if (client.startEngine("stockfish/stockfish.exe")) {
            String bestMove = client.getBestMove(fen, mapDifficulty());
            client.stopEngine();
            return MoveParser.parseMove(bestMove);
        }
        return null;
    }

    public Move takeOver(String fen) throws IOException {
        if (client.startEngine("stockfish/stockfish.exe")) {
            String bestMove = client.getBestMove(fen, mapDifficulty());
            client.stopEngine();
            return MoveParser.parseMove(bestMove);
        }
        return null;
    }
}

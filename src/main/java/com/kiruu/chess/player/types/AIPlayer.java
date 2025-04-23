package com.kiruu.chess.player.types;

import com.kiruu.chess.ai.StockfishClient;
import com.kiruu.chess.model.Board;
import com.kiruu.chess.player.Player;
import com.kiruu.chess.util.FENParser;
import com.kiruu.chess.util.Move;
import com.kiruu.chess.util.MoveParser;

import java.awt.*;
import java.io.IOException;

public class AIPlayer extends Player {
    private DIFFICULTY difficulty;

    public enum DIFFICULTY {EASY, HARD}

    private StockfishClient client;

    public AIPlayer(String name, Color color, DIFFICULTY difficulty) {
        super(name, color);
        this.difficulty = difficulty;
        this.client = new StockfishClient();
    }

    // ==== IMPLEMENT LATER =========
    @Override
    public Move makeMove(Board board) throws IOException {
        StockfishClient stockfish = new StockfishClient();
        if (stockfish.startEngine("stockfish/stockfish.exe")) {
            String fen = FENParser.encode(board);
            System.out.println(fen);
            String bestMove = stockfish.getBestMove(fen, 500);  // 500ms to decide
            stockfish.stopEngine();
            return MoveParser.parseMove(bestMove);
        }
        return null;
    }
}
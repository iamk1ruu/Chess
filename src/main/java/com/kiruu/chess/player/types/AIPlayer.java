package com.kiruu.chess.player.types;

import com.github.tjake.jlama.safetensors.tokenizer.BPETokenizer;
import com.kiruu.chess.ai.LLMClient;
import com.kiruu.chess.model.Board;
import com.kiruu.chess.player.Player;
import com.kiruu.chess.util.FENParser;
import com.kiruu.chess.util.Move;
import com.kiruu.chess.util.Position;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AIPlayer extends Player {
    private DIFFICULTY difficulty;
    public enum DIFFICULTY {EASY, HARD}
    private LLMClient client;
    public AIPlayer(String name, Color color, DIFFICULTY difficulty) {
        super(name, color);
        this.difficulty = difficulty;
        this.client = new LLMClient();
    }

    // ==== IMPLEMENT LATER =========
    @Override
    public Move makeMove(Board board) throws IOException {
        System.out.println(client.request("HELLO!"));
        return null;
    }

    public static void main(String[] args) {
        try {
            AIPlayer a = new AIPlayer("", Color.BLACK, DIFFICULTY.EASY);
            a.makeMove(new Board());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
        /*
        String[] moves = client.request(FENParser.encode(board)).split(" ");
        return new Move(Position.getNotation(moves[0]), Position.getNotation(moves[1]));
    }


    public static void main(String[] args) {
        try {
            AIPlayer test = new AIPlayer("AI", Color.BLACK, DIFFICULTY.EASY);
            Board board = new Board();
            board.setBoardState(FENParser.decode("8/8/8/4k3/8/8/4Q3/4K2q w - - 0 1\n"));
            Move testMove = test.makeMove(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

         */

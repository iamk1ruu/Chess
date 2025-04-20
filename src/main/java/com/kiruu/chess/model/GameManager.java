package com.kiruu.chess.model;

import com.kiruu.chess.util.Move;
import com.kiruu.chess.util.Position;
import com.kiruu.chess.player.Player;

import java.awt.*;
import java.util.ArrayList;

public class GameManager {
    private Color currentTurn = Color.WHITE;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Board board;

    public GameManager(Player player1, Player player2) {
        if (player1.getColor() == Color.WHITE) {
            whitePlayer = player1;
            blackPlayer = player2;
        } else {
            whitePlayer = player2;
            blackPlayer = player1;
        }

        this.board = new Board();
        board.initializeBoard();
    }

    public Player getCurrentPlayer() {
        return currentTurn == Color.WHITE ? whitePlayer : blackPlayer;
    }

    public boolean makeMove(Move move, Player p) {
        if (p != getCurrentPlayer()) {
            System.err.println("Invalid move: it's not your turn.");
            return false;
        }

        Position from = move.getFrom();
        Piece piece = getPiece(from);

        if (piece == null) {
            System.err.println("Invalid move: no piece at the source.");
            return false;
        }

        if (board.validateMove(move)) {
            board.move(move);
            switchTurn();
            System.err.println("[DEBUG] Switched Turn");
            return true;
        }

        System.err.println("[DEBUG] Invalid move: move is not legal.");
        return false;
    }

    private void switchTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public Piece[][] getBoardState() {
        return board.getBoardState();
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public ArrayList<Position> getPossibleMoves(String fxid) {
        return board.validMoves(Position.getNotation(fxid));
    }

    public Piece getPiece(Position pos) {
        return board.getPiece(pos);
    }
}

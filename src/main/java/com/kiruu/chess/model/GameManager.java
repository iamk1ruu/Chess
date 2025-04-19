package com.kiruu.chess.model;

import com.kiruu.chess.util.Move;
import com.kiruu.chess.util.Position;
import com.kiruu.chess.player.Player;

import java.awt.*;
import java.util.ArrayList;

public class GameManager {
    private Color currentTurn = Color.WHITE;
    private final Player player;
    private final Board board;

    public GameManager(Player player) {
        this.player = player;
        this.board = new Board();
        board.initializeBoard();
    }

    public boolean makeMove(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Piece piece = board.getBoardState()[from.getRow()][from.getCol()];

        if (piece == null || piece.getColor() != currentTurn)
            return false;

        if (board.validateMove(move)) {
            board.move(move);
            switchTurn();
            return true;
        }
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
    // !! May be redundant.
    public ArrayList<Position> getPossibleMoves(String fxid) {
        Position pos = Position.getNotation(fxid);
        return board.validMoves(pos);
    }

}

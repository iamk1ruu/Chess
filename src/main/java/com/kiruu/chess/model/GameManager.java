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
    private int state = -1;
    private boolean gameOver = false;
    private Color winner = null;
    // State constants
    public static final int NO_SPECIAL_STATE = -1;
    public static final int WHITE_PROMOTION = 1;
    public static final int BLACK_PROMOTION = 2;
    public static final int WHITE_IN_CHECK = 3;
    public static final int BLACK_IN_CHECK = 4;
    public static final int WHITE_CHECKMATE = 5;
    public static final int BLACK_CHECKMATE = 6;

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
            state = board.checkState(move);

            // Only switch turn if promotion isn't happening
            if (state != WHITE_PROMOTION && state != BLACK_PROMOTION) {
                switchTurn();
                System.out.println("[DEBUG] Switched Turn");
            } else {
                System.out.println("[DEBUG] Promotion pending, turn not switched yet");
            }
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

    public void promotePawn(Position position, Piece promotedPiece) {
        Piece[][] boardState = board.getBoardState();
        boardState[position.getRow()][position.getCol()] = promotedPiece;

        switchTurn();
        System.out.println("[DEBUG] Pawn promoted and turn switched");

        state = NO_SPECIAL_STATE;
    }

    public int getState() {
        return state;
    }

    public ArrayList<Position> getPossibleMoves(String fxid) {
        return board.validMoves(Position.getNotation(fxid));
    }

    public Piece getPiece(Position pos) {
        return board.getPiece(pos);
    }

    public Board getBoard() {
        return board;
    }
    public boolean isGameOver() {
        return gameOver;
    }
    public Color getWinner() {
        return winner;
    }
    public void setGameOver(Color winner) {
        System.err.println("[GAME] " + (Color.black == winner ? "BLACK" : "WHITE" )+ " Wins!");
        gameOver = true;
        this.winner = winner;
    }
}
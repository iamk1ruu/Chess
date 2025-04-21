package com.kiruu.chess.model;

import com.kiruu.chess.model.pieces.*;
import com.kiruu.chess.util.FENParser;
import com.kiruu.chess.util.Move;
import com.kiruu.chess.util.Position;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    private Piece[][] board = new Piece[8][8];
    private ArrayList<Position> doubleForwardPawns = new ArrayList<>();
    private boolean hasWhiteKingMoved = false;
    private boolean hasBlackKingMoved = false;
    private boolean hasWhiteKingsideRookMoved = false;
    private boolean hasWhiteQueensideRookMoved = false;
    private boolean hasBlackKingsideRookMoved = false;
    private boolean hasBlackQueensideRookMoved = false;

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public void initializeBoard() {

        Piece[] backRowBlack = {
                new Rook(Color.BLACK), new Knight(Color.BLACK), new Bishop(Color.BLACK), new Queen(Color.BLACK),
                new King(Color.BLACK), new Bishop(Color.BLACK), new Knight(Color.BLACK), new Rook(Color.BLACK)
        };
        Piece[] backRowWhite = {
                new Rook(Color.WHITE), new Knight(Color.WHITE), new Bishop(Color.WHITE), new Queen(Color.WHITE),
                new King(Color.WHITE), new Bishop(Color.WHITE), new Knight(Color.WHITE), new Rook(Color.WHITE)
        };

        for (int col = 0; col < 8; col++) {
            board[0][col] = backRowBlack[col];
            board[7][col] = backRowWhite[col];
        }

        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn(Color.BLACK);
            board[6][col] = new Pawn(Color.WHITE);
        }

        // Initialize remaining cells to null
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }



    }

    public Piece[][] getBoardState() {
        return board;
    }
    // ==== CHECK FOR VULNERABILITY
    public void setBoardState(Piece[][] board) {
        this.board = board;
    }

    public ArrayList<Position> validMoves(Position from) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        if (!isInBounds(from.getRow(), from.getCol())) {
            return possibleMoves;
        }

        Piece getPiece = board[from.getRow()][from.getCol()];
        if (getPiece == null) {
            return possibleMoves;
        }

        Color color = getPiece.getColor();
        if (getPiece instanceof Pawn) {
            int dir = (color == Color.WHITE) ? -1 : 1;
            int startRow = (color == Color.WHITE) ? 6 : 1;
            int oneStepRow = from.getRow() + dir;
            int ldCol = from.getCol() - 1;
            int rdCol = from.getCol() + 1;

            if (isInBounds(oneStepRow, from.getCol()) && board[oneStepRow][from.getCol()] == null) {
                possibleMoves.add(new Position(oneStepRow, from.getCol()));
                int twoStepRow = from.getRow() + (2 * dir);
                if (from.getRow() == startRow && isInBounds(twoStepRow, from.getCol()) &&
                        board[twoStepRow][from.getCol()] == null) {
                    possibleMoves.add(new Position(twoStepRow, from.getCol()));
                }
            }

            if (isInBounds(oneStepRow, ldCol) && board[oneStepRow][ldCol] != null &&
                    board[oneStepRow][ldCol].getColor() != color) {
                possibleMoves.add(new Position(oneStepRow, ldCol));
            }

            if (isInBounds(oneStepRow, rdCol) && board[oneStepRow][rdCol] != null &&
                    board[oneStepRow][rdCol].getColor() != color) {
                possibleMoves.add(new Position(oneStepRow, rdCol));
            }

            // En passant
            int[] adjacentCols = {from.getCol() - 1, from.getCol() + 1};
            for (int adjCol : adjacentCols) {
                if (isInBounds(from.getRow(), adjCol)) {
                    Piece adjacentPiece = board[from.getRow()][adjCol];
                    if (adjacentPiece instanceof Pawn && adjacentPiece.getColor() != color) {
                        Position adjacentPos = new Position(from.getRow(), adjCol);
                        for (Position doubleMovePos : doubleForwardPawns) {
                            if (Position.equals(doubleMovePos, adjacentPos)) {
                                possibleMoves.add(new Position(from.getRow() + dir, adjCol));
                                break;
                            }
                        }
                    }
                }
            }
        } else if (getPiece instanceof Rook) {
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            for (int[] dir : directions) {
                int r = from.getRow() + dir[0];
                int c = from.getCol() + dir[1];

                while (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null) {
                        possibleMoves.add(new Position(r, c));
                    } else {
                        if (occupyingPiece.getColor() != getPiece.getColor()) {
                            possibleMoves.add(new Position(r, c));
                        }
                        break;
                    }
                    r += dir[0];
                    c += dir[1];
                }
            }
        } else if (getPiece instanceof Bishop) {
            int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

            for (int[] dir : directions) {
                int r = from.getRow() + dir[0];
                int c = from.getCol() + dir[1];

                while (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null) {
                        possibleMoves.add(new Position(r, c));
                    } else {
                        if (occupyingPiece.getColor() != getPiece.getColor()) {
                            possibleMoves.add(new Position(r, c));
                        }
                        break;
                    }
                    r += dir[0];
                    c += dir[1];
                }
            }
        } else if (getPiece instanceof Queen) {
            int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            for (int[] dir : directions) {
                int r = from.getRow() + dir[0];
                int c = from.getCol() + dir[1];

                while (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null) {
                        possibleMoves.add(new Position(r, c));
                    } else {
                        if (occupyingPiece.getColor() != getPiece.getColor()) {
                            possibleMoves.add(new Position(r, c));
                        }
                        break;
                    }
                    r += dir[0];
                    c += dir[1];
                }
            }
        } else if (getPiece instanceof Knight) {
            int[][] knightMoves = {
                    {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                    {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
            };

            for (int[] move : knightMoves) {
                int r = from.getRow() + move[0];
                int c = from.getCol() + move[1];

                if (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null || occupyingPiece.getColor() != getPiece.getColor()) {
                        possibleMoves.add(new Position(r, c));
                    }
                }
            }
        } else if (getPiece instanceof King) {
            int[][] directions = {
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                    {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
            };
            for (int[] dir : directions) {
                int r = from.getRow() + dir[0];
                int c = from.getCol() + dir[1];

                if (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null || occupyingPiece.getColor() != getPiece.getColor()) {
                        possibleMoves.add(new Position(r, c));
                    }
                }
            }

            // Castling
            if (getPiece.getColor() == Color.WHITE) {
                if (!hasWhiteKingMoved && !hasWhiteKingsideRookMoved &&
                        board[7][5] == null && board[7][6] == null &&
                        !isSquareAttacked(new Position(7, 4), Color.BLACK) &&
                        !isSquareAttacked(new Position(7, 5), Color.BLACK) &&
                        !isSquareAttacked(new Position(7, 6), Color.BLACK)) {
                    possibleMoves.add(new Position(7, 6));
                }
                if (!hasWhiteKingMoved && !hasWhiteQueensideRookMoved &&
                        board[7][1] == null && board[7][2] == null && board[7][3] == null &&
                        !isSquareAttacked(new Position(7, 4), Color.BLACK) &&
                        !isSquareAttacked(new Position(7, 3), Color.BLACK) &&
                        !isSquareAttacked(new Position(7, 2), Color.BLACK)) {
                    possibleMoves.add(new Position(7, 2));
                }
            } else {
                if (!hasBlackKingMoved && !hasBlackKingsideRookMoved &&
                        board[0][5] == null && board[0][6] == null &&
                        !isSquareAttacked(new Position(0, 4), Color.WHITE) &&
                        !isSquareAttacked(new Position(0, 5), Color.WHITE) &&
                        !isSquareAttacked(new Position(0, 6), Color.WHITE)) {
                    possibleMoves.add(new Position(0, 6));
                }
                if (!hasBlackKingMoved && !hasBlackQueensideRookMoved &&
                        board[0][1] == null && board[0][2] == null && board[0][3] == null &&
                        !isSquareAttacked(new Position(0, 4), Color.WHITE) &&
                        !isSquareAttacked(new Position(0, 3), Color.WHITE) &&
                        !isSquareAttacked(new Position(0, 2), Color.WHITE)) {
                    possibleMoves.add(new Position(0, 2));
                }
            }
        }

        // Filter out moves that would leave the king in check
        ArrayList<Position> legalMoves = new ArrayList<>();
        for (Position to : possibleMoves) {
            // Save current state
            Piece capturedPiece = board[to.getRow()][to.getCol()];
            // Make the move
            board[to.getRow()][to.getCol()] = getPiece;
            board[from.getRow()][from.getCol()] = null;

            // Check if king is in check after the move
            if (!isInCheck(color)) {
                legalMoves.add(to);
            }

            // Restore the board
            board[from.getRow()][from.getCol()] = getPiece;
            board[to.getRow()][to.getCol()] = capturedPiece;
        }

        return legalMoves;
    }

    private boolean isSquareAttacked(Position square, Color attackingColor) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.getColor() == attackingColor) {
                    Position from = new Position(r, c);

                    // Get raw moves without checking for check to avoid infinite recursion
                    ArrayList<Position> rawMoves = getRawMoves(from);

                    for (Position move : rawMoves) {
                        if (Position.equals(move, square)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // Get moves without checking if they put the king in check
    private ArrayList<Position> getRawMoves(Position from) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        if (!isInBounds(from.getRow(), from.getCol())) {
            return possibleMoves;
        }

        Piece getPiece = board[from.getRow()][from.getCol()];
        if (getPiece == null) {
            return possibleMoves;
        }

        Color color = getPiece.getColor();
        if (getPiece instanceof Pawn) {
            int dir = (color == Color.WHITE) ? -1 : 1;
            int startRow = (color == Color.WHITE) ? 6 : 1;
            int oneStepRow = from.getRow() + dir;
            int ldCol = from.getCol() - 1;
            int rdCol = from.getCol() + 1;

            if (isInBounds(oneStepRow, from.getCol()) && board[oneStepRow][from.getCol()] == null) {
                possibleMoves.add(new Position(oneStepRow, from.getCol()));
                int twoStepRow = from.getRow() + (2 * dir);
                if (from.getRow() == startRow && isInBounds(twoStepRow, from.getCol()) &&
                        board[twoStepRow][from.getCol()] == null) {
                    possibleMoves.add(new Position(twoStepRow, from.getCol()));
                }
            }

            if (isInBounds(oneStepRow, ldCol) && board[oneStepRow][ldCol] != null &&
                    board[oneStepRow][ldCol].getColor() != color) {
                possibleMoves.add(new Position(oneStepRow, ldCol));
            }

            if (isInBounds(oneStepRow, rdCol) && board[oneStepRow][rdCol] != null &&
                    board[oneStepRow][rdCol].getColor() != color) {
                possibleMoves.add(new Position(oneStepRow, rdCol));
            }

            // En passant
            int[] adjacentCols = {from.getCol() - 1, from.getCol() + 1};
            for (int adjCol : adjacentCols) {
                if (isInBounds(from.getRow(), adjCol)) {
                    Piece adjacentPiece = board[from.getRow()][adjCol];
                    if (adjacentPiece instanceof Pawn && adjacentPiece.getColor() != color) {
                        Position adjacentPos = new Position(from.getRow(), adjCol);
                        for (Position doubleMovePos : doubleForwardPawns) {
                            if (Position.equals(doubleMovePos, adjacentPos)) {
                                possibleMoves.add(new Position(from.getRow() + dir, adjCol));
                                break;
                            }
                        }
                    }
                }
            }
        } else if (getPiece instanceof Rook) {
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            for (int[] dir : directions) {
                int r = from.getRow() + dir[0];
                int c = from.getCol() + dir[1];

                while (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null) {
                        possibleMoves.add(new Position(r, c));
                    } else {
                        if (occupyingPiece.getColor() != getPiece.getColor()) {
                            possibleMoves.add(new Position(r, c));
                        }
                        break;
                    }
                    r += dir[0];
                    c += dir[1];
                }
            }
        } else if (getPiece instanceof Bishop) {
            int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

            for (int[] dir : directions) {
                int r = from.getRow() + dir[0];
                int c = from.getCol() + dir[1];

                while (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null) {
                        possibleMoves.add(new Position(r, c));
                    } else {
                        if (occupyingPiece.getColor() != getPiece.getColor()) {
                            possibleMoves.add(new Position(r, c));
                        }
                        break;
                    }
                    r += dir[0];
                    c += dir[1];
                }
            }
        } else if (getPiece instanceof Queen) {
            int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            for (int[] dir : directions) {
                int r = from.getRow() + dir[0];
                int c = from.getCol() + dir[1];

                while (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null) {
                        possibleMoves.add(new Position(r, c));
                    } else {
                        if (occupyingPiece.getColor() != getPiece.getColor()) {
                            possibleMoves.add(new Position(r, c));
                        }
                        break;
                    }
                    r += dir[0];
                    c += dir[1];
                }
            }
        } else if (getPiece instanceof Knight) {
            int[][] knightMoves = {
                    {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                    {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
            };

            for (int[] move : knightMoves) {
                int r = from.getRow() + move[0];
                int c = from.getCol() + move[1];

                if (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null || occupyingPiece.getColor() != getPiece.getColor()) {
                        possibleMoves.add(new Position(r, c));
                    }
                }
            }
        } else if (getPiece instanceof King) {
            int[][] directions = {
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                    {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
            };
            for (int[] dir : directions) {
                int r = from.getRow() + dir[0];
                int c = from.getCol() + dir[1];

                if (isInBounds(r, c)) {
                    Piece occupyingPiece = board[r][c];
                    if (occupyingPiece == null || occupyingPiece.getColor() != getPiece.getColor()) {
                        possibleMoves.add(new Position(r, c));
                    }
                }
            }
        }

        return possibleMoves;
    }

    public boolean validateMove(Move move) {
        ArrayList<Position> possibleMoves = validMoves(move.getFrom());
        for (Position pos : possibleMoves) {
            if (Position.equals(move.getTo(), pos))
                return true;
        }
        return false;
    }

    public Piece getPiece(Position pos) {
        if (!isInBounds(pos.getRow(), pos.getCol())) {
            return null;
        }
        return board[pos.getRow()][pos.getCol()];
    }

    public void move(Move move) {
        Position from = move.getFrom(), to = move.getTo();
        Piece movingPiece = board[from.getRow()][from.getCol()];

        if (movingPiece == null) {
            return;
        }

        if (movingPiece instanceof Pawn) {
            if (Math.abs(from.getRow() - to.getRow()) == 2) {
                doubleForwardPawns.clear();
                doubleForwardPawns.add(to);
            } else {
                if (from.getCol() != to.getCol() && board[to.getRow()][to.getCol()] == null) {
                    // En passant
                    board[to.getRow()][to.getCol()] = movingPiece;
                    board[from.getRow()][to.getCol()] = null;
                    board[from.getRow()][from.getCol()] = null;
                    doubleForwardPawns.clear();
                    return;
                }
                doubleForwardPawns.clear();
            }
        }

        if (movingPiece instanceof Rook) {
            if (Position.equals(from, new Position(7, 7))) {
                hasWhiteKingsideRookMoved = true;
            } else if (Position.equals(from, new Position(7, 0))) {
                hasWhiteQueensideRookMoved = true;
            } else if (Position.equals(from, new Position(0, 7))) {
                hasBlackKingsideRookMoved = true;
            } else if (Position.equals(from, new Position(0, 0))) {
                hasBlackQueensideRookMoved = true;
            }
        }

        if (movingPiece instanceof King) {
            Color color = movingPiece.getColor();

            if (color == Color.WHITE) {
                hasWhiteKingMoved = true;
                if (Position.equals(from, new Position(7, 4)) && Position.equals(to, new Position(7, 6))) {
                    // Kingside castling
                    board[7][5] = board[7][7];
                    board[7][7] = null;
                } else if (Position.equals(from, new Position(7, 4)) && Position.equals(to, new Position(7, 2))) {
                    // Queenside castling
                    board[7][3] = board[7][0];
                    board[7][0] = null;
                }
            } else {
                hasBlackKingMoved = true;
                if (Position.equals(from, new Position(0, 4)) && Position.equals(to, new Position(0, 6))) {
                    // Kingside castling
                    board[0][5] = board[0][7];
                    board[0][7] = null;
                } else if (Position.equals(from, new Position(0, 4)) && Position.equals(to, new Position(0, 2))) {
                    // Queenside castling
                    board[0][3] = board[0][0];
                    board[0][0] = null;
                }
            }
        }

        board[to.getRow()][to.getCol()] = movingPiece;
        board[from.getRow()][from.getCol()] = null;
    }


    public boolean isInCheck(Color color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false; // Safety check

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.getColor() != color) {
                    ArrayList<Position> moves = getRawMoves(new Position(r, c));
                    for (Position move : moves) {
                        if (Position.equals(move, kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public Position findKing(Color color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p instanceof King && p.getColor() == color) {
                    return new Position(r, c);
                }
            }
        }
        return null;
    }

    public int checkState(Move move) {
        if (move.getTo().getRow() == 0
                && board[move.getTo().getRow()][move.getTo().getCol()] instanceof Pawn
                && board[move.getTo().getRow()][move.getTo().getCol()].getColor() == Color.WHITE) {
            return 1;
        }
        if (move.getTo().getRow() == 7
                && board[move.getTo().getRow()][move.getTo().getCol()] instanceof Pawn
                && board[move.getTo().getRow()][move.getTo().getCol()].getColor() == Color.BLACK) {
            return 2;
        }
        System.err.println("[DEBUG] Checking stalemate...");
        if (isStalemate(Color.WHITE)) return 7;
        if (isStalemate(Color.BLACK)) return 8;
        System.err.println("[DEBUG] Checking checkmate...");
        if (isCheckmate(Color.WHITE)) return 5;
        if (isCheckmate(Color.BLACK)) return 6;
        if (isInCheck(Color.WHITE)) return 3;
        if (isInCheck(Color.BLACK)) return 4;
        System.err.println("[DEBUG] No returned value.");
        return -1;
    }

    public boolean isCheckmate(Color color) {
        if (!isInCheck(color)) return false;
        System.err.println("[DEBUG] Checking state...");
        // Generate all legal moves for the color
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.getColor() == color) {
                    ArrayList<Position> moves = validMoves(new Position(r, c));
                    if (!moves.isEmpty()) {
                        return false; // Found at least one legal move
                    }
                }
            }
        }

        return true; // No legal moves found
    }

    public ArrayList<Move> generateAllLegalMoves(Color color) {
        ArrayList<Move> legalMoves = new ArrayList<>();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p != null && p.getColor() == color) {
                    Position from = new Position(r, c);
                    ArrayList<Position> destinations = validMoves(from);

                    for (Position to : destinations) {
                        legalMoves.add(new Move(from, to));
                    }
                }
            }
        }

        return legalMoves;
    }
    public boolean isStalemate(Color color) {
        if (isInCheck(color)) return false; // Can't be stalemate if in check

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.getColor() == color) {
                    ArrayList<Position> moves = validMoves(new Position(r, c));
                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
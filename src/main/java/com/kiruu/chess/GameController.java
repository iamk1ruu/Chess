package com.kiruu.chess;

import com.kiruu.chess.model.GameManager;
import com.kiruu.chess.model.Piece;
import com.kiruu.chess.model.pieces.*;
import com.kiruu.chess.player.Player;
import com.kiruu.chess.player.types.AIPlayer;
import com.kiruu.chess.player.types.HumanPlayer;
import com.kiruu.chess.util.Move;
import com.kiruu.chess.util.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.awt.Color;
import static com.kiruu.chess.player.types.AIPlayer.DIFFICULTY.EASY;
import static com.kiruu.chess.player.types.AIPlayer.DIFFICULTY.HARD;

public class GameController {
    private Player player, opponent;
    private GameManager gm;
    private boolean isClickedOnce = false;
    private Position firstPos, secondPos;
    private int GAMEMODE; // -1 for 2 players, 0 for AI, and 1 for Multiplayer
    @FXML
    private Label LABEL_TURN;

    @FXML
    private Button a1, b1, c1, d1, e1, f1, g1, h1,
            a2, b2, c2, d2, e2, f2, g2, h2,
            a3, b3, c3, d3, e3, f3, g3, h3,
            a4, b4, c4, d4, e4, f4, g4, h4,
            a5, b5, c5, d5, e5, f5, g5, h5,
            a6, b6, c6, d6, e6, f6, g6, h6,
            a7, b7, c7, d7, e7, f7, g7, h7,
            a8, b8, c8, d8, e8, f8, g8, h8;

    private final Button[][] buttons = new Button[8][8];

    public GameController() {
        // For now, the default game state is -1
        GAMEMODE = -1;
        switch (GAMEMODE) {
            case -1:
                player = new HumanPlayer("Player 1", Color.WHITE);
                opponent = new HumanPlayer("Player 2", Color.BLACK);
                break;
            case 0:
                player = new HumanPlayer("Player", Color.WHITE);
                opponent = new AIPlayer("Player", Color.BLACK, HARD);
                break;
        }
        gm = new GameManager(player, opponent);
    }
    public void initialize() {
        populateButtonMatrix();
        updateBoardUI();
    }

    public void populateButtonMatrix() {
        buttons[0][0] = a8;
        buttons[0][1] = b8;
        buttons[0][2] = c8;
        buttons[0][3] = d8;
        buttons[0][4] = e8;
        buttons[0][5] = f8;
        buttons[0][6] = g8;
        buttons[0][7] = h8;
        buttons[1][0] = a7;
        buttons[1][1] = b7;
        buttons[1][2] = c7;
        buttons[1][3] = d7;
        buttons[1][4] = e7;
        buttons[1][5] = f7;
        buttons[1][6] = g7;
        buttons[1][7] = h7;
        buttons[2][0] = a6;
        buttons[2][1] = b6;
        buttons[2][2] = c6;
        buttons[2][3] = d6;
        buttons[2][4] = e6;
        buttons[2][5] = f6;
        buttons[2][6] = g6;
        buttons[2][7] = h6;
        buttons[3][0] = a5;
        buttons[3][1] = b5;
        buttons[3][2] = c5;
        buttons[3][3] = d5;
        buttons[3][4] = e5;
        buttons[3][5] = f5;
        buttons[3][6] = g5;
        buttons[3][7] = h5;
        buttons[4][0] = a4;
        buttons[4][1] = b4;
        buttons[4][2] = c4;
        buttons[4][3] = d4;
        buttons[4][4] = e4;
        buttons[4][5] = f4;
        buttons[4][6] = g4;
        buttons[4][7] = h4;
        buttons[5][0] = a3;
        buttons[5][1] = b3;
        buttons[5][2] = c3;
        buttons[5][3] = d3;
        buttons[5][4] = e3;
        buttons[5][5] = f3;
        buttons[5][6] = g3;
        buttons[5][7] = h3;
        buttons[6][0] = a2;
        buttons[6][1] = b2;
        buttons[6][2] = c2;
        buttons[6][3] = d2;
        buttons[6][4] = e2;
        buttons[6][5] = f2;
        buttons[6][6] = g2;
        buttons[6][7] = h2;
        buttons[7][0] = a1;
        buttons[7][1] = b1;
        buttons[7][2] = c1;
        buttons[7][3] = d1;
        buttons[7][4] = e1;
        buttons[7][5] = f1;
        buttons[7][6] = g1;
        buttons[7][7] = h1;
    }

    public void updateBoardUI() {
        Piece[][] state = gm.getBoardState();
        clearTiles();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece currentPiece = state[i][j];
                if (currentPiece != null) {
                    String colorPrefix = currentPiece.getColor() == Color.WHITE ? "w" : "b";
                    String pieceName = "";

                    if (currentPiece instanceof Pawn) {
                        pieceName = "p";
                    } else if (currentPiece instanceof Rook) {
                        pieceName = "r";
                    } else if (currentPiece instanceof Knight) {
                        pieceName = "n";
                    } else if (currentPiece instanceof Bishop) {
                        pieceName = "b";
                    } else if (currentPiece instanceof Queen) {
                        pieceName = "q";
                    } else if (currentPiece instanceof King) {
                        pieceName = "k";
                    }

                    String imagePath = "/com/kiruu/chess/img/" + colorPrefix + pieceName + ".png";
                    Image img = new Image(getClass().getResourceAsStream(imagePath));
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(50);
                    imgView.setFitHeight(50);
                    buttons[i][j].setGraphic(imgView);
                    buttons[i][j].setText(""); // Remove text in case previously set
                } else {
                    buttons[i][j].setGraphic(null);
                    buttons[i][j].setText("");
                }
            }
        }
    }

    // === FIX THE BUTTON SIZE TO AVOID MISCLICKS
    public void highlightTiles(ArrayList<Position> pos, Position origin) {
        buttons[origin.getRow()][origin.getCol()].setStyle(
                "-fx-background-color: wheat; " +
                        "-fx-effect: dropshadow(gaussian, rgba(244, 164, 96, 0.75), 30, 0.6, 0, 0);"
        );
        for (Position p : pos) {
            buttons[p.getRow()][p.getCol()].setStyle(
                    "-fx-background-color: saddlebrown; " +
                            "-fx-effect: dropshadow(gaussian, rgba(244, 164, 96, 0.75), 30, 0.6, 0, 0);"
            );
        }
    }

    public void clearTiles() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j].setStyle("-fx-background-color: transparent;");
            }
        }
    }

    public void handleClick(ActionEvent e) {
        String getFXID = ((Node) e.getSource()).getId();

        switch (GAMEMODE) {
            case -1:
                if (!isClickedOnce) {
                    firstPos = Position.getNotation(getFXID);
                    // ======= TEMPORARY FAILSAFE APPROACH
                    if (gm.getBoardState()[firstPos.getRow()][firstPos.getCol()] == null)
                        return;
                    if (gm.getPiece(firstPos).getColor() != gm.getCurrentTurn()
                            && gm.getPiece(firstPos) != null)
                        return;
                    highlightTiles(gm.getPossibleMoves(getFXID), firstPos);
                    isClickedOnce = true;
                } else {
                    secondPos = Position.getNotation(getFXID);
                    Player current = gm.getCurrentTurn() == player.getColor() ? player : opponent;
                    gm.makeMove(new Move(firstPos, secondPos), current);
                    updateBoardUI();
                    isClickedOnce = false;
                }
                break;
            case 0:
                // This snippet prevents the current player in this session to avoid making moves
                if (player != gm.getCurrentPlayer())
                    return;
        }

    }
}

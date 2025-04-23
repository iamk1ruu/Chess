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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;

import static com.kiruu.chess.player.types.AIPlayer.DIFFICULTY.EASY;
import static com.kiruu.chess.player.types.AIPlayer.DIFFICULTY.HARD;

public class GameController {
    private Player player, opponent;
    private GameManager gm;
    private boolean isClickedOnce = false;
    private Position firstPos, secondPos;
    private Position promotionPosition;
    private int GAMEMODE; // -1 for 2 players, 0 for AI, and 1 for Multiplayer
    private boolean orientedAtWhite;
    @FXML
    private Label LABEL_TURN;

    @FXML
    private AnchorPane promotionPane;

    @FXML
    private ImageView IMAGE_BOARD;

    @FXML
    private Button queenButton, rookButton, knightButton, bishopButton;
    @FXML
    private GridPane GRID_CONTAINER;
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
    private Button[] btn = {a1, a2};

    public GameController() {
        // For now, the default game state is -1
        GAMEMODE = 0;
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
        //orientedAtWhite = player.getColor() == Color.WHITE;
        orientedAtWhite = true;
    }

    public void initialize() {
        populateButtonMatrix();
        updateBoardUI();
        updateTurnLabel();

        // Initialize promotion pane
        if (promotionPane != null) {
            promotionPane.setVisible(false);
            IMAGE_BOARD.setVisible(true);
            GRID_CONTAINER.setVisible(true);
            // Map the promotion buttons
            for (Node node : promotionPane.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    if (button.getGraphic() instanceof ImageView) {
                        ImageView iv = (ImageView) button.getGraphic();
                        if (iv.getImage() != null) {
                            String url = iv.getImage().getUrl();
                            if (url.contains("wq.png")) {
                                queenButton = button;
                            } else if (url.contains("wr.png")) {
                                rookButton = button;
                            } else if (url.contains("wn.png")) {
                                knightButton = button;
                            } else if (url.contains("wb.png")) {
                                bishopButton = button;
                            }
                        }
                    }
                }
            }

            // Set up promotion button event handlers
            setupPromotionHandlers();
        }
    }


    private void setupPromotionHandlers() {
        if (queenButton != null) {
            queenButton.setOnAction(e -> handlePromotionSelection("queen"));
        }
        if (rookButton != null) {
            rookButton.setOnAction(e -> handlePromotionSelection("rook"));
        }
        if (knightButton != null) {
            knightButton.setOnAction(e -> handlePromotionSelection("knight"));
        }
        if (bishopButton != null) {
            bishopButton.setOnAction(e -> handlePromotionSelection("bishop"));
        }
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
        IMAGE_BOARD.setVisible(true);
        GRID_CONTAINER.setVisible(true);
        if (orientedAtWhite) {
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
        } else {
            for (int i = 7; i >= 0; i--) { // reversed row loop
                for (int j = 7; j >= 0; j--) { // reversed column loop
                    int displayRow = 7 - i;
                    int displayCol = 7 - j;

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
                        buttons[displayRow][displayCol].setGraphic(imgView);
                        buttons[displayRow][displayCol].setText("");
                    } else {
                        buttons[displayRow][displayCol].setGraphic(null);
                        buttons[displayRow][displayCol].setText("");
                    }
                }
            }

            updateTurnLabel();
        }
        updateTurnLabel();
    }

    private void updateTurnLabel() {
        if (LABEL_TURN != null) {
            String turnText = (gm.getCurrentTurn() == Color.WHITE) ? "White's Turn" : "Black's Turn";
            LABEL_TURN.setText(turnText);
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

    public void handleClick(ActionEvent e) throws IOException {
        // If promotion pane is visible, ignore board clicks
        if (promotionPane != null && promotionPane.isVisible()) {
            return;
        }

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
                    System.err.println("[DEBUG] Color: " + (gm.getPiece(firstPos).getColor() == Color.WHITE ? "White" : "Black"));
                    highlightTiles(gm.getPossibleMoves(getFXID), firstPos);
                    isClickedOnce = true;
                } else {
                    secondPos = Position.getNotation(getFXID);
                    Player current = gm.getCurrentTurn() == player.getColor() ? player : opponent;
                    boolean moveMade;
                    if (orientedAtWhite) {
                        moveMade = gm.makeMove(new Move(firstPos, secondPos), current);
                    } else {
                        moveMade = gm.makeMove(gm.getAbsolutePosition(new Move(firstPos, secondPos)), current);
                    }
                    // ==== CREATE A SEPARATE METHOD FOR THIS LATER
                    if (moveMade) {
                        int state = gm.getState();

                        if (state == GameManager.WHITE_PROMOTION || state == GameManager.BLACK_PROMOTION) {
                            Color promotionColor = (state == GameManager.WHITE_PROMOTION) ? Color.WHITE : Color.BLACK;
                            promotionPosition = secondPos;
                            showPromotionPane(promotionColor);
                        }
                        if (state == GameManager.WHITE_IN_CHECK || state == GameManager.BLACK_IN_CHECK) {
                            System.err.println("[DEBUG] King is in check.");
                        }
                        if (state == GameManager.WHITE_CHECKMATE || state == GameManager.BLACK_CHECKMATE) {
                            Color winner = state == GameManager.WHITE_CHECKMATE ? Color.BLACK : Color.WHITE;
                            System.err.println("[DEBUG] " + (state == 5 ? "White" : "Black") + "\'s King Checkmated");
                            gm.setGameOver(winner);
                            gameOver(winner);
                        }
                        if (state == GameManager.WHITE_STALEMATE || state == GameManager.BLACK_STALEMATE) {
                            Color winner = state == GameManager.WHITE_STALEMATE ? Color.BLACK : Color.WHITE;
                            System.err.println("[DEBUG] " + (state == 7 ? "White" : "Black") + "Stalemate");
                            gm.setDraw();
                            gameOver(null);
                        }
                        if (state == GameManager.WHITE_REPETITION_DRAW || state == GameManager.BLACK_REPETITION_DRAW) {
                            Color winner = state == GameManager.WHITE_REPETITION_DRAW ? Color.WHITE : Color.BLACK;
                            System.err.println("[DEBUG] " + (state == 7 ? "White" : "Black") + "Draw by Repetition");
                            gm.setDraw();
                            gameOver(null);
                        }

                    }

                    updateBoardUI();
                    isClickedOnce = false;
                }
                break;
            case 0:
                // This snippet prevents the current player in this session to avoid making moves
                if (player != gm.getCurrentPlayer())
                    return;
                if (!isClickedOnce) {
                    firstPos = Position.getNotation(getFXID);
                    // ======= TEMPORARY FAILSAFE APPROACH
                    if (gm.getBoardState()[firstPos.getRow()][firstPos.getCol()] == null)
                        return;
                    if (gm.getPiece(firstPos).getColor() != gm.getCurrentTurn()
                            && gm.getPiece(firstPos) != null)
                        return;
                    System.err.println("[DEBUG] Color: " + (gm.getPiece(firstPos).getColor() == Color.WHITE ? "White" : "Black"));
                    highlightTiles(gm.getPossibleMoves(getFXID), firstPos);
                    isClickedOnce = true;
                } else {
                    secondPos = Position.getNotation(getFXID);
                    Player current = gm.getCurrentTurn() == player.getColor() ? player : opponent;
                    boolean moveMade;
                    moveMade = gm.makeMove(new Move(firstPos, secondPos), current);

                    // ==== CREATE A SEPARATE METHOD FOR THIS LATER
                    if (moveMade) {
                        int state = gm.getState();

                        if (state == GameManager.WHITE_PROMOTION || state == GameManager.BLACK_PROMOTION) {
                            Color promotionColor = (state == GameManager.WHITE_PROMOTION) ? Color.WHITE : Color.BLACK;
                            promotionPosition = secondPos;
                            showPromotionPane(promotionColor);
                        }
                        if (state == GameManager.WHITE_IN_CHECK || state == GameManager.BLACK_IN_CHECK) {
                            System.err.println("[DEBUG] King is in check.");
                        }
                        if (state == GameManager.WHITE_CHECKMATE || state == GameManager.BLACK_CHECKMATE) {
                            Color winner = state == GameManager.WHITE_CHECKMATE ? Color.BLACK : Color.WHITE;
                            System.err.println("[DEBUG] " + (state == 5 ? "White" : "Black") + "\'s King Checkmated");
                            gm.setGameOver(winner);
                            gameOver(winner);
                        }
                        if (state == GameManager.WHITE_STALEMATE || state == GameManager.BLACK_STALEMATE) {
                            Color winner = state == GameManager.WHITE_STALEMATE ? Color.BLACK : Color.WHITE;
                            System.err.println("[DEBUG] " + (state == 7 ? "White" : "Black") + "Stalemate");
                            gm.setDraw();
                            gameOver(null);
                        }
                        if (state == GameManager.WHITE_REPETITION_DRAW || state == GameManager.BLACK_REPETITION_DRAW) {
                            Color winner = state == GameManager.WHITE_REPETITION_DRAW ? Color.WHITE : Color.BLACK;
                            System.err.println("[DEBUG] " + (state == 7 ? "White" : "Black") + "Draw by Repetition");
                            gm.setDraw();
                            gameOver(null);
                        }

                    }

                    updateBoardUI();
                    isClickedOnce = false;
                }
                break;
        }

    }


    private void showPromotionPane(Color color) {
        if (promotionPane != null) {
            // Update promotion pane images based on the color
            updatePromotionPieceImages(color);

            // Show the promotion pane
            promotionPane.setVisible(true);
        } else {
            System.err.println("Promotion pane not found in FXML!");
        }
    }

    public void gameOver(Color winner) {
        GRID_CONTAINER.setDisable(true);
        LABEL_TURN.setVisible(false);
    }

    private void updatePromotionPieceImages(Color color) {
        String colorPrefix = color == Color.WHITE ? "w" : "b";

        // Update each button image with the correct color
        if (queenButton != null && queenButton.getGraphic() instanceof ImageView) {
            String imagePath = "/com/kiruu/chess/img/" + colorPrefix + "q.png";
            ((ImageView) queenButton.getGraphic()).setImage(new Image(getClass().getResourceAsStream(imagePath)));
        }

        if (rookButton != null && rookButton.getGraphic() instanceof ImageView) {
            String imagePath = "/com/kiruu/chess/img/" + colorPrefix + "r.png";
            ((ImageView) rookButton.getGraphic()).setImage(new Image(getClass().getResourceAsStream(imagePath)));
        }

        if (knightButton != null && knightButton.getGraphic() instanceof ImageView) {
            String imagePath = "/com/kiruu/chess/img/" + colorPrefix + "n.png";
            ((ImageView) knightButton.getGraphic()).setImage(new Image(getClass().getResourceAsStream(imagePath)));
        }

        if (bishopButton != null && bishopButton.getGraphic() instanceof ImageView) {
            String imagePath = "/com/kiruu/chess/img/" + colorPrefix + "b.png";
            ((ImageView) bishopButton.getGraphic()).setImage(new Image(getClass().getResourceAsStream(imagePath)));
        }
    }

    private void handlePromotionSelection(String pieceType) {
        Color currentColor = gm.getCurrentTurn();
        Piece promotedPiece = null;

        switch (pieceType) {
            case "queen":
                promotedPiece = new Queen(currentColor);
                break;
            case "rook":
                promotedPiece = new Rook(currentColor);
                break;
            case "knight":
                promotedPiece = new Knight(currentColor);
                break;
            case "bishop":
                promotedPiece = new Bishop(currentColor);
                break;
        }

        if (promotedPiece != null && promotionPosition != null) {
            gm.promotePawn(promotionPosition, promotedPiece);
            promotionPane.setVisible(false);
            updateBoardUI();
        }
    }
}
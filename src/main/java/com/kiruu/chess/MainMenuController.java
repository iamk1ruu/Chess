package com.kiruu.chess;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {
    // This method is triggered when the user presses the Duel button
    public void duel(ActionEvent e) {
        try {
            // Load the BoardView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardView.fxml"));

            // Load the FXML into a parent container
            Parent boardViewParent = loader.load();

            // Access the GameController from the loader
            GameController gameController = loader.getController();

            // Here, you can initialize or pass any necessary data to the gameController
            // Example: gameController.setupGame();

            // Create a new scene and set it on the stage
            Scene boardScene = new Scene(boardViewParent);
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            stage.setScene(boardScene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

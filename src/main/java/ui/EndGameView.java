package ui;

import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Represents the overlay view displayed when the game ends (Win or Lose).
 * <p>
 * This view provides a message indicating the result and a button to return to the main menu.
 * </p>
 */
public class EndGameView {

    private final StackPane root;

    /**
     * Initializes the end game view with the game result.
     *
     * @param sceneManager The SceneManager to handle navigation back to the menu.
     * @param playerWon    True if the player won, false if the enemy won.
     */
    public EndGameView(SceneManager sceneManager, boolean playerWon) {
        root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        String message = playerWon ? "You Win!" : "Enemy Wins!";
        Label resultLabel = new Label(message);
        resultLabel.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold; -fx-effect: dropshadow(one-pass-box, black, 5, 0.0, 2, 2);");

        Button menuButton = new Button("Return to Main Menu");
        menuButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 20; -fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;");
        menuButton.setOnMouseEntered(e -> menuButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 20; -fx-background-color: #666; -fx-text-fill: white; -fx-background-radius: 5;"));
        menuButton.setOnMouseExited(e -> menuButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 20; -fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;"));

        menuButton.setOnAction(e -> sceneManager.returnToMenu());

        VBox box = new VBox(20, resultLabel, menuButton);
        box.setAlignment(Pos.CENTER);

        root.getChildren().add(box);
    }

    /**
     * Returns the root node of the end game view.
     *
     * @return The root StackPane.
     */
    public StackPane getRoot() {
        return root;
    }
}
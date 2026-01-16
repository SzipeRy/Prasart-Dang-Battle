package ui.renderer;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Utility class for rendering floating damage text popups.
 */
public class DamagePopupRenderer {

    /**
     * Creates and displays a floating damage label at the specified coordinates.
     * <p>
     * The label floats upward and fades out over time.
     * </p>
     *
     * @param battlefieldPane The pane to add the popup to.
     * @param x               The x-coordinate for the popup.
     * @param y               The y-coordinate for the popup.
     * @param amount          The damage amount to display.
     * @param isCritical      True if the damage is critical (displayed in red), false otherwise (orange).
     */
    public static void showDamage(Pane battlefieldPane, double x, double y, int amount, boolean isCritical) {
        Label damageLabel = new Label("-" + amount);
        damageLabel.setLayoutX(x);
        damageLabel.setLayoutY(y - 20);

        damageLabel.setTextFill(isCritical ? Color.RED : Color.ORANGE);
        damageLabel.setFont(Font.font("Arial", FontWeight.BOLD, isCritical ? 20 : 14));
        damageLabel.setStyle("-fx-effect: dropshadow(one-pass-box, black, 2, 0.0, 1, 1);");

        battlefieldPane.getChildren().add(damageLabel);

        TranslateTransition moveUp = new TranslateTransition(Duration.millis(800), damageLabel);
        moveUp.setByY(-40);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(800), damageLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        ParallelTransition animation = new ParallelTransition(moveUp, fadeOut);
        animation.setOnFinished(e -> battlefieldPane.getChildren().remove(damageLabel));
        animation.play();
    }
}
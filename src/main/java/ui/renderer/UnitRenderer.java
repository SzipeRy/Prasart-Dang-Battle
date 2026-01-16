package ui.renderer;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import units.Unit;

import java.util.Objects;

/**
 * Renders units on the battlefield, including their sprite and health bar.
 */
public class UnitRenderer {

    private final Unit unit;
    private final ImageView imageView;
    private final ProgressBar hpBar;

    private double lastX;
    private double walkCycle = 0;
    private static final double BOB_AMPLITUDE = 3.0;
    private static final double BOB_SPEED = 0.25;

    private final double groundY;

    /**
     * Initializes the renderer for a unit.
     *
     * @param unit      The unit logic object.
     * @param imagePath The path to the unit's image resource.
     * @param startX    The initial x-coordinate.
     * @param groundY   The y-coordinate representing the ground level.
     */
    public UnitRenderer(Unit unit, String imagePath, double startX, double groundY) {
        this.unit = unit;
        this.groundY = groundY;
        this.lastX = startX;

        Image image = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        imageView = new ImageView(image);
        imageView.setFitWidth(75);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(startX);

        hpBar = new ProgressBar(1.0);
        hpBar.setPrefWidth(50);
        hpBar.setPrefHeight(10);
        hpBar.setStyle("-fx-accent: lightgreen; -fx-control-inner-background: #444; -fx-text-box-border: transparent;");
        hpBar.setLayoutX(startX);

        if (image.getWidth() > 0) {
            alignToGround(image.getWidth(), image.getHeight());
        } else {
            image.widthProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() > 0) {
                    alignToGround(newVal.doubleValue(), image.getHeight());
                }
            });
        }
    }

    private void alignToGround(double imageW, double imageH) {
        if (imageW <= 0) return;

        double scale = 75.0 / imageW;
        double renderedHeight = imageH * scale;

        imageView.setLayoutY(groundY - renderedHeight);

        hpBar.setLayoutY(groundY - renderedHeight - 15);
    }

    /**
     * Adds the unit's image and health bar to the specified pane.
     *
     * @param battlefieldPane The pane to add the visuals to.
     */
    public void addToPane(Pane battlefieldPane) {
        battlefieldPane.getChildren().addAll(imageView, hpBar);
    }

    /**
     * Removes the unit's image and health bar from the specified pane.
     *
     * @param battlefieldPane The pane to remove the visuals from.
     */
    public void removeFromPane(Pane battlefieldPane) {
        battlefieldPane.getChildren().removeAll(imageView, hpBar);
    }

    /**
     * Updates the unit's position and health bar.
     * <p>
     * Also applies a simple bobbing animation if the unit is moving.
     * </p>
     *
     * @param newX The new x-coordinate of the unit.
     */
    public void update(double newX) {
        double deltaX = Math.abs(newX - lastX);
        boolean isMoving = deltaX > 0.01;

        if (isMoving) {
            walkCycle += BOB_SPEED;
            double yOffset = -Math.abs(Math.sin(walkCycle)) * BOB_AMPLITUDE;
            imageView.setTranslateY(yOffset);
        } else {
            walkCycle = 0;
            imageView.setTranslateY(0);
        }

        imageView.setLayoutX(newX);
        hpBar.setLayoutX(newX);

        lastX = newX;

        double progress = (double) unit.getHp() / unit.getMaxHp();
        hpBar.setProgress(Math.max(0, progress));

        if (!unit.isAlive()) {
            imageView.setOpacity(0.5);
            hpBar.setVisible(false);
            imageView.setTranslateY(0);
        }
    }

    /**
     * Returns the unit logic object associated with this renderer.
     *
     * @return The Unit object.
     */
    public Unit getUnit() {
        return unit;
    }
}
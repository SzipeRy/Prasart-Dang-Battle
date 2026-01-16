package ui.renderer;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import objects.Projectile;

/**
 * Renders projectiles on the battlefield.
 */
public class ProjectileRenderer {
    private final Projectile projectile;
    private final Circle shape;

    /**
     * Initializes a renderer for a specific projectile.
     *
     * @param projectile The projectile logic object to render.
     */
    public ProjectileRenderer(Projectile projectile) {
        this.projectile = projectile;
        Color c = Color.BLACK;
        double radius = 5;

        if ("ARROW".equals(projectile.getImagePath())) {
            c = Color.BROWN;
            radius = 3;
        }

        this.shape = new Circle(radius, c);
        update();
    }

    /**
     * Adds the projectile shape to the specified pane.
     *
     * @param pane The pane to add the shape to.
     */
    public void addToPane(Pane pane) {
        pane.getChildren().add(shape);
    }

    /**
     * Removes the projectile shape from the specified pane.
     *
     * @param pane The pane to remove the shape from.
     */
    public void removeFromPane(Pane pane) {
        pane.getChildren().remove(shape);
    }

    /**
     * Updates the position of the rendered projectile to match its logic coordinates.
     */
    public void update() {
        this.shape.setLayoutX(projectile.getX());
        this.shape.setLayoutY(projectile.getY());
    }

    /**
     * Returns the projectile logic object associated with this renderer.
     *
     * @return The Projectile object.
     */
    public Projectile getProjectile() {
        return projectile;
    }
}
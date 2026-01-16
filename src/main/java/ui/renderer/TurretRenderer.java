package ui.renderer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import turrets.Turret;

/**
 * Renders turrets on the battlefield using an ImageView.
 */
public class TurretRenderer {

    private final Turret TURRET;
    private final ImageView IMAGE_VIEW;

    private double x;
    private double y;

    /**
     * Initializes the renderer for a turret.
     *
     * @param turret        The turret logic object.
     * @param imagePath     The path to the image resource.
     * @param baseX         The starting x-coordinate for the turret slots.
     * @param bottomAnchorY The y-coordinate where the bottom of the turret should align.
     * @param slotIndex     The index of the slot the turret occupies.
     */
    public TurretRenderer(Turret turret, String imagePath, double baseX, double bottomAnchorY, int slotIndex) {
        this.TURRET = turret;

        Image image = new Image(imagePath);
        IMAGE_VIEW = new ImageView(image);

        IMAGE_VIEW.setFitWidth(75);
        IMAGE_VIEW.setPreserveRatio(true);

        this.x = baseX + (slotIndex * 80);
        IMAGE_VIEW.setLayoutX(x);

        Runnable alignToBottom = () -> {
            double h = IMAGE_VIEW.getBoundsInLocal().getHeight();
            IMAGE_VIEW.setLayoutY(bottomAnchorY - h);
        };

        IMAGE_VIEW.boundsInLocalProperty().addListener((obs, oldVal, newVal) -> alignToBottom.run());

        alignToBottom.run();
    }

    /**
     * Adds the turret image to the specified pane.
     *
     * @param layer The pane to add the turret to.
     */
    public void addToPane(Pane layer) {
        layer.getChildren().add(IMAGE_VIEW);
    }

    /**
     * Removes the turret image from the specified pane.
     *
     * @param battlefieldPane The pane to remove the turret from.
     */
    public void removeFromPane(Pane battlefieldPane) {
        battlefieldPane.getChildren().remove(IMAGE_VIEW);
    }

    /**
     * Returns the turret logic object associated with this renderer.
     *
     * @return The Turret object.
     */
    public Turret getTurret() {
        return TURRET;
    }
}
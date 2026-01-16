package ui.renderer;

import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import abilities.SpecialAbility;
import abilities.NukeAbility;

/**
 * Renders visual effects for special abilities on the battlefield.
 */
public class AbilityRenderer {

    private final SpecialAbility ability;

    /**
     * Initializes the renderer with the specific ability to render.
     *
     * @param ability The ability instance.
     */
    public AbilityRenderer(SpecialAbility ability) {
        this.ability = ability;
    }

    /**
     * Renders the ability effect on the given pane.
     *
     * @param battlefieldPane The pane where the effect will be displayed.
     */
    public void render(Pane battlefieldPane) {
        if (getAbility() instanceof NukeAbility) {
            renderNuke(battlefieldPane);
        }
    }

    private void renderNuke(Pane battlefieldPane) {
        Rectangle overlay = new Rectangle(
                battlefieldPane.getWidth(),
                battlefieldPane.getHeight(),
                Color.BROWN
        );
        overlay.setMouseTransparent(true);
        overlay.setOpacity(0.0);

        battlefieldPane.getChildren().add(overlay);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), overlay);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.3); // Reduced max opacity from 0.6 to 0.3

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), overlay);
        fadeOut.setFromValue(0.3);
        fadeOut.setToValue(0.0);

        fadeIn.setOnFinished(e -> fadeOut.play());
        fadeOut.setOnFinished(e -> battlefieldPane.getChildren().remove(overlay));

        fadeIn.play();
    }

    /**
     * Returns the ability associated with this renderer.
     *
     * @return The SpecialAbility object.
     */
    public SpecialAbility getAbility() {
        return ability;
    }
}
package ui;

import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.SceneManager;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.Objects;

/**
 * Represents the main menu and tutorial screens of the application.
 */
public class MenuView {

    private final StackPane root;
    private static final String BUTTON_STYLE =
            "-fx-background-color: #333333; -fx-text-fill: #FFD700; -fx-font-size: 18px; " +
                    "-fx-font-weight: bold; -fx-border-color: #555555; -fx-border-width: 2px; " +
                    "-fx-border-radius: 5; -fx-background-radius: 5; -fx-min-width: 200px;";

    private static final String BUTTON_HOVER_STYLE =
            "-fx-background-color: #555555; -fx-text-fill: #FFFFFF; -fx-font-size: 18px; " +
                    "-fx-font-weight: bold; -fx-border-color: #FFD700; -fx-border-width: 2px; " +
                    "-fx-border-radius: 5; -fx-background-radius: 5; -fx-min-width: 200px;";

    /**
     * Initializes the standard main menu view.
     *
     * @param sceneManager The SceneManager to handle navigation.
     */
    public MenuView(SceneManager sceneManager) {
        root = new StackPane();

        addBackground();

        VBox mainBox = new VBox(25);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setMaxWidth(800);
        mainBox.setMaxHeight(400);
        mainBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 40; -fx-background-radius: 20; -fx-border-color: #8B4513; -fx-border-width: 3; -fx-border-radius: 20;");

        Label title = new Label("Prasart Dang Battle");
        title.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 48px; -fx-font-weight: bold; -fx-font-family: 'Verdana';");
        title.setTextAlignment(TextAlignment.CENTER);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.RED);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.5);
        title.setEffect(dropShadow);

        Button startButton = createStyledButton("Start Game");
        startButton.setOnAction(e -> sceneManager.showBattlefield());

        Button tutorialButton = createStyledButton("How to Play");
        tutorialButton.setOnAction(e -> sceneManager.showTutorial());

        Button exitButton = createStyledButton("Exit");
        exitButton.setOnAction(e -> Platform.exit());

        mainBox.getChildren().addAll(title, startButton, tutorialButton, exitButton);
        root.getChildren().add(mainBox);
    }

    /**
     * Initializes the view for the tutorial/manual screen.
     *
     * @param sceneManager The SceneManager to handle navigation.
     * @param tutorialMode True if this view is being initialized as the tutorial screen.
     */
    public MenuView(SceneManager sceneManager, boolean tutorialMode) {
        root = new StackPane();

        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setMaxWidth(600);
        contentBox.setStyle("-fx-padding: 30;");

        Label header = new Label("Battle Manual");
        header.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold; -fx-underline: true;");

        VBox rulesBox = new VBox(10);
        rulesBox.setAlignment(Pos.CENTER_LEFT);

        rulesBox.getChildren().addAll(
                createRuleLine("Objective:", "Destroy the enemy base on the right."),
                createRuleLine("Resources:", "Currency generates over time. Use it to buy units."),
                new Label(""), // Spacer
                createRuleLine("Unit Counters (Rock-Paper-Scissors):", ""),
                createCounterLine("Melee", "beats", "Ranged", "images/melee_1.png"),
                createCounterLine("Ranged", "beats", "Anti-Armor", "images/ranged_1.png"),
                createCounterLine("Anti-Armor", "beats", "Armored", "images/anti_armored_1.png"),
                createCounterLine("Armored", "beats", "Melee", "images/armored_1.png")
        );

        ScrollPane scroll = new ScrollPane(rulesBox);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scroll.setFitToWidth(true);
        scroll.setMaxHeight(400);
        scroll.getStylesheets().add("data:text/css,.scroll-pane > .viewport { -fx-background-color: transparent; }");

        Button closeButton = createStyledButton("Return to Menu");
        closeButton.setOnAction(e -> sceneManager.showMenu());

        contentBox.getChildren().addAll(header, scroll, closeButton);
        root.getChildren().add(contentBox);
    }

    private void addBackground() {
        try {
            Image bgImage = new Image(Objects.requireNonNull(getClass().getResource("/images/background.png")).toExternalForm());
            ImageView bgView = new ImageView(bgImage);

            bgView.setPreserveRatio(true);
            bgView.fitHeightProperty().bind(root.heightProperty());

            root.getChildren().add(bgView);
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1a1a1a;");
        }
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(BUTTON_STYLE);

        btn.setOnMouseEntered(e -> btn.setStyle(BUTTON_HOVER_STYLE));
        btn.setOnMouseExited(e -> btn.setStyle(BUTTON_STYLE));

        return btn;
    }

    private HBox createRuleLine(String title, String desc) {
        Label t = new Label(title + " ");
        t.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 16px;");

        Label d = new Label(desc);
        d.setStyle("-fx-text-fill: #E0E0E0; -fx-font-size: 16px;");

        HBox line = new HBox(t, d);
        line.setAlignment(Pos.CENTER_LEFT);
        return line;
    }

    private HBox createCounterLine(String winner, String action, String loser, String iconPath) {
        Text t1 = new Text(winner + " ");
        t1.setFill(Color.LIGHTGREEN);
        t1.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Text t2 = new Text(action + " ");
        t2.setFill(Color.WHITE);
        t2.setStyle("-fx-font-size: 16px;");

        Text t3 = new Text(loser);
        t3.setFill(Color.TOMATO);
        t3.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        TextFlow flow = new TextFlow(t1, t2, t3);

        HBox box = new HBox(10, flow);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-padding: 5; -fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 5;");
        return box;
    }

    /**
     * Returns the root node of the menu view.
     *
     * @return The root StackPane.
     */
    public StackPane getRoot() {
        return root;
    }
}
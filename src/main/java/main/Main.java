package main;

import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import controllers.GameController;

/**
 * The entry point of the Prasart Dang Battle application.
 * <p>
 * This class extends {@link Application} to set up the primary stage, initialize
 * the game controller, and launch the SceneManager.
 * </p>
 */
public class Main extends Application {

    private SceneManager sceneManager;
    private GameController gameController;

    /**
     * Initializes and launches the JavaFX application.
     * <p>
     * This method sets up the core game components, including the game controller
     * with initial game parameters and the scene manager responsible for handling
     * scene transitions. It then displays the main menu as the first screen and
     * shows the primary application window.
     * </p>
     * @param primaryStage the primary stage for this application, used to display and manage application scenes
     */
    @Override
    public void start(Stage primaryStage) {
        int startingCurrency = 1000;
        int baseHp = 500;
        gameController = new GameController(startingCurrency, baseHp);

        sceneManager = new SceneManager(primaryStage, gameController);

        sceneManager.showMenu();

        primaryStage.setTitle("Prasart Dang Battle");
        primaryStage.show();
    }

    /**
     * Launch args
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
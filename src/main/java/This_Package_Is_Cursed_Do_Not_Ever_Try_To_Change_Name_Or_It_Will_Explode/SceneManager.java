package This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode;

import javafx.scene.Scene;
import javafx.stage.Stage;

import controllers.GameController;
import ui.BattlefieldView;
import ui.MenuView;

/**
 * Manages the transitions between different scenes in the JavaFX application.
 * <p>
 * This class handles switching between the Main Menu, the Tutorial, and the
 * active Battlefield game view. It also manages background music playback
 * appropriate for each scene.
 * </p>
 */
public class SceneManager {

    private final Stage primaryStage;
    private final GameController gameController;
    private final SoundManager soundManager;

    private Scene menuScene;
    private Scene battlefieldScene;

    /**
     * Initializes the SceneManager with the primary stage and game controller.
     * @param primaryStage The main JavaFX stage.
     * @param gameController The controller managing game logic.
     */
    public SceneManager(Stage primaryStage, GameController gameController) {
        this.primaryStage = primaryStage;
        this.gameController = gameController;
        this.soundManager = new SoundManager();
    }

    /**
     * Displays the main menu scene.
     * Stops battle BGM and plays menu BGM.
     */
    public void showMenu() {
        soundManager.stopBattleBGM();
        soundManager.playMenuBGM();

        MenuView menuView = new MenuView(this);
        menuScene = new Scene(menuView.getRoot(), 1800, 1000);
        primaryStage.setScene(menuScene);
    }

    /**
     * Displays the battlefield scene and starts the game.
     * Resets the game state, stops menu BGM, and plays battle BGM.
     */
    public void showBattlefield() {
        soundManager.stopMenuBGM();

        gameController.resetGame();

        soundManager.playBattleBGM();

        BattlefieldView battlefieldView = new BattlefieldView(this, gameController, soundManager);
        battlefieldScene = new Scene(battlefieldView.getRoot(), 1800, 1000);
        primaryStage.setScene(battlefieldScene);
    }

    /**
     * Displays the tutorial scene.
     * Stops battle BGM.
     */
    public void showTutorial() {
        soundManager.stopBattleBGM();

        MenuView tutorialView = new MenuView(this, true);
        menuScene = new Scene(tutorialView.getRoot(), 1800, 1000);
        primaryStage.setScene(menuScene);
    }

    /**
     * Helper method to return to the main menu (alias for showMenu).
     */
    public void returnToMenu() {
        showMenu();
    }
}
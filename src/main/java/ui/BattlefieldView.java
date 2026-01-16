package ui;

import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.SceneManager;
import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.SoundManager;
import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.GameManager;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import controllers.GameController;
import ui.renderer.AbilityRenderer;
import ui.renderer.ProjectileRenderer;
import ui.renderer.TurretRenderer;
import ui.renderer.UnitRenderer;
import units.Unit;
import units.RangedUnit;
import units.ArmoredUnit;
import units.AntiArmoredUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The primary view for the game battlefield.
 * <p>
 * This class handles the rendering of the scrolling map, units, turrets, projectiles,
 * and base health. It also orchestrates the main game loop {@link AnimationTimer}.
 * </p>
 */
public class BattlefieldView {

    private final StackPane root;
    private final GameController gameController;
    private final SoundManager soundManager;
    private final ScrollPane scrollPane;
    private final Pane battlefieldPane;
    private final Pane turretLayer;
    private final HUD hud;
    private final StackPane gameContentLayer;

    private final List<UnitRenderer> unitRenderers = new ArrayList<>();
    private final List<TurretRenderer> turretRenderers = new ArrayList<>();
    private final List<ProjectileRenderer> projectileRenderers = new ArrayList<>();

    private ProgressBar playerHpBar;
    private ProgressBar enemyHpBar;
    private Label playerHpLabel;
    private Label enemyHpLabel;

    private AnimationTimer gameLoop;

    /**
     * Initializes the battlefield view.
     *
     * @param sceneManager   The SceneManager used to transition between scenes.
     * @param gameController The GameController used for game logic and state updates.
     * @param soundManager   The SoundManager used for audio playback.
     */
    public BattlefieldView(SceneManager sceneManager, GameController gameController, SoundManager soundManager) {
        this.gameController = gameController;
        this.soundManager = soundManager;
        root = new StackPane();

        battlefieldPane = new Pane();
        battlefieldPane.setPrefSize(3000, 1000);

        turretLayer = new Pane();
        turretLayer.setPrefSize(3000, 1000);
        turretLayer.setPickOnBounds(false);

        scrollPane = new ScrollPane(battlefieldPane);
        scrollPane.setPannable(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        BorderPane gameLayer = new BorderPane();
        gameLayer.setCenter(scrollPane);

        hud = new HUD(sceneManager, gameController, this, soundManager);
        BorderPane uiLayer = new BorderPane();
        uiLayer.setPickOnBounds(false);
        uiLayer.setTop(hud.getRoot());

        gameContentLayer = new StackPane();
        gameContentLayer.getChildren().addAll(gameLayer, uiLayer);

        root.getChildren().add(gameContentLayer);

        root.setOnMouseMoved(e -> {
            if (gameLoop == null) return;
            double x = e.getSceneX();
            double width = root.getWidth();
            if (x < 100) {
                scrollPane.setHvalue(Math.max(0, scrollPane.getHvalue() - 0.02));
            } else if (x > width - 100) {
                scrollPane.setHvalue(Math.min(1, scrollPane.getHvalue() + 0.02));
            }
        });

        addBaseVisuals();
        startGameLoop(sceneManager);
    }

    private void addBaseVisuals() {
        try {
            Image bg = new Image(Objects.requireNonNull(getClass().getResource("/images/background.png")).toExternalForm());
            ImageView bgView = new ImageView(bg);
            bgView.setFitHeight(1000);
            battlefieldPane.getChildren().add(bgView);
        } catch (Exception e) {
            Rectangle sky = new Rectangle(3000, 1000, Color.LIGHTBLUE);
            Rectangle ground = new Rectangle(3000, 300, Color.FORESTGREEN);
            ground.setLayoutY(700);
            Rectangle path = new Rectangle(3000, 100, Color.SANDYBROWN);
            path.setLayoutY(800);
            battlefieldPane.getChildren().addAll(sky, ground, path);
        }

        battlefieldPane.getChildren().add(turretLayer);

        ImageView playerBase = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/images/base_1.png")).toExternalForm()));
        playerBase.setFitWidth(300);
        playerBase.setPreserveRatio(true);
        playerBase.setLayoutX(-20);
        playerBase.setLayoutY(620);

        playerHpBar = new ProgressBar(1.0);
        playerHpBar.setStyle("-fx-accent: red;");
        playerHpBar.setLayoutX(100);
        playerHpBar.setLayoutY(600);

        playerHpLabel = new Label();
        playerHpLabel.setLayoutX(123);
        playerHpLabel.setLayoutY(598);
        playerHpLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");

        ImageView enemyBase = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/images/base_2.png")).toExternalForm()));
        enemyBase.setFitWidth(300);
        enemyBase.setPreserveRatio(true);
        enemyBase.setLayoutX(2720);
        enemyBase.setLayoutY(620);

        enemyHpBar = new ProgressBar(1.0);
        enemyHpBar.setStyle("-fx-accent: red;");
        enemyHpBar.setLayoutX(2797);
        enemyHpBar.setLayoutY(600);

        enemyHpLabel = new Label();
        enemyHpLabel.setLayoutX(2820);
        enemyHpLabel.setLayoutY(598);
        enemyHpLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");

        battlefieldPane.getChildren().addAll(playerBase, playerHpBar, playerHpLabel,
                enemyBase, enemyHpBar, enemyHpLabel);
    }

    /**
     * Displays the end game overlay with the result message.
     * Stops the game loop and battle background music.
     *
     * @param playerWon    True if the player won, false otherwise.
     * @param sceneManager The SceneManager used to return to the menu.
     */
    public void showEndGameOverlay(boolean playerWon, SceneManager sceneManager) {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
        soundManager.stopBattleBGM();

        BoxBlur blur = new BoxBlur(10, 10, 3);
        gameContentLayer.setEffect(blur);

        EndGameView endView = new EndGameView(sceneManager, playerWon);
        root.getChildren().add(endView.getRoot());
    }

    /**
     * Adds a TurretRenderer to the battlefield view.
     *
     * @param renderer The renderer for the turret to be added.
     */
    public void addTurretRenderer(TurretRenderer renderer) {
        turretRenderers.add(renderer);
        renderer.addToPane(turretLayer);
    }

    private void startGameLoop(SceneManager sceneManager) {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                GameManager.GameState state = gameController.nextTurn();
                GameManager gm = gameController.getGameManager();

                hud.updateCurrency();

                double maxHp = gm.getPlayerBase().getMaxHp();
                playerHpBar.setProgress((double) gm.getPlayerBase().getHp() / maxHp);
                playerHpLabel.setText(gm.getPlayerBase().getHp() + "/" + (int)maxHp);

                double eMaxHp = gm.getEnemyBase().getMaxHp();
                enemyHpBar.setProgress((double) gm.getEnemyBase().getHp() / eMaxHp);
                enemyHpLabel.setText(gm.getEnemyBase().getHp() + "/" + (int)eMaxHp);

                syncUnits(gm.getPlayerUnits(), true);
                syncUnits(gm.getEnemyUnits(), false);

                turretRenderers.removeIf(r -> {
                    if (!gm.getPlayerBase().getTurrets().contains(r.getTurret())) {
                        r.removeFromPane(turretLayer);
                        return true;
                    }
                    return false;
                });

                List<objects.Projectile> activeProjectiles = gm.getProjectiles();

                projectileRenderers.removeIf(pr -> {
                    if (!activeProjectiles.contains(pr.getProjectile())) {
                        pr.removeFromPane(battlefieldPane);
                        return true;
                    }
                    return false;
                });

                for (objects.Projectile p : activeProjectiles) {
                    boolean found = false;
                    for (ProjectileRenderer pr : projectileRenderers) {
                        if (pr.getProjectile() == p) {
                            pr.update();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        ProjectileRenderer newPr = new ProjectileRenderer(p);
                        newPr.addToPane(battlefieldPane);
                        projectileRenderers.add(newPr);
                        soundManager.playShootSound();
                    }
                }

                for (GameManager.DamageEvent event : gm.getRecentDamageEvents()) {
                    if (event.isBase()) {
                        soundManager.playBaseTakeDamage();
                    } else {
                        soundManager.playUnitTakeDamage();
                    }

                    ui.renderer.DamagePopupRenderer.showDamage(
                            battlefieldPane, event.x(), event.y(), event.amount(), event.isCritical()
                    );
                }

                if (state == GameManager.GameState.PLAYER_WIN) {
                    soundManager.playBaseExplosion();
                    showEndGameOverlay(true, sceneManager);
                } else if (state == GameManager.GameState.ENEMY_WIN) {
                    soundManager.playBaseExplosion();
                    showEndGameOverlay(false, sceneManager);
                }
            }
        };
        gameLoop.start();
    }

    private void syncUnits(List<Unit> logicUnits, boolean isPlayer) {
        unitRenderers.removeIf(renderer -> {
            if (!renderer.getUnit().isAlive()) {
                renderer.removeFromPane(battlefieldPane);
                return true;
            }
            return false;
        });

        for (Unit unit : logicUnits) {
            UnitRenderer renderer = unitRenderers.stream()
                    .filter(r -> r.getUnit() == unit)
                    .findFirst()
                    .orElse(null);

            if (renderer != null) {
                renderer.update(unit.getCoordinate());
            } else {
                String imagePath = getUnitImagePath(unit, isPlayer);
                renderer = new UnitRenderer(unit, imagePath, unit.getCoordinate(), 900);
                addUnitRenderer(renderer);
            }
        }
    }

    private String getUnitImagePath(Unit unit, boolean isPlayer) {
        String baseName = "melee";
        if (unit instanceof RangedUnit) baseName = "ranged";
        else if (unit instanceof ArmoredUnit) baseName = "armored";
        else if (unit instanceof AntiArmoredUnit) baseName = "anti_armored";
        return "/images/" + baseName + (isPlayer ? "_1" : "_2") + ".png";
    }

    /**
     * Returns the root node of the battlefield view.
     *
     * @return The root StackPane.
     */
    public StackPane getRoot() { return root; }

    /**
     * Adds a UnitRenderer to the battlefield view.
     *
     * @param renderer The renderer for the unit to be added.
     */
    public void addUnitRenderer(UnitRenderer renderer) {
        unitRenderers.add(renderer);
        renderer.addToPane(battlefieldPane);
    }

    /**
     * Triggers the rendering of an ability effect.
     *
     * @param renderer The renderer for the ability.
     */
    public void addAbilityRenderer(AbilityRenderer renderer) {
        renderer.render(battlefieldPane);
    }
}
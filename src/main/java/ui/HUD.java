package ui;

import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.SceneManager;
import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import controllers.GameController;
import ui.renderer.TurretRenderer;
import units.*;
import turrets.*;
import abilities.NukeAbility;
import ui.renderer.AbilityRenderer;
import base.Base;

/**
 * The Heads-Up Display (HUD) containing game controls and status information.
 * <p>
 * This class manages the UI for training units, buying/selling turrets, upgrades,
 * and abilities, as well as displaying current currency and queue status.
 * </p>
 */
public class HUD {
    private final BorderPane root;
    private final GameController gameController;
    private final BattlefieldView battlefieldView;
    private final SoundManager soundManager;
    private final Label currencyLabel;
    private final Label queueLabel;

    private Button nukeButton;
    private VBox activeMenu = null;

    private static final String BUTTON_STYLE =
            "-fx-background-color: #333333; -fx-text-fill: #FFD700; -fx-font-size: 14px; " +
                    "-fx-font-weight: bold; -fx-border-color: #555555; -fx-border-width: 2px; " +
                    "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8 15;";

    private static final String BUTTON_HOVER_STYLE =
            "-fx-background-color: #555555; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; " +
                    "-fx-font-weight: bold; -fx-border-color: #FFD700; -fx-border-width: 2px; " +
                    "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8 15;";

    private static final String BUTTON_DISABLED_STYLE =
            "-fx-background-color: #222222; -fx-text-fill: #555555; -fx-font-size: 14px; " +
                    "-fx-font-weight: bold; -fx-border-color: #444444; -fx-border-width: 2px; " +
                    "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8 15;";

    private static final String SUB_MENU_STYLE =
            "-fx-background-color: rgba(0,0,0,0); -fx-padding: 15; -fx-background-radius: 0 0 10 10; ";

    /**
     * Initializes the HUD with necessary controllers and managers.
     *
     * @param sceneManager    The scene manager for scene transitions.
     * @param gameController  The game controller for logic interactions.
     * @param battlefieldView The battlefield view for visual updates.
     * @param soundManager    The sound manager for playing UI sounds.
     */
    public HUD(SceneManager sceneManager, GameController gameController, BattlefieldView battlefieldView, SoundManager soundManager) {
        this.gameController = gameController;
        this.battlefieldView = battlefieldView;
        this.soundManager = soundManager;

        root = new BorderPane();
        root.setPickOnBounds(false);
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 5; -fx-border-color: #8B4513; -fx-border-width: 0 0 3 0;");

        currencyLabel = new Label("Currency: 0");
        currencyLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 20px; -fx-font-weight: bold; -fx-effect: dropshadow(one-pass-box, black, 2, 0.0, 1, 1);");
        queueLabel = new Label("Queue: 0/5");
        queueLabel.setStyle("-fx-text-fill: #E0E0E0; -fx-font-size: 14px; -fx-effect: dropshadow(one-pass-box, black, 2, 0.0, 1, 1);");

        VBox leftBox = new VBox(5, currencyLabel, queueLabel);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setStyle("-fx-padding: 0 0 0 10;");

        Button trainUnitsBtn = createStyledButton("Train Units");
        Button buyTurretsBtn = createStyledButton("Buy Turrets");
        Button sellTurretsBtn = createStyledButton("Sell Turrets");
        Button upgradesBtn = createStyledButton("Upgrades");
        Button abilitiesBtn = createStyledButton("Abilities");

        HBox rightBox = new HBox(10, trainUnitsBtn, buyTurretsBtn, sellTurretsBtn, upgradesBtn, abilitiesBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setStyle("-fx-padding: 10;");

        root.setLeft(leftBox);
        root.setRight(rightBox);

        trainUnitsBtn.setOnAction(e -> toggleMenu(createTrainUnitsMenu()));
        buyTurretsBtn.setOnAction(e -> toggleMenu(createTurretsMenu()));
        sellTurretsBtn.setOnAction(e -> toggleMenu(createSellTurretsMenu()));
        upgradesBtn.setOnAction(e -> toggleMenu(createUpgradesMenu()));
        abilitiesBtn.setOnAction(e -> toggleMenu(createAbilitiesMenu()));
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(BUTTON_STYLE);
        btn.setOnMouseEntered(e -> { if(!btn.isDisable()) btn.setStyle(BUTTON_HOVER_STYLE); });
        btn.setOnMouseExited(e -> { if(!btn.isDisable()) btn.setStyle(BUTTON_STYLE); });
        return btn;
    }

    private void toggleMenu(VBox newMenu) {
        if (activeMenu != null && activeMenu.getUserData().equals(newMenu.getUserData())) {
            root.setBottom(null);
            activeMenu = null;
        } else {
            showMenu(newMenu);
        }
    }

    private void showMenu(VBox newMenu) {
        newMenu.setStyle(SUB_MENU_STYLE);
        newMenu.setAlignment(Pos.CENTER);
        newMenu.setMaxHeight(120);
        root.setBottom(newMenu);
        activeMenu = newMenu;
    }

    private VBox createTrainUnitsMenu() {
        VBox menu = new VBox(10);
        menu.setUserData("TRAIN");
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER);

        container.getChildren().addAll(
                createUnitBtn("Melee", "Deal 2 times damage against Ranged unit", new MeleeUnit(100, 20, 20, 50, 80, 1.0)),
                createUnitBtn("Ranged", "Deal 2 times damage against Anti-Armor unit", new RangedUnit(80, 15, 24, 75, 200, 1.0)),
                createUnitBtn("Anti-Armor", "Deal 2 times damage against Armored unit", new AntiArmoredUnit(90, 25, 20, 90, 80, 2.0)),
                createUnitBtn("Armored", "Deal 2 times damage against Melee unit", new ArmoredUnit(150, 30, 16, 120, 80, 4.0))
        );

        menu.getChildren().add(container);
        return menu;
    }

    private Button createUnitBtn(String text, String tooltip, Unit protoType) {
        Button btn = createStyledButton(text + " (" + protoType.getCost() + ")");
        btn.setTooltip(new Tooltip(tooltip));
        btn.setOnAction(e -> {
            Unit unit = null;
            if (protoType instanceof MeleeUnit) unit = new MeleeUnit(100, 20, 20, 50, 80, 1.0);
            else if (protoType instanceof RangedUnit) unit = new RangedUnit(80, 15, 24, 75, 200, 1.0);
            else if (protoType instanceof AntiArmoredUnit) unit = new AntiArmoredUnit(90, 25, 20, 90, 80, 2.0);
            else if (protoType instanceof ArmoredUnit) unit = new ArmoredUnit(150, 30, 16, 120, 80, 4.0);
            gameController.playerQueueUnit(unit);
        });
        return btn;
    }

    private VBox createTurretsMenu() {
        VBox menu = new VBox(10);
        menu.setUserData("BUY_TURRET");
        Button stdBtn = createStyledButton("Turret (300)");
        stdBtn.setOnAction(e -> placeTurretVisuals(new StandardTurret(20, 600, 300), "/images/turret_1.png"));

        Button rangeBtn = createStyledButton("LongRange (450)");
        rangeBtn.setOnAction(e -> placeTurretVisuals(new LongRangeTurret(20, 900, 450), "/images/long_ranged_turret_1.png"));

        HBox container = new HBox(10, stdBtn, rangeBtn);
        container.setAlignment(Pos.CENTER);
        menu.getChildren().add(container);
        return menu;
    }

    private void placeTurretVisuals(Turret turret, String imagePath) {
        int slot = gameController.playerPlaceTurret(turret);
        if (slot >= 0) {
            TurretRenderer renderer = new TurretRenderer(
                    turret,
                    java.util.Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm(),
                    80, 705, slot
            );
            battlefieldView.addTurretRenderer(renderer);
        }
    }

    private VBox createSellTurretsMenu() {
        VBox menu = new VBox(10);
        menu.setUserData("SELL_TURRET");
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER);

        var turrets = gameController.getGameManager().getPlayerBase().getTurrets();
        if (turrets.isEmpty()) {
            Label emptyLbl = new Label("No Turrets to Sell");
            emptyLbl.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 14px;");
            container.getChildren().add(emptyLbl);
        } else {
            for (int i = 0; i < turrets.size(); i++) {
                int index = i;
                Turret t = turrets.get(i);
                String name = (t instanceof StandardTurret) ? "Std" : "Long";
                Button btn = createStyledButton("Sell " + name + " (Slot " + (i + 1) + ")");
                btn.setOnAction(e -> {
                    gameController.playerSellTurret(index);
                    showMenu(createSellTurretsMenu());
                });
                container.getChildren().add(btn);
            }
        }

        menu.getChildren().add(container);
        return menu;
    }

    private VBox createUpgradesMenu() {
        VBox menu = new VBox(10);
        menu.setUserData("UPGRADE");
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER);

        Button uAtk = createStyledButton("Unit Atk (300)");
        if(Unit.isUpgradedAttack()) disableButton(uAtk);
        uAtk.setOnAction(e -> {
            gameController.playerUpgradeUnitAttack(300);
            if(Unit.isUpgradedAttack()) disableButton(uAtk);
        });

        Button uHp = createStyledButton("Unit HP (300)");
        if(Unit.isUpgradedHp()) disableButton(uHp);
        uHp.setOnAction(e -> {
            gameController.playerUpgradeUnitHp(300);
            if(Unit.isUpgradedHp()) disableButton(uHp);
        });

        Button tAtk = createStyledButton("Turret Atk (400)");
        if(Turret.isUpgradedAttack()) disableButton(tAtk);
        tAtk.setOnAction(e -> {
            gameController.playerUpgradeTurretAttack(400);
            if(Turret.isUpgradedAttack()) disableButton(tAtk);
        });

        Button tRange = createStyledButton("Turret Rng (400)");
        if(Turret.isUpgradedRange()) disableButton(tRange);
        tRange.setOnAction(e -> {
            gameController.playerUpgradeTurretRange(400);
            if(Turret.isUpgradedRange()) disableButton(tRange);
        });

        Button baseCap = createStyledButton("Slot Cap (800)");
        if(Base.getCapacityUpgrades() >= 1) disableButton(baseCap);
        baseCap.setOnAction(e -> {
            gameController.playerUpgradeBaseCapacity(800);
            if(Base.getCapacityUpgrades() >= 1) disableButton(baseCap);
        });

        container.getChildren().addAll(uAtk, uHp, tAtk, tRange, baseCap);
        menu.getChildren().add(container);
        return menu;
    }

    private void disableButton(Button btn) {
        btn.setDisable(true);
        btn.setStyle(BUTTON_DISABLED_STYLE);
        if (!btn.getText().contains("(Max)")) {
            btn.setText(btn.getText() + " (Max)");
        }
    }

    private VBox createAbilitiesMenu() {
        VBox menu = new VBox(10);
        menu.setUserData("ABILITY");

        NukeAbility nuke = gameController.getGameManager().getNukeAbility();
        String btnText = "Nuke (1500)";
        if (!nuke.isReady()) {
            btnText += " [" + nuke.getCurrentCooldown() + "]";
        }

        nukeButton = createStyledButton(btnText);
        nukeButton.setTooltip(new Tooltip("Wipes all enemy units. Cooldown: 60s"));

        if (!nuke.isReady()) {
            nukeButton.setDisable(true);
            nukeButton.setStyle(BUTTON_DISABLED_STYLE);
        }

        nukeButton.setOnAction(e -> {
            if (gameController.playerUseAbility(nuke, gameController.getGameManager().getEnemyUnits())) {
                soundManager.playNukeSound();
                AbilityRenderer renderer = new AbilityRenderer(nuke);
                battlefieldView.addAbilityRenderer(renderer);
            }
        });
        menu.getChildren().add(nukeButton);
        return menu;
    }

    /**
     * Returns the root node of the HUD.
     *
     * @return The BorderPane acting as the root node.
     */
    public BorderPane getRoot() { return root; }

    /**
     * Updates the currency and queue status labels, and refreshes button states.
     * <p>
     * This method is called every game loop tick to keep the UI in sync with the game state.
     * </p>
     */
    public void updateCurrency() {
        var gm = gameController.getGameManager();
        currencyLabel.setText("Currency: " + gm.getCurrencySystem().getBalance());
        String trainingStatus = gm.getCurrentTrainingUnit() != null ? "Training..." : "Idle";
        queueLabel.setText("Queue: " + gm.getQueueSize() + "/5 (" + trainingStatus + ")");

        if (activeMenu != null && "ABILITY".equals(activeMenu.getUserData()) && nukeButton != null) {
            NukeAbility nuke = gm.getNukeAbility();
            String text = "Nuke (1500)";
            if (!nuke.isReady()) {
                text += " [" + nuke.getCurrentCooldown() + "]";
                if (!nukeButton.isDisabled()) {
                    nukeButton.setDisable(true);
                    nukeButton.setStyle(BUTTON_DISABLED_STYLE);
                }
            } else {
                if (nukeButton.isDisabled()) {
                    nukeButton.setDisable(false);
                    nukeButton.setStyle(BUTTON_STYLE);
                }
            }
            nukeButton.setText(text);
        }
    }
}
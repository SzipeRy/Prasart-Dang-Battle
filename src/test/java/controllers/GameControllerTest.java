package controllers;

import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.GameManager;
import base.Base;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import turrets.StandardTurret;
import turrets.Turret;
import units.MeleeUnit;
import units.Unit;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController controller;

    @BeforeEach
    void setUp() {
        controller = new GameController(1000, 500);
    }

    @Test
    void testInitialization() {
        assertNotNull(controller.getGameManager());
        assertEquals(1000, controller.getGameManager().getCurrencySystem().getBalance());
        assertEquals(500, controller.getGameManager().getPlayerBase().getHp());
    }

    @Test
    void testResetGame() {
        // Change state
        controller.playerQueueUnit(new MeleeUnit(10,1,1,1,1,1));
        Unit.setUpgradedAttack(true);

        // Reset
        controller.resetGame();

        assertEquals(0, controller.getGameManager().getQueueSize());
        assertFalse(Unit.isUpgradedAttack(), "Static upgrades should be reset");
        assertEquals(1000, controller.getGameManager().getCurrencySystem().getBalance());
    }

    @Test
    void testPlayerTransactions() {
        Turret t = new StandardTurret(10, 100, 200);
        int slot = controller.playerPlaceTurret(t);

        assertNotEquals(-1, slot);
        assertEquals(800, controller.getGameManager().getCurrencySystem().getBalance());

        controller.playerSellTurret(slot);
        // Sell for half price (100)
        assertEquals(900, controller.getGameManager().getCurrencySystem().getBalance());
    }
}
package This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode;

import abilities.NukeAbility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import turrets.StandardTurret;
import units.MeleeUnit;
import units.Unit;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        Unit.resetUpgrades();
        // Give plenty of cash for tests
        gameManager = new GameManager(5000, 1000);
    }

    @Test
    void testQueueUnit() {
        Unit u = new MeleeUnit(100, 10, 10, 100, 10, 2.0);
        assertTrue(gameManager.queueUnit(u));
        assertEquals(1, gameManager.getQueueSize());

        // Test max queue
        for(int i=0; i<4; i++) gameManager.queueUnit(new MeleeUnit(100,10,10,100,10,2.0));
        assertEquals(5, gameManager.getQueueSize());

        assertFalse(gameManager.queueUnit(u), "Should fail if queue is full");
    }

    @Test
    void testTrainingLogic() {
        // Unit with very short training time for test
        Unit u = new MeleeUnit(100, 10, 10, 100, 10, 0.000000001); // Instant practically
        gameManager.queueUnit(u);

        // Tick twice (start training -> finish training)
        gameManager.tick();
        try { Thread.sleep(1); } catch (Exception e){} // Ensure nanoTime advances
        gameManager.tick();

        assertNull(gameManager.getCurrentTrainingUnit());
        assertEquals(0, gameManager.getQueueSize());
        assertEquals(1, gameManager.getPlayerUnits().size());
    }

    @Test
    void testPlaceTurret() {
        StandardTurret t = new StandardTurret(10, 100, 500);
        int slot = gameManager.placePlayerTurret(t);
        assertTrue(slot >= 0);
        assertEquals(1, gameManager.getPlayerBase().getTurrets().size());

        // Test afford check
        GameManager poorGM = new GameManager(0, 100);
        int failSlot = poorGM.placePlayerTurret(t);
        assertEquals(-1, failSlot);
    }

    @Test
    void testNukeAbilityUsage() {
        NukeAbility nuke = gameManager.getNukeAbility();
        // Ensure cost is covered
        assertTrue(gameManager.useAbility(nuke, Collections.emptyList()));
        // Cost 1500
        assertEquals(3500, gameManager.getCurrencySystem().getBalance());
        assertFalse(nuke.isReady());
    }

    @Test
    void testGameStateWinCondition() {
        // Kill player base
        gameManager.getPlayerBase().takeDamage(99999);
        GameManager.GameState state = gameManager.tick();
        assertEquals(GameManager.GameState.ENEMY_WIN, state);
    }
}
package abilities;

import interfaces.Attackable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import units.MeleeUnit;

import java.util.Arrays; import java.util.Collections; import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NukeAbilityTest {

    private NukeAbility nuke;
    private Attackable dummyTarget;

    @BeforeEach
    void setUp() {
        // Cost: 100, Cooldown: 10, Damage: 50
        nuke = new NukeAbility(100, 10, 50);
        dummyTarget = new MeleeUnit(100, 10, 10, 10, 10, 1);
    }

    @Test
    void testInitialization() {
        assertEquals(100, nuke.getCost());
        assertEquals(10, nuke.getCooldown());
        assertEquals(50, nuke.getDamage());
        assertTrue(nuke.isReady(), "Ability should be ready upon creation");
    }

    @Test
    void testActivateSingleTarget() {
        int initialHp = ((MeleeUnit) dummyTarget).getHp();
        nuke.activate(dummyTarget);

        // Damage has variance, but we expect HP to decrease
        assertTrue(((MeleeUnit) dummyTarget).getHp() < initialHp, "Target should take damage");
        assertFalse(nuke.isReady(), "Ability should be on cooldown after use");
        assertEquals(10, nuke.getCurrentCooldown());
    }

    @Test
    void testActivateListTargets() {
        Attackable t1 = new MeleeUnit(100, 10, 10, 10, 10, 1);
        Attackable t2 = new MeleeUnit(100, 10, 10, 10, 10, 1);
        List<Attackable> targets = Arrays.asList(t1, t2);

        nuke.activate(targets);

        assertTrue(((MeleeUnit) t1).getHp() < 100);
        assertTrue(((MeleeUnit) t2).getHp() < 100);
        assertFalse(nuke.isReady());
    }

    @Test
    void testCooldownLogic() {
        nuke.activate(dummyTarget);
        assertFalse(nuke.isReady());

        // Tick 9 times
        for (int i = 0; i < 9; i++) {
            nuke.tickCooldown();
            assertFalse(nuke.isReady(), "Should still be on cooldown");
        }

        // 10th tick
        nuke.tickCooldown();
        assertTrue(nuke.isReady(), "Should be ready after cooldown expires");
    }

    @Test
    void testActivateOnDeadTarget() {
        // Kill target
        dummyTarget.takeDamage(9999);
        assertFalse(dummyTarget.isAlive());

        nuke.activate(dummyTarget);

        // Should NOT trigger cooldown if target is invalid/dead (logic in activate method)
        // Wait, the code says: if (isReady() && target.isAlive()) { ... triggerCooldown() }
        // So if target is dead, cooldown should NOT trigger.
        assertTrue(nuke.isReady(), "Cooldown should not trigger if target was dead");
    }

    @Test
    void testActivateOnNullOrEmptyList() {
        nuke.activate((List<Attackable>) null);
        assertFalse(nuke.isReady());

        nuke.activate(Collections.emptyList());
        assertFalse(nuke.isReady());
    }

    @Test
    void testNegativeValues() {
        nuke.setDamage(-50);
        assertEquals(0, nuke.getDamage(), "Damage should not be negative");

        nuke.setCost(-10);
        assertEquals(0, nuke.getCost());
    }
}
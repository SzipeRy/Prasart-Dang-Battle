package turrets;

import interfaces.Attackable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import units.MeleeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TurretTest {

    private StandardTurret standardTurret;
    private LongRangeTurret longRangeTurret;

    @BeforeEach
    void setUp() {
        Turret.resetUpgrades();
        standardTurret = new StandardTurret(20, 100, 50);
        longRangeTurret = new LongRangeTurret(20, 200, 100);
    }

    @Test
    void testStandardTurretDamage() {
        Attackable dummy = new MeleeUnit(100, 10, 10, 10, 10, 1);
        int dmg = standardTurret.calculateDamage(dummy);
        // Variance check 20 * (0.75 to 1.25) -> 15 to 25
        assertTrue(dmg >= 15 && dmg <= 25);
    }

    @Test
    void testLongRangeTurretDistanceBonus() {
        // Distance > range * 0.5 (200 * 0.5 = 100). Let's say dist 150.
        // Base dmg ~20. Bonus multiplier 1.5 -> ~30.
        int dmg = longRangeTurret.calculateDamageWithDistance(150);

        // 20 * 0.75 * 1.5 = 22.5
        // 20 * 1.25 * 1.5 = 37.5
        assertTrue(dmg >= 22 && dmg <= 38);
    }

    @Test
    void testLongRangeTurretNoBonus() {
        // Distance < 100.
        int dmg = longRangeTurret.calculateDamageWithDistance(50);
        assertTrue(dmg >= 15 && dmg <= 25);
    }

    @Test
    void testCooldownLogic() {
        long now = 1_000_000_000L;
        standardTurret.setLastAttackTime(now);

        // Cooldown is 1.5s (1,500,000,000 ns)
        assertFalse(standardTurret.canFire(now + 1_000_000_000L)); // +1s
        assertTrue(standardTurret.canFire(now + 1_600_000_000L)); // +1.6s
    }

    @Test
    void testUpgrades() {
        Turret.setUpgradedAttack(true);
        Turret.setUpgradedRange(true);

        // 20 * 1.2 = 24
        assertEquals(24, standardTurret.getAttack());
        // 100 * 1.2 = 120
        assertEquals(120, standardTurret.getRange());
    }
}
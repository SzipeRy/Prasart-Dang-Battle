package systems;

import base.Base;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import turrets.Turret;
import units.Unit;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeSystemTest {

    private CurrencySystem currency;
    private UpgradeSystem upgradeSystem;

    @BeforeEach
    void setUp() {
        currency = new CurrencySystem(1000);
        upgradeSystem = new UpgradeSystem(currency);

        // Reset statics
        Unit.resetUpgrades();
        Turret.resetUpgrades();
        Base.resetUpgrades();
    }

    @Test
    void testUnitAttackUpgrade() {
        assertFalse(Unit.isUpgradedAttack());
        assertTrue(upgradeSystem.upgradeUnitAttack(500));
        assertTrue(Unit.isUpgradedAttack());
        assertEquals(500, currency.getBalance());

        // Try again
        assertFalse(upgradeSystem.upgradeUnitAttack(500), "Should not be able to upgrade twice");
        assertEquals(500, currency.getBalance());
    }

    @Test
    void testInsufficientFunds() {
        currency.setBalance(10);
        assertFalse(upgradeSystem.upgradeUnitAttack(500));
        assertFalse(Unit.isUpgradedAttack());
    }

    @Test
    void testTurretRangeUpgrade() {
        assertFalse(Turret.isUpgradedRange());
        assertTrue(upgradeSystem.upgradeTurretRange(100));
        assertTrue(Turret.isUpgradedRange());
    }

    @Test
    void testBaseCapacityUpgrade() {
        Base base = new Base(100);
        assertEquals(1, base.getTurretCapacity());

        assertTrue(upgradeSystem.upgradeBaseCapacity(base, 500));
        assertEquals(2, base.getTurretCapacity());

        // Try again (maxed out)
        assertFalse(upgradeSystem.upgradeBaseCapacity(base, 500));
        assertEquals(2, base.getTurretCapacity());
    }
}
package base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import turrets.StandardTurret;
import turrets.Turret;

import static org.junit.jupiter.api.Assertions.*;

class BaseTest {

    private Base base;

    @BeforeEach
    void setUp() {
        Base.resetUpgrades(); // Reset static capacity upgrades
        base = new Base(1000);
    }

    @Test
    void testInitialization() {
        assertEquals(1000, base.getHp());
        assertEquals(1000, base.getMaxHp());
        assertEquals(1, base.getTurretCapacity());
        assertTrue(base.getTurrets().isEmpty());
    }

    @Test
    void testTakeDamage() {
        int damage = 100;
        int actualDamage = base.takeDamage(damage);

        // Damage has variance 0.75 to 1.25
        assertTrue(actualDamage >= 75 && actualDamage <= 125);
        assertEquals(1000 - actualDamage, base.getHp());
    }

    @Test
    void testHpBounds() {
        base.setHp(-500);
        assertEquals(0, base.getHp(), "HP cannot be negative");
        assertFalse(base.isAlive());
    }

    @Test
    void testTurretManagement() {
        Turret t1 = new StandardTurret(10, 100, 10);
        Turret t2 = new StandardTurret(10, 100, 10);

        // Capacity is 1
        int slot1 = base.addTurret(t1);
        assertEquals(0, slot1, "Should add to slot 0");

        int slot2 = base.addTurret(t2);
        assertEquals(-1, slot2, "Should fail to add beyond capacity");

        assertTrue(base.removeTurret(t1));
        assertTrue(base.getTurrets().isEmpty());
    }

    @Test
    void testUpgradeCapacity() {
        assertEquals(1, base.getTurretCapacity());

        base.upgradeTurretCapacity();
        assertEquals(2, base.getTurretCapacity());

        // Try to upgrade again (max is 2 based on code logic: capacityUpgrades < 1 && capacity < 2)
        base.upgradeTurretCapacity();
        assertEquals(2, base.getTurretCapacity(), "Should not exceed max capacity of 2");
    }
}
package objects;

import base.Base;
import org.junit.jupiter.api.Test;
import units.MeleeUnit;
import units.Unit;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {

    @Test
    void testTickMovesProjectile() {
        Unit target = new MeleeUnit(100, 10, 10, 10, 10, 1);
        target.setCoordinate(100); // Target at 100

        // Start at 0, speed 10
        Projectile p = new Projectile(0, 860, 10, 10, target, "img", false);

        double initialDist = Math.abs(p.getX() - 100);
        p.tick();
        double newDist = Math.abs(p.getX() - 100);

        assertTrue(newDist < initialDist, "Projectile should move closer to target");
    }

    @Test
    void testHitTarget() {
        Unit target = new MeleeUnit(100, 10, 10, 10, 10, 1);
        target.setCoordinate(10);

        Projectile p = new Projectile(0, 840, 20, 10, target, "img", false);
        p.tick();

        // Projectile should now snap to target pos because distance (10) <= speed (20)
        assertEquals(10, p.getX());
    }

    @Test
    void testTargetDead() {
        Unit target = new MeleeUnit(100, 10, 10, 10, 10, 1);
        target.takeDamage(1000); // Kill it

        Projectile p = new Projectile(0, 0, 10, 10, target, "img", false);
        p.tick();

        assertTrue(p.hasHit(), "Projectile should mark hit/finished if target is dead");
    }

    @Test
    void testCriticalFlag() {
        Projectile p = new Projectile(0, 0, 10, 10, null, "img", true);
        assertTrue(p.isCritical());

        Projectile p2 = new Projectile(0, 0, 10, 10, null, "img");
        assertFalse(p2.isCritical());
    }
}
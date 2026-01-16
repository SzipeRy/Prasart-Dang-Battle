package units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitsTest {

    @BeforeEach
    void reset() {
        Unit.resetUpgrades();
    }

    @Test
    void testRockPaperScissorsLogic() {
        // Melee > Ranged
        Unit melee = new MeleeUnit(100, 10, 10, 10, 10, 1);
        Unit ranged = new RangedUnit(100, 10, 10, 10, 10, 1);

        int dmgMeleeToRanged = melee.calculateDamage(ranged);
        assertEquals(20, dmgMeleeToRanged, "Melee should counter Ranged (10 * 2.0)");

        // Ranged > AntiArmor
        Unit antiArmor = new AntiArmoredUnit(100, 10, 10, 10, 10, 1);
        int dmgRangedToAnti = ranged.calculateDamage(antiArmor);
        assertEquals(20, dmgRangedToAnti, "Ranged should counter AntiArmor");

        // AntiArmor > Armored
        Unit armored = new ArmoredUnit(100, 10, 10, 10, 10, 1);
        int dmgAntiToArmored = antiArmor.calculateDamage(armored);
        assertEquals(20, dmgAntiToArmored, "AntiArmor should counter Armored");

        // Armored > Melee
        int dmgArmoredToMelee = armored.calculateDamage(melee);
        assertEquals(20, dmgArmoredToMelee, "Armored should counter Melee");
    }

    @Test
    void testNormalDamage() {
        Unit melee = new MeleeUnit(100, 10, 10, 10, 10, 1);
        Unit armored = new ArmoredUnit(100, 10, 10, 10, 10, 1);

        // Melee vs Armored (Not a counter)
        int dmg = melee.calculateDamage(armored);
        assertEquals(10, dmg, "Should deal base damage");
    }

    @Test
    void testArmoredDamageReduction() {
        Unit armored = new ArmoredUnit(100, 10, 10, 10, 10, 1);
        // Base damage 100. Reduction 0.8. Result 80.
        // Then variance applies to that 80.
        int reducedDmg = armored.takeDamage(100);

        // 80 * 0.75 = 60
        // 80 * 1.25 = 100
        assertTrue(reducedDmg >= 60 && reducedDmg <= 100);
    }

    @Test
    void testUnitUpgrades() {
        Unit.setUpgradedHp(true);
        Unit.setUpgradedAttack(true);

        // Created AFTER upgrade set
        Unit u = new MeleeUnit(100, 10, 10, 10, 10, 1);

        // HP * 1.5 -> 150
        assertEquals(150, u.getMaxHp());
        assertEquals(150, u.getHp());

        // Atk * 1.2 -> 12
        assertEquals(12, u.getAttack());
    }
}
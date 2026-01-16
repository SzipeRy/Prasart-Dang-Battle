package turrets;

import interfaces.Attackable;

/**
 * A specialized turret with extended range.
 * <p>
 * This turret deals bonus damage to targets that are far away (more than 50% of max range).
 * </p>
 */
public class LongRangeTurret extends Turret {
    /**
     * Initializes a LongRangeTurret with the given stats.
     * @param attack The attack damage.
     * @param range The firing range.
     * @param cost The cost.
     */
    public LongRangeTurret(int attack, int range, int cost) {
        super(attack, range, cost);
    }

    /**
     * Calculates damage with standard variance.
     * @param target The target entity.
     * @return The calculated damage.
     */
    @Override
    public int calculateDamage(Attackable target) {
        return applyVariance(this.getAttack());
    }

    /**
     * Calculates damage based on the distance to the target.
     * <p>
     * If the target is further than 50% of the max range, the damage
     * is multiplied by {@code DAMAGE_MULTIPLIER}.
     * </p>
     * @param distance The distance to the target.
     * @return The calculated damage.
     */
    public int calculateDamageWithDistance(int distance) {
        int dmg = applyVariance(this.getAttack());
        if (distance > this.getRange() * 0.5) {
            dmg *= DAMAGE_MULTIPLIER;
        }
        return dmg;
    }
}
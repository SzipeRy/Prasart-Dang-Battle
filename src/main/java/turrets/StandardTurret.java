package turrets;

import interfaces.Attackable;

/**
 * A basic turret with balanced stats.
 * <p>
 * This turret deals standard damage with random variance.
 * </p>
 */
public class StandardTurret extends Turret {
    /**
     * Initializes a StandardTurret with the given stats.
     * @param attack The attack damage.
     * @param range The firing range.
     * @param cost The cost.
     */
    public StandardTurret(int attack, int range, int cost) {
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
}
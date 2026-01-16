package units;

import interfaces.Attackable;

/**
 * A unit which can attack from close range and can reduce damage taken.
 */
public class ArmoredUnit extends Unit
{
    /**
     * Initializes the field according to given parameters.
     * @param hp The hp of the unit.
     * @param attack The attack of the unit.
     * @param speed The speed of the unit.
     * @param cost The cost of the unit.
     * @param range The attack range of the unit.
     * @param trainingTimeSeconds The time to train of the unit.
     */
    public ArmoredUnit(int hp, int attack, int speed, int cost, int range, double trainingTimeSeconds)
    {
        super(hp, attack, speed, cost, range, trainingTimeSeconds);
    }

    /**
     * Calculate the damage when attack.
     * <p>
     *     If target is {@link MeleeUnit}, the damage is multiply by {@code COUNTER_MULTIPLIER}.
     * </p>
     * @param target The unit or base which is about to take damage.
     * @return The damage from the calculation.
     */
    @Override
    public int calculateDamage(Attackable target)
    {
        int damage = this.getAttack();
        if (target instanceof MeleeUnit)
        {
            damage *= COUNTER_MULTIPLIER;
        }
        return damage;
    }

    /**
     * Reduce the damage that the unit is taken by rounding the multiplication of baseDamage and DAMAGE_REDUCTION.
     * @param baseDamage The base amount of damage received.
     * @return The reduced damage that the unit received.
     */
    @Override
    public int takeDamage(int baseDamage)
    {
        int reducedDamage = (int) Math.round(baseDamage * DAMAGE_REDUCTION);
        return super.takeDamage(reducedDamage);
    }
}
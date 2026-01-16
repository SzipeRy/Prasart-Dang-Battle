package units;

import interfaces.Attackable;

/**
 * A unit which can attack from close range.
 */
public class MeleeUnit extends Unit
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
    public MeleeUnit(int hp, int attack, int speed, int cost, int range, double trainingTimeSeconds)
    {
        super(hp, attack, speed, cost, range, trainingTimeSeconds);
    }

    /**
     * Calculate the damage when attack.
     * <p>
     *     If target is {@link RangedUnit}, the damage is multiply by {@code COUNTER_MULTIPLIER}.
     * </p>
     * @param target The unit or base which is about to take damage.
     * @return The damage from the calculation.
     */
    @Override
    public int calculateDamage(Attackable target)
    {
        int damage = this.getAttack();
        if (target instanceof RangedUnit)
        {
            damage *= COUNTER_MULTIPLIER;
        }
        return damage;
    }
}

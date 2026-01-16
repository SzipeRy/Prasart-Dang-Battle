package units;

import interfaces.Attackable;
import java.util.Random;

/**
 * The unit which will available to attack opponent units or base.
 * Unit should never be instantiated into objects, as it is only designed to be a base class.
 */
public abstract class Unit implements Attackable {
    private int hp;
    private int maxHp;
    private int attack;
    private int speed;
    private int cost;
    private int range;
    private long trainingTime;

    private double coordinate;

    private static boolean upgradedAttack = false;
    private static boolean upgradedHp = false;

    protected static final double COUNTER_MULTIPLIER = 2.0;
    protected static final double DAMAGE_REDUCTION = 0.8;

    private static final Random RANDOM = new Random();
    private long lastAttackTime = 0;
    private long attackCooldown = 1_000_000_000L;

    /**
     * Initializes a new unit with specified base attribute and training time.
     * <p>
     * Sets hp and maxHp to hp * 1.5 if upgradedHp is {@code true}. Otherwise, sets hp and Maxhp to the hp parameter.
     * Sets attack to attack * 1.2 if upgradedAttack is {@code true}. Otherwise, sets attack to the attack parameter.
     * Sets coordinate to 0.
     * Sets trainingTime to trainingTimeSeconds in nanoseconds.
     * Sets the remaining fields to given parameters.
     * </p>
     * @param hp The hp of the unit.
     * @param attack The attack of the unit.
     * @param speed The speed of the unit.
     * @param cost The cost of the unit.
     * @param range The attack range of the unit.
     * @param trainingTimeSeconds The time to train of the unit.
     */
    public Unit(int hp, int attack, int speed, int cost, int range, double trainingTimeSeconds) {
        int finalHp = upgradedHp ? (int)(hp * 1.5) : hp;
        int finalAttack = upgradedAttack ? (int)(attack * 1.2) : attack;

        this.setHp(finalHp);
        this.setMaxHp(finalHp);
        this.setAttack(finalAttack);
        this.setSpeed(speed);
        this.setCost(cost);
        this.setRange(range);
        this.trainingTime = (long)(trainingTimeSeconds * 1_000_000_000L);
        this.coordinate = 0;
    }

    /**
     * This method will call when the unit need to calculate damage deals to the target.
     * @param target The unit or base which is about to take damage.
     * @return The damage deals to the target
     */
    public abstract int calculateDamage(Attackable target);

    /**
     * Returns the amount of damage the target is taken.
     * @param target The unit or base which is attacked.
     * @return The damage that target is taken from calculateDamage method.
     */
    public int attack(Attackable target) {
        return target.takeDamage(calculateDamage(target));
    }

    /**
     * Determines whether this unit can perform an attack at the given time.
     * <p>
     *     An attack is allowed only if the cooldown period has elapsed since the
     *     previous attack. When an attack is permitted, the last attack timestamp
     *     is updated to the provided time.
     * </p>
     *
     * @param now the current time in nanoseconds, typically obtained from
     *            {@link System#nanoTime()}
     * @return {@code true} if the unit can attack at this time; {@code false}
     *         otherwise
     */
    public boolean canAttack(long now) {
        if (now - lastAttackTime >= attackCooldown) {
            lastAttackTime = now;
            return true;
        }
        return false;
    }

    /**
     * Reduce unit's hp by damage from the calculation.
     * Calculate by rounding baseDamage multiply by 0.75 + (0.5 * random double in range of 0.0 &lt;= value &lt; 1.0).
     * @param baseDamage The base amount of damage received.
     * @return The actual damage which comes from the calculation.
     */
    @Override
    public int takeDamage(int baseDamage) {
        double variation = 0.75 + (0.5 * RANDOM.nextDouble());
        int actualDamage = (int) Math.round(baseDamage * variation);
        this.setHp(this.getHp() - actualDamage);
        return actualDamage;
    }

    /**
     * Check whether the unit is alive or not.
     * @return {@code true} if unit's hp is more than 0, {@code false} otherwise.
     */
    @Override
    public boolean isAlive() {
        return this.getHp() > 0;
    }

    /**
     * Sets upgradedAttack and upgradedHp to false.
     */
    // NEW: Method to reset upgrades between games
    public static void resetUpgrades() {
        upgradedAttack = false;
        upgradedHp = false;
    }

    /**
     * Returns hp of the unit.
     * @return The hp of the unit as int.
     */
    public int getHp() { return hp; }

    /**
     * Returns maxHp of the unit.
     * @return The maxHp of the unit as int.
     */
    public int getMaxHp() { return maxHp; }

    /**
     * Returns attack of the unit.
     * @return The attack of the unit as int.
     */
    public int getAttack() { return attack; }

    /**
     * Returns speed of the unit.
     * @return The speed of the unit as int.
     */
    public int getSpeed() { return speed; }

    /**
     * Sets speed of the unit.
     * @param speed The new speed for the unit.
     */
    public void setSpeed(int speed) { this.speed = speed; }

    /**
     * Returns cost of the unit.
     * @return The cost of the unit as int.
     */
    public int getCost() { return cost; }

    /**
     * Sets cost of the unit.
     * @param cost The new cost for the unit.
     */
    public void setCost(int cost) { this.cost = cost; }

    /**
     * Returns range of the unit.
     * @return The range of the unit as int
     */
    public int getRange() { return range; }

    /**
     * Sets range of the unit.
     * @param range The new range for the unit.
     */
    public void setRange(int range) { this.range = range; }

    /**
     * Returns trainingTime of the unit.
     * @return The trainingTime of the unit as long.
     */
    public long getTrainingTime() { return trainingTime; }

    /**
     * Returns coordinate of the unit.
     * @return The coordinate of the unit as double.
     */
    public double getCoordinate() { return coordinate; }

    /**
     * Sets the coordinate of the unit
     * @param coordinate The new coordinate for the unit.
     */
    public void setCoordinate(double coordinate) { this.coordinate = coordinate; }

    /**
     * Sets the hp of the unit.
     * <p>
     *     hp cannot be less than 0.
     * </p>
     * @param hp The new hp for the unit.
     */
    public void setHp(int hp) { this.hp = Math.max(hp, 0); }

    /**
     * Sets the maxHp of the unit.
     * <p>
     *     maxHp cannot be less than 0.
     * </p>
     * @param maxHp The new maxHp for the unit.
     */
    public void setMaxHp(int maxHp) { this.maxHp = Math.max(maxHp, 0); }

    /**
     * Sets the attack of the unit
     * <p>
     *     attack cannot be less than 0.
     * </p>
     * @param attack The new attack for the unit.
     */
    public void setAttack(int attack) { this.attack = Math.max(attack, 0); }


    /**
     * Check if upgradedAttack is enabled.
     * @return {@code true} if the upgradedAttack is enabled, {@code false} otherwise.
     */
    public static boolean isUpgradedAttack() { return upgradedAttack; }

    /**
     * Sets upgradedAttack of the unit.
     * @param upgradedAttack The new upgradedAttack for the unit.
     */
    public static void setUpgradedAttack(boolean upgradedAttack) { Unit.upgradedAttack = upgradedAttack; }

    /**
     * Check if upgradedHp is enabled.
     * @return {@code true} if the upgradedHp is enabled, {@code false} otherwise.
     */
    public static boolean isUpgradedHp() { return upgradedHp; }

    /**
     * Sets upgradedHp of the unit.
     * @param upgradedHp The new upgradedHp for the unit.
     */
    public static void setUpgradedHp(boolean upgradedHp) { Unit.upgradedHp = upgradedHp; }
}
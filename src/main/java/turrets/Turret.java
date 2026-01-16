package turrets;

import interfaces.Attackable;
import java.util.Random;

/**
 * Abstract base class for all defensive turrets.
 * <p>
 * This class defines the common properties of turrets such as attack power, range, cost,
 * and cooldowns. It also manages global upgrades for turret stats.
 * </p>
 */
public abstract class Turret {
    private int attack;
    private int range;
    private int cost;
    private long cooldown;
    private long lastAttackTime;

    private static boolean upgradedAttack = false;
    private static boolean upgradedRange = false;

    protected static final double DAMAGE_MULTIPLIER = 1.5;

    private static final Random RANDOM = new Random();

    /**
     * Initializes a new Turret with specified attack power, range, and cost.
     * Sets the default cooldown to 1.5 seconds and last attack time to 0.
     * @param attack The attack damage of the turret.
     * @param range The firing range of the turret.
     * @param cost The purchase cost of the turret.
     */
    public Turret(int attack, int range, int cost) {
        this.setAttack(attack);
        this.setRange(range);
        this.setCost(cost);
        this.setCooldown(1_500_000_000L);
        this.setLastAttackTime(0);
    }

    /**
     * Calculates the damage this turret deals to a specific target.
     * @param target The target Attackable entity.
     * @return The calculated damage amount.
     */
    public abstract int calculateDamage(Attackable target);

    /**
     * Applies a random variance to the base damage.
     * The variation is between 0.75 and 1.25 of the base damage.
     * @param baseDamage The initial damage amount.
     * @return The damage after applying variance.
     */
    protected int applyVariance(int baseDamage) {
        double variation = 0.75 + (0.5 * RANDOM.nextDouble());
        return (int) Math.round(baseDamage * variation);
    }

    /**
     * Checks if the turret is ready to fire based on its cooldown.
     * Updates the last attack time if it fires.
     * @param now The current game time in nanoseconds.
     * @return {@code true} if the cooldown has elapsed, {@code false} otherwise.
     */
    public boolean canFire(long now) {
        if (now - this.getLastAttackTime() >= this.getCooldown()) {
            this.setLastAttackTime(now);
            return true;
        }
        return false;
    }

    /**
     * Resets the cooldown timer to the current time.
     * @param now The current game time in nanoseconds.
     */
    public void resetCooldown(long now) { this.setLastAttackTime(now); }

    /**
     * Resets the static upgrade flags for all turrets to false.
     */
    public static void resetUpgrades() { upgradedAttack = false; upgradedRange = false; }

    /**
     * Returns the attack damage of the turret, applying the upgrade multiplier if active.
     * @return The effective attack damage.
     */
    public int getAttack() { return upgradedAttack ? (int)(attack * 1.2) : attack; }

    /**
     * Returns the range of the turret, applying the upgrade multiplier if active.
     * @return The effective range.
     */
    public int getRange() { return upgradedRange ? (int)(range * 1.2) : range; }

    /**
     * Returns the cost of the turret.
     * @return The cost.
     */
    public int getCost() { return cost; }

    /**
     * Returns the cooldown duration of the turret in nanoseconds.
     * @return The cooldown duration.
     */
    public long getCooldown() { return cooldown; }

    /**
     * Returns the timestamp of the last attack in nanoseconds.
     * @return The last attack time.
     */
    public long getLastAttackTime() { return lastAttackTime; }

    /**
     * Sets the base attack damage of the turret.
     * @param attack The new attack value (must be non-negative).
     */
    public void setAttack(int attack) { this.attack = Math.max(attack, 0); }

    /**
     * Sets the base range of the turret.
     * @param range The new range value (must be non-negative).
     */
    public void setRange(int range) { this.range = Math.max(range, 0); }

    /**
     * Sets the cost of the turret.
     * @param cost The new cost value (must be non-negative).
     */
    public void setCost(int cost) { this.cost = Math.max(cost, 0); }

    /**
     * Sets the cooldown duration of the turret.
     * @param cooldown The new cooldown duration in nanoseconds (must be non-negative).
     */
    public void setCooldown(long cooldown) { this.cooldown = Math.max(cooldown, 0); }

    /**
     * Sets the timestamp of the last attack.
     * @param lastAttackTime The new last attack time (must be non-negative).
     */
    public void setLastAttackTime(long lastAttackTime) { this.lastAttackTime = Math.max(lastAttackTime, 0); }

    /**
     * Checks if the turret attack upgrade is active.
     * @return {@code true} if upgraded, {@code false} otherwise.
     */
    public static boolean isUpgradedAttack() { return upgradedAttack; }

    /**
     * Sets the status of the turret attack upgrade.
     * @param upgradedAttack The new upgrade status.
     */
    public static void setUpgradedAttack(boolean upgradedAttack) { Turret.upgradedAttack = upgradedAttack; }

    /**
     * Checks if the turret range upgrade is active.
     * @return {@code true} if upgraded, {@code false} otherwise.
     */
    public static boolean isUpgradedRange() { return upgradedRange; }

    /**
     * Sets the status of the turret range upgrade.
     * @param upgradedRange The new upgrade status.
     */
    public static void setUpgradedRange(boolean upgradedRange) { Turret.upgradedRange = upgradedRange; }
}
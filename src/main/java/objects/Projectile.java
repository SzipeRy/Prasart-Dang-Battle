package objects;

import interfaces.Attackable;
import units.Unit;
import base.Base;

/**
 * Represents a projectile fired by a unit or turret in the game.
 * <p>
 * Projectiles travel towards a target and deal damage upon impact.
 * They handle their own movement logic and hit detection in the {@link #tick()} method.
 * </p>
 */
public class Projectile {
    private double x;
    private double y;
    private final double speed;
    private final int damage;
    private final Attackable target;
    private boolean hit;
    private final String imagePath;
    private final boolean isCritical;

    /**
     * Initializes the fields according to given parameters and sets hit to false.
     * @param startX The beginning X-Coordinate of the projectile.
     * @param startY The beginning Y-Coordinate of the projectile.
     * @param speed The speed of the projectile.
     * @param damage The damage deal to target of the projectile.
     * @param target The target which projectile is heading toward to.
     * @param imagePath The image file of projectile.
     * @param isCritical The status which projectile deals critical damage or not.
     */
    public Projectile(double startX, double startY, double speed, int damage, Attackable target, String imagePath, boolean isCritical) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.damage = damage;
        this.target = target;
        this.hit = false;
        this.imagePath = imagePath;
        this.isCritical = isCritical;
    }

    /**
     * Legacy constructor for backward compatibility or non-critical projectiles.
     */
    public Projectile(double startX, double startY, double speed, int damage, Attackable target, String imagePath) {
        this(startX, startY, speed, damage, target, imagePath, false);
    }

    /**
     * Updates the projectile's state for a single game tick.
     * <p>
     * This method moves the projectile toward its target. If the target no longer
     * exists or is no longer alive, the projectile is marked as a hit and stops
     * updating. The destination point is determined based on the target type:
     * <ul>
     * <li>If the target is a {@link Unit}, the projectile aims toward the unit’s
     * approximate center.</li>
     * <li>If the target is a {@link Base}, the projectile aims toward the visual
     * center of the base structure, adjusting for attack direction.</li>
     * </ul>
     * The projectile advances toward the computed position based on its speed and
     * snaps to the target location when it reaches or surpasses the remaining
     * distance.
     *
     */
    public void tick() {
        if (target == null || !target.isAlive()) {
            this.hit = true;
            return;
        }

        double targetX = x;
        double targetY = y;

        if (target instanceof Unit u) {
            targetX = u.getCoordinate();
            targetY = 840;
        } else if (target instanceof Base) {
            if (this.speed > 0) {
                targetX = 2780;
            } else {
                targetX = 220;
            }
            targetY = 820;
        }

        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= Math.abs(speed)) {
            this.x = targetX;
            this.y = targetY;
        } else {
            double moveX = (dx / distance) * Math.abs(speed);
            double moveY = (dy / distance) * Math.abs(speed);
            this.x += moveX;
            this.y += moveY;
        }
    }

    /**
     * Returns X-Coordinate of the projectile.
     * @return The x of projectile as double.
     */
    public double getX() { return x; }

    /**
     * Returns Y-Coordinate of the projectile.
     * @return The y of projectile as double.
     */
    public double getY() { return y; }

    /**
     * Returns damge of the projectile.
     * @return The damage of projectile as int.
     */
    public int getDamage() { return damage; }

    /**
     * Returns target of the projectile.
     * @return The target of projectile as Attackable.
     */
    public Attackable getTarget() { return target; }

    /**
     * Check whether projectile has hit or not.
     * @return The hit of projectile as boolean.
     */
    public boolean hasHit() { return hit; }

    /**
     * Sets hit of the projectile.
     * @param hit The new hit of projectile.
     */
    public void setHit(boolean hit) { this.hit = hit; }

    /**
     * Returns imagePath of the projectile.
     * @return The imagePath of projectile as String.
     */
    public String getImagePath() { return imagePath; }

    /**
     * Check whether the projectile is critical or not.
     * @return {@code true} if isCritical is true, {@code false} otherwise.
     */
    public boolean isCritical() { return isCritical; }
}
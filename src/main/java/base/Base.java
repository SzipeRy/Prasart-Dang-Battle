package base;

import interfaces.Attackable;
import turrets.Turret;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The base of the player for deploying Unit and setup Turret; if hp is 0 or less than 0, the game ends.
 */
public class Base implements Attackable
{
    private int hp;
    private int maxHp;
    private final List<Turret> TURRETS;
    private int turretCapacity;

    private static int capacityUpgrades = 0;

    private static final Random random = new Random();

    /**
     * Initializes the field according to the given parameter, sets turretCapacity to 1,
     * and initializes Arraylist&lt;&gt; for TURRETS.
     * @param hp Health of the base.
     */
    public Base(int hp)
    {
        this.setHp(hp);
        this.setMaxHp(hp);
        this.TURRETS = new ArrayList<>();
        this.setTurretCapacity(1);
    }

    /**
     * Adds new turret into TURRETS if TURRETS size is less than turretCapacity.
     * @param turret The new turret to add into TURRETS list.
     * @return TURRETS size minus by 1 if add successfully. Otherwise, return -1.
     */
    public int addTurret(Turret turret) {
        if (TURRETS.size() < turretCapacity) {
            TURRETS.add(turret);
            return TURRETS.size() - 1;
        }
        return -1;
    }

    /**
     * Removes the turret from TURRETS list.
     * @param turret The turret to be removed.
     * @return True if the turret was found and removed, and false otherwise.
     */
    public boolean removeTurret(Turret turret)
    {
        return TURRETS.remove(turret);
    }

    /**
     * Returns list of TURRETS.
     * @return The TURRETS of the base as List&lt;Turret&gt;.
     */
    public List<Turret> getTurrets()
    {
        return TURRETS;
    }

    /**
     * Increase turretCapacity and capacityUpgrades by 1 if capacityUpgrades is less than 1 and turretCapacity is less than 2.
     */
    public void upgradeTurretCapacity()
    {
        if (capacityUpgrades < 1 && this.getTurretCapacity() < 2)
        {
            this.setTurretCapacity(this.getTurretCapacity() + 1);
            capacityUpgrades++;
        }
    }

    /**
     * Sets capacityUpgrades to 0.
     */
    public static void resetUpgrades() {
        capacityUpgrades = 0;
    }

    /**
     * Reduce base's hp by damage from the calculation.
     * Calculate by rounding baseDamage multiply by 0.75 + (0.5 * random double in range of 0.0 &lt;= value &lt; 1.0).
     * @param baseDamage The damage that the base is received without further calculation.
     * @return The acutal damage that is taking
     */
    @Override
    public int takeDamage(int baseDamage)
    {
        double variation = 0.75 + (0.5 * random.nextDouble());
        int actualDamage = (int) Math.round(baseDamage * variation);
        this.setHp(this.getHp() - actualDamage);
        return actualDamage;
    }

    /**
     * Check if base's hp is more than 0.
     * @return {@code true} if hp is more than 0,{@code false} otherwise.
     */
    @Override
    public boolean isAlive()
    {
        return this.getHp() > 0;
    }

    /**
     * Returns the hp of base.
     * @return The hp of base as int.
     */
    public int getHp()
    {
        return hp;
    }

    /**
     * Returns turretCapacity of base.
     * @return The turretCapacity of base as int.
     */
    public int getTurretCapacity()
    {
        return turretCapacity;
    }

    /**
     * Sets the hp of the base.
     * hp cannot be less than 0.
     * @param hp The new hp for the base.
     */
    public void setHp(int hp)
    {
        this.hp = Math.max(hp, 0);
    }

    /**
     * Sets the turretCapacity of the base.
     * It cannot be more than 2 and has to be at least 1.
     * @param turretCapacity
     */
    public void setTurretCapacity(int turretCapacity)
    {
        this.turretCapacity = Math.min(Math.max(turretCapacity, 1), 2);
    }

    /**
     * Returns the maxHp of the base.
     * @return The maxHp of the base as int.
     */
    public int getMaxHp()
    {
        return maxHp;
    }

    /**
     * Sets the maxHp of the base.
     * It cannot be less than 0.
     * @param maxHp The new maxHp for the base.
     */
    public void setMaxHp(int maxHp)
    {
        this.maxHp = Math.max(maxHp, 0);
    }

    /**
     * Returns capacityUpgrades of the base.
     * @return The capacityUpgrades of the base as int.
     */
    public static int getCapacityUpgrades()
    {
        return capacityUpgrades;
    }
}
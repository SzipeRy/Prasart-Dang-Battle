package systems;

import base.Base;
import turrets.Turret;
import units.Unit;

/**
 * Upgrading System for Unit Attack, Unit HP, Turret Attack, Turret Range and Turret Capacity.
 */
public class UpgradeSystem
{
    private final CurrencySystem currency;

    /**
     * Initializes the field according to the given parameters.
     * @param currency The amount of currency to purchase upgrades.
     */
    public UpgradeSystem(CurrencySystem currency)
    {
        this.currency = currency;
    }

    /**
     * Sets upgradedAttack of Unit class to true if Unit.isUpgradedAttack is false and currency.spend(cost) is true.
     * @param cost The amount of currency to purchase the upgrade.
     * @return {@code true} if upgrade successfully, {@code false} otherwise.
     */
    public boolean upgradeUnitAttack(int cost)
    {
        if (!Unit.isUpgradedAttack() && currency.spend(cost))
        {
            Unit.setUpgradedAttack(true);
            return true;
        }
        return false;
    }

    /**
     * Sets upgradedHp of Unit class to true if Unit.isUpgradedHp is false and currency.spend(cost) is true.
     * @param cost The amount of currency to purchase the upgrade.
     * @return {@code true} if upgrade successfully, {@code false} otherwise.
     */
    public boolean upgradeUnitHp(int cost)
    {
        if (!Unit.isUpgradedHp() && currency.spend(cost))
        {
            Unit.setUpgradedHp(true);
            return true;
        }
        return false;
    }

    /**
     * Sets upgradedAttack of Turret class to true if Turret.isUpgradedAttack is false and currency.spend(cost) is true.
     * @param cost The amount of currency to purchase the upgrade.
     * @return {@code true} if upgrade successfully, {@code false} otherwise.
     */
    public boolean upgradeTurretAttack(int cost)
    {
        if (!Turret.isUpgradedAttack() && currency.spend(cost))
        {
            Turret.setUpgradedAttack(true);
            return true;
        }
        return false;
    }

    /**
     * Sets upgradedRange of Turret class to true if Turret.isUpgradedRange is false and currency.spend(cost) is true.
     * @param cost The amount of currency to purchase the upgrade.
     * @return {@code true} if upgrade successfully, {@code false} otherwise.
     */
    public boolean upgradeTurretRange(int cost)
    {
        if (!Turret.isUpgradedRange() && currency.spend(cost))
        {
            Turret.setUpgradedRange(true);
            return true;
        }
        return false;
    }

    /**
     * Calls upgradeTurretCapacity of the base parameter if Base.getCapacityUpgrades is less than 1 and currency.spend(cost) is true.
     * @param base The base to upgrade turret's capacity
     * @param cost The amount of currency to purchase the upgrade.
     * @return {@code true} if upgrade successfully, {@code false} otherwise.
     */
    public boolean upgradeBaseCapacity(Base base, int cost) {
        if (Base.getCapacityUpgrades() < 1 && currency.spend(cost)) {
            base.upgradeTurretCapacity();
            return true;
        }
        return false;
    }
}
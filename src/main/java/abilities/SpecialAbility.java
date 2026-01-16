package abilities;

import interfaces.Attackable;

/**
 * An abstract class on special ability which player can choose to use.
 */
public abstract class SpecialAbility
{
    private int cost;
    private int cooldown;
    private int currentCooldown;

    /**
     * Initialize the field according to given parameters.
     * Set currentCooldown to 0.
     * @param cost Cost on using the ability
     * @param cooldown Cooldown of the ability
     */
    public SpecialAbility(int cost, int cooldown)
    {
        this.setCost(cost);
        this.setCooldown(cooldown);
        this.setCurrentCooldown(0);
    }

    /**
     * This method activate ability onto the target.
     * @param target The opponent target unit.
     */
    public abstract void activate(Attackable target);

    /**
     * Returns whether ability is ready or not. If ready, returns true. Otherwise, returns false.
     * @return True if currentCooldown is 0. Otherwise, return false.
     */
    public boolean isReady()
    {
        return this.getCurrentCooldown() == 0;
    }

    /**
     * Set the currentCooldown to ability's cooldown.
     */
    protected void triggerCooldown()
    {
        this.setCurrentCooldown(this.getCooldown());
    }

    /**
     * If currentCooldown is more than 0, reduce it by 1.
     */
    public void tickCooldown() {
        if (this.getCurrentCooldown() > 0)
        {
            this.setCurrentCooldown(this.getCurrentCooldown() - 1);
        }
    }

    /**
     * Returns the cost of the ability.
     * @return The cost of ability as int.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets the cost of the ability.
     * cost cannot be less than 0.
     * @param cost The new cost for the ability.
     */
    public void setCost(int cost) {
        this.cost = Math.max(cost, 0);
    }

    /**
     * Returns the cooldown of the ability.
     * @return The cooldown of ability as int.
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Sets the cooldown of the ability.
     * cooldown cannot be less than 0.
     * @param cooldown The new cooldown for the ability.
     */
    public void setCooldown(int cooldown) {
        this.cooldown = Math.max(cooldown, 0);
    }

    /**
     * Returns the currentCooldown of the ability.
     * @return The currentCooldown of ability as int.
     */
    public int getCurrentCooldown() {
        return currentCooldown;
    }

    /**
     * Sets the currentCooldown of the ability.
     * currentCooldown cannot be less than 0.
     * @param currentCooldown The new currentCooldown of ability as int.
     */
    public void setCurrentCooldown(int currentCooldown) {
        this.currentCooldown = Math.max(currentCooldown, 0);
    }
}

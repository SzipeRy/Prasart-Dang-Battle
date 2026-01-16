package abilities;

import interfaces.Attackable;

import java.util.List;

/**
 * The ability to nuke on opponent unit.
 */
public class NukeAbility extends SpecialAbility
{
    private int damage;

    /**
     * Initialize the fields according to given parameters.
     * @param cost Cost on using NukeAbility.
     * @param cooldown Cooldown after using NukeAbility.
     * @param damage Damage deals to target of NukeAbility.
     */
    public NukeAbility(int cost, int cooldown, int damage)
    {
        super(cost, cooldown);
        this.setDamage(damage);
    }

    /**
     * If NukeAbility is ready and target is , deals damage to target and trigger the cooldown.
     * @param target The opponent target unit.
     */
    @Override
    public void activate(Attackable target)
    {
        if (isReady() && target.isAlive())
        {
            target.takeDamage(this.getDamage());
            triggerCooldown();
        }
    }

    /**
     * If NukeAbility is ready, targets is not null and targets is not empty, deals damage to each alive target
     * and trigger cooldown after finished.
     * @param targets List of target.
     */
    public void activate(List<? extends Attackable> targets)
    {
        if (isReady() && targets != null && !targets.isEmpty())
        {
            for (Attackable target : targets)
            {
                if (target.isAlive())
                {
                    target.takeDamage(this.getDamage());
                }
            }
        }
        triggerCooldown();
    }

    /**
     * Returns damage of NukeAbility.
     * @return The damage of NukeAbility as int.
     */
    public int getDamage()
    {
        return damage;
    }

    /**
     * Sets the damage of NukeAbility.
     * damage cannot be less than 0.
     * @param damage The new damge of NukeAbility.
     */
    public void setDamage(int damage)
    {
        this.damage = Math.max(damage, 0);
    }
}

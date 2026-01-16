package interfaces;

/**
 * Interface representing any entity that can take damage and has a life state.
 * <p>
 * This is implemented by Units and Bases to standardize combat interactions.
 * </p>
 */
public interface Attackable
{
    /**
     * This method will call when Base or Unit get attacked.
     * @param amount The base amount of damage received.
     * @return The actual damage.
     */
    int takeDamage(int amount);

    /**
     * This method will check whether Base or Unit is alive or not.
     * @return {@code true} if hp is more than 0,{@code false} otherwise.
     */
    boolean isAlive();
}
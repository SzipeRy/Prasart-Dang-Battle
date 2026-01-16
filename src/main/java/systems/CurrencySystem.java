package systems;

/**
 * The currency in-game for purchasing units, turrets or upgrades.
 */
public class CurrencySystem
{
    private int balance;

    /**
     * Intializes the field according to the given parameter.
     * @param startingBalance
     */
    public CurrencySystem(int startingBalance)
    {
        this.setBalance(startingBalance);
    }

    /**
     * Add amount into your balance.
     * @param amount The amount of balance you have gained.
     */
    public void earn(int amount)
    {
        if (amount > 0)
        {
            this.setBalance(this.getBalance() + amount);
        }
    }

    /**
     * Remove amount from your balance.
     * <p>
     *     Your balance will be removed by amount if amount is more than 0 and canAfford is true.
     * </p>
     * @param amount The amount of balance you have spent.
     * @return {@code true} if spend success, {@code false} otherwise.
     */
    public boolean spend(int amount)
    {
        if (amount > 0 && this.canAfford(amount))
        {
            this.setBalance(this.getBalance() - amount);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Check if your balance is more than the parameter.
     * @param amount The amount to check your balance can afford or not
     * @return {@code true} if your balance is at least number of amount, {@code false} otherwise.
     */
    public boolean canAfford(int amount)
    {
        return this.getBalance() >= amount;
    }

    /**
     * Returns the balance.
     * @return The balance of CurrencySystem as int.
     */
    public int getBalance()
    {
        return balance;
    }

    /**
     * Sets the balance; needs to be at least 0.
     * @param balance The new balance for CurrencySystem.
     */
    public void setBalance(int balance)
    {
        this.balance = Math.max(balance, 0);
    }
}

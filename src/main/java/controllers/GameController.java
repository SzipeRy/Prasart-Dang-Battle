package controllers;

import abilities.SpecialAbility;
import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.GameManager;
import This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode.GameManager.GameState;
import turrets.Turret;
import units.Unit;
import base.Base;

import java.util.List;

/**
 * Controller class that bridges the game logic (GameManager) and the user interface.
 * <p>
 * This class handles player actions such as placing turrets, queuing units,
 * and using abilities, delegating the actual logic execution to the GameManager.
 * It also manages the initialization and resetting of the game state.
 * </p>
 */
public class GameController
{
    private GameManager gameManager;

    private final int startCurrency;
    private final int baseHp;

    /**
     * Initializes the fields according to given parameters and initializes gamaManager with the given parameters as parameters.
     * @param startingCurrency The currency which is given at start.
     * @param baseHp The hp of the base.
     */
    public GameController(int startingCurrency, int baseHp)
    {
        this.startCurrency = startingCurrency;
        this.baseHp = baseHp;
        this.gameManager = new GameManager(startingCurrency, baseHp);
    }

    /**
     * Reset upgrades on unit, turret, and base, then initializes gameManager with startCurrency and baseHp as paramters.
     */
    public void resetGame() {
        Unit.resetUpgrades();
        Turret.resetUpgrades();
        Base.resetUpgrades();

        this.gameManager = new GameManager(startCurrency, baseHp);
    }

    /**
     * Calls queueUnit method in gameManager class with the given parameter.
     * @param unit The unit which player want to add in queue.
     * @return Return value of gameManager.queueUnit method.
     */
    public boolean playerQueueUnit(Unit unit) {
        return gameManager.queueUnit(unit);
    }

    /**
     * Calls placePlayerTurret method in gameManage class with the given parameter.
     * @param turret The turret which player want to place.
     * @return Return value of gameManager.placePlayerTurret method.
     */
    public int playerPlaceTurret(Turret turret) {
        return gameManager.placePlayerTurret(turret);
    }

    /**
     * Calls sellTurret method in gameManager class with the given parameter.
     * @param slotIndex The slot number of player's turret.
     */
    public void playerSellTurret(int slotIndex) {
        gameManager.sellTurret(slotIndex);
    }

    /**
     * Calls useAbility method in gameManager class with given parameters.
     * @param ability The ability player want to use.
     * @param targets List of target which is targeted by the ability.
     * @return Return value of gameManager.useAbility method.
     */
    public boolean playerUseAbility(SpecialAbility ability, List<Unit> targets)
    {
        return gameManager.useAbility(ability, targets);
    }

    /**
     * Calls upgradeUnitAttack method in gameManager class with the given parameter.
     * @param cost The cost to upgrade player's unit attack.
     */
    public void playerUpgradeUnitAttack(int cost)
    {
        gameManager.upgradeUnitAttack(cost);
    }

    /**
     * Calls playerUpgradeUnitHp method in gameManager class with the given parameter.
     * @param cost The cost to upgrade player's unit hp.
     */
    public void playerUpgradeUnitHp(int cost)
    {
        gameManager.upgradeUnitHp(cost);
    }

    /**
     * Calls upgradeTurretAttack method in gameManager class with the given parameter.
     * @param cost The cost to upgrade player's turret attack.
     */
    public void playerUpgradeTurretAttack(int cost)
    {
        gameManager.upgradeTurretAttack(cost);
    }

    /**
     * Calls upgradeTurretRange method in gameManager class with the given parameter.
     * @param cost The cost to upgrade player's turret attacking range.
     */
    public void playerUpgradeTurretRange(int cost)
    {
        gameManager.upgradeTurretRange(cost);
    }

    /**
     * Calls upgradeBaseCapacity method in gameManager class with the given parameter.
     * @param cost The cost to upgrade player's base capacity.
     */
    public void playerUpgradeBaseCapacity(int cost) {
        gameManager.upgradeBaseCapacity(cost);
    }

    /**
     * Returns tick method in gameManager class.
     * @return Return value of tick method in gameManager class as GameState.
     */
    public GameState nextTurn()
    {
        return gameManager.tick();
    }

    /**
     * Returns the gameManager of this class.
     * @return The gameManager of this class as GameManager type value.
     */
    public GameManager getGameManager()
    {
        return gameManager;
    }
}
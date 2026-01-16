package This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode;

import abilities.NukeAbility;
import abilities.SpecialAbility;
import base.Base;
import interfaces.Attackable;
import systems.CurrencySystem;
import systems.UpgradeSystem;
import turrets.Turret;
import turrets.LongRangeTurret;
import units.*;
import objects.Projectile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * The core logic engine for the game.
 * <p>
 * This class maintains the state of the entire battlefield, including units, turrets,
 * bases, projectiles, and currency. It handles the game loop (tick) updates,
 * enemy AI spawning, combat calculations, and win/loss conditions.
 * </p>
 */
public class GameManager {
    public enum GameState { PLAYER_WIN, ENEMY_WIN, ONGOING }

    private static final double PLAYER_SPAWN_X = 150.0;
    private static final double ENEMY_SPAWN_X = 2750.0;
    public static final double PLAYER_BASE_X = 150.0;
    public static final double ENEMY_BASE_X = 2850.0;
    private static final double BASE_HITBOX_DIST = 50.0;
    private static final double COLLISION_RADIUS = 75.0;

    private final CurrencySystem currencySystem;
    private final UpgradeSystem upgradeSystem;
    private final Base playerBase;
    private final Base enemyBase;

    private final List<Unit> playerUnits;
    private final List<Unit> enemyUnits;
    private final List<Turret> playerTurrets;
    private final List<Turret> enemyTurrets;

    private final List<Projectile> projectiles;

    private final List<SpecialAbility> abilities;
    private final NukeAbility nukeAbility;
    private final Queue<Unit> trainingQueue;
    private static final int MAX_QUEUE_SIZE = 5;
    private Unit currentTrainingUnit = null;
    private long trainingStartTime = 0;
    private long lastCurrencyRegenTime = 0;
    private static final long CURRENCY_REGEN_INTERVAL = 1_000_000_000L;
    private final Random random = new Random();
    private long nextEnemySpawnTime = 0;

    private static final long DELAY_AFTER_MELEE = 2_500_000_000L;
    private static final long DELAY_AFTER_RANGED = 2_500_000_000L;
    private static final long DELAY_AFTER_ANTI_ARMOR = 3_500_000_000L;
    private static final long DELAY_AFTER_ARMORED = 5_500_000_000L;

    public record DamageEvent(double x, double y, int amount, boolean isCritical, String damageType, boolean isBase) {}
    private final List<DamageEvent> recentDamageEvents = new ArrayList<>();

    /**
     * Initializes the game manager with starting currency and base health.
     * @param startingCurrency The initial amount of currency for the player.
     * @param baseHp The maximum and initial health for both player and enemy bases.
     */
    public GameManager(int startingCurrency, int baseHp) {
        this.currencySystem = new CurrencySystem(startingCurrency);
        this.upgradeSystem = new UpgradeSystem(this.currencySystem);
        this.playerBase = new Base(baseHp);
        this.enemyBase = new Base(baseHp);

        this.playerUnits = new ArrayList<>();
        this.enemyUnits = new ArrayList<>();
        this.playerTurrets = new ArrayList<>();
        this.enemyTurrets = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        this.abilities = new ArrayList<>();
        this.nukeAbility = new NukeAbility(1500, 60, 9999);
        this.abilities.add(nukeAbility);
        this.trainingQueue = new LinkedList<>();
    }

    /**
     * Updates the game state for a single frame (tick).
     * <p>
     * This method handles currency regeneration, cooldowns, unit training,
     * enemy AI spawning, turret updates, unit movement and collision,
     * projectile updates, and win/loss condition checks.
     * </p>
     * @return The current state of the game (ONGOING, PLAYER_WIN, or ENEMY_WIN).
     */
    public GameState tick() {
        long now = System.nanoTime();
        recentDamageEvents.clear();

        if (now - lastCurrencyRegenTime >= CURRENCY_REGEN_INTERVAL) {
            currencySystem.earn(1);
            for (SpecialAbility ability : abilities) ability.tickCooldown();
            lastCurrencyRegenTime = now;
        }

        updateTraining(now);
        updateEnemyAI(now);

        updateTurrets(playerTurrets, enemyUnits, now);
        updateTurrets(enemyTurrets, playerUnits, now);

        updateUnits(playerUnits, enemyUnits, enemyBase, 1, now);
        updateUnits(enemyUnits, playerUnits, playerBase, -1, now);

        updateProjectiles();

        playerUnits.removeIf(u -> !u.isAlive());
        enemyUnits.removeIf(u -> {
            if(!u.isAlive()) {
                int reward = (int)(u.getCost() * 1.25);
                currencySystem.earn(reward);
                return true;
            }
            return false;
        });

        if (!playerBase.isAlive()) return GameState.ENEMY_WIN;
        if (!enemyBase.isAlive()) return GameState.PLAYER_WIN;

        return GameState.ONGOING;
    }

    /**
     * Updates and resolves all active projectiles in the game.
     * <p>
     *     Each projectile is advanced by one tick and removed if it has already hit
     *     its target, no longer has a valid target, or reaches effective hit distance.
     *     When a projectile impacts a valid target, damage is applied, damage events
     *     are recorded for visual feedback, and the projectile is removed.
     * </p>
     * <p>
     *     Hit detection accounts for different target types by using distinct distance
     *     thresholds and impact positions for units and bases.
     * </p>
     */
    private void updateProjectiles() {
        projectiles.removeIf(p -> {
            p.tick();

            if (p.hasHit()) return true;

            Attackable target = p.getTarget();
            if (target != null && target.isAlive()) {
                double targetX = p.getX();
                double targetY = p.getY();

                boolean isBaseTarget = (target instanceof Base);

                if (target instanceof Unit u) {
                    targetX = u.getCoordinate();
                    targetY = 860;
                } else if (isBaseTarget) {
                    if (target == playerBase) { targetX = 220; targetY = 800; }
                    else { targetX = 2780; targetY = 800; }
                }

                double dist = Math.hypot(p.getX() - targetX, p.getY() - targetY);
                double threshold = isBaseTarget ? 40.0 : 30.0;

                if (dist < threshold) {
                    int actualDealt = target.takeDamage(p.getDamage());
                    double popupY = isBaseTarget ? 650 : 820;
                    recentDamageEvents.add(new DamageEvent(targetX, popupY, actualDealt, p.isCritical(), "RANGE", isBaseTarget));
                    return true;
                }
            } else {
                return true;
            }
            return false;
        });
    }

    /**
     * Updates turret behavior and handles turret attacks for a single game tick.
     * <p>
     *     Each turret checks whether it is off cooldown and selects the closest valid
     *     target within its attack range. If a target is found, damage is calculated
     *     based on the turret type, a projectile is spawned toward the target, and the
     *     turret’s cooldown is reset.
     * </p>
     * <p>
     *     Target distance is measured relative to the owning side’s base position, and
     *     projectile spawn positions are offset based on turret order and ownership.
     * </p>
     *
     * @param turrets the list of turrets to update
     * @param targets the list of potential unit targets
     * @param now     the current time in nanoseconds, typically obtained from
     *                {@link System#nanoTime()}
     */
    private void updateTurrets(List<Turret> turrets, List<Unit> targets, long now) {
        boolean isPlayer = (turrets == playerTurrets);
        double baseX = isPlayer ? PLAYER_BASE_X : ENEMY_BASE_X;

        for (int i = 0; i < turrets.size(); i++) {
            Turret turret = turrets.get(i);
            if (!turret.canFire(now)) continue;

            Unit closestTarget = null;
            double closestDist = Double.MAX_VALUE;

            for (Unit target : targets) {
                if (!target.isAlive()) continue;
                double dist = Math.abs(target.getCoordinate() - baseX);
                if (dist <= turret.getRange()) {
                    if (dist < closestDist) {
                        closestDist = dist;
                        closestTarget = target;
                    }
                }
            }

            if (closestTarget != null) {
                int damage;
                if (turret instanceof LongRangeTurret lrt) {
                    damage = lrt.calculateDamageWithDistance((int) closestDist);
                } else {
                    damage = turret.calculateDamage(closestTarget);
                }

                double startX;
                double startY = 670;

                if (isPlayer) {
                    startX = 80 + (i * 80);
                } else {
                    startX = 2920 - (i * 80);
                }

                double speed = 12.0;
                projectiles.add(new Projectile(startX + 70, startY, speed, damage, closestTarget, "TURRET", false));

                turret.resetCooldown(now);
            }
        }
    }
    /**
     * Updates unit movement, collision, targeting, and combat for a single game tick.
     * <p>
     *     Each allied unit attempts to move forward in the specified direction unless
     *     blocked by enemy units, allied units, or the target base. Units search for
     *     valid targets within range, prioritizing enemy units over the base when
     *     applicable.
     * </p>
     * <p>
     *     If a unit is able to attack and its cooldown has elapsed, it performs either
     *     a ranged or melee attack based on its type. Ranged units fire projectiles,
     *     while melee units apply direct damage and generate damage events for visual
     *     feedback. Counter and critical hit logic is applied where appropriate.
     * </p>
     *
     * @param allies     the list of units to update
     * @param enemies    the list of opposing units used for collision and targeting
     * @param targetBase the base that hostile units are attacking
     * @param direction  the movement direction of the units
     *                  ({@code 1} for right, {@code -1} for left)
     * @param now        the current time in nanoseconds, typically obtained from
     *                  {@link System#nanoTime()}
     */
    private void updateUnits(List<Unit> allies, List<Unit> enemies, Base targetBase, int direction, long now) {
        double baseTargetX = (targetBase == playerBase) ? PLAYER_SPAWN_X : ENEMY_SPAWN_X;

        for (Unit unit : allies) {
            double nextPos = unit.getCoordinate() + (unit.getSpeed() * direction * 0.1);
            boolean blocked = false;
            Attackable target = null;
            double distToBase = Math.abs(nextPos - baseTargetX);
            if (distToBase <= BASE_HITBOX_DIST) blocked = true;
            if (distToBase <= unit.getRange()) target = targetBase;

            for (Unit enemy : enemies) {
                if (!enemy.isAlive()) continue;
                double dist = Math.abs(unit.getCoordinate() - enemy.getCoordinate());
                if (dist <= unit.getRange()) {
                    if (target == null || target == targetBase) target = enemy;
                }
                if (dist <= COLLISION_RADIUS) blocked = true;
            }
            for (Unit ally : allies) {
                if (ally == unit || !ally.isAlive()) continue;
                double relativeDist = (ally.getCoordinate() - unit.getCoordinate()) * direction;
                if (relativeDist > 0 && relativeDist < COLLISION_RADIUS) blocked = true;
            }

            if (!blocked) unit.setCoordinate(nextPos);

            if (target != null && unit.canAttack(now)) {
                if (unit instanceof RangedUnit) {
                    int dmg = unit.calculateDamage(target);
                    boolean isCrit = (target instanceof AntiArmoredUnit);
                    projectiles.add(new Projectile(unit.getCoordinate(), 840, 10.0 * direction, dmg, target, "ARROW", isCrit));
                } else {
                    int damageDealt = unit.attack(target);
                    double targetX = (target instanceof Unit u) ? u.getCoordinate() + 32.5 : baseTargetX + 32.5;
                    boolean isBase = (target instanceof Base);
                    double popupY = isBase ? 650 : 820;

                    boolean isCrit = isCounter(unit, target);

                    recentDamageEvents.add(new DamageEvent(targetX, popupY, damageDealt, isCrit, "MELEE", isBase));
                }
            }
        }
    }

    /**
     * Determines whether the attacking unit has a type advantage over the target.
     * <p>
     *     This method implements the unit counter system, where certain unit types deal
     *     increased or critical damage against specific opposing types.
     * </p>
     *
     * @param attacker the unit performing the attack
     * @param target   the entity being attacked
     * @return {@code true} if the attacker counters the target; {@code false}
     *         otherwise
     */
    private boolean isCounter(Unit attacker, Attackable target) {
        if (attacker instanceof MeleeUnit && target instanceof RangedUnit) return true;
        if (attacker instanceof RangedUnit && target instanceof AntiArmoredUnit) return true;
        if (attacker instanceof AntiArmoredUnit && target instanceof ArmoredUnit) return true;
        if (attacker instanceof ArmoredUnit && target instanceof MeleeUnit) return true;
        return false;
    }

    private void updateEnemyAI(long now) {
        if (nextEnemySpawnTime == 0) nextEnemySpawnTime = now + 2_000_000_000L;
        if (now >= nextEnemySpawnTime) {
            int roll = random.nextInt(4);
            long delay = 0;
            switch (roll) {
                case 0: spawnEnemyUnit(new MeleeUnit(100, 20, 20, 50, 80, 2)); delay = DELAY_AFTER_MELEE; break;
                case 1: spawnEnemyUnit(new RangedUnit(80, 15, 24, 75, 200, 3)); delay = DELAY_AFTER_RANGED; break;
                case 2: spawnEnemyUnit(new AntiArmoredUnit(90, 25, 20, 90, 80, 4)); delay = DELAY_AFTER_ANTI_ARMOR; break;
                case 3: spawnEnemyUnit(new ArmoredUnit(150, 30, 16, 120, 80, 5)); delay = DELAY_AFTER_ARMORED; break;
            }
            nextEnemySpawnTime = now + delay;
        }
    }

    /**
     * Adds the specified unit to the training queue if the queue is not full and
     * the player has enough currency.
     * @param unit The unit to be added to the queue.
     * @return {@code true} if the unit was successfully queued, {@code false} otherwise.
     */
    public boolean queueUnit(Unit unit) {
        if (trainingQueue.size() < MAX_QUEUE_SIZE && currencySystem.spend(unit.getCost())) {
            trainingQueue.add(unit); return true;
        } return false;
    }

    private void updateTraining(long now) {
        if (currentTrainingUnit == null && !trainingQueue.isEmpty()) {
            currentTrainingUnit = trainingQueue.poll(); trainingStartTime = now;
        }
        if (currentTrainingUnit != null && now - trainingStartTime >= currentTrainingUnit.getTrainingTime()) {
            spawnPlayerUnit(currentTrainingUnit); currentTrainingUnit = null;
        }
    }

    /**
     * Spawns a player unit at the player's spawn coordinate.
     * @param unit The unit to spawn.
     */
    public void spawnPlayerUnit(Unit unit) { unit.setCoordinate(PLAYER_SPAWN_X); playerUnits.add(unit); }

    /**
     * Spawns an enemy unit at the enemy's spawn coordinate.
     * @param unit The unit to spawn.
     */
    public void spawnEnemyUnit(Unit unit) { unit.setCoordinate(ENEMY_SPAWN_X); enemyUnits.add(unit); }

    /**
     * Places a turret on the player's base if the player can afford it and the base has capacity.
     * @param turret The turret to place.
     * @return The slot index where the turret was placed, or -1 if placement failed.
     */
    public int placePlayerTurret(Turret turret) {
        if (currencySystem.canAfford(turret.getCost())) {
            int slot = playerBase.addTurret(turret);
            if (slot != -1) { currencySystem.spend(turret.getCost()); playerTurrets.add(turret); }
            return slot;
        } return -1;
    }

    /**
     * Sells the turret at the specified slot index, removing it and refunding a portion of its cost.
     * @param slotIndex The index of the turret slot to sell.
     */
    public void sellTurret(int slotIndex) {
        List<Turret> turrets = playerBase.getTurrets();
        if (slotIndex >= 0 && slotIndex < turrets.size()) {
            Turret t = turrets.get(slotIndex);
            playerBase.removeTurret(t); playerTurrets.remove(t);
            currencySystem.earn(t.getCost() / 2);
        }
    }

    /**
     * Activates a special ability on the given targets if the ability is ready and affordable.
     * @param ability The ability to use.
     * @param targets The list of potential target units.
     * @return {@code true} if the ability was successfully used, {@code false} otherwise.
     */
    public boolean useAbility(SpecialAbility ability, List<Unit> targets) {
        if (ability.isReady() && currencySystem.spend(ability.getCost())) {
            if (ability instanceof NukeAbility nuke)
            {
                nuke.activate(targets);
            }
            else
            {
                ability.activate(targets.getFirst());
            }
            return true;
        } return false;
    }

    /**
     * Upgrades the attack power of all current and future player units.
     * @param cost The cost of the upgrade.
     */
    public void upgradeUnitAttack(int cost) { if (upgradeSystem.upgradeUnitAttack(cost) && Unit.isUpgradedAttack()) for (Unit u : playerUnits) u.setAttack((int)(u.getAttack() * 1.2)); }

    /**
     * Upgrades the health points of all current and future player units.
     * @param cost The cost of the upgrade.
     */
    public void upgradeUnitHp(int cost) { if (upgradeSystem.upgradeUnitHp(cost) && Unit.isUpgradedHp()) for (Unit u : playerUnits) { u.setMaxHp((int)(u.getMaxHp() * 1.5)); u.setHp((int)(u.getHp() * 1.5)); } }

    /**
     * Upgrades the attack power of all turrets.
     * @param cost The cost of the upgrade.
     */
    public void upgradeTurretAttack(int cost) { upgradeSystem.upgradeTurretAttack(cost); }

    /**
     * Upgrades the range of all turrets.
     * @param cost The cost of the upgrade.
     */
    public void upgradeTurretRange(int cost) { upgradeSystem.upgradeTurretRange(cost); }

    /**
     * Upgrades the turret slot capacity of the player's base.
     * @param cost The cost of the upgrade.
     */
    public void upgradeBaseCapacity(int cost) { upgradeSystem.upgradeBaseCapacity(playerBase, cost); }

    /**
     * Returns the currency system associated with this game manager.
     * @return The CurrencySystem object.
     */
    public CurrencySystem getCurrencySystem() { return currencySystem; }

    /**
     * Returns the player's base.
     * @return The player's Base object.
     */
    public Base getPlayerBase() { return playerBase; }

    /**
     * Returns the enemy's base.
     * @return The enemy's Base object.
     */
    public Base getEnemyBase() { return enemyBase; }

    /**
     * Returns the list of currently active player units.
     * @return A list of player Units.
     */
    public List<Unit> getPlayerUnits() { return playerUnits; }

    /**
     * Returns the list of currently active enemy units.
     * @return A list of enemy Units.
     */
    public List<Unit> getEnemyUnits() { return enemyUnits; }

    /**
     * Returns the unit currently being trained.
     * @return The Unit in training, or {@code null} if none.
     */
    public Unit getCurrentTrainingUnit() { return currentTrainingUnit; }

    /**
     * Returns the current number of units in the training queue.
     * @return The size of the training queue.
     */
    public int getQueueSize() { return trainingQueue.size(); }

    /**
     * Returns a list of recent damage events for UI visualization.
     * @return A list of DamageEvent objects.
     */
    public List<DamageEvent> getRecentDamageEvents() { return recentDamageEvents; }

    /**
     * Returns the Nuke ability instance.
     * @return The NukeAbility object.
     */
    public NukeAbility getNukeAbility() { return nukeAbility; }

    /**
     * Returns the list of active projectiles.
     * @return A list of Projectile objects.
     */
    public List<Projectile> getProjectiles() { return projectiles; }
}
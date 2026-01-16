package This_Package_Is_Cursed_Do_Not_Ever_Try_To_Change_Name_Or_It_Will_Explode;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.Objects;

/**
 * Handles the loading and playback of all audio resources in the game.
 * <p>
 * This class manages background music (BGM) players and sound effect (SFX) clips,
 * providing methods to play specific sounds for game events like shooting,
 * damage taking, and explosions.
 * </p>
 */
public class SoundManager {
    private MediaPlayer bgmPlayer;
    private MediaPlayer menuBgmPlayer;

    private AudioClip unitTakeDamageSfx;
    private AudioClip baseTakeDamageSfx;
    private AudioClip shootSfx;
    private AudioClip explosionSfx;
    private AudioClip nukeSfx;

    /**
     * Initializes the SoundManager, loading all BGM and SFX resources.
     */
    public SoundManager() {
        try {
            String bgmPath = Objects.requireNonNull(getClass().getResource("/audioes/BGM.mp3")).toExternalForm();
            bgmMediaInit(bgmPath);
        } catch (Exception e) {
            System.err.println("Error loading Battle BGM: " + e.getMessage());
        }

        try {
            String menuBgmPath = Objects.requireNonNull(getClass().getResource("/audioes/main_menu_BGM.mp3")).toExternalForm();
            menuBgmMediaInit(menuBgmPath);
        } catch (Exception e) {
            System.err.println("Error loading Menu BGM: " + e.getMessage());
        }

        unitTakeDamageSfx = loadClip("/audioes/unit_take_damage.mp3", 0.4);
        baseTakeDamageSfx = loadClip("/audioes/base_take_damage.mp3", 0.5);
        shootSfx = loadClip("/audioes/shoot.mp3", 0.5);
        explosionSfx = loadClip("/audioes/loud_explosion.mp3", 1.0);
        nukeSfx = loadClip("/audioes/nuke.mp3", 1.0);
    }

    private void bgmMediaInit(String path) {
        Media bgmMedia = new Media(path);
        bgmPlayer = new MediaPlayer(bgmMedia);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.setVolume(0.2);
    }

    private void menuBgmMediaInit(String path) {
        Media menuBgMedia = new Media(path);
        menuBgmPlayer = new MediaPlayer(menuBgMedia);
        menuBgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        menuBgmPlayer.setVolume(0.2);
    }

    private AudioClip loadClip(String path, double volume) {
        try {
            String fullPath = Objects.requireNonNull(getClass().getResource(path)).toExternalForm();
            AudioClip clip = new AudioClip(fullPath);
            clip.setVolume(volume);
            return clip;
        } catch (Exception e) {
            System.err.println("Error loading sound '" + path + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Plays the Battle Background Music (BGM).
     */
    public void playBattleBGM() {
        if (bgmPlayer != null) bgmPlayer.play();
    }

    /**
     * Stops the Battle Background Music (BGM).
     */
    public void stopBattleBGM() {
        if (bgmPlayer != null) bgmPlayer.stop();
    }

    /**
     * Plays the Main Menu Background Music (BGM).
     */
    public void playMenuBGM() {
        if (menuBgmPlayer != null) menuBgmPlayer.play();
    }

    /**
     * Stops the Main Menu Background Music (BGM).
     */
    public void stopMenuBGM() {
        if (menuBgmPlayer != null) menuBgmPlayer.stop();
    }

    /**
     * Plays the sound effect for a unit taking damage.
     */
    public void playUnitTakeDamage() {
        if (unitTakeDamageSfx != null) {
            unitTakeDamageSfx.play();
        }
    }

    /**
     * Plays the sound effect for the base taking damage.
     */
    public void playBaseTakeDamage() {
        if (baseTakeDamageSfx != null) {
            baseTakeDamageSfx.play();
        }
    }

    /**
     * Plays the sound effect for a projectile shooting.
     */
    public void playShootSound() {
        if (shootSfx != null) shootSfx.play();
    }

    /**
     * Plays the sound effect for a large explosion (e.g., base destruction).
     */
    public void playBaseExplosion() {
        if (explosionSfx != null) explosionSfx.play();
    }

    /**
     * Plays the sound effect for the Nuke ability.
     */
    public void playNukeSound() {
        if (nukeSfx != null) nukeSfx.play();
    }
}
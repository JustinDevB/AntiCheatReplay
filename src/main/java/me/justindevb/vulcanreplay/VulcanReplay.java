package me.justindevb.vulcanreplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import me.justindevb.vulcanreplay.listeners.KauriListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.justindevb.vulcanreplay.commands.ReloadCommand;
import me.justindevb.vulcanreplay.listeners.PlayerListener;
import me.justindevb.vulcanreplay.listeners.SpartanListener;
import me.justindevb.vulcanreplay.listeners.VulcanListener;
import me.justindevb.vulcanreplay.util.UpdateChecker;

public class VulcanReplay extends JavaPlugin {

    private HashMap<UUID, PlayerCache> playerCache = new HashMap<>();
    private AntiCheat antiCheatType = AntiCheat.NONE;
    private static VulcanReplay instance = null;
    private ListenerBase activeListener;

    @Override
    public void onEnable() {

        checkRequiredPlugins();

        instance = this;

        registerListener();

        initConfig();

        initBstats();

        checkForUpdate();

        handleReload();

        registerCommands();

    }

    @Override
    public void onDisable() {
        this.playerCache.clear();
    }

    private void checkRequiredPlugins() {
        checkReplayAPI();
        findCompatAntiCheat();
    }

    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void findCompatAntiCheat() {
        if (checkVulcanInstalled()) {
            setAntiCheat(AntiCheat.VULCAN);
            return;
        } else if (checkSpartanInstalled()) {
            setAntiCheat(AntiCheat.SPARTAN);
            return;
        }
        disablePlugin();
    }

    /**
     * Check if ReplayAPI is running on the server. If not disable VulcanReplay
     */
    private void checkReplayAPI() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedReplay");
        if (plugin == null || !plugin.isEnabled()) {
            log("AdvancedReplay is required to run this plugin. Shutting down...", true);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Check if Vulcan is running on the server
     */
    private boolean checkVulcanInstalled() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Vulcan");
        if (plugin == null || !plugin.isEnabled())
            return false;
        log("Vulcan detected, enabling support..", false);
        return true;
    }

    /**
     * Check if Spartan is running on the server
     *
     * @return
     */
    private boolean checkSpartanInstalled() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Spartan");
        if (plugin == null || !plugin.isEnabled())
            return false;
        log("Spartan detected, enabling support..", false);
        return true;
    }

    private boolean checkKauriInstalled() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Kauri");
        if (plugin == null || !plugin.isEnabled()) {
            return false;
        }
        log("Kauri detection, enabling support...", false);
        return true;
    }

    /**
     * Initialize the Config
     */
    private void initConfig() {
        initGeneralConfigSettings();

        initDiscordConfigSettings();

        initVulcanConfigSettings();

        // initSpartanConfigSettings();

        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void reloadReplayConfig() {
        reloadConfig();
        findCompatAntiCheat();
    }

    private void initBstats() {
        final int pluginId = 13402;
        Metrics metrics = new Metrics(this, pluginId);
    }

    public PlayerCache getCachedPlayer(UUID uuid) {
        return this.playerCache.get(uuid);
    }

    /**
     * Add a player to the PlayerCache
     *
     * @param uuid
     * @param player
     */
    public void putCachedPlayer(UUID uuid, PlayerCache player) {
        if (this.playerCache.containsKey(uuid))
            return;
        this.playerCache.put(uuid, player);
    }

    /**
     * Check if player is Cached
     *
     * @return
     */
    public boolean isPlayerCached(UUID uuid) {
        return this.playerCache.containsKey(uuid);
    }

    /**
     * Remove a player from the PlayerCache
     *
     * @param uuid
     */
    public void removeCachedPlayer(UUID uuid) {
        if (isPlayerCached(uuid))
            this.playerCache.remove(uuid);
    }

    /**
     * Log a message to the console
     *
     * @param Message to log
     * @param Whether this is a severe message or not
     */
    public void log(String msg, boolean severe) {
        if (severe)
            getLogger().log(Level.SEVERE, msg);
        else
            getLogger().log(Level.INFO, msg);
    }

    private void checkForUpdate() {
        if (!getConfig().getBoolean("General.Check-Update"))
            return;
        new UpdateChecker(this, 97845).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version))
                log("You are up to date!", false);
            else
                log("There is an update available! Download at: https://www.spigotmc.org/resources/vulcan-replay.97845/",
                        true);
        });
    }

    /**
     * Attempt to handle if the plugin was reloaded with /reload or PlugMan
     */
    private void handleReload() {
        Bukkit.getScheduler().runTask(this, () -> {
            if (!Bukkit.getOnlinePlayers().isEmpty()) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PlayerCache cachedPlayer = new PlayerCache(p, this);
                    putCachedPlayer(p.getUniqueId(), cachedPlayer);
                }
            }
        });
    }

    public enum AntiCheat {
        VULCAN("Vulcan"), SPARTAN("Spartan"), KAURI("Kauri"), NONE("None");
        public final String name;

        private AntiCheat(String name) {
            this.name = name;
        }
    }

    /**
     * Set what anticheat plugin the server is using
     *
     * @param type
     */
    private void setAntiCheat(AntiCheat type) {
        this.antiCheatType = type;

        switch (antiCheatType) {
            case VULCAN:
                activeListener = new VulcanListener(this);
                break;
            case SPARTAN:
                activeListener = new SpartanListener(this);
                break;
            case KAURI:
                activeListener = new KauriListener(this);
                break;
            case NONE:
                disablePlugin();
                break;
            default:
                disablePlugin();
                break;

        }
    }

    /**
     * Get the anticheat type the server is using
     *
     * @return
     */
    public AntiCheat getAntiCheat() {
        return this.antiCheatType;
    }

    /**
     * Disable plugin if no supported AntiCheat is found
     */
    private void disablePlugin() {
        log("No supported AntiCheat detected!", true);
        Bukkit.getPluginManager().disablePlugin(this);
    }

    private void initDiscordConfigSettings() {
        String path = "Discord.";
        FileConfiguration config = getConfig();
        config.addDefault(path + "Enabled", true);
        config.addDefault(path + "Webhook", "Enter webhook here");
        config.addDefault(path + "Avatar", "https://i.imgur.com/JPG1Kwk.png");
        config.addDefault(path + "Username", "VulcanReplay");
        config.addDefault(path + "Server-Name", "Server");
    }

    private void initVulcanConfigSettings() {
        List<String> list = new ArrayList<>();
        list.add("timer");
        list.add("strafe");
        getConfig().addDefault("Vulcan.Disabled-Recordings", list);
    }

    private void initSpartanConfigSettings() {
        List<String> list = new ArrayList<>();
        list.add("timer");
        list.add("strafe");
        getConfig().addDefault("Spartan.Disabled-Recordings", list);
    }

    private void initGeneralConfigSettings() {
        FileConfiguration config = getConfig();
        config.addDefault("General.Check-Update", true);
        config.addDefault("General.Nearby-Range", 30);

        config.addDefault("General.Recording-Length", 2);
        config.addDefault("General.Overwrite", false);
    }

    /**
     * Register Plugin Commands
     */
    private void registerCommands() {
        this.getCommand("replayreload").setExecutor(new ReloadCommand());
    }

    public static VulcanReplay getInstance() {
        return instance;
    }

}

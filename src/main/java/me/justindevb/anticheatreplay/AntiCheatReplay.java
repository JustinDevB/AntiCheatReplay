package me.justindevb.anticheatreplay;

import dev.brighten.api.KauriAPI;
import me.justindevb.anticheatreplay.api.AntiCheatReplayAPI;
import me.justindevb.anticheatreplay.commands.ReloadCommand;
import me.justindevb.anticheatreplay.commands.ReportCommand;
import me.justindevb.anticheatreplay.listeners.PlayerListener;
import me.justindevb.anticheatreplay.utils.Messages;
import me.justindevb.anticheatreplay.utils.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

public class AntiCheatReplay extends JavaPlugin {

    private static AntiCheatReplay instance = null;
    private final Map<AntiCheat, ListenerBase> activeListeners = new EnumMap<>(AntiCheat.class);
    private final HashMap<UUID, PlayerCache> playerCache = new HashMap<>();
    private AntiCheat anticheat = null;

    public static AntiCheatReplay getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            instance = this;

            checkRequiredPlugins();
            registerListener();
            initConfig();
            checkForUpdate();
            handleReload();
            registerCommands();
            initBstats();
            new AntiCheatReplayAPI(this);
        });

    }

    @Override
    public void onDisable() {
        this.playerCache.clear();

        // Properly disinitialize listeners
        for (ListenerBase value : this.activeListeners.values()) {
            value.disinit();
        }

        this.activeListeners.clear();
    }

    private void checkRequiredPlugins() {
        checkReplayAPI();
        findCompatAntiCheat();
    }

    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        // Bukkit.getPluginManager().registerEvents(new OreAnnouncerListener(this),
        // this);
    }

    /**
     * Find a compatible AntiCheat and register support for it
     */
    private void findCompatAntiCheat() {
        for (AntiCheat value : AntiCheat.values()) {
            if (value.getChecker().apply(this)) {
                final ListenerBase base = value.getInstantiator().apply(this);
                activeListeners.put(value, base);
                this.anticheat = value;
            }
        }

        if (activeListeners.isEmpty() && !getConfig().getBoolean("General.Keep-Enabled-With-No-Anticheat")) {
            disablePlugin();
        }
    }

    /**
     * Check if ReplayAPI is running on the server. If not disable AntiCheatReplay
     */
    private void checkReplayAPI() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedReplay");
        if (plugin == null || !plugin.isEnabled()) {
            log("AdvancedReplay is required to run this plugin. Shutting down...", true);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Initialize the Config
     */
    private void initConfig() {

        initGeneralConfigSettings();

        initDiscordConfigSettings();

        initVulcanConfigSettings();

        // initSpartanConfigSettings();

        initThemisConfigSettings();

        initIntaveConfigSettings();

        initGrimConfigSettings();

        getConfig().options().copyDefaults(true);
        saveConfig();

        new Messages();
    }

    public void reloadReplayConfig() {
        if (activeListeners.isEmpty())
            KauriAPI.INSTANCE.unregisterEvents(this);
        else {
            for (ListenerBase value : activeListeners.values()) {
                if (value instanceof Listener) {
                    HandlerList.unregisterAll((Listener) value);
                }

                value.disinit();
            }
        }
        reloadConfig();
        findCompatAntiCheat();
        new Messages();
        log("Reloaded Messages.yml", false);
        log("Reloaded config.yml", false);
    }

    private void initBstats() {
        final int pluginId = 13402;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie("anticheat", () -> {
            return this.anticheat.getName();
        }));

        metrics.addCustomChart(new SimplePie("discord-hook", () -> {
            return String.valueOf(getConfig().getBoolean("Discord.Enabled"));
        }));

    }

    /**
     * Return a player in the Player cache if they exist. If not, add them to the
     * cache
     *
     * @param uuid of Player to fetch
     * @return PlayerCache representing the Player
     */
    public PlayerCache getCachedPlayer(UUID uuid) {
        if (this.playerCache.containsKey(uuid))
            return this.playerCache.get(uuid);

        final PlayerCache cachedPlayer = new PlayerCache(Bukkit.getPlayer(uuid), this);
        this.playerCache.put(uuid, cachedPlayer);
        return cachedPlayer;

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
     * @param msg    to log
     * @param severe Whether this is a severe message or not
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
                getLogger().log(Level.INFO, "There is an update available! Download at: https://www.spigotmc.org/resources/anticheat-replay.97845/");
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
        config.addDefault(path + "Username", "AntiCheatReplay");
        config.addDefault(path + "Server-Name", "Server");
        config.addDefault(path + "Red", 0);
        config.addDefault(path + "Green", 255);
        config.addDefault(path + "Blue", 0);
    }

    private void initVulcanConfigSettings() {
        List<String> list = new ArrayList<>();
        list.add("timer");
        list.add("strafe");
        getConfig().addDefault("Vulcan.Disabled-Recordings", list);
    }

    private void initThemisConfigSettings() {
        List<String> list = new ArrayList<>();
        list.add("notify");
        getConfig().addDefault("Themis.Disabled-Actions", list);
    }

    @SuppressWarnings("unused")
    private void initSpartanConfigSettings() {
        List<String> list = new ArrayList<>();
        list.add("timer");
        list.add("strafe");
        getConfig().addDefault("Spartan.Disabled-Recordings", list);
    }

    private void initIntaveConfigSettings() {
        List<String> list = new ArrayList<>();
        list.add("intave");
        list.add("ban");
        list.add("kick");
        getConfig().addDefault("Intave.Punish-Commands", list);
    }

    private void initGrimConfigSettings() {
        List<String> list = new ArrayList<>();
        list.add("kick");
        list.add("ban");
        getConfig().addDefault("Grim.Punish-Commands", list);
    }

    private void initGeneralConfigSettings() {
        FileConfiguration config = getConfig();
        config.addDefault("General.Check-Update", true);
        config.addDefault("General.Nearby-Range", 30);

        config.addDefault("General.Recording-Length", 2);
        config.addDefault("General.Overwrite", false);
        config.addDefault("General.Notify-Staff", true);
        config.addDefault("General.Save-Recording-On-Disconnect", false);
        config.addDefault("General.Always-Save-Recording", false);
        config.addDefault("General.Report-Cooldown", 3);
        config.addDefault("General.Report-Enabled", true);
        config.addDefault("General.Keep-Enabled-With-No-Anticheat", false);
    }

    /**
     * Register Plugin Commands
     */
    private void registerCommands() {
        this.getCommand("replayreload").setExecutor(new ReloadCommand());
        if (getConfig().getBoolean("General.Report-Enabled"))
            this.getCommand("report").setExecutor(new ReportCommand(this));
    }

    public boolean isChecking(final AntiCheat antiCheat) {
        return activeListeners.containsKey(antiCheat);
    }

    public AntiCheat getAntiCheat() {
        return this.anticheat;
    }

}

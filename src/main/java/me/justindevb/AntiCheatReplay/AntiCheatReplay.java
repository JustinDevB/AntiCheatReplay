package me.justindevb.anticheatreplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import cc.funkemunky.api.Atlas;
import me.justindevb.anticheatreplay.Commands.ReloadCommand;
import me.justindevb.anticheatreplay.Listeners.FlappyACListener;
import me.justindevb.anticheatreplay.Listeners.GodsEyeListener;
import me.justindevb.anticheatreplay.Listeners.KarhuListener;
import me.justindevb.anticheatreplay.Listeners.KauriListener;
import me.justindevb.anticheatreplay.Listeners.MatrixListener;
import me.justindevb.anticheatreplay.Listeners.OreAnnouncerListener;
import me.justindevb.anticheatreplay.Listeners.PlayerListener;
import me.justindevb.anticheatreplay.Listeners.SoaromaListener;
import me.justindevb.anticheatreplay.Listeners.SpartanListener;
import me.justindevb.anticheatreplay.Listeners.ThemisListener;
import me.justindevb.anticheatreplay.Listeners.VulcanListener;
import me.justindevb.anticheatreplay.Util.AntiCheatDetector;
import me.justindevb.anticheatreplay.Util.UpdateChecker;

public class AntiCheatReplay extends JavaPlugin {

	private HashMap<UUID, PlayerCache> playerCache = new HashMap<>();
	private AntiCheat antiCheatType = AntiCheat.NONE;
	private static AntiCheatReplay instance = null;
	private Listener activeListener = null;

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
		});

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
		// Bukkit.getPluginManager().registerEvents(new OreAnnouncerListener(this),
		// this);
	}

	/**
	 * Find a compatible AntiCheat and register support for it
	 */
	// TODO: Attempt cleaning this up
	private void findCompatAntiCheat() {
		AntiCheatDetector detector = AntiCheatDetector.getInstance();
		if (detector.checkVulcanInstalled())
			setAntiCheat(AntiCheat.VULCAN);

		if (detector.checkSpartanInstalled())
			setAntiCheat(AntiCheat.SPARTAN);

		if (detector.checkMatrixInstalled())
			setAntiCheat(AntiCheat.MATRIX);

		if (detector.checkGodsEyeInstalled())
			setAntiCheat(AntiCheat.GODSEYE);

		if (detector.checkKauriInstalled())
			setAntiCheat(AntiCheat.KAURI);

		if (detector.checkKarhuInstalled())
			setAntiCheat(AntiCheat.KARHU);

		if (detector.checkThemisInstalled())
			setAntiCheat(AntiCheat.THEMIS);

		if (detector.checkSoaromaInstalled())
			setAntiCheat(AntiCheat.SOAROMA);

		if (detector.checkFlappyInstalled())
			setAntiCheat(AntiCheat.FLAPPY);

		if (getAntiCheat() == AntiCheat.NONE)
			disablePlugin();
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

		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void reloadReplayConfig() {
		if (activeListener == null)
			Atlas.getInstance().getEventManager().unregisterAll(this);
		else
			HandlerList.unregisterAll(activeListener);
		reloadConfig();
		findCompatAntiCheat();
	}

	private void initBstats() {
		final int pluginId = 13402;
		Metrics metrics = new Metrics(this, pluginId);
		metrics.addCustomChart(new SimplePie("anticheat", () -> {
			return getAntiCheat().name;
		}));
	}

	/**
	 * Return a player in the Player cache if they exist. If not, add them to the
	 * cache
	 * 
	 * @param UUID of Player to fetch
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

	/**
	 * Set what anticheat plugin the server is using
	 * 
	 * @param type
	 */
	private void setAntiCheat(AntiCheat type) {
		// TODO: Figure out a better way to reload
		this.antiCheatType = type;

		switch (antiCheatType) {
		case VULCAN:
			activeListener = new VulcanListener(this);
			break;
		case SPARTAN:
			activeListener = new SpartanListener(this);
			break;
		case MATRIX:
			activeListener = new MatrixListener(this);
			break;
		case GODSEYE:
			activeListener = new GodsEyeListener(this);
			break;
		case KAURI:
			new KauriListener(this);
			activeListener = null;
			break;
		case KARHU:
			new KarhuListener(this);
			// Karhu is posessed and has it's own Event System. Similar to Kauri, but worse.
			// Not possible to reload the plugin if using Karhu
			break;
		case THEMIS:
			activeListener = new ThemisListener(this);
			break;
		case SOAROMA:
			activeListener = new SoaromaListener(this);
			break;
		case FLAPPY:
			activeListener = new FlappyACListener(this);
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
		config.addDefault(path + "Username", "AntiCheatReplay");
		config.addDefault(path + "Server-Name", "Server");
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

	public static AntiCheatReplay getInstance() {
		return instance;
	}

}

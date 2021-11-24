package me.justindevb.VulcanReplay;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.justindevb.VulcanReplpay.util.UpdateChecker;

public class VulcanReplay extends JavaPlugin {

	private HashMap<UUID, PlayerCache> playerCache = new HashMap<>();

	@Override
	public void onEnable() {
		registerListener();

		checkRequiredPlugins();

		initConfig();

		initBstats();

		checkForUpdate();

	}

	@Override
	public void onDisable() {
		this.playerCache.clear();
	}

	private void registerListener() {
		Bukkit.getPluginManager().registerEvents(new VulcanListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	private void checkRequiredPlugins() {
		checkReplayAPI();
		checkVulcanInstalled();
		checkVulcanApi();
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
	 * Check if Vulcan is running on the server. If not disable VulcanReplay
	 */
	private void checkVulcanInstalled() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Vulcan");
		if (plugin == null || !plugin.isEnabled()) {
			log("Vulcan is required to run this plugin. Shutting down...", true);
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	/**
	 * Check to see if the Vulcan API is enabled in Vulcan's config.yml
	 */
	private void checkVulcanApi() {
		log("Checking if Vulcan API is enabled", false);
		File file = new File(this.getDataFolder().getParentFile(),
				"Vulcan" + System.getProperty("file.separator") + "config.yml");
		if (!file.exists()) {
			log("Vulcan is not installed!", true);
			return;
		}

		FileConfiguration vulcan = YamlConfiguration.loadConfiguration(file);

		if (vulcan.getBoolean("settings.enable-api")) {
			log("Vulcan API is enabled", false);
			return;
		}

		log("Vulcan API is disabled in Vulcan's config.yml. This must be true for this plugin to work!", true);
		Bukkit.getScheduler().runTask(this, () -> {
			Bukkit.getPluginManager().disablePlugin(this);
		});
	}

	/**
	 * Initialize the Config
	 */
	private void initConfig() {
		FileConfiguration config = getConfig();
		config.addDefault("General.Check-Update", true);
		config.addDefault("General.Nearby-Range", 30);

		List<String> list = new ArrayList<>();
		list.add("timer");
		list.add("strafe");
		config.addDefault("Genral.Disabled-Recordings", list);
		config.addDefault("General.Recording-Length", 2);

		String path = "Discord.";
		config.addDefault(path + "Enabled", true);
		config.addDefault(path + "Webhook", "Enter webhook here");
		config.addDefault(path + "Avatar", "Enter link to a discord avatar");
		config.addDefault(path + "Username", "VulcanReplay");
		config.addDefault(path + "Server-Name", "Server");
		config.options().copyDefaults(true);
		saveConfig();
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

}

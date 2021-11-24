package me.justindevb.VulcanReplay;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class VulcanReplay extends JavaPlugin {

	@Override
	public void onEnable() {
		registerListener();

		checkRequiredPlugins();

		initConfig();

		initBstats();

	}

	private void registerListener() {
		Bukkit.getPluginManager().registerEvents(new VulcanListener(this), this);
	}

	private void checkRequiredPlugins() {
		checkReplayAPI();
		checkVulcan();
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
	private void checkVulcan() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Vulcan");
		if (plugin == null || !plugin.isEnabled()) {
			log("Vulcan is required to run this plugin. Shutting down...", true);
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	/**
	 * Initialize the Config
	 */
	private void initConfig() {
		FileConfiguration config = getConfig();
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

	private void initBstats() {
		final int pluginId = 13402;
		Metrics metrics = new Metrics(this, pluginId);
	}

}

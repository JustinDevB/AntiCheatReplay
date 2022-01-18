package me.justindevb.anticheatreplay.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.justindevb.anticheatreplay.AntiCheatReplay;

public class Messages {
	FileConfiguration config;

	public static String TITLE;
	public static String DESCRIPTION;
	public static String SERVER;
	public static String ONLINE_FOR;
	public static String ONLINE_FOR_MINUTES;
	public static String RECORDING_NAME;
	public static String COMMAND;

	public Messages() {
		Bukkit.getScheduler().runTaskAsynchronously(AntiCheatReplay.getInstance(), () -> {
			File file = new File(AntiCheatReplay.getInstance().getDataFolder(), "Messages.yml");
			config = YamlConfiguration.loadConfiguration(file);

			config.addDefault("Discord.Title", "Instant Replay");
			config.addDefault("Discord.Description", "Recording created");
			config.addDefault("Discord.Server", "Server:");
			config.addDefault("Discord.OnlineFor", "Online for:");
			config.addDefault("Discord.Minutes", "minutes");
			config.addDefault("Discord.RecordingName", "Recording saved as:");
			config.addDefault("Discord.Command", "View with:");

			try {
				config.options().copyDefaults(true);
				config.save(file);
			} catch (IOException e) {
				AntiCheatReplay.getInstance().log("Error loading Messages.yml", true);
				e.printStackTrace();
			}

			initFields();
		});

	}

	private void initFields() {
		TITLE = config.getString("Discord.Title");
		DESCRIPTION = config.getString("Discord.Description");
		SERVER = config.getString("Discord.Server");
		ONLINE_FOR = config.getString("Discord.OnlineFor");
		ONLINE_FOR_MINUTES = config.getString("Discord.Minutes");
		RECORDING_NAME = config.getString("Discord.RecordingName");
		COMMAND = config.getString("Discord.Command");
	}

}

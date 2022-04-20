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
	public static String REPORT_USAGE;
	public static String REPORT_OFFLINE_ERROR;
	public static String COMMAND_NO_PERMISSION;
	public static String COMMAND_REPORT_NOTIFY;
	public static String COMMAND_REPORT_SELF_REPORT;
	public static String COMMAND_REPORT_COOLDOWN;
	public static String REPORT_SUBMITTED;
	public static String NOTIFY_RECORDING;

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

			config.addDefault("Commands.Report.Usage", "Usage: /report <player> (reason)");
			config.addDefault("Commands.Report.Offline", "You can only report an online player!");
			config.addDefault("Commands.No-Permission", "You do not have permission to do this!");
			config.addDefault("Commands.Report.Report-Self", "You can't report yourself!");
			config.addDefault("Commands.Report.Notify", "%r has reported %s for %t");
			config.addDefault("Commands.Report.Success", "Report successfully submitted!");
			config.addDefault("Commands.Report.Cooldown", "You have run that too recently!");

			config.addDefault("General.Notify-Recording", "A recording has been saved: %r");


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
		REPORT_USAGE = config.getString("Commands.Report.Usage");
		REPORT_OFFLINE_ERROR = config.getString("Commands.Report.Offline");
		COMMAND_NO_PERMISSION = config.getString("Commands.No-Permission");
		COMMAND_REPORT_NOTIFY = config.getString("Commands.Report.Notify");
		COMMAND_REPORT_SELF_REPORT = config.getString("Commands.Report.Report-Self");
		REPORT_SUBMITTED = config.getString("Commands.Report.Success");
		NOTIFY_RECORDING = config.getString("General.Notify-Recording");
		COMMAND_REPORT_COOLDOWN = config.getString("Commands.Report.Cooldown");
	}

}

package me.justindev.VulcanReplay.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.justindevb.VulcanReplay.AntiCheat;
import me.justindevb.VulcanReplay.VulcanReplay;
import net.md_5.bungee.api.ChatColor;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("vulcanreplay.reload")) {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission for this!");
			return true;
		}

		if (VulcanReplay.getInstance().getAntiCheat() == AntiCheat.KARHU) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "It is currently not possible to reload this plugin while using Karhu. Please reboot your server instead...");
			return true;
		}

		VulcanReplay.getInstance().reloadReplayConfig();
		sender.sendMessage(ChatColor.DARK_GREEN + "[VulcanReplay] Reloaded config");
		VulcanReplay.getInstance().log("Reloaded config", false);
		return true;
	}

}

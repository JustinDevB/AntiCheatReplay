package me.justindevb.anticheatreplay.commands;

import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import me.justindevb.anticheatreplay.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReportCommand extends ListenerBase implements CommandExecutor {
    public ReportCommand(AntiCheatReplay acReplay) {
        super(acReplay);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.DARK_RED + "Only a player can execute this command!");
            return true;
        }

        Player p = (Player) commandSender;

        if (!p.hasPermission("AntiCheatReplay.report")) {
            p.sendMessage(ChatColor.DARK_RED + Messages.COMMAND_NO_PERMISSION);
            return true;
        }

        if (strings.length == 0) {
            p.sendMessage(ChatColor.GOLD + Messages.REPORT_USAGE);
            return true;
        }

        if (Bukkit.getPlayer(strings[0]) == null || !Bukkit.getPlayer(strings[0]).isOnline()) {
            p.sendMessage(ChatColor.DARK_RED + Messages.REPORT_OFFLINE_ERROR);
            return true;
        }

        if (Bukkit.getPlayer(strings[0]).getName().contentEquals(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + Messages.COMMAND_REPORT_SELF_REPORT);
            return true;
        }

        String reason = "";
        if (strings.length == 1)
            reason = "No Reason Supplied";
        else
            for (int i = 1; i < strings.length; i++)
                reason += strings[i] + " ";

        String notification = Messages.COMMAND_REPORT_NOTIFY;
        notification = notification
                .replace("%r", p.getName())
                .replace("%s", Bukkit.getPlayer(strings[0]).getName())
                .replace("%t", reason);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("AntiCheatReplay.report-notify"))
                player.sendMessage(ChatColor.GOLD + notification);
        }

        reportPlayer(p, Bukkit.getPlayer(strings[0]), reason);


        return true;
    }
}

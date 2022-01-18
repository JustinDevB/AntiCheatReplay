package me.justindevb.anticheatreplay.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.rammelkast.anticheatreloaded.api.event.CheckFailEvent;
import com.rammelkast.anticheatreloaded.api.event.PlayerPunishEvent;

import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;

public class AntiCheatReloadedListener extends ListenerBase implements Listener {

	public AntiCheatReloadedListener(AntiCheatReplay acReplay) {
		super(acReplay);
		Bukkit.getPluginManager().registerEvents(this, acReplay);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onViolationEvent(CheckFailEvent event) {
		Player p = event.getUser().getPlayer();

		if (alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());

		startRecording(p, getReplayName(p, event.getCheck().getName()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAction(PlayerPunishEvent event) {
		final Player p = event.getUser().getPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());
	}

}

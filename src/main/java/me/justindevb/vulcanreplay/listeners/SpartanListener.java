package me.justindevb.vulcanreplay.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import me.justindevb.vulcanreplay.ListenerBase;
import me.justindevb.vulcanreplay.VulcanReplay;
import me.vagdedes.spartan.api.PlayerViolationCommandEvent;
import me.vagdedes.spartan.api.PlayerViolationEvent;

public class SpartanListener extends ListenerBase implements Listener {

	public SpartanListener(VulcanReplay vulcanReplay) {
		super(vulcanReplay);
	}

	@Override
	public void unregister() {
		HandlerList.unregisterAll(this);
	}

	@Override
	public void register() {
		Bukkit.getPluginManager().registerEvents(this, VulcanReplay.getInstance());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onFlagEvent(PlayerViolationEvent event) {
		Player p = event.getPlayer();

		if (alertList.contains(p.getName()))
			return;

		alertList.add(p.getName());

		final String replayName = p.getName() + "-" + event.getHackType().toString() + "-" + getTimeStamp();

		startRecording(p, replayName);
	}

	@EventHandler
	public void onPunish(PlayerViolationCommandEvent event) {
		final Player p = event.getPlayer();

		if (!punishList.contains(p.getName()))
			punishList.add(p.getName());
	}

}

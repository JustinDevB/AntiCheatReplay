package me.justindevb.VulcanReplay.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import godseye.GodsEyePlayerViolationEvent;
import godseye.GodsEyePunishPlayerEvent;
import me.justindevb.VulcanReplay.ListenerBase;
import me.justindevb.VulcanReplay.VulcanReplay;

public class GodsEyeListener extends ListenerBase implements Listener {

	public GodsEyeListener(VulcanReplay vulcanReplay) {
		super(vulcanReplay);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST) 
	public void onFlagEvent(GodsEyePlayerViolationEvent event) {
		Player p = event.getPlayer();

		if (alertList.contains(p.getName()))
			return;

		alertList.add(p.getName());

		startRecording(p, getReplayName(p, event.getDetection()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPunish(GodsEyePunishPlayerEvent event) {
		final Player p = event.getPlayer();

		if (!punishList.contains(p.getName()))
			punishList.add(p.getName());
	}

}

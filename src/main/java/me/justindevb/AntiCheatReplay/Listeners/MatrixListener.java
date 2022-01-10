package me.justindevb.AntiCheatReplay.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.justindevb.AntiCheatReplay.ListenerBase;
import me.justindevb.AntiCheatReplay.AntiCheatReplay;
import me.rerere.matrix.api.events.PlayerViolationCommandEvent;
import me.rerere.matrix.api.events.PlayerViolationEvent;

public class MatrixListener extends ListenerBase implements Listener {

	public MatrixListener(AntiCheatReplay AntiCheatReplay) {
		super(AntiCheatReplay);
		Bukkit.getPluginManager().registerEvents(this, AntiCheatReplay.getInstance());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFlagEvent(PlayerViolationEvent event) {
		Player p = event.getPlayer();

		if (alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());

		final String replayName = p.getName() + "-" + event.getHackType().toString() + "-" + getTimeStamp();

		startRecording(p, replayName);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPunish(PlayerViolationCommandEvent event) {
		final Player p = event.getPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());
	}
	
	

}

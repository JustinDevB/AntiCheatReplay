package me.justindevb.AntiCheatReplay.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.justdoom.flappyanticheat.customevents.FlagEvent;
import com.justdoom.flappyanticheat.customevents.PunishEvent;

import me.justindevb.AntiCheatReplay.ListenerBase;
import me.justindevb.AntiCheatReplay.AntiCheatReplay;

public class FlappyACListener extends ListenerBase implements Listener {

	public FlappyACListener(AntiCheatReplay AntiCheatReplay) {
		super(AntiCheatReplay);
		Bukkit.getPluginManager().registerEvents(this, AntiCheatReplay.getInstance());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onFlagEvent(FlagEvent event) {
		Player p = event.getFlaggedPlayer();

		if (alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());

		startRecording(p, getReplayName(p, event.getCheck().toString()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPunish(PunishEvent event) {
		final Player p = event.getPunishedPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());
	}
	
	

}

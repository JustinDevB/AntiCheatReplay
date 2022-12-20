package me.justindevb.anticheatreplay.listeners.AntiCheats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.elikill58.negativity.spigot.listeners.PlayerCheatAlertEvent;
import com.elikill58.negativity.spigot.listeners.PlayerCheatKickEvent;

import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;

public class NegativityV1Listener extends ListenerBase implements Listener {

	public NegativityV1Listener(AntiCheatReplay acReplay) {
		super(acReplay);
		Bukkit.getPluginManager().registerEvents(this, acReplay);
	}
	
	@EventHandler
	public void onAlert(PlayerCheatAlertEvent e) {
		Player p = e.getPlayer();
		org.bukkit.entity.Player bukkitPlayer = Bukkit.getPlayer(p.getUniqueId());
		if(bukkitPlayer == null || alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());
		startRecording(bukkitPlayer, getReplayName(bukkitPlayer, e.getCheat().getKey()));
	}
	
	@EventHandler
	public void onKick(PlayerCheatKickEvent e) {
		Player p = e.getPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());
	}
}

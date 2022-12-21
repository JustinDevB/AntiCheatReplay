package me.justindevb.anticheatreplay.listeners.AntiCheats;

import org.bukkit.Bukkit;

import com.elikill58.negativity.api.entity.Player;
import com.elikill58.negativity.api.events.EventListener;
import com.elikill58.negativity.api.events.EventManager;
import com.elikill58.negativity.api.events.Listeners;
import com.elikill58.negativity.api.events.negativity.PlayerCheatAlertEvent;
import com.elikill58.negativity.api.events.negativity.PlayerCheatKickEvent;

import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;

public class NegativityV2Listener extends ListenerBase implements Listeners {

	public NegativityV2Listener(AntiCheatReplay acReplay) {
		super(acReplay);
		EventManager.registerEvent(this);
	}
	
	@EventListener
	public void onAlert(PlayerCheatAlertEvent e) {
		Player p = e.getPlayer();
		org.bukkit.entity.Player bukkitPlayer = Bukkit.getPlayer(p.getUniqueId());
		if(bukkitPlayer == null || alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());
		startRecording(bukkitPlayer, getReplayName(bukkitPlayer, e.getCheat().getKey().getLowerKey()));
	}
	
	@EventListener
	public void onKick(PlayerCheatKickEvent e) {
		Player p = e.getPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());
	}
}

package me.justindevb.VulcanReplay.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import dev.brighten.api.listener.KauriFlagEvent;
import dev.brighten.api.listener.KauriPunishEvent;
import me.justindevb.VulcanReplay.ListenerBase;
import me.justindevb.VulcanReplay.VulcanReplay;

public class KauriListener extends ListenerBase implements Listener {

	public KauriListener(VulcanReplay vulcanReplay) {
		super(vulcanReplay);
		Bukkit.getPluginManager().registerEvents(this, VulcanReplay.getInstance());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFlagEvent(KauriFlagEvent event) {

		final Player p = event.getPlayer();

		if (alertList.contains(p.getName()))
			return;

		alertList.add(p.getName());

		final String replayName = p.getName() + "-" + event.getCheck().getName() + "-" + super.getTimeStamp();

		startRecording(p, replayName);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPunish(KauriPunishEvent event) {
		final Player p = event.getPlayer();

		if (!punishList.contains(p.getName()))
			punishList.add(p.getName());

	}

}

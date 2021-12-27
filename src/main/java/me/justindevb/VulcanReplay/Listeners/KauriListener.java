package me.justindevb.VulcanReplay.Listeners;

import org.bukkit.entity.Player;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.events.ListenerPriority;
import dev.brighten.api.listener.KauriFlagEvent;
import dev.brighten.api.listener.KauriPunishEvent;
import me.justindevb.VulcanReplay.ListenerBase;
import me.justindevb.VulcanReplay.VulcanReplay;

public class KauriListener extends ListenerBase implements AtlasListener {

	public KauriListener(VulcanReplay vulcanReplay) {
		super(vulcanReplay);
		Atlas.getInstance().getEventManager().registerListeners(this, VulcanReplay.getInstance());
	}

	@Listen(ignoreCancelled = true, priority = ListenerPriority.HIGHEST)
	public void onFlagEvent(KauriFlagEvent event) {

		final Player p = event.getPlayer();

		if (alertList.contains(p.getName()))
			return;

		alertList.add(p.getName());

		final String replayName = p.getName() + "-" + event.getCheck().getName() + "-" + super.getTimeStamp();

		startRecording(p, replayName);

	}

	@Listen(ignoreCancelled = true, priority = ListenerPriority.HIGHEST)
	public void onPunish(KauriPunishEvent event) {
		final Player p = event.getPlayer();

		if (!punishList.contains(p.getName()))
			punishList.add(p.getName());

	}

}

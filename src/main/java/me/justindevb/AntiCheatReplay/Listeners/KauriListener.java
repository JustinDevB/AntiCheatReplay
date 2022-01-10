package me.justindevb.anticheatreplay.Listeners;

import org.bukkit.entity.Player;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.events.ListenerPriority;
import dev.brighten.api.listener.KauriFlagEvent;
import dev.brighten.api.listener.KauriPunishEvent;
import me.justindevb.anticheatreplay.ListenerBase;
import me.justindevb.anticheatreplay.AntiCheatReplay;

public class KauriListener extends ListenerBase implements AtlasListener {

	public KauriListener(AntiCheatReplay acReplay) {
		super(acReplay);
		Atlas.getInstance().getEventManager().registerListeners(this, AntiCheatReplay.getInstance());
	}

	@Listen(ignoreCancelled = true, priority = ListenerPriority.HIGHEST)
	public void onFlagEvent(KauriFlagEvent event) {

		final Player p = event.getPlayer();

		if (alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());

		final String replayName = p.getName() + "-" + event.getCheck().getName() + "-" + super.getTimeStamp();

		startRecording(p, replayName);

	}

	@Listen(ignoreCancelled = true, priority = ListenerPriority.HIGHEST)
	public void onPunish(KauriPunishEvent event) {
		final Player p = event.getPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());

	}

}

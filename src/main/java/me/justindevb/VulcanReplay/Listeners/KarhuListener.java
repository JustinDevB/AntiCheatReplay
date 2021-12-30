package me.justindevb.VulcanReplay.Listeners;

import org.bukkit.entity.Player;

import me.justindevb.VulcanReplay.ListenerBase;
import me.justindevb.VulcanReplay.VulcanReplay;
import me.liwk.karhu.api.KarhuAPI;
import me.liwk.karhu.api.event.KarhuEvent;
import me.liwk.karhu.api.event.impl.KarhuAlertEvent;
import me.liwk.karhu.api.event.impl.KarhuBanEvent;

public class KarhuListener extends ListenerBase implements me.liwk.karhu.api.event.KarhuListener {

	public KarhuListener(VulcanReplay vulcanReplay) {
		super(vulcanReplay);
		KarhuAPI.getEventRegistry().addListener(this);
	}

	public void onEvent(KarhuEvent event) {
		if (event instanceof KarhuAlertEvent) {
			Player p = ((KarhuAlertEvent) event).getPlayer();

			if (alertList.contains(p.getName()))
				return;

			alertList.add(p.getName());
			startRecording(p,
					this.getReplayName(p, ((KarhuAlertEvent) event).getCheck().getName()).replaceAll(" ", "-"));

		} else if (event instanceof KarhuBanEvent) {
			final Player p = ((KarhuBanEvent) event).getPlayer();

			if (!punishList.contains(p.getName()))
				punishList.add(p.getName());
		}
	}

}

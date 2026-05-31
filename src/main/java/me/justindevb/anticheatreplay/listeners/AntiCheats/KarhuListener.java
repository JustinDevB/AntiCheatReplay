package me.justindevb.anticheatreplay.listeners.AntiCheats;

import org.bukkit.entity.Player;

import me.justindevb.anticheatreplay.ListenerBase;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.liwk.karhu.api.KarhuAPI;
import me.liwk.karhu.api.event.KarhuEvent;
import me.liwk.karhu.api.event.impl.KarhuAlertEvent;
import me.liwk.karhu.api.event.impl.KarhuBanEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KarhuListener extends ListenerBase implements me.liwk.karhu.api.event.KarhuListener {

	public KarhuListener(AntiCheatReplay acReplay) {
		super(acReplay);
		KarhuAPI.getEventRegistry().addListener(this);
	}

	@Override
	public void disinit() {
		Object registry = KarhuAPI.getEventRegistry();
		if (registry == null)
			return;

		invokeIfPresent(registry, "removeListener");
		invokeIfPresent(registry, "unregisterListener");
		invokeIfPresent(registry, "unregister");
	}

	public void onEvent(KarhuEvent event) {
		if (event instanceof KarhuAlertEvent) {
			Player p = ((KarhuAlertEvent) event).getPlayer();

			if (alertList.contains(p.getUniqueId()))
				return;

			alertList.add(p.getUniqueId());
			startRecording(p,
					this.getReplayName(p, ((KarhuAlertEvent) event).getCheck().getName()).replaceAll(" ", "-"));

		} else if (event instanceof KarhuBanEvent) {
			final Player p = ((KarhuBanEvent) event).getPlayer();

			if (!punishList.contains(p.getUniqueId()))
				punishList.add(p.getUniqueId());
		}
	}

	private void invokeIfPresent(Object target, String methodName) {
		try {
			Method method = target.getClass().getMethod(methodName, me.liwk.karhu.api.event.KarhuListener.class);
			method.invoke(target, this);
		} catch (NoSuchMethodException ignored) {
		} catch (IllegalAccessException | InvocationTargetException ex) {
			acReplay.log("Unable to unregister Karhu listener via " + methodName + ": " + ex.getMessage(), true);
		}
	}

}

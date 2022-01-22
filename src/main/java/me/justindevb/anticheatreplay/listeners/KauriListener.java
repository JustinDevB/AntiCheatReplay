package me.justindevb.anticheatreplay.listeners;

import dev.brighten.api.KauriAPI;
import dev.brighten.api.check.CancelType;
import dev.brighten.api.check.KauriCheck;
import dev.brighten.api.event.KauriEvent;
import dev.brighten.api.event.result.CancelResult;
import dev.brighten.api.event.result.FlagResult;
import dev.brighten.api.event.result.PunishResult;
import org.bukkit.entity.Player;

import me.justindevb.anticheatreplay.ListenerBase;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import org.bukkit.event.EventPriority;

import java.util.List;

public class KauriListener extends ListenerBase implements KauriEvent {

	public KauriListener(AntiCheatReplay acReplay) {
		super(acReplay);
		KauriAPI.INSTANCE.registerEvent(acReplay, this);
	}

	@Override
	public FlagResult onFlag(Player p, KauriCheck check, String information, boolean cancelled) {

		if (alertList.contains(p.getUniqueId()))
			return FlagResult.builder().cancelled(false).build();

		alertList.add(p.getUniqueId());

		final String replayName = p.getName() + "-" + check.getName() + "-" + super.getTimeStamp();

		startRecording(p, replayName);

		return FlagResult.builder().cancelled(false).build();

	}

	@Override
	public PunishResult onPunish(Player p, KauriCheck check, String broadcastMessage, List<String> list, boolean cancelled) {

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());

		return PunishResult.builder().cancelled(false).build();

	}

	@Override
	public CancelResult onCancel(Player player, CancelType cancelType, boolean cancelled) {
		return CancelResult.builder().cancelled(false).build();
	}

	@Override
	public EventPriority priority() {
		return EventPriority.NORMAL;
	}
}

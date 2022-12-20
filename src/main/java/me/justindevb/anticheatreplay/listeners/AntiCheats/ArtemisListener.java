package me.justindevb.anticheatreplay.listeners.AntiCheats;

import ac.artemis.anticheat.api.ArtemisServerClient;
import ac.artemis.anticheat.api.alert.Alert;
import ac.artemis.anticheat.api.alert.Punishment;
import ac.artemis.anticheat.api.alert.Severity;
import ac.artemis.anticheat.api.listener.PunishListener;
import ac.artemis.anticheat.api.listener.VerboseListener;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArtemisListener extends ListenerBase implements PunishListener, VerboseListener {

	public ArtemisListener(AntiCheatReplay acReplay) {
		super(acReplay);
		ArtemisServerClient.getAPI().addBanListener(this);
		ArtemisServerClient.getAPI().addVerboseListener(this);
	}

	@Override
	public void disinit() {
		ArtemisServerClient.getAPI().removeBanListener(this);
		ArtemisServerClient.getAPI().removeVerboseListener(this);
		super.disinit();
	}

	@Override
	public void receive(Punishment punishment) {
		if (!punishList.contains(punishment.getUuid()))
			punishList.add(punishment.getUuid());
	}

	@Override
	public void receive(Alert alert) {
		if (!alert.getSeverity().equals(Severity.VIOLATION))
			return;

		if (alertList.contains(alert.getUuid()))
			return;

		final Player player = Bukkit.getPlayer(alert.getUuid());
		if (player == null)
			return;

		alertList.add(alert.getUuid());
		startRecording(player, getReplayName(
				player,
				alert.getCheck().getType().getCorrectName() + alert.getCheck().getVar())
		);
	}
}

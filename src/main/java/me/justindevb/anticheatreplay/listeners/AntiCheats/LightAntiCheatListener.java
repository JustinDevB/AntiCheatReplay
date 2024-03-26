package me.justindevb.anticheatreplay.listeners.AntiCheats;

import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import me.vekster.lightanticheat.api.event.LACPunishmentEvent;
import me.vekster.lightanticheat.api.event.LACViolationEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class LightAntiCheatListener extends ListenerBase implements Listener {
    public LightAntiCheatListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, acReplay);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFlag(LACViolationEvent event) {
        Player p = event.getPlayer();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheckName()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPunishment(LACPunishmentEvent event) {
        final Player p = event.getPlayer();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }
}

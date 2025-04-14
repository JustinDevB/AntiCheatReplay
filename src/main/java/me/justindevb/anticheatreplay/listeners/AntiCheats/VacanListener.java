package me.justindevb.anticheatreplay.listeners.AntiCheats;

import ai.idealistic.vacan.api.PlayerViolationCommandEvent;
import ai.idealistic.vacan.api.PlayerViolationEvent;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class VacanListener extends ListenerBase implements Listener {

    public VacanListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, acReplay);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFlagEvent(PlayerViolationEvent event) {
        Player p = event.getPlayer();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getHackType().toString()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPunish(PlayerViolationCommandEvent event) {
        final Player p = event.getPlayer();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }
}

package me.justindevb.anticheatreplay.listeners;

import ac.grim.grimac.utils.events.FlagEvent;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GrimACListener extends ListenerBase implements Listener {
    public GrimACListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, AntiCheatReplay.getInstance());
    }


    @EventHandler
    public void onFlag(FlagEvent event) {
        Player p = event.getPlayer().bukkitPlayer;

        if (event.isAlert()) {
            if (alertList.contains(p.getUniqueId()))
                return;

            alertList.add(p.getUniqueId());

            startRecording(p, getReplayName(p, event.getCheckName()));

        } else if (event.isSetback()) {   //Temporary punish event until Grim defines punishments
            if (!punishList.contains(p.getUniqueId()))
                punishList.add(p.getUniqueId());
        }

    }
}

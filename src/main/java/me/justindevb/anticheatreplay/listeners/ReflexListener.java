package me.justindevb.anticheatreplay.listeners;

import com.rammelkast.anticheatreloaded.check.CheckResult;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import rip.reflex.api.event.ReflexCheckEvent;
import rip.reflex.api.event.ReflexCommandEvent;

public class ReflexListener extends ListenerBase implements Listener {
    public ReflexListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, acReplay);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCheck(ReflexCheckEvent event) {
        if (event.getResult().isCheckPassed())
            return;

        Player p = event.getPlayer();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        final String replayName = super.getReplayName(p, event.getCheat().toString());

        startRecording(p, replayName);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(ReflexCommandEvent event) {
        final Player p = event.getPlayer();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }

}

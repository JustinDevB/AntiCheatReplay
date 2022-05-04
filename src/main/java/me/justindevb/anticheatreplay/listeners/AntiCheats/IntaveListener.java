package me.justindevb.anticheatreplay.listeners.AntiCheats;

import de.jpx3.intave.access.check.event.IntaveCommandExecutionEvent;
import de.jpx3.intave.access.check.event.IntaveViolationEvent;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class IntaveListener extends ListenerBase implements Listener {

    private final AntiCheatReplay acReplay;
    private List<String> punishCommands = new ArrayList<>();

    public IntaveListener(AntiCheatReplay acReplay) {
        super(acReplay);
        this.acReplay = acReplay;
        Bukkit.getPluginManager().registerEvents(this, acReplay);

        setupIntave();
    }

    private void setupIntave() {
        initIntaveSpecificConfig();
    }

    @EventHandler
    public void onViolation(IntaveViolationEvent event) {
        final Player p = event.player();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.checkName()));
    }

    @EventHandler
    public void onIntaveCommand(IntaveCommandExecutionEvent event) {
        if (!parseCommand(event.command()))
            return;

        final Player p = event.player();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }

    /**
     * Check if IntaveCommand is a punish command defined in the AntiCheatReplay config
     * @param command
     * @return
     */
    private boolean parseCommand(String command) {
        command = command.toLowerCase();
        String[] parts = command.split(" ");
        for (String commands : punishCommands) {
            if (parts[0].contentEquals(commands.toLowerCase()))
                return true;
        }
        return false;
    }


    private void initIntaveSpecificConfig() {
        this.punishCommands = acReplay.getConfig().getStringList("Intave.Punish-Commands");
    }
}

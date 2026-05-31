package me.justindevb.anticheatreplay.listeners.AntiCheats;


import ac.grim.grimac.api.GrimAbstractAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.event.events.CommandExecuteEvent;
import ac.grim.grimac.api.event.events.FlagEvent;
import ac.grim.grimac.api.plugin.BasicGrimPlugin;
import ac.grim.grimac.api.plugin.GrimPlugin;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GrimACListener extends ListenerBase {
	
    private List<String> punishCommands = new ArrayList<>();
    private GrimPlugin grimPlugin;
    private Object grimEventBus;

    public GrimACListener(AntiCheatReplay acReplay) {
        super(acReplay);


        setupGrim();
    }

    private void setupGrim() {

        RegisteredServiceProvider<GrimAbstractAPI> provider = Bukkit.getServicesManager().getRegistration(GrimAbstractAPI.class);

        if (provider == null)
            return;


        initGrimSpecificConfig();

        this.grimPlugin = new BasicGrimPlugin(
                this.acReplay.getLogger(),
                this.acReplay.getDataFolder(),
                acReplay.getDescription().getVersion(),
                acReplay.getDescription().getDescription(),
                acReplay.getDescription().getAuthors()
        );

        GrimAbstractAPI api = provider.getProvider();
        this.grimEventBus = api.getEventBus();

        api.getEventBus().subscribe(grimPlugin, FlagEvent.class, event -> {
            GrimUser p = event.getPlayer();

            if (alertList.contains(p.getUniqueId()))
                return;

            alertList.add(p.getUniqueId());

            Player bukkitPlayer = Bukkit.getPlayer(p.getUniqueId());
            startRecording(bukkitPlayer, getReplayName(bukkitPlayer, event.getCheck().getCheckName()));

        });

        api.getEventBus().subscribe(grimPlugin, CommandExecuteEvent.class, event -> {
            if (!parseCommand(event.getCommand())) {
                return;
            }

            final Player p = Bukkit.getPlayer(event.getPlayer().getUniqueId());

            if (!punishList.contains(p.getUniqueId()))
                punishList.add(p.getUniqueId());
        });


    }

    @Override
    public void disinit() {
        if (grimEventBus == null || grimPlugin == null)
            return;

        invokeIfPresent(grimEventBus, "unsubscribe");
        invokeIfPresent(grimEventBus, "unregister");
    }


 /*   public void onFlag(FlagEvent event) {
        System.out.println("FlagEvent");
        Player p = Bukkit.getPlayer(event.getPlayer().getUniqueId());

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheck().getCheckName()));

    }

*/

  /*  public void onCommand(CommandExecuteEvent event) {
        System.out.println("CommandExec");
        if (!parseCommand(event.getCommand()))
            return;

        final Player p = Bukkit.getPlayer(event.getPlayer().getUniqueId());

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }

   */

    /**
     * Check if CommandExecute returns a command that should save a recording
     * @param command
     * @return
     */
    private boolean parseCommand(String command) {
        System.out.println(command);
        command = command.toLowerCase();
        String[] parts = command.split(" ");
        for (String commands : punishCommands) {
            if (parts[0].contentEquals(commands.toLowerCase()))
                return true;
        }
        return false;
    }

    private void initGrimSpecificConfig() {
        this.punishCommands = acReplay.getConfig().getStringList("Grim.Punish-Commands");
    }

    private void invokeIfPresent(Object target, String methodName) {
        try {
            Method method = target.getClass().getMethod(methodName, GrimPlugin.class);
            method.invoke(target, grimPlugin);
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException | InvocationTargetException ex) {
            acReplay.log("Unable to unregister Grim listener via " + methodName + ": " + ex.getMessage(), true);
        }
    }

}


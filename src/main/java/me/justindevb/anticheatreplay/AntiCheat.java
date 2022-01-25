package me.justindevb.anticheatreplay;


import me.justindevb.anticheatreplay.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public enum AntiCheat {
    VULCAN("Vulcan", "Vulcan", null, VulcanListener::new),
    SPARTAN("Spartan", "Spartan", null, SpartanListener::new),
    MATRIX("Matrix", "Matrix", null, MatrixListener::new),
    GODSEYE("GodsEye", "GodsEye", null, GodsEyeListener::new),
    /*   KAURI("Kauri", "Kauri", new Function<AntiCheatReplay, Boolean>() {
           @Override
           public Boolean apply(AntiCheatReplay antiCheatReplay) {
               Plugin kauri = Bukkit.getPluginManager().getPlugin("Kauri");
               if (kauri == null || !kauri.isEnabled())
                   return false;

               Plugin atlas = Bukkit.getPluginManager().getPlugin("Atlas");
               if (atlas == null || !atlas.isEnabled()) {
                   antiCheatReplay.log("Atlas is required to use Kauri!", true);
                   return false;
               }

               antiCheatReplay.log("Kauri detected, enabling support...", false);
               return true;
           }
       }, KauriListener::new),


       KARHU("Karhu", "Karhu", new Function<AntiCheatReplay, Boolean>() {
           @Override
           public Boolean apply(AntiCheatReplay antiCheatReplay) {
               Plugin karhu = Bukkit.getPluginManager().getPlugin("Karhu");
               if (karhu == null || !karhu.isEnabled())
                   return false;

               Plugin karhuAPI = Bukkit.getPluginManager().getPlugin("KarhuAPI");
               if (karhuAPI == null || !karhuAPI.isEnabled()) {
                   antiCheatReplay.log("KarhuAPI is required to use Kauri", true);
                   return false;
               }

               antiCheatReplay.log("Karhu detected, enabling support...", false);
               return true;
           }

     }, KarhuListener::new), */
    THEMIS("Themis", "Themis", null, ThemisListener::new),
    SOAROMA("Soaroma", "SoaromaSAC", null, SoaromaListener::new),
    FLAPPYAC("FlappyAC", "FlappyAnticheat", null, FlappyACListener::new),
   // ARTEMIS("Artemis", "Loader", null, ArtemisListener::new),
    ANTICHEATRELOADED("AntiCheatReloaded", "AntiCheatReloaded", null, AntiCheatReloadedListener::new),
    VERUS("Verus", "Verus", antiCheatReplay -> {
        Plugin verus = Bukkit.getPluginManager().getPlugin("Verus");
        if (verus == null || !verus.isEnabled())
            return false;

        Plugin verusAPI = Bukkit.getPluginManager().getPlugin("VerusAPI");
        if (verusAPI == null || !verusAPI.isEnabled()) {
            antiCheatReplay.log("VerusAPI is required to use Verus!", true);
            return false;
        }

        antiCheatReplay.log("Verus detected, enabling support...", false);
        return true;
    }, VerusListener::new),
    SPARKY("Sparky", "Sparky", null, SparkyListener::new),
    INTAVE("Intave", "Intave", null, IntaveListener::new),
    LIGHTANTICHEAT("LightAntiCheat", "LightAntiCheat", null, LightAntiCheatListener::new);

    private final String name;
    private final String pluginName;
    private final Function<AntiCheatReplay, Boolean> checker;
    private final Function<AntiCheatReplay, ListenerBase> instantiator;

    AntiCheat(String name, String pluginName, Function<AntiCheatReplay, Boolean> checker, Function<AntiCheatReplay, ListenerBase> instantiator) {
        this.name = name;
        this.pluginName = pluginName;

        if (checker == null) {
            checker = antiCheatReplay -> {
                Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
                if (plugin == null || !plugin.isEnabled())
                    return false;
                antiCheatReplay.log(name + " detected, enabling support..", false);
                return true;
            };
        }
        this.checker = checker;
        this.instantiator = instantiator;
    }

    public String getName() {
        return name;
    }

    public String getPluginName() {
        return pluginName;
    }

    public Function<AntiCheatReplay, Boolean> getChecker() {
        return checker;
    }

    public Function<AntiCheatReplay, ListenerBase> getInstantiator() {
        return instantiator;
    }
}

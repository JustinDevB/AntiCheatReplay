package me.justindevb.anticheatreplay;

import me.justindevb.anticheatreplay.listeners.AntiCheats.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import javax.annotation.Nullable;

public enum AntiCheat {
	GLADIATOR("Gladiator", GladiatorListener::new),
	VULCAN("Vulcan", VulcanListener::new),
	SPARTAN("Spartan", SpartanListener::new),
	VACAN("Vacan", VacanListener::new),
	MATRIX("Matrix", MatrixListener::new),
	GODSEYE("GodsEye", GodsEyeListener::new),
	KAURI("Kauri", "Kauri", antiCheatReplay -> {
		if (!hasPlugin("Kauri"))
			return false;
		if (!hasPlugin("Atlas")) {
			antiCheatReplay.log("Atlas is required to use Kauri!", true);
			return false;
		}

		antiCheatReplay.log("Kauri detected, enabling support...", false);
		return true;
	}, (replay) -> createWithClass(KauriListener.class, replay)),

	KARHU("Karhu", "Karhu", antiCheatReplay -> {
		if (!hasPlugin("KarhuLoader"))
			return false;
		if (!hasPlugin("KarhuAPI")) {
			antiCheatReplay.log("KarhuAPI is required to use Karhu", true);
			return false;
		}

		antiCheatReplay.log("Karhu detected, enabling support...", false);
		return true;
	}, (replay) -> createWithClass(KarhuListener.class, replay)),
	THEMIS("Themis", ThemisListener::new),
	SOAROMA("Soaroma", "SoaromaSAC", null, SoaromaListener::new),
	FLAPPYAC("FlappyAC", "FlappyAnticheat", null, FlappyACListener::new),
//	ARTEMIS("Artemis", "Loader", null, ArtemisListener::new),
	ANTICHEATRELOADED("AntiCheatReloaded", "AntiCheatReloaded", null, AntiCheatReloadedListener::new),
	VERUS("Verus", "Verus", antiCheatReplay -> {
		if (!hasPlugin("Verus"))
			return false;
		if (!hasPlugin("VerusAPI")) {
			antiCheatReplay.log("VerusAPI is required to use Verus!", true);
			return false;
		}

		antiCheatReplay.log("Verus detected, enabling support...", false);
		return true;
	}, VerusListener::new),
	SPARKY("Sparky", SparkyListener::new),
	INTAVE("Intave", IntaveListener::new),
	LIGHTANTICHEAT("LightAntiCheat", LightAntiCheatListener::new),
	ANTIHAXERMAN("AntiHaxerman", AntiHaxermanListener::new),
//	GRIMAC("GrimAC", GrimACListener::new),
	REFLEX("Reflex", ReflexListener::new),
	ASTROAC("AstroAC", AstroACListener::new),
	NEGATIVITY_V1("NegativityV1", "Negativity", antiCheatReplay -> {
		if (!hasPlugin("Negativity"))
			return false;
		if (Bukkit.getPluginManager().getPlugin("Negativity").getDescription().getVersion().startsWith("2.")) // check if it's v1
			return false;
		antiCheatReplay.log("Negativity v1 detected, enabling support..", false);
		return true;
	}, NegativityV1Listener::new);
/*	NEGATIVITY_V2("NegativityV2", "Negativity", antiCheatReplay -> {
		if (!hasPlugin("Negativity"))
			return false;
		if (Bukkit.getPluginManager().getPlugin("Negativity").getDescription().getVersion().startsWith("1.")) // not v2
			return false;
		antiCheatReplay.log("Negativity v2 detected, enabling support..", false);
		return true;
	}, (replay) -> createWithClass(NegativityV2Listener.class, replay));
*/
	private final String name;
	private final String pluginName;
	private final Function<AntiCheatReplay, Boolean> checker;
	private final Function<AntiCheatReplay, ListenerBase> instantiator;

	AntiCheat(String name, Function<AntiCheatReplay, ListenerBase> instantiator) {
		this(name, name, null, instantiator);
	}

	AntiCheat(String name, String pluginName, @Nullable Function<AntiCheatReplay, Boolean> checker, Function<AntiCheatReplay, ListenerBase> instantiator) {
		this.name = name;
		this.pluginName = pluginName;

		if (checker == null) {
			checker = antiCheatReplay -> {
				if (!hasPlugin(pluginName))
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

	private static ListenerBase createWithClass(Class<? extends ListenerBase> instantiatorClass, AntiCheatReplay replay) {
		try {
			return instantiatorClass.getConstructor(AntiCheatReplay.class).newInstance(replay);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static boolean hasPlugin(String pluginName) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		return plugin != null && plugin.isEnabled();
	}
}

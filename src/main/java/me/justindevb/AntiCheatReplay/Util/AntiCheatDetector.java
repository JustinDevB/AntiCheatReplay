package me.justindevb.anticheatreplay.Util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.justindevb.anticheatreplay.AntiCheatReplay;

public class AntiCheatDetector {
	private static AntiCheatDetector instance = null;
	private final AntiCheatReplay acReplay;

	private AntiCheatDetector() {
		instance = this;
		acReplay = AntiCheatReplay.getInstance();
	}

	/**
	 * Check if Vulcan is running on the server
	 */
	public boolean checkVulcanInstalled() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Vulcan");
		if (plugin == null || !plugin.isEnabled())
			return false;
		acReplay.log("Vulcan detected, enabling support..", false);
		return true;
	}

	/**
	 * Check if Spartan is running on the server
	 * 
	 * @return
	 */
	public boolean checkSpartanInstalled() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Spartan");
		if (plugin == null || !plugin.isEnabled())
			return false;
		acReplay.log("Spartan detected, enabling support..", false);
		return true;
	}

	/**
	 * Check if Matrix is running on the server
	 * 
	 * @return
	 */
	public boolean checkMatrixInstalled() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("Matrix");
		if (plugin == null || !plugin.isEnabled())
			return false;
		acReplay.log("Matrix detected, enabling support..", false);
		return true;
	}

	/**
	 * Check if GodsEye is running on the server
	 * 
	 * @return
	 */
	public boolean checkGodsEyeInstalled() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("GodsEye");
		if (plugin == null || !plugin.isEnabled())
			return false;
		acReplay.log("GodsEye detected, enabling support..", false);
		return true;
	}

	/**
	 * Check if Kauri is running on the server
	 * 
	 * @return
	 */
	public boolean checkKauriInstalled() {
		Plugin kauri = Bukkit.getPluginManager().getPlugin("Kauri");
		if (kauri == null || !kauri.isEnabled())
			return false;

		Plugin atlas = Bukkit.getPluginManager().getPlugin("Atlas");
		if (atlas == null || !atlas.isEnabled()) {
			acReplay.log("Atlas is required to use Kauri!", true);
			return false;
		}

		acReplay.log("Kauri detected, enabling support...", false);
		return true;
	}

	/**
	 * Check if Karhu is running on the server
	 * 
	 * @return
	 */
	public boolean checkKarhuInstalled() {
		Plugin karhu = Bukkit.getPluginManager().getPlugin("KarhuLoader");
		if (karhu == null || !karhu.isEnabled())
			return false;
		acReplay.log("Karhu detected, enabling support...", false);
		return true;
	}

	/**
	 * Check if Themis is running on the server
	 * 
	 * @return
	 */
	public boolean checkThemisInstalled() {
		Plugin themis = Bukkit.getPluginManager().getPlugin("Themis");
		if (themis == null || !themis.isEnabled())
			return false;
		acReplay.log("Themis detected, enabling support...", false);
		return true;
	}
	
	/**
	 * Check if Soaroma is running on the server
	 * 
	 * @return
	 */
	public boolean checkSoaromaInstalled() {
		Plugin soaroma = Bukkit.getPluginManager().getPlugin("SoaromaSAC");
		if (soaroma == null || !soaroma.isEnabled())
			return false;
		acReplay.log("Soaroma detected, enabling support...", false);
		return true;
	}
	
	/**
	 * Check if FlappyAC is running on the server
	 * 
	 * @return
	 */
	public boolean checkFlappyInstalled() {
		Plugin flappy = Bukkit.getPluginManager().getPlugin("FlappyAnticheat");
		if (flappy == null || !flappy.isEnabled())
			return false;
		acReplay.log("FlappyAC detected, enabling support...", false);
		return true;
	}

	public static AntiCheatDetector getInstance() {
		if (instance == null)
			instance = new AntiCheatDetector();
		return instance;
	}

}

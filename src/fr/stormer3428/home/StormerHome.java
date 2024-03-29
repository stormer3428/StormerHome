package fr.stormer3428.home;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.home.common.Message;

public class StormerHome extends JavaPlugin{

	public static StormerHome i;
	public static final String UUID = "a39d1ae3-18c5-4c02-8f91-bcb5207d437f";

	@Override
	public void onEnable() {
		i = this;
		getCommand("home").setExecutor(new HomeCommand());
		getCommand("home").setTabCompleter(new HomeTabCompleter());
		getCommand("sethome").setExecutor(new HomeCommand());
		getCommand("sethome").setTabCompleter(new HomeTabCompleter());
		getCommand("delhome").setExecutor(new HomeCommand());
		getCommand("delhome").setTabCompleter(new HomeTabCompleter());
		getCommand("homes").setExecutor(new HomeCommand());
		getCommand("shreload").setExecutor(new HomeCommand());

		getCommand("superadminhome").setExecutor(new HomeCommand());
		getCommand("superadminhome").setTabCompleter(new HomeTabCompleter());

		loadConfig();
		Message.instantiateLang(StormerHome.i);
		ConfigurationSection oldData = getConfig().getConfigurationSection("homes");
		if(oldData != null) {
			Message.normal("Detected an old data format, starting up the updaterListener");
			Message.normal("This should only happen if you have update from <0.0.9 to >0.1.1");
			Message.systemNormal("Detected an old data format, starting up the updaterListener");
			Message.systemNormal("This should only happen if you have updated from <0.0.9 to >0.1.1");

			getServer().getPluginManager().registerEvents(new UpdaterListener(), i);

		}

		reload();
		super.onEnable();		
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void reload() {
		loadConfig();
		Message.instantiateLang(StormerHome.i);
		Home.all.clear();
		ConfigurationSection homesSection = getConfig().getConfigurationSection("homes2");
		boolean debug = getConfig().getBoolean("logHomesRegistering");
		Message.systemNormal("Loading all homes...");
		if(homesSection == null) {
			Message.systemNormal("No homes registered (couldn't find 'homes2' yaml section)");
			return;
		}

		for(String playerUUID : homesSection.getKeys(false)) {
			ConfigurationSection playerHomesSection = homesSection.getConfigurationSection(playerUUID);
			if(playerHomesSection == null) {
				Message.systemError("Config section for " + playerUUID + " is null! skipping...");
				continue;
			}
			if(debug) Message.systemNormal("Loading homes of UUID " + playerUUID + "...");
			for(String home : playerHomesSection.getKeys(false)) {
				ConfigurationSection homeSection = playerHomesSection.getConfigurationSection(home);

				if(homeSection == null) {
					Message.systemError("Config section for " + playerUUID + "." + home +" is null! skipping...");
					continue;
				}
				
				//Message.systemNormal("parsing home " + homeSection.getCurrentPath() + " ...");

				String sx = 		homeSection.getString("x");
				String sy = 		homeSection.getString("y");
				String sz = 		homeSection.getString("z");
				String syaw = 		homeSection.getString("yaw");
				String spitch = 	homeSection.getString("pitch");
				String sworld = 	homeSection.getString("world");
				String playername = homeSection.getString("playername");
				
				try {
					if(Bukkit.getPlayer(java.util.UUID.fromString(playerUUID)) != null) playername = Bukkit.getPlayer(java.util.UUID.fromString(playerUUID)).getName();
				}catch (Exception e) {
					Message.error("Found a home that use formatted using the v0.0.9 format, the format changed to now use UUIDs and thus, is not retro compatible...");
					Message.error("to port that home, simply put it back in the \"homes\" section instead of \"home2\" and it will be ported automatically when the owner of the home joins");
					continue;
				}
				try {
					double x = Double.parseDouble(sx);
					double y = Double.parseDouble(sy);
					double z = Double.parseDouble(sz);
					float yaw = Float.parseFloat(syaw);
					float pitch = Float.parseFloat(spitch);
					World world = Bukkit.getWorld(sworld);

					if(world == null) {
						if(debug) Message.systemError("invalid world name for home : (" + homeSection.getCurrentPath() + ")");
						if(debug) Message.systemError("skipping...");
						loadConfig();
					}
					else Home.createHome(new Location(world, x, y, z, yaw, pitch), java.util.UUID.fromString(playerUUID), home, playername);

				} catch (Exception e) {
					Message.systemError("invalid configuration for home : (" + homeSection.getCurrentPath() + ")");
					Message.systemError("skipping...");
				}
			}
			if(debug) Message.systemNormal("Successfully loaded " + Home.getPlayerHomes(java.util.UUID.fromString(playerUUID)).size() + " homes!");
		}
		loadConfig();
		cleanupPlayerWithNoHomes();
		int size = Home.all.size();
		Message.systemNormal(size + " home" + (size > 1 ? "s" : "") + " loaded!");
	}

	public void cleanupPlayerWithNoHomes() {
		ConfigurationSection homesSection = getConfig().getConfigurationSection("homes2");
		for(String playerUUID : homesSection.getKeys(false)) if(homesSection.getConfigurationSection(playerUUID).getKeys(false).size() == 0) homesSection.set(playerUUID, null);
		loadConfig();
	}	

}

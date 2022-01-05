package fr.stormer3428.home;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class StormerHome extends JavaPlugin{

	public static StormerHome i;
	
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
		
		reload();
		
		super.onEnable();		
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void reload() {
		Home.all.clear();
		Set<String> keys = getConfig().getKeys(true);

		Set<String> players = new HashSet<>();
		
		for(String s : keys) {
			String[] args = s.split("\\.");
			if(args.length > 1 && args[0].equals("homes")) {
				if(!players.contains(args[1])) players.add(args[1]);
			}
		}
		
		for(String player : players) {
			Set<String> homes = new HashSet<>();

			for(String s : keys) {
				String[] args = s.split("\\.");
				if(args.length > 2 && args[0].equals("homes") && args[1].equals(player)) {
					if(!homes.contains(args[2])) homes.add(args[2]);
				}
			}
			
			for(String home : homes) {
				
				System.out.println(player + "." + home);
				
				String path = "homes." + player + "." + home + ".";
				String sx = getConfig().getString(path + "x");
				String sy = getConfig().getString(path + "y");
				String sz = getConfig().getString(path + "z");
				String syaw = getConfig().getString(path + "yaw");
				String spitch = getConfig().getString(path + "pitch");
				String sworld = getConfig().getString(path + "world");
				try {
					double x = Double.parseDouble(sx);
					double y = Double.parseDouble(sy);
					double z = Double.parseDouble(sz);
					float yaw = Float.parseFloat(syaw);
					float pitch = Float.parseFloat(spitch);
					World world = Bukkit.getWorld(sworld);
					
					if(world == null) System.err.println("invalid world name for home : (" + path + ")");
					else {
						Home h = new Home(new Location(world, x, y, z, yaw, pitch), player, home);
						System.out.println("Created home");
						System.out.println(h.toString());
					}
				} catch (Exception e) {
					System.err.println("invalid configuration for home : (" + path + ")");
				}
				
			}
		}
	}
	
}

package fr.stormer3428.home;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Home {

	private Location location;
	private String owner;
	private String name;

	static List<Home> all = new ArrayList<>();

	public Home(@Nonnull Location loc,@Nullable Player p, String n) {
		this(loc, p.getName(), n);
	}

	public Home(@Nonnull Location loc,@Nullable String name, String n) {
		String trunc = name.replaceAll("\\.+", ".");
		for(Home home : all) {
			if(home.owner == trunc && home.name == n) {
				home.setLocation(loc);
				home.setOwner(trunc);
				home.setName(n);
				createHome(home);
				return;
			}
		}
		this.location = loc;
		this.owner = trunc;
		this.name = n;
		createHome(this);
		all.add(this);
	}
	
	private static void createHome(Home home) {
		String path = "homes." + home.owner + "." + home.name + ".";
		StormerHome.i.getConfig().set(path + "x", home.location.getX());
		StormerHome.i.getConfig().set(path + "y", home.location.getY());
		StormerHome.i.getConfig().set(path + "z", home.location.getZ());
		StormerHome.i.getConfig().set(path + "yaw", home.location.getYaw());
		StormerHome.i.getConfig().set(path + "pitch", home.location.getPitch());
		StormerHome.i.getConfig().set(path + "world", home.location.getWorld().getName());
		StormerHome.i.loadConfig();
	}
	
	public static void deleteHome(Home home) {
		String path = "homes." + home.owner + "." + home.name;
		StormerHome.i.getConfig().set(path, "");
		StormerHome.i.loadConfig();
		all.remove(home);
	}
	
	public void delete() {
		deleteHome(this);
	}

	public static Set<Home> getPlayerHomes(String p){
		Set<Home> homes = new HashSet<>();
		String trunc = p.replaceAll("\\.+", ".");
		for(Home home : Home.all) {
			if(home.getOwner().equals(trunc)) {
				homes.add(home);
			}
		}
		return homes;
	}

	public static Set<Home> getPlayerHomes(Player p){
		return getPlayerHomes(p.getName());
	}
	
	public static Home findHome(Player p, String name) {
		return findHome(p.getName(), name);
	}
	
	public static Home findHome(String p, String name) {
		for(Home home : getPlayerHomes(p)) {
			if(home.getName().equals(name)) return home;
		}
		for(Home home : getPlayerHomes(p)) {
			if(home.getName().equalsIgnoreCase(name)) return home;
		}
		return null;
	}
	
	public void home(Player p) {
		Message.normal(p, "Going to " + getName());
		getLocation().getChunk().load(true);
		p.teleport(getLocation());
	}
	
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String p) {
		this.owner = p;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	@Override
	public String toString() {
		return "[Home {"+this.location.toString()+","+this.owner+","+this.name+"}]";
	}

}

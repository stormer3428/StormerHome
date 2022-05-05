package fr.stormer3428.home;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.stormer3428.home.common.Lang;
import fr.stormer3428.home.common.Message;

public class Home {

	private Location location;
	private final UUID ownerUUID;
	private final String ownerName;
	private final String name;

	static List<Home> all = new ArrayList<>();



	public static Home createHome(Location loc, UUID uuid, String homeName, String ownerName) {
		for(Home home : all) {
			if(home.ownerUUID.equals(uuid) && home.name.equalsIgnoreCase(homeName)) {
				home.setLocation(loc);
				saveToConfig(home);
				return home;
			}
		}
		return new Home(loc, uuid, homeName, ownerName);
	}
	
	private Home(Location loc, UUID uuid, String homeName, String ownerName) {
		this.ownerName = ownerName;
		this.location = loc;
		this.ownerUUID = uuid;
		this.name = homeName;
		saveToConfig(this);
		all.add(this);
	}
	
	private static void saveToConfig(Home home) {
		String path = "homes." + home.ownerUUID + "." + home.name + ".";
		StormerHome.i.getConfig().set(path + "x", home.location.getX());
		StormerHome.i.getConfig().set(path + "y", home.location.getY());
		StormerHome.i.getConfig().set(path + "z", home.location.getZ());
		StormerHome.i.getConfig().set(path + "yaw", home.location.getYaw());
		StormerHome.i.getConfig().set(path + "pitch", home.location.getPitch());
		StormerHome.i.getConfig().set(path + "world", home.location.getWorld().getName());
		StormerHome.i.getConfig().set(path + "playername", home.ownerName);
		StormerHome.i.loadConfig();
	}
	
	public static void deleteHome(Home home) {
		String path = "homes." + home.ownerUUID + "." + home.name;
		StormerHome.i.getConfig().set(path, null);
		StormerHome.i.cleanupPlayerWithNoHomes();
		StormerHome.i.loadConfig();
		all.remove(home);
	}
	
	public void delete() {
		deleteHome(this);
	}

	public static Set<Home> getPlayerHomes(UUID uuid){
		Set<Home> homes = new HashSet<>();
		for(Home home : Home.all) {
			if(home.getOwner().equals(uuid)) {
				homes.add(home);
			}
		}
		return homes;
	}

	public static Set<Home> getPlayerHomes(String ownerName){
		Set<Home> homes = new HashSet<>();
		for(Home home : Home.all) if(home.getOwnerName().equals(ownerName)) homes.add(home);
		return homes;
	}

	public static Set<Home> getPlayerHomes(Player p){
		return getPlayerHomes(p.getUniqueId());
	}
	
	public static Home findHome(Player owner, String name) {
		return findHome(owner.getUniqueId(), name);
	}
	
	public static Home findHome(UUID uuid, String name) {
		for(Home home : getPlayerHomes(uuid)) if(home.getName().equalsIgnoreCase(name)) return home;
		return null;
	}
	
	public static Home findHome(String ownerName, String name) {
		for(Home home : all) if(home.getName().equalsIgnoreCase(name) && home.ownerName.equals(ownerName)) return home;
		return null;
	}
	
	public void home(Player p) {
		Message.normal(p, Lang.COMMAND_SUCCESS_HOME.toString().replace("{HOME}", getName()));
		getLocation().getChunk().load(true);
		p.teleport(getLocation());
	}
	
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public UUID getOwner() {
		return this.ownerUUID;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "[Home {"+this.location.toString()+","+this.ownerUUID+","+this.name+"}]";
	}

	public String getOwnerName() {
		return ownerName;
	}

}

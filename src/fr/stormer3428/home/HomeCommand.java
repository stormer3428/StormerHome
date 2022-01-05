package fr.stormer3428.home;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("homes")) {
				listHomes(p);
				return true;
			}else if(cmd.getName().equalsIgnoreCase("home")) {
				if(args.length == 0) {
					Home home = Home.findHome(p, "Home");
					if(home != null) {
						home.home(p);
						return true;
					}
					Message.error(p, "You do not have any default home set");
					return false;
				}
				Home home = Home.findHome(p, args[0]);
				if(home != null) {
					home.home(p);
					return true;
				}
				Message.error(p, "No home with such name : " + args[0]);
				return false;
			}else if(cmd.getName().equalsIgnoreCase("sethome")) {
				int maxHomes = p.isOp() ? StormerHome.i.getConfig().getInt("maxOpHomes") : StormerHome.i.getConfig().getInt("maxHomes");
				int homes = Home.getPlayerHomes(p).size();
				if(homes >= maxHomes && maxHomes != -1) {
					Message.error(p, "You nhave too many homes set");
					return false;
				}
				if(args.length == 0) {
					new Home(p.getLocation(), p, "Home");
					Message.normal(p, "Home set");
					return true;
				}
				new Home(p.getLocation(), p, args[0]);
				Message.normal(p, "Home "+args[0]+" set");
				return true;
			}else if(cmd.getName().equalsIgnoreCase("delhome")) {
				if(args.length == 0) {
					Message.error(p, "You need to specify a name");
					Message.error(p, "Usage : delhome <name>");
					return false;
				}
				Home home = Home.findHome(p, args[0]);
				if(home != null) {
					home.delete();
					Message.normal(p, "Home " + home.getName() + " removed!");
					return true;
				}
				Message.error(p, "No home with such name : " + args[0]);
				return false;
			}else if(cmd.getName().equalsIgnoreCase("shreload")) {
				if(p.isOp()) {
					StormerHome.i.reload();
					Message.normal(p, "Successfully relaoded the plugin");
					return true;
				}
				Message.error(p, "You do not have the permission to use this command");
				return false;
			}
		}
		return false;
	}

	private static void listHomes(Player p) {
		Message.normal(p, "<===========(Homes of "+p.getName()+")===========");
		for(Home home : Home.getPlayerHomes(p)) {
			Message.normal(p, " - " + home.getName() + " :");
			Location loc = home.getLocation();
			Message.normal(p, "Location : " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
			Message.normal(p, "World : " + loc.getWorld().getName());
		}
		Message.normal(p, "<===========(Homes)===========");
	}

}

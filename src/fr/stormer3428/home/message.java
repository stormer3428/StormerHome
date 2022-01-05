package fr.stormer3428.home;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Message {

	private static final String PLUGIN_NAME = "Stormer's Home";
	private static final ChatColor a = ChatColor.AQUA;
	private static final ChatColor b = ChatColor.YELLOW;
	private static final ChatColor c = ChatColor.GREEN;
	
	public static void normal(CommandSender p,String strg){
		String m = (a + "[" + b + PLUGIN_NAME + a + "] "  + c + strg);
		p.sendMessage(m);
	}
	
	public static void normal(String strg){
		for(Player p : Bukkit.getOnlinePlayers()){
			normal(p, strg);
		}
	}
	
	public static void normal(String strg, List<String> p){
		for(Player pls : Bukkit.getOnlinePlayers()){
			if(p.contains(pls.getName())) normal(pls, strg);
		}
	}

	public static void error(CommandSender p, String strg){
		String m = (a + "[" + b + "Error" + a + "] "  + ChatColor.RED + strg);
		p.sendMessage(m);
	}
	
	public static void error(String strg){
		for(Player p : Bukkit.getOnlinePlayers()){
			error(p, strg);
		}
	}
	
	public static void error(String strg, List<String> p){
		for(Player pls : Bukkit.getOnlinePlayers()){
			if(p.contains(pls.getName())) error(pls, strg);
		}
	}

	public static void systemNormal(String strg){
		String m = (a + "[" + b + PLUGIN_NAME + a + "] "  + c + strg);
		Bukkit.getConsoleSender().sendMessage(m);
	}
	
	public static void systemError(String strg){
		String m = (a + "[" + b + PLUGIN_NAME + a + "] "  + ChatColor.RED + strg);
		Bukkit.getConsoleSender().sendMessage(m);
	}

}

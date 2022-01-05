package fr.stormer3428.home;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class message {

	
	
	public static void normal(Player p,String strg){
		String m = (ChatColor.AQUA + "[" + ChatColor.YELLOW + "Home" + ChatColor.AQUA + "] "  + ChatColor.GREEN + strg);
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

	public static void error(Player p, String strg){
		String m = (ChatColor.AQUA + "[" + ChatColor.YELLOW + "Home" + ChatColor.AQUA + "] "  + ChatColor.RED + strg);
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
}

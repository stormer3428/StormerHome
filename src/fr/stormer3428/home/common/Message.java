package fr.stormer3428.home.common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Message {

	private static JavaPlugin plugin;

	public static void instantiateLang(JavaPlugin p) {
		plugin = p;
		loadLang();
	}

	private static void loadLang() {
		File lang = new File(plugin.getDataFolder(), "lang.yml");
		YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(lang);
		if(!lang.exists()) try {
			langConfig.save(lang);
		}catch (IOException e) {
			e.printStackTrace();
			systemError("Failed to create language file");
			systemError("Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
		for(Lang l : Lang.values()) if(langConfig.getString(l.getPath()) == null) langConfig.set(l.getPath(), l.getDef());
		Lang.setFile(langConfig);
		try {
			langConfig.save(lang);
		}catch (IOException e) {
			e.printStackTrace();
			systemError("Failed to save language file");
			systemError("Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}

	public static void normal(CommandSender p,String strg){
		String m = Lang.PREFIX_COMMAND.toString() + strg;
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
		String m = Lang.PREFIX_ERROR.toString() + strg;
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
		String m = Lang.PREFIX_COMMAND.toString() + strg;
		Bukkit.getConsoleSender().sendMessage(m);
	}

	public static void systemError(String strg){
		String m = Lang.PREFIX_ERROR.toString() + strg;
		Bukkit.getConsoleSender().sendMessage(m);
	}

}

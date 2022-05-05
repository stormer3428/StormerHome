package fr.stormer3428.home.common;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {

	ERROR_NO_DEFAULT_HOME("You do not have any default home set"),
	ERROR_NO_HOME_SUCH_NAME("No home with such name : {HOME}"),
	ERROR_NO_PERMISSION("You do not have the permission to use this command"),
	ERROR_TOO_MANY_HOMES("You have too many homes set"),
	ERROR_MISSING_NAME("You need to specify a name"),
	ERROR_MOVED("You moved! teleportation cancelled..."),

	COMMAND_SUCCESS_HOME("Going to {HOME}, please stand still..."),
	COMMAND_SUCCESS_SETHOME("Home {HOME} set"),
	COMMAND_SUCCESS_DELHOME("Home {HOME} removed!"),
	COMMAND_SUCCESS_RELOAD("Successfully relaoded the plugin"),

	COMMAND_LISTHOMES_HEADER("<===========(Homes of {PLAYERNAME})===========>"),
	COMMAND_LISTHOMES_BODY(" - {HOME} : {HOME.WORLD} {HOME.X} {HOME.Y} {HOME.Z}"),
	COMMAND_LISTHOMES_FOOTER("<===========(Homes of {PLAYERNAME})===========>"),
	
	COMMAND_USAGE_DELHOME("Usage : delhome <name>"),
	;
	
	private String path;
	private String def;
	private static YamlConfiguration LANG;
	
	Lang(String d){
		this.path = this.name();
		this.def = d;
	}
	
	public static void setFile(YamlConfiguration config) {
		LANG = config;
	}
	
	@Override
	public String toString() {
		return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def));
	}
	
	public String getPath() {
		return path;
	}

	public String getDef() {
		return def;
	}
}

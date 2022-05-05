package fr.stormer3428.home;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.stormer3428.home.common.Message;

public class UpdaterListener implements Listener {

	public UpdaterListener() {
		reduceOldData();
	}

	private void reduceOldData() {
		ConfigurationSection homesSection = StormerHome.i.getConfig().getConfigurationSection("homes");
		for(String playerName : homesSection.getKeys(false)) {
			if(homesSection.getConfigurationSection(playerName).getKeys(false).size() == 0) homesSection.set(playerName, null);
		}
		if(homesSection.getKeys(false).size() == 0) StormerHome.i.getConfig().set("homes", null);
		StormerHome.i.loadConfig();
	}


	@EventHandler
	public void onjoin(PlayerJoinEvent e) {
		reduceOldData();
		Message.systemNormal("Checking if player " + e.getPlayer().getName() + " has any homes stored in the old format...");

		String formattedPlayerName = e.getPlayer().getName().replace(".", "");
		ConfigurationSection homesSection = StormerHome.i.getConfig().getConfigurationSection("homes");

		Set<String> playerNames = homesSection.getKeys(false);
		if(!playerNames.contains(formattedPlayerName)) {
			Message.systemNormal("Everything seems up to date");
			return;
		}

		ConfigurationSection playerhomesSection = homesSection.getConfigurationSection(formattedPlayerName);

		Set<String> playerHomes = playerhomesSection.getKeys(false);
		Message.systemNormal("Found data, trying to port");
		for(String playerHome : playerHomes) {
			Message.systemNormal("Trying to port " + playerHome);

			ConfigurationSection playerHomeSection = playerhomesSection.getConfigurationSection(playerHome);

			double x = playerHomeSection.getDouble("x");
			double y = playerHomeSection.getDouble("y");
			double z = playerHomeSection.getDouble("z");
			double yaw = playerHomeSection.getDouble("yaw");
			double pitch = playerHomeSection.getDouble("pitch");
			String worldname = playerHomeSection.getString("world");

			World world = Bukkit.getWorld(worldname);
			if(world == null) {
				Message.systemError("Home " + playerHome + " refers to a non existant world " + worldname);
				continue;
			}

			Home.createHome(new Location(world, x, y, z, (float) yaw, (float) pitch), e.getPlayer().getUniqueId(), playerHome, e.getPlayer().getName());
			playerhomesSection.set(playerHome, null);
			Message.systemNormal(playerHome + " successfully ported!");
		}
		
		reduceOldData();
	}
}









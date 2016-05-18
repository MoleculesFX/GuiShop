package moleculesfx.GUIShop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemsConfig {

	public static Main pl;
	public ItemsConfig(Main pl){
		this.pl = pl;
	}
	private FileConfiguration itemConfig = null;
	private File itemConfigFile = null;
	public void reloadCustomConfig(){
		if (itemConfigFile == null){
			itemConfigFile = new File(pl.getDataFolder(), "Items.yml");
		}
		itemConfig = YamlConfiguration.loadConfiguration(itemConfigFile);
		InputStream defConfigStream = pl.getResource("Items.yml");
		if(defConfigStream != null){
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			itemConfig.setDefaults(defConfig);
			itemConfig.options().copyDefaults(true);
		}
	}
	public FileConfiguration getCustomConfig(){
		if(itemConfig == null){
			reloadCustomConfig();
		}
		return itemConfig;
	}
	public void saveCustomConfig(){
		if(itemConfig == null || itemConfigFile == null){
			return;
		}
		try{
			getCustomConfig().save(itemConfigFile);
		} catch (IOException ex){
			pl.getLogger().log(Level.SEVERE, "Could not save config to " + itemConfigFile, ex);
		}
	}

}

package moleculesfx.GUIShop;

import java.util.HashMap;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Utils {
	
	static HashMap<String, String> hm = new HashMap<String, String>();
	static YamlConfiguration conf = YamlConfiguration.loadConfiguration(Main.itemsf);
	
	public static String ColorMsg(String path){
		return ChatColor.translateAlternateColorCodes('&', path);
	}
	public static String firstAllUpperCased(String name){
		return WordUtils.capitalizeFully(name);
	}
	public static boolean isInt(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	public static String getitemConfig(String string){
		for(String s : conf.getConfigurationSection("items").getKeys(false)) {
			hm.put(s, conf.getString("items." + s));
		}
		return hm.get(string);
	}
	public static String getitemConfig2(String string){
		for(String s : conf.getConfigurationSection("items").getKeys(false)) {
			String b = conf.getString("items." + s);
			hm.put(b, s);
		}
		return hm.get(string);
	}
	public static String getFrndlyName(String s){
		String itemfriendlyname = firstAllUpperCased(getitemConfig(s));
		return itemfriendlyname;
	}
	public static String getUnFrndlyName(String s){
		String itemunfriendlyname = getitemConfig2(s);
		return itemunfriendlyname;
	}
}

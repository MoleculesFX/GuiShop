package moleculesfx.GUIShop;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

public class Utils {
	
	public static String ColorMsg(String path){
		return ChatColor.translateAlternateColorCodes('&', path);
	}
	public static String firstAllUpperCased(String name){
		return WordUtils.capitalizeFully(name);
	}
}

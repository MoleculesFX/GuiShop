package moleculesfx.GUIShop;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class ShopCreation implements Listener{
	
	private Main pl;
	public ShopCreation(Main pl){
		this.pl = pl;
	}
	@EventHandler
	public void SignListen(SignChangeEvent event){
		FileConfiguration Cfg = this.pl.getConfig();
		String Prefix = Utils.ColorMsg(Cfg.getString("Prefix"));
		String MSC = Utils.ColorMsg(Cfg.getString("Messages.Sign-Created"));
		String MNP = Utils.ColorMsg(Cfg.getString("Messages.No-Permission"));
		String[] Lines = event.getLines();
		Player player = event.getPlayer();
		if (Lines[0].equalsIgnoreCase("[guishop]")){
			CreateShop(player, Lines, event, Prefix, MSC, MNP);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private static void CreateShop(Player player, String[] Lines, SignChangeEvent event, String Prefix, String MSC, String MNP) {	
		if (player.hasPermission("guishop.create") || player.isOp()){
			if (Lines[1].contains(":")){
				String[] ItemData =  Lines[1].split(":");
				if (ItemData.length <= 3){
					int ID = Integer.parseInt(ItemData[0]);
					short DataTag = Short.parseShort(ItemData[1]);
					String BuyPrice = Lines[2];
					String SellPrice = Lines[3];
					double BPrice = Double.parseDouble(BuyPrice);
					double SPrice = Double.parseDouble(SellPrice);
					if (Material.getMaterial(ID) !=null){
						event.setLine(0, "§1§l[§2§lGUIShop§1§l]");
						event.setLine(1, Utils.firstAllUpperCased(Material.getMaterial(ID).name().toString().replace("_", " ") + ":" + DataTag));
						if (!Lines[3].equalsIgnoreCase("0")){
							event.setLine(3, "§bSell: §2$" + SPrice + "§1/ea");
						}
						if (Lines[3].equalsIgnoreCase("0") || Lines[3].equalsIgnoreCase("-1")){
							event.setLine(3, "§4Can't Sell this item!");
						}
						if (!Lines[2].equalsIgnoreCase("0")){
							event.setLine(2, "§bBuy: §2$" + BPrice + "§1/ea");
						}
						if (Lines[2].equalsIgnoreCase("0") || Lines[2].equalsIgnoreCase("-1")){
							event.setLine(2, "§4Can't Buy this item!");
						}
						player.sendMessage(Prefix + MSC);
					}
				}
			}
			if (Lines[1].contains("/")){
				String[] ItemData =  Lines[1].split("/");
				if (ItemData.length <= 3){
					int ID = Integer.parseInt(ItemData[0]);
					short DataTag = Short.parseShort(ItemData[1]);
					String BuyPrice = Lines[2];
					String SellPrice = Lines[3];
					double BPrice = Double.parseDouble(BuyPrice);
					double SPrice = Double.parseDouble(SellPrice);
					if (Material.getMaterial(ID) !=null){
						event.setLine(0, "§1§l[§2§lGUIShop§1§l]");
						event.setLine(1, Utils.firstAllUpperCased(Material.getMaterial(ID).name().toString().replace("_", " ") + ":" + DataTag));
						if (!Lines[3].equalsIgnoreCase("0")){
							event.setLine(3, "§bSell: §2$" + SPrice + "§1/ea");
						}
						if (Lines[3].equalsIgnoreCase("0") || Lines[2].equalsIgnoreCase("-1")){
								event.setLine(3, "§4Can't Sell this item!");
						}
						if (!Lines[2].equalsIgnoreCase("0")){
							event.setLine(2, "§bBuy: §2$" + BPrice + "§1/ea");
						}
						if (Lines[2].equalsIgnoreCase("0") || Lines[2].equalsIgnoreCase("-1")){
							event.setLine(2, "§4Can't Buy this item!");
						}
						player.sendMessage(Prefix + MSC);
					}
				}
			}
			else{
				int ID = Integer.parseInt(Lines[1]);
				String BuyPrice = Lines[2];
				String SellPrice = Lines[3];
				double BPrice = Double.parseDouble(BuyPrice);
				double SPrice = Double.parseDouble(SellPrice);
				if (Material.getMaterial(ID) !=null){
					event.setLine(0, "§1§l[§2§lGUIShop§1§l]");
					event.setLine(1, Utils.firstAllUpperCased(Material.getMaterial(ID).name().toString().replace("_", " ")));
					if (!Lines[3].equalsIgnoreCase("0")){
						event.setLine(3, "§bSell: §2$" + SPrice + "§1/ea");
					}
					if (Lines[3].equalsIgnoreCase("0") || Lines[3].equalsIgnoreCase("-1")){
						event.setLine(3, "§4Can't Sell this item!");
					}
					if (!Lines[2].equalsIgnoreCase("0")){
						event.setLine(2, "§bBuy: §2$" + BPrice + "§1/ea");
					}
					if (Lines[2].equalsIgnoreCase("0") || Lines[2].equalsIgnoreCase("-1")){
						event.setLine(2, "§4Can't Buy this item!");
					}
					player.sendMessage(Prefix + MSC);
				}
			}	
		}
		else {
			player.sendMessage(Prefix + MNP);
		}
	}
}

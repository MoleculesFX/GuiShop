package moleculesfx.GUIShop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.Lists;

import net.milkbowl.vault.economy.Economy;

public class SignListener implements Listener {
	public static Economy econ = null;
	private Main pl;
	public SignListener(Economy money, Main pl){
		this.pl = pl;
		econ = money;
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
	  Player player = event.getPlayer();
	  Action action = event.getAction();
	  if ((action == Action.RIGHT_CLICK_BLOCK) && ((event.getClickedBlock().getType().name() == "SIGN") || (event.getClickedBlock().getType().name() == "SIGN_POST") || (event.getClickedBlock().getType().name() == "WALL_SIGN")) && 
	    ((event.getClickedBlock().getState() instanceof Sign)))
	  {
	    Sign sign = (Sign)event.getClickedBlock().getState();
	    String[] Lines = sign.getLines();
	    if (Lines[0].equalsIgnoreCase("§1§l[§2§lGUIShop§1§l]"))
	    {
	      event.setCancelled(true);
	      if ((player.hasPermission("guishop.use")) || (player.isOp())){
	    	  
	    	  String SignID = Lines[1].toLowerCase();
	    	  String GetID = Utils.getUnFrndlyName(SignID);
	    	  if (GetID.contains("-")){
	    		  String[] ItemCode = GetID.split("-");
	    		  String ID = ItemCode[0].toUpperCase();
	    		  short DataTag = Short.parseShort(ItemCode[1]);
	    		  String BuyPrice = Lines[2].replace("§bBuy: §2$", "").replace("§1/ea", "").replace("§4Can't Buy this item!", "0");
	    		  String SellPrice = Lines[3].replace("§bSell: §2$", "").replace("§1/ea", "").replace("§4Can't Sell this item!", "0");
	    		  double BPrice = Double.parseDouble(BuyPrice);
	    		  double SPrice = Double.parseDouble(SellPrice);
	    		  player.openInventory(ShopItems(event, BPrice, SPrice, ID, DataTag, Lines));
	    	  }else if (!(GetID.contains("-"))){
	    		  String ID = GetID.toUpperCase();
	    		  short DataTag = 0;
	    		  String BuyPrice = Lines[2].replace("§bBuy: §2$", "").replace("§1/ea", "").replace("§4Can't Buy this item!", "0");
	    		  String SellPrice = Lines[3].replace("§bSell: §2$", "").replace("§1/ea", "").replace("§4Can't Sell this item!", "0");
	    		  double BPrice = Double.parseDouble(BuyPrice);
	    		  double SPrice = Double.parseDouble(SellPrice);
	    		  player.openInventory(ShopItems(event, BPrice, SPrice, ID, DataTag, Lines));
	    	  }
	      }
	      else {
	        player.sendMessage(ChatColor.RED + "You dont have permission to use this sign!");
	      }
	    }
	  }
	}
	
	@SuppressWarnings("deprecation")
	public Inventory ShopItems(PlayerInteractEvent event, double BPrice, double SPrice, String ID, short DataTag, String[] Lines) {	
		FileConfiguration conf = this.pl.getConfig();
		double Balance = econ.getBalance(event.getPlayer().getName());
		String PLName = event.getPlayer().getName();
		String PLDispName = event.getPlayer().getDisplayName();
		String forItemName = null;
		String FriendlyName = null;
		if (DataTag == 0){
			forItemName = Material.getMaterial(ID).name().toString();
			FriendlyName = Utils.getFrndlyName(forItemName);
		}
		if (DataTag != 0){
			forItemName = Material.getMaterial(ID).name().toString() + "-" + DataTag;
			FriendlyName = Utils.getFrndlyName(forItemName);
		}
		Inventory shopinv = Bukkit.createInventory(null, 9*conf.getInt("Rows"), Utils.ColorMsg(conf.getString("GUITitle")));
	    Set<String> BuySlots = conf.getConfigurationSection("BuyItems").getKeys(false);
	    Set<String> SellSlots = conf.getConfigurationSection("SellItems").getKeys(false);
	    Set<String> DispSlots = conf.getConfigurationSection("DispItems").getKeys(false);
		for (String b : BuySlots){
			int buySlot = conf.getInt("BuyItems." + b + ".Slot")-1;
			int Amount = conf.getInt("BuyItems." + b +".Amount");
        	ItemStack itembuy = new ItemStack(Material.getMaterial(ID), Amount, DataTag);
        	ItemMeta itembuyMeta = itembuy.getItemMeta();
			List<String> itembuyLore = conf.getStringList("BuyItems." + b + ".Lores");
			List<String> itembuyLoreStyle = conf.getStringList("PriceStyle");
			ArrayList<String> itembuyLore2 = Lists.newArrayList();
            for (String s2 : itembuyLore){
                String s3 = ChatColor.translateAlternateColorCodes('&', s2)
                		.replace("%sellprice%", this.pl.formatDouble(SPrice*Amount))
                		.replace("%buyprice%", this.pl.formatDouble(BPrice*Amount))
                		.replace("%balance%", this.pl.formatDouble(Balance))
                		.replace("%displayname%", PLDispName)
                		.replace("%player%", PLName)
                		.replace("%itemname%", FriendlyName);
                itembuyLore2.add(s3);
            }
            for (String s4 : itembuyLoreStyle){
            	if (BPrice == 0){
            		String PriceFormat = "§4You can't buy this kind of item.";
                    String s5 = ChatColor.translateAlternateColorCodes('&', s4)
                    		.replace("%price%", PriceFormat);
                    itembuyLore2.add(s5);
            	}else{
                	String PriceFormat = "Price: $" + this.pl.formatDouble(BPrice*Amount);
                    String s5 = ChatColor.translateAlternateColorCodes('&', s4)
                    		.replace("%price%", PriceFormat);
                    itembuyLore2.add(s5);
            	}
            }
			itembuyMeta.setLore(itembuyLore2);
			itembuyMeta.setDisplayName(Utils.ColorMsg(conf.getString("BuyItems." + b + ".Name")
            		.replace("%sellprice%", this.pl.formatDouble(SPrice*Amount))
            		.replace("%buyprice%", this.pl.formatDouble(BPrice*Amount))
            		.replace("%balance%", this.pl.formatDouble(Balance))
            		.replace("%displayname%", PLDispName)
            		.replace("%player%", PLName)
            		.replace("%itemname%", FriendlyName)));
        	itembuy.setItemMeta(itembuyMeta);
        	shopinv.setItem(buySlot, itembuy);
		}
		for (String s : SellSlots){
			int sellSlot = conf.getInt("SellItems." + s + ".Slot")-1;
			int Amount = conf.getInt("SellItems." + s +".Amount");
        	ItemStack itemsell = new ItemStack(Material.getMaterial(ID), Amount, DataTag);
        	ItemMeta itemsellMeta = itemsell.getItemMeta();
			List<String> itemsellLore = conf.getStringList("SellItems." + s + ".Lores");
			List<String> itemsellLoreStyle = conf.getStringList("PriceStyle");
			ArrayList<String> itemsellLore2 = Lists.newArrayList();
            for (String s2 : itemsellLore){
                String s3 = ChatColor.translateAlternateColorCodes('&', s2)
                		.replace("%sellprice%", this.pl.formatDouble(SPrice*Amount))
                		.replace("%buyprice%", this.pl.formatDouble(BPrice*Amount))
                		.replace("%balance%", this.pl.formatDouble(Balance))
                		.replace("%displayname%", PLDispName)
                		.replace("%player%", PLName)
                		.replace("%itemname%", FriendlyName);
                itemsellLore2.add(s3);
            }
            for (String s4 : itemsellLoreStyle){
            	if (SPrice == 0){
            		String PriceFormat = "§4You can't sell this kind of item.";
                    String s5 = ChatColor.translateAlternateColorCodes('&', s4)
                    		.replace("%price%", PriceFormat);
                    itemsellLore2.add(s5);
            	}else{
            		String PriceFormat = "Price: $" + this.pl.formatDouble(SPrice*Amount);
                    String s5 = ChatColor.translateAlternateColorCodes('&', s4)
                    		.replace("%price%", PriceFormat);
                    itemsellLore2.add(s5);
            	}
            }
			itemsellMeta.setLore(itemsellLore2);
			itemsellMeta.setDisplayName(Utils.ColorMsg(conf.getString("SellItems." + s + ".Name")
            		.replace("%sellprice%", this.pl.formatDouble(SPrice*Amount))
            		.replace("%buyprice%", this.pl.formatDouble(BPrice*Amount))
            		.replace("%balance%", this.pl.formatDouble(Balance))
            		.replace("%displayname%", PLDispName)
            		.replace("%player%", PLName)
            		.replace("%itemname%", FriendlyName)));
        	itemsell.setItemMeta(itemsellMeta);
        	shopinv.setItem(sellSlot, itemsell);
		}
		for (String disp : DispSlots){
			int dispSlot = conf.getInt("DispItems." + disp + ".Slot")-1;
			int Amount = conf.getInt("DispItems." + disp +".Amount");
			int Item = conf.getInt("DispItems." + disp +".ID");
			int ItemTag = conf.getInt("DispItems." + disp +".ItemTag");
			boolean PLSkull = conf.getBoolean("DispItems." + disp +".CustomSkull");
			ItemStack itemdisp = new ItemStack(Material.getMaterial(Item), Amount, (short)ItemTag);
			List<String> itemdispLore = conf.getStringList("DispItems." + disp + ".Lores");
			ArrayList<String> itemdispLore2 = Lists.newArrayList();
			for (String s2 : itemdispLore){
				String s3 = ChatColor.translateAlternateColorCodes('&', s2)
						.replace("%balance%", this.pl.formatDouble(Balance))
						.replace("%player%", PLName)
						.replace("%displayname%", PLDispName)
                		.replace("%itemname%", FriendlyName);
				itemdispLore2.add(s3);
	    	}
			if (Item == 397 && ItemTag == 3 && PLSkull == false){
				SkullMeta itemdispMeta = (SkullMeta) itemdisp.getItemMeta();
				itemdispMeta.setOwner(PLName);
				itemdispMeta.setDisplayName(Utils.ColorMsg(conf.getString("DispItems." + disp +".Name")
						.replace("%player%", PLName)
						.replace("%itemname%", FriendlyName)
						.replace("%displayname%", PLDispName)));
				itemdispMeta.setLore(itemdispLore2);
				itemdisp.setItemMeta(itemdispMeta);
				shopinv.setItem(dispSlot, itemdisp);
			}
			if (Item != 397){
				ItemMeta itemdispMeta =  itemdisp.getItemMeta();
				itemdispMeta.setDisplayName(Utils.ColorMsg(conf.getString("DispItems." + disp +".Name")
						.replace("%balance%", this.pl.formatDouble(Balance))
						.replace("%player%", PLName)
						.replace("%displayname%", PLDispName)));
				itemdispMeta.setLore(itemdispLore2);
				itemdisp.setItemMeta(itemdispMeta);
				shopinv.setItem(dispSlot, itemdisp);
			}
			if (Item == 397 && ItemTag == 3 && PLSkull == true){
				SkullMeta itemdispMeta = (SkullMeta) itemdisp.getItemMeta();
				itemdispMeta.setOwner(conf.getString("DispItems." + disp +".CustomSkullName").replace("%player%", PLName));
				itemdispMeta.setDisplayName(Utils.ColorMsg(conf.getString("DispItems." + disp +".Name")
						.replace("%balance%", this.pl.formatDouble(Balance))
						.replace("%player%", PLName)
						.replace("%displayname%", PLDispName)));
				itemdispMeta.setLore(itemdispLore2);
				itemdisp.setItemMeta(itemdispMeta);
				shopinv.setItem(dispSlot, itemdisp);
			}
		}
		event.getPlayer().openInventory(shopinv);
		return shopinv;
	}
}

package moleculesfx.GUIShop;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public static Main pl;
	public static Economy econ;
	public static File configf, itemsf;
    public static FileConfiguration config, items;

	public void onEnable(){
		if (!hookVault()){
		    getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", new Object[] { getDescription().getName() }));
		    getServer().getPluginManager().disablePlugin(this);
		    return;
		}
		getLogger().log(Level.INFO, "Found Vault!");
		getLogger().log(Level.INFO, "GUIShop Enabled!");
		RegisterEvents();
		createFiles();
		this.saveDefaultConfig();
		this.reloadConfig();
		if(getConfig().getBoolean("enable-update-checker") == true){
			 try {
			       HttpURLConnection c = (HttpURLConnection)new URL("http://www.spigotmc.org/api/general.php").openConnection();
			       c.setDoOutput(true);
			       c.setRequestMethod("POST");
			       c.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=23417").getBytes("UTF-8"));
			       String oldVersion = this.getDescription().getVersion();
			       String newVersion = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine().replaceAll("[a-zA-Z ]", "");
			       if(!newVersion.equals(oldVersion)) {
			    	   getLogger().log(Level.INFO, "A new update for GUIShop has been found.");
			       }else if(newVersion.equals(oldVersion)) {
			    	   getLogger().log(Level.INFO, "No new updates found for GUIShop.");
			       }
			     }
			     catch(Exception e) {
			     }
       }
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player p = (Player) sender;
		Block targetblock = p.getTargetBlock((Set<Material>)null, 5);
		Location blockloc = targetblock.getLocation();
		if (command.getName().equalsIgnoreCase("guishop")) {
			if (sender instanceof Player){
				if (args.length == 0){
					p.sendMessage("§bGUIShop Plugin by MoleculesFX.");
					p.sendMessage("§bPlease do: §e/guishop help - for more commands.");
				}else if (args.length == 1){
					if (args[0].equalsIgnoreCase("reload")){
						if (p.hasPermission("guishop.admin") || p.isOp()){
							p.sendMessage(Utils.ColorMsg(getConfig().getString("Prefix") + getConfig().getString("Messages.Reloaded")));
							this.reloadConfig();
						}
					}
					if (args[0].equalsIgnoreCase("help")){
						p.sendMessage("§a§l>>§b§m--§e§m--§b§m--§e§m--§b§m--§e§l[§b§lGUIShop Help§e§l]§b§m--§e§m--§b§m--§e§m--§b§m--§a§l<<");
						p.sendMessage("§b/guishop help - §eshows this dialog.");
						p.sendMessage("§b/guishop reload - §ereloads the config");
						p.sendMessage("§b/guishop <buy price> <sell price> - §ereplaces or creates a GUIShop sign. Can be applied to empty Signs.");
						p.sendMessage("§a§l>>§b§m--§e§m--§b§m--§e§m--§b§m--§e§l[§b§lGUIShop Help§e§l]§b§m--§e§m--§b§m--§e§m--§b§m--§a§l<<");
					}
				}else if (args.length == 2){
					if (Utils.isInt(args[0]) && Utils.isInt(args[1])){
						String BuyPrice = args[0];
						String SellPrice = args[1];
						double BPrice = Double.parseDouble(BuyPrice);
						double SPrice = Double.parseDouble(SellPrice);
						String Prefix = Utils.ColorMsg(getConfig().getString("Prefix"));
						ShopCreation.updateSigns(p, targetblock, blockloc, BPrice, SPrice, Prefix);
					}
				}
			}else if (!(sender instanceof Player)){
				getLogger().log(Level.INFO, "§4Players only!!");
			}
        }
		return false;
	}
	private boolean hookVault(){
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = (Economy)rsp.getProvider();
			return econ != null;
	}
	
	public void RegisterEvents(){
		regEvent(new InvClose(this));
		regEvent(new ShopCreation(this));
		regEvent(new SignListener(econ, this));
		regEvent(new SignProtection(this));
		regEvent(new InvBuyItems(econ, this));
		regEvent(new InvSellItems(econ, this));
	}
	
	public void regEvent(Listener listener){
		  getServer().getPluginManager().registerEvents(listener, this);
	}
	public String formatDouble(double value){
	  NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
	  return nf.format(value);
	}
    private void createFiles(){

        configf = new File(getDataFolder(), "config.yml");
        itemsf = new File(getDataFolder(), "items.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        if (!itemsf.exists()) {
            itemsf.getParentFile().mkdirs();
            saveResource("items.yml", false);
         }

        config = new YamlConfiguration();
        items = new YamlConfiguration();
        try {
            try {
				config.load(configf);
	            items.load(itemsf);
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


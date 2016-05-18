package moleculesfx.GUIShop;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public static Main pl;
	public static Plugin plugins = null;
	public static Economy econ = null;
	public ItemsConfig fileClass = new ItemsConfig(this);

	public void onEnable(){
		if (!hookVault()){
		    getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", new Object[] { getDescription().getName() }));
		    getServer().getPluginManager().disablePlugin(this);
		    return;
		}
		getLogger().log(Level.INFO, "Found Vault!");
		getLogger().log(Level.INFO, "GUIShop Enabled!");
		RegisterEvents();
		this.saveDefaultConfig();
		this.reloadConfig();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player p = (Player) sender;
		if (command.getName().equalsIgnoreCase("guishop")) {
			if (args.length == 0){
				p.sendMessage("§bGUIShop Plugin by MoleculesFX.");
				p.sendMessage("§bPlease do: §e/guishop reload to reload plugin.");
			}else if (args.length == 1){
				if (args[0].equalsIgnoreCase("reload")){
					if (p.hasPermission("guishop.admin") || p.isOp()){
						p.sendMessage(Utils.ColorMsg(getConfig().getString("Prefix") + getConfig().getString("Messages.Reloaded")));
						this.reloadConfig();
					}
				}
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
}


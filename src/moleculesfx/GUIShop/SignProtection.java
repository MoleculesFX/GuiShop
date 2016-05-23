package moleculesfx.GUIShop;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SignProtection implements Listener {
	
	private Main pl;
	
	public SignProtection(Main pl){
		this.pl = pl;
	}
	
	@EventHandler
	public void Protection(BlockBreakEvent evt){
		Player player = evt.getPlayer();
	    Sign sign = (Sign)evt.getBlock().getState();
		String Title = sign.getLine(0);
	    if ((evt.getBlock().getType().equals(Material.WALL_SIGN)) && Title.equalsIgnoreCase("§1§l[§2§lGUIShop§1§l]") || 
	    		(evt.getBlock().getType().equals(Material.SIGN_POST) && Title.equalsIgnoreCase("§1§l[§2§lGUIShop§1§l]"))){
	      if ((Title.equalsIgnoreCase("§1§l[§2§lGUIShop§1§l]")) && (!evt.getPlayer().hasPermission("guihop.remove")) || (!evt.getPlayer().isOp())){
	    	  player.sendMessage(Utils.ColorMsg(this.pl.getConfig().getString("Prefix") + this.pl.getConfig().getString("Messages.No-Permission")));
	          evt.setCancelled(true);
	        }
	    }
	}
}

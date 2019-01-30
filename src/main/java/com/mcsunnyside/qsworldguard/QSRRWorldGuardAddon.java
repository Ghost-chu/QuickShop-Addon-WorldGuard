package com.mcsunnyside.qsworldguard;


import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.Shop.ShopCreateEvent;
import org.maxgamer.quickshop.Shop.ShopPreCreateEvent;

import java.util.Map;

import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class QSRRWorldGuardAddon extends JavaPlugin implements Listener {
	QuickShop qs = null;
	WorldGuardPlugin worldGuard= null;
	StateFlag flag = null;
	@Override
	public void onLoad() {
		flag = Flags.register(new StateFlag("qs-shop-create", false));
		if(flag!=null)
			getLogger().info("Successfully registed WorldGuard Flag: qs-shop-create");
	}
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		qs = (QuickShop) Bukkit.getPluginManager().getPlugin("QuickShop");
		worldGuard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		//Verify plugin is reremake or reremake's forks
		try {
			QuickShop.getVersion();
		}catch (Exception e) {
			getLogger().severe("QSRR WorldGuard Addon only can support under QuickShop-Reremake by Ghost_chu, we can't promise it can working perfactly under other forks");
		}
		saveDefaultConfig();
		getLogger().info("Successfully loaded QSRR's WorldGuard addon.");
		
	}
	@Override
	public void onDisable() {
	}
	@EventHandler
	public void shopCreateEvent (ShopCreateEvent e) {
		 Map<String, ProtectedRegion> regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get((World) e.getShop().getLocation().getWorld()).getRegions();
		 for (ProtectedRegion region : regions.values()) {
			 if(region.contains(e.getShop().getLocation().getBlockX(), e.getShop().getLocation().getBlockY(), e.getShop().getLocation().getBlockZ())) {
				 if(region.getFlag(flag)!=State.ALLOW) {
					 e.setCancelled(true);
					 break;
				 }
			 }
		}
	}
	@EventHandler
	public void shopPreCreateEvent (ShopPreCreateEvent e) {
		 Map<String, ProtectedRegion> regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get((World) e.getLocation().getWorld()).getRegions();
		 for (ProtectedRegion region : regions.values()) {
			 if(region.contains(e.getLocation().getBlockX(), e.getLocation().getBlockY(), e.getLocation().getBlockZ())) {
				 if(region.getFlag(flag)!=State.ALLOW) {
					 e.setCancelled(true);
					 break;
				 }
			 }
		}
	}
}

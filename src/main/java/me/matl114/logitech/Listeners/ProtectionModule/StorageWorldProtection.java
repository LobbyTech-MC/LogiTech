package me.matl114.logitech.Listeners.ProtectionModule;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.ProtectionModule;
import me.matl114.logitech.MyAddon;
import me.matl114.logitech.SlimefunItem.Cargo.SpaceStorage.StorageSpace;

public class StorageWorldProtection implements ProtectionModule {
    Plugin plugin;
    @Override
	public Plugin getPlugin(){
        return this.plugin;
    }

    @Override
	public boolean hasPermission(OfflinePlayer var1, Location var2, Interaction var3){
        if(StorageSpace.ENABLED&& StorageSpace.STORAGE_WORLD==var2.getWorld()){
            return false;
        }else {
            return true;
        }
    }
    @Override
	public void load(){
        //mamba out
        this.plugin= MyAddon.getInstance();
    }
}

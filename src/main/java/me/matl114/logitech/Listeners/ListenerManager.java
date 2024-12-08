package me.matl114.logitech.Listeners;


import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import me.matl114.logitech.Unittest;
import me.matl114.logitech.Listeners.Listeners.BlockMenuClickProtectListener;
import me.matl114.logitech.Listeners.Listeners.BlockOpenListener;
import me.matl114.logitech.Listeners.Listeners.CraftingListener;
import me.matl114.logitech.Listeners.Listeners.LaserGunOnHeadListener;
import me.matl114.logitech.Listeners.Listeners.MilkListener;
import me.matl114.logitech.Listeners.Listeners.PlayerQuiteListener;
import me.matl114.logitech.Listeners.Listeners.PortalTeleport;
import me.matl114.logitech.Listeners.Listeners.PotionClearOnDeath;
import me.matl114.logitech.Listeners.Listeners.SlimefunBlockPlaceLimitListener;
import me.matl114.logitech.Listeners.Listeners.SpawnerListener;
import me.matl114.logitech.Listeners.Listeners.StorageWorldListener;
import me.matl114.logitech.Listeners.Listeners.SuperSpongeDryListener;
import me.matl114.logitech.SlimefunItem.Cargo.SpaceStorage.StorageSpace;
import me.matl114.logitech.Utils.Debug;

public class ListenerManager {
    public static void registerListeners(Plugin plugin,PluginManager manager){
        ListenerManager.plugin=plugin;
        ListenerManager.manager=manager;
       //register(testBlockBreakListener);
        register(EFFECT_CLEAR_MILK_LISTENER);
        register(MULTIBLOCK_REDIRECT);
        register(PORTAL_REDIRECT);
        register(POTION_CLEAR_DEATH);
        register(PLAYER_QUIT_HANDLER);
        if(Debug.isTest(Unittest.SFDATA_TEST)){
            register(CHUNK_LOAD_TEST);
        }
        register(VANILLACRAFT_ALLOW);
        register(ENTITYFEAT_LISTENER);
        register(LASER_ON_HEAD_LISTENER);
        register(SUPERSPONGE_DRY_LISTENER);
        if(StorageSpace.ENABLED){
            register(STORAGESPACE_PROTECTION);
        }
        register(BLOCKMENU_PROTECT_LISTENER);
        register(SLIMEFUN_BLOCKLIMIT_LISTENER);
    }
    public static Plugin plugin;
    public static PluginManager manager;
    public static void register(Listener listener){
        manager.registerEvents(listener,plugin);
    }
    public static final Listener testBlockBreakListener=new BlockBreakListener();
    public static final Listener EFFECT_CLEAR_MILK_LISTENER=new MilkListener();
    public static final Listener MULTIBLOCK_REDIRECT=new BlockOpenListener();
    public static final Listener PORTAL_REDIRECT=new PortalTeleport();
    public static final Listener POTION_CLEAR_DEATH=new PotionClearOnDeath();
    public static final Listener PLAYER_QUIT_HANDLER=new PlayerQuiteListener();
    public static final Listener CHUNK_LOAD_TEST=new ChunkTestListener();
    public static final Listener VANILLACRAFT_ALLOW=new CraftingListener();
    public static final Listener ENTITYFEAT_LISTENER=new SpawnerListener();
    public static final Listener LASER_ON_HEAD_LISTENER=new LaserGunOnHeadListener();
    public static final Listener SUPERSPONGE_DRY_LISTENER=new SuperSpongeDryListener();
    public static final Listener STORAGESPACE_PROTECTION=new StorageWorldListener();
    public static final Listener BLOCKMENU_PROTECT_LISTENER=new BlockMenuClickProtectListener();
    public static final Listener SLIMEFUN_BLOCKLIMIT_LISTENER=new SlimefunBlockPlaceLimitListener();
}

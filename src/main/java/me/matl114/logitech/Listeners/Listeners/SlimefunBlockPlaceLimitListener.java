package me.matl114.logitech.Listeners.Listeners;

import java.util.HashMap;
import java.util.function.Consumer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.matl114.logitech.MyAddon;

public class SlimefunBlockPlaceLimitListener implements Listener {
    static HashMap<SlimefunItem, Consumer<SlimefunBlockPlaceEvent>> eventHandlers = new HashMap<>();
    public static void registerBlockLimit(SlimefunItem item, Consumer<SlimefunBlockPlaceEvent> handler) {
        eventHandlers.put(item, handler);
    }
    @EventHandler
    public void onBlockPlace(SlimefunBlockPlaceEvent event) {
        SlimefunItem item=event.getSlimefunItem();
        if(item.getAddon()== MyAddon.getInstance()){
            if(eventHandlers.containsKey(item)){
                eventHandlers.get(item).accept(event);
            }
        }
    }
}

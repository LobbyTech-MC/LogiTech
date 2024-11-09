package me.matl114.logitech.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import me.matl114.logitech.Utils.Debug;

public class ChunkTestListener implements Listener {
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
       // throw new NotImplementedException("nm");
          Debug.logger("CHUNK LOAD: " + event.getChunk());
    }
}

package me.matl114.logitech.Listeners.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.matl114.logitech.Schedule.ScheduleEffects;

public class PotionClearOnDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        ScheduleEffects.clearEffectsOnDeath(e);
    }
}

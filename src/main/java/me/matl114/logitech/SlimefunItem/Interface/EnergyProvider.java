package me.matl114.logitech.SlimefunItem.Interface;

import javax.annotation.Nonnull;

import org.bukkit.Location;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;

public interface EnergyProvider extends EnergyNetProvider {
    default boolean willExplode(@Nonnull Location l, @Nonnull SlimefunBlockData data) {
        return false;
    }
}

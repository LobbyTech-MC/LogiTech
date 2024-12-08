package me.matl114.logitech.Utils.UtilClass.MenuClass;

import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

public interface GuideMenuHandler {
    public ChestMenu.MenuClickHandler getInstance(Player var1, PlayerProfile var2, SlimefunGuideMode var3);
}

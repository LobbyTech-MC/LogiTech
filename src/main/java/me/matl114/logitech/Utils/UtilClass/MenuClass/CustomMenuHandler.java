package me.matl114.logitech.Utils.UtilClass.MenuClass;

import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

public interface CustomMenuHandler {
    public static ChestMenu.MenuClickHandler EMPTY = ChestMenuUtils.getEmptyClickHandler();
    public static CustomMenuHandler empty() {
        return (cm -> EMPTY);
    }
    public static CustomMenuHandler from(ChestMenu.MenuClickHandler handler) {
        return (cm -> handler);
    }
    /**
     * change between cms ,在跳转中使用
     * @param cm
     * @return
     */
    public ChestMenu.MenuClickHandler getInstance(CustomMenu cm);
}

package me.matl114.logitech.Utils.UtilClass.MenuClass;

import org.bukkit.entity.Player;

public interface PlayerHistoryRecord<T extends Object> {
    void addRecord(Player player, T record);
    T deleteRecord(Player player,T record);
    T getRecord(Player player);
}

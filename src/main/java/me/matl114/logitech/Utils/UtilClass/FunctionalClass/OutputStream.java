package me.matl114.logitech.Utils.UtilClass.FunctionalClass;

import java.util.function.Supplier;

import org.bukkit.entity.Player;

import me.matl114.logitech.Utils.AddUtils;

public interface OutputStream {
    public static OutputStream getNullStream(){
        return (out)->{};
    }
    public static OutputStream getPlayerOut(Player player) {
        return (OutputStream) (out)->AddUtils.sendMessage(player,out.get());
    }
    public void out(Supplier<String> out);
}

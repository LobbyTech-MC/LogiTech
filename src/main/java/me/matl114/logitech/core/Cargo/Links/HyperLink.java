package me.matl114.logitech.core.Cargo.Links;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.matl114.logitech.core.Items.SpecialItems.HypLink;
import me.matl114.logitech.utils.AddUtils;
import me.matl114.logitech.utils.CraftUtils;
import me.matl114.logitech.utils.DataCache;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HyperLink {
    public final static String LOC_DISPLAY_PREFIX = AddUtils.resolveColor("&x&E&B&3&3&E&B链接的坐标: &f");
    public final static String LOC_DISPLAY=AddUtils.resolveColor("&3[%s,x=%d,y=%d,z=%d]");
    public final static Pattern LOC_DISPLAY_PATTERN= Pattern.compile(AddUtils.resolveColor("&3\\[.*?,x=.*?,y=.*?,z=.*?\\]"));
    public final static NamespacedKey KEY_LOC = AddUtils.getNameKey("link_location");
    public static boolean isLink(ItemMeta meta) {
        return meta.getPersistentDataContainer().has(KEY_LOC);
    }
    public static boolean canLink(ItemMeta meta) {
        String it=CraftUtils.parseSfId(meta);
        if(it==null) return false;
        return SlimefunItem.getById(it) instanceof HypLink;
    }
    public boolean canLink(SlimefunItem sfitem){
        return sfitem instanceof HypLink;
    }
    public static Location getLink(ItemMeta meta) {
        try{
            return DataCache.locationFromString(meta.getPersistentDataContainer().get(KEY_LOC,PersistentDataType.STRING));
        }catch(Throwable e){
            return null;
        }

    }
    public static void clearLink(ItemMeta meta) {
        meta.getPersistentDataContainer().remove(KEY_LOC);
        if(meta.hasLore()){
            List<String> lore = meta.getLore();
            for(int i=0;i<lore.size();){
                if(lore.get(i).startsWith(LOC_DISPLAY_PREFIX)){
                    lore.remove(i);
                }else if(LOC_DISPLAY_PATTERN.matcher(lore.get(i)).matches()){
                    lore.remove(i);
                }else{
                    i++;
                }
            }
            meta.setLore(lore);
        }
    }
    public static void setLink(ItemMeta meta, Location loc) {
        if(loc!=null) {
            String  locstr= DataCache.locationToString(loc);
            clearLink(meta);
            meta.getPersistentDataContainer().set(KEY_LOC, PersistentDataType.STRING,locstr);
            List<String> lore=meta.hasLore()? meta.getLore():new ArrayList<>();
            lore.add(LOC_DISPLAY_PREFIX);
            lore.add(LOC_DISPLAY.formatted(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            meta.setLore(lore);

        }
    }
    public static void setLinkLocation(ItemMeta meta, Location loc) {
        if(loc!=null) {
            String  locstr= DataCache.locationToString(loc);
            meta.getPersistentDataContainer().set(KEY_LOC, PersistentDataType.STRING,locstr);
        }
    }
    public static void updateLinkLocationDisplay(ItemMeta meta, Location loc) {
        List<String> lore =meta.hasLore()? meta.getLore():new ArrayList<>();
        if(lore.size()>0){
            lore.set(lore.size()-1,LOC_DISPLAY.formatted(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        }
        else {
            lore.add(LOC_DISPLAY.formatted(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        }
        meta.setLore(lore);
    }
}

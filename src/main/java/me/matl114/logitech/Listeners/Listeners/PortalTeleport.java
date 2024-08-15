package me.matl114.logitech.Listeners.Listeners;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.matl114.logitech.SlimefunItem.Blocks.MultiBlock.PortalCore;
import me.matl114.logitech.Utils.AddUtils;
import me.matl114.logitech.Utils.DataCache;
import me.matl114.logitech.Utils.Debug;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlock;
import me.matl114.logitech.Utils.UtilClass.MultiBlockClass.MultiBlockService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalTeleport implements Listener {
    public final int[] dx=new int[]{0,1,0,-1,0};
    public final int[] dz=new int[]{0,0,1,0,-1};
    @EventHandler
    //沉浸式体验传送门 abab
    public void onTeleportRedirect(PlayerPortalEvent event) {
        Location loco = event.getFrom();
        int x = loco.getBlockX();
        int y = loco.getBlockY()-1;
        int z = loco.getBlockZ();
        loco=new Location(loco.getWorld(),x,y,z);
        Location loc;
        for(int i=0;i<5;++i) {
            loc=loco.clone().add(dx[i],0,dz[i]);
            if(loc.getBlock().getType()!= Material.CRYING_OBSIDIAN){
                return;
            }
            SlimefunItem it = StorageCacheUtils.getSfItem(loc);
            if (it instanceof PortalCore) {
                Location dst = ((PortalCore) it).checkLink(loc);
                Player player = event.getPlayer();
                if (dst != null&&MultiBlockService.safeGetStatus(dst)!=0) {

                    player.setPortalCooldown(60);
                    player.teleport(dst.clone().add(0.5, 1, 0.5));
                    AddUtils.sendMessage(player, "&a传送成功!");

                } else {
                    AddUtils.sendMessage(player, "&c目标传送门未启用,传送失败!");
                }
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onTeleportRedirectEntity(EntityPortalEnterEvent event) {
       // Debug.logger("event called ");

        Entity player=event.getEntity();
        if(player==null) return;
        else if (event.getEntity() instanceof Player) {
            return;
        }
        else  if(event.getEntity().getPortalCooldown()>0) {
            return;
        }
        Location loco = event.getLocation();
        Location loc;
        loc=loco.clone().add(0,-1,0);
        if(loc.getBlock().getType()!= Material.CRYING_OBSIDIAN){
            return;
        }
        SlimefunItem it = StorageCacheUtils.getSfItem(loc);
        if (it instanceof PortalCore) {
            player.setPortalCooldown(180);
            Location dst = ((PortalCore) it).checkLink(loc);

          //  Debug.logger("info");

            if (dst != null&&MultiBlockService.safeGetStatus(dst)!=0) {
                for(int i=4;i>=0;--i) {
                    //尽可能丢到门外面 如果没地方就传送到门里面
                    Location loc2=(dst.clone().add(0.5+dx[i], 1, 0.5+dz[i]));
                    if(loc2.getBlock().getType()== Material.AIR||i==0){
                      //  Debug.logger("check loc ",i);
                        player.teleport(loc2);
                        break;
                    }
                }

            }

           // event.setCancelled(true);
            return;
        }

    }
}
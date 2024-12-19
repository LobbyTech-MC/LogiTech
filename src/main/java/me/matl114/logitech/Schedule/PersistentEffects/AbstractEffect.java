package me.matl114.logitech.Schedule.PersistentEffects;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.matl114.logitech.Schedule.ScheduleEffects;
import me.matl114.logitech.Utils.AddUtils;

public abstract class AbstractEffect {
    String id;
    public AbstractEffect(String name) {
        this.id = AddUtils.idDecorator(name);
    }
    public abstract void aquireEffect(Player p,int level);

    public String getEffectId() {
        return id;
    }

    public  void initEffect(Player p){

        removeEffect(p,1);
    }

    public boolean onDeathClear(){
        return true;
    }

    public void onDeathEvent(PlayerDeathEvent e,int level) {

    }
    public AbstractEffect reigster(){
        return ScheduleEffects.registerEffect(this);
    }

    public abstract void removeEffect(Player p,int level);
    public abstract void tickEffect(Player p,int level);

}

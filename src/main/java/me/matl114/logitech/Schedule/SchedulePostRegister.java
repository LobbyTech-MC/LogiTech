package me.matl114.logitech.Schedule;

import java.util.HashSet;
import java.util.LinkedHashSet;

import me.matl114.logitech.Utils.Debug;
import me.matl114.logitech.Utils.RecipeSupporter;

public class SchedulePostRegister {
    //在服务器启动之后执行的初始化
    public static boolean startPostRegister=false;
    public static boolean recipeSupportorInit=false;
    public static HashSet<Task> registerTasks=new LinkedHashSet<>();
    public static void addPostRegisterTask(Task t) {
        registerTasks.add(t);
    }
    public static void schedulePostRegister(){
        startPostRegister=true;
        Debug.logger("START ADDON POSTREGISTER TASKS");
        RecipeSupporter.init();
        for(Task t : registerTasks){
            t.run();
        }
    }
}

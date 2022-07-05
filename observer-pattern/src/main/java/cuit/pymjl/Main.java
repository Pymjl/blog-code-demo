package cuit.pymjl;

import cuit.pymjl.entity.DoorEvent;
import cuit.pymjl.entity.DoorListener;
import cuit.pymjl.entity.DoorManager;
import cuit.pymjl.entity.impl.DoorListener1;
import cuit.pymjl.entity.impl.DoorListener2;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/5 21:06
 **/
public class Main {
    public static void main(String[] args) {
        DoorManager manager = new DoorManager();
        manager.addListener(new DoorListener1());
        manager.addListener(new DoorListener2());
        manager.openDoor();
        System.out.println("----------------------------------------------------");
        manager.closeDoor();
    }
}

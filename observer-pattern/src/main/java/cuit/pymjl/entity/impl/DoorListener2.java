package cuit.pymjl.entity.impl;

import cuit.pymjl.entity.DoorEvent;
import cuit.pymjl.entity.DoorListener;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/5 20:57
 **/
public class DoorListener2 implements DoorListener {
    @Override
    public void doorEvent(DoorEvent event) {
        if (event.isOpen()) {
            System.out.println("Door2 is open");
        } else {
            System.out.println("Door2 is close");
        }
    }
}

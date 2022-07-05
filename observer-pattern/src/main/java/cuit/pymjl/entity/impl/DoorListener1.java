package cuit.pymjl.entity.impl;

import cuit.pymjl.entity.DoorEvent;
import cuit.pymjl.entity.DoorListener;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/5 20:56
 **/
public class DoorListener1 implements DoorListener {
    @Override
    public void doorEvent(DoorEvent event) {
        if (event.isOpen()) {
            System.out.println("Open door1 and turn on the light inside door1");
        } else {
            System.out.println("Close door1 and turn off the light inside door1");
        }
    }
}

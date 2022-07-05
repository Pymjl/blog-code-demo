package cuit.pymjl.entity;

import java.util.EventObject;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/5 20:51
 **/
public class DoorEvent extends EventObject {
    private boolean isOpen;

    public DoorEvent(Object source, boolean isOpen) {
        super(source);
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }


}

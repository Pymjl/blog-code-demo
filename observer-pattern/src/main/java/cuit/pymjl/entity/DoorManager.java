package cuit.pymjl.entity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/5 21:00
 **/
public class DoorManager {
    private Collection<DoorListener> listeners;

    public DoorManager(Collection<DoorListener> listeners) {
        this.listeners = listeners;
    }

    public DoorManager() {
        this.listeners = new ArrayList<>();
    }

    public void addListener(DoorListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(DoorListener listener) {
        this.listeners.remove(listener);
    }

    public void notifyListeners(DoorEvent event) {
        for (DoorListener listener : listeners) {
            listener.doorEvent(event);
        }
    }

    public void openDoor() {
        notifyListeners(new DoorEvent(this, true));
    }

    public void closeDoor() {
        notifyListeners(new DoorEvent(this, false));
    }


}

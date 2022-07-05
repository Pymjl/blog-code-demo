package cuit.pymjl.entity;

import java.util.EventListener;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/5 20:53
 **/
public interface DoorListener extends EventListener {
    /**
     * 开/关 门事件
     *
     * @param event 事件
     */
    void doorEvent(DoorEvent event);
}

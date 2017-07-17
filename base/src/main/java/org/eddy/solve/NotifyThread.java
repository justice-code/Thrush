package org.eddy.solve;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Justice-love on 2017/7/17.
 */
@Getter @Setter
public class NotifyThread extends Thread {

    private String subNotify;

    public NotifyThread(ThreadGroup group, Runnable target, String name, long stackSize, String subNotify) {
        super(group, target, name, stackSize);
        this.subNotify = subNotify;
    }
}

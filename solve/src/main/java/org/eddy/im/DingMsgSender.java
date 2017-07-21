package org.eddy.im;

/**
 * Created by Justice-love on 2017/7/21.
 */
public enum DingMsgSender {

    markdown {
        @Override
        public void sendMsg(String content) {
            super.sendMsg(content);
        }
    };

    public void sendMsg(String content) {
        throw new RuntimeException("not support");
    }
}

package org.eddy.im;

/**
 * Created by Justice-love on 2017/7/21.
 */
public enum DingMsgSender {

    markdown {
        @Override
        public void sendMsg(String content, String token) {
            ImHttpRequest.send(this, content, token);
        }
    };

    public void sendMsg(String content, String token) {
        throw new RuntimeException("not support");
    }
}

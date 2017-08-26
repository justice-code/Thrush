package org.eddy.pipeline.command;

import org.eddy.captcha.CaptchaUtil;
import org.eddy.config.DingConfig;
import org.eddy.config.HostConfig;
import org.eddy.http.HttpRequest;
import org.eddy.im.DingMsgSender;
import org.eddy.im.MarkDownUtil;
import org.eddy.pipeline.CoordinateUtil;
import org.eddy.pipeline.Pipeline;
import org.eddy.solve.Ticket;
import org.eddy.util.JsonUtil;
import org.eddy.util.OrderUtil;
import org.eddy.util.PassengerUtil;
import org.eddy.util.TicketUtil;
import org.eddy.web.TrainQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Justice-love on 2017/7/20.
 */
public enum Command {

    INIT {
        @Override
        public void execute(String pipeline, Object param) {
            HttpRequest.init();
            HttpRequest.auth();
            String url = CaptchaUtil.saveImage((String) param, HttpRequest.loginCaptchaImage());
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent("登录验证图片", url, pipeline), DingConfig.token);
        }
    },
    LOGIN {
        @Override
        public void execute(String pipeline, Object param) {
            HttpRequest.checkRandCode(CoordinateUtil.computeCoordinate((Integer[]) param));
            String result = HttpRequest.login(CoordinateUtil.computeCoordinate((Integer[]) param));
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(result, pipeline), DingConfig.token);
            String uamtkResult = HttpRequest.uamtk();
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(uamtkResult), DingConfig.token);
            String authClientResult = HttpRequest.authClient();
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(authClientResult, "获取火车票填写页面", HostConfig.domain + "/web/tranBegin", pipeline), DingConfig.token);
        }
    },
    REFRESH_LOGIN_CAPTCHA {
        @Override
        public void execute(String pipeline, Object param) {
            String url = CaptchaUtil.saveImage((String) param, HttpRequest.refreshLoginCaptchaImage());
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent("登录验证图片", url, pipeline), DingConfig.token);
        }
    },
    START_CHOOSE_TRAIN {
        @Override
        public void execute(String pipeline, Object param) {
            String passengers = String.join(",", PassengerUtil.parsePassengerName(HttpRequest.getPassengers()));
//            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(passengers), DingConfig.token);

            try {
                DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(passengers, "火车票填写", HostConfig.domain + "/web/train?passengers=" + URLEncoder.encode(passengers, "UTF-8"), pipeline), DingConfig.token);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    },
    TICKET_QUERY {
        @Override
        public void execute(String pipeline, Object param) {
            TrainQuery query = (TrainQuery) param;
            if ((query.getALong().get() | 1) == 1) {
                DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(query.toString()), DingConfig.token);
            }

            String ticket = HttpRequest.ticketQuery(query);
            List<Ticket> tickets = TicketUtil.genTickets(ticket);

            Ticket tr = TicketUtil.findTicket(tickets, query);
            if (null == tr) {
                try {
                    Thread.sleep(3_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                CommandNotify notify = new CommandNotify();
                notify.setPipeline(pipeline);
                notify.setArg(query);
                notify.setCommand(this);

                Pipeline.putNotify(notify);
                return;
            }


            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(tr.toString()), DingConfig.token);
            String result = HttpRequest.checkUser();
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(result), DingConfig.token);
            result = HttpRequest.submitOrderRequest(tr, query);
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(result), DingConfig.token);
            HttpRequest.initDc();
            result = HttpRequest.checkOrderInfo(query);
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(result), DingConfig.token);

            if (JsonUtil.needPassCode(result)) {
                DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent("亲，请输入验证码"), DingConfig.token);
            } else {
                DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent("无需验证购票, 继续下单流程"), DingConfig.token);
            }
        }
    },
    CONFIRM {
        @Override
        public void execute(String pipeline, Object param) {
            TrainQuery query = (TrainQuery) param;
            String ticket = HttpRequest.ticketQuery(query);
            List<Ticket> tickets = TicketUtil.genTickets(ticket);
            Ticket tr = TicketUtil.findTicket(tickets, query);

            String result = HttpRequest.getQueueCount(tr, query);
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(result), DingConfig.token);
            result = HttpRequest.confirmSingleForQueue(tr, query);
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(result), DingConfig.token);

            result = HttpRequest.queryOrderWaitTime();
            result = OrderUtil.findOrder(result);
            DingMsgSender.markdown.sendMsg(MarkDownUtil.createContent(result), DingConfig.token);
        }
    },
    STOP {
        @Override
        public void execute(String pipeline, Object param) {
            super.execute(pipeline, param);
        }
    };

    public void execute(String pipeline, Object param) {
        throw new RuntimeException("unsupported");
    }

    private static final Logger logger = LoggerFactory.getLogger(Command.class);
}

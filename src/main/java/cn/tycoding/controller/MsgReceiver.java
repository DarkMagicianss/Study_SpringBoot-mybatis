package cn.tycoding.controller;

import cn.tycoding.config.RabbitConfig;
import cn.tycoding.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MsgReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = RabbitConfig.QUEUE_A)
    public void processA(User user) {
        logger.info("接收处理队列A当中的消息： " + user.getUsername() + "   " + user.getPassword());
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_B)
    public void processB(User user) {
        logger.info("接收处理队列B当中的消息： " + user.getUsername() + "   " + user.getPassword());
    }

}
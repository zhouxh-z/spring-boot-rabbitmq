package com.zhouxh.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 17826
 */
@Component
public class Consumer {

    @RabbitListener(queues = "zhouxhQueue",ackMode = "MANUAL",containerFactory = "listenerContainer")
    public void consumer(Message ms, Channel channel) throws IOException {
        System.out.println(ms);
        // 手动签收
         //channel.basicAck(ms.getMessageProperties().getDeliveryTag(),true);
        // 拒收,第二个 Boolean 入参表示消息是否 返回 对列
        // 如果返回队列，存在一个问题 ，该消息将一直重新被消费,需要手动实现重试次数，比如保存到缓存中
        channel.basicNack(ms.getMessageProperties().getDeliveryTag(),true,true);
    }
}

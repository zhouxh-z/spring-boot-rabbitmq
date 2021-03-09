package com.zhouxh.rabbitmq.consumer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 17826
 */
@RestController
public class ProducerSender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    public String exchangeName;

    @Value("${spring.rabbitmq.template.routing-key}")
    public String routeName;

    @RequestMapping("/sendMSG")
    public void sendMSG(){
        rabbitTemplate.convertAndSend(exchangeName,routeName,"testMSG");
    }
}

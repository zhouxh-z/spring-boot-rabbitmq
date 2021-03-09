package com.zhouxh.rabbitmq.consumer;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author 17826
 * 配置类的作用：在rabbitMQ中创建队列，创建交换机，并绑定，创建时间：项目启动后第一次连接mq时
 * 如果手动创建 队列 交换机，并绑定 则不需要这个配置类
 */
@Configuration
public class ProducerConfig {

    @Autowired
    public BeanFactory beanFactory;

    @Value("${spring.rabbitmq.template.exchange}")
    public String exchangeName;

    @Value("${spring.rabbitmq.template.default-receive-queue}")
    public String queueName;

    @Value("${spring.rabbitmq.template.routing-key}")
    public String routeName;

    @Bean("exchange")
    public Exchange buildExchange(){
        // 普通交换机
        return ExchangeBuilder.topicExchange(exchangeName).build();
    }
    @Bean("dlExchange")
    public Exchange buildDlExchange(){
        // 死信交换机
        return ExchangeBuilder.topicExchange("del_exchange").build();
    }

    @Bean("queue")
    public Queue buildQueue(){
        // 普通队列,并绑定死信交换机 todo 这里的绑定不会声明，需要自己声明【buildDlExchange()】
        return QueueBuilder.durable(queueName).deadLetterExchange("del_exchange").build();
    }
    @Bean("dlQueue")
    public Queue buildDlQueue(){
        // 死信队列
        return QueueBuilder.durable(queueName).build();
    }
    @Bean("binder")
    public Binding buildBinder(){
        Queue queue = (Queue) beanFactory.getBean("queue");
        Exchange exchange = (Exchange) beanFactory.getBean("exchange");
        Queue dlQueue = (Queue) beanFactory.getBean("dlQueue");
        Exchange dlExchange = (Exchange) beanFactory.getBean("dlExchange");
        // 绑定死信交换机和死信队列
        BindingBuilder.bind(dlQueue).to(dlExchange).with("dl_#").noargs();
        return BindingBuilder.bind(queue)
                .to(exchange).with(routeName).noargs();
    }
}

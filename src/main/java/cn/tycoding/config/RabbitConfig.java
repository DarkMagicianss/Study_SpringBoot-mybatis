package cn.tycoding.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class RabbitConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    public static final String EXCHANGE = "my-mq-exchange";

    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";

    public static final String ROUTINGKEY_A = "a.*";
    public static final String ROUTINGKEY_B = "b.*";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange ：通过添加属性key-value匹配
     DirectExchange:按照routingkey分发到指定队列
     TopicExchange:多关键字匹配
     */
    //进行direct方式的消息队列的绑定
//    @Bean
//    public DirectExchange defaultExchange() {
//        return new DirectExchange(EXCHANGE);
//    }
    //进行topic方式的消息队列的绑定
    @Bean
    public TopicExchange defaultExchange(){
        return new TopicExchange(EXCHANGE);
    }

    //获取队列
    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A, true); //队列持久
    }

    //获取队列
    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B, true); //队列持久
    }

    //绑定队列 direct方式 routkey关键词
    @Bean
    public Binding bindingA() {
        return BindingBuilder.bind(queueA()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_A);
    }
    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(queueB()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_B);
    }

}

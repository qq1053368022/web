package com.xull.web.testRabbitMq2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @author: xull
 * @date: 2018-08-28 16:14
 */
public class NewTask {
    private static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        String[] messages = new String[]{
                "First message..",
                "Second message....",
                "Third message......",
                "Fourth message........"
        };
        for (String message:messages){
            //MessageProties.PERSISTENT_TEXT_PLAIN 申明消息为粘性
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("[x] Sent '"+message+"'");
        }
        channel.close();
        connection.close();
    }
}

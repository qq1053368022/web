package com.xull.web.testRabbitMQ4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @author: xull
 * @date: 2018-08-29 9:00
 */
public class Publisher {
    private static final String EXCHANGE_NAME = "log";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String selectKey = "error";
        String[] messages = new String[]{
                "First message",
                "Second message",
                "Third message",
                "Fourth message"
        };
        for (String message : messages) {
            for (int i = 0; i < 1000; i++) {
                channel.basicPublish(EXCHANGE_NAME, selectKey, null, message.getBytes());
                System.out.println("[x] Sent '"+message+"'");
            }

        }
        channel.close();
        connection.close();
    }
}

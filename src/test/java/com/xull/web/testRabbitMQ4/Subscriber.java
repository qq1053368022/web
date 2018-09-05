package com.xull.web.testRabbitMQ4;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @author: xull
 * @date: 2018-08-29 10:19
 */
public class Subscriber {
    private static final String EXCHANGE_NAME = "log";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        String bindKey = "error";
        channel.queueBind(queueName, EXCHANGE_NAME, bindKey);
        System.out.println("[*] Waiting for message ...");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[x] Received '"+message+"'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}

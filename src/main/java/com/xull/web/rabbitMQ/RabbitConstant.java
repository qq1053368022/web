package com.xull.web.rabbitMQ;

/**
 * @description:
 * @author: xull
 * @date: 2018-08-30 8:42
 */
public class RabbitConstant {
    //交换器名称
    public final static String EXCHANGE = "exchange_info";
    //队列
    public final static String QUEUE_TRANSACTION = "queue_transaction";
    public final static String QUEUE_CONTRACT = "queue_contract";
    public final static String QUEUE_QUALIFICATION = "queue_qualification";
    //路由Key
    public final static String RK_TRANSACTION = "transaction";
    public final static String RK_CONTRACT = "contract";
    public final static String RK_QUALIFICATION = "qualification";
}

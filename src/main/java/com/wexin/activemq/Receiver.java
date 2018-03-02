package com.wexin.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by Administrator on 2018/1/30.
 * mq 消息接收方（消费者）
 */
public class Receiver {

    public void testReceiver() throws Exception{
        //第一步：建立ConnectionFacotry工厂对象，需要输入用户名、密码、连接地址,使用默认  tcp协议、ip、port可以在activeMQ.xml文件查看
        ConnectionFactory factory = new ActiveMQConnectionFactory(
                "wqs","wqs",  /*添加了activemq的安全机制    在acrivemq.xml文件里添加*/ //Good Day-DNCE
                /*ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,*/"tcp://localhost:61616");

        //第二步：通过ConnectionFacotry工厂,创建一个Connection连接,并且调用Connection的start()开启连接,Connection默认是关闭的
        Connection connection = factory.createConnection();
        connection.start();

        //第三步：通过Connection对象创建session会话(上下文环境对象),用于接收消息,参数说明：1.是否开启事务 2.签收模式  一般我们设置自动签收
        Session session =connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);

        //第四步：通过session创建Destination对象,指的是一个客户端用来指定生产消息来源的对象,在PTP模式中,Destination被称作Queue即队列
        Destination destination = session.createQueue("firstQueue");

        //第五步：通过session来创建MessageConsumer来消费队列里的数据
        MessageConsumer messageConsumer = session.createConsumer(destination);

        while (true){
            //messageConsumer.receive() 参数说明
            //1.设置几秒后就不等待这个消息(不消费)   2.receiveNoWait()直接就不等待这个消息（直接不消费）
            TextMessage textMessage = (TextMessage)messageConsumer.receive();
            System.out.println("消费的数据："+textMessage.getText());
        }
    }

    public static void main(String[] args) throws Exception{
        Receiver receiver = new Receiver();
        receiver.testReceiver();
    }

}

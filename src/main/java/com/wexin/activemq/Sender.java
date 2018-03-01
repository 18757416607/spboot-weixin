package com.wexin.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Java jms 是一种规范  具体实现：ActiveMQ等其他MQ
 * Created by Administrator on 2018/1/29.
 * mq 消息发送方（生产者）
 */
public class Sender {

    public void TestSender() throws  Exception{
        //第一步：建立ConnectionFacotry工厂对象，需要输入用户名、密码、连接地址,使用默认  tcp协议、ip、port可以在activeMQ.xml文件查看
        ConnectionFactory factory = new ActiveMQConnectionFactory(
                "wqs","wqs",  /*添加了activemq的安全机制    在acrivemq.xml文件里添加*/
                /*ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,*/"tcp://localhost:61616");

        //第二步：通过ConnectionFacotry工厂,创建一个Connection连接,并且调用Connection的start()开启连接,Connection默认是关闭的
        Connection connection = factory.createConnection();
        connection.start();

        //第三步：通过Connection对象创建session会话(上下文环境对象),用于接收消息,参数说明：1.是否开启事务 2.签收模式  一般我们设置自动签收
        Session session =connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);

        //第四步：通过session创建Destination对象,指的是一个客户端用来指定生产消息来源的对象,在PTP模式中,Destination被称作Queue即队列
        Destination destination = session.createQueue("firstQueue");

        //第五步：通过session创建消息的发送对象和接收对象（生产者和消费者）,MessageProducer/MessageConsumer
        //此参数可以放一个Destination,意思是这个生产者创建出来的数据往哪个队列存放,设置为null比较灵活,在发送数据时再去决定往哪个队列存放
        MessageProducer messageProducer = session.createProducer(null);

        //第六步：我们可以使用MessageProducer的setDeliveryMode()为其设置持久化特性和非持久化特性（DeliveryMode）
        //messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        //第七步：我们使用JMS规范的TextMessage形式创建数据（通过session对象）,并用MessageProducer的send()发送数据,客户端用receive()接收数据（消费）

        for(int i = 0;i<10;i++){
            TextMessage textMessage = session.createTextMessage("我是队列");
            textMessage.setText(","+i);
            //MessageProducer.send() 参数说明
            //1.目标地址  2.具体的数据信息  3.传送数据的模式  4.优先级  5.消息的过期时间
            messageProducer.send(destination,textMessage);
            //Thread.sleep(1000);
        }

        //关闭Connection连接(必须释放Connection,不然连接多了内存会爆)
        if(connection!=null){
            connection.close();
        }
    }

    public static void main(String[] args) throws  Exception{
        Sender sender = new Sender();
        sender.TestSender();
    }

}

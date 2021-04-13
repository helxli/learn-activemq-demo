package cn.helx.learnactivemqdemo.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author helx
 * @description cn.helx.learnactivemqdemo.topic
 * @create 2021-04-12 20:56
 */
public class JmsProduce_Topic {
    public static final String ACTIVEMQ_URL = "tcp://192.168.3.37:61616";
    public static final String TOPIC_NAME = "myTopic-01";

    public static void main(String[] args) throws JMSException {
        //1 创建连接工场,使用默认用户名密码，编码不再体现
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        //2 获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3 创建会话,此步骤有两个参数，第一个是否以事务的方式提交，第二个默认的签收方式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建主题
        Topic topic = session.createTopic(TOPIC_NAME);

        //5.创建Topic生产者
        MessageProducer producer = session.createProducer(topic);

        for (int i = 1; i <= 6 ; i++) {
            TextMessage textMessage = session.createTextMessage("QueueMessage " + i);
            producer.send(textMessage);
        }

        //6.关闭资源
        producer.close();
        session.close();
        connection.close();

        System.out.println("消息已发送完成");

    }
}

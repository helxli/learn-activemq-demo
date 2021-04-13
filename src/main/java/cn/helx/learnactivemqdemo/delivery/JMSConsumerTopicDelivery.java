package cn.helx.learnactivemqdemo.delivery;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * @author helx
 * @description LearnActiveMQDemo.Queue 消费者 -- 同步
 * @create 2021-04-12 20:27
 */
public class JMSConsumerTopicDelivery {

    public static final String ACTIVEMQ_URL = "tcp://192.168.3.37:61616";
    public static final String TOPIC_NAME = "myTopic-01";

    public static void main(String[] args) throws JMSException, InterruptedException {
        //1 创建连接工场,使用默认用户名密码，编码不再体现
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        //2 获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        //2.1设置ID
        connection.setClientID("li4");

        //3 创建会话,此步骤有两个参数，第一个是否以事务的方式提交，第二个默认的签收方式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4 创建队列
        Topic topic = session.createTopic(TOPIC_NAME);

        //5.创建订阅者 指定订阅名称
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "topicSubscriber");

        //先创建消费者再启动
        connection.start();
        //6.订阅者异步获取消息
        Message message = topicSubscriber.receive();
        while (null != message){
            TextMessage textMessage = (TextMessage) message;
            System.out.println("消费者从MQ接受消息:"+textMessage.getText());
            message = topicSubscriber.receive(1000);
        }

        //7.关闭
        topicSubscriber.close();
        session.close();
        connection.close();

        System.out.println("接受消息完毕");
    }


}

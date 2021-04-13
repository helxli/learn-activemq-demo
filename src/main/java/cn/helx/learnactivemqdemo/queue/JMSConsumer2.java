package cn.helx.learnactivemqdemo.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * @author helx
 * @description LearnActiveMQDemo.Queue 消费者 -- 同步
 * @create 2021-04-12 20:27
 */
public class JMSConsumer2 {

    public static final String ACTIVEMQ_URL = "tcp://192.168.3.37:61616";
    public static final String QUEUE_NAME = "myQueue-01";

    public static void main(String[] args) throws JMSException, InterruptedException {
        //1 创建连接工场,使用默认用户名密码，编码不再体现
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        //2 获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3 创建会话,此步骤有两个参数，第一个是否以事务的方式提交，第二个默认的签收方式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4 创建队列
        Queue queue = session.createQueue(QUEUE_NAME);

        //5.创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
//        MessageConsumer consumer2 = session.createConsumer(queue);

        //6.异步获取消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println(message.getClass());
                if(message instanceof TextMessage){
                    try {
                        String text = ((TextMessage) message).getText();
                        System.out.println("消费者1从MQ接受消息:"+text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //6.异步获取消息
//        consumer2.setMessageListener(new MessageListener() {
//            @Override
//            public void onMessage(Message message) {
//                if(message instanceof TextMessage){
//                    try {
//                        String text = ((TextMessage) message).getText();
//                        System.out.println("消费者2从MQ接受消息:"+text);
//                    } catch (JMSException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        TimeUnit.HOURS.sleep(1);
        //7.关闭
        consumer.close();
        session.close();
        connection.close();

        System.out.println("接受消息完毕");
    }


}

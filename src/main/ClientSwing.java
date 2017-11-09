package main;

import subscriber.SubscriberImpl;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import publisher.Publisher;
import subscriber.Subscriber;
import topicmanager.TopicManager;
import topicmanager.TopicManagerStub;

public class ClientSwing {

    public Map<String,Subscriber> my_subscriptions;
    Publisher publisher;
    String publisherTopic;
    TopicManager topicManager;
    
    JFrame frame;
    JTextArea topic_list_TextArea;
    public JTextArea messages_TextArea;
    public JTextArea my_subscriptions_TextArea;
    JTextArea publisher_TextArea;
    JTextField argument_TextField;
    
    public ClientSwing(TopicManager topicManager) {
        my_subscriptions = new HashMap<String,Subscriber>();
        publisher = null;
        this.topicManager = topicManager;
    }
    public void createAndShowGUI() {

        String user = ((TopicManagerStub)topicManager).user;
        frame = new JFrame("Publisher/Subscriber demo, user : "+user);
        frame.setSize(300,300);
        frame.addWindowListener(new CloseWindowHandler());
        
        topic_list_TextArea = new JTextArea(5,10);
        messages_TextArea = new JTextArea(10,20);
        my_subscriptions_TextArea = new JTextArea(5,10);
        publisher_TextArea = new JTextArea(1,10);
        argument_TextField = new JTextField(20);

        JButton show_topics_button = new JButton("show Topics");
        JButton new_publisher_button = new JButton("new Publisher");
        JButton new_subscriber_button = new JButton("new Subscriber");
        JButton to_unsubscribe_button = new JButton("to unsubscribe");
        JButton to_post_an_event_button = new JButton("post an event");
        JButton to_close_the_app = new JButton("close app.");

        show_topics_button.addActionListener(new showTopicsHandler());
        new_publisher_button.addActionListener(new newPublisherHandler());
        new_subscriber_button.addActionListener(new newSubscriberHandler());
        to_unsubscribe_button.addActionListener(new UnsubscribeHandler());
        to_post_an_event_button.addActionListener(new postEventHandler());
        to_close_the_app.addActionListener(new CloseAppHandler());

        JPanel buttonsPannel = new JPanel(new FlowLayout());
        buttonsPannel.add(show_topics_button);
        buttonsPannel.add(new_publisher_button);
        buttonsPannel.add(new_subscriber_button);
        buttonsPannel.add(to_unsubscribe_button);
        buttonsPannel.add(to_post_an_event_button);
        buttonsPannel.add(to_close_the_app);

        JPanel argumentP = new JPanel(new FlowLayout());
        argumentP.add(new JLabel("Write content to set a new_publisher / new_subscriber / unsubscribe / post_event:"));
        argumentP.add(argument_TextField);

        JPanel topicsP = new JPanel();
        topicsP.setLayout(new BoxLayout(topicsP, BoxLayout.PAGE_AXIS));
        topicsP.add(new JLabel("Topics:"));
        topicsP.add(topic_list_TextArea);
        topicsP.add(new JScrollPane(topic_list_TextArea));
        topicsP.add(new JLabel("My Subscriptions:"));
        topicsP.add(my_subscriptions_TextArea);
        topicsP.add(new JScrollPane(my_subscriptions_TextArea));
        topicsP.add(new JLabel("I'm Publisher of topic:"));
        topicsP.add(publisher_TextArea);
        topicsP.add(new JScrollPane(publisher_TextArea));

        JPanel messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.PAGE_AXIS));
        messagesPanel.add(new JLabel("Messages:"));
        messagesPanel.add(messages_TextArea);
        messagesPanel.add(new JScrollPane(messages_TextArea));

        Container mainPanel = frame.getContentPane();
        mainPanel.add(buttonsPannel, BorderLayout.PAGE_START);
        mainPanel.add(messagesPanel,BorderLayout.CENTER);
        mainPanel.add(argumentP,BorderLayout.PAGE_END);
        mainPanel.add(topicsP,BorderLayout.LINE_START);

        frame.pack();
        frame.setVisible(true);
    }

    class showTopicsHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Set<String> it_topics = topicManager.topics();
             topic_list_TextArea.setText("");
             for (String top : it_topics){
                 topic_list_TextArea.append(top);
                 topic_list_TextArea.append("\n");
             }
        }
    }
    class newPublisherHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(argument_TextField.getText().length() == 0){
                messages_TextArea.append("CONSOLE: Specify a content" +  "\n");
                return;
            }
            else
            {
            publisherTopic = argument_TextField.getText();
            topicManager.removePublisherFromTopic(publisherTopic);
            publisher = topicManager.addPublisherToTopic(publisherTopic);
            publisher_TextArea.setText("");
            publisher_TextArea.append(publisherTopic);
            publisher_TextArea.append("\n");
            messages_TextArea.append("- You are publisher of " + publisherTopic + " topic." + "\n");
            }
        }
    }
    class newSubscriberHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(argument_TextField.getText().length() == 0){
                messages_TextArea.append("CONSOLE: Specify a content" +  "\n");
                return;
            }
            else
            {
                String new_subscription = argument_TextField.getText();
                Subscriber new_subcriber = new SubscriberImpl(ClientSwing.this);
                //1: Check if topic exists
                Boolean is_topic = topicManager.isTopic(new_subscription);
                //2: Check if I am already subscribed
                Boolean am_i_subscribed = my_subscriptions.containsKey(new_subscription);
                //If i am not subscribe and is a topic, just add
                if(is_topic && !am_i_subscribed){
                    //Add to client Swing
                    my_subscriptions.put(new_subscription, new_subcriber);
                    //Add to subscriber
                    topicManager.subscribe(new_subscription, new_subcriber);
                    my_subscriptions_TextArea.setText("");
                    for(String topics : my_subscriptions.keySet()){
                        my_subscriptions_TextArea.append(topics);
                        my_subscriptions_TextArea.append("\n");
                    }
                }            
            }
        }
    }
    class UnsubscribeHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(argument_TextField.getText().length() == 0){
                messages_TextArea.append("CONSOLE: Specify a content" +  "\n");
                return;
            }
            else
            {
                String new_subscription = argument_TextField.getText();
                Subscriber new_subscriber = my_subscriptions.get(new_subscription);
                //1: Check if topic exists
                Boolean is_topic = topicManager.isTopic(new_subscription);
                //2: Check if I am already subscribed
                Boolean am_i_subscribed = my_subscriptions.containsKey(new_subscription);
                if(is_topic && am_i_subscribed){
                    //Add to client Swing
                    my_subscriptions.remove(new_subscription);
                    //Add to subscriber
                    topicManager.unsubscribe(new_subscription, new_subscriber);
                    my_subscriptions_TextArea.setText("");
                    for(String topics : my_subscriptions.keySet()){
                        my_subscriptions_TextArea.append(topics);
                        my_subscriptions_TextArea.append("\n");
                    }
                }
            }
        }
    }
    class postEventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(argument_TextField.getText().length() == 0){
                messages_TextArea.append("CONSOLE: Specify a content" +  "\n");
                return;
            }
            else
            {
                String new_event = argument_TextField.getText();
                publisher.publish(publisherTopic, new_event);
            }
        }
    }
    class CloseAppHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!my_subscriptions.isEmpty()) {
                for (String s : my_subscriptions.keySet()) {
                    topicManager.unsubscribe(s, my_subscriptions.get(s));
                }
            }
            if(!publisherTopic.isEmpty() && publisherTopic != null){
                topicManager.removePublisherFromTopic(publisherTopic);
            }
            System.exit(0);
        }
    }
    class CloseWindowHandler implements WindowListener{
        public void windowDeactivated(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}
        public void windowOpened(WindowEvent e) {}
        public void windowClosing(WindowEvent e) {
            //...
            System.out.println("app closed");
            System.exit(0);
        }
    }
}


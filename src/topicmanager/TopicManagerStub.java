package topicmanager;

import apiREST.apiREST_TopicManager;
import java.util.Set;
import publisher.Publisher;
import publisher.PublisherStub;
import subscriber.Subscriber;
import webSocketService.WebSocketClient;

public class TopicManagerStub implements TopicManager {

  public String user;

  public TopicManagerStub(String user) {
    WebSocketClient.newInstance();
    this.user = user;
  }

  public void close() {
    WebSocketClient.close();
  }

  public Publisher addPublisherToTopic(String topic) {

    PublisherStub new_pubStup = new PublisherStub(topic);
    apiREST_TopicManager.addPublisherToTopic(topic);
    return new_pubStup;

  }

  public int removePublisherFromTopic(String topic) {
    return apiREST_TopicManager.removePublisherFromTopic(topic);
  }

  public boolean isTopic(String topic_name) {

    //...
    return apiREST_TopicManager.isTopic(topic_name);

  }

  public Set<String> topics() {

    //...
    return apiREST_TopicManager.topics();

  }

  public boolean subscribe(String topic, Subscriber subscriber) {
    WebSocketClient.addSubscriber(topic, subscriber);
    //...
    return true;

  }

  public boolean unsubscribe(String topic, Subscriber subscriber) {
    WebSocketClient.removeSubscriber(topic);
    //...
    return true;

  }

}

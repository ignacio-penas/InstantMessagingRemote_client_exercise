package publisher;

import apiREST.apiREST_Publisher;
import util.MyEvent;

public class PublisherStub implements Publisher {

  String topic;

  public PublisherStub(String topic) {
    this.topic = topic;
  }

  public void publish(String topic, String event) {
    
    MyEvent new_event = new MyEvent();
    new_event.content = event;
    new_event.topic = topic;
    apiREST_Publisher.publish(new_event);
  }

}

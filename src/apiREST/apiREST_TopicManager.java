package apiREST;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.MyBoolean;
import util.MyInteger;
import util.MyString;

public class apiREST_TopicManager {
  public static void addPublisherToTopic(String topic) {
    try {
      URL url = new URL(Cons.SERVER_REST + "/topicmanager/addtopic");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      Gson gson = new Gson();
      MyString the_topic = new MyString();
      the_topic.content = topic;
      String json = gson.toJson(the_topic);
      System.out.println(json);
      out.println(json);
      out.flush();
      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static int removePublisherFromTopic(String topic) {
    try {
      URL url = new URL(Cons.SERVER_REST + "/topicmanager/removetopic");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      Gson gson = new Gson();
      MyString the_topic = new MyString();
      the_topic.content = topic;
      String json = gson.toJson(the_topic);
      System.out.println(json);
      out.println(json);
      out.flush();
      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      gson = new Gson();
      MyInteger myInteger = gson.fromJson(in, MyInteger.class);
      return myInteger.content;

    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }
  public static boolean isTopic(String topic) {
    try {
      URL url = new URL(Cons.SERVER_REST + "/topicmanager/istopic");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      Gson gson = new Gson();
      MyString the_topic = new MyString();
      the_topic.content = topic;
      String json = gson.toJson(the_topic);
      System.out.println(json);
      out.println(json);
      out.flush();
      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      gson = new Gson();
      MyBoolean myBoolean = gson.fromJson(in, MyBoolean.class);
      return myBoolean.content == true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  public static Set<String> topics() {
    try {
      URL url = new URL(Cons.SERVER_REST + "/topicmanager/topics");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

      ucon.setRequestMethod("GET");
      ucon.setDoInput(true);
      ucon.setRequestProperty("Accept", "application/json");
      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      Gson gson = new Gson();
      MyString[] reply = gson.fromJson(in, MyString[].class);
      List<MyString> topics = Arrays.asList(reply);
      Set<String> result = new HashSet<String>();
      for (MyString myString : topics) {
        result.add(myString.content);
      }
      return result;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}

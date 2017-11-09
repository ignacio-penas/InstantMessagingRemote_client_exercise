package apiREST;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import util.MyEvent;

public class apiREST_Publisher {
  public static void publish(MyEvent myEvent) {
    try {
      URL url = new URL(Cons.SERVER_REST + "/publisher/publish");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      Gson gson = new Gson();
      String json = gson.toJson(myEvent);
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
}

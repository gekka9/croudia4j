package croudia4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Croudia {
  private String key;
  private String secret;
  private AccessToken token;
  public static final String OAUTHURL = "https://api.croudia.com/oauth/authorize?response_type=code&client_id=";
  public Croudia(String key, String secret){
    this.key=key;
    this.secret=secret;
  }
  public String getOAuthRequestURL(){
    return OAUTHURL+key;
  }
  public String getSecret() {
    return this.secret;
  }
  public String getKey() {
    return this.key;
  }
  public void setAccesstoken(AccessToken token){
    this.token=token;
  }
  public void update(String status){
    try {
      status = URLEncoder.encode(status,"utf-8");
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //ささやき
    String urlStr = "https://api.croudia.com/statuses/update.json?status="+status;
    URL url=null;
    try {
      url = new URL(urlStr);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    HttpURLConnection connection;
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Authorization","Bearer " + this.token.getAccessToken());

      connection.connect();

      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

      String response = "";
      String line="";
      while ((line = reader.readLine()) != null) {
        response += line + "\n";
      }
      reader.close();
    }catch (IOException e){
      e.printStackTrace();
    }
  }
}

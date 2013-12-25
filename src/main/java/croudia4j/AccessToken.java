package croudia4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class AccessToken {
  private String accessToken;
  private String tokenType;
  private int expiresIn;
  private String refreshToken;
  private static final String TOKEN_URL = "http://api.croudia.com/oauth/token";
  private Configuration conf;
  public AccessToken(Map<String,Object> map,Configuration conf){
    this.accessToken=(String) map.get("access_token");
    this.tokenType=(String) map.get("token_type");
    this.expiresIn=(int) map.get("expires_in");
    this.refreshToken=(String) map.get("refresh_token");
    this.conf=conf;
  }
  public String getAccessToken() {
    return this.accessToken;
  }
  public String getTokenType() {
    return this.tokenType;
  }
  public int getExpiresIn() {
    return this.expiresIn;
  }
  public String getRefreshToken() {
    return this.refreshToken;
  }
  public void reflesh(){
    //リクエストのためのマップ
    SortedMap<String, String> params = new TreeMap<>();
    params.put("grant_type", "refresh_token");
    params.put("client_id", this.conf.getKey());
    params.put("client_secret", this.conf.getSecret());
    params.put("refresh_token", this.refreshToken);
    
    //URL生成のためのパラメータ部分の生成
    String parameter = "";
    for (Entry<String, String> param : params.entrySet()) {
        parameter += param.getKey() + "=" + param.getValue() + "&";
    }
    //最後の余計な&を消す
    parameter = parameter.substring(0, parameter.length() - 1);
    //アクセストークンの取得
    URL url=null;
    try {
      url = new URL(TOKEN_URL  + "?" + parameter);
    } catch (MalformedURLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    HttpURLConnection connection;
    String response = "";
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.connect();
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        response += line + "\n";
      }
      reader.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = null;
    try {
      map = mapper.readValue(response, Map.class);
      this.accessToken=(String) map.get("access_token");
      this.tokenType=(String) map.get("token_type");
      this.expiresIn=(int) map.get("expires_in");
      this.refreshToken=(String) map.get("refresh_token");
    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}

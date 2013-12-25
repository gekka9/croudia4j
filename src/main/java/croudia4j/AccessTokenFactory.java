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

public class AccessTokenFactory {
  private String code;
  private Configuration conf;
  private static final String TOKEN_URL = "http://api.croudia.com/oauth/token";
  public AccessTokenFactory(Configuration conf){
    this.conf=conf;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public AccessToken getInstance(){
    //リクエストのためのマップ
    SortedMap<String, String> params = new TreeMap<>();
    params.put("grant_type", "authorization_code");
    params.put("client_id", this.conf.getKey());
    params.put("client_secret", this.conf.getSecret());
    params.put("code", code);
    
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
    return new AccessToken(map,this.conf);
  }
}

package croudia4j;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;


public class SasayakiTest {
    public static final String KEY = "2f148498ce623f32e6e0a8789069c2425d143d268d87088a4e4b7bef7ad43c42";
    public static final String SECRET = "0436798fd793ab73b400f7d5ffefce319d0aab7f2a58e97022807f82c6d924ef";
    public static final String OAUTHURI = "https://api.croudia.com/oauth/authorize?response_type=code&client_id=";
    
    public static void main(String[] args) throws IOException {
        String tokenUrl = "http://api.croudia.com/oauth/token";
        String consumerkey = KEY;
        String consumersecret = SECRET;
        
        //認証
        Desktop desktop = Desktop.getDesktop();
        try {
          desktop.browse(new URI(OAUTHURI+KEY));
        } catch (URISyntaxException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        System.out.println("codeを入力してください");
        System.out.print(">>");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String code = input.readLine();
        System.out.println("code=" + code);

        //リクエストのためのマップ
        SortedMap<String, String> params = new TreeMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", consumerkey);
        params.put("client_secret", consumersecret);
        params.put("code", code);
        
        //URL生成のためのパラメータ部分の生成
        String parameter = "";
        for (Entry<String, String> param : params.entrySet()) {
            parameter += param.getKey() + "=" + param.getValue() + "&";
        }
        //最後の余計な&を消す
        parameter = parameter.substring(0, parameter.length() - 1);

        //アクセストークンの取得
        URL url = new URL(tokenUrl + "?" + parameter);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        String response = "";
        while ((line = reader.readLine()) != null) {
            response += line + "\n";
        }
        reader.close();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(response, Map.class);

        System.out.println("ささやき");
        System.out.print(">>");
        String sasayaki = input.readLine();   
        sasayaki = URLEncoder.encode(sasayaki,"utf-8");
        //ささやき
        String urlStr = "https://api.croudia.com/statuses/update.json?status="+sasayaki;
        url = new URL(urlStr);
        System.out.println(url.toString());
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization","Bearer " + map.get("access_token"));
    
        connection.connect();

        reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        response = "";
        while ((line = reader.readLine()) != null) {
            response += line + "\n";
        }
        reader.close();
        System.out.println(response);
    }
}
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
        String consumerkey = KEY;
        String consumersecret = SECRET;
        Configuration conf = new Configuration();
        conf.setKey(KEY);
        conf.setSecret(SECRET);
        CroudiaFactory factory = new CroudiaFactory(conf);
        Croudia croudia = factory.getInstance();
        String requestURL = croudia.getOAuthRequestURL();
        //認証
        Desktop desktop = Desktop.getDesktop();
        try {
          desktop.browse(new URI(requestURL));
        } catch (URISyntaxException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        System.out.println("codeを入力してください");
        System.out.print(">>");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String code = input.readLine();

        AccessTokenFactory tokenFactory = new AccessTokenFactory(conf);
        tokenFactory.setCode(code);
        AccessToken token =tokenFactory.getInstance();
        croudia.setAccesstoken(token);
       
        System.out.println("ささやき");
        System.out.print(">>");
        String sasayaki = input.readLine();   
        croudia.update(sasayaki);
    }
}
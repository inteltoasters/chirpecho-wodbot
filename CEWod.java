/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import com.json.parsers.JsonParserFactory;
import com.json.parsers.JSONParser;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author vlad
 */

public class CEWod {

	public static int i = 60 * 60 * 24;
        
        public static boolean allow = true;
        
        public static String TOKEN = "<REPLACE WITH VALID CHIRPECHO USER COOKIE>";

	public static void doTheShit() {
		// Get a random word
		// https://random-words-api.kushcreates.com/api?language=en&words=1
		URL url = null;
		try {
			url = new URL("https://random-words-api.kushcreates.com/api?language=en&words=1");
		} catch (MalformedURLException ex) {
			System.getLogger(CEWod.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
		}

		URLConnection con = null;
		try {
			con = url.openConnection();
		} catch (IOException ex) {
			System.getLogger(CEWod.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
		}

		HttpURLConnection http = (HttpURLConnection) con;

		try {
			http.setRequestMethod("GET");
		} catch (ProtocolException ex) {
			System.getLogger(CEWod.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
		}

		http.setDoOutput(true);

		String output = null;
		try {
			output = new String(http.getInputStream().readAllBytes(), "UTF-8");
		} catch (IOException ex) {
			System.getLogger(CEWod.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
		}

		System.out.println(output);

		JsonParserFactory factory = JsonParserFactory.getInstance();
		JSONParser parser = factory.newJsonParser();
		Map jsonMap = parser.parseJson(output);

		List < Map<String, Object>> rootList = (List < Map<String, Object>> ) jsonMap.get("root");
		Map<String, Object> firstMap = rootList.get(0);
		String word = (String) firstMap.get("word");

		System.out.println(word);

		// Post the post
		URL url2 = null;
		try {
			url2 = new URL("https://chirpecho.fun/api/v1/user/post.php");
		} catch (MalformedURLException ex) {
			System.getLogger(CEWod.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
		}

		URLConnection con2 = null;
		try {
			con2 = url2.openConnection();
		} catch (IOException ex) {
			System.getLogger(CEWod.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
		}

		HttpURLConnection http2 = (HttpURLConnection) con2;

		byte[] out = "content=".concat(word).getBytes(StandardCharsets.UTF_8);
		int length = out.length;

		http2.setFixedLengthStreamingMode(length);

		try {
			http2.setRequestMethod("POST"); // PUT is another valid option
		} catch (ProtocolException ex) {
			System.getLogger(CEWod.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
		}

		http2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		http2.setRequestProperty("Cookie", "user=" + TOKEN);
		http2.setDoOutput(true);

		try (OutputStream os = http2.getOutputStream()) {
			os.write(out);
		} catch (IOException ex) {
			System.getLogger(CEWod.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
		}

		try {
			System.out.println(new String(http2.getInputStream().readAllBytes(), "UTF-8"));
		} catch (IOException ex) {
			System.getLogger(CEWod.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
		}
	}

	/**
	 * @param args the command line arguments
	 */

	public static void main(String[] args) {
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override

			public void run() {
                                Calendar c = Calendar.getInstance();
                            
				if (c.get(Calendar.HOUR) == 12 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 1 && allow == true) {
                                    doTheShit();
                                    allow = false;
                                }
                                
                                if (c.get(Calendar.HOUR) == 12 && c.get(Calendar.MINUTE) == 1 && allow == false) {
                                    allow = true;
                                }
                                
                                System.out.println(c.get(Calendar.HOUR) + " : " + c.get(Calendar.MINUTE) + " : " + c.get(Calendar.SECOND));
			}
		}, 0, 1 * 100);
	}

}

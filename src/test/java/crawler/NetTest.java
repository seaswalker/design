package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetTest {

	public static void main(String[] args) throws MalformedURLException, IOException {
		URLConnection connection = new URL("http://career.sdust.edu.cn/servlet/IndexServlet").openConnection();
		//如果没有这一句，百度返回的是JavaScript代码
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}
	
}
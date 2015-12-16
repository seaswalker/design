package crawler;

import java.io.IOException;
import java.util.Map;

import model.Token;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import dao.TokenDao;

public class JsoupDemo {
	
	@Test
	public void parseDetail() throws IOException {
		Document doc = Jsoup.connect("http://career.sdust.edu.cn/servlet/RecruitmentServlet?flag=content&ID=4300").get();
		//共有四个div: 第一个是公司名称
		//第二个中含有点击量
		//第三个中单位所在地
		//第四个即为正文
		Elements divs = doc.select("div[class=content_bottom] div");
		parseTokens(divs.get(3).text());
	}
	
	private static void parseTokens(String source) {
		TokenDao dao = TokenDao.getInstance();
		Map<String, Token> map = dao.getTokenMap();
		for (String key : map.keySet()) {
			if (source.indexOf(key) > -1) {
				System.out.println("捕获: " + key);
			}
		}
	}
	
}

package crawler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Address;
import model.City;
import model.Province;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class JsoupDemo {
	
	protected static final Pattern DETAIL = Pattern.compile("^http://career\\.sdust\\.edu\\.cn/servlet/RecruitmentServlet\\?((ID=([0-9]+)&flag=content)|(flag=content&ID=([0-9]+)))$");
	protected static final Pattern DETAIL_2 = Pattern.compile("^http://career\\.sdkd\\.net\\.cn/servlet/RecruitmentServlet\\?((ID=([0-9]+)&flag=content)|(flag=content&ID=([0-9]+)))$");
	
	@Test
	public void parseDigest() throws IOException {
		Document doc = Jsoup.connect("http://career.sdust.edu.cn/servlet/CareerFairServlet?flag=content&CFID=687").get();
		Elements tds = doc.select("#body_1 table").get(1).select("td");
		System.out.println("招聘时间: " + tds.get(3).text());
		System.out.println("招聘地点: " + tds.get(5).text());
		System.out.println("公司名称: " + tds.get(7).text());
	}
	
	@Test
	public void parseDetail() throws IOException {
		Document doc = Jsoup.connect("http://career.sdust.edu.cn/servlet/RecruitmentServlet?ID=3835&flag=content").get();
		//共有四个div: 第一个是公司名称
		//第二个中含有点击量
		//第三个中单位所在地
		//第四个即为正文
		Elements divs = doc.select("div[class=content_bottom] div");
		String str = divs.get(2).text();
		//System.out.println(extractName(str));
	}
	
	protected static int extractDetailID(String url) {
		Matcher matcher = DETAIL.matcher(url);
		if (matcher.matches()) {
			return extractDetailID(matcher);
		} 
		matcher = DETAIL_2.matcher(url);
		if (matcher.matches()) {
			return extractDetailID(matcher);
		}
		return -1;
	}
	
	protected static int extractDetailID(Matcher matcher) {
		String str = matcher.group(3);
		if (str == null) {
			str = matcher.group(5);
		}
		return Integer.parseInt(str);
	}
	
	public static void main(String[] args) {
		System.out.println(extractDetailID("http://career.sdkd.net.cn/servlet/RecruitmentServlet?flag=content&ID=3887"));
		System.out.println(extractDetailID("http://career.sdust.edu.cn/servlet/RecruitmentServlet?ID=3835&flag=content"));
	}
	
}

package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 封装一条招聘会/信息记录
 * @author skywalker
 *
 */
public class Recruitment {

	private int id;
	private RecruitmentType recruitmentType;
	private String name;
	//点击量
	private int clickCount;
	//单位所在地
	private int provinceId;
	private int cityId;
	//此单位在学校就业网的地址
	private String url;
	//含有的关键词的id
	private List<Integer> tokens = new ArrayList<Integer>();
	
	//招聘会特有
	private Date time;
	//宣讲教室-id
	private int building;
	
	//修改次数
	private int modify;
	
	@Override
	public String toString() {
		return "Recruitment [id=" + id + ", recruitmentType=" + recruitmentType
				+ ", name=" + name + ", clickCount=" + clickCount
				+ ", provinceId=" + provinceId + ", cityId=" + cityId
				+ ", url=" + url + ", tokens=" + tokens + ", time=" + time
				+ ", building=" + building + "]";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public RecruitmentType getRecruitmentType() {
		return recruitmentType;
	}
	public void setRecruitmentType(RecruitmentType recruitmentType) {
		this.recruitmentType = recruitmentType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getBuilding() {
		return building;
	}
	public void setBuilding(int building) {
		this.building = building;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<Integer> getTokens() {
		return tokens;
	}
	public void setTokens(List<Integer> tokens) {
		this.tokens = tokens;
	}

	/**
	 * 增加一个token id
	 * @param id
	 */
	public void addToken(int id) {
		this.tokens.add(id);
	}
	
	/**
	 * 修改次数加一
	 * @return 返回现在的次数
	 */
	public int addModify() {
		return (++modify);
	}
	
}

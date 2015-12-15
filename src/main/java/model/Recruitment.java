package model;

import java.util.Date;

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
	private Address address;
	//薪资水平
	private int salary;
	
	//招聘会特有
	private Date time;
	//宣讲教室
	private String room;
	
	//修改次数
	private int modify;
	
	@Override
	public String toString() {
		return "Recruitment [id=" + id + ", recruitmentType=" + recruitmentType
				+ ", name=" + name + ", clickCount=" + clickCount
				+ ", address=" + address + ", salary=" + salary + ", time="
				+ time + ", room=" + room + "]";
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
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	/**
	 * 修改次数加一
	 * @return 返回现在的次数
	 */
	public int addModify() {
		return (++modify);
	}
	
}

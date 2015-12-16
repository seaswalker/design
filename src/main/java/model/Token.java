package model;

/**
 * 关键词
 * @author skywalker
 *
 */
public class Token {

	private int id;
	private String name;
	//出现次数
	private int count;
	
	@Override
	public String toString() {
		return "Token [id=" + id + ", name=" + name + ", count=" + count + "]";
	}
	
	public void addCount() {
		this.count++;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}

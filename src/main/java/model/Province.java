package model;

/**
 * 省份
 * 北京、上海、重庆、天津作为省处理
 * @author skywalker
 *
 */
public class Province {

	private int id;
	private String name;
	
	public Province(String name) {
		this.name = name;
	}
	
	public Province() {}
	
	@Override
	public String toString() {
		return "Province [name=" + name + "]";
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
	
}

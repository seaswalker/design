package model;

/**
 * 市， 四个直辖市不在此列
 * @author skywalker
 *
 */
public class City {

	private int id;
	private String name;
	
	public City(String name) {
		this.name = name;
	}
	
	public City() {}
	
	@Override
	public String toString() {
		return "City [name=" + name + "]";
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

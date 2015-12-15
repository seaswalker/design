package model;

/**
 * 封装单位所在地: 省 + 市
 * @author skywalker
 *
 */
public class Address {

	private Province province;
	private City city;
	
	public Address(Province province, City city) {
		this.province = province;
		this.city = city;
	}
	
	public Address() {}
	
	@Override
	public String toString() {
		return "Address [province=" + province + ", city=" + city + "]";
	}

	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	
}

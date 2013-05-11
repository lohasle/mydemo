package demo.spring.di.vo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.org.apache.bcel.internal.generic.RETURN;

/**
 * constructor-arg 测试
 * 
 * @author fule
 * 
 */
public class Diaosi {

	private String name;
	private int age;
	private List hobby;
	private Map friends;
	private Set set;
	private boolean ifMarried;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List getHobby() {
		return hobby;
	}

	public void setHobby(List hobby) {
		this.hobby = hobby;
	}

	public Map getFriends() {
		return friends;
	}

	public void setFriends(Map friends) {
		this.friends = friends;
	}

	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}

	public boolean isIfMarried() {
		return ifMarried;
	}

	public void setIfMarried(boolean ifMarried) {
		this.ifMarried = ifMarried;
	}

	public Diaosi() {

	}

	public Diaosi(String name) {
		super();
		this.name = name;
	}

	public Diaosi(String name, int age, List hobby, Map friends, Set set,
			boolean ifMarried) {
		this.name = name;
		this.age = age;
		this.hobby = hobby;
		this.friends = friends;
		this.set = set;
		this.ifMarried = ifMarried;
	}

	public String getInfo() {

		String info = "姓名：" + this.name + "\n年龄:" + this.age + "\n爱好:"
				+ this.hobby + "\n朋友:" + this.friends + "\n婚否:"
				+ this.ifMarried + "\n其他的：" + this.set;
		return info;
	}

	/** 可以通过静态工厂方法实例化bean **/
	public static Diaosi newIntance(String name, int age, List hobby,
			Map friends, Set set, boolean ifMarried) {
		return new Diaosi(name, age, hobby, friends, set, ifMarried);
	}

	/** 实例工厂方法实例化bean **/
	public Diaosi newIntance(String name) {
		return new Diaosi(name);
	}
	
	
	public static void ss(){
		System.out.println("34444444444");
	}
	
	public void init(){
		ss();
		System.out.println(Diaosi.class.getName()+"---------------初始化");
	}

	public void destroy(){
		System.out.println(Diaosi.class.getName()+"---------------销毁");
	}
}

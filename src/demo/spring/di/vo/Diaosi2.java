package demo.spring.di.vo;

public class Diaosi2 {
	private Diaosi diaosi;

	public Diaosi getDiaosi() {
		return diaosi;
	}

	public void setDiaosi(Diaosi diaosi) {
		this.diaosi = diaosi;
	}
	
	
	public void init(){
		System.out.println(Diaosi2.class.getName()+"---------------初始化");
	}

	public void destroy(){
		System.out.println(Diaosi2.class.getName()+"---------------销毁");
	}

}

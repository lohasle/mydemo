package demo.spring.di.svr.ac;

import demo.spring.di.svr.TestCh01;


public class DiaoSiAc {
	private TestCh01 ds;

	private int sss;
	
	public void init(){
		System.out.println(DiaoSiAc.class.getName()+"初始化");
	}
	
	public void destory(){
		System.out.println(DiaoSiAc.class.getName()+"销毁");
	}

	public int getSss() {
		return sss;
	}

	public void setSss(int sss) {
		this.sss = sss;
	}

	public TestCh01 getDs() {
		return ds;
	}

	public void setDs(TestCh01 ds) {
		this.ds = ds;
	}

}

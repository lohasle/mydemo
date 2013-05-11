package demo.spring.di.vo;

public class PrintMechine {
	private int count;

	public void print(String s) {
		System.out.println(s + " printer: " + count++);
	}
}

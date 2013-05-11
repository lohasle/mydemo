package demo.spring.di.svr.impl;

import demo.spring.di.svr.DoSometing;
import demo.spring.di.vo.PrintMechine;

public abstract class DoSometingImpl implements DoSometing {
	private PrintMechine printMechine;

	public void setPrintMechine(PrintMechine printMechine) {
		this.printMechine = printMechine;
	}
	
	public PrintMechine getPrintMechine() {
		return printMechine;
	}

	public abstract PrintMechine createPrototypePrinter();

	public PrintMechine createSingletonPrinter() {
		System.out.println("该方法不会被执行，如果输出就错了");
		return new PrintMechine();
	}

	public void say() {
		printMechine.print("setter");
        createPrototypePrinter().print("prototype");
	}

}

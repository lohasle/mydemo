package demo.spring.di.svr;

import demo.spring.di.vo.PrintMechine;

public interface DoSometing {
	void say();
	PrintMechine createPrototypePrinter();
	PrintMechine createSingletonPrinter();
}

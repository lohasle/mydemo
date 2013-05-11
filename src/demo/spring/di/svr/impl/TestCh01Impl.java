package demo.spring.di.svr.impl;

import demo.spring.di.svr.TestCh01;
import demo.spring.di.vo.Diaosi;

public class TestCh01Impl implements TestCh01 {

	@Override
	public void ws(Diaosi d) {
		// TODO Auto-generated method stub
		System.out.println(d.getInfo());
	}
}

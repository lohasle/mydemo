package demo.dy.svr.impl;

import demo.dy.svr.Mammal;
import demo.dy.svr.Primate;

public class Monkey implements Mammal, Primate {

	@Override
	public void think() {
		// TODO Auto-generated method stub
		System.out.println("思考");
	}

	@Override
	public void eat(String food) {
		// TODO Auto-generated method stub
		System.out.println("food is " + food);
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		System.out.println("哺乳动物");
		return "哺乳动物";
	}

}

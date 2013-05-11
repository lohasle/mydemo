package demo.thread.threadvar;

import java.util.Random;

/**
 * ThreadLocalDemo
 * 
 * @author fule Synchronized用于线程间的数据共享，而ThreadLocal则用于线程间的数据隔离。
 *         <p>
 *         http://lavasoft.blog.51cto.com/62575/51926
 *         </p>
 * 
 */
public class ThreadLocalDemo implements Runnable {

	// 用线程的局部变量来存储对象
	private final static ThreadLocal<Student> tl = new ThreadLocal<Student>();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 获取当前线程的名字
		String currentThreadName = Thread.currentThread().getName();
		System.out.println(currentThreadName + " is running!");
		// 产生一个随机数并打印
		Random random = new Random();
		int age = random.nextInt(100);
		System.out
				.println("thread " + currentThreadName + " set age to:" + age);
		// 获取一个Student对象，并将随机数年龄插入到对象属性中
		Student student = getStudent();
		student.setAge(age);
		System.out.println("thread " + currentThreadName
				+ " first read age is:" + student.getAge());
		try {
			Thread.sleep(500);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		System.out.println("thread " + currentThreadName
				+ " second read age is:" + student.getAge());
	}

	public Student getStudent() {
		Student student = tl.get();
		// 线程首次访问时 stu为null
		if (student == null) {
			student = new Student();
			tl.set(student);
		}
		return student;
	}

	public static void main(String[] args) {
		ThreadLocalDemo td = new ThreadLocalDemo();
		Thread th1 = new Thread(td, "a");
		Thread th2 = new Thread(td, "b");
		th1.start();
		th2.start();
	}
}

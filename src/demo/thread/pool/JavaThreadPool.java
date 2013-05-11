package demo.thread.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import demo.thread.threadvar.Student;

public class JavaThreadPool {

	private static List<String> list;

	/**
	 * newFixedThreadPool：创建固定大小的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小。
	 * 线程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充一个新线程。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		list = new ArrayList<String>();
		list.add("任务1");
		list.add("任务2");
		list.add("任务3");
		list.add("任务4");
		list.add("任务5");
		list.add("任务6");
		list.add("任务7");
		list.add("任务8");
		list.add("任务9");
		list.add("任务10");
		
		// 创建一个可重用固定线程数的线程池
		//ExecutorService pool = Executors.newFixedThreadPool(5);
		ExecutorService pool = Executors.newCachedThreadPool();
		// 创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
		MyThread mh = new MyThread();
		mh.setTaskList(list);
		Thread[] threads = new Thread[10];
		// 将线程放入池中进行执行
		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(mh,"任务"+(i+1));
			pool.execute(threads[i]);
		}
		for (int i = 6; i < 10; i++) {
			threads[i] = new Thread(mh,"任务"+(i+1));
			pool.execute(threads[i]);
		}
		//pool.shutdown();
	}
}

class MyThread implements Runnable {
	private final static ThreadLocal<Student> tl = new ThreadLocal<Student>();
	private List<String> taskList; // 线程执行队列
	private volatile int count;
	private volatile int sqindex;
	public void setTaskList(List<String> taskList) {
		this.taskList = taskList;
	}
	
	private synchronized int getCurrentCount() {
		return count++;
	}

	private synchronized  int getCurrentsqindex() {
		return sqindex++;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (sqindex < taskList.size()) {
			System.out.println(taskList.get(getCurrentsqindex())+"正在执行");
		}
		
		String currentThreadName = Thread.currentThread().getName();
		System.out.println(currentThreadName + "is running!");
		// 产生一个随机数并打印
		System.out.println("thread " + currentThreadName + " set age to:"
				+ getCurrentCount());
		// 获取一个Student对象，并将随机数年龄插入到对象属性中
		Student student = getStudent();
		student.setAge(getCurrentCount());
		System.out.println("thread " + currentThreadName
				+ " first read age is:" + student.getAge());
	/*	try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}*/
		System.out.println("thread " + currentThreadName
				+ " second read age is:" + student.getAge());
		//Thread.yield();
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

}

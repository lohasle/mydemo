package demo.arithmetic.exam;

public class Ex01 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(f(6));
		findprimes(10000);
	}

	public static void findprimes(int a) {
		int count = 0;
		for (int i = 1; i <= a; i++) {
			if (isPrime(i)) {
				++count;
				System.out.print(i + "\t");
			}
		}
		System.out.println("\n" + count);
	}

	public static boolean isPrime(int i) {
		boolean flag = true;
		for (int j = 2; j <= Math.sqrt(i); j++) {
			if (i % j == 0) {
				flag = false;
			}
		}
		return flag;
	}

	public static int f(int a) {
		int b2 = 1;
		int result = 0;
		int b1 = 1;
		if (a == 0 || a == 1) {
			return 1;
		} else {
			for (int i = 0; i < a - 2; i++) {
				result = b1 + b2;
				b1 = b2;
				b2 = result;
			}
			return result;
		}
	}

}

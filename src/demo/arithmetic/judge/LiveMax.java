package demo.arithmetic.judge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 对于一个给定的数字（可能会很大）<br>
 * 从中删除一定量的数字，使得余下的数字按原次序组成的数最大<br>
 * 我们保证原数的位数不超过50，删除数字的个数小于原数的位数<br>
 * @author fule
 *
 */
public class LiveMax {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		System.out.println("请输入一个不超过50位的整数");
//		Scanner input = new Scanner(System.in);
//		String  str = input.next();
//		System.out.println("请输入需要删除的位数");
//		int ws =  input.nextInt();
		
		String  str ="71121192462439821";
		int ws = 3;
		char[] s = str.toCharArray();
		int index = 0;
		int point = 0;
		int tj=0;
		Map<String, Object> map;
		boolean fl = true;
		while(ws>0){
			char[] tt = new char[ws+1];
			if (fl) {
				System.arraycopy(s, 0, tt, 0, ws+1);
			}else {
				System.arraycopy(s, tj, tt, 0, ws+1);
			}
			map = getMax(tt);
			point = (Integer) map.get("i");
			char tmax = (Character) map.get("v");
			if (tt[0]<tmax) {
				System.arraycopy(s, point+tj, s, index, s.length-point-1);
				ws-=point;
				index+=point-1;
				
			}else {
				tj++;
				fl = false;
			}
		}
		System.out.println(Arrays.toString(s));
	}

	public static Map<String, Object> getMax(char[] array){
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;//最大值所在的位子
		char[] a = new char[array.length];
		System.arraycopy(array, 0, a, 0, array.length);
		for (int i = 0; i < a.length-1; i++) {
			for (int j = 0; j < a.length-i-1; j++) {
				if (a[j]<a[j+1]) {
					char temp = a[j];
					a[j] = a[j+1];
					a[j+1] = temp;
				}
			}
		}
		map.put("v", a[0]);
		for (int i = 0; i < array.length; i++) {
			if (a[0]==array[i]) {
				count =i;
			}
		}
		map.put("i", count);
		return map;
	}
	
	

}

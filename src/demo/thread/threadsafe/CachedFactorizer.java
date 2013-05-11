package demo.thread.threadsafe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 因数分解 查看 缓冲中是有已经有结果了 通过代码段 活跃性和性能的问题
 * 
 * @author fule
 * 
 */
@Controller
@RequestMapping("/testThread/")
public class CachedFactorizer {

	private Integer lastNumber;

	private Integer[] lastFactors;

	private long hits;

	private long cacheHits;

	public synchronized long getHits() {
		return hits;
	}

	public synchronized double getCacheHitRatio() {
		return (double) cacheHits / (double) hits;
	}

	@RequestMapping("testCachedFactorizer.do")
	public void testFenjie(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		StringBuffer sBuffer = new StringBuffer("结果<br>");
		Integer i = Integer.parseInt(request.getParameter("i"));
		Integer[] factors = null;
		String string = null;
		synchronized (this) {
			++hits;
			if (i.equals(lastNumber)) {
				factors = lastFactors.clone();
				++cacheHits;
				string = "缓冲中有因式";
			}
		}
		if (factors == null) {
			factors = fators2(i.intValue());
			synchronized (this) {
				lastNumber = i;
				lastFactors = factors.clone();
			}
			string = "缓冲中无因式";
		}
		response.setContentType("text/html;charset=utf-8");

		sBuffer.append(i + "=");
		sBuffer.append(factors[0]);
		for (int j = 1; j < factors.length; j++) {
			sBuffer.append("*" + factors[j]);
		}
		sBuffer.append("<br>" + string);
		sBuffer.append("<br>命中率为" + getCacheHitRatio());
		response.getWriter().println(sBuffer.toString());
	}

	// 递归方法解
	private Integer[] fators(int a, List<Integer> re) {
		if (a <= 3) {
			re.add(a);
		} else {
			for (int i = 2; i <= a; i++) {
				if (a % i == 0) {
					re.add(i);
					a /= i;
					if (a != 1) {
						fators(a, re);
						break;
					}
				}
			}
		}
		return re.toArray(new Integer[] { 0 });
	}

	// 非递归方法解
	private Integer[] fators2(int a) {
		List<Integer> re = new ArrayList<Integer>();
		if (a <= 3) {
			re.add(a);
		} else {
			int count = 2;
			while (a != 1) {
				if (a % count == 0) {
					re.add(count);
					a /= count;
					while (a % count != 0) {
						count++;
						break;
					}
				} else {
					count++;
				}
			}
		}
		return re.toArray(new Integer[] {});
	}

	public static void main(String[] args) {
		CachedFactorizer ch = new CachedFactorizer();
		// Integer[] integers = ch.fators(86832652,new ArrayList<Integer>());
		Integer[] integers = ch.fators2(86832652);
		System.out.print("86832652=");
		for (Integer integer : integers) {
			System.err.print("*" + integer);
		}
	}
}

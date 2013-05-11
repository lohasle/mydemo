package demo.thread.threadsafe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testVolatile/")
public class VolatileDemo {

	// volatile 变量 可见性
	private volatile OneValueCache cache = new OneValueCache(12, new Integer[]{2,2,3});

	@RequestMapping("test.do")
	public void testVolatile(HttpServletResponse response) throws IOException {
		Random random = new Random();
		int num = random.nextInt(100);
		response.setContentType("text/html;charset=utf-8");
		Integer in[] = cache.getFactors(num);
		if (in == null) {
			in = fators2(num);
			cache = new OneValueCache(num, in);
		}
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(num + "=");
		sBuffer.append(in[0]);
		for (int j = 1; j < in.length; j++) {
			sBuffer.append("*" + in[j]);
		}
		response.getWriter().println(sBuffer.toString());
	}

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

}

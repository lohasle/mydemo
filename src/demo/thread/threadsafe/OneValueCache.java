package demo.thread.threadsafe;

import java.util.Arrays;

//不可变容器类
public class OneValueCache {
	private final Integer lastNumber;

	private final Integer[] lastFactors;

	public OneValueCache(Integer i, Integer[] f) {
		lastNumber = i;
		lastFactors = Arrays.copyOf(f, f.length);
	}

	// 到缓存拿结果
	public Integer[] getFactors(Integer i) {
		if (lastNumber == null || !lastNumber.equals(i)) {
			return null;
		}
		return Arrays.copyOf(lastFactors, lastFactors.length);
	}
}

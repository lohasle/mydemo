package demo.arithmetic.x;

import java.io.Serializable;

/**
 * Title: 新安江模型参数
 */

public class XajParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String comp_ID;
	public float Wm; // 张力水容量
	public float Um; // 上层蓄水容量
	public float Lm; // 下层蓄水容量
	public float Dm; // 深层蓄水容量
	public float B; // 张力水蓄水容量曲线的方次
	public float Im; // 不透水的面积比例
	public float K; // 蒸发能力折算系数
	public float C; // 深层蒸发系数
	public float Sm; // 表土自由水蓄水容量
	public float Ex; // 表土自由水蓄水容量曲线的方次
	public float Kg; // 自由水蓄水水库对地下水的出流系数
	public float Ki; // 自由水蓄水水库对壤中流的出流系数
	public float Ci; // 壤中流消退系数
	public float Cg; // 地下水库消退系数
	public float Cs; // 河网蓄水量消退系数
	public float Xe; // 马斯京干法参数－XE
	public float Ke; // 马斯京干法参数－KE
}

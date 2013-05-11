package demo.arithmetic.xaj;

import java.io.Serializable;

/**
 * Title: 新安江模型参数 这里指的是一些可率定的参数
 */

public class XajParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private float Wm; // 张力水容量
	private float Um; // 上层蓄水容量
	private float Lm; // 下层蓄水容量
	private float Dm; // 深层蓄水容量
	private float B; // 张力水蓄水容量曲线的方次
	private float Im; // 不透水的面积比例
	private float K; // 蒸发能力折算系数
	private float C; // 深层蒸发系数
	private float Sm; // 表土自由水蓄水容量
	private float Ex; // 表土自由水蓄水容量曲线的方次
	private float Kg; // 自由水蓄水水库对地下水的出流系数
	private float Ki; // 自由水蓄水水库对壤中流的出流系数
	private float Ci; // 壤中流消退系数
	private float Cg; // 地下水库消退系数
	private float Cs; // 河网蓄水量消退系数
	private float Xe; // 马斯京干法参数－XE
	private float Ke; // 马斯京干法参数－KE

	public XajParam() {
	}

	public XajParam(float wm, float um, float lm, float dm, float b, float im,
			float k, float c, float sm, float ex, float kg, float ki, float ci,
			float cg, float cs, float xe, float ke) {
		super();
		Wm = wm;
		Um = um;
		Lm = lm;
		Dm = dm;
		B = b;
		Im = im;
		K = k;
		C = c;
		Sm = sm;
		Ex = ex;
		Kg = kg;
		Ki = ki;
		Ci = ci;
		Cg = cg;
		Cs = cs;
		Xe = xe;
		Ke = ke;
	}

	public float getWm() {
		return Wm;
	}

	public void setWm(float wm) {
		Wm = wm;
	}

	public float getUm() {
		return Um;
	}

	public void setUm(float um) {
		Um = um;
	}

	public float getLm() {
		return Lm;
	}

	public void setLm(float lm) {
		Lm = lm;
	}

	public float getDm() {
		return Dm;
	}

	public void setDm(float dm) {
		Dm = dm;
	}

	public float getB() {
		return B;
	}

	public void setB(float b) {
		B = b;
	}

	public float getIm() {
		return Im;
	}

	public void setIm(float im) {
		Im = im;
	}

	public float getK() {
		return K;
	}

	public void setK(float k) {
		K = k;
	}

	public float getC() {
		return C;
	}

	public void setC(float c) {
		C = c;
	}

	public float getSm() {
		return Sm;
	}

	public void setSm(float sm) {
		Sm = sm;
	}

	public float getEx() {
		return Ex;
	}

	public void setEx(float ex) {
		Ex = ex;
	}

	public float getKg() {
		return Kg;
	}

	public void setKg(float kg) {
		Kg = kg;
	}

	public float getKi() {
		return Ki;
	}

	public void setKi(float ki) {
		Ki = ki;
	}

	public float getCi() {
		return Ci;
	}

	public void setCi(float ci) {
		Ci = ci;
	}

	public float getCg() {
		return Cg;
	}

	public void setCg(float cg) {
		Cg = cg;
	}

	public float getCs() {
		return Cs;
	}

	public void setCs(float cs) {
		Cs = cs;
	}

	public float getXe() {
		return Xe;
	}

	public void setXe(float xe) {
		Xe = xe;
	}

	public float getKe() {
		return Ke;
	}

	public void setKe(float ke) {
		Ke = ke;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

package demo.arithmetic.xaj;

import java.util.HashMap;
import java.util.Map;

/**
 * 三水源新安江模型
 * 
 * 模拟模型过程 包括 蒸散发 产流 三水源划分 和汇流计算
 * 
 * @author fule
 * 
 * @version 1.0
 */

public class XajModelGc {

	/**
	 * TODO 三水源新安江径流计算模型
	 * 
	 * @param i_periods
	 *            时段数
	 * @param P
	 *            降雨列表
	 * @param em
	 *            蒸发值列表
	 * @param ua0
	 *            初始上层土壤含水量
	 * @param la0
	 *            初始下层土壤含水量
	 * @param da0
	 *            初始深层土壤含水量
	 * @param xajParam
	 *            新安江 模型参数
	 * @return map (R->产生的径流列表<br>
	 *         ua->上层土壤湿度序列<br>
	 *         la->下层土壤湿度序列<br>
	 *         da->深层土壤湿度序列<br>
	 *         )
	 */
	public static Map<String, float[]> runOffModel(int i_periods, float[] P,
			float[] em, float ua0, float la0, float da0, XajParam xajParam) {
		Map<String, float[]> map = new HashMap<String, float[]>();
		float[] R = new float[i_periods + 1];// 径流量
		float[] ua = new float[i_periods + 1];// 上层土壤含水量
		float[] la = new float[i_periods + 1];// 下层土壤含水量
		float[] da = new float[i_periods + 1];// 深层土壤含水量
		float PE;
		float WM, UM, LM, DM, im, b; // 产流量计算模型参数

		float k, c; // 蒸发计算模型参数
		float EU[] = new float[i_periods]; // i_periods 时段长度
		float EL[] = new float[i_periods];
		float ED[] = new float[i_periods];
		float wmt, MM, A, EP;// t时刻的土壤平均含水量、//点蓄水容量的最大值
								// wmt所对应的流域蓄水容量曲线的纵坐标、蒸发
		ua[0] = ua0; // 开始的 上层土壤含水量
		la[0] = la0;// 下层土壤含水量
		da[0] = da0;// 深层土壤含水量

		// 从三水源模型结构变量中取得模型参数
		// 产流量计算模型参数
		WM = xajParam.getWm();// 流域平均蓄水容量
		UM = xajParam.getUm(); // TODO 上层蓄水容量
		LM = xajParam.getLm();
		DM = xajParam.getDm();
		im = xajParam.getIm(); // 不透水的面积比例
		b = xajParam.getB(); // 张力水蓄水容量曲线的方次

		// 蒸发计算模型参数
		k = xajParam.getK();// 蒸发折减系数
		c = xajParam.getC();// 深层蒸发系数

		MM = WM * (1 + b) / (1 - im);// 点蓄水容量的最大值
		for (int i = 0; i < i_periods; i++) {
			wmt = ua[i] + la[i] + da[i]; // 当前时段土壤含水量
			if (wmt > WM)
				wmt = WM;
			EP = k * em[i];
			PE = P[i] - EP;
			// 以下计算径流和
			if (PE >= 0) { // 土湿的计算
				float temp = new Double(Math.pow(1 - wmt / WM, 1 / (b + 1)))
						.floatValue();
				A = MM * (1 - temp);
				if (PE + A < MM) {
					temp = new Double(Math.pow(1 - (PE + A) / MM, b + 1))
							.floatValue();
					R[i] = PE - WM + wmt + WM * temp;
				} else {
					R[i] = PE - WM + wmt;
				}
				EU[i] = k * em[i];
				EL[i] = 0;
				ED[i] = 0;
				// TODO ua 上层土壤含水量 l d
				// 如果上层土壤含水量没有饱和 不会下渗到二三层
				if (ua[i] + PE - R[i] < UM) {
					ua[i + 1] = ua[i] + PE - R[i];
					la[i + 1] = la[i];
					da[i + 1] = da[i];
				} else {// 如果上层土壤含水量已经饱和
					if (ua[i] + la[i] + PE - R[i] - UM > LM) {// 如果第二层土壤含水量已经饱和补充第三层
						ua[i + 1] = UM;
						la[i + 1] = LM;
						da[i + 1] = wmt + PE - R[i] - ua[i + 1] - la[i + 1];
					} else {// 如果第二层土壤含水量没有饱和
						la[i + 1] = ua[i] + la[i] + PE - R[i] - UM; // TODO
																	// 补充第二层土壤湿度
						ua[i + 1] = UM;
						da[i + 1] = da[i];
					}
				}
			} else {// 以下计算蒸发量
				R[i] = 0;
				if (ua[i] + PE > 0) {// 上层没有被蒸发完
					EU[i] = EP; // TODO EP 时段的蒸散发能力 为E[i]*K
					EL[i] = 0;
					ED[i] = 0;
					ua[i + 1] = ua[i] + PE; // 减去蒸发量
					la[i + 1] = la[i];
					da[i + 1] = da[i];
				} else {// 上层已经被蒸发完
					EU[i] = ua[i] + P[i];
					ua[i + 1] = 0;
					if (la[i] > c * LM) {// 二层 em 实测蒸发值
						EL[i] = (k * em[i] - EU[i]) * la[i] / LM;
						la[i + 1] = la[i] - EL[i];
						da[i + 1] = da[i];
						ED[i] = 0;
					} else {// 三层
						if (la[i] > c * (k * em[i] - EU[i])) {
							EL[i] = c * (k * em[i] - EU[i]);
							ED[i] = 0;
							la[i + 1] = la[i] - EL[i];
							da[i + 1] = da[i];
						} else {
							EL[i] = la[i];
							la[i + 1] = 0;
							ED[i] = c * (k * em[i] - EU[i]) - EL[i];
							da[i + 1] = da[i] - ED[i];
						}
					}
				}
			}
			// emt=EU[i]+EL[i]+ED[i] //当前时段蒸发量
			// 检验 蒸散发模型 水量平衡方程
			if (ua[i + 1] < 0)
				ua[i + 1] = 0;
			if (la[i + 1] < 0)
				la[i + 1] = 0;
			if (da[i + 1] < 0)
				da[i + 1] = 0;

			if (ua[i + 1] > UM)
				ua[i + 1] = UM;
			if (la[i + 1] > LM)
				la[i + 1] = LM;
			if (da[i + 1] > DM)
				da[i + 1] = DM;

			System.out.println("R[" + i + "]->" + R[i]);
			System.out.println("ua[" + i + "]->" + ua[i]);
			System.out.println("la[" + i + "]->" + la[i]);
			System.out.println("da[" + i + "]->" + da[i]);
		}
		map.put("R", R);
		map.put("ua", ua);
		map.put("la", la);
		map.put("da", da);
		return map;
	}

	/**
	 * TODO 三水源新安江分水源水源划分计算模型
	 * 
	 * 这里没有考虑水源的消退
	 * 
	 * @param p
	 *            降雨列表
	 * @param R
	 *            径流量列表
	 * @param em
	 *            蒸发列表
	 * @param fr0
	 *            开始自由水蓄水深度
	 * @param s0
	 *            开始自由水蓄水容量
	 * @param i_step
	 *            时段间隔
	 * @param i_periods
	 *            时段数 时段数
	 * @param xajParam
	 * 
	 * @return map RI->壤中流径流量序列<br>
	 *         RS->地面径流量序列<br>
	 *         RG->地下径流量序列<br>
	 *         s->自由水容量序列<br>
	 *         fr->产流面积序列<br>
	 */
	public static Map<String, float[]> sourceModel(float[] P, float[] R,
			float[] em, float fr0, float s0, int i_step, int i_periods,
			XajParam xajParam) {
		Map<String, float[]> map = new HashMap<String, float[]>();
		float[] RI = new float[i_periods];
		float[] RS = new float[i_periods];
		float[] RG = new float[i_periods];
		float[] fr = new float[i_periods];
		float[] s = new float[i_periods + 1];
		float Im, sm, ex, kg, ki; // 分水源计算模型参数
		float k, c; // 蒸发计算模型参数
		float PE;
		float SMM, AU, X;
		float SS, Q, GD, ID;
		int d, G;
		float KID, KGD;

		Im = xajParam.getIm();// 不透水的面积比例
		sm = xajParam.getSm();// 自由水的平均蓄水容量
		ex = xajParam.getEx();// 自由蓄水容量曲线的曲线系数
		kg = xajParam.getKg();// 地下径流的出流系数
		ki = xajParam.getKi();// kss-壤中流的出流系数

		k = xajParam.getK();// 蒸发能力折算系数
		c = xajParam.getC();// 深层 蒸散发

		d = 24 / i_step; // 一天分的时段数 分段了计算
		float temp = new Double(Math.pow(1 - (kg + ki), 1.0f / d)).floatValue();
		KID = (1 - temp) / (1 + kg / ki);// kssd-和kss对应的参数
		KGD = KID * kg / ki;// 和kg对应的参数
		SMM = (1 + ex) * sm;// 自由水的点蓄水容量的最大值
		int i_max_len = 3 * 24 / i_step;// TODO 消退的时段数 3???? 最多三天的消退时间边界？
		s[0] = s0;
		fr[0] = fr0;// s-自由水蓄水深

		for (int i = 0; i < i_periods; i++) {
			PE = P[i] - k * em[i];
			if (PE > 0) {
				if (i == 0) {
					X = fr0;
				} else {
					X = fr[i - 1];// 图7-9中的k 各个点开始的自由水水深
				}
				// 图7-9
				fr[i] = R[i] / PE;// TODO 计算产流面积 R?
				if (fr[i] < 0.001f)
					fr[i] = 0.001f;
				s[i + 1] = X * s[i] / fr[i];// TODO 自由水，产流面积越来越大？？？？？
				SS = s[i + 1];// 暂时保存
				Q = R[i] / fr[i];
				G = new Float(Q / 5.0f).intValue() + 1;// 解决差分计算的误差问题，5毫米一段把每时段分为G段
				Q = Q / G;
				temp = new Double(Math.pow(1 - (KGD + KID), 1.0f / G))
						.floatValue();
				ID = (1 - temp) / (1 + KGD / KID);// 以每个时段的KSSD和KGD为参数计算G时段中的每个时段的KSSD和KGD
				GD = ID * KGD / KID;
				RS[i] = 0;
				RG[i] = 0;
				RI[i] = 0;// 地面径流RS、地下径流RG、壤中流RSS(RI)
				for (int j = 0; j < G; j++) {
					if (s[i + 1] > sm)
						s[i + 1] = sm;
					temp = new Double(Math.pow(1 - s[i + 1] / sm, 1 / (1 + ex)))
							.floatValue();
					AU = SMM * (1 - temp);//
					if (AU + Q < SMM) {
						float tmp = new Double(Math.pow(1 - (Q + AU) / SMM,
								1 + ex)).floatValue();
						RS[i] += (Q - sm + s[i + 1] + sm * tmp) * fr[i];
					} else {
						RS[i] += (Q + s[i + 1] - sm) * fr[i];// G个时段的径流的和
					}
					s[i + 1] += (j + 1) * Q - RS[i] / fr[i];// 暂时保存每个G时段的自由水深度
					RG[i] += s[i + 1] * GD * fr[i];
					RI[i] += s[i + 1] * ID * fr[i];
					s[i + 1] = (j + 1) * Q + SS - (RS[i] + RI[i] + RG[i])
							/ fr[i];

				}
				if (s[i + 1] > sm)
					s[i + 1] = sm;
			} else {// PE<0，不需要分时段
				if (i == 0) {
					fr[i] = fr0;
				} else {
					fr[i] = fr[i - 1];// 产流面积不变
				}
				RS[i] = 0.0f;
				RG[i] = s[i] * KGD * fr[i];//
				RI[i] = RG[i] * KID / KGD;
				if (fr[i] < 0.001f)
					fr[i] = 0.001f;
				s[i + 1] = s[i] - (RG[i] + RI[i]) / fr[i]; // TODO 水量/面积=水深
				if (s[i + 1] < 0)
					s[i + 1] = 0;
				if (s[i + 1] > sm)
					s[i + 1] = sm;
			}
			System.out.println("RG[" + i + "]->" + RG[i]);
			System.out.println("RS[" + i + "]->" + RS[i]);
			System.out.println("RI[" + i + "]->" + RI[i]);
		}

		/*
		 * // TODO 参考C++ 消退计算 for (int i = i_periods; i < i_periods + i_max_len;
		 * i++) {// 消退阶段 fr[i] = fr[i - 1]; RS[i] = 0; RG[i] = s[i] * KGD *
		 * fr[i];// RI[i] = RG[i] * KID / KGD;// ，rss[i]=kss[i]*s[i]*fr[i] s[i +
		 * 1] = s[i] - (RG[i] + RI[i]) / fr[i]; if (s[i + 1] < 0) s[i + 1] = 0;
		 * if (s[i + 1] > sm) s[i + 1] = sm; }
		 */
		map.put("RS", RS);
		map.put("RG", RG);
		map.put("RI", RI);
		map.put("s", s);
		map.put("fr", fr);
		return map;
	}

	/**
	 * TODO 三水源新安江汇流计算模型--线形水库模型
	 * 
	 * @param i_step
	 *            步长
	 * @param i_periods
	 *            时段数
	 * @param area
	 *            流域面积
	 * @param RG
	 * @param RI
	 * @param RS
	 * @param xajParam
	 * @return dq 总径流
	 */
	public static float[] flowModelByLine(int i_step, int i_periods,
			float area, float[] RG, float[] RI, float[] RS, XajParam xajParam) {
		int i_floods;
		float cs, ci, cg; // 汇流计算参数,消退系数和马斯京干参数.cs-河网蓄水量的消退系数
		float CSD, CID, CGD;

		// 从三水源模型结构变量中取得模型参数
		cs = xajParam.getCs();
		ci = xajParam.getCi();
		cg = xajParam.getCg();

		float d = 24 / i_step; // 一天分的时段数
		CSD = new Double(Math.pow(cs, 1 / d)).floatValue();// 每时段的参数值
		CID = new Double(Math.pow(ci, 1 / d)).floatValue();
		CGD = new Double(Math.pow(cg, 1 / d)).floatValue();

		float U = area / (3.6f * i_step); // 单位换算系数，将径流深转化为流量
		i_floods = i_periods + 7 * 24 / i_step;// +7天的时段
		float[] dq = new float[i_floods];
		float QG[] = new float[i_floods];
		float QI[] = new float[i_floods];
		float QS[] = new float[i_floods];

		// 地下径流和壤中流汇流
		for (int i = 0; i < i_floods; i++) {
			if (i == 0) {
				QS[i] = RS[i] * (1 - CSD) * U;// 地面径流
				QG[i] = RG[i] * (1 - CGD) * U;// 地下径流
				QI[i] = RI[i] * (1 - CID) * U;// 壤中流
			} else {
				if (i < i_periods) {
					QS[i] = QS[i - 1] * CSD + RS[i] * (1 - CSD) * U;
					QG[i] = QG[i - 1] * CGD + RG[i] * (1 - CGD) * U;
					QI[i] = QI[i - 1] * CID + RI[i] * (1 - CID) * U;
				} else {
					QS[i] = QS[i - 1] * CSD;
					QG[i] = QG[i - 1] * CGD;
					QI[i] = QI[i - 1] * CID;
				}
			}
			System.out.println("QS[" + i + "]->" + QS[i]);
			System.out.println("QG[" + i + "]->" + QG[i]);
			System.out.println("QI[" + i + "]->" + QI[i]);
		}

		dq[0] = 0;
		for (int i = 1; i < i_floods; i++) {// 总径流
			dq[i] = QG[i - 1] + QI[i - 1] + QS[i - 1];
			if (dq[i] < 0)
				dq[i] = 0;// 总出口流量
			System.out.println(i + "时段总出口流量为dq[" + i + "]->" + dq[i]);
		}
		return dq;
	}

	/**
	 * TODO 三水源新安江汇流计算模型--河网汇流模型
	 * 
	 * @param i_step
	 *            步长
	 * @param i_periods
	 *            时段数
	 * @param area
	 *            流域面积
	 * @param unitgraph  单位线
	 * @param i_point
	 * @param RG
	 * @param RI
	 * @param RS
	 * @param xajParam
	 * @param dq
	 *            返回的流量
	 * @return 洪水长度
	 */
	public static int flow_xaj3_model(int i_step, int i_periods, float area,
			float[] unitgraph, int i_point, float[] RG, float[] RI, float[] RS,
			XajParam xajParam, float[] dq) {
		float ci, cg, uh; // 汇流计算参数,消退系数和马斯京干参数.cs-河网蓄水量的消退系数
		float CID, CGD;
		int i_max_len = i_periods + i_point - 1 + 3 * 24 / i_step; // 包括消退时间，i_point—单位线的长度
		float QG[] = new float[i_max_len];
		float QI[] = new float[i_max_len];
		float QS[] = new float[i_max_len];

		// 从三水源模型结构变量中取得模型参数
		ci = xajParam.getCi();
		cg = xajParam.getCg();

		float d = 24 / i_step; // 一天分的时段数
		CID = new Double(Math.pow(ci, 1 / d)).floatValue();
		CGD = new Double(Math.pow(cg, 1 / d)).floatValue();

		float U = area / (3.6f * i_step); // 单位换算系数，将径流深转化为流量

		for (int i = 0; i < i_max_len; i++) {
			QS[i] = 0.0f;
		}

		// 地下径流和壤中流汇流
		for (int i = 0; i < i_max_len; i++) {
			if (i == 0) {
				QG[i] = RG[i] * (1 - CGD) * U;
				QI[i] = RI[i] * (1 - CID) * U;
			} else {
				if (i < i_periods) {
					QG[i] = QG[i - 1] * CGD + RG[i] * (1 - CGD) * U;
					QI[i] = QI[i - 1] * CID + RI[i] * (1 - CID) * U;
				} else {
					QG[i] = QG[i - 1] * CGD;
					QI[i] = QI[i - 1] * CID;
				}
			}
		}

		for (int i = 0; i < i_periods; i++) {// 地面径流汇流采用单位线

			for (int j = 0; j < i_point; j++) {
				QS[j + i] += RS[i] * unitgraph[j] * U;
			}
		}

		dq[0] = 0;
		for (int i = 1; i < i_max_len; i++) {// 总径流
			if (i < i_periods + i_point) {// 有地面径流
				dq[i] = QG[i] + QI[i] + QS[i];
			} else {// 没有地面径流
				dq[i] = QG[i] + QI[i];
			}
			if (dq[i] < 0)
				dq[i] = 0;
		}

		return i_max_len;

	}

	/**
	 * 汇流系数法河道汇流计算
	 * 
	 * @param i_sub_floods
	 *            :分单元汇流洪水过程时段数
	 * @param coe
	 *            演算系数
	 * @param inflow
	 * @return 演进流量
	 */
	public static float[] routing_coefficient(int i_sub_floods, float[] coe,
			float[] inflow) {// 每阶段的流量折减
		int i_coe_length = coe.length;
		float[] dq_routing = new float[i_sub_floods + i_coe_length - 1];
		for (int ii = 0; ii < i_sub_floods; ii++) {
			for (int k = 0; k < i_coe_length; k++) {
				dq_routing[ii + k] += inflow[ii] * coe[k];
			}
		}
		return dq_routing;
	}

	/**
	 * 马斯京干法河道汇流计算
	 * 
	 * @param m
	 *            计算时段数
	 * @param time_step
	 *            步长
	 * @param n
	 *            河段数
	 * @param k
	 *            马斯京干法参数，蓄水流量关系曲线的坡度
	 * @param x
	 *            马斯京干法参数，流量比重系数
	 * @param inflow
	 *            return dq_routing 演算流量
	 */
	public static float[] routing_mask(int m, int time_step_hour, int n,
			float k, float x, float[] inflow) {// ？？？？？？？？？？？？？？
		float[] dq_routing = new float[m];
		float time_step = time_step_hour / 24.0f;// 转化成天

		float[] QC = new float[n];
		float QI1 = 0, QI2 = 0, QO1 = 0, QO2 = 0;

		k = k / n;
		x = 0.5f - n * (0.5f - x);
		if ((time_step >= 2 * k * x) && (time_step <= 2 * k - 2 * k * x)) {// 马斯京干法适用范围判断
			float c0 = (0.5f * time_step - k * x)
					/ (k - k * x + 0.5f * time_step);
			float c1 = (k * x + 0.5f * time_step)
					/ (k - k * x + 0.5f * time_step);
			float c2 = (k - k * x - 0.5f * time_step)
					/ (k - k * x + 0.5f * time_step);

			for (int i = 0; i < n; i++) {// 河段初始流
				if (i < m) {
					QC[i] = inflow[0];// 入流等于出流
				} else {
					QC[i] = 0.0f;
				}
			}

			dq_routing[0] = inflow[0];// 入流等于出流

			for (int i = 1; i < m; i++) {
				for (int j = 0; j < n; j++) {
					QO1 = QC[j];
					if (j == 0) {
						QI1 = inflow[i - 1]; // TODO
						QI2 = inflow[i];
					}
					QO2 = c0 * QI2 + c1 * QI1 + c2 * QO1;
					QI1 = QO1;
					QI2 = QO2;
					QC[j] = QO2;
				}
				if (QO2 < 0.0001f)
					QO2 = 0.0f;
				dq_routing[i] = QO2;
			}
		}
		return dq_routing;

	}

	// test
	public static void main(String[] args) {
		XajParam x = new XajParam(45, 12, 45, 67, 0.3f, 0, 0.95f, 0.11f, 28, 1,
				0.45f, 0.5f, 0.995f, 0.933f, 0.88f, 0.4f, 0.3f);
		int i_periods = 100;
		float[] P = new float[i_periods];
		float[] em = new float[i_periods];

		for (int i = 0; i < P.length; i++) {
			if (i == 0) {
				P[i] = 10.5f;
				em[i] = 1.3f;
			} else {
				P[i] = P[i - 1] + 0.4f;
				em[i] = em[i - 1] + 0.6f;
			}
		}

		// 径流计算
		System.out.println("**********径流计算程序   begin ***********");
		Map<String, float[]> map = runOffModel(i_periods, P, em, 10, 17, 45, x);
		float[] R = map.get("R");
		/*
		 * for (float f : R) { System.out.println(f); }
		 */
		System.out.println("**********径流计算程序   end ***********\n\n");

		// 三水源划分 取时段长度为3h
		System.out.println("**********水源划分程序   begin ***********");
		Map<String, float[]> map2 = sourceModel(P, R, em, 12, 12, 3, i_periods,
				x);
		float[] RI = map2.get("RI");
		float[] RS = map2.get("RS");
		float[] RG = map2.get("RG");
		System.out.println("**********径流划分程序   end ***********");

		// 汇流计算
		System.out.println("**********线形水库汇流计算程序   begin ***********");
		flowModelByLine(3, i_periods, 45, RG, RI, RS, x);
		System.out.println("**********线形水库汇流计算程序   end ***********");
	}
}

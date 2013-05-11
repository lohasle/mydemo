package demo.arithmetic.x;

/**
 * 新安江模型通用函数
 */

public class XajModelFun {

	/**
	 * 三水源新安江产流计算模型
	 * 
	 * @param time_step
	 *            步长
	 * @param i_periods
	 *            时段数
	 * @param P
	 *            降雨
	 * @param em
	 *            蒸发值
	 * @param ua0
	 * @param la0
	 * @param da0
	 * @param xajParam
	 * @param R
	 *            净雨
	 * @param ua
	 *            上层土壤含水量
	 * @param la
	 *            下层土壤含水量
	 * @param da
	 *            深层土壤含水量
	 * @return
	 */
	public static void runoff_xaj3_model(int i_periods, float[] P, float[] em,
			float ua0, float la0, float da0, XajParam xajParam, float[] R,
			float[] ua, float[] la, float[] da) {// 图5-2
		float PE;
		float WM, UM, LM, DM, im, b; // 产流量计算模型参数  
		float sm, ex, kg, ki; // 分水源计算模型参数   SM自由水蓄水容量  ex自由水蓄水容量曲线指数 kg地下径流日出流系数  KI 壤中流流日出流系数
		float k, c; // 蒸发计算模型参数  
		// float ci,cg,ke,xe; //汇流计算参数

		float EU[] = new float[i_periods];   //i_periods 时段长度
		float EL[] = new float[i_periods];
		float ED[] = new float[i_periods];
		float wmt, MM, A, EP;// t时刻的土壤平均含水量、//点蓄水容量的最大值（7-1）
								// 、wmt所对应的流域蓄水容量曲线的纵坐标、蒸发
		ua[0] = ua0; //      开始的 上层土壤含水量
		la[0] = la0;// 		  下层土壤含水量
		da[0] = da0;//       深层土壤含水量

		// 从三水源模型结构变量中取得模型参数
		// 产流量计算模型参数
		WM = xajParam.Wm;// 流域平均蓄水容量
		UM = xajParam.Um;   // TODO 上层蓄水容量 
		LM = xajParam.Lm;
		DM = xajParam.Dm;
		im = xajParam.Im; // 不透水的面积比例
		b = xajParam.B; // 张力水蓄水容量曲线的方次

		// 分水源计算模型参数
		sm = xajParam.Sm; // 表土自由水蓄水容量
		ex = xajParam.Ex; // 表土自由水蓄水容量曲线的方次
		kg = xajParam.Kg; // 自由水蓄水水库对地下水的出流系数
		ki = xajParam.Ki; // 自由水蓄水水库对壤中流的出流系数

		// 蒸发计算模型参数
		k = xajParam.K;// 蒸发折减系数
		c = xajParam.C;// 深层蒸发系数

		// 汇流计算参数
		// ci=xajParam.getCi(); //壤中流消退系数
		// cg=xajParam.getCg(); //地下水库消退系数
		// uh=xajParam.getCs(); //单元流域上地面径流的单位线
		// ke=xajParam.getKe(); //马斯京干法参数－KE
		// xe=xajParam.getXe(); //马斯京干法参数－XE

		MM = WM * (1 + b) / (1 - im);// 点蓄水容量的最大值（7-1）
		for (int i = 0; i < i_periods; i++) // ?????????
		{
			wmt = ua[i] + la[i] + da[i]; // 当前时段土壤含水量
			if (wmt > WM)
				wmt = WM;

			EP = k * em[i];
			PE = P[i] - EP;

			// 以下计算径流和
			if (PE >= 0) {  //土湿的计算
				float temp = new Double(Math.pow(1 - wmt / WM, 1 / (b + 1)))
						.floatValue();// （3-6）
				A = MM * (1 - temp);
				if (PE + A < MM)// （3-7）
				{
					temp = new Double(Math.pow(1 - (PE + A) / MM, b + 1))
							.floatValue();
					R[i] = PE - WM + wmt + WM * temp;
				} else {
					R[i] = PE - WM + wmt;
				}
				EU[i] = k * em[i];
				EL[i] = 0;
				ED[i] = 0;
				//TODO ua 上层土壤含水量  l d 
				if (ua[i] + PE - R[i] < UM)// 如果上层土壤含水量没有饱和  不会下渗到二三层
				{
					ua[i + 1] = ua[i] + PE - R[i];  
					la[i + 1] = la[i];
					da[i + 1] = da[i];
				} else// //如果上层土壤含水量已经饱和
				{
					if (ua[i] + la[i] + PE - R[i] - UM > LM)// 如果第二层土壤含水量已经饱和  补充第三层
					{
						ua[i + 1] = UM;
						la[i + 1] = LM;
						da[i + 1] = wmt + PE - R[i] - ua[i + 1] - la[i + 1];
					} else// 如果第二层土壤含水量没有饱和
					{
						la[i + 1] = ua[i] + la[i] + PE - R[i] - UM;  //TODO  补充第二层土壤湿度
						ua[i + 1] = UM;
						da[i + 1] = da[i];
					}
				}
			}
			// 以下计算蒸发量  
			else {
				R[i] = 0;
				if (ua[i] + PE > 0)// 上层没有被蒸发完
				{
					EU[i] = EP;  //TODO  EP 时段的蒸散发能力  为E[i]*K 
					EL[i] = 0;
					ED[i] = 0;
					ua[i + 1] = ua[i] + PE;  //减去蒸发量
					la[i + 1] = la[i];
					da[i + 1] = da[i];
				} else// 上层已经被蒸发完
				{
					EU[i] = ua[i] + P[i];
					ua[i + 1] = 0;
					if (la[i] > c * LM) {//二层  em 实测蒸发值
						EL[i] = (k * em[i] - EU[i]) * la[i] / LM;
						la[i + 1] = la[i] - EL[i];
						da[i + 1] = da[i];
						ED[i] = 0;
					} else {//三层
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
			// 检验  蒸散发模型  水量平衡方程
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
		}

	}

	/**
	 * 三水源新安江分水源产流计算模型
	 * 
	 * @param p
	 *            降雨
	 * @param r
	 *            净雨
	 * @param em
	 *            蒸发
	 * @param fr0  开始自由水蓄水深度
	 * @param s0     开始自由水蓄水容量
	 * @param i_step  时段间隔
	 * @param i_periods 时段数
	 *            时段数
	 * @param xajParam
	 * @param rg
	 *            下 返回
	 * @param ri
	 *            中 返回
	 * @param rs
	 *            上 返回
	 * @param fr
	 *            返回，产流面积
	 * @param s
	 *            返回
	 */
	public static int runoff_division_xaj3_model(float[] P, float[] R,
			float[] em, float fr0, float s0, int i_step, int i_periods,
			XajParam xajParam, float[] RG, float[] RI, float[] RS, float[] fr,
			float[] s) {
		float Im, sm, ex, kg, ki; // 分水源计算模型参数
		float k, c; // 蒸发计算模型参数
		float PE;
		float SMM, AU, X;
		float SS, Q, GD, ID;
		int d, G;
		float KID, KGD;

		Im = xajParam.Im;//不透水的面积比例
		sm = xajParam.Sm;// 自由水的平均蓄水容量
		ex = xajParam.Ex;// 自由蓄水容量曲线的曲线系数
		kg = xajParam.Kg;// 地下径流的出流系数
		ki = xajParam.Ki;// kss-壤中流的出流系数

		k = xajParam.K;// 蒸发能力折算系数
		c = xajParam.C;// 深层 蒸散发

		d = 24 / i_step; // 一天分的时段数  分段了计算
		float temp = new Double(Math.pow(1 - (kg + ki), 1.0f / d)).floatValue();
		KID = (1 - temp) / (1 + kg / ki);// kssd-和kss对应的参数
		KGD = KID * kg / ki;// 和kg对应的参数
		SMM = (1 + ex) * sm;// 自由水的点蓄水容量的最大值
		int i_max_len = 3 * 24 / i_step;// TODO 消退的时段数  3????  最多三天的消退时间边界？
		s[0] = s0;
		fr[0] = fr0;// s-自由水蓄水深

		for (int i = 0; i < i_periods; i++) {
			PE = P[i] - k * em[i];
			if (PE > 0) {
				if (i == 0) {
					X = fr0;
				} else {
					X = fr[i - 1];// 图7-9中的k   各个点开始的自由水水深
				}
				// 图7-9
				fr[i] = R[i] / PE;// TODO  计算产流面积   R? 
				if (fr[i] < 0.001f)
					fr[i] = 0.001f;
				s[i + 1] = X * s[i] / fr[i];// TODO 自由水，产流面积越来越大？？？？？
				SS = s[i + 1];// 暂时保存
				Q = R[i] / fr[i];
				G = new Float(Q / 5.0f).intValue() + 1;// 解决差分计算的误差问题，5毫米一段把每时段分为G段
				Q = Q / G;
				temp = new Double(Math.pow(1 - (KGD + KID), 1.0f / G))
						.floatValue();
				ID = (1 - temp) / (1 + KGD / KID);// 以每个时段的KSSD和KGD为参数采用公式(7-9)计算G时段中的每个时段的KSSD和KGD
				GD = ID * KGD / KID;
				RS[i] = 0;
				RG[i] = 0;
				RI[i] = 0;// 地面径流RS、地下径流RG、壤中流RSS(RI)
				for (int j = 0; j < G; j++) {
					if (s[i + 1] > sm)
						s[i + 1] = sm;
					temp = new Double(Math.pow(1 - s[i + 1] / sm, 1 / (1 + ex)))
							.floatValue();
					AU = SMM * (1 - temp);// （7-6)
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
				RG[i] = s[i] * KGD * fr[i];// （7-5）
				RI[i] = RG[i] * KID / KGD;
				if (fr[i] < 0.001f)
					fr[i] = 0.001f;
				s[i + 1] = s[i] - (RG[i] + RI[i]) / fr[i]; // i+1????水量/面积=水深
				if (s[i + 1] < 0)
					s[i + 1] = 0;
				if (s[i + 1] > sm)
					s[i + 1] = sm;
			}
		}
		
		//TODO  参考C++ 消退计算
		for (int i = i_periods; i < i_periods + i_max_len; i++) {// 消退阶段
			fr[i] = fr[i - 1];
			RS[i] = 0;
			RG[i] = s[i] * KGD * fr[i];// （7-5）
			RI[i] = RG[i] * KID / KGD;// （7-5），rss[i]=kss[i]*s[i]*fr[i]
			s[i + 1] = s[i] - (RG[i] + RI[i]) / fr[i];
			if (s[i + 1] < 0)
				s[i + 1] = 0;
			if (s[i + 1] > sm)
				s[i + 1] = sm;
		}

		return i_periods + i_max_len;

	}

	/**
	 * 三水源新安江汇流计算模型--线形水库模型
	 * 
	 * @param i_step  步长
	 * @param i_periods
	 *            时段数
	 * @param area  流域面积
	 * @param RG
	 * @param RI
	 * @param RS
	 * @param xajParam
	 * @param dq
	 *            总径流
	 * @return
	 */
	public static int flow_xaj3_net(int i_step, int i_periods, float area,
			float[] RG, float[] RI, float[] RS, XajParam xajParam, float[] dq) {
		int i_floods;

		// float Im,sm,ex,kg,ki; //分水源计算模型参数,ex-表土自由水蓄水容量曲线的方次,k-自由水蓄水水库的出流系数

		float cs, ci, cg; // 汇流计算参数,消退系数和马斯京干参数.cs-河网蓄水量的消退系数
		float CSD, CID, CGD;

		// 从三水源模型结构变量中取得模型参数
		cs = xajParam.Cs;
		ci = xajParam.Ci;
		cg = xajParam.Cg;

		int d = 24 / i_step; // 一天分的时段数
		CSD = new Double(Math.pow(cs, 1 / d)).floatValue();// 每时段的参数值
		CID = new Double(Math.pow(ci, 1 / d)).floatValue();
		CGD = new Double(Math.pow(cg, 1 / d)).floatValue();

		float U = area / (3.6f * i_step); // 单位换算系数，将径流深转化为流量
		i_floods = i_periods + 7 * 24 / i_step;// +7天的时段
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
		}

		dq[0] = 0;
		for (int i = 1; i < i_floods; i++) // 总径流
		{
			dq[i] = QG[i - 1] + QI[i - 1] + QS[i - 1];
			if (dq[i] < 0)
				dq[i] = 0;//总出口流量
		}

		return i_floods;
	}

	/**
	 * 三水源新安江汇流计算模型--河网汇流模型
	 * 
	 * @param i_step  步长
	 * @param i_periods  时段数
	 * @param area  流域面积
	 * @param unitgraph  
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
		// float Im,sm,ex,kg,ki; //分水源计算模型参数,ex-表土自由水蓄水容量曲线的方次,k-自由水蓄水水库的出流系数

		float ci, cg, uh; // 汇流计算参数,消退系数和马斯京干参数.cs-河网蓄水量的消退系数
		float CID, CGD;
		int i_max_len = i_periods + i_point - 1 + 3 * 24 / i_step; // 包括消退时间，i_point—单位线的长度
		float QG[] = new float[i_max_len];
		float QI[] = new float[i_max_len];
		float QS[] = new float[i_max_len];

		// 从三水源模型结构变量中取得模型参数
		ci = xajParam.Ci;
		cg = xajParam.Cg;

		int d = 24 / i_step; // 一天分的时段数
		// CSD=new Double(Math.pow(cs,1/d)).floatValue();
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

		for (int i = 0; i < i_periods; i++) // 地面径流汇流采用单位线
		{

			for (int j = 0; j < i_point; j++) {
				QS[j + i] += RS[i] * unitgraph[j] * U;
			}
		}

		dq[0] = 0;
		for (int i = 1; i < i_max_len; i++) // 总径流
		{
			if (i < i_periods + i_point)// 有地面径流
			{
				dq[i] = QG[i] + QI[i] + QS[i];
			} else// 没有地面径流
			{
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
		if ((time_step >= 2 * k * x) && (time_step <= 2 * k - 2 * k * x)) // 马斯京干法适用范围判断（3-37）
		{
			float c0 = (0.5f * time_step - k * x)
					/ (k - k * x + 0.5f * time_step);
			float c1 = (k * x + 0.5f * time_step)
					/ (k - k * x + 0.5f * time_step);
			float c2 = (k - k * x - 0.5f * time_step)
					/ (k - k * x + 0.5f * time_step);

			for (int i = 0; i < n; i++) // 河段初始流
			{
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
						QI1 = inflow[i - 1]; // 数组越界
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
		} else {
			// 马斯京干法适用范围外
		}

		return dq_routing;

	}
	
	//test
	public static void main(String[] args) {
		XajParam x= new XajParam();
		
	}
}

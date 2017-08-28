package com.titan.ynsjy.statistics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.esri.core.geodatabase.GeodatabaseFeature;

import android.content.Context;

public class StatisticsUtil {

	public static StatisticsUtil statisticsUtil;

	public static StatisticsUtil getStatisticsUtil() {
		return statisticsUtil;
	}

	public static void setStatisticsUtil(StatisticsUtil statisticsUtil) {
		StatisticsUtil.statisticsUtil = statisticsUtil;
	}

	public synchronized static StatisticsUtil getInstance(Context context) {
		return statisticsUtil;
	}

	/** 获取 违法使用林地案件清单 数据 */
	public static ArrayList<LdqdAreaSum> getLdqdData(
			List<GeodatabaseFeature> list, String tjdwStr) {
		ArrayList<LdqdAreaSum> data = new ArrayList<LdqdAreaSum>();
		int size = 0;
		if (list != null) {
			size = list.size();
		}

		for (int i = 0; i < size; i++) {
			GeodatabaseFeature feature = list.get(i);
			LdqdAreaSum ldqd = new LdqdAreaSum();
			/** 统计单位 */
			ldqd.tjdw = tjdwStr;
			/** 案件编号 */
			ldqd.anjbh = "LD-" + (i + 1);
			/** 案件名称 */
			Object xmmc = feature.getAttributeValue("XMMC");
			if (xmmc != null) {
				ldqd.anjmc = xmmc.toString();
			} else {
				ldqd.anjmc = "";
			}
			/** 项目类别 */
			Object XMLB = feature.getAttributeValue("XMLB");
			if (XMLB != null) {
				ldqd.xmlb = XMLB.toString();
			} else {
				ldqd.xmlb = "";
			}
			/** 违法使用林地责任单位（责任人）名称（姓名） */
			Object ZRZT = feature.getAttributeValue("ZRZT");
			if (ZRZT != null) {
				ldqd.zrrmc = ZRZT.toString();
			} else {
				ldqd.zrrmc = "";
			}
			/** 责任单位法定代表人姓名 */
			ldqd.dbrmc = "";
			/** 涉及本次核实的疑似图斑编号 */
			Object TBBH = feature.getAttributeValue("TBBH");
			if (TBBH != null) {
				ldqd.ystbbh = TBBH.toString();
			} else {
				ldqd.ystbbh = "";
			}
			/** 涉及林地落界小班号 */
			Object XBH = feature.getAttributeValue("XBH");
			if (XBH != null) {
				ldqd.ljxbh = XBH.toString();
			} else {
				ldqd.ljxbh = "";
			}
			/** 开工日期 */
			Object PZSJ = feature.getAttributeValue("PZSJ");
			if (PZSJ != null) {
				ldqd.kgrq = PZSJ.toString();
			} else {
				ldqd.kgrq = "";
			}
			/** 违法使用林地面积（公顷） */
			if (list.get(i) != null ) {
				double a = list.get(i).getGeometry().calculateArea2D() / 10000;
				DecimalFormat df = new DecimalFormat("0.00");
				ldqd.wfsyldmj = Double.parseDouble(df.format(a));
			} else {
				ldqd.wfsyldmj = 0;
			}
			/** 违法使用林地地类 */
			Object LBDL = feature.getAttributeValue("LBDL");
			if (LBDL != null) {
				ldqd.lddl = LBDL.toString();
			} else {
				ldqd.lddl = "";
			}
			/** 违法使用林地的森林类别 */
			Object ForestType = feature.getAttributeValue("ForestType");
			if (ForestType != null) {
				ldqd.sllb = ForestType.toString();
			} else {
				ldqd.sllb = "";
			}
			/** 违法使用林地类型 */
			Object LDLX = feature.getAttributeValue("LDLX");
			if (LDLX != null) {
				ldqd.ldlx = LDLX.toString();
			} else {
				ldqd.ldlx = "";
			}
			/** 违法使用林地的行为类型 */
			Object WFXWLX = feature.getAttributeValue("WFXWLX");
			if (WFXWLX != null) {
				ldqd.ldxwlx = WFXWLX.toString();
			} else {
				ldqd.ldxwlx = "";
			}
			/** 违法使用林地行为的查处情况 */
			Object CCQK = feature.getAttributeValue("CCQK");
			if (CCQK != null) {
				ldqd.ccqk = CCQK.toString();
			} else {
				ldqd.ccqk = "";
			}
			/** 违法使用林地行为的依法处理情况及查处依据 */
			Object CCYJ_1 = feature.getAttributeValue("CCYJ_1");
			if (CCYJ_1 != null) {
				ldqd.clqk = CCYJ_1.toString();
			} else {
				ldqd.clqk = "";
			}
			/** 案件性质 */
			ldqd.anjxz = "";
			/** 有无无证采伐 */
			Object IFWZCF = feature.getAttributeValue("IFWZCF");
			if (IFWZCF != null) {
				ldqd.ywwzcf = IFWZCF.toString();
			} else {
				ldqd.ywwzcf = "";
			}
			/** 无证采伐面积(公顷） */
			Object CFMJ = feature.getAttributeValue("CFMJ");
			if (CFMJ != null && !CFMJ.toString().equals("")) {
				ldqd.wzcfmj = Double.parseDouble(CFMJ.toString());
			} else {
				ldqd.wzcfmj = 0;
			}
			/** 无证采伐蓄积（m3） */
			Object CFXJ = feature.getAttributeValue("CFXJ");
			if (CFXJ != null && !CFXJ.toString().equals("")) {
				ldqd.wzcfxj = Double.parseDouble(CFXJ.toString());
			} else {
				ldqd.wzcfxj = 0;
			}
			/** 无证采伐林木的查处情况 */
			Object CCQK_1 = feature.getAttributeValue("CCQK");
			if (CCQK_1 != null) {
				ldqd.wzccqk = CCQK_1.toString();
			} else {
				ldqd.wzccqk = "";
			}
			/** 督办级别 */
			ldqd.dbjb = "";
			/** 备注 */
			Object BEIZHU = feature.getAttributeValue("BEIZHU");
			if (BEIZHU != null) {
				ldqd.bz = BEIZHU.toString();
			} else {
				ldqd.bz = "";
			}
			data.add(ldqd);
		}
		return data;
	}

	public static ArrayList<LgyjAreaSum> showLgyjData(
			List<GeodatabaseFeature> list, String xiang_name, String cun_name,
			String tjdwStr) {

		ArrayList<LgyjAreaSum> data = new ArrayList<LgyjAreaSum>();
		if (list == null) {
			return data;
		}

		int size = list.size();
		// 合计小计
		LgyjAreaSum hjxj = new LgyjAreaSum();
		// 统计单位
		hjxj.tjdw = tjdwStr;
		// 乡镇
		String xiang = xiang_name;
		// 村、林班
		String cun = cun_name;
		// 合计 起数
		double hjqs = 0;
		// 合计面积
		double hjmj = 0;
		// 1、国家级、省级重点项目 起数
		double gjsjqs = 0;
		// 1、国家级、省级重点项目 面积
		double gjsjmj = 0;
		// 2、市（州）批准及招商引资项目 起数
		double shipzqs = 0;
		// 2、市（州）批准及招商引资项目 面积
		double shipzmj = 0;
		// 3、县（市、区、特区）批准的招商引资项目 起数
		double xianpzqs = 0;
		// 3、县（市、区、特区）批准的招商引资项目 面积
		double xianpzmj = 0;
		// 4、各级各类园区内建设项目 起数
		double gjyqqs = 0;
		// 4、各级各类园区内建设项目 面积
		double gjyqmj = 0;
		// 5、采矿、采石采砂取土及其他项目 起数
		double ckqtqs = 0;
		// 5、采矿、采石采砂取土及其他项目 面积
		double ckqtmj = 0;

		for (int i = 0; i < size; i++) {
			GeodatabaseFeature feature = list.get(i);

			Object obj = feature.getAttributeValue("XMLB");
			if (obj == null) {
				continue;
			}

			Object xiang_obj = feature.getAttributeValue("乡名称");
			Object cun_obj = feature.getAttributeValue("村名称");

			if (xiang_obj == null || cun_obj == null) {
				continue;
			}

			if (!cun_name.equals(cun_obj.toString()) && !cun_name.equals("合计")) {
				continue;
			}

			hjqs = hjqs + 1;
			hjmj = hjmj + feature.getGeometry().calculateArea2D() / 10000;

			String xmlb = obj.toString();
			if (xmlb.contains("国家级")) {
				gjsjqs = gjsjqs + 1;
				gjsjmj = gjsjmj + feature.getGeometry().calculateArea2D()
						/ 10000;
			} else if (xmlb.contains("省级")) {
				gjsjqs = gjsjqs + 1;
				gjsjmj = gjsjmj + feature.getGeometry().calculateArea2D()
						/ 10000;
			} else if (xmlb.contains("市（州）批准")) {
				shipzqs = shipzqs + 1;
				shipzmj = shipzmj + feature.getGeometry().calculateArea2D()
						/ 10000;
			} else if (xmlb.contains("县（市、区、特区）批准")) {
				xianpzqs = xianpzqs + 1;
				xianpzmj = xianpzmj + feature.getGeometry().calculateArea2D()
						/ 10000;
			} else if (xmlb.contains("各类园区")) {
				gjyqqs = gjyqqs + 1;
				gjyqmj = gjyqmj + feature.getGeometry().calculateArea2D()
						/ 10000;
			} else if (xmlb.contains("采矿、采石")) {
				ckqtqs = ckqtqs + 1;
				ckqtmj = ckqtmj + feature.getGeometry().calculateArea2D()
						/ 10000;
			} else if (xmlb.contains("其他")) {
				ckqtqs = ckqtqs + 1;
				ckqtmj = ckqtmj + feature.getGeometry().calculateArea2D()
						/ 10000;
			} else if (xmlb.contains("其它")) {
				ckqtqs = ckqtqs + 1;
				ckqtmj = ckqtmj + feature.getGeometry().calculateArea2D()
						/ 10000;
			}
		}
		hjxj.tjdw = tjdwStr;
		hjxj.xiang = xiang;
		hjxj.cun = cun;
		hjxj.hjqs = hjqs;
		hjxj.hjmj = hjmj;
		hjxj.gjsjzdqs = gjsjqs;
		hjxj.gjsjzdmj = gjsjmj;
		hjxj.shipzxmqs = shipzqs;
		hjxj.shipzxmmj = shipzmj;
		hjxj.xianzsqs = xianpzqs;
		hjxj.xianzsmj = xianpzmj;
		hjxj.gjnjxmqs = gjyqqs;
		hjxj.gjnjxmmj = gjyqmj;
		hjxj.ckcsxmqs = ckqtqs;
		hjxj.ckcsxmmj = ckqtmj;
		data.add(hjxj);
		return data;
	}

	/**
	 * 国有小班统计
	 *
	 * @param list
	 *            type 值 为国有(1 )、集体(2 )、其他 3
	 */
	public static ArrayList<LDAreaSum> showLDData_Guoyou(
			List<GeodatabaseFeature> list, String stype, String tjdwStr) {
		ArrayList<LDAreaSum> data = new ArrayList<>();
		if (list == null) {
			return data;
		}
		ArrayList<String> list_string = new ArrayList<String>();
		String type = "";
		if (stype.equals("1")) {
			list_string.add("10");
			type = "国有";
		} else if (stype.equals("2")) {
			list_string.add("21");
			list_string.add("22");
			list_string.add("23");
			type = "集体";
		} else {
			// 这种情况是 林地权属为null时
			list_string.add("10");
			list_string.add("21");
			list_string.add("22");
			list_string.add("23");
			type = "其他";
		}

		int size = list.size();
		// 合计小计
		LDAreaSum hjxj = new LDAreaSum();
		// 统计单位

		hjxj.tjdw = tjdwStr;
		// 林地所有权
		hjxj.ldsyq = type;
		// 森林类别
		hjxj.sllb = "小计";
		// 总面积
		double hjxj_zmj = 0;
		// 林业用地合计
		double hjxj_lyyd_hj = 0;
		// 有林地小计
		double hjxj_yld_xj = 0;
		// 乔木林
		double hjxj_qml = 0;
		// 竹林
		double hjxj_zl = 0;
		// 疏林地
		double hjxj_sld = 0;
		// 灌木林地小计
		double hjxj_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_gmjjl = 0;
		// 其他灌木林地
		double hjxj_qtgmld = 0;
		// 未成林造地小計
		double hjxj_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_rgzlwcld = 0;
		// 封育未成林地
		double hjxj_fywcld = 0;
		// 苗圃地
		double hjxj_mpd = 0;
		// 无立木林地小计
		double hjxj_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_cfjd = 0;
		// 火烧迹地
		double hjxj_hsjd = 0;
		// 其他无立木林地
		double hjxj_qtwlmld_xj = 0;
		// 林业用地 宜林地小计
		double hjxj_yild_xj = 0;
		// 荒山荒地
		double hjxj_hshd = 0;
		// 沙荒地
		double hjxj_shd = 0;
		// 其他林宜地
		double hjxj_qtyld = 0;
		// 辅助生产林地
		double hjxj_fzscld = 0;
		// 非林业用地合计
		double hjxj_flyyd_hj = 0;
		// 非林业用地 有林地
		double hjxj_flyyd_yld = 0;
		// 非林业灌木林地小计
		double hjxj_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_fly_qtgmld = 0;

		for (int i = 0; i < size; i++) {

			// 总面积
			if (list.get(i).getAttributeValue("MIAN_JI") == null) {
				continue;
			}
			if (type.equals("其他")) {
				if (list.get(i).getAttributeValue("LD_QS") == null
						|| !list_string.contains(list.get(i)
								.getAttributeValue("LD_QS").toString())) {

					if (list.get(i).getAttributeValue("GLLX") != null) {

						if (list.get(i).getAttributeValue("DI_LEI") == null) {
							continue;
						}

						hjxj_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

						// 林业用地合计
						if (list.get(i).getAttributeValue("GLLX").equals("10")) {

							hjxj_lyyd_hj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("011"))
								hjxj_yld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0111"))
								// 乔木林
								hjxj_qml += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0113"))
								// 竹林
								hjxj_zl += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0120"))
								// 疏林地
								hjxj_sld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 灌木林地小计
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("013"))

								hjxj_gmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0131")) {
								// 国家特别灌木林小计
								hjxj_gjtbgmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							}

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0131")) {
								if (list.get(i).getAttributeValue("LIN_ZHONG") != null
										&& list.get(i)
												.getAttributeValue("LIN_ZHONG")
												.toString().startsWith("25")) {
									// 灌木经济林
									hjxj_gmjjl += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}
							}

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0132"))
								// 其他灌木林地
								hjxj_qtgmld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 未成林造林統計
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("014"))
								hjxj_wclzl_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 人工造林未成林地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0141"))
								hjxj_rgzlwcld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0142"))
								hjxj_fywcld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0150"))
								// 苗圃地
								hjxj_mpd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("016"))
								hjxj_wlmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0161"))
								// 采伐迹地
								hjxj_cfjd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0162"))
								// 火烧迹地
								hjxj_hsjd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 其他无立木林地
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("163"))
								hjxj_qtwlmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							// 宜林地小计
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("017"))
								hjxj_yild_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							// 荒山荒地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0171"))
								hjxj_hshd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 沙荒地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0172"))

								hjxj_shd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 其他林宜地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0173"))

								hjxj_qtyld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 辅助生产林地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0180"))

								hjxj_fzscld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

						} else {// 非林业用地合计

							hjxj_flyyd_hj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("011")) {
								hjxj_flyyd_yld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							}
							// 灌木林地小计
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("013"))
								hjxj_fly_gmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0131"))
								// 国家特别灌木林
								hjxj_fly_gjtbgdgmld += Double.valueOf(list
										.get(i).getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0132"))
								// 其他灌木林地
								hjxj_fly_qtgmld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
						}
					}
				}
			} else {
				if (list.get(i).getAttributeValue("LD_QS") != null
						&& list_string.contains(list.get(i)
								.getAttributeValue("LD_QS").toString())) {

					if (list.get(i).getAttributeValue("GLLX") != null) {

						hjxj_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("DI_LEI") == null) {
							continue;
						}
						// 林业用地合计
						if (list.get(i).getAttributeValue("GLLX").equals("10")) {

							hjxj_lyyd_hj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("011"))
								hjxj_yld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0111"))
								// 乔木林
								hjxj_qml += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0113"))
								// 竹林
								hjxj_zl += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0120"))
								// 疏林地
								hjxj_sld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 灌木林地小计
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("013"))

								hjxj_gmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0131")) {
								// 国家特别灌木林小计
								hjxj_gjtbgmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							}

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0131")) {
								if (list.get(i).getAttributeValue("LIN_ZHONG") != null
										&& list.get(i)
												.getAttributeValue("LIN_ZHONG")
												.toString().startsWith("25")) {
									// 灌木经济林
									hjxj_gmjjl += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}
							}

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0132"))
								// 其他灌木林地
								hjxj_qtgmld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 未成林造林統計
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("014"))
								hjxj_wclzl_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 人工造林未成林地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0141"))
								hjxj_rgzlwcld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0142"))
								hjxj_fywcld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0150"))
								// 苗圃地
								hjxj_mpd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("016"))
								hjxj_wlmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0161"))
								// 采伐迹地
								hjxj_cfjd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0162"))
								// 火烧迹地
								hjxj_hsjd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 其他无立木林地
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("163"))
								hjxj_qtwlmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							// 宜林地小计
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("017"))
								hjxj_yild_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							// 荒山荒地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0171"))
								hjxj_hshd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 沙荒地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0172"))

								hjxj_shd += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 其他林宜地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0173"))

								hjxj_qtyld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							// 辅助生产林地
							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0180"))

								hjxj_fzscld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

						} else {// 非林业用地合计

							hjxj_flyyd_hj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("011")) {
								hjxj_flyyd_yld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							}
							// 灌木林地小计
							if (list.get(i).getAttributeValue("DI_LEI")
									.toString().startsWith("013"))
								hjxj_fly_gmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0131"))
								// 国家特别灌木林
								hjxj_fly_gjtbgdgmld += Double.valueOf(list
										.get(i).getAttributeValue("MIAN_JI")
										.toString());

							if (list.get(i).getAttributeValue("DI_LEI")
									.equals("0132"))
								// 其他灌木林地
								hjxj_fly_qtgmld += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
						}
					}
				}
			}
		}
		hjxj.zmj = "" + hjxj_zmj;
		hjxj.lyyd_hj = "" + hjxj_lyyd_hj;
		hjxj.yld_xj = "" + hjxj_yld_xj;
		hjxj.qml = "" + hjxj_qml;
		hjxj.zl = "" + hjxj_zl;
		hjxj.sld = "" + hjxj_sld;
		hjxj.gmld_xj = "" + hjxj_gmld_xj;
		hjxj.gjtbgmld_xj = "" + hjxj_gjtbgmld_xj;
		hjxj.gmjjl = "" + hjxj_gmjjl;
		hjxj.qtgmld = "" + hjxj_qtgmld;
		hjxj.wclzl_xj = "" + hjxj_wclzl_xj;
		hjxj.rgzlwclzld = "" + hjxj_rgzlwcld;
		hjxj.fywcld = "" + hjxj_fywcld;
		hjxj.mpd = "" + hjxj_mpd;
		hjxj.wlmld_xj = "" + hjxj_wlmld_xj;
		hjxj.cfjd = "" + hjxj_cfjd;
		hjxj.hsjd = "" + hjxj_hsjd;
		hjxj.qtwlmld = "" + hjxj_qtwlmld_xj;
		hjxj.yildxj = "" + hjxj_yild_xj;
		hjxj.hshd = "" + hjxj_hshd;
		hjxj.shd = "" + hjxj_shd;
		hjxj.qtyld = "" + hjxj_qtyld;
		hjxj.fzscld = "" + hjxj_fzscld;
		hjxj.flyyd_hj = "" + hjxj_flyyd_hj;
		hjxj.fly_yld = "" + hjxj_flyyd_yld;
		// 非林业灌木林地小计
		hjxj.fly_gmld_xj = "" + hjxj_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj.fly_gjtbgdgmld = "" + hjxj_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj.fly_qtgmld = "" + hjxj_fly_qtgmld;
		data.add(hjxj);

		// 合计小计_公益林
		LDAreaSum hjxj_gyl = new LDAreaSum();
		// 统计单位
		hjxj_gyl.tjdw = tjdwStr;
		// 林地使用权
		hjxj_gyl.ldsyq = type;
		// 森林类别
		hjxj_gyl.sllb = "公益林地";
		// 总面积
		double hjxj_gyl_zmj = 0;
		// 林业用地合计
		double hjxj_gyl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_gyl_yld_xj = 0;
		// 乔木林
		double hjxj_gyl_qml = 0;
		// 竹林
		double hjxj_gyl_zl = 0;
		// 疏林地
		double hjxj_gyl_sld = 0;
		// 灌木林地小计
		double hjxj_gyl_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gyl_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_gyl_gmjjl = 0;
		// 其他灌木林地
		double hjxj_gyl_qtgmld = 0;
		// 未成林造地小計
		double hjxj_gyl_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_gyl_rgzlwcld = 0;
		// 封育未成林地
		double hjxj_gyl_fywcld = 0;
		// 苗圃地
		double hjxj_gyl_mpd = 0;
		// 无立木林地小计
		double hjxj_gyl_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_gyl_cfjd = 0;
		// 火烧迹地
		double hjxj_gyl_hsjd = 0;
		// 其他无立木林地
		double hjxj_gyl_qtwlmld_xj = 0;
		// 林业用地 宜林地小计
		double hjxj_gyl_yild_xj = 0;
		// 荒山荒地
		double hjxj_gyl_hshd = 0;
		// 沙荒地
		double hjxj_gyl_shd = 0;
		// 其他林宜地
		double hjxj_gyl_qtyld = 0;
		// 辅助生产林地
		double hjxj_gyl_fzscld = 0;
		// 非林业用地合计
		double hjxj_gyl_flyyd_hj = 0;
		// 非林业用地 有林地
		double hjxj_gyl_flyyd_yld = 0;
		// 非林业灌木林地小计
		double hjxj_gyl_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_gyl_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_gyl_fly_qtgmld = 0;

		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("MIAN_JI") == null) {
				continue;
			}

			if (type.equals("其他")) {
				if (list.get(i).getAttributeValue("LD_QS") == null
						|| !list_string.contains(list.get(i)
								.getAttributeValue("LD_QS").toString())) {

					if (list.get(i).getAttributeValue("SEN_LIN_LB") != null
							&& list.get(i).getAttributeValue("SEN_LIN_LB")
									.equals("011")) {

						hjxj_gyl_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("GLLX") != null) {

							if (list.get(i).getAttributeValue("DI_LEI") == null) {
								continue;
							}
							// 林业用地合计
							if (list.get(i).getAttributeValue("GLLX")
									.equals("10")) {

								hjxj_gyl_lyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011"))
									hjxj_gyl_yld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0111"))
									// 乔木林
									hjxj_gyl_qml += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0113"))
									// 竹林
									hjxj_gyl_zl += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0120"))
									// 疏林地
									hjxj_gyl_sld += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))

									hjxj_gyl_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									// 国家特别灌木林小计
									hjxj_gyl_gjtbgmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									if (list.get(i).getAttributeValue(
											"LIN_ZHONG") != null
											&& list.get(i)
													.getAttributeValue(
															"LIN_ZHONG")
													.toString()
													.startsWith("25")) {
										// 灌木经济林
										hjxj_gyl_gmjjl += Double.valueOf(list
												.get(i)
												.getAttributeValue("MIAN_JI")
												.toString());
									}
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_gyl_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 未成林造林統計
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("014"))
									hjxj_gyl_wclzl_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 人工造林未成林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0141"))
									hjxj_gyl_rgzlwcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0142"))
									hjxj_gyl_fywcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0150"))
									// 苗圃地
									hjxj_gyl_mpd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("016"))
									hjxj_gyl_wlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0161"))
									// 采伐迹地
									hjxj_gyl_cfjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0162"))
									// 火烧迹地
									hjxj_gyl_hsjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他无立木林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("163"))
									hjxj_gyl_qtwlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 宜林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("017"))
									hjxj_gyl_yild_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 荒山荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0171"))
									hjxj_gyl_hshd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 沙荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0172"))

									hjxj_gyl_shd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他林宜地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0173"))

									hjxj_gyl_qtyld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 辅助生产林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0180"))

									hjxj_gyl_fzscld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

							} else {// 非林业用地合计

								hjxj_gyl_flyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011")) {
									hjxj_gyl_flyyd_yld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))
									hjxj_gyl_fly_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131"))
									// 国家特别灌木林
									hjxj_gyl_fly_gjtbgdgmld += Double
											.valueOf(list
													.get(i)
													.getAttributeValue(
															"MIAN_JI")
													.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_gyl_fly_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
							}
						}
					}
				}
			} else {
				if (list.get(i).getAttributeValue("LD_QS") != null
						&& list_string.contains(list.get(i)
								.getAttributeValue("LD_QS").toString())) {

					if (list.get(i).getAttributeValue("SEN_LIN_LB") != null
							&& list.get(i).getAttributeValue("SEN_LIN_LB")
									.equals("011")) {

						hjxj_gyl_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("GLLX") != null) {

							if (list.get(i).getAttributeValue("DI_LEI") == null) {
								continue;
							}
							// 林业用地合计
							if (list.get(i).getAttributeValue("GLLX")
									.equals("10")) {

								hjxj_gyl_lyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011"))
									hjxj_gyl_yld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0111"))
									// 乔木林
									hjxj_gyl_qml += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0113"))
									// 竹林
									hjxj_gyl_zl += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0120"))
									// 疏林地
									hjxj_gyl_sld += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))

									hjxj_gyl_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									// 国家特别灌木林小计
									hjxj_gyl_gjtbgmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									if (list.get(i).getAttributeValue(
											"LIN_ZHONG") != null
											&& list.get(i)
													.getAttributeValue(
															"LIN_ZHONG")
													.toString()
													.startsWith("25")) {
										// 灌木经济林
										hjxj_gyl_gmjjl += Double.valueOf(list
												.get(i)
												.getAttributeValue("MIAN_JI")
												.toString());
									}
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_gyl_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 未成林造林統計
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("014"))
									hjxj_gyl_wclzl_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 人工造林未成林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0141"))
									hjxj_gyl_rgzlwcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0142"))
									hjxj_gyl_fywcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0150"))
									// 苗圃地
									hjxj_gyl_mpd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("016"))
									hjxj_gyl_wlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0161"))
									// 采伐迹地
									hjxj_gyl_cfjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0162"))
									// 火烧迹地
									hjxj_gyl_hsjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他无立木林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("163"))
									hjxj_gyl_qtwlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 宜林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("017"))
									hjxj_gyl_yild_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 荒山荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0171"))
									hjxj_gyl_hshd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 沙荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0172"))
									hjxj_gyl_shd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他林宜地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0173"))

									hjxj_gyl_qtyld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 辅助生产林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0180"))

									hjxj_gyl_fzscld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
							} else {// 非林业用地合计

								hjxj_gyl_flyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011")) {
									hjxj_gyl_flyyd_yld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))
									hjxj_gyl_fly_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131"))
									// 国家特别灌木林
									hjxj_gyl_fly_gjtbgdgmld += Double
											.valueOf(list
													.get(i)
													.getAttributeValue(
															"MIAN_JI")
													.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_gyl_fly_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
							}
						}
					}
				}
			}
		}
		hjxj_gyl.zmj = "" + hjxj_gyl_zmj;
		hjxj_gyl.lyyd_hj = "" + hjxj_gyl_lyyd_hj;
		hjxj_gyl.yld_xj = "" + hjxj_gyl_yld_xj;
		hjxj_gyl.qml = "" + hjxj_gyl_qml;
		hjxj_gyl.zl = "" + hjxj_gyl_zl;
		hjxj_gyl.sld = "" + hjxj_gyl_sld;
		hjxj_gyl.gmld_xj = "" + hjxj_gyl_gmld_xj;
		hjxj_gyl.gjtbgmld_xj = "" + hjxj_gyl_gjtbgmld_xj;
		hjxj_gyl.gmjjl = "" + hjxj_gyl_gmjjl;
		hjxj_gyl.qtgmld = "" + hjxj_gyl_qtgmld;
		hjxj_gyl.wclzl_xj = "" + hjxj_gyl_wclzl_xj;
		hjxj_gyl.rgzlwclzld = "" + hjxj_gyl_rgzlwcld;
		hjxj_gyl.fywcld = "" + hjxj_gyl_fywcld;
		hjxj_gyl.mpd = "" + hjxj_gyl_mpd;
		hjxj_gyl.wlmld_xj = "" + hjxj_gyl_wlmld_xj;
		hjxj_gyl.cfjd = "" + hjxj_gyl_cfjd;
		hjxj_gyl.hsjd = "" + hjxj_gyl_hsjd;
		hjxj_gyl.qtwlmld = "" + hjxj_gyl_qtwlmld_xj;
		hjxj_gyl.yildxj = "" + hjxj_gyl_yild_xj;
		hjxj_gyl.hshd = "" + hjxj_gyl_hshd;
		hjxj_gyl.shd = "" + hjxj_gyl_shd;
		hjxj_gyl.qtyld = "" + hjxj_gyl_qtyld;
		hjxj_gyl.fzscld = "" + hjxj_gyl_fzscld;
		hjxj_gyl.flyyd_hj = "" + hjxj_gyl_flyyd_hj;
		hjxj_gyl.fly_yld = "" + hjxj_gyl_flyyd_yld;
		// 非林业灌木林地小计
		hjxj_gyl.fly_gmld_xj = "" + hjxj_gyl_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_gyl.fly_gjtbgdgmld = "" + hjxj_gyl_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_gyl.fly_qtgmld = "" + hjxj_gyl_fly_qtgmld;
		data.add(hjxj_gyl);

		// 合计小计_商品林
		LDAreaSum hjxj_spl = new LDAreaSum();
		// 统计单位
		hjxj_spl.tjdw = tjdwStr;
		// 林地使用权
		hjxj_spl.ldsyq = type;
		// 森林类别
		hjxj_spl.sllb = "商品林地";
		// 总面积
		double hjxj_spl_zmj = 0;
		// 林业用地合计
		double hjxj_spl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_spl_yld_xj = 0;
		// 乔木林
		double hjxj_spl_qml = 0;
		// 竹林
		double hjxj_spl_zl = 0;
		// 疏林地
		double hjxj_spl_sld = 0;
		// 灌木林地小计
		double hjxj_spl_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_spl_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_spl_gmjjl = 0;
		// 其他灌木林地
		double hjxj_spl_qtgmld = 0;
		// 未成林造地小計
		double hjxj_spl_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_spl_rgzlwcld = 0;
		// 封育未成林地
		double hjxj_spl_fywcld = 0;
		// 苗圃地
		double hjxj_spl_mpd = 0;
		// 无立木林地小计
		double hjxj_spl_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_spl_cfjd = 0;
		// 火烧迹地
		double hjxj_spl_hsjd = 0;
		// 其他无立木林地
		double hjxj_spl_qtwlmld_xj = 0;
		// 林业用地 宜林地小计
		double hjxj_spl_yild_xj = 0;
		// 荒山荒地
		double hjxj_spl_hshd = 0;
		// 沙荒地
		double hjxj_spl_shd = 0;
		// 其他林宜地
		double hjxj_spl_qtyld = 0;
		// 辅助生产林地
		double hjxj_spl_fzscld = 0;
		// 非林业用地合计
		double hjxj_spl_flyyd_hj = 0;
		// 非林业用地 有林地
		double hjxj_spl_flyyd_yld = 0;
		// 非林业灌木林地小计
		double hjxj_spl_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_spl_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_spl_fly_qtgmld = 0;

		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("MIAN_JI") == null) {
				continue;
			}

			if (type.equals("其他")) {
				if (list.get(i).getAttributeValue("LD_QS") == null
						|| !list_string.contains(list.get(i)
								.getAttributeValue("LD_QS").toString())) {

					if (list.get(i).getAttributeValue("SEN_LIN_LB") != null
							&& list.get(i).getAttributeValue("SEN_LIN_LB")
									.equals("012")) {

						hjxj_spl_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("GLLX") != null) {

							if (list.get(i).getAttributeValue("DI_LEI") == null) {
								continue;
							}
							// 林业用地合计
							if (list.get(i).getAttributeValue("GLLX")
									.equals("10")) {

								hjxj_spl_lyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011"))
									hjxj_spl_yld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0111"))
									// 乔木林
									hjxj_spl_qml += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0113"))
									// 竹林
									hjxj_spl_zl += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0120"))
									// 疏林地
									hjxj_spl_sld += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))

									hjxj_spl_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									// 国家特别灌木林小计
									hjxj_spl_gjtbgmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									if (list.get(i).getAttributeValue(
											"LIN_ZHONG") != null
											&& list.get(i)
													.getAttributeValue(
															"LIN_ZHONG")
													.toString()
													.startsWith("25")) {
										// 灌木经济林
										hjxj_spl_gmjjl += Double.valueOf(list
												.get(i)
												.getAttributeValue("MIAN_JI")
												.toString());
									}
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_spl_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 未成林造林統計
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("014"))
									hjxj_spl_wclzl_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 人工造林未成林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0141"))
									hjxj_spl_rgzlwcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0142"))
									hjxj_spl_fywcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0150"))
									// 苗圃地
									hjxj_spl_mpd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("016"))
									hjxj_spl_wlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0161"))
									// 采伐迹地
									hjxj_spl_cfjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0162"))
									// 火烧迹地
									hjxj_spl_hsjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他无立木林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("163"))
									hjxj_spl_qtwlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 宜林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("017"))
									hjxj_spl_yild_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 荒山荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0171"))
									hjxj_spl_hshd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 沙荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0172"))

									hjxj_spl_shd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他林宜地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0173"))

									hjxj_spl_qtyld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 辅助生产林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0180"))

									hjxj_spl_fzscld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

							} else {// 非林业用地合计

								hjxj_spl_flyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011")) {
									hjxj_spl_flyyd_yld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))
									hjxj_spl_fly_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131"))
									// 国家特别灌木林
									hjxj_spl_fly_gjtbgdgmld += Double
											.valueOf(list
													.get(i)
													.getAttributeValue(
															"MIAN_JI")
													.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_spl_fly_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
							}
						}
					}

				}
			} else {
				if (list.get(i).getAttributeValue("LD_QS") != null
						&& list_string.contains(list.get(i)
								.getAttributeValue("LD_QS").toString())) {
					if (list.get(i).getAttributeValue("SEN_LIN_LB") != null
							&& list.get(i).getAttributeValue("SEN_LIN_LB")
									.equals("012")) {

						hjxj_spl_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("GLLX") != null) {

							if (list.get(i).getAttributeValue("DI_LEI") == null) {
								continue;
							}
							// 林业用地合计
							if (list.get(i).getAttributeValue("GLLX")
									.equals("10")) {

								hjxj_spl_lyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011"))
									hjxj_spl_yld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0111"))
									// 乔木林
									hjxj_spl_qml += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0113"))
									// 竹林
									hjxj_spl_zl += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0120"))
									// 疏林地
									hjxj_spl_sld += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))

									hjxj_spl_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									// 国家特别灌木林小计
									hjxj_spl_gjtbgmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									if (list.get(i).getAttributeValue(
											"LIN_ZHONG") != null
											&& list.get(i)
													.getAttributeValue(
															"LIN_ZHONG")
													.toString()
													.startsWith("25")) {
										// 灌木经济林
										hjxj_spl_gmjjl += Double.valueOf(list
												.get(i)
												.getAttributeValue("MIAN_JI")
												.toString());
									}
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_spl_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 未成林造林統計
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("014"))
									hjxj_spl_wclzl_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 人工造林未成林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0141"))
									hjxj_spl_rgzlwcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0142"))
									hjxj_spl_fywcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0150"))
									// 苗圃地
									hjxj_spl_mpd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("016"))
									hjxj_spl_wlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0161"))
									// 采伐迹地
									hjxj_spl_cfjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0162"))
									// 火烧迹地
									hjxj_spl_hsjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他无立木林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("163"))
									hjxj_spl_qtwlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 宜林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("017"))
									hjxj_spl_yild_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 荒山荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0171"))
									hjxj_spl_hshd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 沙荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0172"))

									hjxj_spl_shd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他林宜地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0173"))

									hjxj_spl_qtyld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 辅助生产林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0180"))

									hjxj_spl_fzscld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

							} else {// 非林业用地合计

								hjxj_spl_flyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011")) {
									hjxj_spl_flyyd_yld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))
									hjxj_spl_fly_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131"))
									// 国家特别灌木林
									hjxj_spl_fly_gjtbgdgmld += Double
											.valueOf(list
													.get(i)
													.getAttributeValue(
															"MIAN_JI")
													.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_spl_fly_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
							}
						}
					}
				}
			}
		}
		hjxj_spl.zmj = "" + hjxj_spl_zmj;
		hjxj_spl.lyyd_hj = "" + hjxj_spl_lyyd_hj;
		hjxj_spl.yld_xj = "" + hjxj_spl_yld_xj;
		hjxj_spl.qml = "" + hjxj_spl_qml;
		hjxj_spl.zl = "" + hjxj_spl_zl;
		hjxj_spl.sld = "" + hjxj_spl_sld;
		hjxj_spl.gmld_xj = "" + hjxj_spl_gmld_xj;
		hjxj_spl.gjtbgmld_xj = "" + hjxj_spl_gjtbgmld_xj;
		hjxj_spl.gmjjl = "" + hjxj_spl_gmjjl;
		hjxj_spl.qtgmld = "" + hjxj_spl_qtgmld;
		hjxj_spl.wclzl_xj = "" + hjxj_spl_wclzl_xj;
		hjxj_spl.rgzlwclzld = "" + hjxj_spl_rgzlwcld;
		hjxj_spl.fywcld = "" + hjxj_spl_fywcld;
		hjxj_spl.mpd = "" + hjxj_spl_mpd;
		hjxj_spl.wlmld_xj = "" + hjxj_spl_wlmld_xj;
		hjxj_spl.cfjd = "" + hjxj_spl_cfjd;
		hjxj_spl.hsjd = "" + hjxj_spl_hsjd;
		hjxj_spl.qtwlmld = "" + hjxj_spl_qtwlmld_xj;
		hjxj_spl.yildxj = "" + hjxj_spl_yild_xj;
		hjxj_spl.hshd = "" + hjxj_spl_hshd;
		hjxj_spl.shd = "" + hjxj_spl_shd;
		hjxj_spl.qtyld = "" + hjxj_spl_qtyld;
		hjxj_spl.fzscld = "" + hjxj_spl_fzscld;
		hjxj_spl.flyyd_hj = "" + hjxj_spl_flyyd_hj;
		hjxj_spl.fly_yld = "" + hjxj_spl_flyyd_yld;
		// 非林业灌木林地小计
		hjxj_spl.fly_gmld_xj = "" + hjxj_spl_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_spl.fly_gjtbgdgmld = "" + hjxj_spl_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_spl.fly_qtgmld = "" + hjxj_spl_fly_qtgmld;
		data.add(hjxj_spl);

		// 合计小计_其他
		LDAreaSum hjxj_qt = new LDAreaSum();
		// 统计单位
		hjxj_qt.tjdw = tjdwStr;
		// 林地使用权
		hjxj_qt.ldsyq = type;
		// 森林类别
		hjxj_qt.sllb = "其他";
		// 总面积
		double hjxj_qt_zmj = 0;
		// 林业用地合计
		double hjxj_qt_lyyd_hj = 0;
		// 有林地小计
		double hjxj_qt_yld_xj = 0;
		// 乔木林
		double hjxj_qt_qml = 0;
		// 竹林
		double hjxj_qt_zl = 0;
		// 疏林地
		double hjxj_qt_sld = 0;
		// 灌木林地小计
		double hjxj_qt_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_qt_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_qt_gmjjl = 0;
		// 其他灌木林地
		double hjxj_qt_qtgmld = 0;
		// 未成林造地小計
		double hjxj_qt_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_qt_rgzlwcld = 0;
		// 封育未成林地
		double hjxj_qt_fywcld = 0;
		// 苗圃地
		double hjxj_qt_mpd = 0;
		// 无立木林地小计
		double hjxj_qt_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_qt_cfjd = 0;
		// 火烧迹地
		double hjxj_qt_hsjd = 0;
		// 其他无立木林地
		double hjxj_qt_qtwlmld_xj = 0;
		// 林业用地 宜林地小计
		double hjxj_qt_yild_xj = 0;
		// 荒山荒地
		double hjxj_qt_hshd = 0;
		// 沙荒地
		double hjxj_qt_shd = 0;
		// 其他林宜地
		double hjxj_qt_qtyld = 0;
		// 辅助生产林地
		double hjxj_qt_fzscld = 0;
		// 非林业用地合计
		double hjxj_qt_flyyd_hj = 0;
		// 非林业用地 有林地
		double hjxj_qt_flyyd_yld = 0;
		// 非林业灌木林地小计
		double hjxj_qt_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_qt_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_qt_fly_qtgmld = 0;

		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("MIAN_JI") == null) {
				continue;
			}
			if (type.equals("其他")) {
				if (list.get(i).getAttributeValue("LD_QS") == null
						|| !list_string.contains(list.get(i)
								.getAttributeValue("LD_QS").toString())) {

					if (list.get(i).getAttributeValue("SEN_LIN_LB") == null
							|| !(list.get(i).getAttributeValue("SEN_LIN_LB")
									.equals("011") || list.get(i)
									.getAttributeValue("SEN_LIN_LB")
									.equals("012"))) {

						hjxj_qt_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("GLLX") != null) {

							if (list.get(i).getAttributeValue("DI_LEI") == null) {
								continue;
							}
							// 林业用地合计
							if (list.get(i).getAttributeValue("GLLX")
									.equals("10")) {

								hjxj_qt_lyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011"))
									hjxj_qt_yld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0111"))
									// 乔木林
									hjxj_qt_qml += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0113"))
									// 竹林
									hjxj_qt_zl += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0120"))
									// 疏林地
									hjxj_qt_sld += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))

									hjxj_qt_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									// 国家特别灌木林小计
									hjxj_qt_gjtbgmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									if (list.get(i).getAttributeValue(
											"LIN_ZHONG") != null
											&& list.get(i)
													.getAttributeValue(
															"LIN_ZHONG")
													.toString()
													.startsWith("25")) {
										// 灌木经济林
										hjxj_qt_gmjjl += Double.valueOf(list
												.get(i)
												.getAttributeValue("MIAN_JI")
												.toString());
									}
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_qt_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 未成林造林統計
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("014"))
									hjxj_qt_wclzl_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 人工造林未成林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0141"))
									hjxj_qt_rgzlwcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0142"))
									hjxj_qt_fywcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0150"))
									// 苗圃地
									hjxj_qt_mpd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("016"))
									hjxj_qt_wlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0161"))
									// 采伐迹地
									hjxj_qt_cfjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0162"))
									// 火烧迹地
									hjxj_qt_hsjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他无立木林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("163"))
									hjxj_qt_qtwlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 宜林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("017"))
									hjxj_qt_yild_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 荒山荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0171"))
									hjxj_qt_hshd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 沙荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0172"))

									hjxj_qt_shd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他林宜地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0173"))

									hjxj_qt_qtyld += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 辅助生产林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0180"))

									hjxj_qt_fzscld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

							} else {// 非林业用地合计

								hjxj_qt_flyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011")) {
									hjxj_qt_flyyd_yld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))
									hjxj_qt_fly_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131"))
									// 国家特别灌木林
									hjxj_qt_fly_gjtbgdgmld += Double
											.valueOf(list
													.get(i)
													.getAttributeValue(
															"MIAN_JI")
													.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_qt_fly_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
							}
						}
					}
				}
			} else {
				if (list.get(i).getAttributeValue("LD_QS") != null
						&& list_string.contains(list.get(i)
								.getAttributeValue("LD_QS").toString())) {

					if (list.get(i).getAttributeValue("SEN_LIN_LB") == null
							|| !(list.get(i).getAttributeValue("SEN_LIN_LB")
									.equals("011") || list.get(i)
									.getAttributeValue("SEN_LIN_LB")
									.equals("012"))) {

						hjxj_qt_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("GLLX") != null) {

							if (list.get(i).getAttributeValue("DI_LEI") == null) {
								continue;
							}
							// 林业用地合计
							if (list.get(i).getAttributeValue("GLLX")
									.equals("10")) {

								hjxj_qt_lyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011"))
									hjxj_qt_yld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0111"))
									// 乔木林
									hjxj_qt_qml += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0113"))
									// 竹林
									hjxj_qt_zl += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0120"))
									// 疏林地
									hjxj_qt_sld += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))

									hjxj_qt_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									// 国家特别灌木林小计
									hjxj_qt_gjtbgmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131")) {
									if (list.get(i).getAttributeValue(
											"LIN_ZHONG") != null
											&& list.get(i)
													.getAttributeValue(
															"LIN_ZHONG")
													.toString()
													.startsWith("25")) {
										// 灌木经济林
										hjxj_qt_gmjjl += Double.valueOf(list
												.get(i)
												.getAttributeValue("MIAN_JI")
												.toString());
									}
								}

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_qt_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 未成林造林統計
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("014"))
									hjxj_qt_wclzl_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 人工造林未成林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0141"))
									hjxj_qt_rgzlwcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0142"))
									hjxj_qt_fywcld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0150"))
									// 苗圃地
									hjxj_qt_mpd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("016"))
									hjxj_qt_wlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0161"))
									// 采伐迹地
									hjxj_qt_cfjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0162"))
									// 火烧迹地
									hjxj_qt_hsjd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他无立木林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("163"))
									hjxj_qt_qtwlmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 宜林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("017"))
									hjxj_qt_yild_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								// 荒山荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0171"))
									hjxj_qt_hshd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 沙荒地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0172"))

									hjxj_qt_shd += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 其他林宜地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0173"))

									hjxj_qt_qtyld += Double.valueOf(list.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								// 辅助生产林地
								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0180"))

									hjxj_qt_fzscld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

							} else {// 非林业用地合计

								hjxj_qt_flyyd_hj += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("011")) {
									hjxj_qt_flyyd_yld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
								}
								// 灌木林地小计
								if (list.get(i).getAttributeValue("DI_LEI")
										.toString().startsWith("013"))
									hjxj_qt_fly_gmld_xj += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0131"))
									// 国家特别灌木林
									hjxj_qt_fly_gjtbgdgmld += Double
											.valueOf(list
													.get(i)
													.getAttributeValue(
															"MIAN_JI")
													.toString());

								if (list.get(i).getAttributeValue("DI_LEI")
										.equals("0132"))
									// 其他灌木林地
									hjxj_qt_fly_qtgmld += Double.valueOf(list
											.get(i)
											.getAttributeValue("MIAN_JI")
											.toString());
							}
						}
					}
				}
			}
		}
		hjxj_qt.zmj = "" + hjxj_qt_zmj;
		hjxj_qt.lyyd_hj = "" + hjxj_qt_lyyd_hj;
		hjxj_qt.yld_xj = "" + hjxj_qt_yld_xj;
		hjxj_qt.qml = "" + hjxj_qt_qml;
		hjxj_qt.zl = "" + hjxj_qt_zl;
		hjxj_qt.sld = "" + hjxj_qt_sld;
		hjxj_qt.gmld_xj = "" + hjxj_qt_gmld_xj;
		hjxj_qt.gjtbgmld_xj = "" + hjxj_qt_gjtbgmld_xj;
		hjxj_qt.gmjjl = "" + hjxj_qt_gmjjl;
		hjxj_qt.qtgmld = "" + hjxj_qt_qtgmld;
		hjxj_qt.wclzl_xj = "" + hjxj_qt_wclzl_xj;
		hjxj_qt.rgzlwclzld = "" + hjxj_qt_rgzlwcld;
		hjxj_qt.fywcld = "" + hjxj_qt_fywcld;
		hjxj_qt.mpd = "" + hjxj_qt_mpd;
		hjxj_qt.wlmld_xj = "" + hjxj_qt_wlmld_xj;
		hjxj_qt.cfjd = "" + hjxj_qt_cfjd;
		hjxj_qt.hsjd = "" + hjxj_qt_hsjd;
		hjxj_qt.qtwlmld = "" + hjxj_qt_qtwlmld_xj;
		hjxj_qt.yildxj = "" + hjxj_qt_yild_xj;
		hjxj_qt.hshd = "" + hjxj_qt_hshd;
		hjxj_qt.shd = "" + hjxj_qt_shd;
		hjxj_qt.qtyld = "" + hjxj_qt_qtyld;
		hjxj_qt.fzscld = "" + hjxj_qt_fzscld;
		hjxj_qt.flyyd_hj = "" + hjxj_qt_flyyd_hj;
		hjxj_qt.fly_yld = "" + hjxj_qt_flyyd_yld;
		// 非林业灌木林地小计
		hjxj_qt.fly_gmld_xj = "" + hjxj_qt_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_qt.fly_gjtbgdgmld = "" + hjxj_qt_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_qt.fly_qtgmld = "" + hjxj_qt_fly_qtgmld;
		data.add(hjxj_qt);
		return data;
	}

	/**
	 * 二调国有小班统计
	 * 
	 * @param list
	 *            type 值 为国有(1 )、集体(2 )、 3 其它
	 */
	public static ArrayList<EDAreaSum> showEDData_Guoyou(
			List<GeodatabaseFeature> list, String stype, String tjdwStr) {
		ArrayList<EDAreaSum> data = new ArrayList<EDAreaSum>();
		if (list == null) {
			return data;
		}
		String type = "";
		if (stype.equals("1")) {
			type = "国有";
		} else if (stype.equals("2")) {
			type = "集体";
		} else if (stype.equals("3")) {
			type = "个人";
		} else if (stype.equals("4")) {
			type = "民营";
		} else {
			type = "其他";
		}

		int size = list.size();
		// 合计小计
		EDAreaSum hjxj = new EDAreaSum();
		// 统计单位
		hjxj.tjdw = tjdwStr;
		// 林地使用权
		hjxj.ldsyq = type;
		// 森林类别
		hjxj.sllb = "小计";
		double hjxj_zmj = 0;
		// 林业用地合计
		double hjxj_lyyd_hj = 0;
		// 有林地小计
		double hjxj_yld_xj = 0;
		// 乔木林
		double hjxj_qml_xj = 0;
		// 纯林
		double hjxj_cl = 0;
		// 混交林
		double hjxj_hjl = 0;
		// 竹林
		double hjxj_zl = 0;
		// 疏林地
		double hjxj_sld = 0;
		// 灌木林地小计
		double hjxj_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_gmjjl = 0;
		// 其他灌木林地
		double hjxj_qtgmld = 0;
		// 未成林造地小計
		double hjxj_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_lgzlwcld = 0;
		// 封育未成林地
		double hjxj_fwwcld = 0;
		// 苗圃地
		double hjxj_mpd = 0;
		// 无立木林地小计
		double hjxj_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_cfjd = 0;
		// 火烧迹地
		double hjxj_hsjd = 0;
		// 其他无立木林地
		double hjxj_qtwlmld = 0;
		// 宜林地小计
		double hjxj_yild_xj = 0;
		// 荒山荒地
		double hjxj_hshd = 0;
		// 石山地
		double hjxj_ssd = 0;
		// 砂石山地
		double hjxj_sssd = 0;
		// 其他林宜地
		double hjxj_qtlyd = 0;
		// 辅助生产林地
		double hjxj_fzscld = 0;

		// 非林业用地合计
		double hjxj_fly_hj = 0;
		// 非林业用地_有林地
		double hjxj_fly_yld = 0;
		// 非林业用地_灌木林地_小计
		double hjxj_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_fly_qtgmld = 0;
		// 非林业其中≥25°坡耕地
		double hjxj_fly_qz_pgd = 0;
		// 非林业其中 其它非林地
		double hjxj_fly_qz_qtfld = 0;

		for (int i = 0; i < size; i++) {

			if (stype.equals("其他")) {
				// 总面积
				if (list.get(i).getAttributeValue("面积") == null) {
					continue;
				}

				if (list.get(i).getAttributeValue("林地使用权") == null
						|| !(list.get(i).getAttributeValue("林地使用权").toString()
								.equals("1")
								|| list.get(i).getAttributeValue("林地使用权")
										.toString().equals("2")
								|| list.get(i).getAttributeValue("林地使用权")
										.toString().equals("3") || list.get(i)
								.getAttributeValue("林地使用权").toString()
								.equals("4"))) {
					if (list.get(i).getAttributeValue("用地性质") == null) {
						continue;
					}

					if (list.get(i).getAttributeValue("地类") == null) {
						continue;
					}

					hjxj_zmj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

					// 林业用地 合计
					if (list.get(i).getAttributeValue("用地性质").toString()
							.equals("1")) {

						hjxj_lyyd_hj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("11"))
							// 纯林
							hjxj_cl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("12"))
							// 混交林
							hjxj_hjl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("13"))
							// 竹林
							hjxj_zl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("20"))
							// 疏林地
							hjxj_sld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 灌木林地小计
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("3"))

							hjxj_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("31"))
							// 国家特别灌木林小计
							hjxj_gjtbgmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("32"))
							// 其他灌木林地
							hjxj_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 灌木经济林
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("31")) {
							if (list.get(i).getAttributeValue("区划林种") != null) {
								int m = Integer.parseInt(list.get(i)
										.getAttributeValue("区划林种").toString());
								if (m > 50 && m < 56) {
									hjxj_gmjjl += Double
											.valueOf(list.get(i)
													.getAttributeValue("面积")
													.toString());
								}
							}
						}

						// 未成林造林統計
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("4"))

							hjxj_wclzl_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 人工造林未成林地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("41"))

							hjxj_lgzlwcld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 封育未成林地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("42"))

							hjxj_fwwcld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("50"))
							// 苗圃地
							hjxj_mpd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("61"))
							// 采伐迹地
							hjxj_cfjd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("62"))
							// 火烧迹地
							hjxj_hsjd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("63"))
							// 其它无立木林地
							hjxj_qtwlmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 无立木林地
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("6"))

							hjxj_wlmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 宜林地小计
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("7"))

							hjxj_yild_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 荒山荒地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("71"))

							hjxj_hshd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 石山地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("73"))

							hjxj_ssd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 砂石山地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("74"))

							hjxj_sssd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 其他林宜地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("72"))

							hjxj_qtlyd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 辅助生产林地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("80"))

							hjxj_fzscld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

					} else if (list.get(i).getAttributeValue("用地性质").toString()
							.equals("2")) {
						// 非林业用地合计
						hjxj_fly_hj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						// 有林地
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("1"))
							hjxj_fly_yld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 灌木林地小计
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("3"))

							hjxj_fly_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("31"))
							// 国家特别灌木林小计

							hjxj_fly_gjtbgdgmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("32"))
							// 其他灌木林地
							hjxj_fly_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("91"))
							// ≥25°坡耕地
							hjxj_fly_qz_pgd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("92"))
							// 其它非林地
							hjxj_fly_qz_qtfld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
					}
				}
			} else {
				// 总面积
				if (list.get(i).getAttributeValue("面积") == null) {
					continue;
				}

				if (list.get(i).getAttributeValue("林地使用权") == null) {
					continue;
				}

				hjxj_zmj += Double.valueOf(list.get(i).getAttributeValue("面积")
						.toString());

				// 林业用地 合计
				if (list.get(i).getAttributeValue("林地使用权").toString()
						.equals(stype)) {

					if (list.get(i).getAttributeValue("用地性质") == null) {
						continue;
					}

					if (list.get(i).getAttributeValue("地类") == null) {
						continue;
					}

					if (list.get(i).getAttributeValue("用地性质").toString()
							.equals("1")) {

						hjxj_lyyd_hj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("11"))
							// 纯林
							hjxj_cl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("12"))
							// 混交林
							hjxj_hjl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("13"))
							// 竹林
							hjxj_zl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("20"))
							// 疏林地
							hjxj_sld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 灌木林地小计
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("3"))

							hjxj_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("31"))
							// 国家特别灌木林小计
							hjxj_gjtbgmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("32"))
							// 其他灌木林地
							hjxj_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 灌木经济林
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("31")) {
							if (list.get(i).getAttributeValue("区划林种") != null) {
								int m = Integer.parseInt(list.get(i)
										.getAttributeValue("区划林种").toString());
								if (m > 50 && m < 56) {
									hjxj_gmjjl += Double
											.valueOf(list.get(i)
													.getAttributeValue("面积")
													.toString());
								}
							}
						}

						// 未成林造林統計
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("4"))

							hjxj_wclzl_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 人工造林未成林地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("41"))

							hjxj_lgzlwcld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 封育未成林地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("42"))

							hjxj_fwwcld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("50"))
							// 苗圃地
							hjxj_mpd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("61"))
							// 采伐迹地
							hjxj_cfjd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("62"))
							// 火烧迹地
							hjxj_hsjd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("63"))
							// 其它无立木林地
							hjxj_qtwlmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 无立木林地
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("6"))

							hjxj_wlmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 宜林地小计
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("7"))

							hjxj_yild_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 荒山荒地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("71"))

							hjxj_hshd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 石山地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("73"))

							hjxj_ssd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 砂石山地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("74"))

							hjxj_sssd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 其他林宜地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("72"))

							hjxj_qtlyd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 辅助生产林地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("80"))

							hjxj_fzscld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

					} else if (list.get(i).getAttributeValue("用地性质").toString()
							.equals("2")) {

						// 非林业用地合计
						hjxj_fly_hj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						// 有林地
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("1"))
							hjxj_fly_yld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 灌木林地小计
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("3"))

							hjxj_fly_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("31"))
							// 国家特别灌木林小计

							hjxj_fly_gjtbgdgmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("32"))
							// 其他灌木林地
							hjxj_fly_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("91"))
							// ≥25°坡耕地
							hjxj_fly_qz_pgd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("92"))
							// 其它非林地
							hjxj_fly_qz_qtfld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
					}
				}
			}
		}
		// 总面积
		hjxj.zmj = "" + hjxj_zmj;
		hjxj.lyyd_hj = "" + hjxj_lyyd_hj;
		hjxj_qml_xj = (hjxj_cl + hjxj_hjl);
		hjxj_yld_xj = (hjxj_qml_xj + hjxj_zl);
		hjxj.yld_xj = "" + hjxj_yld_xj;
		hjxj.qml_xj = "" + hjxj_qml_xj;
		hjxj.cl = "" + hjxj_cl;
		hjxj.hjl = "" + hjxj_hjl;
		hjxj.zl = "" + hjxj_zl;
		hjxj.sld = "" + hjxj_sld;
		hjxj.gmld_xj = "" + hjxj_gmld_xj;
		hjxj.gjtbgmld_xj = "" + hjxj_gjtbgmld_xj;
		hjxj.gmjjl = "" + hjxj_gmjjl;
		hjxj.qtgmld = "" + hjxj_qtgmld;
		hjxj.wclzl_xj = "" + hjxj_wclzl_xj;
		hjxj.lgzlwcld = "" + hjxj_lgzlwcld;
		hjxj.fwwcld = "" + hjxj_fwwcld;
		hjxj.mpd = "" + hjxj_mpd;
		hjxj.wlmld_xj = "" + hjxj_wlmld_xj;
		hjxj.cfjd = "" + hjxj_cfjd;
		hjxj.hsjd = "" + hjxj_hsjd;
		hjxj.qtwlmld = "" + hjxj_qtwlmld;
		hjxj.yldxj = "" + hjxj_yild_xj;
		hjxj.hshd = "" + hjxj_hshd;
		hjxj.ssd = "" + hjxj_ssd;
		hjxj.sssd = "" + hjxj_sssd;
		hjxj.qtlyd = "" + hjxj_qtlyd;
		hjxj.fzscld = "" + hjxj_fzscld;
		hjxj.flyyd_hj = "" + hjxj_fly_hj;
		hjxj.fly_yld = "" + hjxj_fly_yld;
		// 非林业灌木林地小计
		hjxj.fly_gmld_xj = "" + hjxj_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj.fly_gjtbgdgmld = "" + hjxj_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj.fly_qtgmld = "" + hjxj_fly_qtgmld;

		hjxj.fly_pld = "" + hjxj_fly_qz_pgd;
		hjxj.fly_qtfld = "" + hjxj_fly_qz_qtfld;
		data.add(hjxj);

		// 合计小计_公益林
		EDAreaSum hjxj_gyl = new EDAreaSum();
		// 统计单位
		hjxj_gyl.tjdw = tjdwStr;
		// 林地使用权
		hjxj_gyl.ldsyq = type;
		// 森林类别
		hjxj_gyl.sllb = "生态公益林(地)";
		double hjxj_gyl_zmj = 0;
		// 林业用地合计
		double hjxj_gyl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_gyl_yld_xj = 0;
		// 乔木林
		double hjxj_gyl_qml_xj = 0;
		// 纯林
		double hjxj_gyl_cl = 0;
		// 混交林
		double hjxj_gyl_hjl = 0;
		// 竹林
		double hjxj_gyl_zl = 0;
		// 疏林地
		double hjxj_gyl_sld = 0;
		// 灌木林地小计
		double hjxj_gyl_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gyl_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_gyl_gmjjl = 0;
		// 其他灌木林地
		double hjxj_gyl_qtgmld = 0;
		// 未成林造地小計
		double hjxj_gyl_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_gyl_lgzlwcld = 0;
		// 封育未成林地
		double hjxj_gyl_fwwcld = 0;
		// 苗圃地
		double hjxj_gyl_mpd = 0;
		// 无立木林地小计
		double hjxj_gyl_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_gyl_cfjd = 0;
		// 火烧迹地
		double hjxj_gyl_hsjd = 0;
		// 其他无立木林地
		double hjxj_gyl_qtwlmld = 0;
		// 宜林地小计
		double hjxj_gyl_yild_xj = 0;
		// 荒山荒地
		double hjxj_gyl_hshd = 0;
		// 石山地
		double hjxj_gyl_ssd = 0;
		// 砂石山地
		double hjxj_gyl_sssd = 0;
		// 其他林宜地
		double hjxj_gyl_qtlyd = 0;
		// 辅助生产林地
		double hjxj_gyl_fzscld = 0;

		// 非林业用地合计
		double hjxj_gyl_fly_hj = 0;
		// 非林业用地_有林地
		double hjxj_gyl_fly_yld = 0;
		// 非林业用地_灌木林地_小计
		double hjxj_gyl_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_gyl_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_gyl_fly_qtgmld = 0;
		// 非林业其中≥25°坡耕地
		double hjxj_gyl_fly_qz_pgd = 0;
		// 非林业其中 其它非林地
		double hjxj_gyl_fly_qz_qtfld = 0;

		for (int i = 0; i < size; i++) {

			if (type.equals("其他")) {
				// 总面积
				if (list.get(i).getAttributeValue("面积") == null) {
					continue;
				}
				if (list.get(i).getAttributeValue("林地使用权") == null
						|| !(list.get(i).getAttributeValue("林地使用权").toString()
								.equals("1")
								|| list.get(i).getAttributeValue("林地使用权")
										.toString().equals("2")
								|| list.get(i).getAttributeValue("林地使用权")
										.toString().equals("3") || list.get(i)
								.getAttributeValue("林地使用权").toString()
								.equals("4"))) {
					if (list.get(i).getAttributeValue("森林分类") == null) {
						continue;
					}

					if (list.get(i).getAttributeValue("森林分类").toString()
							.startsWith("1")) {

						if (list.get(i).getAttributeValue("用地性质") == null) {
							continue;
						}
						if (list.get(i).getAttributeValue("地类") == null) {
							continue;
						}

						hjxj_gyl_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						// 林业用地 合计
						if (list.get(i).getAttributeValue("用地性质").toString()
								.equals("1")) {

							hjxj_gyl_lyyd_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("11"))
								// 纯林
								hjxj_gyl_cl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("12"))
								// 混交林
								hjxj_gyl_hjl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("13"))
								// 竹林
								hjxj_gyl_zl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("20"))
								// 疏林地
								hjxj_gyl_sld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_gyl_gmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31"))
								// 国家特别灌木林小计
								hjxj_gyl_gjtbgmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("32"))
								// 其他灌木林地
								hjxj_gyl_qtgmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木经济林
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31")) {
								if (list.get(i).getAttributeValue("区划林种") != null) {
									int m = Integer.parseInt(list.get(i)
											.getAttributeValue("区划林种")
											.toString());
									if (m > 50 && m < 56) {
										hjxj_gyl_gmjjl += Double.valueOf(list
												.get(i).getAttributeValue("面积")
												.toString());
									}
								}
							}
							// 未成林造林統計
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("4"))

								hjxj_gyl_wclzl_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 人工造林未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("41"))

								hjxj_gyl_lgzlwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 封育未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("42"))

								hjxj_gyl_fwwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("50"))
								// 苗圃地
								hjxj_gyl_mpd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("61"))
								// 采伐迹地
								hjxj_gyl_cfjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("62"))
								// 火烧迹地
								hjxj_gyl_hsjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("63"))
								// 其它无立木林地
								hjxj_gyl_qtwlmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 无立木林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("6"))

								hjxj_gyl_wlmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 宜林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("7"))

								hjxj_gyl_yild_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 荒山荒地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("71"))

								hjxj_gyl_hshd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("73"))

								hjxj_gyl_ssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 砂石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("74"))

								hjxj_gyl_sssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 其他林宜地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("72"))

								hjxj_gyl_qtlyd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 辅助生产林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("80"))

								hjxj_gyl_fzscld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

						} else if (list.get(i).getAttributeValue("用地性质")
								.toString().equals("2")) {

							// 非林业用地合计
							hjxj_gyl_fly_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							// 有林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("1"))
								hjxj_gyl_fly_yld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_gyl_fly_gmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("31"))
								// 国家特别灌木林小计

								hjxj_gyl_fly_gjtbgdgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("32"))
								// 其他灌木林地
								hjxj_gyl_fly_qtgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("91"))
								// ≥25°坡耕地
								hjxj_gyl_fly_qz_pgd += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("92"))
								// 其它非林地
								hjxj_gyl_fly_qz_qtfld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());
						}
					}
				}
			} else {
				// 总面积
				if (list.get(i).getAttributeValue("面积") == null) {
					continue;
				}
				if (list.get(i).getAttributeValue("林地使用权") == null) {
					continue;
				}
				if (list.get(i).getAttributeValue("森林分类") == null) {
					continue;
				}

				if (list.get(i).getAttributeValue("林地使用权").toString()
						.equals(stype)) {

					if (list.get(i).getAttributeValue("森林分类").toString()
							.startsWith("1")) {

						if (list.get(i).getAttributeValue("用地性质") == null) {
							continue;
						}
						if (list.get(i).getAttributeValue("地类") == null) {
							continue;
						}

						hjxj_gyl_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						// 林业用地 合计
						if (list.get(i).getAttributeValue("用地性质").toString()
								.equals("1")) {

							hjxj_gyl_lyyd_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("11"))
								// 纯林
								hjxj_gyl_cl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("12"))
								// 混交林
								hjxj_gyl_hjl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("13"))
								// 竹林
								hjxj_gyl_zl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("20"))
								// 疏林地
								hjxj_gyl_sld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_gyl_gmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31"))
								// 国家特别灌木林小计
								hjxj_gyl_gjtbgmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("32"))
								// 其他灌木林地
								hjxj_gyl_qtgmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木经济林
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31")) {
								if (list.get(i).getAttributeValue("区划林种") != null) {
									int m = Integer.parseInt(list.get(i)
											.getAttributeValue("区划林种")
											.toString());
									if (m > 50 && m < 56) {
										hjxj_gyl_gmjjl += Double.valueOf(list
												.get(i).getAttributeValue("面积")
												.toString());
									}
								}
							}

							// 未成林造林統計
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("4"))

								hjxj_gyl_wclzl_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 人工造林未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("41"))

								hjxj_gyl_lgzlwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 封育未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("42"))

								hjxj_gyl_fwwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("50"))
								// 苗圃地
								hjxj_gyl_mpd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("61"))
								// 采伐迹地
								hjxj_gyl_cfjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("62"))
								// 火烧迹地
								hjxj_gyl_hsjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("63"))
								// 其它无立木林地
								hjxj_gyl_qtwlmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 无立木林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("6"))

								hjxj_gyl_wlmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 宜林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("7"))

								hjxj_gyl_yild_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 荒山荒地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("71"))

								hjxj_gyl_hshd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("73"))

								hjxj_gyl_ssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 砂石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("74"))

								hjxj_gyl_sssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 其他林宜地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("72"))

								hjxj_gyl_qtlyd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 辅助生产林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("80"))

								hjxj_gyl_fzscld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

						} else if (list.get(i).getAttributeValue("用地性质")
								.toString().equals("2")) {

							// 非林业用地合计
							hjxj_gyl_fly_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							// 有林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("1"))
								hjxj_gyl_fly_yld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_gyl_fly_gmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("31"))
								// 国家特别灌木林小计

								hjxj_gyl_fly_gjtbgdgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("32"))
								// 其他灌木林地
								hjxj_gyl_fly_qtgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("91"))
								// ≥25°坡耕地
								hjxj_gyl_fly_qz_pgd += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("92"))
								// 其它非林地
								hjxj_gyl_fly_qz_qtfld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());
						}
					}
				}
			}
		}
		// 总面积
		hjxj_gyl.zmj = "" + hjxj_gyl_zmj;
		hjxj_gyl.lyyd_hj = "" + hjxj_gyl_lyyd_hj;
		hjxj_gyl_qml_xj = (hjxj_gyl_cl + hjxj_gyl_hjl);
		hjxj_gyl_yld_xj = (hjxj_gyl_qml_xj + hjxj_gyl_zl);
		hjxj_gyl.yld_xj = "" + hjxj_gyl_yld_xj;
		hjxj_gyl.qml_xj = "" + hjxj_gyl_qml_xj;
		hjxj_gyl.cl = "" + hjxj_gyl_cl;
		hjxj_gyl.hjl = "" + hjxj_gyl_hjl;
		hjxj_gyl.zl = "" + hjxj_gyl_zl;
		hjxj_gyl.sld = "" + hjxj_gyl_sld;
		hjxj_gyl.gmld_xj = "" + hjxj_gyl_gmld_xj;
		hjxj_gyl.gjtbgmld_xj = "" + hjxj_gyl_gjtbgmld_xj;
		hjxj_gyl.gmjjl = "" + hjxj_gyl_gmjjl;
		hjxj_gyl.qtgmld = "" + hjxj_gyl_qtgmld;
		hjxj_gyl.wclzl_xj = "" + hjxj_gyl_wclzl_xj;
		hjxj_gyl.lgzlwcld = "" + hjxj_gyl_lgzlwcld;
		hjxj_gyl.fwwcld = "" + hjxj_gyl_fwwcld;
		hjxj_gyl.mpd = "" + hjxj_gyl_mpd;
		hjxj_gyl.wlmld_xj = "" + hjxj_gyl_wlmld_xj;
		hjxj_gyl.cfjd = "" + hjxj_gyl_cfjd;
		hjxj_gyl.hsjd = "" + hjxj_gyl_hsjd;
		hjxj_gyl.qtwlmld = "" + hjxj_gyl_qtwlmld;
		hjxj_gyl.yldxj = "" + hjxj_gyl_yild_xj;
		hjxj_gyl.hshd = "" + hjxj_gyl_hshd;
		hjxj_gyl.ssd = "" + hjxj_gyl_ssd;
		hjxj_gyl.sssd = "" + hjxj_gyl_sssd;
		hjxj_gyl.qtlyd = "" + hjxj_gyl_qtlyd;
		hjxj_gyl.fzscld = "" + hjxj_gyl_fzscld;
		hjxj_gyl.flyyd_hj = "" + hjxj_gyl_fly_hj;
		hjxj_gyl.fly_yld = "" + hjxj_gyl_fly_yld;
		// 非林业灌木林地小计
		hjxj_gyl.fly_gmld_xj = "" + hjxj_gyl_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_gyl.fly_gjtbgdgmld = "" + hjxj_gyl_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_gyl.fly_qtgmld = "" + hjxj_gyl_fly_qtgmld;

		hjxj_gyl.fly_pld = "" + hjxj_gyl_fly_qz_pgd;
		hjxj_gyl.fly_qtfld = "" + hjxj_gyl_fly_qz_qtfld;
		data.add(hjxj_gyl);

		// 合计小计_商品林
		EDAreaSum hjxj_spl = new EDAreaSum();
		// 统计单位
		hjxj_spl.tjdw = tjdwStr;
		// 林地使用权
		hjxj_spl.ldsyq = type;
		// 森林类别
		hjxj_spl.sllb = "商品林";
		double hjxj_spl_zmj = 0;
		// 林业用地合计
		double hjxj_spl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_spl_yld_xj = 0;
		// 乔木林
		double hjxj_spl_qml_xj = 0;
		// 纯林
		double hjxj_spl_cl = 0;
		// 混交林
		double hjxj_spl_hjl = 0;
		// 竹林
		double hjxj_spl_zl = 0;
		// 疏林地
		double hjxj_spl_sld = 0;
		// 灌木林地小计
		double hjxj_spl_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_spl_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_spl_gmjjl = 0;
		// 其他灌木林地
		double hjxj_spl_qtgmld = 0;
		// 未成林造地小計
		double hjxj_spl_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_spl_lgzlwcld = 0;
		// 封育未成林地
		double hjxj_spl_fwwcld = 0;
		// 苗圃地
		double hjxj_spl_mpd = 0;
		// 无立木林地小计
		double hjxj_spl_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_spl_cfjd = 0;
		// 火烧迹地
		double hjxj_spl_hsjd = 0;
		// 其他无立木林地
		double hjxj_spl_qtwlmld = 0;
		// 宜林地小计
		double hjxj_spl_yild_xj = 0;
		// 荒山荒地
		double hjxj_spl_hshd = 0;
		// 石山地
		double hjxj_spl_ssd = 0;
		// 砂石山地
		double hjxj_spl_sssd = 0;
		// 其他林宜地
		double hjxj_spl_qtlyd = 0;
		// 辅助生产林地
		double hjxj_spl_fzscld = 0;
		// 非林业用地合计
		double hjxj_spl_fly_hj = 0;
		// 非林业用地_有林地
		double hjxj_spl_fly_yld = 0;
		// 非林业用地_灌木林地_小计
		double hjxj_spl_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_spl_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_spl_fly_qtgmld = 0;
		// 非林业其中≥25°坡耕地
		double hjxj_spl_fly_qz_pgd = 0;
		// 非林业其中 其它非林地
		double hjxj_spl_fly_qz_qtfld = 0;

		for (int i = 0; i < size; i++) {
			if (type.equals("其他")) {
				// 总面积
				if (list.get(i).getAttributeValue("面积") == null) {
					continue;
				}
				if (list.get(i).getAttributeValue("林地使用权") == null
						|| !(list.get(i).getAttributeValue("林地使用权").toString()
								.equals("1")
								|| list.get(i).getAttributeValue("林地使用权")
										.toString().equals("2")
								|| list.get(i).getAttributeValue("林地使用权")
										.toString().equals("3") || list.get(i)
								.getAttributeValue("林地使用权").toString()
								.equals("4"))) {
					if (list.get(i).getAttributeValue("森林分类") == null) {
						continue;
					}

					if (list.get(i).getAttributeValue("森林分类").toString()
							.startsWith("2")) {
						if (list.get(i).getAttributeValue("用地性质") == null) {
							continue;
						}
						if (list.get(i).getAttributeValue("地类") == null) {
							continue;
						}
						hjxj_spl_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						// 林业用地 合计
						if (list.get(i).getAttributeValue("用地性质").toString()
								.equals("1")) {

							hjxj_spl_lyyd_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("11"))
								// 纯林
								hjxj_spl_cl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("12"))
								// 混交林
								hjxj_spl_hjl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("13"))
								// 竹林
								hjxj_spl_zl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("20"))
								// 疏林地
								hjxj_spl_sld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_spl_gmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31"))
								// 国家特别灌木林小计
								hjxj_spl_gjtbgmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("32"))
								// 其他灌木林地
								hjxj_spl_qtgmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木经济林
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31")) {
								if (list.get(i).getAttributeValue("区划林种") != null) {
									int m = Integer.parseInt(list.get(i)
											.getAttributeValue("区划林种")
											.toString());
									if (m > 50 && m < 56) {
										hjxj_spl_gmjjl += Double.valueOf(list
												.get(i).getAttributeValue("面积")
												.toString());
									}
								}
							}

							// 未成林造林統計
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("4"))

								hjxj_spl_wclzl_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 人工造林未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("41"))

								hjxj_spl_lgzlwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 封育未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("42"))

								hjxj_spl_fwwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("50"))
								// 苗圃地
								hjxj_spl_mpd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("61"))
								// 采伐迹地
								hjxj_spl_cfjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("62"))
								// 火烧迹地
								hjxj_spl_hsjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("63"))
								// 其它无立木林地
								hjxj_spl_qtwlmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 无立木林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("6"))

								hjxj_spl_wlmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 宜林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("7"))

								hjxj_spl_yild_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 荒山荒地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("71"))

								hjxj_spl_hshd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("73"))

								hjxj_spl_ssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 砂石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("74"))

								hjxj_spl_sssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 其他林宜地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("72"))

								hjxj_spl_qtlyd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 辅助生产林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("80"))

								hjxj_spl_fzscld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

						} else if (list.get(i).getAttributeValue("用地性质")
								.toString().equals("2")) {

							// 非林业用地合计
							hjxj_spl_fly_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							// 有林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("1"))
								hjxj_spl_fly_yld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_spl_fly_gmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("31"))
								// 国家特别灌木林小计

								hjxj_spl_fly_gjtbgdgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("32"))
								// 其他灌木林地
								hjxj_spl_fly_qtgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("91"))
								// ≥25°坡耕地
								hjxj_spl_fly_qz_pgd += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("92"))
								// 其它非林地
								hjxj_spl_fly_qz_qtfld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());
						}
					}
				}
			} else {
				// 总面积
				if (list.get(i).getAttributeValue("面积") == null) {
					continue;
				}
				if (list.get(i).getAttributeValue("林地使用权") == null) {
					continue;
				}
				if (list.get(i).getAttributeValue("森林分类") == null) {
					continue;
				}

				if (list.get(i).getAttributeValue("林地使用权").toString()
						.equals(stype)) {

					if (list.get(i).getAttributeValue("森林分类").toString()
							.startsWith("2")) {
						if (list.get(i).getAttributeValue("用地性质") == null) {
							continue;
						}
						if (list.get(i).getAttributeValue("地类") == null) {
							continue;
						}
						hjxj_spl_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						// 林业用地 合计
						if (list.get(i).getAttributeValue("用地性质").toString()
								.equals("1")) {

							hjxj_spl_lyyd_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("11"))
								// 纯林
								hjxj_spl_cl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("12"))
								// 混交林
								hjxj_spl_hjl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("13"))
								// 竹林
								hjxj_spl_zl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("20"))
								// 疏林地
								hjxj_spl_sld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_spl_gmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31"))
								// 国家特别灌木林小计
								hjxj_spl_gjtbgmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("32"))
								// 其他灌木林地
								hjxj_spl_qtgmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木经济林
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31")) {
								if (list.get(i).getAttributeValue("区划林种") != null) {
									int m = Integer.parseInt(list.get(i)
											.getAttributeValue("区划林种")
											.toString());
									if (m > 50 && m < 56) {
										hjxj_spl_gmjjl += Double.valueOf(list
												.get(i).getAttributeValue("面积")
												.toString());
									}
								}
							}
							// 未成林造林統計
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("4"))

								hjxj_spl_wclzl_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 人工造林未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("41"))

								hjxj_spl_lgzlwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 封育未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("42"))

								hjxj_spl_fwwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("50"))
								// 苗圃地
								hjxj_spl_mpd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("61"))
								// 采伐迹地
								hjxj_spl_cfjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("62"))
								// 火烧迹地
								hjxj_spl_hsjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("63"))
								// 其它无立木林地
								hjxj_spl_qtwlmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 无立木林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("6"))

								hjxj_spl_wlmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 宜林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("7"))

								hjxj_spl_yild_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 荒山荒地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("71"))

								hjxj_spl_hshd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("73"))

								hjxj_spl_ssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 砂石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("74"))

								hjxj_spl_sssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 其他林宜地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("72"))

								hjxj_spl_qtlyd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 辅助生产林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("80"))

								hjxj_spl_fzscld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

						} else if (list.get(i).getAttributeValue("用地性质")
								.toString().equals("2")) {

							// 非林业用地合计
							hjxj_spl_fly_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							// 有林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("1"))
								hjxj_spl_fly_yld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_spl_fly_gmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("31"))
								// 国家特别灌木林小计

								hjxj_spl_fly_gjtbgdgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("32"))
								// 其他灌木林地
								hjxj_spl_fly_qtgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("91"))
								// ≥25°坡耕地
								hjxj_spl_fly_qz_pgd += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("92"))
								// 其它非林地
								hjxj_spl_fly_qz_qtfld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());
						}
					}
				}
			}
		}
		// 总面积
		hjxj_spl.zmj = "" + hjxj_spl_zmj;
		hjxj_spl.lyyd_hj = "" + hjxj_spl_lyyd_hj;
		hjxj_spl_qml_xj = (hjxj_spl_cl + hjxj_spl_hjl);
		hjxj_spl_yld_xj = (hjxj_spl_qml_xj + hjxj_spl_zl);
		hjxj_spl.yld_xj = "" + hjxj_spl_yld_xj;
		hjxj_spl.qml_xj = "" + hjxj_spl_qml_xj;
		hjxj_spl.cl = "" + hjxj_spl_cl;
		hjxj_spl.hjl = "" + hjxj_spl_hjl;
		hjxj_spl.zl = "" + hjxj_spl_zl;
		hjxj_spl.sld = "" + hjxj_spl_sld;
		hjxj_spl.gmld_xj = "" + hjxj_spl_gmld_xj;
		hjxj_spl.gjtbgmld_xj = "" + hjxj_spl_gjtbgmld_xj;
		hjxj_spl.gmjjl = "" + hjxj_spl_gmjjl;
		hjxj_spl.qtgmld = "" + hjxj_spl_qtgmld;
		hjxj_spl.wclzl_xj = "" + hjxj_spl_wclzl_xj;
		hjxj_spl.lgzlwcld = "" + hjxj_spl_lgzlwcld;
		hjxj_spl.fwwcld = "" + hjxj_spl_fwwcld;
		hjxj_spl.mpd = "" + hjxj_spl_mpd;
		hjxj_spl.wlmld_xj = "" + hjxj_spl_wlmld_xj;
		hjxj_spl.cfjd = "" + hjxj_spl_cfjd;
		hjxj_spl.hsjd = "" + hjxj_spl_hsjd;
		hjxj_spl.qtwlmld = "" + hjxj_spl_qtwlmld;
		hjxj_spl.yldxj = "" + hjxj_spl_yild_xj;
		hjxj_spl.hshd = "" + hjxj_spl_hshd;
		hjxj_spl.ssd = "" + hjxj_spl_ssd;
		hjxj_spl.sssd = "" + hjxj_spl_sssd;
		hjxj_spl.qtlyd = "" + hjxj_spl_qtlyd;
		hjxj_spl.fzscld = "" + hjxj_spl_fzscld;
		hjxj_spl.flyyd_hj = "" + hjxj_spl_fly_hj;
		hjxj_spl.fly_yld = "" + hjxj_spl_fly_yld;
		// 非林业灌木林地小计
		hjxj_spl.fly_gmld_xj = "" + hjxj_spl_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_spl.fly_gjtbgdgmld = "" + hjxj_spl_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_spl.fly_qtgmld = "" + hjxj_spl_fly_qtgmld;

		hjxj_spl.fly_pld = "" + hjxj_spl_fly_qz_pgd;
		hjxj_spl.fly_qtfld = "" + hjxj_spl_fly_qz_qtfld;
		data.add(hjxj_spl);

		// 合计小计_商品林
		EDAreaSum hjxj_qt = new EDAreaSum();
		// 统计单位
		hjxj_qt.tjdw = tjdwStr;
		// 林地使用权
		hjxj_qt.ldsyq = type;
		// 森林类别
		hjxj_qt.sllb = "其他";
		double hjxj_qt_zmj = 0;
		// 林业用地合计
		double hjxj_qt_lyyd_hj = 0;
		// 有林地小计
		double hjxj_qt_yld_xj = 0;
		// 乔木林
		double hjxj_qt_qml_xj = 0;
		// 纯林
		double hjxj_qt_cl = 0;
		// 混交林
		double hjxj_qt_hjl = 0;
		// 竹林
		double hjxj_qt_zl = 0;
		// 疏林地
		double hjxj_qt_sld = 0;
		// 灌木林地小计
		double hjxj_qt_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_qt_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_qt_gmjjl = 0;
		// 其他灌木林地
		double hjxj_qt_qtgmld = 0;
		// 未成林造地小計
		double hjxj_qt_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_qt_lgzlwcld = 0;
		// 封育未成林地
		double hjxj_qt_fwwcld = 0;
		// 苗圃地
		double hjxj_qt_mpd = 0;
		// 无立木林地小计
		double hjxj_qt_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_qt_cfjd = 0;
		// 火烧迹地
		double hjxj_qt_hsjd = 0;
		// 其他无立木林地
		double hjxj_qt_qtwlmld = 0;
		// 宜林地小计
		double hjxj_qt_yild_xj = 0;
		// 荒山荒地
		double hjxj_qt_hshd = 0;
		// 石山地
		double hjxj_qt_ssd = 0;
		// 砂石山地
		double hjxj_qt_sssd = 0;
		// 其他林宜地
		double hjxj_qt_qtlyd = 0;
		// 辅助生产林地
		double hjxj_qt_fzscld = 0;

		// 非林业用地合计
		double hjxj_qt_fly_hj = 0;
		// 非林业用地_有林地
		double hjxj_qt_fly_yld = 0;
		// 非林业用地_灌木林地_小计
		double hjxj_qt_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_qt_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_qt_fly_qtgmld = 0;
		// 非林业其中≥25°坡耕地
		double hjxj_qt_fly_qz_pgd = 0;
		// 非林业其中 其它非林地
		double hjxj_qt_fly_qz_qtfld = 0;

		for (int i = 0; i < size; i++) {
			if (type.equals("其他")) {
				// 总面积
				if (list.get(i).getAttributeValue("面积") == null) {
					continue;
				}

				if (list.get(i).getAttributeValue("林地使用权") == null
						|| !(list.get(i).getAttributeValue("林地使用权").equals("1")
								|| list.get(i).getAttributeValue("林地使用权")
										.equals("2")
								|| list.get(i).getAttributeValue("林地使用权")
										.equals("3") || list.get(i)
								.getAttributeValue("林地使用权").equals("4"))) {
					if (list.get(i).getAttributeValue("森林分类") == null
							|| !(list.get(i).getAttributeValue("森林分类")
									.toString().startsWith("1") || list.get(i)
									.getAttributeValue("森林分类").toString()
									.startsWith("2"))) {

						if (list.get(i).getAttributeValue("用地性质") == null) {
							continue;
						}
						if (list.get(i).getAttributeValue("地类") == null) {
							continue;
						}
						hjxj_qt_zmj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						// 林业用地 合计
						if (list.get(i).getAttributeValue("用地性质").toString()
								.equals("1")) {

							hjxj_qt_lyyd_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("11"))
								// 纯林
								hjxj_qt_cl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("12"))
								// 混交林
								hjxj_qt_hjl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("13"))
								// 竹林
								hjxj_qt_zl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("20"))
								// 疏林地
								hjxj_qt_sld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_qt_gmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31"))
								// 国家特别灌木林小计
								hjxj_qt_gjtbgmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("32"))
								// 其他灌木林地
								hjxj_qt_qtgmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木经济林
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("31")) {
								if (list.get(i).getAttributeValue("区划林种") != null) {
									int m = Integer.parseInt(list.get(i)
											.getAttributeValue("区划林种")
											.toString());
									if (m > 50 && m < 56) {
										hjxj_qt_gmjjl += Double.valueOf(list
												.get(i).getAttributeValue("面积")
												.toString());
									}
								}
							}
							// 未成林造林統計
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("4"))

								hjxj_qt_wclzl_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 人工造林未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("41"))

								hjxj_qt_lgzlwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 封育未成林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("42"))

								hjxj_qt_fwwcld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("50"))
								// 苗圃地
								hjxj_qt_mpd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("61"))
								// 采伐迹地
								hjxj_qt_cfjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("62"))
								// 火烧迹地
								hjxj_qt_hsjd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							if (list.get(i).getAttributeValue("地类").toString()
									.equals("63"))
								// 其它无立木林地
								hjxj_qt_qtwlmld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 无立木林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("6"))

								hjxj_qt_wlmld_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 宜林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("7"))

								hjxj_qt_yild_xj += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 荒山荒地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("71"))

								hjxj_qt_hshd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("73"))

								hjxj_qt_ssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 砂石山地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("74"))

								hjxj_qt_sssd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 其他林宜地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("72"))

								hjxj_qt_qtlyd += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							// 辅助生产林地
							if (list.get(i).getAttributeValue("地类").toString()
									.equals("80"))

								hjxj_qt_fzscld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

						} else if (list.get(i).getAttributeValue("用地性质")
								.toString().equals("2")) {

							// 非林业用地合计
							hjxj_qt_fly_hj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

							// 有林地
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("1"))
								hjxj_qt_fly_yld += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());

							// 灌木林地小计
							if (list.get(i).getAttributeValue("地类").toString()
									.startsWith("3"))

								hjxj_qt_fly_gmld_xj += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("31"))
								// 国家特别灌木林小计

								hjxj_qt_fly_gjtbgdgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("32"))
								// 其他灌木林地
								hjxj_qt_fly_qtgmld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("91"))
								// ≥25°坡耕地
								hjxj_qt_fly_qz_pgd += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());

							if (list.get(i).getAttributeValue("地类")
									.equals("92"))
								// 其它非林地
								hjxj_qt_fly_qz_qtfld += Double.valueOf(list
										.get(i).getAttributeValue("面积")
										.toString());
						}
					}
				}
			} else {
				// 总面积
				if (list.get(i).getAttributeValue("面积") == null) {
					continue;
				}

				if (list.get(i).getAttributeValue("林地使用权") == null) {
					continue;
				}

				if (list.get(i).getAttributeValue("森林分类") == null
						|| !(list.get(i).getAttributeValue("森林分类").toString()
								.startsWith("1") || list.get(i)
								.getAttributeValue("森林分类").toString()
								.startsWith("2"))) {

					if (list.get(i).getAttributeValue("用地性质") == null) {
						continue;
					}
					if (list.get(i).getAttributeValue("地类") == null) {
						continue;
					}
					hjxj_qt_zmj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

					// 林业用地 合计
					if (list.get(i).getAttributeValue("用地性质").toString()
							.equals("1")) {

						hjxj_qt_lyyd_hj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("11"))
							// 纯林
							hjxj_qt_cl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("12"))
							// 混交林
							hjxj_qt_hjl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("13"))
							// 竹林
							hjxj_qt_zl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("20"))
							// 疏林地
							hjxj_qt_sld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 灌木林地小计
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("3"))

							hjxj_qt_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("31"))
							// 国家特别灌木林小计
							hjxj_qt_gjtbgmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("32"))
							// 其他灌木林地
							hjxj_qt_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 灌木经济林
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("31")) {
							if (list.get(i).getAttributeValue("区划林种") != null) {
								int m = Integer.parseInt(list.get(i)
										.getAttributeValue("区划林种").toString());
								if (m > 50 && m < 56) {
									hjxj_qt_gmjjl += Double
											.valueOf(list.get(i)
													.getAttributeValue("面积")
													.toString());
								}
							}
						}
						// 未成林造林統計
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("4"))

							hjxj_qt_wclzl_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 人工造林未成林地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("41"))

							hjxj_qt_lgzlwcld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 封育未成林地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("42"))

							hjxj_qt_fwwcld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("50"))
							// 苗圃地
							hjxj_qt_mpd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("61"))
							// 采伐迹地
							hjxj_qt_cfjd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("62"))
							// 火烧迹地
							hjxj_qt_hsjd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").toString()
								.equals("63"))
							// 其它无立木林地
							hjxj_qt_qtwlmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 无立木林地
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("6"))

							hjxj_qt_wlmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 宜林地小计
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("7"))

							hjxj_qt_yild_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 荒山荒地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("71"))

							hjxj_qt_hshd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 石山地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("73"))

							hjxj_qt_ssd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 砂石山地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("74"))

							hjxj_qt_sssd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 其他林宜地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("72"))

							hjxj_qt_qtlyd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						// 辅助生产林地
						if (list.get(i).getAttributeValue("地类").toString()
								.equals("80"))

							hjxj_qt_fzscld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

					} else if (list.get(i).getAttributeValue("用地性质").toString()
							.equals("2")) {

						// 非林业用地合计
						hjxj_qt_fly_hj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

						// 有林地
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("1"))
							hjxj_qt_fly_yld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						// 灌木林地小计
						if (list.get(i).getAttributeValue("地类").toString()
								.startsWith("3"))

							hjxj_qt_fly_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("31"))
							// 国家特别灌木林小计

							hjxj_qt_fly_gjtbgdgmld += Double.valueOf(list
									.get(i).getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("32"))
							// 其他灌木林地
							hjxj_qt_fly_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("91"))
							// ≥25°坡耕地
							hjxj_qt_fly_qz_pgd += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());

						if (list.get(i).getAttributeValue("地类").equals("92"))
							// 其它非林地
							hjxj_qt_fly_qz_qtfld += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
					}
				}
			}
		}
		// 总面积
		hjxj_qt.zmj = "" + hjxj_qt_zmj;
		hjxj_qt.lyyd_hj = "" + hjxj_qt_lyyd_hj;
		hjxj_qt_qml_xj = (hjxj_qt_cl + hjxj_qt_hjl);
		hjxj_qt_yld_xj = (hjxj_qt_qml_xj + hjxj_qt_zl);
		hjxj_qt.yld_xj = "" + hjxj_qt_yld_xj;
		hjxj_qt.qml_xj = "" + hjxj_qt_qml_xj;
		hjxj_qt.cl = "" + hjxj_qt_cl;
		hjxj_qt.hjl = "" + hjxj_qt_hjl;
		hjxj_qt.zl = "" + hjxj_qt_zl;
		hjxj_qt.sld = "" + hjxj_qt_sld;
		hjxj_qt.gmld_xj = "" + hjxj_qt_gmld_xj;
		hjxj_qt.gjtbgmld_xj = "" + hjxj_qt_gjtbgmld_xj;
		hjxj_qt.gmjjl = "" + hjxj_qt_gmjjl;
		hjxj_qt.qtgmld = "" + hjxj_qt_qtgmld;
		hjxj_qt.wclzl_xj = "" + hjxj_qt_wclzl_xj;
		hjxj_qt.lgzlwcld = "" + hjxj_qt_lgzlwcld;
		hjxj_qt.fwwcld = "" + hjxj_qt_fwwcld;
		hjxj_qt.mpd = "" + hjxj_qt_mpd;
		hjxj_qt.wlmld_xj = "" + hjxj_qt_wlmld_xj;
		hjxj_qt.cfjd = "" + hjxj_qt_cfjd;
		hjxj_qt.hsjd = "" + hjxj_qt_hsjd;
		hjxj_qt.qtwlmld = "" + hjxj_qt_qtwlmld;
		hjxj_qt.yldxj = "" + hjxj_qt_yild_xj;
		hjxj_qt.hshd = "" + hjxj_qt_hshd;
		hjxj_qt.ssd = "" + hjxj_qt_ssd;
		hjxj_qt.sssd = "" + hjxj_qt_sssd;
		hjxj_qt.qtlyd = "" + hjxj_qt_qtlyd;
		hjxj_qt.fzscld = "" + hjxj_qt_fzscld;
		hjxj_qt.flyyd_hj = "" + hjxj_qt_fly_hj;
		hjxj_qt.fly_yld = "" + hjxj_qt_fly_yld;
		// 非林业灌木林地小计
		hjxj_qt.fly_gmld_xj = "" + hjxj_qt_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_qt.fly_gjtbgdgmld = "" + hjxj_qt_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_qt.fly_qtgmld = "" + hjxj_qt_fly_qtgmld;

		hjxj_qt.fly_pld = "" + hjxj_qt_fly_qz_pgd;
		hjxj_qt.fly_qtfld = "" + hjxj_qt_fly_qz_qtfld;
		// 总面积
		data.add(hjxj_qt);

		hjxj.zmj = "" + (hjxj_gyl_zmj + hjxj_spl_zmj + hjxj_qt_zmj);
		hjxj.yld_xj = "" + (hjxj_gyl_yld_xj + hjxj_spl_yld_xj + hjxj_qt_yld_xj);
		hjxj.flyyd_hj = ""
				+ (hjxj_gyl_fly_hj + hjxj_spl_fly_hj + hjxj_qt_fly_hj);
		return data;
	}

	/**
	 * 公益林统计表
	 * 
	 * @param list
	 */
	public static ArrayList<GYLAreaSum> getGYLData(
			List<GeodatabaseFeature> list, String tjdwStr) {
		ArrayList<GYLAreaSum> data = new ArrayList<GYLAreaSum>();
		int size = list.size();
		// 合计合计
		GYLAreaSum hjxj = new GYLAreaSum();
		// 统计单位
		hjxj.tjdw = tjdwStr;
		// 权属
		hjxj.qs = "合计";
		// 林业用地合计
		double hjxj_lyyd_hj = 0;
		// 有林地小计
		double hjxj_yld_xj = 0;
		// 乔木林
		double hjxj_qml = 0;
		// 竹林
		double hjxj_zl = 0;
		// 疏林地
		double hjxj_sld = 0;
		// 灌木林地小计
		double hjxj_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gjtbgmld_xj = 0;
		// 其他灌木林地
		double hjxj_qtgmld = 0;
		// 未成林造地小計
		double hjxj_wclzl_xj = 0;
		// 苗圃地
		double hjxj_mpd = 0;
		// 无立木林地
		double hjxj_wlmld_xj = 0;
		// 宜林地
		double hjxj_yild = 0;
		// 辅助生产林地
		double hjxj_fzscld = 0;

		// 合计
		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("MJ") == null) {
				continue;
			}
			if (list.get(i).getAttributeValue("DL") == null) {
				continue;
			}
			// 林业用地合计
			hjxj_lyyd_hj += Double.valueOf(list.get(i).getAttributeValue("MJ")
					.toString());

			if (list.get(i).getAttributeValue("DL").toString().contains("111"))
				// 乔木林
				hjxj_qml += Double.valueOf(list.get(i).getAttributeValue("MJ")
						.toString());
			if (list.get(i).getAttributeValue("DL").toString().contains("113"))
				// 竹林
				hjxj_zl += Double.valueOf(list.get(i).getAttributeValue("MJ")
						.toString());

			if (list.get(i).getAttributeValue("DL").toString().contains("120"))
				// 疏林地
				hjxj_sld += Double.valueOf(list.get(i).getAttributeValue("MJ")
						.toString());
			// 灌木林地小计
			if (list.get(i).getAttributeValue("DL").toString().startsWith("13"))

				hjxj_gmld_xj += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());

			if (list.get(i).getAttributeValue("DL").toString().contains("131"))
				// 国家特别灌木林小计
				hjxj_gjtbgmld_xj += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());

			if (list.get(i).getAttributeValue("DL").toString().contains("132"))
				// 其他灌木林地
				hjxj_qtgmld += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());
			// 未成林造林統計
			if (list.get(i).getAttributeValue("DL").toString().toString()
					.startsWith("14"))

				hjxj_wclzl_xj += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());

			if (list.get(i).getAttributeValue("DL").toString().contains("150"))
				// 苗圃地
				hjxj_mpd += Double.valueOf(list.get(i).getAttributeValue("MJ")
						.toString());
			// 无立木林地
			if (list.get(i).getAttributeValue("DL").toString().toString()
					.startsWith("16"))

				hjxj_wlmld_xj += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());

			// 宜林地
			if (list.get(i).getAttributeValue("DL").toString().startsWith("17"))
				hjxj_yild += Double.valueOf(list.get(i).getAttributeValue("MJ")
						.toString());
			// 辅助生产林地
			if (list.get(i).getAttributeValue("DL").toString().contains("180"))

				hjxj_fzscld += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());
		}
		// 总面积
		hjxj.lyyd_hj = "" + hjxj_lyyd_hj;
		hjxj_yld_xj = (hjxj_qml + hjxj_zl);
		hjxj.yld_xj = "" + hjxj_yld_xj;
		hjxj.qml = "" + hjxj_qml;
		hjxj.zl = "" + hjxj_zl;
		hjxj.sld = "" + hjxj_sld;
		hjxj.gmld_xj = "" + (hjxj_gjtbgmld_xj + hjxj_qtgmld);
		hjxj.gjtbgmld_xj = "" + hjxj_gjtbgmld_xj;
		hjxj.qtgmld = "" + hjxj_qtgmld;
		hjxj.wclzl_xj = "" + hjxj_wclzl_xj;
		hjxj.mpd = "" + hjxj_mpd;
		hjxj.wlmld_xj = "" + hjxj_wlmld_xj;
		hjxj.yild = "" + hjxj_yild;
		hjxj.fzscld = "" + hjxj_fzscld;
		data.add(hjxj);

		// 国有合计
		GYLAreaSum hjxj_gyl = new GYLAreaSum();
		// 统计单位
		hjxj_gyl.tjdw = tjdwStr;
		// 权属
		hjxj_gyl.qs = "国有";
		// 林业用地合计
		double hjxj_gyl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_gyl_yld_xj = 0;
		// 乔木林
		double hjxj_gyl_qml = 0;
		// 竹林
		double hjxj_gyl_zl = 0;
		// 疏林地
		double hjxj_gyl_sld = 0;
		// 灌木林地小计
		double hjxj_gyl_gmld_xj = 0;
		// 国家特别灌木林
		double hjxj_gyl_gjtbgmld_xj = 0;
		// 其他灌木林地
		double hjxj_gyl_qtgmld = 0;
		// 未成林造林统计
		double hjxj_gyl_wclzl_xj = 0;
		// 苗圃地
		double hjxj_gyl_mpd = 0;
		// 无立木林地
		double hjxj_gyl_wlmld_xj = 0;
		// 宜林地
		double hjxj_gyl_yild = 0;
		// 辅助生产林地
		double hjxj_gyl_fzscld = 0;

		for (int i = 0; i < size; i++) {
			if (list.get(i).getAttributeValue("MJ") == null) {
				continue;
			}
			if (list.get(i).getAttributeValue("LDSY") == null) {
				continue;
			}
			if (list.get(i).getAttributeValue("DL") == null) {
				continue;
			}
			// 林业用地国有合计
			if (list.get(i).getAttributeValue("LDSY").toString().equals("1")) {
				hjxj_gyl_lyyd_hj += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("11")) {
					// 有林地 小计
					hjxj_gyl_yld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				}

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("111"))
					// 乔木林
					hjxj_gyl_qml += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				if (list.get(i).getAttributeValue("DL").toString()
						.contains("113"))
					// 竹林
					hjxj_gyl_zl += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("120"))
					// 疏林地
					hjxj_gyl_sld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 灌木林地小计
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("13"))

					hjxj_gyl_gmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("131"))
					// 国家特别灌木林小计
					hjxj_gyl_gjtbgmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("132"))
					// 其他灌木林地
					hjxj_gyl_qtgmld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				// 未成林造林统计
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("14"))

					hjxj_gyl_wclzl_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("150"))
					// 苗圃地
					hjxj_gyl_mpd += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				// 无立木林地
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("16"))

					hjxj_gyl_wlmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 宜林地
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("17"))

					hjxj_gyl_yild += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				// 辅助生产林地
				if (list.get(i).getAttributeValue("DL").toString()
						.contains("180"))

					hjxj_gyl_fzscld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

			}
		}
		// 总面积
		hjxj_gyl.lyyd_hj = "" + hjxj_gyl_lyyd_hj;
		hjxj_gyl.yld_xj = "" + (hjxj_gyl_zl + hjxj_gyl_qml);
		hjxj_gyl.zl = "" + hjxj_gyl_zl;
		hjxj_gyl.qml = "" + hjxj_gyl_qml;
		hjxj_gyl.sld = "" + hjxj_gyl_sld;
		hjxj_gyl.gmld_xj = "" + (hjxj_gyl_gjtbgmld_xj + hjxj_gyl_qtgmld);
		hjxj_gyl.gjtbgmld_xj = "" + hjxj_gyl_gjtbgmld_xj;
		hjxj_gyl.qtgmld = "" + hjxj_gyl_qtgmld;
		hjxj_gyl.wclzl_xj = "" + hjxj_gyl_wclzl_xj;
		hjxj_gyl.mpd = "" + hjxj_gyl_mpd;
		hjxj_gyl.wlmld_xj = "" + hjxj_gyl_wlmld_xj;
		hjxj_gyl.yild = "" + hjxj_gyl_yild;
		hjxj_gyl.fzscld = "" + hjxj_gyl_fzscld;
		data.add(hjxj_gyl);

		// 集体
		GYLAreaSum hjxj_spl = new GYLAreaSum();
		// 统计单位
		hjxj_spl.tjdw = tjdwStr;
		// 权属
		hjxj_spl.qs = "集体";
		// 林业用地合计
		double hjxj_spl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_spl_yld_xj = 0;
		// 乔木林
		double hjxj_spl_qml = 0;
		// 竹林
		double hjxj_spl_zl = 0;
		// 疏林地
		double hjxj_spl_sld = 0;
		// 灌木林地小计
		double hjxj_spl_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_spl_gjtbgmld_xj = 0;
		// 其他灌木林地
		double hjxj_spl_qtgmld = 0;
		// 未成林造林统计
		double hjxj_spl_wclzl_xj = 0;
		// 苗圃地
		double hjxj_spl_mpd = 0;
		// 无立木林地
		double hjxj_spl_wlmld_xj = 0;
		// 宜林地
		double hjxj_spl_yild = 0;
		// 辅助生产林地
		double hjxj_spl_fzscld = 0;

		for (int i = 0; i < size; i++) {
			if (list.get(i).getAttributeValue("MJ") == null) {
				continue;
			}
			if (list.get(i).getAttributeValue("LDSY") == null) {
				continue;
			}
			if (list.get(i).getAttributeValue("DL") == null) {
				continue;
			}

			// 林业用地合计
			if (list.get(i).getAttributeValue("LDSY").toString().equals("2")) {
				hjxj_spl_lyyd_hj += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());
				if (list.get(i).getAttributeValue("DL").toString()
						.contains("111"))
					// 乔木林
					hjxj_spl_qml += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				if (list.get(i).getAttributeValue("DL").toString()
						.contains("113"))
					// 竹林
					hjxj_spl_zl += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("120"))
					// 疏林地
					hjxj_spl_sld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 灌木林地小计
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("13"))

					hjxj_spl_gmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("131"))
					// 国家特别灌木林小计
					hjxj_spl_gjtbgmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("132"))
					// 其他灌木林地
					hjxj_spl_qtgmld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				// 未成林造林统计
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("14"))

					hjxj_spl_wclzl_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("150"))
					// 苗圃地
					hjxj_spl_mpd += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 无立木林地
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("16"))

					hjxj_spl_wlmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 宜林地
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("17"))

					hjxj_spl_yild += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				// 辅助生产林地
				if (list.get(i).getAttributeValue("DL").toString()
						.contains("180"))

					hjxj_spl_fzscld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

			}
		}
		// 总面积
		hjxj_spl.lyyd_hj = "" + hjxj_spl_lyyd_hj;
		hjxj_spl_yld_xj = (hjxj_spl_qml + hjxj_spl_zl);
		hjxj_spl.yld_xj = "" + hjxj_spl_yld_xj;
		hjxj_spl.zl = "" + hjxj_spl_zl;
		hjxj_spl.qml = "" + hjxj_spl_qml;
		hjxj_spl.sld = "" + hjxj_spl_sld;
		hjxj_spl.gmld_xj = "" + (hjxj_spl_gjtbgmld_xj + hjxj_spl_qtgmld);
		hjxj_spl.gjtbgmld_xj = "" + hjxj_spl_gjtbgmld_xj;
		hjxj_spl.qtgmld = "" + hjxj_spl_qtgmld;
		hjxj_spl.wclzl_xj = "" + hjxj_spl_wclzl_xj;
		hjxj_spl.mpd = "" + hjxj_spl_mpd;
		hjxj_spl.wlmld_xj = "" + hjxj_spl_wlmld_xj;
		hjxj_spl.yild = "" + hjxj_spl_yild;
		hjxj_spl.fzscld = "" + hjxj_spl_fzscld;
		data.add(hjxj_spl);

		// 个人
		GYLAreaSum hjxj_gr = new GYLAreaSum();
		// 统计单位
		hjxj_gr.tjdw = tjdwStr;
		// 权属
		hjxj_gr.qs = "个人";
		// 林业用地合计
		double hjxj_gr_lyyd_hj = 0;
		// 有林地小计
		double hjxj_gr_yld_xj = 0;
		// 乔木林
		double hjxj_gr_qml = 0;
		// 竹林
		double hjxj_gr_zl = 0;
		// 疏林地
		double hjxj_gr_sld = 0;
		// 灌木林地小计
		double hjxj_gr_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gr_gjtbgmld_xj = 0;
		// 其他灌木林地
		double hjxj_gr_qtgmld = 0;
		// 未成林造林统计
		double hjxj_gr_wclzl_xj = 0;
		// 苗圃地
		double hjxj_gr_mpd = 0;
		// 无立木林地
		double hjxj_gr_wlmld_xj = 0;
		// 宜林地
		double hjxj_gr_yild = 0;
		// 辅助生产林地
		double hjxj_gr_fzscld = 0;

		for (int i = 0; i < size; i++) {
			if (list.get(i).getAttributeValue("MJ") == null) {
				continue;
			}
			if (list.get(i).getAttributeValue("LDSY") == null) {
				continue;
			}
			if (list.get(i).getAttributeValue("DL") == null) {
				continue;
			}

			// 林业用地合计
			if (list.get(i).getAttributeValue("LDSY").toString().equals("3")) {
				hjxj_gr_lyyd_hj += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());
				if (list.get(i).getAttributeValue("DL").toString()
						.contains("111"))
					// 乔木林
					hjxj_gr_qml += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				if (list.get(i).getAttributeValue("DL").toString()
						.contains("113"))
					// 竹林
					hjxj_gr_zl += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("120"))
					// 疏林地
					hjxj_gr_sld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 灌木林地小计
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("13"))

					hjxj_gr_gmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("131"))
					// 国家特别灌木林小计
					hjxj_gr_gjtbgmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("132"))
					// 其他灌木林地
					hjxj_gr_qtgmld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				// 未成林造林统计
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("14"))

					hjxj_gr_wclzl_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.contains("150"))
					// 苗圃地
					hjxj_gr_mpd += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 无立木林地
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("16"))

					hjxj_gr_wlmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 宜林地
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("17"))

					hjxj_gr_yild += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				// 辅助生产林地
				if (list.get(i).getAttributeValue("DL").toString()
						.contains("180"))

					hjxj_gr_fzscld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

			}
		}
		// 总面积
		hjxj_gr.lyyd_hj = "" + hjxj_gr_lyyd_hj;
		hjxj_gr_yld_xj = (hjxj_gr_qml + hjxj_gr_zl);
		hjxj_gr.yld_xj = "" + hjxj_gr_yld_xj;
		hjxj_gr.zl = "" + hjxj_gr_zl;
		hjxj_gr.qml = "" + hjxj_gr_qml;
		hjxj_gr.sld = "" + hjxj_gr_sld;
		hjxj_gr.gmld_xj = "" + (hjxj_gr_gjtbgmld_xj + hjxj_gr_qtgmld);
		hjxj_gr.gjtbgmld_xj = "" + hjxj_gr_gjtbgmld_xj;
		hjxj_gr.qtgmld = "" + hjxj_gr_qtgmld;
		hjxj_gr.wclzl_xj = "" + hjxj_gr_wclzl_xj;
		hjxj_gr.mpd = "" + hjxj_gr_mpd;
		hjxj_gr.wlmld_xj = "" + hjxj_gr_wlmld_xj;
		hjxj_gr.yild = "" + hjxj_gr_yild;
		hjxj_gr.fzscld = "" + hjxj_gr_fzscld;
		data.add(hjxj_gr);

		// 其他
		GYLAreaSum hjxj_qt = new GYLAreaSum();
		// 统计单位
		hjxj_qt.tjdw = tjdwStr;
		// 权属
		hjxj_qt.qs = "其它";
		// 林业用地合计
		double hjxj_qt_lyyd_hj = 0;
		// 有林地小计
		double hjxj_qt_yld_xj = 0;
		// 乔木林
		double hjxj_qt_qml = 0;
		// 竹林
		double hjxj_qt_zl = 0;
		// 疏林地
		double hjxj_qt_sld = 0;
		// 灌木林地小计
		double hjxj_qt_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_qt_gjtbgmld_xj = 0;
		// 其他灌木林地
		double hjxj_qt_qtgmld = 0;
		// 未成林造林统计
		double hjxj_qt_wclzl_xj = 0;
		// 苗圃地
		double hjxj_qt_mpd = 0;
		// 无立木林地
		double hjxj_qt_wlmld_xj = 0;
		// 宜林地
		double hjxj_qt_yild = 0;
		// 辅助生产林地
		double hjxj_qt_fzscld = 0;

		for (int i = 0; i < size; i++) {
			if (list.get(i).getAttributeValue("MJ") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("DL") == null) {
				continue;
			}

			// 林业用地合计
			if (list.get(i).getAttributeValue("LDSY") == null
					|| !(list.get(i).getAttributeValue("LDSY").toString()
							.equals("1")
							|| list.get(i).getAttributeValue("LDSY").toString()
									.equals("2") || list.get(i)
							.getAttributeValue("LDSY").toString().equals("3"))) {

				hjxj_qt_lyyd_hj += Double.valueOf(list.get(i)
						.getAttributeValue("MJ").toString());
				if (list.get(i).getAttributeValue("DL").toString()
						.equals("111"))
					// 乔木林
					hjxj_qt_qml += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				if (list.get(i).getAttributeValue("DL").equals("113"))
					// 竹林
					hjxj_qt_zl += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.equals("120"))
					// 疏林地
					hjxj_qt_sld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 灌木林地小计
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("13"))

					hjxj_qt_gmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.equals("131"))
					// 国家特别灌木林小计
					hjxj_qt_gjtbgmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.equals("132"))
					// 其他灌木林地
					hjxj_qt_qtgmld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				// 未成林造林统计
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("14"))

					hjxj_qt_wclzl_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				if (list.get(i).getAttributeValue("DL").toString()
						.equals("150"))
					// 苗圃地
					hjxj_qt_mpd += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 无立木林地
				if (list.get(i).getAttributeValue("DL").toString()
						.startsWith("16"))

					hjxj_qt_wlmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

				// 宜林地
				if (list.get(i).getAttributeValue("DL").toString()
						.equals("170"))

					hjxj_qt_yild += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());
				// 辅助生产林地
				if (list.get(i).getAttributeValue("DL").toString()
						.equals("180"))

					hjxj_qt_fzscld += Double.valueOf(list.get(i)
							.getAttributeValue("MJ").toString());

			}
		}
		// 总面积
		hjxj_qt.lyyd_hj = "" + hjxj_qt_lyyd_hj;
		hjxj_qt_yld_xj = (hjxj_qt_qml + hjxj_qt_zl);
		hjxj_qt.yld_xj = "" + hjxj_qt_yld_xj;
		hjxj_qt.zl = "" + hjxj_qt_zl;
		hjxj_qt.qml = "" + hjxj_qt_qml;
		hjxj_qt.sld = "" + hjxj_qt_sld;
		hjxj_qt.gmld_xj = "" + (hjxj_qt_gjtbgmld_xj + hjxj_qt_qtgmld);
		hjxj_qt.gjtbgmld_xj = "" + hjxj_qt_gjtbgmld_xj;
		hjxj_qt.qtgmld = "" + hjxj_qt_qtgmld;
		hjxj_qt.wclzl_xj = "" + hjxj_qt_wclzl_xj;
		hjxj_qt.mpd = "" + hjxj_qt_mpd;
		hjxj_qt.wlmld_xj = "" + hjxj_qt_wlmld_xj;
		hjxj_qt.yild = "" + hjxj_qt_yild;
		hjxj_qt.fzscld = "" + hjxj_qt_fzscld;
		data.add(hjxj_qt);
		return data;
	}

	public static ArrayList<LGAreaSum> getLQZDData(
			List<GeodatabaseFeature> list, String tjdwStr) {
		ArrayList<LGAreaSum> data = new ArrayList<LGAreaSum>();
		int size = list.size();
		// 合计
		LGAreaSum hjxj = new LGAreaSum();
		// 统计单位
		hjxj.tjdw = tjdwStr;
		// 林业用地_森林类别
		hjxj.sllb = "合计";
		// 按林权主体类型_总计
		double hjxj_lqzt_hj = 0;
		// 按林权主体类型—国家
		double hjxj_lqzt_gj = 0;
		// 按林权主体类型—集体
		double hjxj_lqzt_jt = 0;
		// 按林权主体类型—民营
		double hjxj_lqzt_my = 0;
		// 按林权主体类型__其他
		double hjxj_lqzt_qt = 0;
		// 按使用权类型_——合计
		double hjxj_syqlx_hj = 0;
		// 按使用权类型__国有山
		double hjxj_syqlx_gys = 0;
		// 按使用权类型__民营小计
		double hjxj_syqlx_my_xj = 0;
		// 按使用权类型__集体山
		double hjxj_syqlx_jts = 0;
		// 按使用权类型__自留山
		double hjxj_syqlx_zls = 0;
		// 按使用权类型__责任山
		double hjxj_syqlx_zrs = 0;
		// 按使用权类型__承包山
		double hjxj_syqlx_cbs = 0;
		// 按使用权类型__其他
		double hjxj_syqlx_qt = 0;

		// 合计
		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("面积") == null) {
				continue;
			}
			// 按林权主体类型——合计
			hjxj_lqzt_hj += Double.valueOf(list.get(i).getAttributeValue("面积")
					.toString());

			if (list.get(i).getAttributeValue("林权主体类型") != null) {

				if (list.get(i).getAttributeValue("林权主体类型").toString()
						.contains("国")) {
					// 按林权主体类型—国家
					hjxj_lqzt_gj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				} else if (list.get(i).getAttributeValue("林权主体类型").toString()
						.contains("集")) {
					// 按林权主体类型—集体
					hjxj_lqzt_jt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				} else if (list.get(i).getAttributeValue("林权主体类型").toString()
						.contains("民")) {
					// 按林权主体类型—民营
					hjxj_lqzt_my += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				} else if (list.get(i).getAttributeValue("林权主体类型").toString()
						.contains("其")) {
					// 按林权主体类型__其他
					hjxj_lqzt_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				} else {
					// 按林权主体类型__其他
					hjxj_lqzt_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				}
			} else {
				// 按林权主体类型__其他
				hjxj_lqzt_qt += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());
			}

			hjxj_syqlx_hj += Double.valueOf(list.get(i).getAttributeValue("面积")
					.toString());

			if (list.get(i).getAttributeValue("使用权类型") != null) {
				if (list.get(i).getAttributeValue("使用权类型").toString()
						.contains("国")) {
					hjxj_syqlx_gys += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				} else if (list.get(i).getAttributeValue("使用权类型").toString()
						.contains("集")) {
					hjxj_syqlx_jts += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				} else if (list.get(i).getAttributeValue("使用权类型").toString()
						.contains("民")) {
					hjxj_syqlx_my_xj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				} else if (list.get(i).getAttributeValue("使用权类型").toString()
						.contains("自")) {
					hjxj_syqlx_zls += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				} else if (list.get(i).getAttributeValue("使用权类型").toString()
						.contains("责")) {
					hjxj_syqlx_zrs += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				} else if (list.get(i).getAttributeValue("使用权类型").toString()
						.contains("承")) {
					hjxj_syqlx_cbs += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				} else if (list.get(i).getAttributeValue("使用权类型").toString()
						.contains("其")) {
					hjxj_syqlx_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				} else {
					hjxj_syqlx_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				}
			} else {
				hjxj_syqlx_qt += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());
			}
		}
		// 总面积
		hjxj.lqzt_hj = "" + hjxj_lqzt_hj;
		hjxj.lqzt_gj = "" + hjxj_lqzt_gj;
		hjxj.lqzt_jt = "" + hjxj_lqzt_jt;
		hjxj.lqzt_my = "" + hjxj_lqzt_my;
		hjxj.lqzt_qt = "" + hjxj_lqzt_qt;
		hjxj.syqlx_hj = "" + hjxj_syqlx_hj;
		hjxj.syqlx_gys = "" + hjxj_syqlx_gys;
		hjxj.syqlx_jts = "" + hjxj_syqlx_jts;
		hjxj.syqlx_my_xj = "" + (hjxj_syqlx_zls + hjxj_syqlx_zrs);
		// if(hjxj_syqlx_zls != 0 || hjxj_syqlx_zrs != 0){
		// hjxj.syqlx_my_xj = ""+(hjxj_syqlx_zls+hjxj_syqlx_zrs);
		// }else{
		// hjxj.syqlx_my_xj = ""+hjxj_syqlx_my_xj;
		// }
		hjxj.syqlx_zls = "" + hjxj_syqlx_zls;
		hjxj.syqlx_zrs = "" + hjxj_syqlx_zrs;
		hjxj.syqlx_cbs = "" + hjxj_syqlx_cbs;
		hjxj.syqlx_qt = "" + hjxj_syqlx_qt;
		data.add(hjxj);

		// 合计
		LGAreaSum hjxj_gyl = new LGAreaSum();
		// 统计单位
		hjxj_gyl.tjdw = tjdwStr;
		// 林业用地_森林类别
		hjxj_gyl.sllb = "公益林";
		// 按林权主体类型_总计
		double hjxj_gyl_lqzt_hj = 0;
		// 按林权主体类型—国家
		double hjxj_gyl_lqzt_gj = 0;
		// 按林权主体类型—集体
		double hjxj_gyl_lqzt_jt = 0;
		// 按林权主体类型—民营
		double hjxj_gyl_lqzt_my = 0;
		// 按林权主体类型__其他
		double hjxj_gyl_lqzt_qt = 0;
		// 按使用权类型_——合计
		double hjxj_gyl_syqlx_hj = 0;
		// 按使用权类型__国有山
		double hjxj_gyl_syqlx_gys = 0;
		// 按使用权类型——民营-小计
		double hjxj_gyl_syqlx_my_xj = 0;
		// 按使用权类型__集体山
		double hjxj_gyl_syqlx_jts = 0;
		// 按使用权类型__自留山
		double hjxj_gyl_syqlx_zls = 0;
		// 按使用权类型__责任山
		double hjxj_gyl_syqlx_zrs = 0;
		// 按使用权类型__承包山
		double hjxj_gyl_syqlx_cbs = 0;
		// 按使用权类型__其他
		double hjxj_gyl_syqlx_qt = 0;

		// 合计
		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("面积") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("森林类别") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("森林类别").toString().contains("益")) {
				// 按林权主体类型——合计
				hjxj_gyl_lqzt_hj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("林权主体类型") != null) {

					if (list.get(i).getAttributeValue("林权主体类型").toString()
							.contains("国")) {
						// 按林权主体类型—国家
						hjxj_gyl_lqzt_gj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("林权主体类型")
							.toString().contains("集")) {
						// 按林权主体类型—集体
						hjxj_gyl_lqzt_jt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("林权主体类型")
							.toString().contains("民")) {
						// 按林权主体类型—民营
						hjxj_gyl_lqzt_my += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("林权主体类型")
							.toString().contains("其")) {
						// 按林权主体类型__其他
						hjxj_gyl_lqzt_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else {
						// 按林权主体类型__其他
						hjxj_gyl_lqzt_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					}
				} else {
					hjxj_gyl_lqzt_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				}

				hjxj_gyl_syqlx_hj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("使用权类型") != null) {

					if (list.get(i).getAttributeValue("使用权类型").toString()
							.contains("国")) {
						hjxj_gyl_syqlx_gys += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("集")) {
						hjxj_gyl_syqlx_jts += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("民")) {
						hjxj_gyl_syqlx_my_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("自")) {
						hjxj_gyl_syqlx_zls += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("责")) {
						hjxj_gyl_syqlx_zrs += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("承")) {
						hjxj_gyl_syqlx_cbs += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("其")) {
						hjxj_gyl_syqlx_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else {
						hjxj_gyl_syqlx_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					}
				} else {
					hjxj_gyl_syqlx_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				}
			}
		}
		// 总面积
		hjxj_gyl.lqzt_hj = "" + hjxj_gyl_lqzt_hj;
		hjxj_gyl.lqzt_gj = "" + hjxj_gyl_lqzt_gj;
		hjxj_gyl.lqzt_jt = "" + hjxj_gyl_lqzt_jt;
		hjxj_gyl.lqzt_my = "" + hjxj_gyl_lqzt_my;
		hjxj_gyl.lqzt_qt = "" + hjxj_gyl_lqzt_qt;
		hjxj_gyl.syqlx_hj = "" + hjxj_gyl_syqlx_hj;
		hjxj_gyl.syqlx_gys = "" + hjxj_gyl_syqlx_gys;
		hjxj_gyl.syqlx_jts = "" + hjxj_gyl_syqlx_jts;
		// if(hjxj_gyl_syqlx_zls != 0 || hjxj_gyl_syqlx_zrs != 0){
		// hjxj_gyl.syqlx_my_xj = ""+(hjxj_gyl_syqlx_zls+hjxj_gyl_syqlx_zrs);
		// }else{
		// hjxj_gyl.syqlx_my_xj = ""+hjxj_gyl_syqlx_my_xj;
		// }

		hjxj_gyl.syqlx_my_xj = "" + (hjxj_gyl_syqlx_zls + hjxj_gyl_syqlx_zrs);
		hjxj_gyl.syqlx_zls = "" + hjxj_gyl_syqlx_zls;
		hjxj_gyl.syqlx_zrs = "" + hjxj_gyl_syqlx_zrs;
		hjxj_gyl.syqlx_cbs = "" + hjxj_gyl_syqlx_cbs;
		hjxj_gyl.syqlx_qt = "" + hjxj_gyl_syqlx_qt;
		data.add(hjxj_gyl);

		// 合计
		LGAreaSum hjxj_spl = new LGAreaSum();
		// 统计单位
		hjxj_spl.tjdw = tjdwStr;
		// 林业用地_森林类别
		hjxj_spl.sllb = "商品林";
		// 按林权主体类型_总计
		double hjxj_spl_lqzt_hj = 0;
		// 按林权主体类型—国家
		double hjxj_spl_lqzt_gj = 0;
		// 按林权主体类型—集体
		double hjxj_spl_lqzt_jt = 0;
		// 按林权主体类型—民营
		double hjxj_spl_lqzt_my = 0;
		// 按林权主体类型__其他
		double hjxj_spl_lqzt_qt = 0;
		// 按使用权类型_——合计
		double hjxj_spl_syqlx_hj = 0;
		// 按使用权类型__国有山
		double hjxj_spl_syqlx_gys = 0;
		// 按使用权类型__集体山
		double hjxj_spl_syqlx_jts = 0;
		// 按使用权类型-民营-小计
		double hjxj_spl_syqlx_my_xj = 0;
		// 按使用权类型__自留山
		double hjxj_spl_syqlx_zls = 0;
		// 按使用权类型__责任山
		double hjxj_spl_syqlx_zrs = 0;
		// 按使用权类型__承包山
		double hjxj_spl_syqlx_cbs = 0;
		// 按使用权类型__其他
		double hjxj_spl_syqlx_qt = 0;

		// 合计
		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("面积") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("森林类别") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("森林类别").toString().contains("商")) {

				// 按林权主体类型——合计
				hjxj_spl_lqzt_hj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());
				if (list.get(i).getAttributeValue("林权主体类型") != null) {

					if (list.get(i).getAttributeValue("林权主体类型").toString()
							.contains("国")) {
						// 按林权主体类型—国家
						hjxj_spl_lqzt_gj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("林权主体类型")
							.toString().contains("集")) {
						// 按林权主体类型—集体
						hjxj_spl_lqzt_jt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("林权主体类型")
							.toString().contains("民")) {
						// 按林权主体类型—民营
						hjxj_spl_lqzt_my += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("林权主体类型")
							.toString().contains("其")) {
						// 按林权主体类型__其他
						hjxj_spl_lqzt_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else {
						// 按林权主体类型__其他
						hjxj_spl_lqzt_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					}
				} else {
					// 按林权主体类型__其他
					hjxj_spl_lqzt_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				}

				hjxj_spl_syqlx_hj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());
				if (list.get(i).getAttributeValue("使用权类型") != null) {

					if (list.get(i).getAttributeValue("使用权类型").toString()
							.contains("国")) {
						hjxj_spl_syqlx_gys += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("集")) {
						hjxj_spl_syqlx_jts += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("民")) {
						hjxj_spl_syqlx_my_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("自")) {
						hjxj_spl_syqlx_zls += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("责")) {
						hjxj_spl_syqlx_zrs += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("承")) {
						hjxj_spl_syqlx_cbs += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("其")) {
						hjxj_spl_syqlx_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else {
						hjxj_spl_syqlx_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					}
				} else {
					hjxj_spl_syqlx_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				}
			}
		}
		// 总面积
		hjxj_spl.lqzt_hj = "" + hjxj_spl_lqzt_hj;
		hjxj_spl.lqzt_gj = "" + hjxj_spl_lqzt_gj;
		hjxj_spl.lqzt_jt = "" + hjxj_spl_lqzt_jt;
		hjxj_spl.lqzt_my = "" + hjxj_spl_lqzt_my;
		hjxj_spl.lqzt_qt = "" + hjxj_spl_lqzt_qt;
		hjxj_spl.syqlx_hj = "" + hjxj_spl_syqlx_hj;
		hjxj_spl.syqlx_gys = "" + hjxj_spl_syqlx_gys;
		hjxj_spl.syqlx_jts = "" + hjxj_spl_syqlx_jts;
		// if(hjxj_spl_syqlx_zls != 0 || hjxj_spl_syqlx_zrs != 0){
		// hjxj_spl.syqlx_my_xj = ""+(hjxj_spl_syqlx_zls+hjxj_spl_syqlx_zrs);
		// }else{
		// hjxj_spl.syqlx_my_xj = ""+hjxj_spl_syqlx_my_xj;
		// }
		hjxj_spl.syqlx_my_xj = "" + (hjxj_spl_syqlx_zls + hjxj_spl_syqlx_zrs);
		hjxj_spl.syqlx_zls = "" + hjxj_spl_syqlx_zls;
		hjxj_spl.syqlx_zrs = "" + hjxj_spl_syqlx_zrs;
		hjxj_spl.syqlx_cbs = "" + hjxj_spl_syqlx_cbs;
		hjxj_spl.syqlx_qt = "" + hjxj_spl_syqlx_qt;
		data.add(hjxj_spl);

		// 合计
		LGAreaSum hjxj_qt = new LGAreaSum();
		// 统计单位
		hjxj_qt.tjdw = tjdwStr;
		// 林业用地_森林类别
		hjxj_qt.sllb = "其他";
		// 按林权主体类型_总计
		double hjxj_qt_lqzt_hj = 0;
		// 按林权主体类型—国家
		double hjxj_qt_lqzt_gj = 0;
		// 按林权主体类型—集体
		double hjxj_qt_lqzt_jt = 0;
		// 按林权主体类型—民营
		double hjxj_qt_lqzt_my = 0;
		// 按林权主体类型__其他
		double hjxj_qt_lqzt_qt = 0;
		// 按使用权类型_——合计
		double hjxj_qt_syqlx_hj = 0;
		// 按使用权类型__国有山
		double hjxj_qt_syqlx_gys = 0;
		// 按使用权类型__集体山
		double hjxj_qt_syqlx_jts = 0;
		// 按使用权类型——民营-小计
		double hjxj_qt_syqlx_my_xj = 0;
		// 按使用权类型__自留山
		double hjxj_qt_syqlx_zls = 0;
		// 按使用权类型__责任山
		double hjxj_qt_syqlx_zrs = 0;
		// 按使用权类型__承包山
		double hjxj_qt_syqlx_cbs = 0;
		// 按使用权类型__其他
		double hjxj_qt_syqlx_qt = 0;

		// 合计
		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("面积") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("森林类别") == null
					|| !(list.get(i).getAttributeValue("森林类别").toString()
							.contains("益") || list.get(i)
							.getAttributeValue("森林类别").toString().contains("商"))) {

				// 按林权主体类型——合计
				hjxj_qt_lqzt_hj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("林权主体类型") != null) {

					if (list.get(i).getAttributeValue("林权主体类型").toString()
							.contains("国")) {
						// 按林权主体类型—国家
						hjxj_qt_lqzt_gj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("林权主体类型")
							.toString().contains("集")) {
						// 按林权主体类型—集体
						hjxj_qt_lqzt_jt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("林权主体类型")
							.toString().contains("民")) {
						// 按林权主体类型—民营
						hjxj_qt_lqzt_my += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("林权主体类型")
							.toString().contains("其")) {
						// 按林权主体类型__其他
						hjxj_qt_lqzt_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else {
						// 按林权主体类型__其他
						hjxj_qt_lqzt_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					}
				} else {
					// 按林权主体类型__其他
					hjxj_qt_lqzt_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				}

				hjxj_qt_syqlx_hj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("使用权类型") != null) {

					if (list.get(i).getAttributeValue("使用权类型").toString()
							.contains("国")) {
						hjxj_qt_syqlx_gys += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("集")) {
						hjxj_qt_syqlx_jts += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("民")) {
						hjxj_qt_syqlx_my_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("自")) {
						hjxj_qt_syqlx_zls += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("责")) {
						hjxj_qt_syqlx_zrs += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("承")) {
						hjxj_qt_syqlx_cbs += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else if (list.get(i).getAttributeValue("使用权类型")
							.toString().contains("其")) {
						hjxj_qt_syqlx_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					} else {
						hjxj_qt_syqlx_qt += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					}
				} else {
					hjxj_qt_syqlx_qt += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				}
			}
		}
		// 总面积
		hjxj_qt.lqzt_hj = "" + hjxj_qt_lqzt_hj;
		hjxj_qt.lqzt_gj = "" + hjxj_qt_lqzt_gj;
		hjxj_qt.lqzt_jt = "" + hjxj_qt_lqzt_jt;
		hjxj_qt.lqzt_my = "" + hjxj_qt_lqzt_my;
		hjxj_qt.lqzt_qt = "" + hjxj_qt_lqzt_qt;
		hjxj_qt.syqlx_hj = "" + hjxj_qt_syqlx_hj;
		hjxj_qt.syqlx_gys = "" + hjxj_qt_syqlx_gys;
		hjxj_qt.syqlx_jts = "" + hjxj_qt_syqlx_jts;
		// if(hjxj_qt_syqlx_zls != 0 || hjxj_qt_syqlx_zrs != 0){
		// hjxj_qt.syqlx_my_xj = ""+(hjxj_qt_syqlx_zls+hjxj_qt_syqlx_zrs);
		// }else{
		// hjxj_qt.syqlx_my_xj = ""+hjxj_qt_syqlx_my_xj;
		// }
		hjxj_qt.syqlx_my_xj = "" + (hjxj_qt_syqlx_zls + hjxj_qt_syqlx_zrs);
		hjxj_qt.syqlx_zls = "" + hjxj_qt_syqlx_zls;
		hjxj_qt.syqlx_zrs = "" + hjxj_qt_syqlx_zrs;
		hjxj_qt.syqlx_cbs = "" + hjxj_qt_syqlx_cbs;
		hjxj_qt.syqlx_qt = "" + hjxj_qt_syqlx_qt;
		data.add(hjxj_qt);
		return data;

	}

	public static ArrayList<EDAreaSum> getEDData(List<GeodatabaseFeature> list,
			String tjdwStr) {
		ArrayList<EDAreaSum> data = new ArrayList<EDAreaSum>();
		int size = list.size();
		// 合计小计
		EDAreaSum hjxj = new EDAreaSum();
		// 统计单位
		hjxj.tjdw = tjdwStr;
		// 林地使用权
		hjxj.ldsyq = "合计";
		// 森林类别
		hjxj.sllb = "小计";
		// 总面积
		double hjxj_zmj = 0;
		// 林业用地合计
		double hjxj_lyyd_hj = 0;
		// 有林地小计
		double hjxj_yld_xj = 0;
		// 乔木林
		double hjxj_qml_xj = 0;
		// 纯林
		double hjxj_cl = 0;
		// 混交林
		double hjxj_hjl = 0;
		// 竹林
		double hjxj_zl = 0;
		// 疏林地
		double hjxj_sld = 0;
		// 灌木林地小计
		double hjxj_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_gmjjl = 0;
		// 其他灌木林地
		double hjxj_qtgmld = 0;
		// 未成林造地小計
		double hjxj_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_lgzlwcld = 0;
		// 封育未成林地
		double hjxj_fwwcld = 0;
		// 苗圃地
		double hjxj_mpd = 0;
		// 无立木林地小计
		double hjxj_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_cfjd = 0;
		// 火烧迹地
		double hjxj_hsjd = 0;
		// 其他无立木林地
		double hjxj_qtwlmld = 0;
		// 宜林地小计
		double hjxj_yild_xj = 0;
		// 荒山荒地
		double hjxj_hshd = 0;
		// 石山地
		double hjxj_ssd = 0;
		// 砂石山地
		double hjxj_sssd = 0;
		// 其他林宜地
		double hjxj_qtlyd = 0;
		// 辅助生产林地
		double hjxj_fzscld = 0;
		// 非林业用地合计
		double hjxj_fly_hj = 0;
		// 非林业用地_有林地
		double hjxj_fly_yld = 0;
		// 非林业用地_灌木林地_小计
		double hjxj_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_fly_qtgmld = 0;
		// 非林业其中≥25°坡耕地
		double hjxj_fly_qz_pgd = 0;
		// 非林业其中 其它非林地
		double hjxj_fly_qz_qtfld = 0;

		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("面积") == null) {
				continue;
			}

			// 林业用地1 非林业用地2
			if (list.get(i).getAttributeValue("用地性质") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("地类") == null) {
				continue;
			}

			hjxj_zmj += Double.valueOf(list.get(i).getAttributeValue("面积")
					.toString());

			// 林业用地
			if (list.get(i).getAttributeValue("用地性质").toString().equals("1")) {

				hjxj_lyyd_hj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").toString().equals("11"))
					// 纯林
					hjxj_cl += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").toString().equals("12"))
					// 混交林
					hjxj_hjl += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").toString().equals("13"))
					// 竹林
					hjxj_zl += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").toString().equals("20"))
					// 疏林地
					hjxj_sld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 灌木林地小计
				if (list.get(i).getAttributeValue("地类").toString()
						.startsWith("3"))

					hjxj_gmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").toString().equals("31"))
					// 国家特别灌木林
					hjxj_gjtbgmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 灌木经济林
				if (list.get(i).getAttributeValue("地类").toString().equals("31")) {
					if (list.get(i).getAttributeValue("区划林种") != null) {
						int m = Integer.parseInt(list.get(i)
								.getAttributeValue("区划林种").toString());
						if (m > 50 && m < 56) {
							hjxj_gmjjl += Double.valueOf(list.get(i)
									.getAttributeValue("面积").toString());
						}
					}
				}

				if (list.get(i).getAttributeValue("地类").toString().equals("32"))
					// 其他灌木林地
					hjxj_qtgmld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				// 未成林造林統計
				if (list.get(i).getAttributeValue("地类").toString()
						.startsWith("4"))

					hjxj_wclzl_xj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 人工造林未成林地
				if (list.get(i).getAttributeValue("地类").toString().equals("41"))
					hjxj_lgzlwcld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 封育未成林地
				if (list.get(i).getAttributeValue("地类").toString().equals("42"))
					hjxj_fwwcld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").toString().equals("50"))
					// 苗圃地
					hjxj_mpd += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").toString().equals("61"))
					// 采伐迹地
					hjxj_cfjd += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				if (list.get(i).getAttributeValue("地类").toString().equals("62"))
					// 火烧迹地
					hjxj_hsjd += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").toString().equals("63"))
					// 其它无立木林地
					hjxj_qtwlmld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				// 无立木林地
				if (list.get(i).getAttributeValue("地类").toString()
						.startsWith("6"))

					hjxj_wlmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 宜林地小计
				if (list.get(i).getAttributeValue("地类").toString()
						.startsWith("7"))

					hjxj_yild_xj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 荒山荒地
				if (list.get(i).getAttributeValue("地类").toString().equals("71"))

					hjxj_hshd += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 石山地
				if (list.get(i).getAttributeValue("地类").toString().equals("73"))

					hjxj_ssd += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 砂石山地
				if (list.get(i).getAttributeValue("地类").toString().equals("74"))

					hjxj_sssd += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 其他林宜地
				if (list.get(i).getAttributeValue("地类").toString().equals("72"))

					hjxj_qtlyd += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
				// 辅助生产林地
				if (list.get(i).getAttributeValue("地类").toString().equals("80"))

					hjxj_fzscld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

			}
			// 非林业用地
			else if (list.get(i).getAttributeValue("用地性质").toString()
					.equals("2")) {

				// 非林业用地合计
				hjxj_fly_hj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());

				// 有林地
				if (list.get(i).getAttributeValue("地类").toString()
						.startsWith("1"))
					hjxj_fly_yld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				// 灌木林地小计
				if (list.get(i).getAttributeValue("地类").toString()
						.startsWith("3"))

					hjxj_fly_gmld_xj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").equals("31"))
					// 国家特别灌木林小计

					hjxj_fly_gjtbgdgmld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").equals("32"))
					// 其他灌木林地
					hjxj_fly_qtgmld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").equals("91"))
					// ≥25°坡耕地
					hjxj_fly_qz_pgd += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("地类").equals("92"))
					// 其它非林地
					hjxj_fly_qz_qtfld += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());
			}
		}
		// 总面积
		hjxj.zmj = "" + hjxj_zmj;
		hjxj.lyyd_hj = "" + hjxj_lyyd_hj;
		hjxj_qml_xj = (hjxj_cl + hjxj_hjl);
		hjxj_yld_xj = (hjxj_qml_xj + hjxj_zl);
		hjxj.yld_xj = "" + hjxj_yld_xj;
		hjxj.qml_xj = "" + hjxj_qml_xj;
		hjxj.cl = "" + hjxj_cl;
		hjxj.hjl = "" + hjxj_hjl;
		hjxj.zl = "" + hjxj_zl;
		hjxj.sld = "" + hjxj_sld;
		hjxj.gmld_xj = "" + hjxj_gmld_xj;
		hjxj.gjtbgmld_xj = "" + hjxj_gjtbgmld_xj;
		hjxj.gmjjl = "" + hjxj_gmjjl;
		hjxj.qtgmld = "" + hjxj_qtgmld;
		hjxj.wclzl_xj = "" + hjxj_wclzl_xj;
		hjxj.lgzlwcld = "" + hjxj_lgzlwcld;
		hjxj.fwwcld = "" + hjxj_fwwcld;
		hjxj.mpd = "" + hjxj_mpd;
		hjxj.wlmld_xj = "" + hjxj_wlmld_xj;
		hjxj.cfjd = "" + hjxj_cfjd;
		hjxj.hsjd = "" + hjxj_hsjd;
		hjxj.qtwlmld = "" + hjxj_qtwlmld;
		hjxj.yldxj = "" + hjxj_yild_xj;
		hjxj.hshd = "" + hjxj_hshd;
		hjxj.ssd = "" + hjxj_ssd;
		hjxj.sssd = "" + hjxj_sssd;
		hjxj.qtlyd = "" + hjxj_qtlyd;
		hjxj.fzscld = "" + hjxj_fzscld;
		hjxj.flyyd_hj = "" + hjxj_fly_hj;
		hjxj.fly_yld = "" + hjxj_fly_yld;
		// 非林业灌木林地小计
		hjxj.fly_gmld_xj = "" + hjxj_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj.fly_gjtbgdgmld = "" + hjxj_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj.fly_qtgmld = "" + hjxj_fly_qtgmld;
		hjxj.fly_pld = "" + hjxj_fly_qz_pgd;
		hjxj.fly_qtfld = "" + hjxj_fly_qz_qtfld;

		data.add(hjxj);

		// 合计小计
		EDAreaSum hjxj_gyl = new EDAreaSum();
		// 统计单位
		hjxj_gyl.tjdw = tjdwStr;
		// 林地使用权
		hjxj_gyl.ldsyq = "合计";
		// 森林类别
		hjxj_gyl.sllb = "生态公益林(地)";
		double hjxj_gyl_zmj = 0;
		// 林业用地合计
		double hjxj_gyl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_gyl_yld_xj = 0;
		// 乔木林
		double hjxj_gyl_qml_xj = 0;
		// 纯林
		double hjxj_gyl_cl = 0;
		// 混交林
		double hjxj_gyl_hjl = 0;
		// 竹林
		double hjxj_gyl_zl = 0;
		// 疏林地
		double hjxj_gyl_sld = 0;
		// 灌木林地小计
		double hjxj_gyl_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gyl_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_gyl_gmjjl = 0;
		// 其他灌木林地
		double hjxj_gyl_qtgmld = 0;
		// 未成林造地小計
		double hjxj_gyl_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_gyl_lgzlwcld = 0;
		// 封育未成林地
		double hjxj_gyl_fwwcld = 0;
		// 苗圃地
		double hjxj_gyl_mpd = 0;
		// 无立木林地小计
		double hjxj_gyl_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_gyl_cfjd = 0;
		// 火烧迹地
		double hjxj_gyl_hsjd = 0;
		// 其他无立木林地
		double hjxj_gyl_qtwlmld = 0;
		// 宜林地小计
		double hjxj_gyl_yild_xj = 0;
		// 荒山荒地
		double hjxj_gyl_hshd = 0;
		// 石山地
		double hjxj_gyl_ssd = 0;
		// 砂石山地
		double hjxj_gyl_sssd = 0;
		// 其他林宜地
		double hjxj_gyl_qtlyd = 0;
		// 辅助生产林地
		double hjxj_gyl_fzscld = 0;
		// 非林业用地合计
		double hjxj_gyl_fly_hj = 0;
		// 非林业用地_有林地
		double hjxj_gyl_fly_yld = 0;
		// 非林业用地_灌木林地_小计
		double hjxj_gyl_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_gyl_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_gyl_fly_qtgmld = 0;
		// 非林业其中≥25°坡耕地
		double hjxj_gyl_fly_qz_pgd = 0;
		// 非林业其中 其它非林地
		double hjxj_gyl_fly_qz_qtfld = 0;

		for (int i = 0; i < size; i++) {
			// 10 11 12 13 公益林 20商品林
			// 总面积
			if (list.get(i).getAttributeValue("面积") == null) {
				continue;
			}
			if (list.get(i).getAttributeValue("森林分类") == null) {
				continue;
			}
			// 林业用地 合计
			if (list.get(i).getAttributeValue("森林分类").toString()
					.startsWith("1")) {

				if (list.get(i).getAttributeValue("用地性质") == null) {
					continue;
				}

				if (list.get(i).getAttributeValue("地类") == null) {
					continue;
				}

				hjxj_gyl_zmj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("用地性质").toString()
						.equals("1")) {

					hjxj_gyl_lyyd_hj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("11"))
						// 纯林
						hjxj_gyl_cl += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("12"))
						// 混交林
						hjxj_gyl_hjl += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("13"))
						// 竹林
						hjxj_gyl_zl += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("20"))
						// 疏林地
						hjxj_gyl_sld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 灌木林地小计
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("3"))

						hjxj_gyl_gmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("31"))
						// 国家特别灌木林小计
						hjxj_gyl_gjtbgmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("32"))
						// 其他灌木林地
						hjxj_gyl_qtgmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 灌木经济林
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("31")) {
						if (list.get(i).getAttributeValue("区划林种") != null) {
							int m = Integer.parseInt(list.get(i)
									.getAttributeValue("区划林种").toString());
							if (m > 50 && m < 56) {
								hjxj_gyl_gmjjl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							}
						}
					}

					// 未成林造林統計
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("4"))

						hjxj_gyl_wclzl_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 人工造林未成林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("41"))

						hjxj_gyl_lgzlwcld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 封育未成林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("42"))

						hjxj_gyl_fwwcld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("50"))
						// 苗圃地
						hjxj_gyl_mpd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("61"))
						// 采伐迹地
						hjxj_gyl_cfjd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("62"))
						// 火烧迹地
						hjxj_gyl_hsjd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("63"))
						// 其它无立木林地
						hjxj_gyl_qtwlmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 无立木林地
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("6"))

						hjxj_gyl_wlmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 宜林地小计
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("7"))
						hjxj_gyl_yild_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 荒山荒地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("71"))
						hjxj_gyl_hshd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 石山地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("73"))
						hjxj_gyl_ssd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 砂石山地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("74"))
						hjxj_gyl_sssd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 其他宜林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("72"))
						hjxj_gyl_qtlyd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 辅助生产林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("80"))

						hjxj_gyl_fzscld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

				} else if (list.get(i).getAttributeValue("用地性质").toString()
						.equals("2")) {

					// 非林业用地合计
					hjxj_gyl_fly_hj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

					// 有林地
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("1"))
						hjxj_gyl_fly_yld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 灌木林地小计
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("3"))

						hjxj_gyl_fly_gmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("31"))
						// 国家特别灌木林小计
						hjxj_gyl_fly_gjtbgdgmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("32"))
						// 其他灌木林地
						hjxj_gyl_fly_qtgmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("91"))
						// ≥25°坡耕地
						hjxj_gyl_fly_qz_pgd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("92"))
						// 其它非林地
						hjxj_gyl_fly_qz_qtfld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
				}
			}
		}
		// 总面积
		hjxj_gyl.zmj = "" + hjxj_gyl_zmj;
		hjxj_gyl.lyyd_hj = "" + hjxj_gyl_lyyd_hj;
		hjxj_gyl_qml_xj = (hjxj_gyl_cl + hjxj_gyl_hjl);
		hjxj_gyl_yld_xj = (hjxj_gyl_qml_xj + hjxj_gyl_zl);
		hjxj_gyl.yld_xj = "" + hjxj_gyl_yld_xj;
		hjxj_gyl.qml_xj = "" + hjxj_gyl_qml_xj;
		hjxj_gyl.cl = "" + hjxj_gyl_cl;
		hjxj_gyl.hjl = "" + hjxj_gyl_hjl;
		hjxj_gyl.zl = "" + hjxj_gyl_zl;
		hjxj_gyl.sld = "" + hjxj_gyl_sld;
		hjxj_gyl.gmld_xj = "" + hjxj_gyl_gmld_xj;
		hjxj_gyl.gjtbgmld_xj = "" + hjxj_gyl_gjtbgmld_xj;
		hjxj_gyl.gmjjl = "" + hjxj_gyl_gmjjl;
		hjxj_gyl.qtgmld = "" + hjxj_gyl_qtgmld;
		hjxj_gyl.wclzl_xj = "" + hjxj_gyl_wclzl_xj;
		hjxj_gyl.lgzlwcld = "" + hjxj_gyl_lgzlwcld;
		hjxj_gyl.fwwcld = "" + hjxj_gyl_fwwcld;
		hjxj_gyl.mpd = "" + hjxj_gyl_mpd;
		hjxj_gyl.wlmld_xj = "" + hjxj_gyl_wlmld_xj;
		hjxj_gyl.cfjd = "" + hjxj_gyl_cfjd;
		hjxj_gyl.hsjd = "" + hjxj_gyl_hsjd;
		hjxj_gyl.qtwlmld = "" + hjxj_gyl_qtwlmld;
		hjxj_gyl.yldxj = "" + hjxj_gyl_yild_xj;
		hjxj_gyl.hshd = "" + hjxj_gyl_hshd;
		hjxj_gyl.ssd = "" + hjxj_gyl_ssd;
		hjxj_gyl.sssd = "" + hjxj_gyl_sssd;
		hjxj_gyl.qtlyd = "" + hjxj_gyl_qtlyd;
		hjxj_gyl.fzscld = "" + hjxj_gyl_fzscld;
		hjxj_gyl.flyyd_hj = "" + hjxj_gyl_fly_hj;
		hjxj_gyl.fly_yld = "" + hjxj_gyl_fly_yld;
		// 非林业灌木林地小计
		hjxj_gyl.fly_gmld_xj = "" + hjxj_gyl_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_gyl.fly_gjtbgdgmld = "" + hjxj_gyl_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_gyl.fly_qtgmld = "" + hjxj_gyl_fly_qtgmld;
		hjxj_gyl.fly_pld = "" + hjxj_gyl_fly_qz_pgd;
		hjxj_gyl.fly_qtfld = "" + hjxj_gyl_fly_qz_qtfld;
		data.add(hjxj_gyl);

		// 合计小计
		EDAreaSum hjxj_spl = new EDAreaSum();
		// 统计单位
		hjxj_spl.tjdw = tjdwStr;
		// 林地使用权
		hjxj_spl.ldsyq = "合计";
		// 森林类别
		hjxj_spl.sllb = "商品林";
		double hjxj_spl_zmj = 0;
		// 林业用地合计
		double hjxj_spl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_spl_yld_xj = 0;
		// 乔木林
		double hjxj_spl_qml_xj = 0;
		// 纯林
		double hjxj_spl_cl = 0;
		// 混交林
		double hjxj_spl_hjl = 0;
		// 竹林
		double hjxj_spl_zl = 0;
		// 疏林地
		double hjxj_spl_sld = 0;
		// 灌木林地小计
		double hjxj_spl_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_spl_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_spl_gmjjl = 0;
		// 其他灌木林地
		double hjxj_spl_qtgmld = 0;
		// 未成林造地小計
		double hjxj_spl_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_spl_lgzlwcld = 0;
		// 封育未成林地
		double hjxj_spl_fwwcld = 0;
		// 苗圃地
		double hjxj_spl_mpd = 0;
		// 无立木林地小计
		double hjxj_spl_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_spl_cfjd = 0;
		// 火烧迹地
		double hjxj_spl_hsjd = 0;
		// 其他无立木林地
		double hjxj_spl_qtwlmld = 0;
		// 宜林地小计
		double hjxj_spl_yild_xj = 0;
		// 荒山荒地
		double hjxj_spl_hshd = 0;
		// 石山地
		double hjxj_spl_ssd = 0;
		// 砂石山地
		double hjxj_spl_sssd = 0;
		// 其他林宜地
		double hjxj_spl_qtlyd = 0;
		// 辅助生产林地
		double hjxj_spl_fzscld = 0;
		// 非林业用地合计
		double hjxj_spl_fly_hj = 0;
		// 非林业用地_有林地
		double hjxj_spl_fly_yld = 0;
		// 非林业用地_灌木林地_小计
		double hjxj_spl_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_spl_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_spl_fly_qtgmld = 0;
		// 非林业其中≥25°坡耕地
		double hjxj_spl_fly_qz_pgd = 0;
		// 非林业其中 其它非林地
		double hjxj_spl_fly_qz_qtfld = 0;

		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("面积") == null) {
				continue;
			}
			if (list.get(i).getAttributeValue("森林分类") == null) {
				continue;
			}
			// 林业用地 合计
			if (list.get(i).getAttributeValue("森林分类").toString()
					.startsWith("2")) {

				if (list.get(i).getAttributeValue("用地性质") == null) {
					continue;
				}
				if (list.get(i).getAttributeValue("地类") == null) {
					continue;
				}

				hjxj_spl_zmj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("用地性质").toString()
						.equals("1")) {
					hjxj_spl_lyyd_hj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("11"))
						// 纯林
						hjxj_spl_cl += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("12"))
						// 混交林
						hjxj_spl_hjl += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("13"))
						// 竹林
						hjxj_spl_zl += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("20"))
						// 疏林地
						hjxj_spl_sld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 灌木林地小计
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("3"))

						hjxj_spl_gmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("31"))
						// 国家特别灌木林小计
						hjxj_spl_gjtbgmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("32"))
						// 其他灌木林地
						hjxj_spl_qtgmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 灌木经济林
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("31")) {
						if (list.get(i).getAttributeValue("区划林种") != null) {
							int m = Integer.parseInt(list.get(i)
									.getAttributeValue("区划林种").toString());
							if (m > 50 && m < 56) {
								hjxj_spl_gmjjl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							}
						}
					}

					// 未成林造林統計
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("4"))

						hjxj_spl_wclzl_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 人工造林未成林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("41"))

						hjxj_spl_lgzlwcld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 封育未成林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("42"))

						hjxj_spl_fwwcld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("50"))
						// 苗圃地
						hjxj_spl_mpd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("61"))
						// 采伐迹地
						hjxj_spl_cfjd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("62"))
						// 火烧迹地
						hjxj_spl_hsjd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("63"))
						// 其它无立木林地
						hjxj_spl_qtwlmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 无立木林地
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("6"))

						hjxj_spl_wlmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 宜林地小计
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("7"))

						hjxj_spl_yild_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 荒山荒地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("71"))

						hjxj_spl_hshd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 石山地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("73"))

						hjxj_spl_ssd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 砂石山地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("74"))

						hjxj_spl_sssd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 其他林宜地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("72"))

						hjxj_spl_qtlyd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 辅助生产林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("80"))

						hjxj_spl_fzscld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

				} else if (list.get(i).getAttributeValue("用地性质").toString()
						.equals("2")) {

					// 非林业用地合计
					hjxj_spl_fly_hj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

					// 有林地
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("1"))
						hjxj_spl_fly_yld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 灌木林地小计
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("3"))

						hjxj_spl_fly_gmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("31"))
						// 国家特别灌木林小计

						hjxj_spl_fly_gjtbgdgmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("32"))
						// 其他灌木林地
						hjxj_spl_fly_qtgmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("91"))
						// ≥25°坡耕地
						hjxj_spl_fly_qz_pgd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("92"))
						// 其它非林地
						hjxj_spl_fly_qz_qtfld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
				}
			}
		}
		// 总面积
		hjxj_spl.zmj = "" + hjxj_spl_zmj;
		hjxj_spl.lyyd_hj = "" + hjxj_spl_lyyd_hj;
		hjxj_spl_qml_xj = (hjxj_spl_cl + hjxj_spl_hjl);
		hjxj_spl_yld_xj = (hjxj_spl_qml_xj + hjxj_spl_zl);
		hjxj_spl.yld_xj = "" + hjxj_spl_yld_xj;
		hjxj_spl.qml_xj = "" + hjxj_spl_qml_xj;
		hjxj_spl.cl = "" + hjxj_spl_cl;
		hjxj_spl.hjl = "" + hjxj_spl_hjl;
		hjxj_spl.zl = "" + hjxj_spl_zl;
		hjxj_spl.sld = "" + hjxj_spl_sld;
		hjxj_spl.gmld_xj = "" + hjxj_spl_gmld_xj;
		hjxj_spl.gjtbgmld_xj = "" + hjxj_spl_gjtbgmld_xj;
		hjxj_spl.gmjjl = "" + hjxj_spl_gmjjl;
		hjxj_spl.qtgmld = "" + hjxj_spl_qtgmld;
		hjxj_spl.wclzl_xj = "" + hjxj_spl_wclzl_xj;
		hjxj_spl.lgzlwcld = "" + hjxj_spl_lgzlwcld;
		hjxj_spl.fwwcld = "" + hjxj_spl_fwwcld;
		hjxj_spl.mpd = "" + hjxj_spl_mpd;
		hjxj_spl.wlmld_xj = "" + hjxj_spl_wlmld_xj;
		hjxj_spl.cfjd = "" + hjxj_spl_cfjd;
		hjxj_spl.hsjd = "" + hjxj_spl_hsjd;
		hjxj_spl.qtwlmld = "" + hjxj_spl_qtwlmld;
		hjxj_spl.yldxj = "" + hjxj_spl_yild_xj;
		hjxj_spl.hshd = "" + hjxj_spl_hshd;
		hjxj_spl.ssd = "" + hjxj_spl_ssd;
		hjxj_spl.sssd = "" + hjxj_spl_sssd;
		hjxj_spl.qtlyd = "" + hjxj_spl_qtlyd;
		hjxj_spl.fzscld = "" + hjxj_spl_fzscld;
		hjxj_spl.flyyd_hj = "" + hjxj_spl_fly_hj;
		hjxj_spl.fly_yld = "" + hjxj_spl_fly_yld;
		// 非林业灌木林地小计
		hjxj_spl.fly_gmld_xj = "" + hjxj_spl_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_spl.fly_gjtbgdgmld = "" + hjxj_spl_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_spl.fly_qtgmld = "" + hjxj_spl_fly_qtgmld;

		hjxj_spl.fly_pld = "" + hjxj_spl_fly_qz_pgd;
		hjxj_spl.fly_qtfld = "" + hjxj_spl_fly_qz_qtfld;
		data.add(hjxj_spl);

		// 合计小计
		EDAreaSum hjxj_qt = new EDAreaSum();
		// 统计单位
		hjxj_qt.tjdw = tjdwStr;
		// 林地使用权
		hjxj_qt.ldsyq = "合计";
		// 森林类别
		hjxj_qt.sllb = "其他";
		double hjxj_qt_zmj = 0;
		// 林业用地合计
		double hjxj_qt_lyyd_hj = 0;
		// 有林地小计
		double hjxj_qt_yld_xj = 0;
		// 乔木林
		double hjxj_qt_qml_xj = 0;
		// 纯林
		double hjxj_qt_cl = 0;
		// 混交林
		double hjxj_qt_hjl = 0;
		// 竹林
		double hjxj_qt_zl = 0;
		// 疏林地
		double hjxj_qt_sld = 0;
		// 灌木林地小计
		double hjxj_qt_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_qt_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_qt_gmjjl = 0;
		// 其他灌木林地
		double hjxj_qt_qtgmld = 0;
		// 未成林造地小計
		double hjxj_qt_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_qt_lgzlwcld = 0;
		// 封育未成林地
		double hjxj_qt_fwwcld = 0;
		// 苗圃地
		double hjxj_qt_mpd = 0;
		// 无立木林地小计
		double hjxj_qt_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_qt_cfjd = 0;
		// 火烧迹地
		double hjxj_qt_hsjd = 0;
		// 其他无立木林地
		double hjxj_qt_qtwlmld = 0;
		// 宜林地小计
		double hjxj_qt_yild_xj = 0;
		// 荒山荒地
		double hjxj_qt_hshd = 0;
		// 石山地
		double hjxj_qt_ssd = 0;
		// 砂石山地
		double hjxj_qt_sssd = 0;
		// 其他林宜地
		double hjxj_qt_qtlyd = 0;
		// 辅助生产林地
		double hjxj_qt_fzscld = 0;
		// 非林业用地合计
		double hjxj_qt_fly_hj = 0;
		// 非林业用地_有林地
		double hjxj_qt_fly_yld = 0;
		// 非林业用地_灌木林地_小计
		double hjxj_qt_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_qt_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_qt_fly_qtgmld = 0;
		// 非林业其中≥25°坡耕地
		double hjxj_qt_fly_qz_pgd = 0;
		// 非林业其中 其它非林地
		double hjxj_qt_fly_qz_qtfld = 0;

		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("面积") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("森林分类") == null
					|| !(list.get(i).getAttributeValue("森林分类").toString()
							.startsWith("1") || list.get(i)
							.getAttributeValue("森林分类").toString()
							.startsWith("2"))) {

				// 1 林业用地 2非林业用地
				if (list.get(i).getAttributeValue("用地性质") == null) {
					continue;
				}

				if (list.get(i).getAttributeValue("地类") == null) {
					continue;
				}

				hjxj_qt_zmj += Double.valueOf(list.get(i)
						.getAttributeValue("面积").toString());

				if (list.get(i).getAttributeValue("用地性质").toString()
						.equals("1")) {// 林业用地

					hjxj_qt_lyyd_hj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("11"))
						// 纯林
						hjxj_qt_cl += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("12"))
						// 混交林
						hjxj_qt_hjl += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("13"))
						// 竹林
						hjxj_qt_zl += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("20"))
						// 疏林地
						hjxj_qt_sld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 灌木林地小计
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("3"))

						hjxj_qt_gmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("31"))
						// 国家特别灌木林小计
						hjxj_qt_gjtbgmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("32"))
						// 其他灌木林地
						hjxj_qt_qtgmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 灌木经济林
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("31")) {
						if (list.get(i).getAttributeValue("区划林种") != null) {
							int m = Integer.parseInt(list.get(i)
									.getAttributeValue("区划林种").toString());
							if (m > 50 && m < 56) {
								hjxj_qt_gmjjl += Double.valueOf(list.get(i)
										.getAttributeValue("面积").toString());
							}
						}
					}

					// 未成林造林統計
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("4"))

						hjxj_qt_wclzl_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 人工造林未成林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("41"))

						hjxj_qt_lgzlwcld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 封育未成林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("42"))

						hjxj_qt_fwwcld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("50"))
						// 苗圃地
						hjxj_qt_mpd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("61"))
						// 采伐迹地
						hjxj_qt_cfjd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("62"))
						// 火烧迹地
						hjxj_qt_hsjd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.equals("63"))
						// 其它无立木林地
						hjxj_qt_qtwlmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 无立木林地
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("6"))

						hjxj_qt_wlmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 宜林地小计
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("7"))

						hjxj_qt_yild_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 荒山荒地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("71"))

						hjxj_qt_hshd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 石山地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("73"))

						hjxj_qt_ssd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 砂石山地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("74"))

						hjxj_qt_sssd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 其他林宜地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("72"))

						hjxj_qt_qtlyd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
					// 辅助生产林地
					if (list.get(i).getAttributeValue("地类").toString()
							.equals("80"))

						hjxj_qt_fzscld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

				} else if (list.get(i).getAttributeValue("用地性质").toString()
						.equals("2")) {

					// 非林业用地合计
					hjxj_qt_fly_hj += Double.valueOf(list.get(i)
							.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("1"))
						hjxj_qt_fly_yld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					// 灌木林地小计
					if (list.get(i).getAttributeValue("地类").toString()
							.startsWith("3"))

						hjxj_qt_fly_gmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("31"))
						// 国家特别灌木林小计

						hjxj_qt_fly_gjtbgdgmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("32"))
						// 其他灌木林地
						hjxj_qt_fly_qtgmld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("91"))
						// ≥25°坡耕地
						hjxj_qt_fly_qz_pgd += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());

					if (list.get(i).getAttributeValue("地类").equals("92"))
						// 其它非林地
						hjxj_qt_fly_qz_qtfld += Double.valueOf(list.get(i)
								.getAttributeValue("面积").toString());
				}
			}
		}
		// 总面积
		hjxj_qt.zmj = "" + hjxj_qt_zmj;
		hjxj_qt.lyyd_hj = "" + hjxj_qt_lyyd_hj;
		hjxj_qt_qml_xj = (hjxj_qt_cl + hjxj_qt_hjl);
		hjxj_qt_yld_xj = (hjxj_qt_qml_xj + hjxj_qt_zl);
		hjxj_qt.yld_xj = "" + hjxj_qt_yld_xj;
		hjxj_qt.qml_xj = "" + hjxj_qt_qml_xj;
		hjxj_qt.cl = "" + hjxj_qt_cl;
		hjxj_qt.hjl = "" + hjxj_qt_hjl;
		hjxj_qt.zl = "" + hjxj_qt_zl;
		hjxj_qt.sld = "" + hjxj_qt_sld;
		hjxj_qt.gmld_xj = "" + hjxj_qt_gmld_xj;
		hjxj_qt.gjtbgmld_xj = "" + hjxj_qt_gjtbgmld_xj;
		hjxj_qt.gmjjl = "" + hjxj_qt_gmjjl;
		hjxj_qt.qtgmld = "" + hjxj_qt_qtgmld;
		hjxj_qt.wclzl_xj = "" + hjxj_qt_wclzl_xj;
		hjxj_qt.lgzlwcld = "" + hjxj_qt_lgzlwcld;
		hjxj_qt.fwwcld = "" + hjxj_qt_fwwcld;
		hjxj_qt.mpd = "" + hjxj_qt_mpd;
		hjxj_qt.wlmld_xj = "" + hjxj_qt_wlmld_xj;
		hjxj_qt.cfjd = "" + hjxj_qt_cfjd;
		hjxj_qt.hsjd = "" + hjxj_qt_hsjd;
		hjxj_qt.qtwlmld = "" + hjxj_qt_qtwlmld;
		hjxj_qt.yldxj = "" + hjxj_qt_yild_xj;
		hjxj_qt.hshd = "" + hjxj_qt_hshd;
		hjxj_qt.ssd = "" + hjxj_qt_ssd;
		hjxj_qt.sssd = "" + hjxj_qt_sssd;
		hjxj_qt.qtlyd = "" + hjxj_qt_qtlyd;
		hjxj_qt.fzscld = "" + hjxj_qt_fzscld;
		hjxj_qt.flyyd_hj = "" + hjxj_qt_fly_hj;
		hjxj_qt.fly_yld = "" + hjxj_qt_fly_yld;
		// 非林业灌木林地小计
		hjxj_qt.fly_gmld_xj = "" + hjxj_qt_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_qt.fly_gjtbgdgmld = "" + hjxj_qt_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_qt.fly_qtgmld = "" + hjxj_qt_fly_qtgmld;

		hjxj_qt.fly_pld = "" + hjxj_qt_fly_qz_pgd;
		hjxj_qt.fly_qtfld = "" + hjxj_qt_fly_qz_qtfld;
		data.add(hjxj_qt);
		hjxj.zmj = "" + (hjxj_gyl_zmj + hjxj_spl_zmj + hjxj_qt_zmj);
		return data;
	}

	public static ArrayList<LDAreaSum> getLDLJData(
			List<GeodatabaseFeature> list, String tjdwStr) {
		ArrayList<LDAreaSum> data = new ArrayList<LDAreaSum>();
		int size = list.size();
		// 合计小计
		LDAreaSum hjxj = new LDAreaSum();
		// 统计单位

		hjxj.tjdw = tjdwStr;
		// 林地所有权
		hjxj.ldsyq = "合计";
		// 森林类别
		hjxj.sllb = "小计";
		// 总面积
		double hjxj_zmj = 0;
		// 林业用地合计
		double hjxj_lyyd_hj = 0;
		// 有林地小计
		double hjxj_yld_xj = 0;
		// 乔木林
		double hjxj_qml = 0;
		// 竹林
		double hjxj_zl = 0;
		// 疏林地
		double hjxj_sld = 0;
		// 灌木林地小计
		double hjxj_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_gmjjl = 0;
		// 其他灌木林地
		double hjxj_qtgmld = 0;
		// 未成林造地小計
		double hjxj_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_rgzlwcld = 0;
		// 封育未成林地
		double hjxj_fywcld = 0;
		// 苗圃地
		double hjxj_mpd = 0;
		// 无立木林地小计
		double hjxj_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_cfjd = 0;
		// 火烧迹地
		double hjxj_hsjd = 0;
		// 其他无立木林地
		double hjxj_qtwlmld_xj = 0;
		// 林业用地 宜林地小计
		double hjxj_yild_xj = 0;
		// 荒山荒地
		double hjxj_hshd = 0;
		// 沙荒地
		double hjxj_shd = 0;
		// 其他林宜地
		double hjxj_qtyld = 0;
		// 辅助生产林地
		double hjxj_fzscld = 0;
		// 非林业用地合计
		double hjxj_flyyd_hj = 0;
		// 非林业用地 有林地
		double hjxj_flyyd_yld = 0;
		// 非林业灌木林地小计
		double hjxj_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_fly_qtgmld = 0;

		for (int i = 0; i < size; i++) {

			// 总面积
			if (list.get(i).getAttributeValue("MIAN_JI") == null) {
				continue;
			}

			hjxj_zmj += Double.valueOf(list.get(i).getAttributeValue("MIAN_JI")
					.toString());

			if (list.get(i).getAttributeValue("GLLX") != null) {

				if (list.get(i).getAttributeValue("DI_LEI") == null) {
					continue;
				}
				// 林业用地合计
				if (list.get(i).getAttributeValue("GLLX").equals("10")) {

					hjxj_lyyd_hj += Double.valueOf(list.get(i)
							.getAttributeValue("MIAN_JI").toString());
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("011"))
						hjxj_yld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0111"))
						// 乔木林
						hjxj_qml += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0113"))
						// 竹林
						hjxj_zl += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0120"))
						// 疏林地
						hjxj_sld += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					// 灌木林地小计
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("013"))

						hjxj_gmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0131")) {
						// 国家特别灌木林小计
						hjxj_gjtbgmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					}

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0131")) {
						if (list.get(i).getAttributeValue("LIN_ZHONG") != null
								&& list.get(i).getAttributeValue("LIN_ZHONG")
										.toString().startsWith("25")) {
							// 灌木经济林
							hjxj_gmjjl += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						}
					}

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0132"))
						// 其他灌木林地
						hjxj_qtgmld += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					// 未成林造林統計
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("014"))
						hjxj_wclzl_xj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					// 人工造林未成林地
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0141"))
						hjxj_rgzlwcld += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0142"))
						hjxj_fywcld += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0150"))
						// 苗圃地
						hjxj_mpd += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					// if(list.get(i).getAttributeValue("DI_LEI").toString().contains("016"))
					// hjxj_wlmld_xj += Double.valueOf(list.get(i)
					// .getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0161"))
						// 采伐迹地
						hjxj_cfjd += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0162"))
						// 火烧迹地
						hjxj_hsjd += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					// 其他无立木林地
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.startsWith("163"))
						hjxj_qtwlmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					// 宜林地小计
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("017"))
						hjxj_yild_xj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					// 荒山荒地
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0171"))
						hjxj_hshd += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					// 沙荒地
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0172"))

						hjxj_shd += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					// 其他林宜地
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0173"))

						hjxj_qtyld += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					// 辅助生产林地
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0180"))

						hjxj_fzscld += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

				} else if (list.get(i).getAttributeValue("GLLX").equals("20")) {// 非林业用地合计

					hjxj_flyyd_hj += Double.valueOf(list.get(i)
							.getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.startsWith("011")) {
						hjxj_flyyd_yld += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
					}
					// 灌木林地小计
					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("013"))
						hjxj_fly_gmld_xj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0131"))
						// 国家特别灌木林
						hjxj_fly_gjtbgdgmld += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

					if (list.get(i).getAttributeValue("DI_LEI").toString()
							.contains("0132"))
						// 其他灌木林地
						hjxj_fly_qtgmld += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
				}
			}

		}
		hjxj.zmj = "" + hjxj_zmj;
		hjxj.lyyd_hj = "" + hjxj_lyyd_hj;
		hjxj.yld_xj = "" + hjxj_yld_xj;
		hjxj.qml = "" + hjxj_qml;
		hjxj.zl = "" + hjxj_zl;
		hjxj.sld = "" + hjxj_sld;
		hjxj.gmld_xj = "" + hjxj_gmld_xj;
		hjxj.gjtbgmld_xj = "" + hjxj_gjtbgmld_xj;
		hjxj.gmjjl = "" + hjxj_gmjjl;
		hjxj.qtgmld = "" + hjxj_qtgmld;
		hjxj.wclzl_xj = "" + hjxj_wclzl_xj;
		hjxj.rgzlwclzld = "" + hjxj_rgzlwcld;
		hjxj.fywcld = "" + hjxj_fywcld;
		hjxj.mpd = "" + hjxj_mpd;
		hjxj.wlmld_xj = "" + (hjxj_cfjd + hjxj_hsjd + hjxj_qtwlmld_xj);
		hjxj.cfjd = "" + hjxj_cfjd;
		hjxj.hsjd = "" + hjxj_hsjd;
		hjxj.qtwlmld = "" + hjxj_qtwlmld_xj;
		hjxj.yildxj = "" + hjxj_yild_xj;
		hjxj.hshd = "" + hjxj_hshd;
		hjxj.shd = "" + hjxj_shd;
		hjxj.qtyld = "" + hjxj_qtyld;
		hjxj.fzscld = "" + hjxj_fzscld;
		hjxj.flyyd_hj = "" + hjxj_flyyd_hj;
		hjxj.fly_yld = "" + hjxj_flyyd_yld;
		// 非林业灌木林地小计
		hjxj.fly_gmld_xj = "" + hjxj_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj.fly_gjtbgdgmld = "" + hjxj_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj.fly_qtgmld = "" + hjxj_fly_qtgmld;
		data.add(hjxj);

		// 合计小计_公益林
		LDAreaSum hjxj_gyl = new LDAreaSum();
		// 统计单位
		hjxj_gyl.tjdw = tjdwStr;
		// 林地使用权
		hjxj_gyl.ldsyq = "合计";
		// 森林类别
		hjxj_gyl.sllb = "公益林地";
		// 总面积
		double hjxj_gyl_zmj = 0;
		// 林业用地合计
		double hjxj_gyl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_gyl_yld_xj = 0;
		// 乔木林
		double hjxj_gyl_qml = 0;
		// 竹林
		double hjxj_gyl_zl = 0;
		// 疏林地
		double hjxj_gyl_sld = 0;
		// 灌木林地小计
		double hjxj_gyl_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_gyl_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_gyl_gmjjl = 0;
		// 其他灌木林地
		double hjxj_gyl_qtgmld = 0;
		// 未成林造地小計
		double hjxj_gyl_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_gyl_rgzlwcld = 0;
		// 封育未成林地
		double hjxj_gyl_fywcld = 0;
		// 苗圃地
		double hjxj_gyl_mpd = 0;
		// 无立木林地小计
		double hjxj_gyl_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_gyl_cfjd = 0;
		// 火烧迹地
		double hjxj_gyl_hsjd = 0;
		// 其他无立木林地
		double hjxj_gyl_qtwlmld_xj = 0;
		// 林业用地 宜林地小计
		double hjxj_gyl_yild_xj = 0;
		// 荒山荒地
		double hjxj_gyl_hshd = 0;
		// 沙荒地
		double hjxj_gyl_shd = 0;
		// 其他林宜地
		double hjxj_gyl_qtyld = 0;
		// 辅助生产林地
		double hjxj_gyl_fzscld = 0;
		// 非林业用地合计
		double hjxj_gyl_flyyd_hj = 0;
		// 非林业用地 有林地
		double hjxj_gyl_flyyd_yld = 0;
		// 非林业灌木林地小计
		double hjxj_gyl_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_gyl_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_gyl_fly_qtgmld = 0;

		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("MIAN_JI") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("SEN_LIN_LB") != null
					&& list.get(i).getAttributeValue("SEN_LIN_LB")
							.equals("011")) {

				hjxj_gyl_zmj += Double.valueOf(list.get(i)
						.getAttributeValue("MIAN_JI").toString());
				if (list.get(i).getAttributeValue("GLLX") != null) {

					if (list.get(i).getAttributeValue("DI_LEI") == null) {
						continue;
					}
					// 林业用地合计
					if (list.get(i).getAttributeValue("GLLX").equals("10")) {

						hjxj_gyl_lyyd_hj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("011"))
							hjxj_gyl_yld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0111"))
							// 乔木林
							hjxj_gyl_qml += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0113"))
							// 竹林
							hjxj_gyl_zl += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0120"))
							// 疏林地
							hjxj_gyl_sld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 灌木林地小计
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("013"))

							hjxj_gyl_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0131")) {
							// 国家特别灌木林小计
							hjxj_gyl_gjtbgmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						}

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0131")) {
							if (list.get(i).getAttributeValue("LIN_ZHONG") != null
									&& list.get(i)
											.getAttributeValue("LIN_ZHONG")
											.toString().startsWith("25")) {
								// 灌木经济林
								hjxj_gyl_gmjjl += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							}
						}

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0132"))
							// 其他灌木林地
							hjxj_gyl_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 未成林造林統計
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("014"))
							hjxj_gyl_wclzl_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 人工造林未成林地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0141"))
							hjxj_gyl_rgzlwcld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0142"))
							hjxj_gyl_fywcld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0150"))
							// 苗圃地
							hjxj_gyl_mpd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						// if(list.get(i).getAttributeValue("DI_LEI").toString().startsWith("016"))
						// hjxj_gyl_wlmld_xj += Double.valueOf(list.get(i)
						// .getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0161"))
							// 采伐迹地
							hjxj_gyl_cfjd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0162"))
							// 火烧迹地
							hjxj_gyl_hsjd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 其他无立木林地
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("163"))
							hjxj_gyl_qtwlmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						// 宜林地小计
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("017"))
							hjxj_gyl_yild_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						// 荒山荒地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0171"))
							hjxj_gyl_hshd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 沙荒地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0172"))

							hjxj_gyl_shd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 其他林宜地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0173"))

							hjxj_gyl_qtyld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 辅助生产林地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0180"))

							hjxj_gyl_fzscld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

					} else if (list.get(i).getAttributeValue("GLLX")
							.equals("20")) {// 非林业用地合计

						hjxj_gyl_flyyd_hj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("011")) {
							hjxj_gyl_flyyd_yld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						}
						// 灌木林地小计
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("013"))
							hjxj_gyl_fly_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0131"))
							// 国家特别灌木林
							hjxj_gyl_fly_gjtbgdgmld += Double.valueOf(list
									.get(i).getAttributeValue("MIAN_JI")
									.toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0132"))
							// 其他灌木林地
							hjxj_gyl_fly_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
					}
				}
			}
		}
		hjxj_gyl.zmj = "" + hjxj_gyl_zmj;
		hjxj_gyl.lyyd_hj = "" + hjxj_gyl_lyyd_hj;
		hjxj_gyl.yld_xj = "" + hjxj_gyl_yld_xj;
		hjxj_gyl.qml = "" + hjxj_gyl_qml;
		hjxj_gyl.zl = "" + hjxj_gyl_zl;
		hjxj_gyl.sld = "" + hjxj_gyl_sld;
		hjxj_gyl.gmld_xj = "" + hjxj_gyl_gmld_xj;
		hjxj_gyl.gjtbgmld_xj = "" + hjxj_gyl_gjtbgmld_xj;
		hjxj_gyl.gmjjl = "" + hjxj_gyl_gmjjl;
		hjxj_gyl.qtgmld = "" + hjxj_gyl_qtgmld;
		hjxj_gyl.wclzl_xj = "" + hjxj_gyl_wclzl_xj;
		hjxj_gyl.rgzlwclzld = "" + hjxj_gyl_rgzlwcld;
		hjxj_gyl.fywcld = "" + hjxj_gyl_fywcld;
		hjxj_gyl.mpd = "" + hjxj_gyl_mpd;
		hjxj_gyl.wlmld_xj = ""
				+ (hjxj_gyl_cfjd + hjxj_gyl_hsjd + hjxj_gyl_qtwlmld_xj);
		hjxj_gyl.cfjd = "" + hjxj_gyl_cfjd;
		hjxj_gyl.hsjd = "" + hjxj_gyl_hsjd;
		hjxj_gyl.qtwlmld = "" + hjxj_gyl_qtwlmld_xj;
		hjxj_gyl.yildxj = "" + hjxj_gyl_yild_xj;
		hjxj_gyl.hshd = "" + hjxj_gyl_hshd;
		hjxj_gyl.shd = "" + hjxj_gyl_shd;
		hjxj_gyl.qtyld = "" + hjxj_gyl_qtyld;
		hjxj_gyl.fzscld = "" + hjxj_gyl_fzscld;
		hjxj_gyl.flyyd_hj = "" + hjxj_gyl_flyyd_hj;
		hjxj_gyl.fly_yld = "" + hjxj_gyl_flyyd_yld;
		// 非林业灌木林地小计
		hjxj_gyl.fly_gmld_xj = "" + hjxj_gyl_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_gyl.fly_gjtbgdgmld = "" + hjxj_gyl_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_gyl.fly_qtgmld = "" + hjxj_gyl_fly_qtgmld;
		data.add(hjxj_gyl);

		// 合计小计_商品林
		LDAreaSum hjxj_spl = new LDAreaSum();
		// 统计单位
		hjxj_spl.tjdw = tjdwStr;
		// 林地使用权
		hjxj_spl.ldsyq = "合计";
		// 森林类别
		hjxj_spl.sllb = "商品林地";
		// 总面积
		double hjxj_spl_zmj = 0;
		// 林业用地合计
		double hjxj_spl_lyyd_hj = 0;
		// 有林地小计
		double hjxj_spl_yld_xj = 0;
		// 乔木林
		double hjxj_spl_qml = 0;
		// 竹林
		double hjxj_spl_zl = 0;
		// 疏林地
		double hjxj_spl_sld = 0;
		// 灌木林地小计
		double hjxj_spl_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_spl_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_spl_gmjjl = 0;
		// 其他灌木林地
		double hjxj_spl_qtgmld = 0;
		// 未成林造地小計
		double hjxj_spl_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_spl_rgzlwcld = 0;
		// 封育未成林地
		double hjxj_spl_fywcld = 0;
		// 苗圃地
		double hjxj_spl_mpd = 0;
		// 无立木林地小计
		double hjxj_spl_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_spl_cfjd = 0;
		// 火烧迹地
		double hjxj_spl_hsjd = 0;
		// 其他无立木林地
		double hjxj_spl_qtwlmld_xj = 0;
		// 林业用地 宜林地小计
		double hjxj_spl_yild_xj = 0;
		// 荒山荒地
		double hjxj_spl_hshd = 0;
		// 沙荒地
		double hjxj_spl_shd = 0;
		// 其他林宜地
		double hjxj_spl_qtyld = 0;
		// 辅助生产林地
		double hjxj_spl_fzscld = 0;
		// 非林业用地合计
		double hjxj_spl_flyyd_hj = 0;
		// 非林业用地 有林地
		double hjxj_spl_flyyd_yld = 0;
		// 非林业灌木林地小计
		double hjxj_spl_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_spl_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_spl_fly_qtgmld = 0;

		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("MIAN_JI") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("SEN_LIN_LB") != null
					&& list.get(i).getAttributeValue("SEN_LIN_LB")
							.equals("012")) {

				hjxj_spl_zmj += Double.valueOf(list.get(i)
						.getAttributeValue("MIAN_JI").toString());
				if (list.get(i).getAttributeValue("GLLX") != null) {

					if (list.get(i).getAttributeValue("DI_LEI") == null) {
						continue;
					}
					// 林业用地合计
					if (list.get(i).getAttributeValue("GLLX").equals("10")) {

						hjxj_spl_lyyd_hj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("011"))
							hjxj_spl_yld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0111"))
							// 乔木林
							hjxj_spl_qml += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0113"))
							// 竹林
							hjxj_spl_zl += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0120"))
							// 疏林地
							hjxj_spl_sld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 灌木林地小计
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("013"))

							hjxj_spl_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0131")) {
							// 国家特别灌木林小计
							hjxj_spl_gjtbgmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						}

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0131")) {
							if (list.get(i).getAttributeValue("LIN_ZHONG") != null
									&& list.get(i)
											.getAttributeValue("LIN_ZHONG")
											.toString().startsWith("25")) {
								// 灌木经济林
								hjxj_spl_gmjjl += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							}
						}

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0132"))
							// 其他灌木林地
							hjxj_spl_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 未成林造林統計
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("014"))
							hjxj_spl_wclzl_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 人工造林未成林地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0141"))
							hjxj_spl_rgzlwcld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0142"))
							hjxj_spl_fywcld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0150"))
							// 苗圃地
							hjxj_spl_mpd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						// if(list.get(i).getAttributeValue("DI_LEI").toString().startsWith("016"))
						// hjxj_spl_wlmld_xj += Double.valueOf(list.get(i)
						// .getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0161"))
							// 采伐迹地
							hjxj_spl_cfjd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0162"))
							// 火烧迹地
							hjxj_spl_hsjd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 其他无立木林地
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("163"))
							hjxj_spl_qtwlmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						// 宜林地小计
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("017"))
							hjxj_spl_yild_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						// 荒山荒地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0171"))
							hjxj_spl_hshd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 沙荒地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0172"))

							hjxj_spl_shd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 其他林宜地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0173"))

							hjxj_spl_qtyld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 辅助生产林地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0180"))

							hjxj_spl_fzscld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

					} else if (list.get(i).getAttributeValue("GLLX")
							.equals("20")) {// 非林业用地合计

						hjxj_spl_flyyd_hj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("011")) {
							hjxj_spl_flyyd_yld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						}
						// 灌木林地小计
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("013"))
							hjxj_spl_fly_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0131"))
							// 国家特别灌木林
							hjxj_spl_fly_gjtbgdgmld += Double.valueOf(list
									.get(i).getAttributeValue("MIAN_JI")
									.toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0132"))
							// 其他灌木林地
							hjxj_spl_fly_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
					}
				}
			}
		}
		hjxj_spl.zmj = "" + hjxj_spl_zmj;
		hjxj_spl.lyyd_hj = "" + hjxj_spl_lyyd_hj;
		hjxj_spl.yld_xj = "" + hjxj_spl_yld_xj;
		hjxj_spl.qml = "" + hjxj_spl_qml;
		hjxj_spl.zl = "" + hjxj_spl_zl;
		hjxj_spl.sld = "" + hjxj_spl_sld;
		hjxj_spl.gmld_xj = "" + hjxj_spl_gmld_xj;
		hjxj_spl.gjtbgmld_xj = "" + hjxj_spl_gjtbgmld_xj;
		hjxj_spl.gmjjl = "" + hjxj_spl_gmjjl;
		hjxj_spl.qtgmld = "" + hjxj_spl_qtgmld;
		hjxj_spl.wclzl_xj = "" + hjxj_spl_wclzl_xj;
		hjxj_spl.rgzlwclzld = "" + hjxj_spl_rgzlwcld;
		hjxj_spl.fywcld = "" + hjxj_spl_fywcld;
		hjxj_spl.mpd = "" + hjxj_spl_mpd;
		hjxj_spl.wlmld_xj = ""
				+ (hjxj_spl_cfjd + hjxj_spl_hsjd + hjxj_spl_qtwlmld_xj);
		hjxj_spl.cfjd = "" + hjxj_spl_cfjd;
		hjxj_spl.hsjd = "" + hjxj_spl_hsjd;
		hjxj_spl.qtwlmld = "" + hjxj_spl_qtwlmld_xj;
		hjxj_spl.yildxj = "" + hjxj_spl_yild_xj;
		hjxj_spl.hshd = "" + hjxj_spl_hshd;
		hjxj_spl.shd = "" + hjxj_spl_shd;
		hjxj_spl.qtyld = "" + hjxj_spl_qtyld;
		hjxj_spl.fzscld = "" + hjxj_spl_fzscld;
		hjxj_spl.flyyd_hj = "" + hjxj_spl_flyyd_hj;
		hjxj_spl.fly_yld = "" + hjxj_spl_flyyd_yld;
		// 非林业灌木林地小计
		hjxj_spl.fly_gmld_xj = "" + hjxj_spl_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_spl.fly_gjtbgdgmld = "" + hjxj_spl_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_spl.fly_qtgmld = "" + hjxj_spl_fly_qtgmld;
		data.add(hjxj_spl);

		// 合计小计_其他
		LDAreaSum hjxj_qt = new LDAreaSum();
		// 统计单位
		hjxj_qt.tjdw = tjdwStr;
		// 林地使用权
		hjxj_qt.ldsyq = "合计";
		// 森林类别
		hjxj_qt.sllb = "其他";
		// 总面积
		double hjxj_qt_zmj = 0;
		// 林业用地合计
		double hjxj_qt_lyyd_hj = 0;
		// 有林地小计
		double hjxj_qt_yld_xj = 0;
		// 乔木林
		double hjxj_qt_qml = 0;
		// 竹林
		double hjxj_qt_zl = 0;
		// 疏林地
		double hjxj_qt_sld = 0;
		// 灌木林地小计
		double hjxj_qt_gmld_xj = 0;
		// 国家特别灌木林小计
		double hjxj_qt_gjtbgmld_xj = 0;
		// 灌木经济林
		double hjxj_qt_gmjjl = 0;
		// 其他灌木林地
		double hjxj_qt_qtgmld = 0;
		// 未成林造地小計
		double hjxj_qt_wclzl_xj = 0;
		// 人工造林未成林地
		double hjxj_qt_rgzlwcld = 0;
		// 封育未成林地
		double hjxj_qt_fywcld = 0;
		// 苗圃地
		double hjxj_qt_mpd = 0;
		// 无立木林地小计
		double hjxj_qt_wlmld_xj = 0;
		// 采伐迹地
		double hjxj_qt_cfjd = 0;
		// 火烧迹地
		double hjxj_qt_hsjd = 0;
		// 其他无立木林地
		double hjxj_qt_qtwlmld_xj = 0;
		// 林业用地 宜林地小计
		double hjxj_qt_yild_xj = 0;
		// 荒山荒地
		double hjxj_qt_hshd = 0;
		// 沙荒地
		double hjxj_qt_shd = 0;
		// 其他林宜地
		double hjxj_qt_qtyld = 0;
		// 辅助生产林地
		double hjxj_qt_fzscld = 0;
		// 非林业用地合计
		double hjxj_qt_flyyd_hj = 0;
		// 非林业用地 有林地
		double hjxj_qt_flyyd_yld = 0;
		// 非林业灌木林地小计
		double hjxj_qt_fly_gmld_xj = 0;
		// 非林业国家特别灌木林
		double hjxj_qt_fly_gjtbgdgmld = 0;
		// 非林业其他灌木林地
		double hjxj_qt_fly_qtgmld = 0;

		for (int i = 0; i < size; i++) {
			// 总面积
			if (list.get(i).getAttributeValue("MIAN_JI") == null) {
				continue;
			}

			if (list.get(i).getAttributeValue("SEN_LIN_LB") == null
					|| !(list.get(i).getAttributeValue("SEN_LIN_LB")
							.equals("011") || list.get(i)
							.getAttributeValue("SEN_LIN_LB").equals("012"))) {

				hjxj_qt_zmj += Double.valueOf(list.get(i)
						.getAttributeValue("MIAN_JI").toString());
				if (list.get(i).getAttributeValue("GLLX") != null) {

					if (list.get(i).getAttributeValue("DI_LEI") == null) {
						continue;
					}
					// 林业用地合计
					if (list.get(i).getAttributeValue("GLLX").equals("10")) {

						hjxj_qt_lyyd_hj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("011"))
							hjxj_qt_yld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0111"))
							// 乔木林
							hjxj_qt_qml += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0113"))
							// 竹林
							hjxj_qt_zl += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0120"))
							// 疏林地
							hjxj_qt_sld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 灌木林地小计
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("013"))

							hjxj_qt_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0131")) {
							// 国家特别灌木林小计
							hjxj_qt_gjtbgmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						}

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0131")) {
							if (list.get(i).getAttributeValue("LIN_ZHONG") != null
									&& list.get(i)
											.getAttributeValue("LIN_ZHONG")
											.toString().startsWith("25")) {
								// 灌木经济林
								hjxj_qt_gmjjl += Double.valueOf(list.get(i)
										.getAttributeValue("MIAN_JI")
										.toString());
							}
						}

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0132"))
							// 其他灌木林地
							hjxj_qt_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 未成林造林統計
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("014"))
							hjxj_qt_wclzl_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 人工造林未成林地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0141"))
							hjxj_qt_rgzlwcld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0142"))
							hjxj_qt_fywcld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0150"))
							// 苗圃地
							hjxj_qt_mpd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						// if(list.get(i).getAttributeValue("DI_LEI").toString().startsWith("016"))
						// hjxj_qt_wlmld_xj += Double.valueOf(list.get(i)
						// .getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0161"))
							// 采伐迹地
							hjxj_qt_cfjd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0162"))
							// 火烧迹地
							hjxj_qt_hsjd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 其他无立木林地
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("163"))
							hjxj_qt_qtwlmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						// 宜林地小计
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("017"))
							hjxj_qt_yild_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						// 荒山荒地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0171"))
							hjxj_qt_hshd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 沙荒地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0172"))

							hjxj_qt_shd += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 其他林宜地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0173"))

							hjxj_qt_qtyld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						// 辅助生产林地
						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0180"))

							hjxj_qt_fzscld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

					} else if (list.get(i).getAttributeValue("GLLX")
							.equals("20")) {// 非林业用地合计

						hjxj_qt_flyyd_hj += Double.valueOf(list.get(i)
								.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("011")) {
							hjxj_qt_flyyd_yld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
						}
						// 灌木林地小计
						if (list.get(i).getAttributeValue("DI_LEI").toString()
								.startsWith("013"))
							hjxj_qt_fly_gmld_xj += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0131"))
							// 国家特别灌木林
							hjxj_qt_fly_gjtbgdgmld += Double.valueOf(list
									.get(i).getAttributeValue("MIAN_JI")
									.toString());

						if (list.get(i).getAttributeValue("DI_LEI")
								.equals("0132"))
							// 其他灌木林地
							hjxj_qt_fly_qtgmld += Double.valueOf(list.get(i)
									.getAttributeValue("MIAN_JI").toString());
					}
				}
			}
		}
		hjxj_qt.zmj = "" + hjxj_qt_zmj;
		hjxj_qt.lyyd_hj = "" + hjxj_qt_lyyd_hj;
		hjxj_qt.yld_xj = "" + hjxj_qt_yld_xj;
		hjxj_qt.qml = "" + hjxj_qt_qml;
		hjxj_qt.zl = "" + hjxj_qt_zl;
		hjxj_qt.sld = "" + hjxj_qt_sld;
		hjxj_qt.gmld_xj = "" + hjxj_qt_gmld_xj;
		hjxj_qt.gjtbgmld_xj = "" + hjxj_qt_gjtbgmld_xj;
		hjxj_qt.gmjjl = "" + hjxj_qt_gmjjl;
		hjxj_qt.qtgmld = "" + hjxj_qt_qtgmld;
		hjxj_qt.wclzl_xj = "" + hjxj_qt_wclzl_xj;
		hjxj_qt.rgzlwclzld = "" + hjxj_qt_rgzlwcld;
		hjxj_qt.fywcld = "" + hjxj_qt_fywcld;
		hjxj_qt.mpd = "" + hjxj_qt_mpd;
		hjxj_qt.wlmld_xj = ""
				+ (hjxj_qt_cfjd + hjxj_qt_hsjd + hjxj_qt_qtwlmld_xj);
		hjxj_qt.cfjd = "" + hjxj_qt_cfjd;
		hjxj_qt.hsjd = "" + hjxj_qt_hsjd;
		hjxj_qt.qtwlmld = "" + hjxj_qt_qtwlmld_xj;
		hjxj_qt.yildxj = "" + hjxj_qt_yild_xj;
		hjxj_qt.hshd = "" + hjxj_qt_hshd;
		hjxj_qt.shd = "" + hjxj_qt_shd;
		hjxj_qt.qtyld = "" + hjxj_qt_qtyld;
		hjxj_qt.fzscld = "" + hjxj_qt_fzscld;
		hjxj_qt.flyyd_hj = "" + hjxj_qt_flyyd_hj;
		hjxj_qt.fly_yld = "" + hjxj_qt_flyyd_yld;
		// 非林业灌木林地小计
		hjxj_qt.fly_gmld_xj = "" + hjxj_qt_fly_gmld_xj;
		// 非林业国家特别灌木林
		hjxj_qt.fly_gjtbgdgmld = "" + hjxj_qt_fly_gjtbgdgmld;
		// 非林业其他灌木林地
		hjxj_qt.fly_qtgmld = "" + hjxj_qt_fly_qtgmld;
		data.add(hjxj_qt);
		return data;
	}

	public static ArrayList<LgyjAreaSum> getLGYJData(
			List<GeodatabaseFeature> list, String tjdwStr) {
		ArrayList<LgyjAreaSum> data = new ArrayList<LgyjAreaSum>();
		int size = list.size();
		// 合计小计
		LgyjAreaSum hjxj = new LgyjAreaSum();
		// 统计单位
		hjxj.tjdw = tjdwStr;
		// 乡镇
		String xiang = "";
		// 村、林班
		String cun = "";
		// 合计 起数
		double hjqs = 0;
		// 合计面积
		double hjmj = 0;
		// 1、国家级、省级重点项目 起数
		double gjsjqs = 0;
		// 1、国家级、省级重点项目 面积
		double gjsjmj = 0;
		// 2、市（州）批准及招商引资项目 起数
		double shipzqs = 0;
		// 2、市（州）批准及招商引资项目 面积
		double shipzmj = 0;
		// 3、县（市、区、特区）批准的招商引资项目 起数
		double xianpzqs = 0;
		// 3、县（市、区、特区）批准的招商引资项目 面积
		double xianpzmj = 0;
		// 4、各级各类园区内建设项目 起数
		double gjyqqs = 0;
		// 4、各级各类园区内建设项目 面积
		double gjyqmj = 0;
		// 5、采矿、采石采砂取土及其他项目 起数
		double ckqtqs = 0;
		// 5、采矿、采石采砂取土及其他项目 面积
		double ckqtmj = 0;

		for (int i = 0; i < size; i++) {
			GeodatabaseFeature feature = list.get(i);
			Object obj = feature.getAttributeValue("XMLB");
			if (obj == null) {
				continue;
			}

			hjqs++;

			Object xiang_obj = feature.getAttributeValue("乡名称");
			if (xiang_obj == null) {
				xiang = "";
			} else {
				xiang = xiang_obj.toString();
			}

			Object cun_obj = feature.getAttributeValue("村名称");
			if (cun_obj == null) {
				cun = "";
			} else {
				cun = cun_obj.toString();
			}
			String xmlb = obj.toString();
			if (xmlb.contains("国家级")) {

			} else if (xmlb.contains("省级")) {

			} else if (xmlb.contains("市（州）批准")) {

			} else if (xmlb.contains("县（市、区、特区）批准")) {

			} else if (xmlb.contains("各类园区")) {

			} else if (xmlb.contains("采矿、采石")) {

			} else if (xmlb.contains("其他")) {

			} else if (xmlb.contains("其它")) {

			}
		}
		return data;
	}

}

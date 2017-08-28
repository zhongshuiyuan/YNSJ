package com.titan.ynsjy.statistics;

import java.io.Serializable;
/**违法使用林地案件清单*/
public class LdqdAreaSum implements Serializable {
	/* */
	private static final long serialVersionUID = 1L;
	
	/**统计单位 */
	public String tjdw = "0";
	/**案件编号 */
	public String anjbh = "0";
	/**案件名称 */
	public String anjmc = "0";
	/**项目类别 */
	public String xmlb = "0";
	/**违法使用林地责任单位（责任人）名称（姓名） */
	public String zrrmc = "0";
	/**责任单位法定代表人姓名*/
	public String dbrmc = "0";
	/**涉及本次核实的疑似图斑编号 */
	public String ystbbh = "0";
	/**涉及林地落界小班号 */
	public String ljxbh = "0";
	/**开工日期 */
	public String kgrq = "0";
	/**违法使用林地面积（公顷）*/
	public double wfsyldmj = 0;
	/**违法使用林地地类*/
	public String lddl = "0";
	/**违法使用林地的森林类别 */
	public String sllb = "0";
	/**违法使用林地类型 */
	public String ldlx = "0";
	/**违法使用林地的行为类型 */
	public String ldxwlx = "0";
	/**违法使用林地行为的查处情况*/
	public String ccqk = "0";
	/**违法使用林地行为的依法处理情况及查处依据*/
	public String clqk = "0";
	/**案件性质*/
	public String anjxz = "0";
	/**有无无证采伐 */
	public String ywwzcf = "0";
	/**无证采伐面积(公顷） */
	public double wzcfmj = 0;
	/**无证采伐蓄积（m3） */
	public double wzcfxj = 0;
	/**无证采伐林木的查处情况*/
	public String wzccqk = "0";
	/**督办级别 */
	public String dbjb = "0";
	/**备注 */
	public String bz = "0";
	
}

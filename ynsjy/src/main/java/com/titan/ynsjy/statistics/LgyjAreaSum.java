package com.titan.ynsjy.statistics;

import java.io.Serializable;

public class LgyjAreaSum implements Serializable {
	/* */
	private static final long serialVersionUID = 1L;
	
	/**统计单位 */
	public String tjdw = "0";
	/**乡镇 */
	public String xiang = "0";
	/**村 */
	public String cun = "0";
	/**合计起数 */
	public double hjqs = 0;
	/**合计面积 */
	public double hjmj = 0;
	/**国家省级重点项目起数 */
	public double gjsjzdqs = 0;
	/**国家省级重点项目面积 */
	public double gjsjzdmj = 0;
	/**市批准招商引资项目起数 */
	public double shipzxmqs = 0;
	/**市批准招商引资项目面积 */
	public double shipzxmmj = 0;
	/**县（市、区、特区）批准的招商引资项目起数 */
	public double xianzsqs = 0;
	/**县（市、区、特区）批准的招商引资项目面积 */
	public double xianzsmj = 0;
	/**各级各类园区内建设项目起数 */
	public double gjnjxmqs = 0;
	/**各级各类园区内建设项目面积 */
	public double gjnjxmmj = 0;
	/**采矿、采石采砂取土及其他项目起数 */
	public double ckcsxmqs = 0;
	/**采矿、采石采砂取土及其他项目面积 */
	public double ckcsxmmj = 0;
}

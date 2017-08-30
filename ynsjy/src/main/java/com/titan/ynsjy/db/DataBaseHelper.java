package com.titan.ynsjy.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.esri.core.geometry.Point;
import com.titan.model.Photo;
import com.titan.ynsjy.dao.DaoMaster;
import com.titan.ynsjy.dao.DaoMaster.DevOpenHelper;
import com.titan.ynsjy.dao.DaoSession;
import com.titan.ynsjy.dao.Jgdc;
import com.titan.ynsjy.dao.JgdcDao;
import com.titan.ynsjy.dao.JgdcDao.Properties;
import com.titan.ynsjy.dao.Ym;
import com.titan.ynsjy.dao.YmDao;
import com.titan.ynsjy.entity.GjPoint;
import com.titan.ynsjy.entity.Row;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ResourcesManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import jsqlite.Callback;
import jsqlite.Database;

public class DataBaseHelper{
	public static DataBaseHelper dataBaseHelperUtil;

	public static DaoMaster daoMaster;
	public static DaoSession daoSession;
	
	public static DataBaseHelper getDataBaseHelperUtil() {
		return dataBaseHelperUtil;
	}

	public static void setDataBaseHelperUtil(DataBaseHelper dataBaseHelperUtil) {
		DataBaseHelper.dataBaseHelperUtil = dataBaseHelperUtil;
	}

	public synchronized static DataBaseHelper getInstance(Context context) {
		return dataBaseHelperUtil;
	}

	/**
	 * 新增照片
	 * @param context
	 * @param photo
	 * @return
	 */
	public static boolean addNewPhoto(Context context, Photo photo) {
		boolean flag = false;
		try {
			String databaseName = ResourcesManager.getInstance(context).getDataBase("guiji.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into photos(FK_FXH_UID,TIME,INFO,REMARK,ADDEESS,URI,Geometry) "
					+ " values("+photo.getFk_Fxh_Uid()+","
					+ photo.getTime() + ","
					+ photo.getInfo() + ","
					+ photo.getRemark() + ","
					+ photo.getAddeess() + ","
					+ photo.getUri() + ","
					+ "geomfromtext('POINT(" + photo.getLon() + " " + photo.getLat() + ")',4490))";
			db.exec(sql, null);
			db.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}
	
	/**����ֶε�geodatabase���ݿ����sqlite���ݿ�
	 * dbpath:���ݿ��ļ����ش��·��
	 * tbname:Ҫ��ӵ����ݿ������*/
	public static void addColumnToTab(Context context,String dbpath,String tbname,String column){
		try {
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(dbpath, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "ALTER TABLE "+tbname+" ADD "+column+" TEXT";
			db.exec(sql, null);
			db.close();
			addDataToTab(context, dbpath, tbname, column);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	/** ����ֶε�ͬʱ�ڱ��м�������Ӷε���Ϣ GDB_ColumnRegistry*/
	public static void addDataToTab(Context context,String dbpath,String tbname,String column){
		try {
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(dbpath, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into GDB_ColumnRegistry values('"+tbname+"','"+column+"',5,50,null,null,4,null)";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	/**�������ݿ�WYBH*/
	public static void updateWYBH(Context context,String dbpath,String tbname,String value,long id){
		try {
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(dbpath, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "UPDATE "+tbname+" SET WYBH = '"+value+"' WHERE OBJECTID = '"+id+"' ";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/** �����ľ�������ݵ��������ݿ� */
	public static int addDataToYmdc(Context context, String path) {
		try {
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(path, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into YZL_YMDCB values(null)";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return getTabaleMaxID(context, "YZL_YMDCB", path);
	}

	/** ��ȡС�������� */
	public static List<String> getXdmType(Context context) {
		final List<String> list = new ArrayList<String>();
		try {
			String databaseName = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select distinct type from station";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6
					list.add(data[0]);
					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ����С���� */
	public static List<HashMap<String, Object>> getAddressInfo(Context context,
			String keyword) {
		final List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			String databaseName = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from station where name like '%" + keyword + "%'";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6
					Object name = data[2];
					String lon = data[3];
					String lau = data[4];
					String type = data[5];
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("filename", name + "(" + type + ")");
					Point point = new Point(Double.valueOf(lon), Double
							.valueOf(lau));
					map.put("point", point);
					list.add(map);
					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 本地保存轨迹数据集 */
	public static boolean addPointGuiji(Context context, String sbh, double lon,
			double lat, String time, String state) {
		boolean flag = false;
		try {
			String databaseName = ResourcesManager.getInstance(context).getDataBase("guiji.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into point values(null," + lon + "," + lat
					+ ",'" + sbh + "','" + time + "'," + state
					+ ",geomfromtext('POINT(" + lon + " " + lat + ")',2343))";
			db.exec(sql, null);
			db.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/** 本地轨迹数据点添加 */
	public boolean addGuijiData(Context context, String sbh, double lon,
								double lat, String time, String state,String dbpath) {
		boolean flag = false;
		try {
			String databaseName = ResourcesManager.getInstance(context).getDataBase(dbpath);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into point values(null," + lon + "," + lat
					+ ",'" + sbh + "','" + time + "'," + state
					+ ",geomfromtext('POINT(" + lon + " " + lat + ")',2343))";
			db.exec(sql, null);
			db.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 估计数据查询*/
	public static List<GjPoint> selectPointGuiji(Context context,String sbh, String startTime, String endTime) {
		final List<GjPoint> list = new ArrayList<GjPoint>();
		try {
			String databaseName = ResourcesManager.getInstance(context).getDataBase("guiji.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "SELECT * FROM point WHERE SBH ='" + sbh
					+ "' and time between datetime('" + startTime
					+ "') and datetime('" + endTime
					+ "') order by datetime(time) desc";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6
					int id = Integer.parseInt(data[0]);
					GjPoint point = new GjPoint(id,data[1],data[2],data[3],data[4],data[5]);
					list.add(point);
					return false;
				}

				@Override
				public void columns(String[] arg0) {
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** �����������ص��ѯ��ľ */
	public static List<Ym> getYms(Context context, String ydh, String path) {
		List<Ym> list = new ArrayList<Ym>();
		try {
			DevOpenHelper<Ym> helper = new DevOpenHelper<Ym>(context, Ym.class, path);
			SQLiteDatabase db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
			YmDao ymdao = daoSession.getYmDao();
			QueryBuilder<Ym> serym = ymdao.queryBuilder().where(YmDao.Properties.YDH.eq(ydh));
			list = serym.list();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	/** ����������ľ�Ÿ�����ľ 
	 * @param <T>*/
	public static <T> boolean updateYm(Context context, Ym ym, String path) {
		try {
			
			DevOpenHelper<Ym> helper = new DevOpenHelper<Ym>(context, Ym.class, path);
			SQLiteDatabase db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
			YmDao ymdao = daoSession.getYmDao();
			ymdao.update(ym);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** ���������ľ */
	public static boolean addYm(Context context, Ym newym, String path) {
		try {
			DevOpenHelper<Ym> helper = new DevOpenHelper<Ym>(context, Ym.class, path);
			SQLiteDatabase db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
			YmDao ymdao = daoSession.getYmDao();
			ymdao.insertOrReplace(newym);

			db.close();
			return true;
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}

	/** ����������ľ��ɾ����ľ */
	public static boolean deleteYm(Context context, String ymbh, String path) {
		try {
			DevOpenHelper<Ym> helper = new DevOpenHelper<Ym>(context,Ym.class, path);
			SQLiteDatabase db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
			YmDao ymdao = daoSession.getYmDao();
			QueryBuilder<Ym> qb = ymdao.queryBuilder();
			DeleteQuery<Ym> bd = qb.where(YmDao.Properties.YDH.eq(ymbh)).buildDelete();
			bd.executeDeleteWithoutDetachingEntities();
			db.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** ��������С����Ƶ��ѯ�ǹ����  */
	public static Jgdc getJgdc(Context context, String kzdh, String path)
	{
		try {
			DevOpenHelper<Jgdc> helper = new DevOpenHelper<Jgdc>(context,Jgdc.class, path);
			SQLiteDatabase db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
			JgdcDao jgdcdao = daoSession.getJgdcDao();
			QueryBuilder<Jgdc> serjgdc = jgdcdao.queryBuilder().where(Properties.KZDH.eq(kzdh));
			List<Jgdc> list = serjgdc.list();
			if (list.size() > 0) {
				return list.get(0);
			}

			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**��������С����Ƶ��ѯ�ǹ����*/
	public static List<Map<String, String>> getJgdc(Context context,
			String kzdh, final List<Map<String, String>> list, String path)
	{
		try {
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(path, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "SELECT * FROM JGDC_TB WHERE KZDH ='" + kzdh + "' ";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {
					if (data.length > 0) {
						for (int i = 0; i < data.length; i++) {
							Map<String, String> feildmap = list.get(i);
							feildmap.put("value", data[i]);
						}
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/** �������ݿ��Ƶ�Ÿ��½ǹ�������Ϣ */
	public static boolean updateJgdc(Context context, Jgdc jg, String path)
	{
		try {
			DevOpenHelper<Jgdc> helper = new DevOpenHelper<Jgdc>(context,Jgdc.class, path);
			SQLiteDatabase db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
			JgdcDao jgdcdao = daoSession.getJgdcDao();
			jgdcdao.update(jg);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	/**�ǹ���������Ƿ����*/
	public static boolean isHasJgdc(Context context, String kzdh,String path) {
		DevOpenHelper<Jgdc> helper = new DevOpenHelper<Jgdc>(context,Jgdc.class, path);
		SQLiteDatabase db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		JgdcDao jgdcdao = daoSession.getJgdcDao();
		List<Jgdc> list = jgdcdao.queryBuilder().where(Properties.KZDH.eq(kzdh)).list();
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/** ��ѯС������ʷ���� */
	public static List<HashMap<String, Object>> selDataHistory(Context context) {
		final List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			String dbName = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(dbName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from HISTORY where time order by datetime(time) desc limit 15";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("filename", data[0]);
					map.put("time", data[1]);
					map.put("lon", data[2]);
					map.put("lat", data[3]);
					list.add(map);
					return false;
				}

				@Override
				public void columns(String[] arg0) {
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ���С�������������ݿ� */
	public static void addPointToSearchData(Context context, String lon,
			String lat, String dbname, String name, String type) {
		try {
			String databaseName = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into station values(null,null,'" + name
					+ "','" + lon + "','" + lat + "','" + type + "',null)";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** �������������������ʷ���ݿ��� */
	@SuppressLint("SimpleDateFormat")
	public static void addDataToHistory(Context context, String searchTxt) {
		try {
			String databaseName = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql = "insert into HISTORY values('" + searchTxt + "','"
					+ dateFormat.format(new Date()) + "',null,null)";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static boolean sel_result = false;

	/** ��ѯ����������ʷ�� */
	public static boolean selDataHistoryByString(Context context,
			String searchTxt) {
		try {
			String databaseName = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from HISTORY where name ='" + searchTxt
					+ "'";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						sel_result = true;
					}
					return true;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sel_result;
	}

	/** ��ȡ�û���ʷ���� */
	public static ArrayList<Map<String, String>> getUserList(Context context,
			String dbname) {
		final ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from user ";
			db.exec(sql, new Callback() {

				@Override
				public boolean newrow(String[] data) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", data[0]);
					map.put("pw", data[1]);
					list.add(map);
					return false;
				}

				@Override
				public void columns(String[] arg1) {

				}

				@Override
				public void types(String[] arg2) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** �û���¼��֤ */
	static String loginResult = "";

	public static String checkLogin(Context context, String dbname, String name) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			final Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from user where name = '" + name + "'";
			db.exec(sql, new Callback() {

				@Override
				public boolean newrow(String[] data) {
					if (!(data.length > 0)) {
						loginResult = "�û���������";
					} else {
						loginResult = data[0] + ":" + data[1];
					}
					return false;
				}

				@Override
				public void columns(String[] arg1) {

				}

				@Override
				public void types(String[] arg2) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return loginResult;
	}

	/** ��ȡ�û���ʷ���� */
	public static void checkUserList(final Context context,
			final String dbname, final String username, final String psw) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from user where name = " + username;
			db.exec(sql, new Callback() {

				@Override
				public boolean newrow(String[] data) {
					if (!(data.length > 0)) {
						addUserName(context, dbname, username, psw);
					}
					return false;
				}

				@Override
				public void columns(String[] arg1) {

				}

				@Override
				public void types(String[] arg2) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ӵ�¼�û��������û��� */
	public static void addUserName(Context context, String dbname,
			String username, String psw) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into user values(" + username + "," + psw
					+ ")";
			db.exec(sql, new Callback() {

				@Override
				public boolean newrow(String[] data) {

					return false;
				}

				@Override
				public void columns(String[] arg1) {

				}

				@Override
				public void types(String[] arg2) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� �к�����̤������� ���������ݿ� */
	public static void addYhswTcdData(Context context, String dbname,
			String dcdbh, String dclxbh, String dcr, String dctime,
			String dcdw, String lon, String lat, String hb, String xdm,
			String xbh, String xbmj, String jzmc, String cbtj, String lfzc,
			String whbw, String yhswmc, String mcwhcd, String ct,
			String mcwhmj, String ly, String bz, String city, String county,
			String town, String village, String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTDCDTCB (DCDID,DCLXID,DCPERSON,DCTIME,DCDEPARTMENT,LON,LAT,ALTITUDE,TOPONYMY,XBH,XBAREA,JZNAME,CBTJ,LFZC,WHBW,YHSWNAME,MCWHCD,CT,MCWHMJ,SOURCE,REMARK,CITY,COUNTY,TOWN,VILLAGE,UPLOADSTATUS,SBR,SBSJ) values('"
					+ dcdbh
					+ "','"
					+ dclxbh
					+ "','"
					+ dcr
					+ "','"
					+ dctime
					+ "','"
					+ dcdw
					+ "','"
					+ lon
					+ "','"
					+ lat
					+ "','"
					+ hb
					+ "','"
					+ xdm
					+ "','"
					+ xbh
					+ "','"
					+ xbmj
					+ "','"
					+ jzmc
					+ "','"
					+ cbtj
					+ "','"
					+ lfzc
					+ "','"
					+ whbw
					+ "','"
					+ yhswmc
					+ "','"
					+ mcwhcd
					+ "','"
					+ ct
					+ "','"
					+ mcwhmj
					+ "','"
					+ ly
					+ "','"
					+ bz
					+ "','"
					+ city
					+ "','"
					+ county
					+ "','"
					+ town
					+ "','"
					+ village
					+ "','"
					+ 0
					+ "','"
					+ sbr
					+ "','" + sbsj + "' )";
			// String sql = "select * from station where name like '%" +
			// searchTxt + "%'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** �����к�����̤��� �������� */
	public static List<HashMap<String, String>> searchYhswTcdData(
			Context context, String dbname, String jzmc, String sbzt) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTDCDTCB where 1=1";
			if (!"".equals(jzmc)) {
				sql = sql + " and JZNAME like  '%" + jzmc + "%'";
			}
			if (!"".equals(sbzt)) {
				sql = sql + " and UPLOADSTATUS =" + sbzt;
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** �����к�����̤��·�� �������� */
	public static List<HashMap<String, String>> searchYhswTclxData(
			Context context, String dbname, String jzmc, String sbzt) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTLXTCB where 1=1";
			if (!"".equals(jzmc)) {
				sql = sql + " and JZMC like  '%" + jzmc + "%'";
			}
			if (!"".equals(sbzt)) {
				sql = sql + " and UPLOADSTATUS =" + sbzt;
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��� �к�����̤��·������ ���������ݿ⣬ֻ��һ���ֶξ���̤��·��ID */
	public static void addYhswTclxData(Context context, String dbname,
			String lxid, String lon, String lat) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTLXTCB (TCLXID,TCQDLON,TCQDLAT,TCPERSON,TCTIME,TCDEPARTMENT,TCDBMJ,TCZDLON,TCZDLAT,YHSWNAME,JZMC,LFZC,CITY,COUNTY,TOWN,VILLAGE,REMARK,UPLOADSTATUS,SBR,SBSJ) values('"
					+ lxid
					+ "','"
					+ lon
					+ "', '"
					+ lat
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ ""
					+ "', '"
					+ "" + "', '" + "0" + "', '" + "" + "', '" + "" + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� �к�����̤��·������ ���������ݿ� */
	public static void updateYhswTclxData(Context context, String dbname,
			String lxid, String tcr, String tcrq, String tcdw, String tcdbmj,
			String tczdjd, String tczdwd, String yhswmc, String jzmc,
			String lfzc, String city, String county, String town,
			String village, String bz, String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update BUSI_PESTLXTCB set TCPERSON='" + tcr
					+ "',TCPERSON='" + tcr + "',TCTIME='" + tcrq
					+ "',TCDEPARTMENT='" + tcdw + "',TCDBMJ='" + tcdbmj
					+ "',TCZDLON='" + tczdjd + "',TCZDLAT='" + tczdwd
					+ "',YHSWNAME='" + yhswmc + "',JZMC='" + jzmc + "',LFZC='"
					+ lfzc + "',CITY='" + city + "',COUNTY='" + county
					+ "',TOWN='" + town + "',VILLAGE='" + village
					+ "',REMARK='" + bz + "',UPLOADSTATUS='" + 0 + "',SBR='"
					+ sbr + "',SBSJ='" + sbsj + "' where TCLXID = '" + lxid
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� �к�����̤��·������ ���������ݿ� �յ㾭γ�Ȳ��� */
	public static void updateYhswTclxData2(Context context, String dbname,
			String lxid, String tcr, String tcrq, String tcdw, String tcdbmj,
			String yhswmc, String jzmc, String lfzc, String city,
			String county, String town, String village, String bz, String sbr,
			String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update BUSI_PESTLXTCB set TCPERSON='" + tcr
					+ "',TCPERSON='" + tcr + "',TCTIME='" + tcrq
					+ "',TCDEPARTMENT='" + tcdw + "',TCDBMJ='" + tcdbmj
					+ "',YHSWNAME='" + yhswmc + "',JZMC='" + jzmc + "',LFZC='"
					+ lfzc + "',CITY='" + city + "',COUNTY='" + county
					+ "',TOWN='" + town + "',VILLAGE='" + village
					+ "',REMARK='" + bz + "',UPLOADSTATUS='" + 0 + "',SBR='"
					+ sbr + "',SBSJ='" + sbsj + "' where TCLXID = '" + lxid
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ�к�����̤��·�� �������� */
	@SuppressLint("DefaultLocale")
	public static List<HashMap<String, String>> getYhswTclxData(
			Context context, String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTLXTCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					Log.i("9090", "arg0++" + arg0.length);
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("9090", "Exception+" + e.toString());
		}
		return list;
	}

	/** ɾ�� �к�����̤��·�����ݸ���ID */
	public static void deleteYhswTclxData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTLXTCB where ID = '" + id + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� �к�����̤��·������ ����TCLXID */
	public static void deleteYhswTclx2Data(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTLXTCB where TCLXID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ�к�����̤��� �������� */
	public static List<HashMap<String, String>> getYhswTcdData(Context context,
			String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTDCDTCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� �к�����̤������� */
	public static void deleteYhswTcdData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTDCDTCB where ID = '" + id + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� �к�����̤������� ���������ݿ� */
	public static void updateYhswTcdData(Context context, String dbname,
			String dcdbh, String dcr, String dcsj, String dcdw, String jd,
			String wd, String hb, String xdm, String xbh, String xbmj,
			String jzmc, String cbtj, String lfzc, String whbw, String yhswmc,
			String mcwhcd, String ct, String mcwhmj, String ly, String bz,
			String city, String county, String town, String village,
			String sczt, String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update BUSI_PESTDCDTCB set DCPERSON='" + dcr
					+ "',DCTIME='" + dcsj + "',DCDEPARTMENT='" + dcdw
					+ "',LON='" + jd + "',LAT='" + wd + "',ALTITUDE='" + hb
					+ "',TOPONYMY='" + xdm + "',XBH='" + xbh + "',XBAREA='"
					+ xbmj + "',JZNAME='" + jzmc + "',CBTJ='" + cbtj
					+ "',LFZC='" + lfzc + "',WHBW='" + whbw + "',YHSWNAME='"
					+ yhswmc + "',MCWHCD='" + mcwhcd + "',CT='" + ct
					+ "' ,MCWHMJ='" + mcwhmj + "',SOURCE='" + ly + "',REMARK='"
					+ bz + "',CITY='" + city + "',COUNTY='" + county
					+ "',TOWN='" + town + "',VILLAGE='" + village
					+ "',UPLOADSTATUS='" + 0 + "',SBR='" + sbr + "',SBSJ='"
					+ sbsj + "'  where DCDID = '" + dcdbh + "'";
			// String sql = "select * from station where name like '%" +
			// searchTxt + "%'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** ��� �к��������س溦�������� ���������ݿ� */
	public static void addYhswYdchdcData(Context context, String dbname,
			String ydbh, String dcr, String dcsj, String dcdw, String ydxz,
			String ydc, String ydk, String ydmj, String dclx, String yddbmj,
			String zhmj, String hb, String ycjjd, String ycjwd, String xbh,
			String xbmj, String city, String county, String town,
			String village, String xdm, String jzmc, String chmc, String ct,
			String whbw, String whcd, String ly, String cbtj, String cl,
			String czl, String ckmd, String sl, String ybd, String jkzs,
			String swzs, String ykh, String yksd, String pw, String px,
			String gslx, String mpmc, String mpzmj, String cml, String zymmpz,
			String bz, String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTYDCHDCB (YDBH,DCR,DCTIME,DCDW,YDXZ,YDC,YDK,YDMJ,DCLX,YDDBMJ,ZHMJ,HB,YCJJD,YCJWD,XBH,XBMJ,CITY,COUNTY,TOWN,VILLAGE,XDM,JZMC,HCMC,CT,WHBW,WHCD,SOURCE,CBTJ,CAGE,CZL,CKMD,SAGE,YBD,JKZS,SWZS,YKH,YKSD,PW,PX,GSLX,MPMC,MPZMJ,CML,ZYMMPZ,BZ,SCZT,SBR,SBSJ) values('"
					+ ydbh
					+ "','"
					+ dcr
					+ "', '"
					+ dcsj
					+ "', '"
					+ dcdw
					+ "', '"
					+ ydxz
					+ "', '"
					+ ydc
					+ "', '"
					+ ydk
					+ "', '"
					+ ydmj
					+ "', '"
					+ dclx
					+ "', '"
					+ yddbmj
					+ "', '"
					+ zhmj
					+ "', '"
					+ hb
					+ "', '"
					+ ycjjd
					+ "', '"
					+ ycjwd
					+ "', '"
					+ xbh
					+ "', '"
					+ xbmj
					+ "', '"
					+ city
					+ "', '"
					+ county
					+ "', '"
					+ town
					+ "', '"
					+ village
					+ "', '"
					+ xdm
					+ "', '"
					+ jzmc
					+ "', '"
					+ chmc
					+ "', '"
					+ ct
					+ "', '"
					+ whbw
					+ "', '"
					+ whcd
					+ "', '"
					+ ly
					+ "', '"
					+ cbtj
					+ "', '"
					+ cl
					+ "', '"
					+ czl
					+ "', '"
					+ ckmd
					+ "', '"
					+ sl
					+ "', '"
					+ ybd
					+ "', '"
					+ jkzs
					+ "', '"
					+ swzs
					+ "', '"
					+ ykh
					+ "', '"
					+ yksd
					+ "', '"
					+ pw
					+ "', '"
					+ px
					+ "', '"
					+ gslx
					+ "', '"
					+ mpmc
					+ "', '"
					+ mpzmj
					+ "', '"
					+ cml
					+ "', '"
					+ zymmpz
					+ "', '"
					+ bz
					+ "', '"
					+ 0
					+ "', '"
					+ sbr
					+ "', '"
					+ sbsj
					+ "')";
			// String sql = "select * from station where name like '%" +
			// searchTxt + "%'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** �����к��������س溦 �������� */
	public static List<HashMap<String, String>> searchYhswYdchData(
			Context context, String dbname, String hcmc, String sbzt) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYDCHDCB where 1=1";
			if (!"".equals(hcmc)) {
				sql = sql + " and HCMC like  '%" + hcmc + "%'";
			}
			if (!"".equals(sbzt)) {
				sql = sql + " and SCZT =" + sbzt;
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ������س溦������Ϣ
	 * 
	 * @param context
	 * @param dbname
	 *            ���ݿ���
	 * @param ydbh
	 *            ���ر��
	 * @param ysbh
	 *            �������
	 * @param luan
	 *            ��
	 * @param yc
	 *            �׳�
	 * @param yong
	 *            Ӽ
	 * @param cc
	 *            �ɳ�
	 * @param cd
	 *            ���
	 * @param sg
	 *            ����
	 * @param xj
	 *            �ؾ�
	 * @param gf
	 *            �ڷ�
	 */
	public static void addYhswYdchxcData(Context context, String dbname,
			String ydbh, String ysbh, String luan, String yc, String yong,
			String cc, String cd, String sg, String xj, String gf) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTYDCHXCB (YDBH,YSBH,LUAN,YC,YONG,CC,CD,SG,XJ,GF) values('"
					+ ydbh
					+ "','"
					+ ysbh
					+ "','"
					+ luan
					+ "','"
					+ yc
					+ "','"
					+ yong
					+ "','"
					+ cc
					+ "','"
					+ cd
					+ "','"
					+ sg
					+ "','"
					+ xj
					+ "','" + gf + "')";
			// String sql = "select * from station where name like '%" +
			// searchTxt + "%'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ�к��������س溦���� �������� */
	public static List<HashMap<String, String>> getYhswYdchdcData(
			Context context, String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYDCHDCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					Log.i("bug", "columns+" + arg0.length);
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ���� �к��������س溦������Ϣ ���������ݿ� */
	public static void updateYhswYdchdcData(Context context, String dbname,
			String ydbh, String dcr, String dcsj, String dcdw, String ydxz,
			String ydc, String ydk, String ydmj, String dclx, String yddbmj,
			String zhmj, String hb, String ycjjd, String ycjwd, String xbh,
			String xbmj, String city, String county, String town,
			String village, String xdm, String jzmc, String chmc, String ct,
			String whbw, String whcd, String ly, String cbtj, String cl,
			String czl, String ckmd, String sl, String ybd, String jkzs,
			String swzs, String ykh, String yksd, String pw, String px,
			String gslx, String mpmc, String mpzmj, String cml, String zymmpz,
			String bz, String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update BUSI_PESTYDCHDCB set DCR='" + dcr
					+ "',DCTIME='" + dcsj + "',DCDW='" + dcdw + "',YDXZ='"
					+ ydxz + "',YDC='" + ydc + "',YDK='" + ydk + "',YDMJ='"
					+ ydmj + "',DCLX='" + dclx + "',YDDBMJ='" + yddbmj
					+ "',ZHMJ='" + zhmj + "',HB='" + hb + "',YCJJD='" + ycjjd
					+ "',YCJWD='" + ycjwd + "',XBH='" + xbh + "',XBMJ='" + xbmj
					+ "',CITY='" + city + "',COUNTY='" + county + "',TOWN='"
					+ town + "',VILLAGE='" + village + "',XDM='" + xdm
					+ "',JZMC='" + jzmc + "',HCMC='" + chmc + "',CT='" + ct
					+ "',WHBW='" + whbw + "',WHCD='" + whcd + "',SOURCE='" + ly
					+ "',CBTJ='" + cbtj + "',CAGE='" + cl + "',CZL='" + czl
					+ "',CKMD='" + ckmd + "',SAGE='" + sl + "',YBD='" + ybd
					+ "',JKZS='" + jkzs + "',SWZS='" + swzs + "',YKH='" + ykh
					+ "',YKSD='" + yksd + "',PW='" + pw + "',PX='" + px
					+ "',GSLX='" + gslx + "',MPMC='" + mpmc + "',MPZMJ='"
					+ mpzmj + "',CML='" + cml + "',ZYMMPZ='" + zymmpz
					+ "',BZ='" + bz + "',SCZT='" + 0 + "',SBR='" + sbr
					+ "',SBSJ='" + sbsj + "'  where YDBH='" + ydbh + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** ɾ�� �к��������س溦�������� */
	public static void deleteYhswYdchdcData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTYDCHDCB where ID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ�к��������س溦��ϸ���� �������� */
	public static List<HashMap<String, String>> getYhswydxxdcData(
			Context context, String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYDCHXCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� �к��������س溦���������� */
	public static void deleteYhswYdchxcData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTYDCHXCB where ID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** �����к��������ز��� �������� */
	public static List<HashMap<String, String>> searchYhswYdbhData(
			Context context, String dbname, String bhmc, String sbzt) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYDBHDCB where 1=1";
			if (!"".equals(bhmc)) {
				sql = sql + " and BHMC like  '%" + bhmc + "%'";
			}
			if (!"".equals(sbzt)) {
				sql = sql + " and SCZT =" + sbzt;
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��� �к��������ز������� */
	public static void addYhswYdbhdcData(Context context, String dbname,
			String ydbh, String dcr, String dcsj, String dcdw, String ydxz,
			String ydc, String ydk, String ydmj, String dclx, String yddbmj,
			String zhmj, String hb, String ycjjd, String ycjwd, String xbh,
			String xbmj, String city, String county, String town,
			String village, String xdm, String jzmc, String bhmc, String fblx,
			String ly, String whcd, String cbtj, String gslx, String bgl,
			String bgzs, String pjxj, String sl, String ybd, String pd,
			String dx, String ykh, String yksd, String pjg, String yhzwgd,
			String mpmc, String mpzmj, String cml, String zymmpz, String bz,
			String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTYDBHDCB (YDBH,DCR,DCSJ,DCDW,YDXZ,YDC,YDK,YDMJ,DCLX,YDDBMJ,ZHMJ,HB,YCJLON,YCJLAT,XBH,XBMJ,CITY,COUNTY,TOWN,VILLAGE,XDM,JZMC,BHMC,FBLX,LY,WHCD,CBTJ,GSLX,BGL,BGZS,PJXJ,SL,YBD,PD,DX,YKH,YKSD,PJG,YHZWGD,MPMC,MPZMJ,CML,ZYMMPZ,BZ,SCZT,SBR,SBSJ) values('"
					+ ydbh
					+ "','"
					+ dcr
					+ "', '"
					+ dcsj
					+ "', '"
					+ dcdw
					+ "', '"
					+ ydxz
					+ "', '"
					+ ydc
					+ "', '"
					+ ydk
					+ "', '"
					+ ydmj
					+ "', '"
					+ dclx
					+ "', '"
					+ yddbmj
					+ "', '"
					+ zhmj
					+ "', '"
					+ hb
					+ "', '"
					+ ycjjd
					+ "', '"
					+ ycjwd
					+ "', '"
					+ xbh
					+ "', '"
					+ xbmj
					+ "', '"
					+ city
					+ "', '"
					+ county
					+ "', '"
					+ town
					+ "', '"
					+ village
					+ "', '"
					+ xdm
					+ "', '"
					+ jzmc
					+ "', '"
					+ bhmc
					+ "', '"
					+ fblx
					+ "', '"
					+ ly
					+ "', '"
					+ whcd
					+ "', '"
					+ cbtj
					+ "', '"
					+ gslx
					+ "', '"
					+ bgl
					+ "', '"
					+ bgzs
					+ "', '"
					+ pjxj
					+ "', '"
					+ sl
					+ "', '"
					+ ybd
					+ "', '"
					+ pd
					+ "', '"
					+ dx
					+ "', '"
					+ ykh
					+ "', '"
					+ yksd
					+ "', '"
					+ pjg
					+ "', '"
					+ yhzwgd
					+ "', '"
					+ mpmc
					+ "', '"
					+ mpzmj
					+ "', '"
					+ cml
					+ "', '"
					+ zymmpz
					+ "', '"
					+ bz
					+ "', '"
					+ 0
					+ "', '"
					+ sbr
					+ "', '"
					+ sbsj
					+ "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� �к��������ز������ */
	public static void addYhswYdbhxcData(Context context, String dbname,
			String ydbh, String ysbh, String bhfj, String sg, String xj,
			String gf) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTYDBHXCB (YDBH,YSBH,BHFJ,SG,XJ,GF) values('"
					+ ydbh
					+ "','"
					+ ysbh
					+ "', '"
					+ bhfj
					+ "', '"
					+ sg
					+ "', '" + xj + "', '" + gf + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ�к��������ز������� �������� */
	public static List<HashMap<String, String>> getYhswYdbhdcData(
			Context context, String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYDBHDCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��ȡ�к��������ز�����ϸ���� �������� */
	public static List<HashMap<String, String>> getYhswydbhxxdcData(
			Context context, String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYDBHXCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ���� �к��������ز���������Ϣ ���������ݿ� */
	public static void updateYhswYdbhdcData(Context context, String dbname,
			String ydbh, String dcr, String dcsj, String dcdw, String ydxz,
			String ydc, String ydk, String ydmj, String dclx, String yddbmj,
			String zhmj, String hb, String ycjjd, String ycjwd, String xbh,
			String xbmj, String city, String county, String town,
			String village, String xdm, String jzmc, String bhmc, String fblx,
			String ly, String whcd, String cbtj, String gslx, String bgl,
			String bgzs, String pjxj, String sl, String ybd, String pd,
			String dx, String ykh, String yksd, String pjg, String yhzwgd,
			String mpmc, String mpzmj, String cml, String zymmpz, String bz,
			String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update BUSI_PESTYDBHDCB set DCR='" + dcr + "',DCSJ='"
					+ dcsj + "',DCDW='" + dcdw + "',YDXZ='" + ydxz + "',YDC='"
					+ ydc + "',YDK='" + ydk + "',YDMJ='" + ydmj + "',DCLX='"
					+ dclx + "',YDDBMJ='" + yddbmj + "',ZHMJ='" + zhmj
					+ "',HB='" + hb + "',YCJLON='" + ycjjd + "',YCJLAT='"
					+ ycjwd + "',XBH='" + xbh + "',XBMJ='" + xbmj + "',CITY='"
					+ city + "',COUNTY='" + county + "',TOWN='" + town
					+ "',VILLAGE='" + village + "',XDM='" + xdm + "',JZMC='"
					+ jzmc + "',BHMC='" + bhmc + "',FBLX='" + fblx + "',LY='"
					+ ly + "',WHCD='" + whcd + "',CBTJ='" + cbtj + "',GSLX='"
					+ gslx + "',BGL='" + bgl + "',BGZS='" + bgzs + "',PJXJ='"
					+ pjxj + "',SL='" + sl + "',YBD='" + ybd + "',PD='" + pd
					+ "',DX='" + dx + "',YKH='" + ykh + "',YKSD='" + yksd
					+ "',PJG='" + pjg + "',YHZWGD='" + yhzwgd + "',MPMC='"
					+ mpmc + "',MPZMJ='" + mpzmj + "',CML='" + cml
					+ "',ZYMMPZ='" + zymmpz + "',BZ='" + bz + "',SCZT='" + 0
					+ "',SBR='" + sbr + "',SBSJ='" + sbsj + "'  where YDBH='"
					+ ydbh + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** ɾ�� �к��������ز����������� */
	public static void deleteYhswYdbhdcData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTYDBHDCB where ID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� �к��������ز������������� */
	public static void deleteYhswYdbhxcData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTYDBHXCB where ID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� �к������к�ֲ����� */
	public static void addYhswYhzwdcData(Context context, String dbname,
			String ydbh, String dcr, String dcsj, String dcdw, String ydxz,
			String ydc, String ydk, String ydmj, String yddbmj, String zhmj,
			String hb, String ycjjd, String ycjwd, String xbh, String xbmj,
			String city, String county, String town, String village,
			String jzmc, String yhzwmc, String yhzwgd, String yhzwdj,
			String whzwmc, String whbw, String whcd, String ly, String cbtj,
			String bhzgs, String zgzs, String whl, String whzwszzk,
			String pjxj, String pjg, String sl, String ybd, String dx,
			String dj, String fblx, String pd, String yksd, String bz,
			String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTYHZWDCB (YDBH,DCR,DCSJ,DCDW,YDXZ,YDC,YDK,YDMJ,YDDBMJ,ZHMJ,HB,YCJLON,YCJLAT,XBH,XBMJ,CITY,COUNTY,TOWN,VILLAGE,JZMC,YHZWMC,YHZWGD,YHZWDJ,WHZWMC,WHBW,WHCD,LY,CBTJ,BHZGS,ZGZS,WHL,WHZWSZZK,PJXJ,PJG,SL,YBD,DX,DJ,FBLX,PD,YKSD,BZ,SCZT,SBR,SBSJ) values('"
					+ ydbh
					+ "','"
					+ dcr
					+ "','"
					+ dcsj
					+ "','"
					+ dcdw
					+ "','"
					+ ydxz
					+ "','"
					+ ydc
					+ "','"
					+ ydk
					+ "','"
					+ ydmj
					+ "','"
					+ yddbmj
					+ "','"
					+ zhmj
					+ "','"
					+ hb
					+ "','"
					+ ycjjd
					+ "','"
					+ ycjwd
					+ "','"
					+ xbh
					+ "','"
					+ xbmj
					+ "','"
					+ city
					+ "','"
					+ county
					+ "','"
					+ town
					+ "','"
					+ village
					+ "','"
					+ jzmc
					+ "','"
					+ yhzwmc
					+ "','"
					+ yhzwgd
					+ "','"
					+ yhzwdj
					+ "','"
					+ whzwmc
					+ "','"
					+ whbw
					+ "','"
					+ whcd
					+ "','"
					+ ly
					+ "','"
					+ cbtj
					+ "','"
					+ bhzgs
					+ "','"
					+ zgzs
					+ "','"
					+ whl
					+ "','"
					+ whzwszzk
					+ "','"
					+ pjxj
					+ "','"
					+ pjg
					+ "','"
					+ sl
					+ "','"
					+ ybd
					+ "','"
					+ dx
					+ "','"
					+ dj
					+ "','"
					+ fblx
					+ "','"
					+ pd
					+ "','"
					+ yksd
					+ "','"
					+ bz
					+ "','"
					+ 0
					+ "','" + sbr + "','" + sbsj + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ�к������к�ֲ����� �������� */
	public static List<HashMap<String, String>> getYhswYhzwData(
			Context context, String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYHZWDCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** �����к������к�ֲ�� �������� */
	public static List<HashMap<String, String>> searchYhswYhzwData(
			Context context, String dbname, String yhzwmc, String sbzt) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYHZWDCB where 1=1";
			if (!"".equals(yhzwmc)) {
				sql = sql + " and YHZWMC like  '%" + yhzwmc + "%'";
			}
			if (!"".equals(sbzt)) {
				sql = sql + " and SCZT =" + sbzt;
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ���� �к������к�ֲ�������Ϣ ���������ݿ� */
	public static void updateYhswYhzwdcData(Context context, String dbname,
			String ydbh, String dcr, String dcsj, String dcdw, String ydxz,
			String ydc, String ydk, String ydmj, String yddbmj, String zhmj,
			String hb, String ycjjd, String ycjwd, String xbh, String xbmj,
			String city, String county, String town, String village,
			String jzmc, String yhzwmc, String yhzwgd, String yhzwdj,
			String whzwmc, String whbw, String whcd, String ly, String cbtj,
			String bhzgs, String zgzs, String whl, String whzwszzk,
			String pjxj, String pjg, String sl, String ybd, String dx,
			String dj, String fblx, String pd, String yksd, String bz,
			String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update BUSI_PESTYHZWDCB set DCR='" + dcr + "',DCSJ='"
					+ dcsj + "',DCDW='" + dcdw + "',YDXZ='" + ydxz + "',YDC='"
					+ ydc + "',YDK='" + ydk + "',YDMJ='" + ydmj + "',YDDBMJ='"
					+ yddbmj + "',ZHMJ='" + zhmj + "',HB='" + hb + "',YCJLON='"
					+ ycjjd + "',YCJLAT='" + ycjwd + "',XBH='" + xbh
					+ "',XBMJ='" + xbmj + "',CITY='" + city + "',COUNTY='"
					+ county + "',TOWN='" + town + "',VILLAGE='" + village
					+ "',JZMC='" + jzmc + "',YHZWMC='" + yhzwmc + "',YHZWGD='"
					+ yhzwgd + "',YHZWDJ='" + yhzwdj + "',WHZWMC='" + whzwmc
					+ "',WHBW='" + whbw + "',WHCD='" + whcd + "',LY='" + ly
					+ "',CBTJ='" + cbtj + "',BHZGS='" + bhzgs + "',ZGZS='"
					+ zgzs + "',WHL='" + whl + "',WHZWSZZK='" + whzwszzk
					+ "',PJXJ='" + pjxj + "',PJG='" + pjg + "',SL='" + sl
					+ "',YBD='" + ybd + "',DX='" + dx + "',DJ='" + dj
					+ "',FBLX='" + fblx + "',PD='" + pd + "',YKSD='" + yksd
					+ "',BZ='" + bz + "',SCZT='" + 0 + "',SBR='" + sbr
					+ "',SBSJ='" + sbsj + "'  where YDBH='" + ydbh + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** ɾ�� �к��������ز����������� */
	public static void deleteYhswYhzwdcData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTYHZWDCB where ID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� �к�����ľ�Ĳ��溦���� */
	public static void addYhswMcbchdcData(Context context, String dbname,
			String mcbchbh, String dcr, String dcsj, String dcdw, String bdcdw,
			String dcdjd, String dcdwd, String zyjgcp, String xdm,
			String xykcmcpz, String zyjgjysz, String ccmcsl, String kcl,
			String ccmcpz, String bcmc, String whcd, String whbw, String ct,
			String city, String county, String town, String village, String bz,
			String sczt, String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTMCBCHDCB (MCBCHBH,DCR,DCSJ,DCDW,BDCDW,DCDJD,DCDWD,ZYJGCP,XDM,XYKCMCPZ,ZYJGJYSZ,CCMCSL,CCL,CCMCPZ,BCMC,WHCD,WHBW,CT,CITY,COUNTY,TOWN,VILLAGE,BZ,SCZT,SBR,SBSJ) values('"
					+ mcbchbh
					+ "','"
					+ dcr
					+ "','"
					+ dcsj
					+ "','"
					+ dcdw
					+ "','"
					+ bdcdw
					+ "','"
					+ dcdjd
					+ "','"
					+ dcdwd
					+ "','"
					+ zyjgcp
					+ "','"
					+ xdm
					+ "','"
					+ xykcmcpz
					+ "','"
					+ zyjgjysz
					+ "','"
					+ ccmcsl
					+ "','"
					+ kcl
					+ "','"
					+ ccmcpz
					+ "','"
					+ bcmc
					+ "','"
					+ whcd
					+ "','"
					+ whbw
					+ "','"
					+ ct
					+ "','"
					+ city
					+ "','"
					+ county
					+ "','"
					+ town
					+ "','"
					+ village
					+ "','"
					+ bz
					+ "','"
					+ 0
					+ "','"
					+ sbr
					+ "','"
					+ sbsj + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** �����к�����ľ�Ĳ��溦 �������� */
	public static List<HashMap<String, String>> searchYhswMcbchData(
			Context context, String dbname, String whcd, String sbzt) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTMCBCHDCB where 1=1";
			if (!"".equals(whcd)) {
				sql = sql + " and WHCD =  " + whcd;
			}
			if (!"".equals(sbzt)) {
				sql = sql + " and SCZT =" + sbzt;
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��ȡ�к�����Ŀ�ⲡ�溦���� �������� */
	public static List<HashMap<String, String>> getYhswMcbchData(
			Context context, String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTMCBCHDCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ���� �к�����ľ�Ĳ��溦������Ϣ ���������ݿ� */
	public static void updateYhswMcbchdcData(Context context, String dbname,
			String mcbchbh, String dcr, String dcsj, String dcdw, String bdcdw,
			String dcdjd, String dcdwd, String zyjgcp, String xdm,
			String xykcmcpz, String zyjgjysz, String ccmcsl, String kcl,
			String ccmcpz, String bcmc, String whcd, String whbw, String ct,
			String city, String county, String town, String village, String bz,
			String sczt, String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update BUSI_PESTMCBCHDCB set DCR='" + dcr
					+ "',DCSJ='" + dcsj + "',DCDW='" + dcdw + "',BDCDW='"
					+ bdcdw + "',DCDJD='" + dcdjd + "',DCDWD='" + dcdwd
					+ "',ZYJGCP='" + zyjgcp + "',XDM='" + xdm + "',XYKCMCPZ='"
					+ xykcmcpz + "',ZYJGJYSZ='" + zyjgjysz + "',CCMCSL='"
					+ ccmcsl + "',CCL='" + kcl + "',CCMCPZ='" + ccmcpz
					+ "',BCMC='" + bcmc + "',WHCD='" + whcd + "',WHBW='" + whbw
					+ "',CT='" + ct + "',CITY='" + city + "',COUNTY='" + county
					+ "',TOWN='" + town + "',VILLAGE='" + village + "',BZ='"
					+ bz + "',SCZT='" + 0 + "',SBR='" + sbr + "',SBSJ='" + sbsj
					+ "'  where MCBCHBH='" + mcbchbh + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** ɾ�� �к�����ľ�Ĳ��溦�������� */
	public static void deleteYhswMcbchdcData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTMCBCHDCB where ID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� �к������ճ�Ƶ��� */
	public static void addYhswYcddcData(Context context, String dbname,
			String ycdbh, String ycdmc, String dcr, String dcdw, String dcsj,
			String dcdjd, String dcdwd, String hb, String xdm, String xbh,
			String xbmj, String zyhcmc, String lfzc, String hcsl, String ybd,
			String pjg, String pjxj, String city, String county, String town,
			String village, String bz, String sczt, String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTYCDDCB (YCDBH,YCDMC,DCR,DCDW,DCSJ,DCDJD,DCDWD,HB,XDM,XBH,XBMJ,ZYHCMC,LFZC,HCSL,YBD,PJG,PJXJ,CITY,COUNTY,TOWN,VILLAGE,BZ,SCZT,SBR,SBSJ) values('"
					+ ycdbh
					+ "','"
					+ ycdmc
					+ "','"
					+ dcr
					+ "','"
					+ dcdw
					+ "','"
					+ dcsj
					+ "','"
					+ dcdjd
					+ "','"
					+ dcdwd
					+ "','"
					+ hb
					+ "','"
					+ xdm
					+ "','"
					+ xbh
					+ "','"
					+ xbmj
					+ "','"
					+ zyhcmc
					+ "','"
					+ lfzc
					+ "','"
					+ hcsl
					+ "','"
					+ ybd
					+ "','"
					+ pjg
					+ "','"
					+ pjxj
					+ "','"
					+ city
					+ "','"
					+ county
					+ "','"
					+ town
					+ "','"
					+ village
					+ "','"
					+ bz
					+ "','"
					+ 0
					+ "','"
					+ sbr + "','" + sbsj + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** �����к������ճ�� �������� */
	public static List<HashMap<String, String>> searchYhswYcdData(
			Context context, String dbname, String ycdmc, String sbzt) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYCDDCB where 1=1";
			if (!"".equals(ycdmc)) {
				sql = sql + " and YCDMC like  '%" + ycdmc + "%'";
			}
			if (!"".equals(sbzt)) {
				sql = sql + " and SCZT =" + sbzt;
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��ȡ�к������ճ�Ƶ��� �������� */
	public static List<HashMap<String, String>> getYhswYcddcData(
			Context context, String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTYCDDCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� �к������ճ�Ƶ������� */
	public static void deleteYhswYcddcData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTYCDDCB where ID = '" + id + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� �к������ճ�Ƶ�����Ϣ ���������ݿ� */
	public static void updateYhswYcddcData(Context context, String dbname,
			String ycdbh, String ycdmc, String dcr, String dcdw, String dcsj,
			String dcdjd, String dcdwd, String hb, String xdm, String xbh,
			String xbmj, String zyhcmc, String lfzc, String hcsl, String ybd,
			String pjg, String pjxj, String city, String county, String town,
			String village, String bz, String sczt, String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update BUSI_PESTYCDDCB set YCDMC='" + ycdmc
					+ "',DCR='" + dcr + "',DCSJ='" + dcsj + "',DCDW='" + dcdw
					+ "',DCDJD='" + dcdjd + "',DCDWD='" + dcdwd + "',HB='" + hb
					+ "'," + "XDM='" + xdm + "',XBH='" + xbh + "',XBMJ='"
					+ xbmj + "',ZYHCMC='" + zyhcmc + "',LFZC='" + lfzc
					+ "',HCSL='" + hcsl + "',YBD='" + ybd + "',PJG='" + pjg
					+ "',PJXJ='" + pjxj + "',CITY='" + city + "',COUNTY='"
					+ county + "',TOWN='" + town + "',VILLAGE='" + village
					+ "'," + "BZ='" + bz + "',SCZT='" + 0 + "',SBR='" + sbr
					+ "',SBSJ='" + sbsj + "'  where YCDBH='" + ycdbh + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** ��� �к������ɲ��߳没�ղ� */
	public static void addYhswSxcbpcData(Context context, String dbname,
			String ydbh, String dcr, String dcsj, String dcdw, String ksjd,
			String kswd, String xbh, String xbmj, String whcd, String ksmj,
			String sz, String kssl, String jzmc, String pjg, String pjxj,
			String qyr, String qybw, String qysl, String jjsl, String xdm,
			String jdr, String jdrq, String jdjg, String mclbf, String city,
			String county, String town, String village, String bz, String sczt,
			String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_PESTSCXCBPCB (YDBH,DCR,DCSJ,DCDW,KSJD,KSWD,XBH,XBMJ,WHCD,KSMJ,SZ,KSSL,JZMC,PJG,PJXJ,QYR,QYBW,QYSL,JJSL,XDM,JDR,JDRQ,JDJG,MCLBF,CITY,COUNTY,TOWN,VILLAGE,BZ,SCZT,SBR,SBSJ) values('"
					+ ydbh
					+ "','"
					+ dcr
					+ "','"
					+ dcsj
					+ "','"
					+ dcdw
					+ "','"
					+ ksjd
					+ "','"
					+ kswd
					+ "','"
					+ xbh
					+ "','"
					+ xbmj
					+ "','"
					+ whcd
					+ "','"
					+ ksmj
					+ "','"
					+ sz
					+ "','"
					+ kssl
					+ "','"
					+ jzmc
					+ "','"
					+ pjg
					+ "','"
					+ pjxj
					+ "','"
					+ qyr
					+ "','"
					+ qybw
					+ "','"
					+ qysl
					+ "','"
					+ jjsl
					+ "','"
					+ xdm
					+ "','"
					+ jdr
					+ "','"
					+ jdrq
					+ "','"
					+ jdjg
					+ "','"
					+ mclbf
					+ "','"
					+ city
					+ "','"
					+ county
					+ "','"
					+ town
					+ "','"
					+ village
					+ "','"
					+ bz
					+ "','"
					+ 0
					+ "','"
					+ sbr
					+ "','"
					+ sbsj
					+ "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ�к������ɲ��߳没�ղ� �������� */
	public static List<HashMap<String, String>> getYhswScxcbpcData(
			Context context, String dbname) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTSCXCBPCB";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** �����к������ɲ��߳没 �������� */
	public static List<HashMap<String, String>> searchYhswScxcbData(
			Context context, String dbname, String jzmc, String sbzt) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_PESTSCXCBPCB where 1=1";
			if (!"".equals(jzmc)) {
				sql = sql + " and JZMC like  '%" + jzmc + "%'";
			}
			if (!"".equals(sbzt)) {
				sql = sql + " and SCZT =" + sbzt;
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� �к������ɲ��߳没�ղ����� */
	public static void deleteYhswScxcbpcData(Context context, String dbname,
			String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_PESTSCXCBPCB where ID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� �к������ɲ��߳没�ղ���Ϣ ���������ݿ� */
	public static void updateYhswScxcbpcData(Context context, String dbname,
			String ydbh, String dcr, String dcsj, String dcdw, String ksjd,
			String kswd, String xbh, String xbmj, String whcd, String ksmj,
			String sz, String kssl, String jzmc, String pjg, String pjxj,
			String qyr, String qybw, String qysl, String jjsl, String xdm,
			String jdr, String jdrq, String jdjg, String mclbf, String city,
			String county, String town, String village, String bz, String sczt,
			String sbr, String sbsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update BUSI_PESTSCXCBPCB set DCR='" + dcr
					+ "',DCSJ='" + dcsj + "',DCDW='" + dcdw + "',KSJD='" + ksjd
					+ "',KSWD='" + kswd + "',XBH='" + xbh + "'," + "XBMJ='"
					+ xbmj + "',WHCD='" + whcd + "',KSMJ='" + ksmj + "',SZ='"
					+ sz + "',KSSL='" + kssl + "',JZMC='" + jzmc + "',PJG='"
					+ pjg + "',PJXJ='" + pjxj + "',QYR='" + qyr + "',QYBW='"
					+ qybw + "',QYSL='" + qysl + "',JJSL='" + jjsl + "',XDM='"
					+ xdm + "'," + "JDR='" + jdr + "',JDRQ='" + jdrq
					+ "',JDJG='" + jdjg + "',MCLBF='" + mclbf + "',CITY='"
					+ city + "',COUNTY='" + county + "',TOWN='" + town
					+ "',VILLAGE='" + village + "',BZ='" + bz + "',SCZT='"
					+ sczt + "',SBR='" + sbr + "',SBSJ='" + sbsj
					+ "'  where YDBH='" + ydbh + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** ɾ�� ɭ��������� ���ص������Ϣ */
	public static void deleteLxqcYddcbData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCYDDCJLB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� ���ص������Ϣ */
	public static void addLxqcYddcbData(Context context, String[] zd,
			List<HashMap<String, String>> list) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			// String sql1 = "insert into BUSI_PESTSCXCBPCB () values('" + sbsj
			// + "')";
			String sql = "insert into BUSI_LXQCYDDCJLB(";
			int size = zd.length;
			for (int i = 0; i < size - 1; i++) {
				sql = sql + zd[i] + ",";
			}
			sql = sql + zd[size - 1];

			sql = sql + ") values ( ";

			for (int i = 0; i < size - 1; i++) {
				sql = sql + "'" + list.get(0).get(zd[i]) + "',";
			}
			sql = sql + "'" + list.get(0).get(zd[size - 1]) + "'";

			sql = sql + " )";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ ɭ����Դ������� ���ص���� �������� */
	public static List<HashMap<String, String>> getLxqcYddcData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCYDDCJLB where YDH = '" + ydh
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��ȡ ɭ����Դ������� ��ľ���� �������� */
	public static List<HashMap<String, String>> getlxqcxmdcData(Context context) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCXMDCB where YDH = '" + 1234
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��� ɭ�����������ľ�������Ϣ */
	public static void addLxqcXmdcbData(Context context, String[] zd,
			Map<String, String> map) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			// String sql1 = "insert into BUSI_PESTSCXCBPCB () values('" + sbsj
			// + "')";
			String sql = "insert into BUSI_LXQCXMDCB(";
			int size = zd.length;
			for (int i = 0; i < size - 1; i++) {
				sql = sql + zd[i] + ",";
			}
			sql = sql + zd[size - 1];

			sql = sql + ") values ( ";

			for (int i = 0; i < size - 1; i++) {
				sql = sql + "'" + map.get(zd[i]) + "',";
			}
			sql = sql + "'" + map.get(zd[size - 1]) + "'";

			sql = sql + " )";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� ɭ�����������ľ�������Ϣ */
	public static void deleteLxqcXmdcbData(Context context, String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCXMDCB where ID = '" + id + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� ɭ�����������ľ�����ȫ����Ϣ */
	public static void deleteLxqcXmdcbAllData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCXMDCB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ ɭ����Դ������� ɭ���ֺ���� �������� */
	public static List<HashMap<String, String>> getslzhqkdcData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCSLZHQKB where YDH = '" + ydh
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��� ɭ���������ɭ���ֺ����������Ϣ */
	public static void addLxqcSlzhqkData(Context context, String[] zd,
			Map<String, String> map) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			// String sql1 = "insert into BUSI_PESTSCXCBPCB () values('" + sbsj
			// + "')";
			String sql = "insert into BUSI_LXQCSLZHQKB(";
			int size = zd.length;
			for (int i = 0; i < size - 1; i++) {
				sql = sql + zd[i] + ",";
			}
			sql = sql + zd[size - 1];

			sql = sql + ") values ( ";

			for (int i = 0; i < size - 1; i++) {
				sql = sql + "'" + map.get(zd[i]) + "',";
			}
			sql = sql + "'" + map.get(zd[size - 1]) + "'";

			sql = sql + " )";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� ɭ���������ɭ���ֺ����������Ϣ */
	public static void deleteLxqcSlzhData(Context context, String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCSLZHQKB where ID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� ɭ���������ɭ���ֺ����ȫ��������Ϣ */
	public static void deleteLxqcSlzhAllData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCSLZHQKB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ ɭ����Դ������� �����������ر仯��� �������� */
	public static List<HashMap<String, String>> getFcqlydbhqkData(
			Context context) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCFCQYDBH where YDH = '" + 1234
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� ɭ��������� �����������ر仯��� �������� */
	public static void deleteFcqnydbhqkAllData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCFCQYDBH where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� �����������ر仯��� �������� */
	public static void addFcqnydbhqkData(Context context, String[] zd,
			HashMap<String, String> map) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			// String sql1 = "insert into BUSI_PESTSCXCBPCB () values('" + sbsj
			// + "')";
			String sql = "insert into BUSI_LXQCFCQYDBH(";
			int size = zd.length;
			for (int i = 0; i < size - 1; i++) {
				sql = sql + zd[i] + ",";
			}
			sql = sql + zd[size - 1];

			sql = sql + ") values ( ";

			for (int i = 0; i < size - 1; i++) {
				sql = sql + "'" + map.get(zd[i]) + "',";
			}
			sql = sql + "'" + map.get(zd[size - 1]) + "'";

			sql = sql + " )";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ ɭ����Դ������� ��Ȼ����������� �������� */
	public static List<HashMap<String, String>> getTrgxqkdcData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCTRGXQKB where YDH = '" + ydh
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��� ɭ��������� ��Ȼ����������� ��Ϣ */
	public static void addLxqcTrgxdcbData(Context context, String[] zd,
			Map<String, String> map) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			// String sql1 = "insert into BUSI_PESTSCXCBPCB () values('" + sbsj
			// + "')";
			String sql = "insert into BUSI_LXQCTRGXQKB(";
			int size = zd.length;
			for (int i = 0; i < size - 1; i++) {
				sql = sql + zd[i] + ",";
			}
			sql = sql + zd[size - 1];

			sql = sql + ") values ( ";

			for (int i = 0; i < size - 1; i++) {
				sql = sql + "'" + map.get(zd[i]) + "',";
			}
			sql = sql + "'" + map.get(zd[size - 1]) + "'";

			sql = sql + " )";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� ɭ��������� ��Ȼ����������� ��Ϣ */
	public static void deleteLxqcTrgxqkdcData(Context context, String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCTRGXQKB where ID = '" + id
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� ɭ��������� ��Ȼ��������������� ��Ϣ */
	public static void deleteLxqcTrgxqkdcAllData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCTRGXQKB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� ɭ��������� δ�������ֵص��� ��Ϣ */
	public static List<HashMap<String, String>> searchLxqcWclzlddcData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCWCLZLDDCB where YDH = '" + ydh
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� ɭ��������� δ�������ֵص��� ��Ϣ */
	public static void deleteLxqcWclzlddcAllData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCWCLZLDDCB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɭ��������� δ�������ֵص��� ��� */
	public static void addLxqcWclzlddcData(Context context, String ydh,
			String zldqk, String zlnd, String ml, String czmd, String mmch,
			String fyghcs, String szzc) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCWCLZLDDCB (YDH,ZLDQK,ZLND,Ml,CZMD,MMCH,FYGHCS,SZZC) values('"
					+ ydh
					+ "','"
					+ zldqk
					+ "','"
					+ zlnd
					+ "','"
					+ ml
					+ "','"
					+ czmd
					+ "','"
					+ mmch
					+ "','"
					+ fyghcs
					+ "','"
					+ szzc
					+ "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���� ɭ��������� ֲ������ ��Ϣ
	 * 
	 * @param context
	 * @param ydh
	 *            ���غ�
	 * @param zbtype
	 *            ֲ������ 0 Ϊ������Ϣ���� �������˵�� ��������λ�� �����ܸǶ����� ɭ��������� ֲ������ ��Ϣ 1Ϊ��ľ��Ϣ
	 *            2Ϊ�ݱ���Ϣ 3Ϊ�ر�����Ϣ
	 * @return
	 */
	public static List<HashMap<String, String>> searchLxqcZbdcData(
			Context context, String ydh, String zbtype) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "";
			if ("0".equals(zbtype)) {
				sql = "select YFQKSM,YFSZWZ,YFZGD from BUSI_LXQCZBDCB where YDH = '"
						+ ydh + "'  and ZBTYPE = '" + zbtype + "'";
			} else if ("1".equals(zbtype)) {
				sql = "select GMMC,GMZS,GMPJG,GMPJDJ,GMGD from BUSI_LXQCZBDCB where YDH = '"
						+ ydh + "'  and ZBTYPE = '" + zbtype + "'";
			} else if ("2".equals(zbtype)) {
				sql = "select CBMC,CBPJG,CBGD from BUSI_LXQCZBDCB where YDH = '"
						+ ydh + "'  and ZBTYPE = '" + zbtype + "'";
			} else if ("3".equals(zbtype)) {
				sql = "select DBWMC,DBWPJG,DBWGD from BUSI_LXQCZBDCB where YDH = '"
						+ ydh + "'  and ZBTYPE = '" + zbtype + "'";
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @param context
	 * @param ydh
	 * @param zbtype
	 *            ֲ������ 0 Ϊ������Ϣ���� �������˵�� ��������λ�� �����ܸǶ����� ɭ��������� ֲ������ ��Ϣ 1Ϊ��ľ��Ϣ
	 *            2Ϊ�ݱ���Ϣ 3Ϊ�ر�����Ϣ
	 */
	public static void deleteLxqcZbdcData(Context context, String ydh,
			String zbtype) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCZBDCB where YDH = '" + ydh
					+ "' and ZBTYPE = '" + zbtype + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɭ��������� ֲ��������Ϣ ���
	 * 
	 * @param context
	 * @param zbtypeֲ������
	 *            0 Ϊ������Ϣ���� �������˵�� ��������λ�� �����ܸǶ����� ɭ��������� ֲ������ ��Ϣ 1Ϊ��ľ��Ϣ 2Ϊ�ݱ���Ϣ
	 *            3Ϊ�ر�����Ϣ
	 */
	public static void addLxqcZbdcData(Context context, String ydh,
			String yfqksm, String yfszwz, String yfzgd, String zbtype,
			String gmmc, String gmzs, String gmpjg, String gmpjdj, String gmgd,
			String cbmc, String cbpjg, String cbgd, String dbwmc,
			String dbwpjg, String dbwgd) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "";
			if ("0".equals(zbtype)) {
				sql = "insert into BUSI_LXQCZBDCB (YDH,YFQKSM,YFSZWZ,YFZGD,ZBTYPE) values('"
						+ ydh
						+ "','"
						+ yfqksm
						+ "','"
						+ yfszwz
						+ "','"
						+ yfzgd
						+ "','" + zbtype + "')";
			} else if ("1".equals(zbtype)) {
				sql = "insert into BUSI_LXQCZBDCB (YDH,ZBTYPE,GMMC,GMZS,GMPJG,GMPJDJ,GMGD) values('"
						+ ydh
						+ "','"
						+ zbtype
						+ "','"
						+ gmmc
						+ "','"
						+ gmzs
						+ "','" + gmpjg + "','" + gmpjdj + "','" + gmgd + "')";
			} else if ("2".equals(zbtype)) {
				sql = "insert into BUSI_LXQCZBDCB (YDH,ZBTYPE,CBMC,CBPJG,CBGD) values('"
						+ ydh
						+ "','"
						+ zbtype
						+ "','"
						+ cbmc
						+ "','"
						+ cbpjg
						+ "','" + cbgd + "')";
			} else if ("3".equals(zbtype)) {
				sql = "insert into BUSI_LXQCZBDCB (YDH,ZBTYPE,DBWMC,DBWPJG,DBWGD) values('"
						+ ydh
						+ "','"
						+ zbtype
						+ "','"
						+ dbwmc
						+ "','"
						+ dbwpjg
						+ "','" + dbwgd + "')";
			}
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� ɭ��������� ���߲�����¼ ��Ϣ */
	public static List<HashMap<String, String>> searchLxqcSgcljlData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCSGCLJLB where YDH = '" + ydh
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� ɭ��������� ���߲�����¼��Ϣ */
	public static void deleteLxqcSgcljlData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCSGCLJLB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� ���߲�����¼��Ϣ */
	public static void addLxqcSgcljlData(Context context, String ydh,
			String ymh, String sz, String xj, String sg, String zzxg) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCSGCLJLB (YDH,YMH,SZ,XJ,SG,ZZXG) values('"
					+ ydh
					+ "','"
					+ ymh
					+ "','"
					+ sz
					+ "','"
					+ xj
					+ "','"
					+ sg
					+ "','" + zzxg + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� ɭ��������� ���߲�����¼ ��Ϣ */
	public static List<HashMap<String, String>> searchLxqcYxcljlData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCYXCLB where YDH = '" + ydh
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� ɭ��������� ���߲�����¼ ��Ϣ */
	public static void deleteLxqcYxcljlData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCYXCLB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� ���߲�����¼ ��Ϣ */
	public static void addLxqcYxcljlData(Context context, String ydh,
			String cz, String fwj, String qxj, String xj, String spj) {
		try {
			ResourcesManager.getInstance(context);
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCYXCLB (YDH,CZ,FWJ,QXJ,XJ,SPJ) values('"
					+ ydh
					+ "','"
					+ cz
					+ "','"
					+ fwj
					+ "','"
					+ qxj
					+ "','"
					+ xj
					+ "','" + spj + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���� ɭ��������� �ܽ������¼ ��ϢtypeΪ1Ϊ������Ϣ2Ϊlistview��Ϣ
	 * inputtypeΪ��1��Ϊ����������ϢΪ��2��Ϊ����listview��Ϣ
	 * */
	public static List<HashMap<String, String>> searchLxqcZjcljlData(
			Context context, String ydh, String inputtype) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "";
			if ("1".equals(inputtype)) {
				sql = "select YDH,ZJJDBHC,ZJXDBHC,ZJZCWC from BUSI_LXQCZJCLB where YDH = '"
						+ ydh + "' and TYPE= '" + 1 + "'";
			} else if ("2".equals(inputtype)) {
				sql = "select YDH,CZ,FWJ,QXJ,XJ,SPJ from BUSI_LXQCZJCLB where YDH = '"
						+ ydh + "' and TYPE= '" + 2 + "'";
			}
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��� ɭ��������� �ܽ������¼ ��Ϣ */
	public static void addLxqcZjcljlData(Context context, String ydh,
			String cz, String fwj, String qxj, String xj, String spj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCZJCLB (YDH,CZ,FWJ,QXJ,XJ,SPJ,TYPE) values('"
					+ ydh
					+ "','"
					+ cz
					+ "','"
					+ fwj
					+ "','"
					+ qxj
					+ "','"
					+ xj
					+ "','" + spj + "','" + 2 + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� �ܽ������¼���� ��Ϣ */
	public static void addLxqcZjcljlGyData(Context context, String ydh,
			String jdbhc, String xdbhc, String zcwc) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCZJCLB (YDH,ZJJDBHC,ZJXDBHC,ZJZCWC,TYPE) values('"
					+ ydh
					+ "','"
					+ jdbhc
					+ "','"
					+ xdbhc
					+ "','"
					+ zcwc
					+ "','" + 1 + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� ɭ��������� �ܽ������¼ ��Ϣ */
	public static void deleteLxqcZjcljlData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCZJCLB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� ɭ��������� ����λ��ͼ ��Ϣ */
	public static List<HashMap<String, String>> searchLxqcYdwztData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select MC,BH,FWJ,SPJ from BUSI_LXQCYDWZTB where YDH = '"
					+ ydh + "'  and  TYPE = '" + 1 + "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ���� ɭ��������� ����λ��ͼ ��������˵�� ��Ϣ */
	public static HashMap<String, String> searchLxqcYdwztYdtzData(
			Context context, String ydh) {
		final HashMap<String, String> map = new HashMap<String, String>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select YDTZSM from BUSI_LXQCYDWZTB where YDH = '"
					+ ydh + "' and TYPE = '" + 0 + "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						if (BussUtil.isEmperty(arg0[0])) {
							map.put(columns[0].toUpperCase(), arg0[0]);
						} else {
							map.put(columns[0].toUpperCase(), "");
						}
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/** ɾ�� ɭ��������� ����λ��ͼ ��Ϣ */
	public static void deleteLxqcYdwztData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCYDWZTB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� ����λ��ͼ ��Ϣ type=1 */
	public static void addLxqcYdwztData(Context context, String ydh, String mc,
			String bh, String fwj, String spj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCYDWZTB (YDH,MC,BH,FWJ,SPJ,TYPE) values('"
					+ ydh
					+ "','"
					+ mc
					+ "','"
					+ bh
					+ "','"
					+ fwj
					+ "','"
					+ spj
					+ "','" + 1 + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� ����λ��ͼ ��������˵�� ��Ϣ type=0 */
	public static void addLxqcYdwztYdtzData(Context context, String ydh,
			String ydtzsm) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCYDWZTB (YDH,YDTZSM,TYPE) values('"
					+ ydh + "','" + ydtzsm + "','" + 0 + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� ɭ��������� ����λ��ͼ ��Ϣ */
	public static List<HashMap<String, String>> searchLxqcYindwztData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select MC,BH,FWJ,SPJ from BUSI_LXQCYINDWZTB where YDH = '"
					+ ydh + "'  and  TYPE = '" + 1 + "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ���� ɭ��������� ����λ��ͼ ���� ��Ϣ */
	public static HashMap<String, String> searchLxqcYindwzgyData(
			Context context, String ydh) {
		final HashMap<String, String> map = new HashMap<String, String>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select ZBFWJ,CFWJ,YXJL,LC,GPSX,GPSY,YDTZSM from BUSI_LXQCYINDWZTB where YDH = '"
					+ ydh + "' and TYPE = '" + 0 + "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/** ɾ�� ɭ��������� ����λ��ͼ ���� ��Ϣ */
	public static void deleteLxqcYindwzgyData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCYINDWZTB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� ����λ��ͼ ���� ��Ϣtype=0 */
	public static void addLxqcYindwzgyData(Context context, String ydh,
			String ydtzsm, String zbfwj, String cfwj, String yxjl, String lc,
			String hzb, String zzb) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCYINDWZTB (YDH,ZBFWJ,CFWJ,YXJL,LC,GPSX,GPSY,YDTZSM,TYPE) values('"
					+ ydh
					+ "','"
					+ zbfwj
					+ "','"
					+ cfwj
					+ "','"
					+ yxjl
					+ "','"
					+ lc
					+ "','"
					+ hzb
					+ "','"
					+ zzb
					+ "','"
					+ ydtzsm
					+ "','"
					+ 0 + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� ����λ��ͼ ��Ϣtype=1 */
	public static void addLxqcYindwzgyData(Context context, String ydh,
			String mc, String bh, String fwj, String spj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCYINDWZTB (YDH,MC,BH,FWJ,SPJ,TYPE) values('"
					+ ydh
					+ "','"
					+ mc
					+ "','"
					+ bh
					+ "','"
					+ fwj
					+ "','"
					+ spj
					+ "','" + 1 + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� ɭ��������� ����ֵ����¼ ��Ϣ */
	public static List<HashMap<String, String>> searchLxqcKjldcjlData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCKJLDCB where YDH = '" + ydh
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� ɭ��������� ����ֵ����¼ ��Ϣ */
	public static void deleteLxqcKjldcjlData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCKJLDCB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� ����ֵ����¼ */
	public static void addLxqcKjldcjlData(Context context, String[] zd,
			Map<String, String> map) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCKJLDCB(";
			int size = zd.length;
			for (int i = 0; i < size - 1; i++) {
				sql = sql + zd[i] + ",";
			}
			sql = sql + zd[size - 1];

			sql = sql + ") values ( ";

			for (int i = 0; i < size - 1; i++) {
				sql = sql + "'" + map.get(zd[i]) + "',";
			}
			sql = sql + "'" + map.get(zd[size - 1]) + "'";

			sql = sql + " )";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� ɭ��������� GPS���������¼��Ϣ */
	public static List<HashMap<String, String>> searchLxqcGpshjdcbData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCGPSHJDCB where YDH = '" + ydh
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� ɭ��������� GPS���������¼��Ϣ */
	public static void deleteLxqcGpshjdcbData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCGPSHJDCB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� GPS���������¼��Ϣ */
	public static void addLxqcGpshjdcbData(Context context, String ydh,
			String kssj, String jssj, String cjjl, String xzydtj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCGPSHJDCB (YDH,CJKSSJ,CJJSSJ,CJJL,XZYDTJ) values('"
					+ ydh
					+ "','"
					+ kssj
					+ "','"
					+ jssj
					+ "','"
					+ cjjl
					+ "','"
					+ xzydtj + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ ɭ����Դ������� ÿľ��� �������� */
	public static List<HashMap<String, String>> getLxqcMmjcData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCMMJCJLB where YDH = '" + ydh
					+ "' order by ID";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						map.put(arg0[0], "false");
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ɾ�� ɭ����Դ������� ÿľ��� �������� */
	public static void deleteLxqcMmjcData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCMMJCJLB where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ����Դ������� ÿľ��� �������� */
	public static void addLxqcMmjcData(Context context, String[] zd,
			Map<String, String> map) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCMMJCJLB(";
			int size = zd.length;
			for (int i = 0; i < size - 1; i++) {
				sql = sql + zd[i] + ",";
			}
			sql = sql + zd[size - 1];

			sql = sql + ") values ( ";

			for (int i = 0; i < size - 1; i++) {
				sql = sql + "'" + map.get(zd[i]) + "',";
			}
			sql = sql + "'" + map.get(zd[size - 1]) + "'";

			sql = sql + " )";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ɾ�� ɭ��������� ���ر�Ŀ¼��Ϣ */
	public static void deleteLxqcYdbmmData(Context context, String ydh) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from BUSI_LXQCYDBMM where YDH = '" + ydh
					+ "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��� ɭ��������� ���ر�Ŀ¼��Ϣ */
	public static void addLxqcLxqcYdbmmData(Context context, String ydh,
			String zdcfsj, String zdyzsj, String dcjssj, String fhzdsj) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into BUSI_LXQCYDBMM (YDH,ZDCFSJ,ZDYZSJ,DCJSSJ,FHZDSJ) values('"
					+ ydh
					+ "','"
					+ zdcfsj
					+ "','"
					+ zdyzsj
					+ "','"
					+ dcjssj
					+ "','" + fhzdsj + "')";
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� ɭ��������� ���ر�Ŀ¼��Ϣ */
	public static List<HashMap<String, String>> searchLxqcYdbmmData(
			Context context, String ydh) {
		final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from BUSI_LXQCYDBMM where YDH = '" + ydh
					+ "'";
			db.exec(sql, new Callback() {
				String[] columns = null;

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < arg0.length; i++) {
							if (BussUtil.isEmperty(arg0[i])) {
								map.put(columns[i].toUpperCase(), arg0[i]);
							} else {
								map.put(columns[i].toUpperCase(), "");
							}
						}
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��ȡ������Ӧ�Ĵ����� */
	static String cun_name = null;

	public static String getCUNDMName(Context ctx, String xdm, String xidm,
			String cundm) {
		cun_name = null;
		try {
			ResourcesManager.getInstance(ctx);
			String databaseName = ResourcesManager.getInstance(ctx).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from CUN where xian ='" + xdm
					+ "' and xiang = '" + xidm + "' and cun = '" + cundm + "'";
			db.exec(sql, new Callback() {
				// String[] arrays = null;
				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {
					cun_name = data[6];
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					// arrays = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cun_name;
	}

	/** ��ȡ������Ӧ�Ĵ���ֵ */
	static String xiang_name = null;
	public static String getXIDMName(Context ctx, String xdm, String xidm) {
		try {
			ResourcesManager.getInstance(ctx);
			String databaseName = ResourcesManager.getInstance(ctx).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from XIANG where xian ='" + xdm
					+ "' and xiang = '" + xidm + "'";
			db.exec(sql, new Callback() {
				// String[] arrays = null;
				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {
					xiang_name = data[10];
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					// arrays = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xiang_name;
	}

	/** ����������ȡ����Ϣ */

	public static List<Row> getXiangList(Context ctx, String xian) {
		final List<Row> list = new ArrayList<Row>();
		try {
			String databaseName = ResourcesManager.getInstance(ctx).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select xian,xiang,xiang_name from XIANG where xian ='"
					+ xian + "'";
			db.exec(sql, new Callback() {
				// String[] arrays = null;
				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {
					Row row = new Row();
					row.setId(data[0] + data[1]);
					row.setName(data[2]);
					list.add(row);
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					// arrays = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ����������ȡ����Ϣ */

	public static List<Row> getCunList(Context ctx, String xiang) {
		final List<Row> list = new ArrayList<Row>();
		try {
			ResourcesManager.getInstance(ctx);
			String databaseName = ResourcesManager.getInstance(ctx).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String xian = xiang.substring(0, 6);
			xiang = xiang.substring(6);
			String sql = "select xian,xiang,cun,cun_name from cun where xian ='"
					+ xian + "' and xiang ='" + xiang + "'";
			db.exec(sql, new Callback() {
				// String[] arrays = null;
				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {
					Row row = new Row();
					row.setId(data[0] + data[1] + data[2]);
					row.setName(data[3]);
					list.add(row);
					/*
					 * String item = data[0]+data[1]+"-"+data[2];
					 * list.add(item);
					 */
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					// arrays = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��ȡ���ݿ��ش����Ӧ�Ĵ���ֵ */
	static String xian_name = null;

	public static String getXDMName(Context ctx, String dm) {
		xian_name = null;
		try {
			ResourcesManager.getInstance(ctx);
			String databaseName = ResourcesManager.getInstance(ctx).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from XIAN where xian ='" + dm + "'";
			db.exec(sql, new Callback() {
				// String[] arrays = null;
				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {
					xian_name = data[10];
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					// arrays = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xian_name;
	}

	/** ���ݴ����ȡ���� */
	static String xiangname = null;

	public static String getXiangName(Context ctx, String xdm, String xiangdm) {

		try {
			ResourcesManager.getInstance(ctx);
			String databaseName = ResourcesManager.getInstance(ctx).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select xiang_name from xiang where xian ='" + xdm
					+ "' and xiang = '" + xiangdm + "'";
			db.exec(sql, new Callback() {
				// String[] arrays = null;
				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {
					xiangname = data[0];
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					// arrays = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xiangname;
	}

	/** ���ݴ����ȡ���� */
	static String cunname = null;

	public static String getCunName(Context context, String xdm, String xiangdm,
			String cundm) {
		try {
			ResourcesManager.getInstance(context);
			String databaseName = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select cun_name from cun where xian ='" + xdm
					+ "' and xiang = '" + xiangdm + "' and cun = '" + cundm
					+ "'";
			db.exec(sql, new Callback() {
				// String[] arrays = null;
				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {
					cunname = data[0];
					return false;
				}

				@Override
				public void columns(String[] arg0) {
					// arrays = arg0;
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cunname;
	}

	/** ���� ���ִ������Ϣ */
	public static List<Row> searchShuZhongData(Context context) {
		final List<Row> list = new ArrayList<Row>();
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from SHUZHONG ";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] arg0) {
					if (arg0.length > 0) {
						Row row = new Row();
						for (int i = 0; i < arg0.length; i++) {
							if (i == 1) {
								row.setId(arg0[i]);
							} else if (i == 2) {
								row.setName(arg0[i]);
							}
						}
						list.add(row);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0) {
				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/** ��ȡ��ǰ������ID */
	static int maxID = 1;

	public static int getTabaleMaxID(Context context, String tableName,
			String path) {
		try {
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(path, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select max(id) from " + tableName;
			
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {
				}

				@Override
				public boolean newrow(String[] data) {
					maxID = Integer.parseInt(data[0]) + 1;
					return false;
				}

				@Override
				public void columns(String[] arg0) {
				}
			});
			
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return maxID;
	}
	
	/** ���� 6.8 ���ܶ�1.0�����(G)�������(M)��׼�� ��Ϣ*/
	public static HashMap<String, String> searchXuJiLiangData(Context context,String shuzhong,double shugao)
	{
		final HashMap<String, String> map = new HashMap<String, String>();
		try
		{
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select DMJ,XJL from XUJIBZB where SHUZHONG= '" + shuzhong + "' and PJG ='" + shugao + "'";
			db.exec(sql, new Callback()
			{
				String[] columns = null;

				@Override
				public void types(String[] arg0)
				{
				}

				@Override
				public boolean newrow(String[] arg0)
				{
					if (arg0.length > 0)
					{
						for (int i = 0; i < arg0.length; i++)
						{
							if (BussUtil.isEmperty(arg0[i]))
							{
								map.put(columns[i], arg0[i]);
							} else
							{
								map.put(columns[i], "");
							}
						}
					}
					return false;
				}

				@Override
				public void columns(String[] arg0)
				{
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}
	/**ɾ���ҵ��ղ�����*/
	public static boolean deleteScdData(Context context,String id) {
		try {
			String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = " delete from SHOUCANG where ID = '" + id + "'";
			db.exec(sql, null);
			db.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}

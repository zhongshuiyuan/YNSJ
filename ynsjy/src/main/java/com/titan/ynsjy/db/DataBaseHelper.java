package com.titan.ynsjy.db;

import android.content.Context;

import com.titan.model.EditInfo;
import com.titan.model.Photo;
import com.titan.ynsjy.dao.DaoMaster;
import com.titan.ynsjy.dao.DaoSession;
import com.titan.ynsjy.entity.GjPoint;
import com.titan.ynsjy.util.BussUtil;
import com.titan.ynsjy.util.ResourcesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsqlite.Callback;
import jsqlite.Database;

public class DataBaseHelper {
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
     *
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
                    + " values(" + photo.getFk_Fxh_Uid() + ","
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

    /**
     * 保存编辑
     *
     * @param context
     * @return
     */
    public static boolean saveEdit(Context context, EditInfo editInfo) {
        boolean flag = false;
        try {
            String databaseName = ResourcesManager.getInstance(context).getDataBase("db_sjy.sqlite");
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new jsqlite.Database();
            db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "insert into edit (FK_EDIT_UID,MODIFYINFO,MODIFYTIME,BEFOREINFO,AFTERINFO,INFO,REMARK,Geometry) "
                    + " values("
                    + editInfo.getFk_Edit_Uid() + ","
                    + editInfo.getModifyinfo() + ","
                    + editInfo.getModifytime() + ","
                    + editInfo.getBeforeinfo() + ","
                    + editInfo.getAfterinfo() + ","
                    + editInfo.getInfo() + ","
                    + editInfo.getRemark() + ","
                    + "GeomFromGeoJSON('" + editInfo.getGeometry() + "')";
            db.exec(sql, null);
            db.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            return flag;
        }
        return flag;
    }

    /**
     * ��ȡС��������
     */
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

    /**
     * 本地保存轨迹数据集
     */
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

    /**
     * 本地轨迹数据点添加
     */
    public boolean addGuijiData(Context context, String sbh, double lon,
                                double lat, String time, String state, String dbpath) {
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

    /**
     * 估计数据查询
     */
    public static List<GjPoint> selectPointGuiji(Context context, String sbh, String startTime, String endTime) {
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
                    GjPoint point = new GjPoint(id, data[1], data[2], data[3], data[4], data[5]);
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

    static boolean sel_result = false;

    /**
     * ��ѯ����������ʷ��
     */
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

    /**
     * ��ȡ�û���ʷ����
     */
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

    /**
     * �û���¼��֤
     */
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

    /**
     * ��ȡ�û���ʷ����
     */
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

    /**
     * ��ӵ�¼�û��������û���
     */
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


    /**
     * ���� 6.8 ���ܶ�1.0�����(G)�������(M)��׼�� ��Ϣ
     */
    public static HashMap<String, String> searchXuJiLiangData(Context context, String shuzhong, double shugao) {
        final HashMap<String, String> map = new HashMap<String, String>();
        try {
            String filename = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new jsqlite.Database();
            db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "select DMJ,XJL from XUJIBZB where SHUZHONG= '" + shuzhong + "' and PJG ='" + shugao + "'";
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
                                map.put(columns[i], arg0[i]);
                            } else {
                                map.put(columns[i], "");
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

}

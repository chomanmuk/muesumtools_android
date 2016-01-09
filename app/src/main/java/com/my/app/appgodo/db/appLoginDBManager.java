package com.my.app.appgodo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.my.app.appgodo.Dto.Logininfo;
/**
 * Created by Mark on 2015-12-21.
 */
public class appLoginDBManager {
    static final String dbName = "godoDB.db";
    static final String Table_APPIDS = "LoginTable";
    static final int dbVersion = 1;
    // DB관련 객체 선언
    private OpenHelper opener; // DB opener
    private SQLiteDatabase db; // DB controller

    // 부가적인 객체들
    private Context context;
    // 생성자
    public appLoginDBManager(Context context) {
        this.context = context;
        this.opener = new OpenHelper(context, dbName, null, dbVersion);
        db = opener.getWritableDatabase();
    }
    // Opener of DB and Table
    private class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
            super(context, name, null, version);
            // TODO Auto-generated constructor stub
        }

        // 생성된 DB가 없을 경우에 한번만 호출됨
        @Override
        public void onCreate(SQLiteDatabase arg0) {
            // String dropSql = "drop table if exists " + tableName;
            // db.execSQL(dropSql);

            String createSql = "CREATE TABLE IF NOT EXISTS " + Table_APPIDS + " ( sno INTEGER PRIMARY KEY AUTOINCREMENT, m_no INTEGER (10), m_id varchar(30), m_name varchar(255))";
            arg0.execSQL(createSql);
            // Toast.makeText(context, "DB is opened", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
            if(arg1 < arg2){
                db.execSQL("DROP TABLE IF EXISTS " + Table_APPIDS);
            }
        }
    }
    // 데이터 추가
    public void insertData(Logininfo info) {
        String sql = "insert into " + Table_APPIDS + " ( m_no, m_id, m_name ) values('"  + info.getM_no() + "', '"  + info.getM_id() + "', '" + info.getM_name() + "');";
        db.execSQL(sql);
    }

    // 데이터 갱신
    public void updateData(Logininfo info, int index) {
        String sql = "update " + Table_APPIDS + " set m_no = '" + info.getM_no()  + "'" +
                ", m_id='" + info.getM_id() + "'" +
                ", m_name='" + info.getM_name() + "'" +
                " where sno = " + index
                + ";";
        db.execSQL(sql);
    }

    // 데이터 삭제
    public void removeData(int index) {
        String sql = "delete from " + Table_APPIDS + " where sno = " + index + ";";
        db.execSQL(sql);
    }

    // 데이터 검색
    public Logininfo selectData(int index) {
        String sql = "select m_no, m_id, m_name from " + Table_APPIDS + " where sno = " + index
                + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            Logininfo info = new Logininfo();
            info.setM_no(result.getLong(0));
            info.setM_id(result.getString(1));
            info.setM_name(result.getString(2));
            return info;
        }
        result.close();
        return null;
    }
}

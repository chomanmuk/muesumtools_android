package com.my.app.appgodo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.my.app.appgodo.Dto.Logininfo;
import com.my.app.appgodo.Dto.PushDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 2015-12-23.
 */
public class PushDBManager {
    static final String dbName = "godoPushDB.db";
    static final String Table_APPIDS = "pushTable";
    static final int dbVersion = 1;
    // DB관련 객체 선언
    private OpenHelper opener; // DB opener
    private SQLiteDatabase db; // DB controller

    // 부가적인 객체들
    private Context context;
    // 생성자
    public PushDBManager(Context context) {
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

            String createSql = "CREATE TABLE IF NOT EXISTS " + Table_APPIDS + " ( sno INTEGER PRIMARY KEY AUTOINCREMENT, ordno VARCHAR (20), ptype INTEGER (10), message TEXT, regdt VARCHAR (20) )";
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
    public void insertData(PushDto info) {
        String sql = "insert into " + Table_APPIDS + " (ordno, ptype, message, regdt ) values('" + info.getOrdno() + "','"  + info.getPtype() + "', '"  + info.getMessage() + "', '" + info.getRegdt() + "');";
        db.execSQL(sql);
    }

    // 데이터 갱신
    public void updateData(PushDto info, int index) {
        String sql = "update " + Table_APPIDS + " set ordno='" + info.getOrdno() + "'" +
                ", ptype = '" + info.getPtype()  + "'" +
                ", message='" + info.getMessage() + "'" +
                ", regdt='" + info.getRegdt() + "'" +
                " where sno = " + index
                + ";";
        db.execSQL(sql);
    }

    // 데이터 삭제
    public void removeData(int index) {
        String sql = "delete from " + Table_APPIDS + " where sno = " + index + ";";
        db.execSQL(sql);
    }
    //검색
    public ArrayList<PushDto> search(int index){
        String sql = "select ordno, ptype, message, regdt from " + Table_APPIDS + " where ptype = " + index + " order by sno desc "
                + ";";
        Cursor result = db.rawQuery(sql, null);
        ArrayList<PushDto> pushDtoList = new ArrayList<PushDto>();
        // result(Cursor 객체)가 비어 있으면 false 리턴

        while (result.moveToNext()) {
            PushDto info = new PushDto();
            info.setOrdno(result.getString(0));
            info.setPtype(result.getLong(1));
            info.setMessage(result.getString(2));
            info.setRegdt(result.getString(3));
            pushDtoList.add(info);
        }
        result.close();
        return pushDtoList;
    }
    // 데이터 검색
    public PushDto selectData(int index) {
        String sql = "select ordno, ptype, message, regdt from " + Table_APPIDS + " where sno = " + index
                + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            PushDto info = new PushDto();
            info.setOrdno(result.getString(0));
            info.setPtype(result.getLong(1));
            info.setMessage(result.getString(2));
            info.setRegdt(result.getString(3));
            return info;
        }
        result.close();
        return null;
    }
}

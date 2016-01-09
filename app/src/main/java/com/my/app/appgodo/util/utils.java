package com.my.app.appgodo.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.my.app.appgodo.Adapter.SpinnerAdapter;
import com.my.app.appgodo.Dto.HttpPostDto;
import com.my.app.appgodo.MainActivity;
import com.my.app.appgodo.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Mark on 2015-12-21.
 */
public class utils{

    public static ActionBarDrawerToggle drawerLayOut(final AppCompatActivity act, DrawerLayout mDrawerLayout, ExpandableListView mDrawerList){
        ActionBarDrawerToggle mDrawerToggle;
        mDrawerToggle = new ActionBarDrawerToggle(
                act,
                mDrawerLayout,
                R.drawable.btn_xml_menu,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                act.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                act.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        return mDrawerToggle;
    }
    public static String getMonth(int i){
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, i); // 1년을 더한다.

        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        Log.d("getDateKey", format.format(cal.getTime()));
        return format.format(cal.getTime());

    }
    public static String getDate(int i){
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, i); // 1년을 더한다.

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Log.d("getDateKey", format.format(cal.getTime()));
        return format.format(cal.getTime());

    }
    public static String getDateKey(String key){
        TimeZone seoul = TimeZone.getTimeZone("Asia/Seoul");

        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmm");
        Log.d("getDateKey", format.format(date));
        return format.format(date);

    }
    public static void setSkeyspanner(final AppCompatActivity act){
        String[] item = new String[]{"= 통합검색 =","주문번호","주문자명","수령자명","아이디", "주문자 핸드폰번호"};
        Spinner skey = (Spinner) act.findViewById(R.id.skey);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(act, item);
        skey.setAdapter(spinnerAdapter);
    }
    public static void setSkeyspanner2(final AppCompatActivity act){
        String[] item = new String[]{"상품명","고유번호","상품코드"};
        Spinner skey = (Spinner) act.findViewById(R.id.skey2);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(act, item);
        skey.setAdapter(spinnerAdapter);
    }
    public static void setSgkeyspanner(final AppCompatActivity act){
        String[] item = new String[]{"상품명","브랜드","제조사"};
        Spinner skey = (Spinner) act.findViewById(R.id.sgkey);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(act, item);
        skey.setAdapter(spinnerAdapter);
    }
    public static void setDtkindspanner(final AppCompatActivity act){
        String[] item = new String[]{"주문일","결제확인일","배송일"};
        Spinner skey = (Spinner) act.findViewById(R.id.dtkind);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(act, item);
        skey.setAdapter(spinnerAdapter);
    }
    public static void setDtkind2spanner(final AppCompatActivity act){
        String[] item = new String[]{"주문일","입금일","배송일","배송완료일"};
        Spinner skey = (Spinner) act.findViewById(R.id.dtkind2);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(act, item);
        skey.setAdapter(spinnerAdapter);
    }
    public static void setLogList(final AppCompatActivity act, TableLayout tblayout, String result){
        setSkeyspanner2(act);
        setDtkind2spanner(act);
        tblayout.removeAllViews();
        int price1 = 0, price2 = 0, price3 = 0, pcnt = 0;
        String[] titleitem = new String[]{"날짜","건수","주문금액","결제금액","판매이익"};
        NumberFormat nf = NumberFormat.getInstance();
        tblayout.setStretchAllColumns(true);
        tblayout.setShrinkAllColumns(true);
        TableRow rowTitle = new TableRow(act);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        for(int i=0; i< titleitem.length;i++) {
            TextView title = new TextView(act);
            title.setText(titleitem[i]);
            title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setBackgroundResource(R.color.myactionOverColor);
            title.setTypeface(Typeface.SERIF, Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, act.getResources().getDisplayMetrics()));
            rowTitle.addView(title, params);
        }
        tblayout.addView(rowTitle);

        try {
            JSONObject json = new JSONObject(result);
            JSONArray order = json.getJSONArray("data");
            Log.d("table", "" + order.length());
            TextView totalcnt = (TextView)act.findViewById(R.id.totalcnt2);
            totalcnt.setText("총 " + json.getString("cnt") + "건");
            for(int i=0;i<order.length();i++){
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, act.getResources().getDisplayMetrics()));
                JSONObject item = order.getJSONObject(i);
                TableRow tr = new TableRow(act);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                if(i%2 == 0) {
                    tr.setBackgroundResource(R.color.tr1);
                }else{
                    tr.setBackgroundResource(R.color.tr2);
                }
                //날짜
                TextView date = new TextView(act);
                date.setText(item.getString("date"));
                date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                date.setGravity(Gravity.CENTER);
                tr.addView(date, params);
                //총수량
                TextView cnt = new TextView(act);
                cnt.setText(nf.format(item.getInt("payment_cnt")));
                pcnt = pcnt + item.getInt("payment_cnt");
                cnt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                cnt.setGravity(Gravity.RIGHT);
                tr.addView(cnt, params);
                //주문금액
                TextView tdp = new TextView(act);
                tdp.setText(nf.format(item.getInt("tot_price")));
                price1 = price1 + item.getInt("tot_price");
                tdp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                tdp.setGravity(Gravity.RIGHT);
                tr.addView(tdp, params);
                //결제금액
                TextView odp = new TextView(act);
                odp.setText(nf.format(item.getInt("tot_settle")));
                price2 = price2 + item.getInt("tot_settle");
                odp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                odp.setGravity(Gravity.RIGHT);
                tr.addView(odp, params);
                //판매이익
                TextView sdp = new TextView(act);
                sdp.setText(nf.format(item.getInt("tot_earn")));
                price3 = price3 + item.getInt("tot_earn");
                sdp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                sdp.setGravity(Gravity.RIGHT);
                tr.addView(sdp, params);
                tblayout.addView(tr);
            }
            // 합계 출력
            TableRow footerTitle = new TableRow(act);
            footerTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, act.getResources().getDisplayMetrics()));
            TextView title = new TextView(act);
            title.setText("합계");
            title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setBackgroundResource(R.color.myactionOverColor);
            title.setTypeface(Typeface.SERIF, Typeface.BOLD);
            footerTitle.addView(title, params);
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView title2 = new TextView(act);
            title2.setText(nf.format(pcnt));
            title2.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, act.getResources().getDisplayMetrics()));
            title2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            title2.setTextColor(Color.WHITE);
            title2.setGravity(Gravity.RIGHT | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            title2.setBackgroundResource(R.color.pagebg);
            title2.setTypeface(Typeface.SERIF, Typeface.BOLD);
            footerTitle.addView(title2, params2);
            TextView tot1 = new TextView(act);
            tot1.setText(nf.format(price1));
            tot1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            tot1.setGravity(Gravity.RIGHT| Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            tot1.setTextColor(Color.WHITE);
            tot1.setBackgroundResource(R.color.pagebg);
            tot1.setTypeface(Typeface.SERIF, Typeface.BOLD);
            footerTitle.addView(tot1, params2);

            TextView tot2 = new TextView(act);
            tot2.setText(nf.format(price2));
            tot2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            tot2.setGravity(Gravity.RIGHT| Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            tot2.setTextColor(Color.WHITE);
            tot2.setBackgroundResource(R.color.pagebg);
            tot2.setTypeface(Typeface.SERIF, Typeface.BOLD);
            footerTitle.addView(tot2, params2);

            TextView tot3 = new TextView(act);
            tot3.setText(nf.format(price3));
            tot3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            tot3.setGravity(Gravity.RIGHT| Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            tot3.setTextColor(Color.WHITE);
            tot3.setBackgroundResource(R.color.pagebg);
            tot3.setTypeface(Typeface.SERIF, Typeface.BOLD);
            footerTitle.addView(tot3, params2);

            tblayout.addView(footerTitle, params);

        } catch (JSONException e) {
        e.printStackTrace();
    }
    }
    public static void setOrderList(final AppCompatActivity act, TableLayout tblayout, String result){
        setSkeyspanner(act);
        setSgkeyspanner(act);
        setDtkindspanner(act);
        tblayout.removeAllViews();
        int price = 0;
        String[] titleitem = new String[]{"주문번호","이름","금액","결제","상태"};
        NumberFormat nf = NumberFormat.getInstance();
        //nf.setMaximumIntegerDigits(5);
        tblayout.setStretchAllColumns(true);
        tblayout.setShrinkAllColumns(true);
        TableRow rowTitle = new TableRow(act);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        for(int i=0; i< titleitem.length;i++) {
            TextView title = new TextView(act);
            title.setText(titleitem[i]);
            title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setBackgroundResource(R.color.myactionOverColor);
            title.setTypeface(Typeface.SERIF, Typeface.BOLD);
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, act.getResources().getDisplayMetrics()));
            rowTitle.addView(title, params);
        }
        tblayout.addView(rowTitle);
        try {

            JSONObject json = new JSONObject(result);
            JSONArray order = json.getJSONArray("order");
            Log.d("table", "" + order.length());
            TextView totalcnt = (TextView)act.findViewById(R.id.totalcnt);
            totalcnt.setText("총 " + json.getString("cnt") + "건");
            for(int i=0;i<order.length();i++){
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, act.getResources().getDisplayMetrics()));
                JSONObject item = order.getJSONObject(i);
                TableRow tr = new TableRow(act);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                if(i%2 == 0) {
                    tr.setBackgroundResource(R.color.tr1);
                }else{
                    tr.setBackgroundResource(R.color.tr2);
                }
                TextView od = new TextView(act);
                od.setText(item.getString("ordno"));
                od.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                od.setGravity(Gravity.CENTER);
                tr.addView(od, params);

                TextView nm = new TextView(act);
                nm.setText(item.getString("nameOrder"));
                nm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                nm.setGravity(Gravity.CENTER);
                tr.addView(nm, params);
                TextView pc = new TextView(act);
                pc.setText(nf.format(item.getInt("prn_settleprice"))+"원");
                price += item.getInt("prn_settleprice");
                pc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                pc.setGravity(Gravity.CENTER);
                tr.addView(pc, params);
                TextView sk = new TextView(act);
                sk.setText(item.getString("settlekind"));
                sk.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                sk.setGravity(Gravity.CENTER);
                tr.addView(sk, params);
                TextView st = new TextView(act);
                st.setText(item.getString("GroupNameMap"));
                st.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                st.setGravity(Gravity.CENTER);
                tr.addView(st, params);
                tblayout.addView(tr);
            }

            // 합계 출력
            TableRow footerTitle = new TableRow(act);
            footerTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, act.getResources().getDisplayMetrics()));
            TextView title = new TextView(act);
            title.setText("합계");
            title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setBackgroundResource(R.color.myactionOverColor);
            title.setTypeface(Typeface.SERIF, Typeface.BOLD);
            footerTitle.addView(title, params);
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView title2 = new TextView(act);
            title2.setText(nf.format(price) + " 원");
            title2.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, act.getResources().getDisplayMetrics()));
            title2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);

            title2.setTextColor(Color.WHITE);
            title2.setGravity(Gravity.RIGHT|Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            title2.setBackgroundResource(R.color.pagebg);
            title2.setTypeface(Typeface.SERIF, Typeface.BOLD);
            params2.span =4;
            footerTitle.addView(title2, params2);
            tblayout.addView(footerTitle, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public static String getDataJson(final AppCompatActivity act, String myUrl, ArrayList<HttpPostDto> info){

        Log.d("httppost", myUrl);

        DefaultHttpClient client= new DefaultHttpClient();
        try{
            HttpPost httpPost = new HttpPost(myUrl);
            MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
            multiPartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multiPartEntityBuilder.setBoundary("1234567");
            multiPartEntityBuilder.addPart("appkey", new StringBody(encode(act.getString(R.string.shopEng) + getDateKey("key")), Charset.forName(act.getString(R.string.charset))));
            for(int i=0;i<info.size();i++) {
                HttpPostDto postDto = info.get(i);
                Log.d("httppost", String.valueOf(i) + " - " + info.get(i).getHkey() + " : " + info.get(i).getHvalue());
                multiPartEntityBuilder.addPart(postDto.getHkey(), new StringBody(postDto.getHvalue(), Charset.forName(act.getString(R.string.charset))));
            }

            httpPost.setEntity(multiPartEntityBuilder.build());
            /* 지연시간3초*/
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 3000);
            HttpConnectionParams.setSoTimeout(params, 3000);

            /*데이터 보내고 받는 과정*/

            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                return EntityUtils.toString(entity);
            }
            else{
                return "No string.";
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d("httppost", e.getMessage());
            client.getConnectionManager().shutdown();
            return  "";
        }
    }
    public static String encode(String str){
        String MD5 = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }
    public static void setDiglogEndingMsg(final AppCompatActivity act, String msg){
        new AlertDialog
                .Builder(act)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which){
                        act.moveTaskToBack(true);
                        act.finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        })
                .setCancelable(true)
                .create()
                .show();
    }
}
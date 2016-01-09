package com.my.app.appgodo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.my.app.appgodo.Dto.HttpPostDto;
import com.my.app.appgodo.Dto.Logininfo;
import com.my.app.appgodo.db.appLoginDBManager;
import com.my.app.appgodo.util.utils;

import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {
    public static Context mcontext;
    private Handler mHandler;
    private static final int LOGIN_CHECK = 1;
    private ProgressBar progressBar;
    private TextView loadText;
    private UserOrderDataTask mOrderTask = null;
    private UserLogDataTask mLogTask = null;
    private Runnable mRunnable;
    private AppCompatActivity mActivity;
    private int step = 0;
    private int reset = 0;
    private appLoginDBManager loginDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskWrites()
                .detectDiskReads()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork()
                .detectCustomSlowCalls()
                .penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadText    = (TextView) findViewById(R.id.loadtext);
        mActivity = this;
        mcontext = this;
        step = 0;
        /**
         * GCMID설정
         * */
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, "481426464088");
        } else {
            Log.e("id", regId);
        }
        if(((myApplication) this.getApplication()).getRegGCMId() == null) {
            ((myApplication) this.getApplication()).setRegGCMId(regId);
            System.out.println("들어간값2 :" + ((myApplication) this.getApplication()).getRegGCMId());
        }
        mRunnable = new Runnable(){
            public void run() {
                if(((myApplication) getApplication()).getRegGCMId().isEmpty()) {
                    Log.e("GCM체크 ", ((myApplication) getApplication()).getRegGCMId());
                    progressBar.setProgress(25);
                    loadText.setText("GCM 코드 확인... (1/4)");
                    mHandler.postDelayed(mRunnable, 1000);

                }
                    progressBar.setProgress(25);
                    loadText.setText("로그인 정보 확인... (2/4)");
                    loginDBManager = new appLoginDBManager(mcontext);
                    Logininfo info = (Logininfo) loginDBManager.selectData(1);
                    if(info == null){
                        Log.e("로그인 체크 ", "로그인정보 없음");
                        Toast.makeText(mcontext, "로그인 정보가 없습니다. 로그인 해주세요", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mcontext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        return;

                    }


                    if(((myApplication)mActivity.getApplication()).getOrderData().equals("")) {
                            progressBar.setProgress(50);
                            loadText.setText("주문정보를 가저옵니다... (3/4)");

                            if (mOrderTask != null) {
                                return;
                            }
                            mOrderTask = new UserOrderDataTask();
                            mOrderTask.execute((Void) null);
                        return;
                    }
                    if(((myApplication)mActivity.getApplication()).getLogData().equals("")) {
                            progressBar.setProgress(75);
                            loadText.setText("통계정보를 가저옵니다... (4/4)");
                            if (mLogTask != null) {
                                return;
                            }
                        mLogTask = new UserLogDataTask();
                        mLogTask.execute((Void) null);
                        return;
                    }
                    if(!((myApplication)mActivity.getApplication()).getOrderData().equals("") && !((myApplication)mActivity.getApplication()).getLogData().equals("")) {
                            progressBar.setProgress(100);
                        try {
                            Thread.sleep(1000);
                            Intent intent = new Intent(mcontext, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


            }
        };
        mHandler = new Handler();
        mHandler.post(mRunnable); // Runnable 객체 실행
        mHandler.postAtFrontOfQueue(mRunnable); // Runnable 객체를 Queue 맨앞에 할당
        mHandler.postDelayed(mRunnable, 1000); // Runnable 객체를 1초 뒤에 실행
    }
    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 1000);
    }

    /**
     * 주문데이터 수집
     * the user.
     */
    public class UserOrderDataTask extends AsyncTask<Void, Void, Boolean> {

        UserOrderDataTask() { }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.

                Thread.sleep(1000);
                ArrayList<HttpPostDto> postList = new ArrayList<HttpPostDto>();
                HttpPostDto postDto = new HttpPostDto();
                postDto.setHkey("mode");
                postDto.setHvalue("Order");
                postList.add(postDto);
                Logininfo info = (Logininfo) loginDBManager.selectData(1);
                HttpPostDto postDto1 = new HttpPostDto();
                postDto1.setHkey("m_no");
                postDto1.setHvalue(String.valueOf(info.getM_no()));
                postList.add(postDto1);
                HttpPostDto postDto2 = new HttpPostDto();
                postDto2.setHkey("sregdt");
                postDto2.setHvalue(utils.getDate(0));
                postList.add(postDto2);
                ((myApplication)mActivity.getApplication()).setOrderData(utils.getDataJson(mActivity, mActivity.getString(R.string.shop_url) + "/app/indb.php", postList));

                if(((myApplication)mActivity.getApplication()).getOrderData().equals("")){
                    return false;
                }else{
                    return true;
                }

            } catch (InterruptedException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mOrderTask = null;


            if (success) {
                Log.d("주문정보 완료", ((myApplication)mActivity.getApplication()).getOrderData());
                reset = 0;
                mHandler.postDelayed(mRunnable, 1000);

            }else{
                Log.d("주문정보 실패", ((myApplication)mActivity.getApplication()).getOrderData());
                if(reset < 5) {
                    reset++;
                    Toast.makeText(mActivity, "주문정보를 가져오지 못했습니다. 다시 시도합니다.", Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(mRunnable, 1000);
                }else{
                    Toast.makeText(mActivity, "모바일 네트웍에 문제가 발생했습니다. 확인후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mOrderTask = null;

        }
    }
    /**
     * 통계데이터 수집
     * the user.
     */
    public class UserLogDataTask extends AsyncTask<Void, Void, Boolean> {

        UserLogDataTask() { }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.

                Thread.sleep(1000);
                ArrayList<HttpPostDto> postList = new ArrayList<HttpPostDto>();
                HttpPostDto postDto = new HttpPostDto();
                postDto.setHkey("mode");
                postDto.setHvalue("Log");
                postList.add(postDto);
                Logininfo info = (Logininfo) loginDBManager.selectData(1);
                HttpPostDto postDto1 = new HttpPostDto();
                postDto1.setHkey("m_no");
                postDto1.setHvalue(String.valueOf(info.getM_no()));
                postList.add(postDto1);
                HttpPostDto postDto2 = new HttpPostDto();
                postDto2.setHkey("sregdt");
                postDto2.setHvalue(utils.getDate(-7));
                postList.add(postDto2);
                HttpPostDto postDto3 = new HttpPostDto();
                postDto3.setHkey("dtkind");
                postDto3.setHvalue("orddt");
                postList.add(postDto3);

                ((myApplication)mActivity.getApplication()).setLogData(utils.getDataJson(mActivity, mActivity.getString(R.string.shop_url) + "/app/indb.php", postList));


                if(((myApplication)mActivity.getApplication()).getLogData().equals("")){
                    return false;
                }else{
                    return true;
                }

            } catch (InterruptedException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLogTask = null;


            if (success) {
                Log.d("통계정보 완료", ((myApplication)mActivity.getApplication()).getLogData());
                reset = 0;
                mHandler.postDelayed(mRunnable, 1000);

            }else{
                if(reset < 5) {
                    reset++;
                    Toast.makeText(mActivity, "통계 정보를 가져오지 못했습니다. 다시 시도합니다..", Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(mRunnable, 1000);
                }else{
                    Toast.makeText(mActivity, "모바일 네트웍에 문제가 발생했습니다. 확인후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mLogTask = null;

        }
    }
}

package com.my.app.appgodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my.app.appgodo.Adapter.TalkListAdapter;
import com.my.app.appgodo.Dto.HttpPostDto;
import com.my.app.appgodo.Dto.Logininfo;
import com.my.app.appgodo.Dto.PushDto;
import com.my.app.appgodo.db.PushDBManager;
import com.my.app.appgodo.db.appLoginDBManager;
import com.my.app.appgodo.util.BaseExpandableAdapter;
import com.my.app.appgodo.util.CustomViewPager;
import com.my.app.appgodo.util.utils;

import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private static CustomViewPager mViewPager;
    private static DrawerLayout mDrawerLayout;
    private static ExpandableListView mDrawerList;
    private static ActionBarDrawerToggle mDrawerToggle;
    private static AppCompatActivity mActivity;
    private static Context mContext;
    private static ArrayList<String> mGroupList;
    private static ArrayList<ArrayList<String>> mChildList;
    private static BaseExpandableAdapter mAdapter;
    private static ListView             m_ListView;
    private static TableLayout orderList;
    private static TalkListAdapter mListViewAdapter;
    private static ArrayList<PushDto> mPushList;
    private static float mTouchX, mTouchY;
    private static final String ACTION_KEY_TYPE = "ActionKeyType";
    private static final String ACTION_TALK_TYPE = "ActionTalk";
    private static final String ACTION_KEY_VALUE = "ActionKeyValue";
    private static PushDBManager pushDBManager;
    private static final int ACTION_TYPE_SETTEXT = 0;
    private static final int ACTION_TYPE_SETSCROLL = 1;
    private static final int ACTION_TMODE_SETTEXT = 2;
    private static final int ACTION_ORDER_SETTEXT = 3;
    private static final int ACTION_ORDER_LOAD_SETTEXT = 4;
    private static final int ACTION_LOG_SETTEXT = 5;
    private static final int ACTION_LOG_LOAD_SETTEXT = 6;
    private static final int INVALID_POINTER_ID = -1;
    private static int mActivePointerId = INVALID_POINTER_ID;
    private static View footer, header;
    private static boolean justOnce = true, isHeader = false;
    private static UserOrderDataTask mOrderTask = null;
    private static UserLogDataTask mLogTask = null;
    private static appLoginDBManager loginDBManager;
    private static int tmp1, tmp2, tmp3, tmp4;
    private static String stmp1, stmp2, stmp3, stmp4, stmp5;
    private static String mode = "";
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
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
        mActivity = this;
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.btn_xml_menu);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) mActivity.findViewById(R.id.left_drawer);
        mDrawerToggle = utils.drawerLayOut(mActivity, mDrawerLayout, mDrawerList);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        setGnb();
    }

    /* 클릭리스너 */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    public static void selectItem(final int position) {

        //mDrawerList.setItemChecked(position, true);
        //mDrawerLayout.closeDrawer(mDrawerList);
    }
    public void setGnb() {
        mGroupList = new ArrayList<String>();
        mChildList = new ArrayList<ArrayList<String>>();
        mGroupList.clear();
        mChildList.clear();

        ArrayList<String> mChildListContent1 = new ArrayList<String>();
        ArrayList<String> mChildListContent2 = new ArrayList<String>();
        ArrayList<String> mChildListContent3 = new ArrayList<String>();
        ArrayList<String> mChildListContent4 = new ArrayList<String>();

        mGroupList.add("알림내역");
        mGroupList.add("주문내역");
        mGroupList.add("매출통계");
        //mGroupList.add("환경설정");
        mChildListContent1.clear();
        mChildListContent1.add("알림내역");
        mChildList.add(mChildListContent1);
        mChildListContent2.clear();
        mChildListContent2.add("주문리스트");
       // mChildListContent2.add("주문취소리스트");
      //  mChildListContent2.add("반품/교환접수리스트");
        mChildList.add(mChildListContent2);
        mChildListContent3.clear();
        mChildListContent3.add("일별매출통계");
        mChildListContent3.add("월별매출통계");
        //mChildListContent3.add("일별주문통계");
       // mChildListContent3.add("월별주문통계");
        //mChildListContent3.add("상품별주문통계");
        mChildList.add(mChildListContent3);
        mChildListContent4.clear();
        mChildListContent4.add("환경설정");
        mChildList.add(mChildListContent4);

        mAdapter = new BaseExpandableAdapter(mActivity, mGroupList, mChildList);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        selectItem(0);
        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                switch (groupPosition) {
                    case 0:
                        switch (childPosition) {
                            case 0:
                                mViewPager.setCurrentItem(0);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition) {
                            case 0:
                                mViewPager.setCurrentItem(1);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                break;
                            case 1:
                                mViewPager.setCurrentItem(1);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                break;
                            case 2:
                                mViewPager.setCurrentItem(1);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                break;
                            case 3:
                                mViewPager.setCurrentItem(1);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                break;
                        }
                        break;
                    case 2:

                        switch (childPosition) {
                            case 0:
                                mode = "Log";
                                mViewPager.setCurrentItem(2);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                setTitle("일별매출통계");
                                ((EditText)mActivity.findViewById(R.id.sregdt2)).setText(utils.getDate(-7));
                                ((EditText)mActivity.findViewById(R.id.eregdt2)).setText(utils.getDate(0));
                                sendActionMsg(ACTION_LOG_LOAD_SETTEXT, mode);
                                break;
                            case 1:
                                mode = "Log1";
                                mViewPager.setCurrentItem(2);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                setTitle("월별매출통계");
                                ((EditText)mActivity.findViewById(R.id.sregdt2)).setText(utils.getMonth(-2));
                                ((EditText)mActivity.findViewById(R.id.eregdt2)).setText(utils.getMonth(0));
                                sendActionMsg(ACTION_LOG_LOAD_SETTEXT, mode);
                                break;
                            case 2:
                                mode = "Log2";
                                mViewPager.setCurrentItem(2);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                ((EditText)mViewPager.getChildAt(2).findViewById(R.id.sregdt2)).setText(utils.getDate(-7));
                                ((EditText)mViewPager.getChildAt(2).findViewById(R.id.eregdt2)).setText(utils.getDate(0));
                                sendActionMsg(ACTION_LOG_LOAD_SETTEXT, mode);
                                break;
                            case 3:
                                mode = "Log3";
                                mViewPager.setCurrentItem(2);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                sendActionMsg(ACTION_LOG_LOAD_SETTEXT, mode);
                                break;
                            case 4:
                                mode = "Log4";
                                mViewPager.setCurrentItem(2);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                sendActionMsg(ACTION_LOG_LOAD_SETTEXT, mode);
                                break;
                        }
                        break;
                    case 3:
                        switch (childPosition) {
                            case 0:
                                mViewPager.setCurrentItem(3);
                                mDrawerLayout.closeDrawer(mDrawerList);
                                break;
                        }
                        break;
                }
                return false;
            }
        });
        sendActionMsg(ACTION_TYPE_SETTEXT, "테스트");


    }
    //핸들러 호출 함수
    public static void sendActionMsg(int action, String value) {
        Message msg = mActionHandler.obtainMessage();

        Bundle bundle = new Bundle();
        bundle.putInt(ACTION_KEY_TYPE, action);
        bundle.putString(ACTION_KEY_VALUE, value);

        msg.setData(bundle);
        mActionHandler.sendMessage(msg);
    }

    public static void sendActionMsg(int action, int value) {
        Message msg = mActionHandler.obtainMessage();

        Bundle bundle = new Bundle();
        bundle.putInt(ACTION_KEY_TYPE, action);
        bundle.putInt(ACTION_KEY_VALUE, value);

        msg.setData(bundle);
        mActionHandler.sendMessage(msg);
    }
    public static AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.d("scroll", firstVisibleItem + "-" + visibleItemCount + "-" + totalItemCount);

        }
    };
    public static  View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mActivePointerId = event.getPointerId(0);
                    break;
                case MotionEvent.ACTION_MOVE :
                    isHeader = true;
                    break;
                case MotionEvent.ACTION_UP:
                    if(isHeader == true) {
                        isHeader = false;
                        sendActionMsg(ACTION_TMODE_SETTEXT, "테스트");
                        m_ListView.removeHeaderView(header);
                        header = null;
                    }
                    break;
            }
            return false;
        }
    };
    public static Handler mActionHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch(data.getInt(ACTION_KEY_TYPE)) {
                case ACTION_TYPE_SETTEXT:
                    if(mDrawerList.getAdapter() == null){
                        mDrawerList.setAdapter(mAdapter);
                    }else{
                        mDrawerList.invalidateViews();
                        mAdapter.notifyDataSetChanged();
                        mDrawerList.setAdapter(mAdapter);
                    }

                    break;
                case ACTION_ORDER_SETTEXT:
                    orderList = (TableLayout) mActivity.findViewById(R.id.orderList);

                    utils.setOrderList(mActivity, orderList, ((myApplication)mActivity.getApplication()).getOrderData());
                    break;
                case ACTION_LOG_SETTEXT:
                    orderList = (TableLayout) mActivity.findViewById(R.id.orderList2);

                    utils.setLogList(mActivity, orderList, ((myApplication) mActivity.getApplication()).getLogData());
                    break;
                case ACTION_TMODE_SETTEXT:

                    pushDBManager = new PushDBManager(mContext);
                    mPushList = pushDBManager.search(0);
                    m_ListView = (ListView) mActivity.findViewById(R.id.listView);
                    mListViewAdapter = new TalkListAdapter(mContext, mPushList);
                    m_ListView.setAdapter(mListViewAdapter);
                    break;

                case ACTION_ORDER_LOAD_SETTEXT:
                    if (mOrderTask != null) {
                        return;
                    }
                    EditText sword = (EditText) mActivity.findViewById(R.id.sword);
                    EditText sgword = (EditText) mActivity.findViewById(R.id.sgword);
                    EditText sregdt = (EditText) mActivity.findViewById(R.id.sregdt);
                    EditText eregdt = (EditText) mActivity.findViewById(R.id.eregdt);
                    Spinner skey = (Spinner) mActivity.findViewById(R.id.skey);
                    Spinner sgkey = (Spinner) mActivity.findViewById(R.id.sgkey);
                    Spinner dtkind = (Spinner) mActivity.findViewById(R.id.dtkind);

                    mOrderTask = new UserOrderDataTask(skey.getSelectedItemPosition(), sgkey.getSelectedItemPosition(), dtkind.getSelectedItemPosition(), sword.getText().toString(), sgword.getText().toString(), sregdt.getText().toString(), eregdt.getText().toString());
                    mOrderTask.execute((Void) null);
                    break;
                case ACTION_LOG_LOAD_SETTEXT:
                    if (mLogTask != null) {
                        return;
                    }
                    sword = (EditText) mActivity.findViewById(R.id.sword2);
                    sregdt = (EditText) mActivity.findViewById(R.id.sregdt2);
                    eregdt = (EditText) mActivity.findViewById(R.id.eregdt2);
                    skey = (Spinner) mActivity.findViewById(R.id.skey2);
                    dtkind = (Spinner) mActivity.findViewById(R.id.dtkind2);
                    Log.d("---", mode);
                    mLogTask = new UserLogDataTask(skey.getSelectedItemPosition(), dtkind.getSelectedItemPosition(), sword.getText().toString(), sregdt.getText().toString(), eregdt.getText().toString());
                    mLogTask.execute((Void) null);
                    break;
            }

        }
    };
    /**
     * 통계데이터 수집
     * the user.
     */
    public static class UserLogDataTask extends AsyncTask<Void, Void, Boolean> {
        private int skey=0, dtkind=0;
        private String sword, sregdt, eregdt;
        UserLogDataTask(int skey, int dtkind, String sword, String sregdt, String eregdt) {
        this.skey = skey; this.dtkind = dtkind; this.sword = sword; this.sregdt = sregdt; this.eregdt = eregdt;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                String[] Skeyitem = new String[]{"OI.goodsnm","OI.goodsno","G.goodscd"};
                String[] dtkinditem = new String[]{"orddt","cdt","ddt","confirmdt"};


                Thread.sleep(1000);
                ArrayList<HttpPostDto> postList = new ArrayList<HttpPostDto>();
                HttpPostDto postDto = new HttpPostDto();
                postDto.setHkey("mode");
                if(mode=="") {
                    postDto.setHvalue("Log");
                }else{
                    postDto.setHvalue(mode);
                }

                postList.add(postDto);
                loginDBManager = new appLoginDBManager(mActivity);
                Logininfo info = (Logininfo) loginDBManager.selectData(1);
                HttpPostDto postDto1 = new HttpPostDto();
                postDto1.setHkey("m_no");
                postDto1.setHvalue(String.valueOf(info.getM_no()));
                postList.add(postDto1);

                HttpPostDto postDto2 = new HttpPostDto();
                postDto2.setHkey("skey");
                postDto2.setHvalue(Skeyitem[skey]);
                postList.add(postDto2);

                HttpPostDto postDto4 = new HttpPostDto();
                postDto4.setHkey("dtkind");
                postDto4.setHvalue(dtkinditem[dtkind]);
                postList.add(postDto4);

                HttpPostDto postDto5 = new HttpPostDto();
                postDto5.setHkey("sword");
                postDto5.setHvalue(sword);
                postList.add(postDto5);

                HttpPostDto postDto7 = new HttpPostDto();
                postDto7.setHkey("sregdt");
                postDto7.setHvalue(sregdt);
                postList.add(postDto7);

                HttpPostDto postDto8 = new HttpPostDto();
                postDto8.setHkey("eregdt");
                postDto8.setHvalue(eregdt);
                postList.add(postDto8);
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
                Log.d(mode +"통계정보 완료", ((myApplication) mActivity.getApplication()).getLogData());
                sendActionMsg(ACTION_LOG_SETTEXT, mode);

            }else{
                Log.d(mode +"통계정보 실패", ((myApplication)mActivity.getApplication()).getLogData());
                Toast.makeText(mActivity, "모바일 네트웍에 문제가 발생했습니다. 확인후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mLogTask = null;

        }
    }
    /**
     * 주문데이터 수집
     * the user.
     */
    public static class UserOrderDataTask extends AsyncTask<Void, Void, Boolean> {
        private int skey=0, sgkey=0, dtkind=0;
        private String sword, sgword, sregdt, eregdt;
        UserOrderDataTask(int skey,int sgkey,int dtkind, String sword,String sgword,String sregdt, String eregdt) {
            this.skey = skey; this.sgkey = sgkey; this.dtkind = dtkind; this.sword = sword; this.sgword = sgword; this.sregdt = sregdt; this.eregdt = eregdt;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                String[] Skeyitem = new String[]{"all","ordno","nameOrder","nameReceiver","m_id", "mobileOrder"};
                String[] sgkeyitem = new String[]{"goodsnm","brandnm","maker"};
                String[] dtkinditem = new String[]{"orddt","cdt","ddt"};


                Thread.sleep(1000);
                ArrayList<HttpPostDto> postList = new ArrayList<HttpPostDto>();
                HttpPostDto postDto = new HttpPostDto();
                postDto.setHkey("mode");
                postDto.setHvalue("Order");
                postList.add(postDto);
                loginDBManager = new appLoginDBManager(mActivity);
                Logininfo info = (Logininfo) loginDBManager.selectData(1);
                HttpPostDto postDto1 = new HttpPostDto();
                postDto1.setHkey("m_no");
                postDto1.setHvalue(String.valueOf(info.getM_no()));
                postList.add(postDto1);

                HttpPostDto postDto2 = new HttpPostDto();
                postDto2.setHkey("skey");
                postDto2.setHvalue(Skeyitem[skey]);
                postList.add(postDto2);
                HttpPostDto postDto3 = new HttpPostDto();
                postDto3.setHkey("sgkey");
                postDto3.setHvalue(sgkeyitem[sgkey]);
                postList.add(postDto3);
                HttpPostDto postDto4 = new HttpPostDto();
                postDto4.setHkey("dtkind");
                postDto4.setHvalue(dtkinditem[dtkind]);
                postList.add(postDto4);

                HttpPostDto postDto5 = new HttpPostDto();
                postDto5.setHkey("sword");
                postDto5.setHvalue(sword);
                postList.add(postDto5);

                HttpPostDto postDto6 = new HttpPostDto();
                postDto6.setHkey("sgword");
                postDto6.setHvalue(sgword);
                postList.add(postDto6);

                HttpPostDto postDto7 = new HttpPostDto();
                postDto7.setHkey("sregdt");
                postDto7.setHvalue(sregdt);
                postList.add(postDto7);

                HttpPostDto postDto8 = new HttpPostDto();
                postDto8.setHkey("eregdt");
                postDto8.setHvalue(eregdt);
                postList.add(postDto8);
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
                Log.d("주문정보 완료", ((myApplication) mActivity.getApplication()).getOrderData());
                sendActionMsg(ACTION_ORDER_SETTEXT, "테스트");

            }else{
                Log.d("주문정보 실패", ((myApplication)mActivity.getApplication()).getOrderData());
                Toast.makeText(mActivity, "모바일 네트웍에 문제가 발생했습니다. 확인후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mOrderTask = null;

        }
    }
    public static Spinner.OnItemSelectedListener myOnitemSelected = new Spinner.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (view.getId()){
                case R.id.skey:
                    tmp1 = position;
                    break;
                case R.id.sgkey:
                    tmp2 = position;
                    break;
                case R.id.dtkind:
                    tmp3 = position;
                    break;
                case R.id.skey2:
                    tmp1 = position;
                    break;
                case R.id.dtkind2:
                    tmp3 = position;
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    public static View.OnClickListener mDateDatepicker = new View.OnClickListener(){
        private int myYear, myMonth, myDay, myHour, myMinute;
        final Calendar c = Calendar.getInstance();
        @Override
        public void onClick(View v) {
            myYear = c.get(Calendar.YEAR);
            myMonth = c.get(Calendar.MONTH);
            myDay = c.get(Calendar.DAY_OF_MONTH);
            switch (v.getId()){
                case R.id.sregdt :
                    Dialog dlgsDate = new DatePickerDialog(mActivity, myDateSetsListener,  myYear, myMonth, myDay);
                    dlgsDate.show();
                    break;
                case R.id.eregdt :
                    Dialog dlgeDate = new DatePickerDialog(mActivity, myDateSeteListener,  myYear, myMonth, myDay);
                    dlgeDate.show();
                    break;
                case R.id.sregdt2 :
                    Dialog dlgsDate2 = new DatePickerDialog(mActivity, myDateSetsListener2,  myYear, myMonth, myDay);
                    dlgsDate2.show();
                    break;
                case R.id.eregdt2 :
                    Dialog dlgeDate2 = new DatePickerDialog(mActivity, myDateSeteListener2,  myYear, myMonth, myDay);
                    dlgeDate2.show();
                    break;
                case R.id.btnOrder :
                        sendActionMsg(ACTION_ORDER_LOAD_SETTEXT, "ddd");

                    break;
                case R.id.btnOrder2 :
                    /*
                    sword = (EditText) mActivity.findViewById(R.id.sword2);
                    sregdt = (EditText) mActivity.findViewById(R.id.sregdt2);
                    eregdt = (EditText) mActivity.findViewById(R.id.eregdt2);
                    stmp1 = sword.getText().toString();
                    stmp2 = sregdt.getText().toString();
                    stmp3 = eregdt.getText().toString();
*/
                    sendActionMsg(ACTION_LOG_LOAD_SETTEXT, "테스트");

                    break;
            }
        }
    };
    public static DatePickerDialog.OnDateSetListener myDateSetsListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            EditText sregdt = (EditText)mActivity.findViewById(R.id.sregdt);
            sregdt.setText(String.valueOf(year)+ String.valueOf(monthOfYear + 1) + String.valueOf(dayOfMonth));
        }
    };
    public static DatePickerDialog.OnDateSetListener myDateSeteListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            EditText eregdt = (EditText)mActivity.findViewById(R.id.eregdt);
            eregdt.setText(String.valueOf(year)+ String.valueOf(monthOfYear + 1) + String.valueOf(dayOfMonth));
        }
    };
    public static DatePickerDialog.OnDateSetListener myDateSetsListener2 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            EditText sregdt = (EditText)mActivity.findViewById(R.id.sregdt2);
            switch (mode){
                case "Log1":
                    sregdt.setText(String.valueOf(year)+ String.valueOf(monthOfYear + 1) );
                    break;
                default:
                    sregdt.setText(String.valueOf(year)+ String.valueOf(monthOfYear + 1) + String.valueOf(dayOfMonth));
                    break;
            }

        }
    };
    public static DatePickerDialog.OnDateSetListener myDateSeteListener2 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            EditText eregdt = (EditText)mActivity.findViewById(R.id.eregdt2);
            switch (mode){
                case "Log1":
                    eregdt.setText(String.valueOf(year)+ String.valueOf(monthOfYear + 1) );
                    break;
                default:
                    eregdt.setText(String.valueOf(year)+ String.valueOf(monthOfYear + 1) + String.valueOf(dayOfMonth));
                    break;
            }

        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed()
    {
        // super.onBackPressed();
        utils.setDiglogEndingMsg(mActivity, getString(R.string.app_name) + "(을)를 종료 하시겠습니까?");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.talk, container, false);
                    sendActionMsg(ACTION_TMODE_SETTEXT, "테스트");
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.order, container, false);

                    ImageButton btnOrder = (ImageButton) rootView.findViewById(R.id.btnOrder);
                    btnOrder.setOnClickListener(mDateDatepicker);
                    EditText sregdt = (EditText)rootView.findViewById(R.id.sregdt);
                    sregdt.setText(utils.getDate(0));
                    sregdt.setOnClickListener(mDateDatepicker);
                    EditText eregdt = (EditText)rootView.findViewById(R.id.eregdt);
                    eregdt.setText(utils.getDate(0));
                    eregdt.setOnClickListener(mDateDatepicker);

                    sendActionMsg(ACTION_ORDER_SETTEXT, mode);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.log, container, false);
                    ImageButton btnOrder2 = (ImageButton) rootView.findViewById(R.id.btnOrder2);
                    btnOrder2.setOnClickListener(mDateDatepicker);
                     sregdt = (EditText)rootView.findViewById(R.id.sregdt2);
                    sregdt.setText(utils.getDate(-7));
                    sregdt.setOnClickListener(mDateDatepicker);
                    eregdt = (EditText)rootView.findViewById(R.id.eregdt2);
                    eregdt.setText(utils.getDate(0));
                    eregdt.setOnClickListener(mDateDatepicker);
                    sendActionMsg(ACTION_LOG_SETTEXT, mode);

                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.config, container, false);
                    break;
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_section1);
                case 1:
                    Log.d("1111", "Order");
                    return getString(R.string.title_section2);
                case 2:
                    Log.d("1111", "Log");
                    return getString(R.string.title_section3);
                case 3:
                    return getString(R.string.title_section4);
            }
            return null;
        }
    }

}

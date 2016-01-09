package com.my.app.appgodo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.my.app.appgodo.Dto.HttpPostDto;
import com.my.app.appgodo.Dto.Logininfo;
import com.my.app.appgodo.db.appLoginDBManager;
import com.my.app.appgodo.util.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button btn_button;
    private UserLoginTask mAuthTask = null;
    private final String Tag = "Login";
    private AppCompatActivity mActivity;
    private static final String ACTION_KEY_TYPE = "ActionKeyType";
    private static final String ACTION_KEY_VALUE = "ActionKeyValue";
    private appLoginDBManager loginDBManager;
    private static final int ACTION_TYPE_SETTEXT = 0;
    private static final int ACTION_TYPE_SETSCROLL = 1;
    private static Bundle extra;
    private static Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mActivity = this;
        setTitle("로그인해주세요");
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        btn_button = (Button)findViewById(R.id.email_sign_in_button);
        btn_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.setEnabled(true);
                attemptLogin();
            }
        });
        extra = new Bundle();
        intent = getIntent();
    }
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Log.d("로그인", "로그인 중 입니다.");
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String result;
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.

                Thread.sleep(2000);
                ArrayList<HttpPostDto> postList = new ArrayList<HttpPostDto>();
                HttpPostDto postDto = new HttpPostDto();
                postDto.setHkey("mode");
                postDto.setHvalue("Login");
                postList.add(postDto);
                HttpPostDto postDto1 = new HttpPostDto();
                postDto1.setHkey("m_email");
                postDto1.setHvalue(mEmail);
                postList.add(postDto1);
                HttpPostDto postDto2 = new HttpPostDto();
                postDto2.setHkey("m_pw");
                postDto2.setHvalue(mPassword);
                postList.add(postDto2);
                HttpPostDto postDto3 = new HttpPostDto();
                postDto3.setHkey("m_gcm");
                postDto3.setHvalue(((myApplication)mActivity.getApplication()).getRegGCMId());
                postList.add(postDto3);
                HttpPostDto postDto4 = new HttpPostDto();
                postDto4.setHkey("mos");
                postDto4.setHvalue("android");
                postList.add(postDto4);
                result = utils.getDataJson(mActivity, mActivity.getString(R.string.shop_url) + "/app/indb.php", postList);

                if(result.equals("false")){
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
            mAuthTask = null;


            if (success) {
                Log.d(Tag, result);
                try {

                    loginDBManager = new appLoginDBManager(mActivity);
                    Logininfo info = new Logininfo();
                    JSONObject json = new JSONObject(result);
                    info.setM_no(json.getLong("m_no"));
                    info.setM_id(json.getString("m_id"));
                    info.setM_name(json.getString("name"));
                    loginDBManager.insertData(info);
                    extra.putInt("data", 1);
                    intent.putExtras(extra);
                    Log.e("로그인 체크 ", String.valueOf(RESULT_OK));
                    mActivity.setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                btn_button.setEnabled(false);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }
    }
}

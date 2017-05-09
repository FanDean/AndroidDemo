package com.fandean.floatinglabels;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 本Demo功能并不完善。比如没有验证用户名是否包含特殊字符；没有验证密码的最小长度；没有让用户输入两次密码来确认密码。
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mInputName, mInputEmail, mInputPassword;
    private TextInputLayout mInputLayoutName, mInputLayoutEmail, mInputLayoutPassword;
    private Button mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mInputName = (EditText) findViewById(R.id.input_name);
        mInputEmail = (EditText) findViewById(R.id.input_email);
        mInputPassword = (EditText) findViewById(R.id.input_password);
        mInputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        mInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        mInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        mBtnSignUp = (Button) findViewById(R.id.btn_signup);


/*        //监听EditText的文本更改，会存在当用户不输入任何内容直接离开，却不提示错误的情况
        mInputName.addTextChangedListener(new MyTextWatcher(mInputName));
        mInputEmail.addTextChangedListener(new MyTextWatcher(mInputEmail));
        mInputPassword.addTextChangedListener(new MyTextWatcher(mInputPassword));*/

        mInputName.setOnFocusChangeListener(new MyFocusChange());
        mInputEmail.setOnFocusChangeListener(new MyFocusChange());
        mInputPassword.setOnFocusChangeListener(new MyFocusChange());

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    /**
     * 验证表单
     */
    private void submitForm(){
        if (!validateName()) return;
        if (!validateEmail()) return;
        if (!validatePassword()) return;

        //验证成功，弹出提示
        Toast.makeText(getApplicationContext(), "注册成功，Thank You!", Toast.LENGTH_LONG).show();
    }

    private void requestFocus(View view){
        //requestFocus()使该视图获得焦点，但不是每次都能成功，所以需要判断
//        if (view.requestFocus()){
//            //设置输入模式，弹出输入法输入界面
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
    }

    private boolean validateName(){
        if(mInputName.getText().toString().trim().isEmpty()){
            //设置错误提示
            mInputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(mInputName);
            return false;
        } else {
            //关闭(清除)之前的错误提示
            mInputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail(){
        String email = mInputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)){
            mInputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(mInputEmail);
            return false;
        } else {
            mInputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword(){
        if (mInputPassword.getText().toString().trim().isEmpty()){
            mInputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(mInputPassword);
            return false;
        } else {
            mInputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email){

        //判断email是否为空和判断email是否符合规范（使用了正则表达式）
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

/*
    private class MyTextWatcher implements TextWatcher{
        private View mView;

        private MyTextWatcher(View view){
            mView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (mView.getId()){
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                default:
                    break;
            }
        }
    }*/

    private class MyFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) { //失去焦点
                switch (v.getId()) {
                    case R.id.input_name:
                        validateName();
                        break;
                    case R.id.input_email:
                        validateEmail();
                        break;
                    case R.id.input_password:
                        validatePassword();
                        break;
                    default:
                        break;
                }
            } else {
                //获取焦点
            }
        }
    }
}

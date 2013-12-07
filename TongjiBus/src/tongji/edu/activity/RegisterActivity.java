package tongji.edu.activity;

import tongji.edu.url.AllUrl;
import tongji.edu.util.AllActivity;
import tongji.edu.util.Constants;
import tongji.edu.util.MyDialogFactory;
import tongji.edu.util.NetWork;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 注册模块
 * 
 * @author zlj
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener {


	private Button mBtnRegister; // 注册按钮
	private Button mRegBack; // 返回按钮
	private EditText mNameEt, mPasswdEt, mPasswdEt2,mPhone; // 姓名，密码和密码确认,以及用户手机号码
	private Dialog mDialog = null; // 对话框
	private String ip, port;
	private String username;

	private String password;

	private Handler registerhandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题
		setContentView(R.layout.register);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		ip = prefs.getString(this.getString(R.string.ksetserver), Constants.ip);
		port = prefs.getString(this.getString(R.string.ksetport), Constants.port);
		initView();
		AllActivity allActivity = AllActivity.getInstance();
		allActivity.addActivity(this);

	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		mNameEt = (EditText) findViewById(R.id.reg_name);
		mPasswdEt = (EditText) findViewById(R.id.reg_password);
		mPasswdEt2 = (EditText) findViewById(R.id.reg_password2);
		mPhone=(EditText)findViewById(R.id.reg_phone);
		
		

		mBtnRegister = (Button) findViewById(R.id.register_btn);
		mRegBack = (Button) findViewById(R.id.reg_back_btn);
		mBtnRegister.setOnClickListener(this);
		mRegBack.setOnClickListener(this);

	}

	/**
	 * 监听不同的按钮点击事件
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.register_btn:
			estimate();
			break;
		case R.id.reg_back_btn:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = MyDialogFactory.creatRequestDialog(this, "正在注册...");
		mDialog.show();
	}

	private void dismissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	/**
	 * 进行注册之前判断
	 */
	private void estimate() {
		final String name = mNameEt.getText().toString().trim();
		final String passwd = mPasswdEt.getText().toString().trim();
		String passwd2 = mPasswdEt2.getText().toString().trim();
		String phone=mPhone.getText().toString().trim();
		username = name;
		password = passwd;

		// 前台验证，带*不能为空
		if (name.equals("") || passwd.equals("") || passwd2.equals("") ) {
			MyDialogFactory.toastDialog(RegisterActivity.this, "用户注册", "*选项不能为空");
		} else if (!passwd.equals(passwd2)) {
			MyDialogFactory.toastDialog(RegisterActivity.this, "用户注册",
					"两次密码输入不一致");
			mPasswdEt.setText("");
			mPasswdEt2.setText("");
			mPasswdEt.setHint("");
			mPasswdEt.setHint("");
		} else if (!NetWork.isNetworkAvailable(this)) {
			toastNetWork(this);
		} else {

			showRequestDialog();
			registAccount(name, passwd,phone);
		}

	}

	/**
	 * 在服务器上注册帐号
	 */
	private void registAccount(final String username, final String password,final String phone) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				final String in=new AllUrl().registe(ip, port, username, password, phone);
				registerhandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						dismissDialog();
						if(in.equals("0")){
							Toast.makeText(RegisterActivity.this, "对不起,该学号已经被注册", 1).show();
						}else if(in.equals("1")){
							Toast.makeText(RegisterActivity.this, "注册成功，请登录同济大学邮箱进行激活", 1).show();
						}else{
							Toast.makeText(RegisterActivity.this, "对不起,网络连接出错", 1).show();
						}
						
					}
				});
				
				
			}

		}).start();
	}

	private void toastNetWork(Context context) {
		new AlertDialog.Builder(context)
				.setTitle("提示")
				.setMessage("网络连接未打开")
				.setPositiveButton("前往打开",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(intent);
							}
						}).setNegativeButton("取消", null).create().show();
	}
}

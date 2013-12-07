package tongji.edu.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tongji.edu.bean.LoginResponeBean;
import tongji.edu.bean.RouteBean;
import tongji.edu.url.AllUrl;
import tongji.edu.util.Constants;
import tongji.edu.util.MyDialogFactory;
import tongji.edu.util.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LoginActivity extends Activity {
	private EditText Et_user;
	private EditText Et_pwd;
	private String username, password, ip, port, weekend;
	private TextView findpwd, regist;
	private Button login;
	private SharedPreferences sharedPrefrences;
	private Handler mHandler = new Handler();
	private Dialog mDialog;

	private Thread loginThread; // 登录线程，点击取消可以打断
	private long animationTime=700;  //动画持续时间

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LayoutInflater inflate=LayoutInflater.from(LoginActivity.this);
		View parents=inflate.inflate(R.layout.activity_login, null);
		setContentView(parents);
		
		LinearLayout lay=(LinearLayout) parents.findViewById(R.id.login_up);
		Button loginbtn=(Button)parents.findViewById(R.id.login_btn);
		
		AnimationSet ani=new AnimationSet(true);
		final TranslateAnimation tanimation = new TranslateAnimation(
				0, 0, 250, 0);
		tanimation
				.setDuration(animationTime);// 设置动画持续时间
		
		AlphaAnimation aAnimation=new AlphaAnimation(0, 1);
		aAnimation.setDuration(animationTime);
		
		ani.addAnimation(tanimation);
		ani.addAnimation(aAnimation);
		
		
		
		lay.setAnimation(ani);
		loginbtn.setAnimation(ani);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		init();
		super.onResume();
	}

	private void init() {
		Et_user = (EditText) findViewById(R.id.login_username);
		Et_pwd = (EditText) findViewById(R.id.login_password);
		findpwd = (TextView) findViewById(R.id.find_pwd);
		regist = (TextView) findViewById(R.id.register_txt);
		login = (Button) findViewById(R.id.login_btn);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		ip = prefs.getString(this.getString(R.string.ksetserver), Constants.ip);
		port = prefs.getString(this.getString(R.string.ksetport),
				Constants.port);

		sharedPrefrences = this.getSharedPreferences("user",
				MODE_WORLD_READABLE);

		if (sharedPrefrences != null && Constants.logout_flag == false) {
			Et_user.setText(sharedPrefrences.getString("username", null));
			Et_pwd.setText(sharedPrefrences.getString("password", null));

		}
		findpwd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final EditText in = new EditText(LoginActivity.this);
				AlertDialog a = new AlertDialog.Builder(LoginActivity.this)
						.setTitle("请输入账号,找回的密码系统将发送到您的同济大学邮箱")
						.setView(in)
						.setNegativeButton("取消", null)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										String username = in.getText()
												.toString();
										findPassword(username);
									}
								}).create();
				a.show();
			}
		});
		regist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = Et_user.getText().toString().trim();
				password = Et_pwd.getText().toString().trim();
				if (username.equals("") || password.equals("")) {
					Toast.makeText(LoginActivity.this, "用户名和密码均不能为空",
							Toast.LENGTH_LONG).show();
				} else {
					// 将信息写入sharedPreference
					Editor editor = sharedPrefrences.edit();
					editor.putString("username", username);
					editor.putString("password", password);
					editor.commit();
					showRequestDialog("正在登录,请稍等");
					if (Utils.dayForWeek(new Date()) == 5
							|| Utils.dayForWeek(new Date()) == 6) {
						weekend = "1";
					} else {
						weekend = "0";
					}
					startLogin(username, password, weekend);
				}

			}
		});

	}

	/**
	 * 尝试登录
	 */
	private void startLogin(final String username, final String password,
			final String weekend) {
		loginThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(Constants.sleeptime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String in = new AllUrl().login(ip, port, username, password,
						weekend);
				System.out.println("ip:" + ip + "  port" + port + "  username"
						+ username + "  password" + password + "  weekend"
						+ weekend);
				if (in.equals("0")) { // 如果登录失败，即用户名或者密码有误
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(LoginActivity.this,
									"对不起，您输入的用户名密码有误", 1).show();
							dismissDialog();
						}
					});
				} else if (in.equals("")) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(LoginActivity.this, "对不起，您未打开网络连接",
									1).show();
							dismissDialog();
						}
					});
				} else { // 登录成功，即可跳转
					Constants.username = username; // 临时保存用户名，抢座时候会用到
					final LoginResponeBean loginRespon = new Gson().fromJson(
							in, LoginResponeBean.class);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dismissDialog();
							Intent intent = new Intent(LoginActivity.this,
									HomeActivity.class);
							intent.putExtra("routelist",
									loginRespon.getRoutelist());
							Constants.isAdmin = (loginRespon.getIsAdmin() == 1 ? true
									: false);
							startActivity(intent);
							finish();

						}
					});
				}

			}
		});
		loginThread.start();
	}

	private void findPassword(final String username) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String in = new AllUrl().findPwd(ip, port, username);
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if ("1".equals(in)) {
							Toast.makeText(LoginActivity.this,
									"密码已发送到您的同济大学邮箱", 1).show();
						} else if ("0".equals(in)) {
							Toast.makeText(LoginActivity.this, "不存在该用户,密码找回失败",
									1).show();
						} else if ("".equals(in)) {
							Toast.makeText(LoginActivity.this, "网络异常", 1)
									.show();
						}
					}
				});

			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_settings:
			// Intent intent = new Intent(LoginActivity.this,
			// Preferences.class);
			// startActivity(intent);
			// break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 显示请求栏
	 * 
	 * @param s
	 */
	private void showRequestDialog(String s) {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = MyDialogFactory.creatRequestDialog(this, s + "...");
		mDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				System.out.println("取消登录");
				loginThread.interrupt();

			}
		});
		mDialog.show();
	}

	/**
	 * 取消请求栏
	 */
	private void dismissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

}

package tongji.edu.activity;

import java.util.ArrayList;

import tongji.edu.bean.RouteBean;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class HomeActivity extends TabActivity {

	private RadioGroup radioGroup;
	private TabHost mTabHost;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐去标题栏（应用程序的名字）
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		radioGroup = (RadioGroup) findViewById(R.id.main_tab_group);
		mTabHost = getTabHost();
		// Intent用于activity之间通信
		intent = new Intent(this, MyTabActivity.class);
		// Tab页跳转切换操作，切换到不同页面
		mTabHost.addTab(mTabHost.newTabSpec("车次").setIndicator("")
				.setContent(intent));
		mTabHost.addTab(mTabHost.newTabSpec("凭证").setIndicator("")
				.setContent(intent));
		mTabHost.addTab(mTabHost.newTabSpec("历史").setIndicator("")
				.setContent(intent));
		mTabHost.addTab(mTabHost.newTabSpec("退票").setIndicator("")
				.setContent(intent));

		ArrayList<RouteBean> routelist = (ArrayList<RouteBean>) getIntent()
				.getSerializableExtra("routelist");

		intent.putExtra("routelist", routelist);

		intent.putExtra("tabhost", 0);
		mTabHost.setCurrentTab(0);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.bus_num:// 车次
					intent.putExtra("tabhost", 0);
					ArrayList<RouteBean> routelist = (ArrayList<RouteBean>) getIntent()
							.getSerializableExtra("routelist");

					System.out.println(routelist);
					intent.putExtra("routelist", routelist);
					mTabHost.setCurrentTab(0);
					break;
				case R.id.record_num:// 凭证
					intent.putExtra("tabhost", 1);
					mTabHost.setCurrentTab(1);
					break;
				case R.id.history:// 历史
					intent.putExtra("tabhost", 2);
					mTabHost.setCurrentTab(2);
					break;
				case R.id.cancel_ticket:// 退票
					intent.putExtra("tabhost", 3);
					mTabHost.setCurrentTab(3);
					break;
				default:
					break;
				}
			}
		});
	}
	
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				new AlertDialog.Builder(HomeActivity.this)
						.setTitle("温馨提示")
						.setMessage("您确定要退出吗?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										HomeActivity.this.finish();
									}
								}).setNegativeButton("取消", null).create()
						.show();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

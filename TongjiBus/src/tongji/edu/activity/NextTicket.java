package tongji.edu.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import tongji.edu.bean.TicketBean;
import tongji.edu.url.AllUrl;
import tongji.edu.util.Constants;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

public class NextTicket extends FragmentActivity {
	private LinearLayout allView;
	private LayoutInflater inflater;
	private View two;
	private ArrayList<TicketBean> ticketlist;
	private boolean isCancel; // 是否为退票界面
	private Handler mHandler = new Handler();
	private String ip, port;

	private static final String MAP_FRAGMENT_TAG = "map";
	private AMap aMap;
	private SupportMapFragment mMapFragment;
	private HashMap<String, String> palaceFilter = new HashMap<String, String>();
	private Constants constants;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.tab_main);
		allView = (LinearLayout) findViewById(R.id.rest);

		inflater = LayoutInflater.from(getApplicationContext());
		ticketlist = (ArrayList<TicketBean>) getIntent().getSerializableExtra(
				"ticketlist");
		isCancel = getIntent().getBooleanExtra("isCancel", false);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		ip = prefs.getString(this.getString(R.string.ksetserver), Constants.ip);
		port = prefs.getString(this.getString(R.string.ksetport),
				Constants.port);

		palaceFilter.put("同济北苑", "北苑");
		palaceFilter.put("嘉定校区", "嘉定");
		palaceFilter.put("四平校区", "四平");
		palaceFilter.put("沪西校区", "沪西");
		palaceFilter.put("曹杨八村", "曹杨");
	}

	/**
	 * 显示下一张凭证
	 * 
	 * @param ticket
	 */
	private void clickNext(TicketBean ticket) {
		two = inflater.inflate(R.layout.tab2, null);
		Button next = (Button) two.findViewById(R.id.next_btn);
		next.setVisibility(View.GONE);

		setUsernameInTicket(two, ticket);
		Button back = (Button) two.findViewById(R.id.back_btn);
		back.setText("上一张");
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		TextView title = (TextView) two.findViewById(R.id.textView2);
		title.setText("车票(2/2)");
		Button cancel = (Button) two.findViewById(R.id.cancel);
		if (!isCancel)
			cancel.setVisibility(View.GONE);
		else {
			cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog a = new AlertDialog.Builder(NextTicket.this)
							.setMessage("确认要退掉这张票吗?")
							.setNegativeButton("取消", null)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO
											// Auto-generated
											// method stub
											cancelTicket(ticketlist.get(1));
										}
									}).create();
					a.show();

				}
			});
		}

		ImageView bus_icon = (ImageView) two.findViewById(R.id.bus_icon);
		ProgressBar pb = (ProgressBar) two.findViewById(R.id.progress);

		TextView gotime = (TextView) two.findViewById(R.id.when_to_go);
		TextView start_place = (TextView) two.findViewById(R.id.start_place);
		TextView end_place = (TextView) two.findViewById(R.id.end_place);
		TextView path_line = (TextView) two.findViewById(R.id.path_line);
		TicketBean one = ticket;
		gotime.setText(one.getTicket_time());
		start_place.setText(palaceFilter.get(one.getStart()));
		end_place.setText(palaceFilter.get(one.getEnd()));
		path_line.setText(one.getLine());

		LinearLayout mapFather = (LinearLayout) two
				.findViewById(R.id.map_father);

		initMap(mapFather.getId());

		LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		par.gravity = Gravity.CENTER;

		allView.removeAllViews();
		allView.addView(two, par);

		final TranslateAnimation animation = new TranslateAnimation(0, 150, 0,
				0);
		animation.setDuration(new Constants().circleTime);// 设置动画持续时间
		animation.setRepeatCount(Animation.INFINITE);// 不断重复
		bus_icon.setAnimation(animation);
		pb.setMax(100); // 设置最大完成度为100
		changeProgress(pb);
	}

	private void setUsernameInTicket(View view, TicketBean ticket) {
		TextView nameText = (TextView) view.findViewById(R.id.name);
		nameText.setText("周亮俊");

		TextView usernameText = (TextView) view.findViewById(R.id.username);
		usernameText.setText(ticket.getUsername());
	}

	private void initMap(int ViewId) {
		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(ViewId, mMapFragment, MAP_FRAGMENT_TAG);
		fragmentTransaction.commit();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						addMarkerInMap();
					}
				}, 200);
			}
		}).start();

	}

	private void addMarkerInMap() {
		if (aMap == null) {
			aMap = mMapFragment.getMap();
			aMap.addMarker(new MarkerOptions()
					.position(new LatLng(39.990770, 116.472220))
					.title("Marker").snippet("起点：嘉定校区")
					.icon(BitmapDescriptorFactory.defaultMarker()));

		}
	}

	/**
	 * 退票
	 * 
	 * @param ticket
	 */
	private void cancelTicket(final TicketBean ticket) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String in = new AllUrl().cancelTicket(ip, port,
						ticket.getId() + "", ticket.getBus_id());
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						if ("1".equals(in)) { // 退票成功
							onBackPressed();
							Toast.makeText(NextTicket.this, "退票成功!", 1).show();
						} else if ("0".equals(in)) { // 退票失败
							Toast.makeText(NextTicket.this, "退票失败,请刷新之后重试", 1)
									.show();
						} else { // 网络连接异常
							Toast.makeText(NextTicket.this, "网络出错,请检查网络", 1)
									.show();
						}
					}
				});

			}
		}).start();
	}

	private void changeProgress(final ProgressBar pb) {

		Constants.timer = new Timer();
		constants = new Constants();
		Constants.timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pb.setProgress(constants.i);
						(constants.i)++;
						if (constants.i == 100)
							constants.i = 0;
					}
				});
			}
		}, new Date(), constants.circleTime / 100);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (Constants.timer != null)
			Constants.timer.cancel();

		if (mMapFragment != null) {
			mMapFragment.getFragmentManager().popBackStack();
			mMapFragment.onDestroy();
			mMapFragment = null;
		}
		if (aMap != null) {
			aMap = null;
		}

		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		clickNext(ticketlist.get(1));
		super.onResume();
	}

}

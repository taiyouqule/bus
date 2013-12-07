package tongji.edu.activity;

import java.util.ArrayList;
import java.util.Date;

import tongji.edu.bean.BusBean;
import tongji.edu.db.DBAdapter;
import tongji.edu.url.AllUrl;
import tongji.edu.util.Constants;
import tongji.edu.util.MyDialogFactory;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 抢座界面
 * @author Administrator
 *
 */
public class ShowBusActivity extends Activity {

	private TextView EdTitle;
	private ListView listview;
	private String routeName; // 线路名称，如四平到嘉定
	private String ip, port;
	private Handler mHandler = new Handler();
	private Dialog mDialog;
	private DBAdapter dbAdepter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_bus);

		dbAdepter = new DBAdapter(ShowBusActivity.this);
		dbAdepter.open();
		EdTitle = (TextView) findViewById(R.id.route_title);
		listview = (ListView) findViewById(R.id.buslist);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		ip = prefs.getString(this.getString(R.string.ksetserver), Constants.ip);
		port = prefs.getString(this.getString(R.string.ksetport),
				Constants.port);

		Intent intent = getIntent();
		String weekend = intent.getStringExtra("weekend");
		String line = intent.getStringExtra("line");
		routeName = line;
		ArrayList<BusBean> buslist = (ArrayList<BusBean>) intent
				.getSerializableExtra("buslist");
		if (weekend.equals("1")) {
			EdTitle.setText(line + "(双休日)");
		} else {
			EdTitle.setText(line + "(工作日)");
		}
		listview.setAdapter(new BusAdapter(ShowBusActivity.this, buslist));

	}

	private void getTicket(final String username, final String get_ticket_time,
			final String bus_id, final String bustime, final String line) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String in = new AllUrl().getTicket(ip, port, username,
						get_ticket_time, bus_id);
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						dismissDialog();
						if (in.equals("")) {
							Toast.makeText(ShowBusActivity.this, "网络连接出错，抢座失败",
									1).show();
						} else if (in.contains("1#")) {
							Toast.makeText(ShowBusActivity.this,
									"恭喜您抢坐成功!请进入个人中心查看预定信息", 1).show();

//							OneRecordBean record = new OneRecordBean();
//							record.setGet_ticket_time(DateUtil.longToStr(get_ticket_time));
//							record.setBus_time(bustime);
//							record.setRoute_name(routeName);
//							record.setLine(line);
//							record.setUsername(username);
//							dbAdepter.insert(record); // 数据库中增加一条
						} else if (in.contains("2#")) {
							Toast.makeText(ShowBusActivity.this, "回程票抢票成功", 1)
									.show();

//							OneRecordBean record = new OneRecordBean();
//							record.setGet_ticket_time(DateUtil.longToStr(get_ticket_time));
//							record.setBus_time(bustime);
//							record.setRoute_name(routeName);
//							record.setLine(line);
//							record.setUsername(username);
//							dbAdepter.insert(record); // 数据库中增加一条
						} else if (in.equals("0#0")) {
							Toast.makeText(ShowBusActivity.this, "没有空位了", 1)
									.show();
						} else if (in.equals("0#1")) {

							Toast.makeText(ShowBusActivity.this,
									"您已经拥有本条线路上的订票，无需再抢票", 1).show();
						} else if (in.equals("0#2")) {

							Toast.makeText(ShowBusActivity.this,
									"抢票机会使用完毕，但仍可以可以抢回程票", 1).show();
						} else if (in.equals("0#3")) {
							Toast.makeText(ShowBusActivity.this,
									"所有抢票机会使用完毕，回程票已经抢好", 1).show();
						}
					}
				});
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dbAdepter.close();
		super.onDestroy();
	}

	public class BusAdapter extends BaseAdapter {
		private ArrayList<BusBean> buslist = null;
		private LayoutInflater mInflater;

		public BusAdapter(Context context, ArrayList<BusBean> buslist) {
			this.buslist = buslist;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return buslist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.bus_item, null);

			}

			final BusBean one = buslist.get(position);
			TextView time = ((TextView) convertView.findViewById(R.id.go_time));
			TextView line = (TextView) convertView.findViewById(R.id.go_line);
			ImageView icon = ((ImageView) convertView
					.findViewById(R.id.bus_icon));
			time.setText(one.getTime());
			line.setText(one.getLine());
			if (one.getRest() > 0) {
				icon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.n_bus));
			} else {
				icon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.y_bus));
			}
			Button btn = ((Button) convertView.findViewById(R.id.get_ticket));
			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showRequestDialog("抢座中,请稍等");

					String username = Constants.username;
					String get_ticket_time = new Date().getTime()+"";
					String bus_id = one.getBus_id();
					String bustime = one.getTime();
					String line = one.getLine();

					getTicket(username, get_ticket_time, bus_id, bustime, line);
				}
			});

			return convertView;
		}

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

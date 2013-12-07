package tongji.edu.activity;

import java.util.ArrayList;

import tongji.edu.bean.BusBean;
import tongji.edu.url.AllUrl;
import tongji.edu.util.Constants;
import tongji.edu.util.MyDialogFactory;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 管理员设置剩余座位数界面
 * 
 * @author Administrator
 * 
 */
public class SetRestActivity extends Activity {

	private TextView EdTitle;
	private ListView listview;
	private String routeName; // 线路名称，如四平到嘉定
	private String ip, port;
	private Handler mHandler = new Handler();
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_bus);

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
		listview.setAdapter(new BusAdapter(SetRestActivity.this, buslist));

	}

	private void setRest(final int rest, final String bus_id) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String in = new AllUrl().setRest(ip, port, rest, bus_id);
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						dismissDialog();
						if (in.equals("")) {
							Toast.makeText(SetRestActivity.this,
									"网络连接出错，座位设置失败", 1).show();
						} else if (in.equals("0")) {
							Toast.makeText(SetRestActivity.this, "设定座位失败,请重试",
									1).show();

						} else if (in.contains("1")) {
							Toast.makeText(SetRestActivity.this, "设定座位成功", 1)
									.show();
						} else {
							Toast.makeText(SetRestActivity.this, "出现未知错误,请重试",
									1).show();
						}
					}
				});
			}
		}).start();
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
				convertView = mInflater.inflate(R.layout.bus_set_item, null);

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
			Button btn = ((Button) convertView.findViewById(R.id.set_ticket));
			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					final EditText b = new EditText(SetRestActivity.this);
					AlertDialog a = new AlertDialog.Builder(
							SetRestActivity.this)
							.setTitle("请输入座位数")
							.setView(b)
							.setNegativeButton("取消", null)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											String temp = b.getText()
													.toString();
											try {
												int rest = Integer
														.parseInt(temp);
												showRequestDialog("正在设定座位,请稍等");
												if (rest >= 0 && rest <= 100) {
													setRest(rest,
															one.getBus_id());
												} else {
													mHandler.post(new Runnable() {

														@Override
														public void run() {
															// TODO
															// Auto-generated
															// method stub
															dismissDialog();
															Toast.makeText(
																	SetRestActivity.this,
																	"请输入0至100中间的整数",
																	1).show();
															b.setText("");
														}
													});
												}

											} catch (Exception e) {
												// TODO: handle exception
												mHandler.post(new Runnable() {

													@Override
													public void run() {
														// TODO Auto-generated
														// method stub
														dismissDialog();
														Toast.makeText(
																SetRestActivity.this,
																"请输入0至100中间的整数",
																1).show();
														b.setText("");
													}
												});

											}

										}
									}).create();
					a.show();

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

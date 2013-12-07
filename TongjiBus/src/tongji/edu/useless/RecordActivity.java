//package tongji.edu.useless;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import tongji.edu.activity.R;
//import tongji.edu.activity.R.id;
//import tongji.edu.activity.R.layout;
//import tongji.edu.bean.OneRecordBean;
//import tongji.edu.db.DBAdapter;
//import tongji.edu.util.Constants;
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//
///**
// * 校车凭证,暂时没用
// * 
// * @author Administrator
// * 
// */
//public class RecordActivity extends Activity {
//
//	private Button back;
//	private ListView listview;
//	private DBAdapter dbAdepter;
//	private OneRecordBean[] data = null;
//	private SimpleAdapter adapter;
//	private List<Map<String, Object>> listDate;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.record_bus);
//
//		back = (Button) findViewById(R.id.top_back_btn);
//		listview = (ListView) findViewById(R.id.record_list);
//		back.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				onBackPressed();
//			}
//		});
//
//		dbAdepter = new DBAdapter(RecordActivity.this);
//		dbAdepter.open();
//		data = dbAdepter.queryByUsername(Constants.username);
//
//		listDate = getData();
//
//		adapter = new SimpleAdapter(RecordActivity.this, listDate,
//				R.layout.record_item,
//				new String[] { "time", "fromTo", "line" }, new int[] {
//						R.id.start_time, R.id.path, R.id.go_line, });
//
//		listview.setAdapter(adapter);
//
//	}
//
//	// ArrayList<ShowRecordBean> getTrueRecord(OneRecordBean[] all){
//	// ArrayList<ShowRecordBean> trueData=new ArrayList<ShowRecordBean>();
//	// for(OneRecordBean temp:all){
//	// ShowRecordBean one=new ShowRecordBean();
//	//
//	// SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	// sd.parse(one.getBus_time());
//	// }
//	//
//	// return trueData;
//	// }
//	// public class RecordAdapter extends BaseAdapter {
//	// private OneRecordBean[] records = null;
//	// private LayoutInflater mInflater;
//	//
//	// public RecordAdapter(Context context, OneRecordBean[] records) {
//	// this.records = records;
//	// mInflater = LayoutInflater.from(context);
//	// }
//	//
//	// @Override
//	// public int getCount() {
//	// // TODO Auto-generated method stub
//	// return records.length;
//	// }
//	//
//	// @Override
//	// public Object getItem(int position) {
//	// // TODO Auto-generated method stub
//	// return position;
//	// }
//	//
//	// @Override
//	// public long getItemId(int position) {
//	// // TODO Auto-generated method stub
//	// return position;
//	// }
//	//
//	// @Override
//	// public View getView(final int position, View convertView,
//	// ViewGroup parent) {
//	// // TODO Auto-generated method stub
//	// if (convertView == null) {
//	// convertView = mInflater.inflate(R.layout.record_item, null);
//	//
//	// }
//	//
//	// final OneRecordBean one = records[position];
//	// TextView time = ((TextView) convertView.findViewById(R.id.start_time));
//	// TextView path = (TextView) convertView.findViewById(R.id.path);
//	// TextView line = (TextView) convertView.findViewById(R.id.go_line);
//	//
//	//
//	// time.setText();
//	// line.setText(one.getLine());
//	// if (one.getRest() > 0) {
//	// icon.setBackgroundDrawable(getResources().getDrawable(
//	// R.drawable.n_bus));
//	// } else {
//	// icon.setBackgroundDrawable(getResources().getDrawable(
//	// R.drawable.y_bus));
//	// }
//	// Button btn = ((Button) convertView.findViewById(R.id.get_ticket));
//	// btn.setOnClickListener(new View.OnClickListener() {
//	//
//	// @Override
//	// public void onClick(View v) {
//	// // TODO Auto-generated method stub
//	// showRequestDialog("抢座中,请稍等");
//	//
//	// String username = Constants.username;
//	// String get_ticket_time = new SimpleDateFormat(
//	// "yyyy-MM-dd HH:mm:ss").format(new Date());
//	// String bus_id = one.getBus_id();
//	// String bustime = one.getTime();
//	// String line = one.getLine();
//	//
//	// getTicket(username, get_ticket_time, bus_id, bustime, line);
//	// }
//	// });
//	//
//	// return convertView;
//	// }
//	//
//	// }
//
//	/**
//	 * 初始化listview内的数据
//	 * 
//	 * @return
//	 */
//	private List<Map<String, Object>> getData() {
//
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		if (data != null) {
//
//			for (OneRecordBean oneRecord : data) {
//
//				map = new HashMap<String, Object>();
//
//				SimpleDateFormat sd = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm:ss");
//				Date da = null;
//				try {
//					Calendar cal = Calendar.getInstance();
//
//					da = sd.parse(oneRecord.getGet_ticket_time());
//
//					cal.setTime(da);
//					cal.add(Calendar.DAY_OF_MONTH, 1);
//					da = cal.getTime();
//
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				da.setHours(0);
//				da.setMinutes(0);
//				da.setSeconds(0);
//				String tempTime = sd.format(da).substring(0, 10);
//				map.put("id", oneRecord.getId());
//				map.put("time", tempTime + "  " + oneRecord.getBus_time());
//
//				map.put("fromTo", oneRecord.getRoute_name());
//
//				map.put("line", oneRecord.getLine());
//
//				list.add(map);
//			}
//		}
//		return list;
//
//	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		dbAdepter.close();
//		super.onDestroy();
//	}
//
//}

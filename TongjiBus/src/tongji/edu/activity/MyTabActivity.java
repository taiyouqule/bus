package tongji.edu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tongji.edu.bean.BusBean;
import tongji.edu.bean.RouteBean;
import tongji.edu.bean.TicketBean;
import tongji.edu.url.AllUrl;
import tongji.edu.util.Constants;
import tongji.edu.util.MyDialogFactory;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyTabActivity extends FragmentActivity {

	private int getID;
	private LinearLayout allView;
	private LayoutInflater inflater;
	private View one, two, three, four;
	private ArrayList<RouteBean> routelist;
	private String ip, port;
	private Dialog mDialog;
	private Handler mHandler = new Handler();

	private static final String MAP_FRAGMENT_TAG = "map";
	private AMap aMap;
	private SupportMapFragment mMapFragment;
	private HashMap<String, String> palaceFilter = new HashMap<String, String>();
	private Constants constants;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_main);
		allView = (LinearLayout) findViewById(R.id.rest);

		inflater = LayoutInflater.from(getApplicationContext());
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

	private SimpleAdapter getAdapter(ArrayList<RouteBean> routelist) {
		// SimpleAdapter adapter = new SimpleAdapter(this, getData(routelist),
		// R.layout.route_item,
		//
		// new String[] { "start", "end" }, new int[] { R.id.start_name,
		// R.id.end_name });

		SimpleAdapter adapter = new SimpleAdapter(this, getData(routelist),
				android.R.layout.simple_list_item_1,

				new String[] { "line" }, new int[] { android.R.id.text1 });
		return adapter;
	}

	private List<Map<String, Object>> getData(ArrayList<RouteBean> routelist) {

		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map;

		for (int i = 0; i < routelist.size(); i++) {
			RouteBean route = routelist.get(i);
			map = new HashMap<String, Object>();

			map.put("route_id", route.getRoute_id());
			// map.put("start", route.getStart());
			// map.put("end", route.getEnd());
			map.put("weekend", route.getWeekend());
			map.put("line", route.getStart() + "  至  " + route.getEnd());

			list.add(map);
		}

		return list;

	}

	private SimpleAdapter getHistoryAdapter(ArrayList<TicketBean> oldticketlist) {

		SimpleAdapter adapter = new SimpleAdapter(this,
				getHistoryData(oldticketlist), R.layout.record_item,

				new String[] { "time", "direction", "line" }, new int[] {
						R.id.start_time, R.id.path, R.id.go_line });
		return adapter;
	}

	private List<Map<String, Object>> getHistoryData(
			ArrayList<TicketBean> oldticketlist) {

		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map;

		for (int i = 0; i < oldticketlist.size(); i++) {
			TicketBean one = oldticketlist.get(i);
			map = new HashMap<String, Object>();

			map.put("time", one.getTicket_time());
			map.put("line", one.getLine());
			map.put("direction", one.getStart() + "  至  " + one.getEnd());

			list.add(map);
		}

		return list;

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 接收MainActivity传来的信息
		getID = getIntent().getIntExtra("tabhost", 0);
		routelist = (ArrayList<RouteBean>) getIntent().getSerializableExtra(
				"routelist");
		if (getID == 0) {
			one = inflater.inflate(R.layout.tab1, null);
			allView.removeAllViews();
			Button back = (Button) one.findViewById(R.id.back_btn);
			back.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			final ListView listview = (ListView) one.findViewById(R.id.list);
			listview.setAdapter(getAdapter(routelist));

			AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					// TODO Auto-generated method stub
					Object obj = listview.getAdapter().getItem(arg2);
					HashMap<String, Object> map = (HashMap<String, Object>) obj;
					String route_id = map.get("route_id") + "";
					String weekend = map.get("weekend") + "";
					String line = map.get("line") + "";
					getBus(ip, port, route_id, weekend, line);

				}
			};
			listview.setOnItemClickListener(listViewListener);
			allView.addView(one);

		} else if (getID == 1) {
			getNewTicket(); // 查看车票
		} else if (getID == 2) {
			getOldTicket(); // 查看乘车历史
		} else if (getID == 3) {
			getNewTicketForCancel(); // 退票
		}
	}

	private void getBus(final String ip, final String port,
			final String route_id, final String weekend, final String line) {
		showRequestDialog("正在获取班车信息");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				String in = new AllUrl().getBus(ip, port, route_id);
				final ArrayList<BusBean> buslist = (ArrayList<BusBean>) new Gson()
						.fromJson(in, new TypeToken<List<BusBean>>() {
						}.getType());
				if (routelist != null && routelist.size() > 0) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							Intent intent = null;
							if (Constants.isAdmin) {
								intent = new Intent(MyTabActivity.this,
										SetRestActivity.class);
							} else {
								intent = new Intent(MyTabActivity.this,
										ShowBusActivity.class);
							}

							intent.putExtra("weekend", weekend);
							intent.putExtra("buslist", buslist);
							intent.putExtra("line", line);
							startActivity(intent);
							dismissDialog();
						}
					});
				} else {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(MyTabActivity.this,
									"未能获得班车信息,请重新获取", 1).show();
						}
					});
				}

			}
		}).start();
	}

	/**
	 * 查看乘车历史
	 */
	private void getOldTicket() {
		showRequestDialog("正在获取乘车历史");
		new Thread(new Runnable() {
			public void run() {

				String tempTime = new Date().getTime() + "";
				String in = new AllUrl().getOldOrNewTicket(ip, port,
						Constants.username, tempTime, "1");
				final ArrayList<TicketBean> ticketlist = (ArrayList<TicketBean>) new Gson()
						.fromJson(in, new TypeToken<List<TicketBean>>() {
						}.getType());
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						dismissDialog();
						if (ticketlist.size() == 0) {

							System.out.println("没有乘车记录");

							three = inflater.inflate(R.layout.tab_no_history,
									null);
							Button back = (Button) three
									.findViewById(R.id.back_btn);
							back.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									onBackPressed();
								}
							});

							LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.FILL_PARENT,
									LinearLayout.LayoutParams.FILL_PARENT);
							par.gravity = Gravity.CENTER;

							allView.removeAllViews();
							allView.addView(three, par);

						} else if (ticketlist.size() > 0) {

							System.out.println("乘车记录共" + ticketlist.size()
									+ "条");

							three = inflater.inflate(R.layout.tab3, null);
							Button back = (Button) three
									.findViewById(R.id.top_back_btn);
							back.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									onBackPressed();
								}
							});

							ListView list = (ListView) three
									.findViewById(R.id.record_list);
							list.setAdapter(getHistoryAdapter(ticketlist));

							LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.FILL_PARENT,
									LinearLayout.LayoutParams.FILL_PARENT);
							par.gravity = Gravity.CENTER;

							allView.removeAllViews();
							allView.addView(three, par);

						}
					}
				});

			}
		}).start();
	}

	/**
	 * 获得可使用车票
	 */
	private void getNewTicket() {
		showRequestDialog("正在获取车票信息");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				String tempTime = new Date().getTime() + "";
				String in = new AllUrl().getOldOrNewTicket(ip, port,
						Constants.username, tempTime, "0");
				final ArrayList<TicketBean> ticketlist = (ArrayList<TicketBean>) new Gson()
						.fromJson(in, new TypeToken<List<TicketBean>>() {
						}.getType());
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						dismissDialog();
						if (ticketlist != null && ticketlist.size() > 0) {
							if (ticketlist.size() == 2) { // 显示“下一张”按钮
								System.out.println("共两张车票");

								two = inflater.inflate(R.layout.tab2, null);

								TextView title = (TextView) two
										.findViewById(R.id.textView2);
								title.setText("车票(1/2)");
								Button next = (Button) two
										.findViewById(R.id.next_btn);
								setUsernameInTicket(two, ticketlist);
								next.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(
												MyTabActivity.this,
												NextTicket.class);
										intent.putExtra("ticketlist",
												ticketlist);
										intent.putExtra("isCancel", false);
										startActivity(intent);
									}
								});

								Button back = (Button) two
										.findViewById(R.id.back_btn);
								back.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										onBackPressed();
									}
								});

								Button cancel = (Button) two
										.findViewById(R.id.cancel);
								cancel.setVisibility(View.GONE);

								ImageView bus_icon = (ImageView) two
										.findViewById(R.id.bus_icon);
								ProgressBar pb = (ProgressBar) two
										.findViewById(R.id.progress);

								TextView gotime = (TextView) two
										.findViewById(R.id.when_to_go);
								TextView start_place = (TextView) two
										.findViewById(R.id.start_place);
								TextView end_place = (TextView) two
										.findViewById(R.id.end_place);
								TextView path_line = (TextView) two
										.findViewById(R.id.path_line);
								TicketBean one = ticketlist.get(0);
								gotime.setText(one.getTicket_time());
								start_place.setText(palaceFilter.get(one
										.getStart()));
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

								final TranslateAnimation animation = new TranslateAnimation(
										0, 150, 0, 0);
								animation
										.setDuration(new Constants().circleTime);// 设置动画持续时间
								animation.setRepeatCount(Animation.INFINITE);// 不断重复
								bus_icon.setAnimation(animation);
								pb.setMax(100); // 设置最大完成度为100
								changeProgress(pb);
							} else if (ticketlist.size() == 1) { // 显示当前页但不显示"下一页"按钮
								System.out.println("共1张车票");

								two = inflater.inflate(R.layout.tab2, null);
								Button next = (Button) two
										.findViewById(R.id.next_btn);
								next.setVisibility(View.GONE);

								Button back = (Button) two
										.findViewById(R.id.back_btn);
								
								setUsernameInTicket(two, ticketlist);
								back.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										onBackPressed();
									}
								});
								Button cancel = (Button) two
										.findViewById(R.id.cancel);
								cancel.setVisibility(View.GONE);

								ImageView bus_icon = (ImageView) two
										.findViewById(R.id.bus_icon);
								ProgressBar pb = (ProgressBar) two
										.findViewById(R.id.progress);

								TextView gotime = (TextView) two
										.findViewById(R.id.when_to_go);
								TextView start_place = (TextView) two
										.findViewById(R.id.start_place);
								TextView end_place = (TextView) two
										.findViewById(R.id.end_place);
								TextView path_line = (TextView) two
										.findViewById(R.id.path_line);
								TicketBean one = ticketlist.get(0);
								gotime.setText(one.getTicket_time());
								start_place.setText(palaceFilter.get(one
										.getStart()));
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

								final TranslateAnimation animation = new TranslateAnimation(
										0, 150, 0, 0);
								animation
										.setDuration(new Constants().circleTime);// 设置动画持续时间
								animation.setRepeatCount(Animation.INFINITE);// 不断重复
								bus_icon.setAnimation(animation);
								pb.setMax(100); // 设置最大完成度为100
								changeProgress(pb);

							}
						} else if (ticketlist.size() == 0) { // 显示暂时没有车票页面
							System.out.println("没有车票");

							two = inflater
									.inflate(R.layout.tab_no_ticket, null);

							LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.FILL_PARENT,
									LinearLayout.LayoutParams.FILL_PARENT);
							par.gravity = Gravity.CENTER;

							allView.removeAllViews();
							allView.addView(two, par);

						}
					}
				});

			}
		}).start();

	}

	/**
	 * 设定姓名和学号
	 * @param view
	 * @param ticketlist
	 */
	private void setUsernameInTicket(View view,ArrayList<TicketBean> ticketlist){
		TextView nameText=(TextView)view.findViewById(R.id.name);
		nameText.setText("周亮俊");
		
		TextView usernameText=(TextView)view.findViewById(R.id.username);
		usernameText.setText(ticketlist.get(0).getUsername());
	}
	/**
	 * 退票操作
	 */
	private void getNewTicketForCancel() {

		showRequestDialog("正在获取车票信息");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				String tempTime = new Date().getTime() + "";
				String in = new AllUrl().getOldOrNewTicket(ip, port,
						Constants.username, tempTime, "0");
				final ArrayList<TicketBean> ticketlist = (ArrayList<TicketBean>) new Gson()
						.fromJson(in, new TypeToken<List<TicketBean>>() {
						}.getType());
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						dismissDialog();
						if (ticketlist != null && ticketlist.size() > 0) {
							if (ticketlist.size() == 2) { // 显示“下一张”按钮
								System.out.println("共两张车票");

								four = inflater.inflate(R.layout.tab4, null);

								TextView title = (TextView) four
										.findViewById(R.id.textView2);
								title.setText("车票(1/2)");
								
								setUsernameInTicket(four, ticketlist);
								Button next = (Button) four
										.findViewById(R.id.next_btn);
								next.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(
												MyTabActivity.this,
												NextTicket.class);
										intent.putExtra("ticketlist",
												ticketlist);
										intent.putExtra("isCancel", true);
										startActivity(intent);
									}
								});

								Button back = (Button) four
										.findViewById(R.id.back_btn);
								back.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										onBackPressed();
									}
								});

								Button cancel = (Button) four
										.findViewById(R.id.cancel);
								cancel.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										AlertDialog a = new AlertDialog.Builder(
												MyTabActivity.this)
												.setMessage("确认要退掉这张票吗?")
												.setNegativeButton("取消", null)
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// TODO
																// Auto-generated
																// method stub
																cancelTicket(ticketlist
																		.get(0));
																onPause();
																onResume();
															}
														}).create();
										a.show();

									}
								});

								ImageView bus_icon = (ImageView) four
										.findViewById(R.id.bus_icon);
								ProgressBar pb = (ProgressBar) four
										.findViewById(R.id.progress);

								TextView gotime = (TextView) four
										.findViewById(R.id.when_to_go);
								TextView start_place = (TextView) four
										.findViewById(R.id.start_place);
								TextView end_place = (TextView) four
										.findViewById(R.id.end_place);
								TextView path_line = (TextView) four
										.findViewById(R.id.path_line);
								TicketBean one = ticketlist.get(0);
								gotime.setText(one.getTicket_time());
								start_place.setText(palaceFilter.get(one
										.getStart()));
								end_place.setText(palaceFilter.get(one.getEnd()));
								path_line.setText(one.getLine());

								LinearLayout mapFather = (LinearLayout) four
										.findViewById(R.id.map_father_cancel);

								initMap(mapFather.getId());

								LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.FILL_PARENT,
										LinearLayout.LayoutParams.FILL_PARENT);
								par.gravity = Gravity.CENTER;

								allView.removeAllViews();
								allView.addView(four, par);

								final TranslateAnimation animation = new TranslateAnimation(
										0, 150, 0, 0);
								animation
										.setDuration(new Constants().circleTime);// 设置动画持续时间
								animation.setRepeatCount(Animation.INFINITE);// 不断重复
								bus_icon.setAnimation(animation);
								pb.setMax(100); // 设置最大完成度为100
								changeProgress(pb);
							} else if (ticketlist.size() == 1) { // 显示当前页但不显示"下一页"按钮
								System.out.println("共1张车票");

								four = inflater.inflate(R.layout.tab4, null);
								Button next = (Button) four
										.findViewById(R.id.next_btn);
								next.setVisibility(View.GONE);
								
								setUsernameInTicket(four, ticketlist);
								Button back = (Button) four
										.findViewById(R.id.back_btn);
								back.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										onBackPressed();
									}
								});
								Button cancel = (Button) four
										.findViewById(R.id.cancel);
								cancel.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {

										// TODO Auto-generated method stub
										AlertDialog a = new AlertDialog.Builder(
												MyTabActivity.this)
												.setMessage("确认要退掉这张票吗?")
												.setNegativeButton("取消", null)
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// TODO
																// Auto-generated
																// method stub
																cancelTicket(ticketlist
																		.get(0));
															}
														}).create();
										a.show();

									

									}
								});

								ImageView bus_icon = (ImageView) four
										.findViewById(R.id.bus_icon);
								ProgressBar pb = (ProgressBar) four
										.findViewById(R.id.progress);

								TextView gotime = (TextView) four
										.findViewById(R.id.when_to_go);
								TextView start_place = (TextView) four
										.findViewById(R.id.start_place);
								TextView end_place = (TextView) four
										.findViewById(R.id.end_place);
								TextView path_line = (TextView) four
										.findViewById(R.id.path_line);
								TicketBean one = ticketlist.get(0);
								gotime.setText(one.getTicket_time());
								start_place.setText(palaceFilter.get(one
										.getStart()));
								end_place.setText(palaceFilter.get(one.getEnd()));
								path_line.setText(one.getLine());

								LinearLayout mapFather = (LinearLayout) four
										.findViewById(R.id.map_father_cancel);

								initMap(mapFather.getId());

								LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.FILL_PARENT,
										LinearLayout.LayoutParams.FILL_PARENT);
								par.gravity = Gravity.CENTER;

								allView.removeAllViews();
								allView.addView(four, par);

								final TranslateAnimation animation = new TranslateAnimation(
										0, 150, 0, 0);
								animation
										.setDuration(new Constants().circleTime);// 设置动画持续时间
								animation.setRepeatCount(Animation.INFINITE);// 不断重复
								bus_icon.setAnimation(animation);
								pb.setMax(100); // 设置最大完成度为100
								changeProgress(pb);

							}
						} else if (ticketlist.size() == 0) { // 显示暂时没有车票页面
							showNoTicket();

						}
					}
				});

			}
		}).start();

	}

	private void showNoTicket() {
		System.out.println("没有车票");

		two = inflater.inflate(R.layout.tab_no_ticket, null);

		LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		par.gravity = Gravity.CENTER;

		allView.removeAllViews();
		allView.addView(two, par);
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
							showNoTicket();
							Toast.makeText(MyTabActivity.this, "退票成功!", 1)
									.show();
						} else if ("0".equals(in)) { // 退票失败
							Toast.makeText(MyTabActivity.this, "退票失败,请刷新之后重试",
									1).show();
						} else { // 网络连接异常
							Toast.makeText(MyTabActivity.this, "网络出错,请检查网络", 1)
									.show();
						}
					}
				});

			}
		}).start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (Constants.timer != null)
			Constants.timer.cancel();

		if (mMapFragment != null) {
			mMapFragment.onDestroy();
			mMapFragment = null;
		}
		if (aMap != null) {
			aMap = null;
		}

		super.onPause();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(MyTabActivity.this)
					.setTitle("温馨提示")
					.setMessage("您确定要退出吗?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									MyTabActivity.this.finish();
								}
							}).setNegativeButton("取消", null).create().show();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

/*
 * 
 */
package tongji.edu.util;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 * @author zlj
 *
 */
public class NetWork {

	/**
	 * 判断服务器是否开启
	 * @param urlString	服务器的地址
	 * @return	返回服务器是否开启
	 */
	public static boolean serverAvailable(String ip,String port) {
		String urlString = "http://" + ip + ":" + port + "/shareLocation";
		try {
			URL url = new URL(urlString);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setConnectTimeout(3000);
			httpURLConnection.connect();

			int httpResponse = httpURLConnection.getResponseCode();
			if (httpResponse == HttpURLConnection.HTTP_OK)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断手机网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

}

package tongji.edu.url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AllUrl {
	private static String getGBKCodeRequest(String in) {
		try {
			return java.net.URLEncoder.encode(in, "gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return in;
		}
	}

	/**
	 * 注册时调用
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param phone
	 * @return
	 */
	public String registe(String ip, String port, String username,
			String password, String phone) {

		BufferedReader br = null;
		StringBuffer urlmessage = new StringBuffer();

		URL url;
		try {
			url = new URL("http://" + ip + ":" + port
					+ "/TJbus/RegisterServlet?username=" + username
					+ "&password=" + password + "&phone=" + phone);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(4000);
			conn.setRequestMethod("GET");

			InputStream pp = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(pp));

			String b = new String();
			while ((b = br.readLine()) != null)
				urlmessage.append(b);

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlmessage.toString();

	}

	/**
	 * 登录
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param weekend
	 * @return
	 */
	public String login(String ip, String port, String username,
			String password, String weekend) {

		BufferedReader br = null;
		StringBuffer urlmessage = new StringBuffer();

		URL url;
		try {
			url = new URL("http://" + ip + ":" + port
					+ "/TJbus/LoginServlet?username=" + username + "&password="
					+ password + "&weekend=" + weekend);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(8000);
			conn.setRequestMethod("GET");

			InputStream pp = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(pp));

			String b = new String();
			while ((b = br.readLine()) != null)
				urlmessage.append(b);

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlmessage.toString();

	}

	/**
	 * 获取指定线路上的班车
	 * 
	 * @param ip
	 * @param port
	 * @param route_id
	 * @return
	 */
	public String getBus(String ip, String port, String route_id) {

		BufferedReader br = null;
		StringBuffer urlmessage = new StringBuffer();

		URL url;
		try {
			url = new URL("http://" + ip + ":" + port
					+ "/TJbus/GetBusServlet?route_id=" + route_id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(4000);
			conn.setRequestMethod("GET");

			InputStream pp = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(pp));

			String b = new String();
			while ((b = br.readLine()) != null)
				urlmessage.append(b);

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlmessage.toString();

	}

	/**
	 * 抢座
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param get_ticket_time
	 * @param bus_id
	 * @return
	 */
	public String getTicket(String ip, String port, String username,
			String get_ticket_time, String bus_id) {

		BufferedReader br = null;
		StringBuffer urlmessage = new StringBuffer();

		URL url;
		try {
			url = new URL("http://" + ip + ":" + port
					+ "/TJbus/getTicketServlet?username=" + username
					+ "&get_ticket_time=" + getGBKCodeRequest(get_ticket_time)
					+ "&bus_id=" + bus_id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(4000);
			conn.setRequestMethod("GET");

			InputStream pp = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(pp));

			String b = new String();
			while ((b = br.readLine()) != null)
				urlmessage.append(b);

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlmessage.toString();

	}

	/**
	 * 当history=0表示是在这个时间之后的车票 当history=1表示是在这个时间之前的车票
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param curtime
	 * @param history
	 * @return
	 */
	public String getOldOrNewTicket(String ip, String port, String username,
			String curtime, String history) {

		BufferedReader br = null;
		StringBuffer urlmessage = new StringBuffer();

		URL url;
		try {
			url = new URL("http://" + ip + ":" + port
					+ "/TJbus/TicketServlet?username=" + username + "&curtime="
					+ curtime + "&history=" + history);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(4000);
			conn.setRequestMethod("GET");

			InputStream pp = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(pp));

			String b = new String();
			while ((b = br.readLine()) != null)
				urlmessage.append(b);

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlmessage.toString();

	}

	/**
	 * 退票
	 * @param ip
	 * @param port
	 * @param ticket_id
	 * @param bus_id
	 * @return
	 */
	public String cancelTicket(String ip, String port, String ticket_id,
			String bus_id) {

		BufferedReader br = null;
		StringBuffer urlmessage = new StringBuffer();

		URL url;
		try {
			url = new URL("http://" + ip + ":" + port
					+ "/TJbus/CancelTicketServlet?ticket_id=" + ticket_id
					+ "&bus_id=" + bus_id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(4000);
			conn.setRequestMethod("GET");

			InputStream pp = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(pp));

			String b = new String();
			while ((b = br.readLine()) != null)
				urlmessage.append(b);

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlmessage.toString();

	}
	
	
	/**
	 * 通过用户名找回密码，如果返回1表示成功找回
	 * @param ip
	 * @param port
	 * @param username
	 * @return
	 */
	public String findPwd(String ip, String port,String username) {

		BufferedReader br = null;
		StringBuffer urlmessage = new StringBuffer();

		URL url;
		try {
			url = new URL("http://" + ip + ":" + port
					+ "/TJbus/FindpwdServlet?username=" + username
					);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(4000);
			conn.setRequestMethod("GET");

			InputStream pp = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(pp));

			String b = new String();
			while ((b = br.readLine()) != null)
				urlmessage.append(b);

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlmessage.toString();

	}
	
	
	/**
	 * 设定座位数，返回1表示成功
	 * @param ip
	 * @param port
	 * @param rest
	 * @param bus_id
	 * @return
	 */
	public String setRest(String ip, String port,int rest,String bus_id) {

		BufferedReader br = null;
		StringBuffer urlmessage = new StringBuffer();

		URL url;
		try {
			url = new URL("http://" + ip + ":" + port
					+ "/TJbus/SetRestServlet?rest=" + rest+"&bus_id="+bus_id
					);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(4000);
			conn.setRequestMethod("GET");

			InputStream pp = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(pp));

			String b = new String();
			while ((b = br.readLine()) != null)
				urlmessage.append(b);

			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlmessage.toString();

	}
}

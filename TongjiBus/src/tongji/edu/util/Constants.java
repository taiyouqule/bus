package tongji.edu.util;

import java.util.ArrayList;
import java.util.Timer;

import tongji.edu.bean.TicketBean;

public class Constants {
	public static boolean logout_flag=false;  //是否注销
	
	public static final String ip="115.200.53.148";
	public static final String port="8080";
	
	public static long sleeptime=600;
	public static String username="";   //临时保存用户名
	
	
	public  int  i=0;   //记录进度条
	public  long circleTime=4000;   //车票动画的周期
	
	public static Timer timer ;  //定时器，刷新车票界面的进度条
	
	public static boolean isAdmin=false;    //记录该用户是否为管理员
	public ArrayList<TicketBean> newTicket=new ArrayList<TicketBean>();   //保存历史
	
	
	public ArrayList<TicketBean> oldTicket=new ArrayList<TicketBean>();
	
}

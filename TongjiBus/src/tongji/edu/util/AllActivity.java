package tongji.edu.util;

import java.util.LinkedList;

import android.app.Activity;
/**
 * 管理所有的Activity  可以实现一键退出
 * @author zlj
 *
 */
public class AllActivity {
	 private static AllActivity allActivity=new AllActivity();
	 
	 private static LinkedList<Activity> list;
	 
	private AllActivity(){
		this.list=new LinkedList<Activity>();
	}
	public static synchronized AllActivity getInstance(){
		return allActivity;
	}
	public static synchronized LinkedList<Activity> getList(){
		return allActivity.list;
	}
	public static synchronized void addActivity(Activity a){
		list.add(a);
	}
	public static synchronized void deleteActivity(Activity a){
		if(list.contains(a))
			list.remove(a);
	}
	public static synchronized void clear(){
		list.clear();
	}
	public static synchronized void closeAll(){
		
		for(Activity a:list){
			a.finish();
		}
		System.exit(0);
	}

}

package tongji.edu.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 登录之后的返回界面
 * @author Administrator
 *
 */
public class LoginResponeBean implements Parcelable {
	private int isAdmin;
	private ArrayList<RouteBean> routelist;

	@Override
	public String toString() {
		return "LoginResponeBean [isAdmin=" + isAdmin + ", routelist="
				+ routelist + "]";
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public ArrayList<RouteBean> getRoutelist() {
		return routelist;
	}

	public void setRoutelist(ArrayList<RouteBean> routelist) {
		this.routelist = routelist;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(isAdmin);
		dest.writeList(routelist);
	}

	public static final Parcelable.Creator<LoginResponeBean> CREATOR = new Parcelable.Creator<LoginResponeBean>() {

		@Override
		public LoginResponeBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			LoginResponeBean one = new LoginResponeBean();
			one.isAdmin = source.readInt();
			one.routelist = new ArrayList<RouteBean>();
			source.readList(one.routelist, ClassLoader.getSystemClassLoader());

			return one;
		}

		@Override
		public LoginResponeBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new LoginResponeBean[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public LoginResponeBean() {

	}
}

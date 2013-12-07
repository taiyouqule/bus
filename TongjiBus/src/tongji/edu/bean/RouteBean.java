package tongji.edu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 一条路线的信息
 * 
 * @author Administrator
 * 
 */
public class RouteBean implements Parcelable {
	/**
	 * 
	 */
	private int route_id;
	private String start;
	private String end;
	private String weekend;

	public int getRoute_id() {
		return route_id;
	}

	public void setRoute_id(int route_id) {
		this.route_id = route_id;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	@Override
	public String toString() {
		return "RouteBean [route_id=" + route_id + ", start=" + start
				+ ", end=" + end + ", weekend=" + weekend + "]";
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getWeekend() {
		return weekend;
	}

	public void setWeekend(String weekend) {
		this.weekend = weekend;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(route_id);
		dest.writeString(start);
		dest.writeString(end);
		dest.writeString(weekend);
	}

	public static final Parcelable.Creator<RouteBean> CREATOR = new Parcelable.Creator<RouteBean>() {

		@Override
		public RouteBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			RouteBean one = new RouteBean();
			one.route_id = source.readInt();
			one.start = source.readString();
			one.end = source.readString();
			one.weekend = source.readString();

			return one;
		}

		@Override
		public RouteBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new RouteBean[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public RouteBean() {

	}
}

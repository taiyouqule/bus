package tongji.edu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 一辆班车的信息
 * 
 * @author Administrator
 * 
 */
public class BusBean implements Parcelable {
	private String bus_id;
	private String time;
	private String line;
	private int rest;

	@Override
	public String toString() {
		return "BusBean [bus_id=" + bus_id + ", time=" + time + ", line="
				+ line + ", rest=" + rest + "]";
	}

	public String getBus_id() {
		return bus_id;
	}

	public void setBus_id(String bus_id) {
		this.bus_id = bus_id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getRest() {
		return rest;
	}

	public void setRest(int rest) {
		this.rest = rest;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeString(bus_id);
		dest.writeString(time);
		dest.writeString(line);
		dest.writeInt(rest);
	}

	public static final Parcelable.Creator<BusBean> CREATOR = new Parcelable.Creator<BusBean>() {

		@Override
		public BusBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			BusBean one = new BusBean();

			one.bus_id = source.readString();
			one.time = source.readString();
			one.line = source.readString();
			one.rest = source.readInt();

			return one;
		}

		@Override
		public BusBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BusBean[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public BusBean() {

	}
}

package tongji.edu.useless;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 对应于数据库中的一条数据,暂时没用
 * @author Administrator
 *
 */
public class OneRecordBean implements Parcelable {
	private int id;
	private String get_ticket_time;
	private String bus_time;
	private String route_name;
	private String line;
	private String username;

	@Override
	public String toString() {
		return "OneRecordBean [id=" + id + ", get_ticket_time="
				+ get_ticket_time + ", bus_time=" + bus_time + ", route_name="
				+ route_name + ", line=" + line + ", username=" + username
				+ "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGet_ticket_time() {
		return get_ticket_time;
	}

	public void setGet_ticket_time(String get_ticket_time) {
		this.get_ticket_time = get_ticket_time;
	}

	public String getBus_time() {
		return bus_time;
	}

	public void setBus_time(String bus_time) {
		this.bus_time = bus_time;
	}

	public String getRoute_name() {
		return route_name;
	}

	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(get_ticket_time);
		dest.writeString(bus_time);
		dest.writeString(route_name);
		dest.writeString(line);
		dest.writeString(username);
	}

	public static final Parcelable.Creator<OneRecordBean> CREATOR = new Parcelable.Creator<OneRecordBean>() {

		@Override
		public OneRecordBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			OneRecordBean one = new OneRecordBean();
			one.id = source.readInt();
			one.get_ticket_time = source.readString();
			one.bus_time = source.readString();
			one.route_name = source.readString();
			one.line = source.readString();
			one.username = source.readString();

			return one;
		}

		@Override
		public OneRecordBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new OneRecordBean[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public OneRecordBean() {

	}

}

package tongji.edu.bean;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 对应于一张车票
 * @author Administrator
 *
 */
public class TicketBean implements Parcelable {
	private int id;
	private int route_id;
	private String bus_id;
	private String username;
	private String get_ticket_time;
	private String ticket_time;
	private String start;
	private String end;
	private String line;

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGet_ticket_time() {
		return get_ticket_time;
	}

	public void setGet_ticket_time(String get_ticket_time) {
		this.get_ticket_time = get_ticket_time;
	}

	public int getRoute_id() {
		return route_id;
	}

	public void setRoute_id(int route_id) {
		this.route_id = route_id;
	}

	public String getBus_id() {
		return bus_id;
	}

	public void setBus_id(String bus_id) {
		this.bus_id = bus_id;
	}

	public String getTicket_time() {
		return ticket_time;
	}

	public void setTicket_time(String ticket_time) {
		this.ticket_time = ticket_time;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "TicketBean [id=" + id + ", route_id=" + route_id + ", bus_id="
				+ bus_id + ", username=" + username + ", get_ticket_time="
				+ get_ticket_time + ", ticket_time=" + ticket_time + ", start="
				+ start + ", end=" + end + ", line=" + line + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeInt(id);
		dest.writeInt(route_id);
		dest.writeString(bus_id);
		dest.writeString(username);
		dest.writeString(get_ticket_time);
		dest.writeString(ticket_time);
		dest.writeString(start);
		dest.writeString(end);
		dest.writeString(line);
	}

	public static final Parcelable.Creator<TicketBean> CREATOR = new Parcelable.Creator<TicketBean>() {

		@Override
		public TicketBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TicketBean one = new TicketBean();

			one.id = source.readInt();
			one.route_id = source.readInt();
			one.bus_id = source.readString();
			one.username = source.readString();
			one.get_ticket_time = source.readString();
			one.ticket_time = source.readString();
			one.start = source.readString();
			one.end = source.readString();
			one.line = source.readString();

			return one;
		}

		@Override
		public TicketBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TicketBean[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public TicketBean() {

	}
}

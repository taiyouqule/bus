package tongji.edu.useless;
/**
 * 对应于一条校车凭证
 * @author Administrator
 *
 */
public class ShowRecordBean {
	private String time;
	private String fromTo;
	private String line;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFromTo() {
		return fromTo;
	}
	public void setFromTo(String fromTo) {
		this.fromTo = fromTo;
	}
	public String getLine() {
		return line;
	}
	@Override
	public String toString() {
		return "ShowRecordBean [time=" + time + ", fromTo=" + fromTo
				+ ", line=" + line + "]";
	}
	public void setLine(String line) {
		this.line = line;
	}
}

package invisaPC.Rest.device;

public class TX_Single_Properties extends Properties {

	public int video_quality = 0;
	public int video_source_optimisation = 0;
	public int hid = 0;
	public int head_1_edid = 0;
	public int mouse_keyboard_timeout = 0;

	public int getMouse_keyboard_timeout() { 
		return mouse_keyboard_timeout;
	}
	public int getVideo_quality() {
		return video_quality;
	}
	public void setVideo_quality(int video_quality) {
		this.video_quality = video_quality;
	}
	public int getVideo_source_optimisation() {
		return video_source_optimisation;
	}
	public void setVideo_source_optimisation(int video_source_optimisation) {
		this.video_source_optimisation = video_source_optimisation;
	}
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getHead_1_edid() {
		return head_1_edid;
	}
	public void setHead_1_edid(int head_1_edid) {
		this.head_1_edid = head_1_edid;
	}
	
}

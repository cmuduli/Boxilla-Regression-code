package invisaPC.Rest.device;

public class TX_Properties extends Properties{
	
	public int video_quality = 0;
	public int hid = 0;
	public int mouse_keyboard_timeout = 0;
	public int head_1_edid = 0;
	public int head_2_edid = 0;
	
	public int getVideo_quality() {
		return video_quality;
	}
	public void setVideo_quality(int video_quality) {
		this.video_quality = video_quality;
	}
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public int getMouse_keyboard_timeout() {
		return mouse_keyboard_timeout;
	}
	public void setMouse_keyboard_timeout(int mouse_keyboard_timeout) {
		this.mouse_keyboard_timeout = mouse_keyboard_timeout;
	}
	public int getHead_1_edid() {
		return head_1_edid;
	}
	public void setHead_1_edid(int head_1_edid) {
		this.head_1_edid = head_1_edid;
	}
	public int getHead_2_edid() {
		return head_2_edid;
	}
	public void setHead_2_edid(int head_2_edid) {
		this.head_2_edid = head_2_edid;
	}

}

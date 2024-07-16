package invisaPC.Rest.device;
/**
 * This class models the parameters for an RX device
 * @author Boxilla
 *
 */
public class RX_Properties extends Properties {
	
	public int power_mode = 0;
	public int http_enable = 0;
	
	public int getPower_mode() {
		return power_mode;
	}
	public void setPower_mode(int power_mode) {
		this.power_mode = power_mode;
	}
	public int getHttp_enable() {
		return http_enable;
	}
	public void setHttp_enable(int http_enable) {
		this.http_enable = http_enable;
	}
	
	

}

package TableModel;

import java.util.Arrays;

public class DeviceGroups {

	private boolean exists;
	private String name;
	private String numberOfDevices;
	private String[] deviceList;
	private String status;
	private String zone;
	
	public DeviceGroups(boolean exists, String name, String numberOfDevices, String[] deviceList,
			String status, String zone) {
		this.exists = exists;
		this.name = name;
		this.numberOfDevices = numberOfDevices;
		this.deviceList = deviceList;
		this.status = status;
		this.zone = zone;
	}
	
	
	public boolean equals(DeviceGroups o) {
		DeviceGroups devGroup = (DeviceGroups) o;
		if(devGroup.exists == o.exists && devGroup.name.equals(o.name) && 
				devGroup.numberOfDevices.equals(o.numberOfDevices) && devGroup.status.equals(o.status) &&
				devGroup.zone.equals(o.zone)) {
		return 	Arrays.equals(devGroup.deviceList, o.deviceList);
		}
		return false;
	}

	
}

package extra;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Creates a pool of appliances that test cases can pick and choose from.
 * 
 * The properties file device.properties will be read and any device specified 
 * in this file will be added to the pool. This is then used to manage and unmanage
 * devices.
 * 
 * @author Brendan O'Regan
 *
 */

public class AppliancePool {

	private ArrayList<Device> allDevices = new ArrayList<Device>();
	private ArrayList<Device> txSinglePool = new ArrayList<Device>();
	private ArrayList<Device> txSingleExtraPool = new ArrayList<Device>();
	private ArrayList<Device> txDualPool = new ArrayList<Device>();
	private ArrayList<Device> rxSinglePool = new ArrayList<Device>();
	private ArrayList<Device> rxDualPool = new ArrayList<Device>();
	private ArrayList<Device> rxEmeraldPool = new ArrayList<Device>();
	private ArrayList<Device> txEmeraldPool = new ArrayList<Device>();
	private Properties deviceProperties = new Properties();


	public ArrayList<Device> getTxEmeraldPool() {
		return txEmeraldPool;
	}
	
	public ArrayList<Device> getRxEmeraldPool() {
		return rxEmeraldPool;
	}
	/**
	 * Gets all single head transmitter devices in the pool
	 * @return ArrayList containing all single TX devices in the pool
	 */
	public ArrayList<Device> getTxSinglePool() {
		return txSinglePool;
	}
	public ArrayList<Device> getTxSingleExtraPool() {
		return txSinglePool;
	}
	
	/**
	 * 
	 * @param txSinglePool
	 */
	public void setTxSinglePool(ArrayList<Device> txSinglePool) {
		this.txSinglePool = txSinglePool;
	}
	public void setTxSingleExtraPool(ArrayList<Device> txSinglePool) {
		this.txSinglePool = txSinglePool;
	}
	
	
	/**
	 * Gets all dual head transmitter devices in the pool
	 * @return
	 */
	public ArrayList<Device> getTxDualPool() {
		return txDualPool;
	}
	/**
	 * 
	 * @param txDualPool
	 */
	public void setTxDualPool(ArrayList<Device> txDualPool) {
		this.txDualPool = txDualPool;
	}
	/**
	 * Gets all single head receiver devices in the pool
	 * @return
	 */
	public ArrayList<Device> getRxSinglePool() {
		return rxSinglePool;
	}
	
	/**
	 * 
	 * @param rxSinglePool
	 */
	public void setRxSinglePool(ArrayList<Device> rxSinglePool) {
		this.rxSinglePool = rxSinglePool;
	}
	/**
	 * Gets all dual head receiver devices in the pool
	 * @return
	 */
	public ArrayList<Device> getRxDualPool() {
		return rxDualPool;
	}
	/**
	 * 
	 * @param rxDualPool
	 */
	public void setRxDualPool(ArrayList<Device> rxDualPool) {
		this.rxDualPool = rxDualPool;
	}

	/**
	 * Will load the device property file into memory for use in test cases
	 */
	public void loadProperties() {
		try {
			InputStream in = new FileInputStream("device.properties");
			deviceProperties.load(in);
			in.close();
			System.out.println("Device properties loaded! ");
		} catch (IOException e) {
			System.out.println("Properties file failed to load");
		}
	}

	/**
	 * Sets device properties.
	 * Loops through all the device[x] properties in the 
	 * device.properties file. Up to a max of 20(change value 
	 * in the loop for more)
	 */
	public void setDevices() {
		//invisaPC
		for(int j=0; j < 20; j++) {
			String details = deviceProperties.getProperty("device" + j);
			if(details == null) {
				break;
			}
			String[] parsedDetails = details.split(",");
			Device device = new Device(parsedDetails[0], parsedDetails[1], parsedDetails[2], parsedDetails[3], false, parsedDetails[6]);
			device.setReceiver(parsedDetails[4]);
			device.setDual(parsedDetails[5]);
			allDevices.add(device);
		}


		//emerald
		for(int k= 0; k < 20; k++) {
			String eDetails = deviceProperties.getProperty("edevice" + k);
			if(eDetails == null) {
				break;
			}
			String[] parsedDetails = eDetails.split(",");
			Device device = new Device(parsedDetails[0], parsedDetails[1], parsedDetails[2], parsedDetails[3], true, parsedDetails[6]);
			device.setReceiver(parsedDetails[4]);
			device.setDual(parsedDetails[5]);
			allDevices.add(device);
			System.out.println(device.toString());
		}
	}


	//split the devices into their proper lists
	/**
	 * Utility method used to split the devices into their correct list
	 */
	public void caterogizeDevices() {
		for(Device a : allDevices) {
			System.out.println(a.toString());
		}

		for(Device d : allDevices) {
			//single head tx
			if(d.isReceiver() ==false && d.isDual() == false && !d.getEmerald()) {
				txSinglePool.add(d);
				//dual head tx
			}else if(d.isReceiver() ==false && d.isDual() == true  && !d.getEmerald()) {
				txDualPool.add(d);
				//single head rx
			}else if(d.isReceiver() == true && d.isDual() == false  && !d.getEmerald()) {
				rxSinglePool.add(d);
			}
			//dual head rx
			else if(d.isReceiver() == true && d.isDual() == true  && !d.getEmerald()) {
				rxDualPool.add(d);
			}
			//emerald
			else if(d.isReceiver() == true && d.getEmerald() == true ) {
				rxEmeraldPool.add(d);
			}else if(d.isReceiver() == false && d.getEmerald() == true) {
				txEmeraldPool.add(d);
			}
		}
	}
	public Device getTxEmerald() {
		Device d = getTxEmeraldPool().get(0);
		getTxEmeraldPool().remove(0);
		return d;
	}
	public Device getRxEmerald() {
		System.out.println(getRxEmeraldPool().size());
		Device d = getRxEmeraldPool().get(0);
		getRxEmeraldPool().remove(0);
		return d;
	}

	/**
	 * Returns the first free single head transmitter from the pool.
	 * Also removes this device from the pool
	 * @return Device object representing single head tx
	 */
	public Device getTxSingle() {
		Device d = getTxSinglePool().get(0);
		getTxSinglePool().remove(0);
		return d;
	}
	
	 
		public Device getTxSingleExtra() {
			Device d = getTxSinglePool().get(0);
			getTxSinglePool().remove(0);
			return d;
		}
	/**
	 * Returns the first free dual head transmitter from the pool.
	 * Also removes this device from the pool
	 * @return Device object representing dual head tx
	 */
	public Device getTxDual() {
		Device d = getTxDualPool().get(0);
		getTxDualPool().remove(0);
		return d;
	}
	/**
	 * Returns the first free single head receiver from the pool.
	 * Also removes this device from the pool
	 * @return Device object representing single head rx
	 */
	public Device getRxSingle() {
		Device d = getRxSinglePool().get(0);
		getRxSinglePool().remove(0);
		return d;
	}
	/**
	 * Returns the first free dual head receiver from the pool.
	 * Also removes this device from the pool
	 * @return Device object representing dual head rx
	 */
	public Device getRxDual() {
		Device d = getRxDualPool().get(0);
		getRxDualPool().remove(0);
		return d;
	}

	/**
	 * Sets up devices.
	 * Loads properties file, sets the device objects and sorts the devices
	 */
	public void getAllDevices() {
		loadProperties();
		setDevices();
		caterogizeDevices();
	}
}

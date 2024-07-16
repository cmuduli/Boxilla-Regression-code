package database.objects;

public abstract class Kvm_Upgrades {
	
	private String id = "";
	private String lsm_brand = "";
	private String lsm_class = "";
	private String lsm_type = "";
	private String lsm_version = "";
	private String lsm_compatibility = "";
	private String lsm_builddate = "";
	private String created_at = "";
	private String updated_at = "";
	private String filename = "";
	private String active_image = "";
	
	
	@Override
	public int hashCode() {
		int result = 231;
		
		result = 44 * result + this.id.hashCode();
		result = 44 * result + this.lsm_brand.hashCode();
		result = 44 * result + this.lsm_class.hashCode();
		result = 44 * result + this.lsm_type.hashCode();
		result = 44 * result + this.created_at.hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		Kvm_Upgrades upgrade = (Kvm_Upgrades) obj;
		
		if(this.id.equals(upgrade.getId()) && this.lsm_brand.equals(upgrade.getLsm_brand()) && 
				this.lsm_class.equals(upgrade.getLsm_class()) && this.lsm_type.equals(upgrade.getLsm_type()) &&
				this.lsm_version.equals(upgrade.getLsm_type()) && this.lsm_compatibility.equals(upgrade.getLsm_compatibility())
				&& this.lsm_builddate.equals(upgrade.getLsm_builddate()) && this.created_at.equals(upgrade.getCreated_at()) &&
				this.updated_at.equals(upgrade.getUpdated_at()) && this.filename.equals(upgrade.getFilename()) && this.active_image.equals(upgrade.getActive_image())) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String sep = System.lineSeparator();
		return "id:" + this.id + sep + "lsm_brand:" + this.lsm_brand + sep + "lsm_class:" + this.lsm_class +
				sep + "lsm_type:" + this.lsm_type + sep + "lsm_version:" + this.lsm_version + sep + 
				"lsm_compatibility:" + this.lsm_compatibility + sep + "lsm_builddate:" + this.lsm_builddate + 
				sep + "created at:" + this.created_at + sep + "updated_at:" + this.updated_at + sep + 
				"filename" + this.filename + sep  + "active_image:" + this.active_image;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		if(id == null) {
			this.id = "";
		}else {
			this.id = id;
		}
	}
	
	public String getLsm_brand() {
		return lsm_brand;
	}
	
	public void setLsm_brand(String lsm_brand) {
		if(lsm_brand == null) {
			this.lsm_brand = "";
		}else {
			this.lsm_brand = lsm_brand;
		}
	}
	
	public String getLsm_class() {
		return lsm_class;
	}
	
	public void setLsm_class(String lsm_class) {
		if(lsm_class == null) {
			this.lsm_class = "";
		}else {
			this.lsm_class = lsm_class;
		}
	}
	
	
	public String getLsm_type() {
		return lsm_type;
	}
	
	public void setLsm_type(String lsm_type) {
		if(lsm_type == null) {
			this.lsm_type = "";
		}else {
			this.lsm_type = lsm_type;
		}
	}
	
	public String getLsm_version() {
		return lsm_version;
	}
	
	public void setLsm_version(String lsm_version) {
		if(lsm_version == null) {
			this.lsm_version = "";
		}else {
			this.lsm_version = lsm_version;
		}
	}
	
	public String getLsm_compatibility() {
		return lsm_compatibility;
	}
	
	public void setLsm_compatibility(String lsm_compatibility) {
		if(lsm_compatibility == null) {
			this.lsm_compatibility = "";
		}else {
			this.lsm_compatibility = lsm_compatibility;
		}
	}
	
	public String getLsm_builddate() {
		return lsm_builddate;
	}
	
	public void setLsm_builddate(String lsm_builddate) {
		if(lsm_builddate == null) {
			this.lsm_builddate = "";
		}else {
			this.lsm_builddate = lsm_builddate;
		}
	}
	
	public String getCreated_at() {
		return created_at;
	}
	
	public void setCreated_at(String created_at) {
		if(created_at == null) {
			this.created_at = "";
		}else {
			this.created_at = created_at;
		}
		
	}
	
	public String getUpdated_at() {
		return updated_at;
	}
	
	public void setUpdated_at(String updated_at) {
		if(updated_at == null) {
			this.updated_at = "";
		}else {
			this.updated_at = updated_at;
		}
	}
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		if(filename == null) {
			this.filename = "";
		}else {
			this.filename = filename;
		}
	}
	
	public String getActive_image() {
		return active_image;
	}
	public void setActive_image(String active_image) {
		if(active_image == null) {
			this.active_image = "";
		}else {
			this.active_image = active_image;
		}
	}

}

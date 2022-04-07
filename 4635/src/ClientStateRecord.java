import java.sql.Timestamp;

public class ClientStateRecord {
	private String clientName;
	private Boolean isActive;
	private Timestamp registeredSince;
	
	ClientStateRecord(String name) {
		clientName = name;
		isActive = true;
		registeredSince = new Timestamp(System.currentTimeMillis());		
	}
	
	public synchronized String getClientName() {
		return clientName;
	}
	public synchronized void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public synchronized Boolean getIsActive() {
		return isActive;
	}
	public synchronized void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}

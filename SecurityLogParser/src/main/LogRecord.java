package main;

public class LogRecord {
	private long timestamp;
	private String logLevel;
	private String ipAddress;
	private String message;
	
	public LogRecord(long timestamp, String logLevel, String ipAddress, String message) {
		this.setTimestamp(timestamp);
		this.setLogLevel(logLevel);
		this.setIpAddress(ipAddress);
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return String.format("[%d] [%s] [IP: %s] - %s", timestamp, logLevel, ipAddress, message);
	}
}

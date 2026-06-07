package main;

import java.util.List;

public class ThreatDetector {
	
	public static int binarySearchTimestamp(List<LogRecord> records, long targetTimestamp) {
		int low = 0;
		int high = records.size() - 1;
		
		while (low <= high) {
			int mid = low + (high - low) / 2;
			long midTimestamp = records.get(mid).getTimestamp();
			
			if (midTimestamp == targetTimestamp) {
				return mid;
			} else if (midTimestamp < targetTimestamp) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		
		return -1;
	}
	
	public static void scanForBruteForce(List<LogRecord> sortedRecords) {
		System.out.println("\n--- Scanning for behavioral Threats ---");
		boolean threatDetected = false;
		
		for (int i = 0; i < sortedRecords.size(); i++) {
			LogRecord current = sortedRecords.get(i);
			
			if (!current.getLogLevel().equals("WARN")) {
				continue; // looking for WARNING flags
			}
			
			int failedCount = 1;
			String targetIp = current.getIpAddress();
			
			for (int j = i + 1; j < sortedRecords.size(); j++) {
				LogRecord next = sortedRecords.get(j);
				
				if (next.getTimestamp() - current.getTimestamp() > 5) {
					break; // if the gap between the logs exceed 5 seconds, break the scan
				}
				
				if (next.getIpAddress().equals(targetIp) && next.getLogLevel().equals("WARN")) {
					failedCount++;
				}
			}
			
			if (failedCount >= 2) {
				System.out.printf("[ALERT] Dynamic Brute Force Warning! IP Address [%s] generated %d failed login attempts within 5 seconds.%n",
						targetIp, failedCount);
				threatDetected = true;
				i += (failedCount - 1);
			}
		}
		
		if (!threatDetected) {
			System.out.println("[INFO] Dynamic behavioral scanning complete. No threats identified.");
		}
	}
}

package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class LogGenerator {
	
	public static void generateMockLogFile(String fileName, int totalRecords) {
		Random random = new Random();
		long startTimestamp = 1717758000L; // start epoch time
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			
			for (int i = 0; i < totalRecords; i++) {
				long timestamp = startTimestamp + random.nextInt(totalRecords * 2);
				String ip = "192.168.1." + random.nextInt(254);
				
				// select log randomly
				int typeChance = random.nextInt(100);
				String logLine;
				
				if (typeChance < 5) {
					logLine = String.format("[%d] [ERROR] [IP: %s - Structural overflow warning detected!", timestamp, ip);
				} else if (typeChance < 15) {
					String hackerIp = "10.0.0.99";
					logLine = String.format("[%d] [WARN] [IP: %s] - Login failure: bad credentials.", timestamp, hackerIp);
				} else if (typeChance < 40) {
					logLine = String.format("[%d] [WARN] [IP: %s] - Slow disk read response time.", timestamp, ip);
				} else {
					logLine = String.format("[%d] [INFO] [IP: %s] - Resource processed successfully.", timestamp, ip);
				}
				
				writer.write(logLine);
				writer.newLine();
			}
			
			System.out.println("Generated " + totalRecords +" mock server logs in: " + fileName);
		} catch (IOException e) {
			System.err.println();
		}
	}
}

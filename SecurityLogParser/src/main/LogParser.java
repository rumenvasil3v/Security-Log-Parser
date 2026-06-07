package main;

public class LogParser {
	
	public static LogRecord parse(String rawLog) {
		if (rawLog == null || rawLog.isEmpty()) {
			return null;
		}
		
		CustomStack stack = new CustomStack();
		
		for (int i = 0; i < rawLog.length(); i++) {
			char character = rawLog.charAt(i);
			
			if (character == '[') {
				stack.push(character);
			} else if (character == ']') {
				if (stack.isEmpty()) {
					return null;
				}
				
				stack.pop();
			}
		}
		
		if (!stack.isEmpty()) {
			return null; // that means the stack contains opening brackets, therefore the log has unclosed brackets
		}
		
		try {
			String[] parts = rawLog.split("\\]\\s*\\[");
			
			long timestamp = Long.parseLong(parts[0].replace("[", "").trim());
			String logLevel = parts[1].trim();
			
			String remaining = parts[2];
			String ipAddress = remaining.substring(4, remaining.indexOf("]")).trim();
			String message = remaining.substring(remaining.indexOf("-") + 1).trim();
			
			return new LogRecord(timestamp, logLevel, ipAddress, message);
		} catch (Exception e) {
			return null;
		}
	}
}

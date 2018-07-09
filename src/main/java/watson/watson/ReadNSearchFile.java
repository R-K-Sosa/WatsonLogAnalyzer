package watson.watson;

import java.io.File;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class ReadNSearchFile {
	
	static String filepath = "newestLogData.csv";
	static String searchTerms = "stacktrace: java.lang.Exception: <H1>SRVE0255E: A WebGroup/Virtual Host to handle /FilenetPassThruApp/api/filenet has not been defined.</H1><BR><H3>SRVE0255E: A WebGroup/Virtual Host to handle ibmb2bp0.portsmouth.uk.ibm.com:9449 has not been defined.</H3><BR>";
	
	private static Scanner x;
	public static void main(String[] args) {
		String filepath = "newestLogData.csv";
		//String searchTerms = "stacktrace: java.lang.Exception: <H1>SRVE0255E: A WebGroup/Virtual Host to handle /FilenetPassThruApp/api/filenet has not been defined.</H1><BR><H3>SRVE0255E: A WebGroup/Virtual Host to handle ibmb2bp0.portsmouth.uk.ibm.com:9449 has not been defined.</H3><BR>";
		
		//below can be written as a try catch if needed
		//readRecord(searchTerms, filepath);
		
	}
	
	public static void readRecord(String searchTerms, String filepath) throws Exception {
		
		boolean found = false;
		String dateAndTime = ""; String logEntry = "";
		
		//try {
			x = new Scanner(new File(ReadNSearchFile.filepath));
			x.useDelimiter("[,\n]");
			
			while (x.hasNext() && !found) {
				dateAndTime = x.next();
				logEntry = x.next();
				
				if (logEntry.equals(ReadNSearchFile.searchTerms)) {
					found = true;
				}
			}
			
			if (found) {
				JOptionPane.showMessageDialog(null, "Date and Time: " + dateAndTime + "Log Entry: " + logEntry);
			}
			
			else {
				JOptionPane.showMessageDialog(null, "Record Not Found");
			}
		//}
		
		//catch(Exception e) {
		//	JOptionPane.showMessageDialog(null, "Error");
		//}
		
	}
	
}

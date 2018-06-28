package watson.watson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import javax.swing.text.Document;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.omg.CORBA.portable.InputStream;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.GetCollectionOptions;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import java.nio.file.*;
public class LogAnalyzer {

	public static void main(String args[]) throws Exception
	{
		String IMID = "DB5JSKDLZ";
		//authentication token for bot
		String botToken = "xoxb-377960480037-380083483923-4BFuqRwyFhISCBPHLwcreQEH";
		//the string to fetch IM history with WatsonChatBot
		String imHistory = "https://slack.com/api/im.history?token=" + botToken + "&channel=" + IMID +"&count=4&pretty=1";
		
		URL imHistoryURL = new URL(imHistory);
        URLConnection yc = imHistoryURL.openConnection();
        BufferedReader in1 = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String imResponse = "";
        String inputLine;
        while ((inputLine = in1.readLine()) != null) 
            imResponse = imResponse + inputLine;
        in1.close();
        
        
        int a = 15 + (imResponse.indexOf("url_private"));
		int b = imResponse.indexOf('"', a);
		System.out.println(imResponse.substring(a, b));
		
		String url = imResponse.substring(a, b);
		url = url.replace("\\", "");
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// optional default is GET
		con.setRequestMethod("GET");
		
		//add request header
		con.setRequestProperty("Authorization", "Bearer " + botToken);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		if(responseCode == 200) {
			System.out.println("Website Reached");
		}
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String logLine;
		StringBuffer response = new StringBuffer();

		while ((logLine = in.readLine()) != null) {
			response.append(logLine + " \n ");
			//System.out.println(logLine);
		}
		
		
		
		
		in.close();
		int numberOfLogEntries = 0;
		String bracket = "[";
		for( int i = 0; i < response.length( ); i++ )
		{
			if( (response.charAt(i)) == bracket.charAt(0) ) {
				numberOfLogEntries++;
			}
			
		}
		System.out.println("There are " + numberOfLogEntries + " log entries.");
		
		int numberOfLines = 0;
		String newLine = " \n ";
		for( int i = 0; i < response.length( ); i++ ) {
		
			if( response.indexOf(newLine, i) != -1){
				numberOfLines++;
				i = (response.indexOf(newLine, i));
				
			}
		}
		
		System.out.println("There are " + numberOfLines + " lines.");
		LinkedList<String> dateAndTimes = new LinkedList<String>();
		LinkedList<String> correspondingLogEntries = new LinkedList<String>();
		
		
		String datePattern = "\\d{1,2}/\\d{1,2}/\\d{2}\\s+\\d{1,2}:\\d{2}:\\d{2}:\\d{3}";
		//String logEntryPattern = "(?<=(" + datePattern + "))" + "(.*)" + "(?=(" + datePattern + "))";
		Pattern r = Pattern.compile(datePattern);
		Matcher m = r.matcher(response);
		int howManyDates = 0;
		while(m.find()) {
			System.out.println("Found value: " + m.group());
			String dateAndTime = m.group();
			dateAndTimes.add(dateAndTime);
			howManyDates++;
		}
		/*Pattern p = Pattern.compile(logEntryPattern);
		Matcher n = p.matcher(response);
		int howManyEntries = 0;
		while(n.find()) {
			System.out.println("Found value: " + n.group());
			String correspondingLogEntry = n.group();
			correspondingLogEntries.add(correspondingLogEntry);
			howManyEntries++;
		}
		*/
		System.out.println(howManyDates + " Dates Found");
		//System.out.println(howManyEntries + " Entries Found");
		/*
		for(int i = 0; i <response.length();i++) {
			if(response.charAt(i) == bracket.charAt(0) && (response.substring(i, (response.indexOf("]", i) + 1))).length() > 23 && (response.substring(i, (response.indexOf("]", i) + 1))).length() < 27) {
				String dateAndTime = response.substring(i, (response.indexOf("]", i) + 1));
				dateAndTimes.add(dateAndTime);
				
				
				try{
				String correspondingLogEntry = response.substring((response.indexOf("]", i) + 1), (response.indexOf("[", i + 1)) - 1);
				correspondingLogEntries.add(correspondingLogEntry);
				}
				catch(Exception e){
					String correspondingLogEntry = response.substring((response.indexOf("]", i) + 1), response.length());
					correspondingLogEntries.add(correspondingLogEntry);
				}
				
				//the commented line below prints entire log
				//System.out.println(dateAndTime + "    " + correspondingLogEntry); 
			}
		}
		*/
		
		
		PrintWriter pw = null;
		try {
		    pw = new PrintWriter(new File("newerLogData.csv"));
		} 
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		
		StringBuilder builder = new StringBuilder();
		String ColumnNamesList = "Date And Time, Log Entry";
		// No need give the headers Like: id, Name on builder.append
		
		builder.append(ColumnNamesList +"\n");
		
		
		for(int i = 0; i < dateAndTimes.size(); i++) {
			builder.append('"' + dateAndTimes.get(i) + '"' + ",");
			builder.append('"' + correspondingLogEntries.get(i) + '"' + " \n");
		}
		pw.write(builder.toString());
		pw.close();
		
	}
	
}

//NOTE:The URLs may no longer be valid by the time you view this course.
//If you get errors due to some of thesepages being unavailable,
//feel free to substitute them with other valid URLs.

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

public class PageDownloader implements Runnable {
	
	String[] urlsList;
	
	public PageDownloader(String[] urlsList) {
		this.urlsList = urlsList;
	}
	
	@Override
	public void run() {
		
		try {
		
			for(String urlString : urlsList) {
				
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException(Thread.currentThread().getName() + 
													" interrupted");
				}			
				
				URL url = new URL(urlString);
				String filename = urlString.substring(urlString.lastIndexOf("/") + 1).trim();
				BufferedReader reader =  new BufferedReader(new InputStreamReader(url.openStream()));
		        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		        
		        String line;
		        while ((line = reader.readLine()) != null) {
		            writer.write(line);
		         }            
		        System.out.println("Page downloaded to " + filename);
		        
		        writer.close();
		        // Thread.sleep(1000);		        

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public static void main(String args[]) 
    { 
		
		String[] urls = new String[]{"https://iweb.dl.sourceforge.net/project/reactos/ReactOS/0.4.14/ReactOS-0.4.14-iso.zip",
				"https://downloads-global.3cx.com/downloads/debian10iso/debian-amd64-netinst-3cx.iso"};


		Thread downloaderOne = new Thread(new PageDownloader(Arrays.copyOfRange(urls, 0, 1)));
		Thread downloaderTwo = new Thread(new PageDownloader(Arrays.copyOfRange(urls, 1, urls.length)));

		try{
        	
        	long startTime = System.currentTimeMillis();
            downloaderOne.start();
            downloaderTwo.start();
            
        	Thread.sleep(10000);
        	downloaderOne.interrupt();
        	downloaderTwo.join();
        	long endTime = System.currentTimeMillis();
        	
        	System.out.println("Total time taken: " + (endTime-startTime)/1000 + "s");
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        
    } 

}
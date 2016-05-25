package si.matjazcerkvenik.dtools.io;

import java.io.File;
import java.io.FileFilter;

import si.matjazcerkvenik.dtools.context.DToolsContext;

public class CleanThread extends Thread {
	
	@Override
	public void run() {
		
		while (true) {
			
			DToolsContext.getInstance().getLogger().info("CleanThread: started");
			
			File dir = new File(DToolsContext.HOME_DIR + "/temp");
			File[] files = dir.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File f) {
					String filename = f.getName().substring(0, f.getName().length() - 4);
					try {
						long name = Long.parseLong(filename);
						long now = System.currentTimeMillis();
						
						if (name < (now - (1 * 3600 * 1000))) {
							// older than 1 hour
							return true;
						}
						
					} catch (NumberFormatException e) {
						// so what..
					}
					return false;
				}
			});
			
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					DToolsContext.getInstance().getLogger().info("CleanThread: deleted: " + files[i].getAbsolutePath());
					files[i].delete();
				}
			}
			
			try {
				sleep(60 * 60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void test() {
		File dir = new File("/Users/matjaz/Developer/Projekti/DTools/temp");
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				
				String filename = f.getName().substring(0, f.getName().length() - 4);
				try {
					long name = Long.parseLong(filename);
					long now = System.currentTimeMillis();
					
					if (name < (now - (1 * 3600 * 1000))) {
						// older than 1 hour
						return true;
					}
					
				} catch (NumberFormatException e) {
					// so what..
				}
				return false;
			}
		});
		
		for (int i = 0; i < files.length; i++) {
			System.out.println(i + ".\t" + files[i].getName());
		}
	}
	
	public static void main(String[] args) {
		test();		
		
	}

}

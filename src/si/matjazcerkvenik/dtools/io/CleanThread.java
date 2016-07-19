package si.matjazcerkvenik.dtools.io;

import java.io.File;
import java.io.FileFilter;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.ssh.SshResponse;

/**
 * This thread periodically checks $DTOOLS_HOME$/temp directory and deletes files older than 1 hour.
 * 
 * @author matjaz
 *
 */
public class CleanThread extends Thread {
	
	private boolean running = true;
	
	@Override
	public void run() {
		
		try {
			sleep(60 * 1000);  // wait until tomcat is up and running before starting this thread
		} catch (InterruptedException e1) {
		}
		
		while (running) {
			
			DToolsContext.getInstance().getLogger().info("CleanThread: started");
			
			cleanTempDir(1);
			cleanSshResponses(DProps.getPropertyInt(DProps.SSH_HISTORY_AGING_PERIOD));
			
			try {
				sleep(60 * 60 * 1000);
			} catch (InterruptedException e) {
				DToolsContext.getInstance().getLogger().debug("CleanThread: interrupted");
			}
			
		}
		
	}
	
	public void stopCleanThread() {
		running = false;
		this.interrupt();
	}
	
	
	/**
	 * Clean files in temp directory which are older than given time in hours
	 * @param olderThan (hours)
	 */
	private void cleanTempDir(final int olderThan) {
		
		File dir = new File(DToolsContext.HOME_DIR + "/temp");
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				String filename = f.getName().substring(0, f.getName().length() - 4);
				try {
					long name = Long.parseLong(filename);
					long now = System.currentTimeMillis();
					
					if (name < (now - (olderThan * 3600 * 1000))) {
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
				DToolsContext.getInstance().getLogger().debug("CleanThread:cleanTempDir(): delete: " + files[i].getAbsolutePath());
				files[i].delete();
			}
		}
		
	}
	
	private void cleanSshResponses(final int olderThan) {
		
		File dir = new File(DToolsContext.HOME_DIR + "/config/users/default/ssh/saved/");
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				
				// accept only xml files which are older than given time
				
				if (!f.getAbsolutePath().endsWith(".xml")) {
					return false;
				}
				
				String filename = f.getName().substring(0, f.getName().length() - 4);
				String temp[] = filename.split("@");
				try {
					long name = Long.parseLong(temp[0]);
					long now = System.currentTimeMillis();
					
					if (name < (now - (olderThan * 3600 * 1000))) {
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
				// check persistance flag (favorite)
				SshResponse r = DAO.getInstance().loadSshResponse(files[i].getAbsolutePath());
				if (!r.isFavorite()) {
					DToolsContext.getInstance().getLogger().debug("CleanThread:cleanSshResponses(): delete: " + files[i].getAbsolutePath());
					r.deleteTxt();
					files[i].delete();
				}
				
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

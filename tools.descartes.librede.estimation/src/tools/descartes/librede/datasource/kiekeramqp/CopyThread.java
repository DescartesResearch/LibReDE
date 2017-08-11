package tools.descartes.librede.datasource.kiekeramqp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CopyThread extends Thread {

	private String sourcedir;
	private String targetdir;
	
	public CopyThread(String sourcedir, String targetdir) {
		this.sourcedir =sourcedir;
		this.targetdir =targetdir;
	}
	
	
	@Override
	public void run() {
		File srcDir = new File(sourcedir);
		File destDir = new File(targetdir);
		try {
			FileUtils.copyDirectoryToDirectory(srcDir, destDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FileUtils.deleteDirectory(srcDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

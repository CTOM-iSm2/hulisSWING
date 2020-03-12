package util.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GaussianFilter extends FileFilter implements HasDefaultExtension {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) 
            return true;
       
 
        String extension = FilesUtil.getExtension(f);
        return extension.equals("com");
 
	}

	@Override
	public String getDescription() {
		return "Gaussian (*.com)";
	}

	public String getDefaultExtension() {
		return "com";
	}

}

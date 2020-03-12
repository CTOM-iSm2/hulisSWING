package util.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GaussianTestFilter extends FileFilter implements HasDefaultExtension {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) 
            return true;
       
 
        String extension = FilesUtil.getExtension(f);
        return extension.equals("test");
 
	}

	@Override
	public String getDescription() {
		return "Hulis Gaussian test (*.test)";
	}

	public String getDefaultExtension() {
		return "test";
	}

}

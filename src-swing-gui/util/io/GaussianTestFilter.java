package util.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GaussianTestFilter extends FileFilter implements HasDefaultExtension {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) 
            return true;
       
 
        String extension = FilesUtil.getExtension(f);
        return extension.equals("tcom");
 
	}

	@Override
	public String getDescription() {
		return "HuLiS Gaussian test input (*.tcom)";
	}

	public String getDefaultExtension() {
		return "tcom";
	}

}

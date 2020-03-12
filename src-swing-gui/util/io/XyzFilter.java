package util.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XyzFilter extends FileFilter implements HasDefaultExtension {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) 
            return true;
       
 
        String extension = FilesUtil.getExtension(f);
        return extension.equals("xyz");
 
	}

	@Override
	public String getDescription() {
		return "XYZ (*.xyz)";
	}

	public String getDefaultExtension() {
		return "xyz";
	}

}

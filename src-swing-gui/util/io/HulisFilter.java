package util.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class HulisFilter extends FileFilter implements HasDefaultExtension{

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) 
            return true;
       
 
        String extension = FilesUtil.getExtension(f);
        return extension.equals("hulis");
 
	}

	@Override
	public String getDescription() {
		return "HuLiS (*.hulis)";
	}

	public String getDefaultExtension() {
		return "hulis";
	}

}

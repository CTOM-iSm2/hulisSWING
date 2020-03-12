package util.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XmlFilter extends FileFilter implements HasDefaultExtension{

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) 
            return true;
       
 
        String extension = FilesUtil.getExtension(f);
        return extension.equals("xml");
 
	}

	@Override
	public String getDescription() {
		return "XML (*.xml)";
	}

	public String getDefaultExtension() {
		return "xml";
	}

}

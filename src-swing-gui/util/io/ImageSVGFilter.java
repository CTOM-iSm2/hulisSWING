package util.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageSVGFilter extends FileFilter implements HasDefaultExtension {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) 
            return true;
       
        String extension = FilesUtil.getExtension(f);
        return extension.equals("svg");
 
	}

	@Override
	public String getDescription() {
		/*int i = 0;
		String types = "Images (";
		for (String t : ImageIO.getWriterFormatNames()) {
			if (i == 1)
				types += ", "; 
			types += "*." + t;
			i =1;
		}
		types += ")";*/
		return "Image SVG (*.svg)";
	}

	public String getDefaultExtension() {
		return "svg";
	}

}

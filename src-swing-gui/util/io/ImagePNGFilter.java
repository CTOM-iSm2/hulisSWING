package util.io;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

public class ImagePNGFilter extends FileFilter implements HasDefaultExtension {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) 
            return true;
       
		String extension = FilesUtil.getExtension(f);
		for (String t : ImageIO.getWriterFormatNames()) {
			if (extension.equals(t)) 
				return true;
		}
        
        return false;
 
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
		return "Image PNG (*.png)";
	}

	public String getDefaultExtension() {
		return "png";
	}

}

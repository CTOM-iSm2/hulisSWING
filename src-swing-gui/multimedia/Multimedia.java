package multimedia;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Multimedia {

	static public void main(String argv[]) {
		try {
			Multimedia m = new Multimedia("mouseclick.wav");
			m.playwav();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	File file;

	public Multimedia(String file) throws Exception {
		this.file = new File(file);
	}

	public void playwav() throws Exception {
		// lit un fichier wav.
		/*
		 * TODO : Gerer les exceptions
		 */
		AudioInputStream stream = AudioSystem.getAudioInputStream(this.file);
		DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
		Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(stream);
		clip.start();
	}
}
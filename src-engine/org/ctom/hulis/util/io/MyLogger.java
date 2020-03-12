package org.ctom.hulis.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class MyLogger {
	private static StringBuffer log = new StringBuffer();
	
	public void logp(String level, String sourceClass, String sourceMethod,
			String msg) {
		StringBuffer buffer = new StringBuffer();;
		buffer.append(new Date());
		buffer.append(" ");
		buffer.append(sourceClass);
		buffer.append(" ");
		buffer.append(sourceMethod);
		buffer.append("\n");
		buffer.append(level);
		buffer.append(": ");
		buffer.append(msg);
		buffer.append("\n");
		
		HuckelIO.PrintIf(buffer);
		MyLogger.log.append(buffer);
	}

	public void logp(String level, String sourceClass, String sourceMethod,
			String msg, Throwable thrown) {
		// affiche l'exception a l'ecran
		thrown.printStackTrace();
		logp(level, sourceClass, sourceMethod, msg);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// l'instruction suivante memorise l'exception dans le log
		thrown.printStackTrace(new PrintWriter(outStream, true)); 
		try {
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MyLogger.log.append(outStream);

	}

	@Override
	public String toString() {
		return MyLogger.log.toString();
	}
}

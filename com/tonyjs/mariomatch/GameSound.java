package com.tonyjs.mariomatch;

import java.io.*;
import javax.sound.sampled.*;

public class GameSound {
	private static File songFile, themeSongFile;
	private static Clip songClip;
	final static String sep = java.io.File.separator;

	public static void setSongFileToPlay(String song)
	{
		songFile = new File("sounds" + sep + song + ".wav");
		playSongFile(songFile);
	}
	
	public static Clip startThemeSong()
	{
		themeSongFile = new File("sounds" + sep + "gameTheme.wav");
		return playSongFile(themeSongFile);
	}

	public static Clip playSongFile(File song)
	{
		try {
			AudioInputStream stream;
			AudioFormat format;
			DataLine.Info info;

			stream = AudioSystem.getAudioInputStream(song);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			songClip = (Clip) AudioSystem.getLine(info);
			songClip.open(stream);
			if (songClip.isRunning()) {
				songClip.stop();
			} else {
				songClip.start();
			}
		}
		catch (Exception e) {
			System.out.println("Could not load song file");
		}
		return songClip;
	}
}

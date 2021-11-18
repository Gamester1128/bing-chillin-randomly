package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

	public static void main(String[] args) {

		JSONObject obj = null;
		try {
			obj = (JSONObject) new JSONParser().parse(readFile("config.json"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Random rand = new Random();
		String url = (String) obj.get("path");
		while (true) {
			long time = (long) obj.get("time");
			try {
				int randTime = rand.nextInt((int)time * 1000 * 60);
				System.out.println(randTime);
				Thread.sleep(randTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Playing Sound");
			playSound(url);
		}
	}

	public static String readFile(String url) {
		String total = "";
		String newLine;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(url));
			while ((newLine = bufferedReader.readLine()) != null) {
				// process the line as required
				total += newLine;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return total;
	}

	public static void playSound(String url) {
		try {
			File file = new File(url);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public static void setVolume(Clip clip, int level) {
		Objects.requireNonNull(clip);
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
		if (volume != null) {
			volume.setValue((float) (level / 100.0));
		}
	}

}

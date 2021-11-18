package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
	
	// path to config file 
	private static final String configPath = "config.json";

	public static void main(String[] args) {
		JSONObject json = null;
		try {
			// get json file at config path
			json = (JSONObject) new JSONParser().parse(readFile(configPath));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// All the configuration from json file
		String cfg_url = (String) json.get("path");
		char cfg_unit = ((String) json.get("unit")).charAt(0);
		int cfg_time = (int) (long) json.get("time");

		// Initialise
		Random rand = new Random();
		boolean running = true;
		int i = 1;
		while (running) {

			// get multiplier for how many milllis to sleep based on cfg_unit
			int mulitiplier = 1;
			switch (cfg_unit) {
			case 'm':
				mulitiplier *= 60;
			case 's':
				mulitiplier *= 1000;
			}
			// stores the upper bound for random
			int randTime = (int) cfg_time * mulitiplier;
			System.out.println("Lower Bound for Random: 0ms");
			System.out.println("Upper Bound for Random: " + (randTime - 1) + "ms");

			sleep(rand.nextInt(randTime));

			System.out.println("#" + i + " : Playing Sound!");
			playSound(cfg_url);
		}
	}

	// puts main thread to sleep
	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Return string storing everything from file at url
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

	// Used to play .wav soundtrack found at url, yoinked it form
	// https://stackoverflow.com/questions/6045384/playing-mp3-and-wav-in-java
	// and modified to take path
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

}

package com.gdd.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkParser {

	/**
	 * Parse a file containing the distance matrix
	 * 
	 * @param file
	 *            the path of the file to parse
	 * @return a list of list (i.e. a matrix) of distances
	 */
	public static ArrayList<ArrayList<Integer>> parse(String file) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		// #1 scan each line
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(file));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// #2 scan each integer of the line
				result.add(new ArrayList<Integer>());
				Scanner scannerInt = new Scanner(line);
				while (scannerInt.hasNextInt()) {
					Integer distance = scannerInt.nextInt();
					result.get(result.size() - 1).add(distance);
				}
				// remove the last empty list
				if (result.get(result.size() - 1).size() == 0) {
					result.remove((int) result.size() - 1);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
}

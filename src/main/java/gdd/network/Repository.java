package gdd.network;

import java.util.HashMap;

/**
 * Stores every message incoming and delete them one every peers read it
 */
public class Repository {

	// keys are "uid;counter"
	// values are couple <m;read> where m is the message and read the number of
	// read left: when read is equal to 0, the key value is deleted from repo
	private static HashMap<String, Couple> repo = new HashMap<String, Couple>();

	private static Integer nbPeers;

	public static void setNbPeers(Integer nbPeers) {
		Repository.nbPeers = nbPeers;
	}

	public static Msg get(String key) {
		Couple value = repo.get(key);
		if (value == null) {
			// TODO: error
			System.out.println("Key does not seems to exist: " + key);
		} else {
			if (value.read == 1) {
				// delete value
				repo.remove(key);
			} else {
				value.read = value.read - 1;
			}
		}
		return value.m;
	}

	public static void put(String key, Msg m) {
		Couple value = new Couple(m, nbPeers - 1); // -1 cause source already
													// read is own message
		repo.put(key, value);
	}
}

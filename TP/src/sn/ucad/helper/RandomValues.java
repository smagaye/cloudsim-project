package sn.ucad.helper;

public class RandomValues {
	public static int between(int min, int max) {
		int nombreAleatoire = min + (int)(Math.random() * ((max - min) + 1));
		return nombreAleatoire;
	}
}

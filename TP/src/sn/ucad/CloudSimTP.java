package sn.ucad;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import sn.ucad.helper.Const;
import sn.ucad.helper.ExcelWriter;
import sn.ucad.manager.CloudSimManager;


public class CloudSimTP {

	/**
	 * Creates main() to run this example
	 */
	public static void main(String[] args) {

		double aleatoire[] =new double[10];
		double fixe[] =new double[10];

		for (int i = 0; i < 10; i++) {
			aleatoire[i]=CloudSimManager.simulationAleatoire("Simulation Aléatoire",i+1);
			fixe[i]=CloudSimManager.simulationSimple("Simulation la taille de la requête est fixé à "+Const.QUERY_SIZE_FIXED,i+1,Const.QUERY_SIZE_FIXED);
		}
		
		System.out.println("----------------------------------");
		System.out.println("----------- Tableau  -------------");
		System.out.println("----------------------------------");
		System.out.println("Nombre de Vms\tFixe\t\tAleatoire");
		
		try {
			ExcelWriter.generateExcelFileUsingTemplate(fixe,aleatoire);
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
	
	}
}

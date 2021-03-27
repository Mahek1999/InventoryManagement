package com.higradius.main;

import com.higradius.main.loader.DataLoader;

/*
 * This is our main class. From where the execution of our program would start.
 */
public class InvoiceDetailApplication {
	public static void main(String x[])
	{
		DataLoader dataLoader = new DataLoader();
		//Check whether the file has already been uploaded or not.
		boolean isCsvUploaded = dataLoader.isFileAlreadyUploaded();
		if(isCsvUploaded)
			System.out.println("The file has already been uploaded. Thank you");
		else {
			System.out.println("Inserting data.........Please Wait!");
			//If the file has not been uploaded. Upload it.
			dataLoader.uploadCsvToDatabase();
		}


	}
}

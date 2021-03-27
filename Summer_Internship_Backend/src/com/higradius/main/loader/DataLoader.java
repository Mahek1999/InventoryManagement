package com.higradius.main.loader;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.higradius.main.database.JdbcConnectionFactory;
import com.higradius.main.service.InvoiceService;
import com.higradius.main.service.serviceImpl.InvoiceServiceImpl;

//More like a controller.
public class DataLoader {

	
	private InvoiceService invoiceService;
	private final String FILE_NAME = "resources/csv/1805456.csv";

	public DataLoader() {
		//Reference the class of which we need to have the implementation.
		//In our case. we only have a single implementation so we will use that.
		this.invoiceService = new InvoiceServiceImpl();
	}


	public void uploadCsvToDatabase() {
		File csvFile = new File(FILE_NAME);
		//Calls the insertAllInvoiceAllRecords function implemented in InvoiceServiceImpl.class
		invoiceService.insertAllInvoiceRecords(csvFile);
	}

	//To check whether the file as already been updated or not.
	public boolean isFileAlreadyUploaded() {
		Connection conn = JdbcConnectionFactory.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT COUNT(*) AS 'rows_num' FROM invoice_details");
			if (resultSet.next()) {
				int numberOfRows = resultSet.getInt("rows_num");
				if (numberOfRows == 0)
					return false;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				conn.close();
				statement.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return true;
	}
}

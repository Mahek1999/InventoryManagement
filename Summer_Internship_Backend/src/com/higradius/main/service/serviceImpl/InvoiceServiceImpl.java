package com.higradius.main.service.serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.higradius.main.DAO.InvoiceDetailDTO;
import com.higradius.main.DAO.TemplateDTO;
import com.higradius.main.database.JdbcConnectionFactory;
import com.higradius.main.model.InvoiceDetail;
import com.higradius.main.payload.EditRecordRequest;
import com.higradius.main.service.InvoiceService;

public class InvoiceServiceImpl implements InvoiceService {

	// Choosing a batch size of 50
	private final int BATCH_SIZE = 100;
	String value = "";

	@Override
	public void insertAllInvoiceRecords(File file) {
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			// Get the first row.
			String row[] = br.readLine().split(",");
			// CSV has 2 column with same name as "document_create_date". We will be using
			// the "document_create_date.1".
			row[8] = "document_create_date.1";
			// This map the columnName to its index in row array. This would make easy for
			// us to access the column from csv directly using columnName.
			Map<String, Integer> columnNameToIndexMapper = new HashMap<>();
			for (int i = 0; i < row.length; i++) {
				columnNameToIndexMapper.put(row[i], i);
			}
			// Invoke the insertAllInvoiceRecordsBatch function.
			insertAllInvoiceRecordsBatch(br, columnNameToIndexMapper);
		} catch (FileNotFoundException fle) {
			System.out.println("The " + file.getName() + " cannot be read");
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	private void insertAllInvoiceRecordsBatch(BufferedReader br, Map<String, Integer> columnNameToIndexMapper) {

		// Initialize the query. Using final so that it cannot be altered later.
		final String query = "INSERT INTO invoice_details "
				+ "(business_code, cust_number, name_customer, clear_date, business_year, doc_id, posting_date, "
				+ "document_create_date, due_in_date, invoice_currency, document_type, "
				+ "posting_id, area_business, total_open_amount, baseline_create_date, cust_payment_terms, "
				+ "invoice_id, isOpen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?)";
		Connection conn = null;
		PreparedStatement psInsert = null;
		InvoiceDetail invoiceDetail = null;
		String line = null;
		String row[] = null;
		// To Maintain the count of batch.
		int countBatchSize = 0;
		try {
			// Get a connection to the SQL.
			conn = JdbcConnectionFactory.getConnection();
			// Prepare the query to execute.
			psInsert = conn.prepareStatement(query);
			// Read the csv line by line.
			while ((line = br.readLine()) != null) {
				// Get the row in the array splitted by ','.
				row = line.split(",");
				// Map the csv row to POJO data field.
				invoiceDetail = getInvoiceDetails(row, columnNameToIndexMapper);
				// If primary key is defined.
				if (invoiceDetail != null) {
					countBatchSize++;

					psInsert.setString(1, invoiceDetail.getBusinessCode());
					psInsert.setString(2, invoiceDetail.getCustNumber());
					psInsert.setString(3, invoiceDetail.getNameCustomer());
					psInsert.setTimestamp(4, invoiceDetail.getClearDate());

					// As Business Year is Short type and setShort() always receive a primitive data
					// type. We explicitly need to set null for such data type.
					if (invoiceDetail.getBusinessYear() == null)
						psInsert.setNull(5, Types.SMALLINT);
					else
						psInsert.setShort(5, invoiceDetail.getBusinessYear());

					psInsert.setLong(6, invoiceDetail.getDocId());
					psInsert.setDate(7, invoiceDetail.getPostingDate());
					psInsert.setDate(8, invoiceDetail.getDocumentCreateDate());
					psInsert.setDate(9, invoiceDetail.getDueInDate());
					psInsert.setString(10, invoiceDetail.getInvoiceCurrency());
					psInsert.setString(11, invoiceDetail.getDocumentType());

					// As Posting id is Short type and setShort() always receive a primitive data
					// type. We explicitly need to set null for such data type.
					if (invoiceDetail.getPostingId() == null)
						psInsert.setNull(12, Types.TINYINT);
					else
						psInsert.setShort(12, invoiceDetail.getPostingId());

					psInsert.setString(13, invoiceDetail.getAreaBusiness());

					// As Total Open Amount is Double type and setDouble() always receive a
					// primitive data type. We explicitly need to set null for such data type.
					if (invoiceDetail.getTotalOpenAmount() == null)
						psInsert.setNull(14, Types.DOUBLE);
					else
						psInsert.setDouble(14, invoiceDetail.getTotalOpenAmount());

					psInsert.setDate(15, invoiceDetail.getBaselineCreateDate());
					psInsert.setString(16, invoiceDetail.getCustPaymentTerms());
					// As InvoiceId is Long type and setLong() always receive a primitive data type.
					// We explicitly need to set null for such data type.
					if (invoiceDetail.getInvoiceId() == null)
						psInsert.setNull(17, Types.BIGINT);
					else
						psInsert.setLong(17, invoiceDetail.getInvoiceId());

					if (invoiceDetail.getIsOpen() == null)
						psInsert.setNull(18, Types.SMALLINT);
					else
						psInsert.setByte(18, invoiceDetail.getIsOpen());

					// Keep stacking the queries.
					psInsert.addBatch();

					// Execute every 50 query.
					if (countBatchSize % BATCH_SIZE == 0) {
						psInsert.executeBatch();
						System.out.println(countBatchSize + " Records Uploaded..");
					}

				}
				// Execute the remaining query.
				psInsert.executeBatch();
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException nfe) {
			System.out.println("Unable to parse string.");
			nfe.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				conn.close();
				psInsert.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	// To Map row elements to POJO data field.
	private InvoiceDetail getInvoiceDetails(String row[], Map<String, Integer> columnNameToIndexMapper) {

		InvoiceDetail invoiceDetail = null;
		String docId = row[columnNameToIndexMapper.get("doc_id")];
		try {
			// If primary key is null. SKIP
			if (!docId.isEmpty()) {

				invoiceDetail = new InvoiceDetail();
				// Get rid of .0 appended after every docId in my csv.
				docId = docId.substring(0, docId.lastIndexOf('.'));
				invoiceDetail.setDocId(Long.parseLong(docId));

				// For Business Code
				value = row[columnNameToIndexMapper.get("business_code")];

				if (!value.isEmpty())
					invoiceDetail.setBusinessCode(value);
				else
					invoiceDetail.setBusinessCode(null);

				// For Customer Name
				value = row[columnNameToIndexMapper.get("name_customer")];
				if (!value.isEmpty())
					invoiceDetail.setNameCustomer(value);
				else
					invoiceDetail.setNameCustomer(null);

				// For Customer Number
				value = row[columnNameToIndexMapper.get("cust_number")];
				if (!value.isEmpty())

					invoiceDetail.setCustNumber(value);
				else
					invoiceDetail.setCustNumber(null);

				// For Clear Date.
				value = row[columnNameToIndexMapper.get("clear_date")];
				if (!value.isEmpty()) {
					// Convert String to Timestamp.
					Timestamp clearDateTimestamp = Timestamp.valueOf(value);
					invoiceDetail.setClearDate(clearDateTimestamp);
				} else
					invoiceDetail.setClearDate(null);

				// For Business Year
				value = row[columnNameToIndexMapper.get("buisness_year")];
				if (!value.isEmpty()) {

					value = value.substring(0, value.lastIndexOf('.'));
					invoiceDetail.setBusinessYear(Short.parseShort(value));
				} else
					invoiceDetail.setBusinessYear(null);

				// For Posting Date
				value = row[columnNameToIndexMapper.get("posting_date")];
				if (!value.isEmpty()) {
					Date postingDate = Date.valueOf(value);
					invoiceDetail.setPostingDate(postingDate);
				} else
					invoiceDetail.setPostingDate(null);

				// For Document Create Date
				value = row[columnNameToIndexMapper.get("document_create_date.1")];
				if (!value.isEmpty()) {
					// Convert the date into yyyy-mm-dd format.
					Date date = toDateFormat(value);
					invoiceDetail.setDocumentCreateDate(date);
				} else
					invoiceDetail.setDocumentCreateDate(null);

				// For Due in Date
				value = row[columnNameToIndexMapper.get("due_in_date")];
				if (!value.isEmpty()) {
					// Convert the date into yyyy-mm-dddd format.
					Date date = toDateFormat(value);
					invoiceDetail.setDueInDate(date);
				} else
					invoiceDetail.setDueInDate(null);

				// For Invoice Currency
				value = row[columnNameToIndexMapper.get("invoice_currency")];
				if (!value.isEmpty()) {
					invoiceDetail.setInvoiceCurrency(value);
				} else
					invoiceDetail.setInvoiceCurrency(null);

				// For Document Type
				value = row[columnNameToIndexMapper.get("document type")];
				if (!value.isEmpty()) {
					invoiceDetail.setDocumentType(value);
				} else
					invoiceDetail.setDocumentType(null);

			}

			// For Posting Id
			value = row[columnNameToIndexMapper.get("posting_id")];
			if (!value.isEmpty()) {
				value = value.substring(0, value.lastIndexOf('.'));
				invoiceDetail.setPostingId(Short.parseShort(value));
			} else
				invoiceDetail.setPostingId(null);

			// For Area Business
			value = row[columnNameToIndexMapper.get("area_business")];
			if (!value.isEmpty()) {

				invoiceDetail.setAreaBusiness(value);
			} else
				invoiceDetail.setAreaBusiness(null);

			// For Total Open Amount
			value = row[columnNameToIndexMapper.get("total_open_amount")];
			if (!value.isEmpty()) {

				invoiceDetail.setTotalOpenAmount(Double.parseDouble(value));
			} else
				invoiceDetail.setTotalOpenAmount(null);

			// For Baseline Create Date
			value = row[columnNameToIndexMapper.get("baseline_create_date")];
			if (!value.isEmpty()) {
				// Convert into yyyymmdd format.
				Date date = toDateFormat(value);
				invoiceDetail.setBaselineCreateDate(date);
			} else
				invoiceDetail.setBaselineCreateDate(null);

			// For Customer Payment Terms.
			value = row[columnNameToIndexMapper.get("cust_payment_terms")];
			if (!value.isEmpty()) {

				invoiceDetail.setCustPaymentTerms(value);
			} else
				invoiceDetail.setCustNumber(null);

			// For Invoice - Id
			value = row[columnNameToIndexMapper.get("invoice_id")];
			if (!value.isEmpty()) {
				value = value.substring(0, value.lastIndexOf('.'));
				invoiceDetail.setInvoiceId(Long.parseLong(value));
			} else
				invoiceDetail.setInvoiceId(null);

			// For isOpen
			value = row[columnNameToIndexMapper.get("isOpen")];
			if (!value.isEmpty()) {

				invoiceDetail.setIsOpen(Byte.parseByte(value));
			} else
				invoiceDetail.setIsOpen(null);
		} catch (NullPointerException e) {
			System.out.println(value);
		}
		return invoiceDetail;
	}

	private Date toDateFormat(String dateTime) {
		// Define a SimpleDateFormat with specific pattern.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		java.util.Date convertedDate = null;
		Date sqlDate = null;
		try {
			// Parse date
			convertedDate = dateFormat.parse(dateTime);
			// Set a new pattern
			SimpleDateFormat sdfnewformat = new SimpleDateFormat("yyyy-MM-dd");
			// Convert into that format.
			String finalDateString = sdfnewformat.format(convertedDate);
			sqlDate = Date.valueOf(finalDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqlDate;
	}

	@Override
	public Optional<List<InvoiceDetailDTO>> getAllRecords() {
		List<InvoiceDetailDTO> resultSetList = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcConnectionFactory.getConnection();
			stmt = conn.createStatement();
			String query = "SELECT * FROM invoice_details LIMIT 5";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {

				InvoiceDetailDTO invoiceDetail = new InvoiceDetailDTO();
				setUpInvoiceDetail(rs, invoiceDetail);
				resultSetList.add(invoiceDetail);
			}

		} catch (SQLException sqe) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return Optional.of(resultSetList);
	}

	@Override
	public void deleteRecord(String docId[]) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countBatchSize = 0;
		try {
			conn = JdbcConnectionFactory.getConnection();
			String query = "DELETE FROM `invoice_details` WHERE doc_id=?";
			pstmt = conn.prepareStatement(query);
			for (String value : docId) {
				countBatchSize++;
				Long docIdLong = Long.parseLong(value);
				pstmt.setLong(1, docIdLong);
				System.out.println("In deleteRecord for " + docIdLong);

				pstmt.addBatch();
				if (countBatchSize % 5 == 0) {
					pstmt.executeBatch();
				}
			}
			System.out.println("Executing query");
			int arr[] = pstmt.executeBatch();
			System.out.println(Arrays.asList(arr));
		} catch (SQLException sqe) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void insertInvoiceRecord(InvoiceDetailDTO invoiceDetailDAO) {

		final String query = "INSERT INTO invoice_details "
				+ "(business_code, cust_number, name_customer, clear_date, business_year, doc_id, posting_date, "
				+ "document_create_date, due_in_date, invoice_currency, document_type, "
				+ "posting_id, area_business, total_open_amount, baseline_create_date, cust_payment_terms, "
				+ "invoice_id, isOpen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?)";
		Connection conn = null;
		PreparedStatement psInsert = null;
		try {
			// Get a connection to the SQL.
			conn = JdbcConnectionFactory.getConnection();
			// Prepare the query to execute.
			psInsert = conn.prepareStatement(query);

			psInsert.setNull(1, Types.CHAR);
			psInsert.setString(2, invoiceDetailDAO.getCustNumber());
			psInsert.setString(3, invoiceDetailDAO.getNameCustomer());
			psInsert.setNull(4, Types.TIMESTAMP);
			psInsert.setNull(5, Types.SMALLINT);
			psInsert.setLong(6, invoiceDetailDAO.getInvoiceId());

			long millis = System.currentTimeMillis();
			Date date = new java.sql.Date(millis);
			psInsert.setDate(7, date);
			psInsert.setDate(8, date);
			psInsert.setDate(9, invoiceDetailDAO.getDueInDate());
			psInsert.setString(10, "USD");
			psInsert.setNull(11, Types.CHAR);
			psInsert.setNull(12, Types.SMALLINT);
			psInsert.setNull(13, Types.VARCHAR);
			psInsert.setDouble(14, invoiceDetailDAO.getTotalOpenAmount());
			psInsert.setDate(15, date);
			psInsert.setNull(16, Types.CHAR);
			psInsert.setLong(17, invoiceDetailDAO.getInvoiceId());
			psInsert.setByte(18, (byte) 1);
			psInsert.executeUpdate();
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		} catch (NumberFormatException nfe) {
			System.out.println("Unable to parse string.");
			nfe.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				conn.close();
				psInsert.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Optional<List<InvoiceDetailDTO>> getRecordByPages(int pageCount, int limit) {
		List<InvoiceDetailDTO> resultSetList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = JdbcConnectionFactory.getConnection();
			String query = "SELECT * FROM invoice_details LIMIT ? OFFSET ? ";
			pstmt = conn.prepareStatement(query);
			int offset = pageCount * limit;
			pstmt.setInt(1, limit);
			pstmt.setInt(2, offset);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				InvoiceDetailDTO invoiceDetail = new InvoiceDetailDTO();
				setUpInvoiceDetail(rs, invoiceDetail);
				resultSetList.add(invoiceDetail);
			}

		} catch (SQLException sqe) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return Optional.of(resultSetList);
	}

	@Override
	public void updateInvoiceRecord( EditRecordRequest invoiceDetail) {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = JdbcConnectionFactory.getConnection();
			String query = "UPDATE invoice_details SET total_open_amount = ? , notes = ?"
					+ " WHERE doc_id = ?";
			
			stmt = conn.prepareStatement(query);
			
			stmt.setDouble(1,invoiceDetail.getTotalOpenAmount());
			stmt.setString(2, invoiceDetail.getNotes());
			stmt.setLong(3,invoiceDetail.getDocId());
			
			stmt.executeUpdate();
			

		} catch (SQLException sqe) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}


	}

	private void setUpInvoiceDetail(ResultSet rs, InvoiceDetailDTO invoiceDetail) throws SQLException {
		invoiceDetail.setCustNumber(rs.getString("cust_number"));
		invoiceDetail.setNameCustomer(rs.getString("name_customer"));
		invoiceDetail.setDueInDate(rs.getDate("due_in_date"));
		invoiceDetail.setTotalOpenAmount(rs.getDouble("total_open_amount"));
		invoiceDetail.setInvoiceId(rs.getLong("invoice_id"));
		invoiceDetail.setNotes(rs.getString("notes"));
		invoiceDetail.setDocId(rs.getLong("doc_id"));

	}

	@Override
	public Optional<List<TemplateDTO>> getInvoiceRecordsByDocIds(String str) {
		
		List<TemplateDTO> resultSetList = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcConnectionFactory.getConnection();
			stmt = conn.createStatement();
			String query = "SELECT * FROM invoice_details WHERE doc_id IN";
			String temp = str;
			str = "(" + temp+ ")";
			query = query + " " + str;
			System.out.println("The string after modify is : "+str);
			query.replace("(?)", str);
			System.out.println("The modified query is : "+query);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {

				TemplateDTO templateDAO = new TemplateDTO();
				templateDAO.setTotalOpenAmount(rs.getDouble("total_open_amount"));
				templateDAO.setPoNumber(rs.getLong("invoice_id"));
				templateDAO.setInvoiceId(templateDAO.getPoNumber());
				templateDAO.setCurrency(rs.getString("invoice_currency"));
				templateDAO.setDueInDate(rs.getDate("due_in_date"));
				templateDAO.setInvoiceDate(rs.getDate("document_create_date"));
				
				resultSetList.add(templateDAO);
			}

		} catch (SQLException sqe) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return Optional.of(resultSetList);
	}

	@Override
	public Optional<List<InvoiceDetailDTO>> searchByInvoiceId(String str) {
		
		List<InvoiceDetailDTO> resultSetList = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JdbcConnectionFactory.getConnection();
			stmt = conn.createStatement();
			String parameter = "'"+str+"%"+"'";
			String query = "SELECT * FROM invoice_details WHERE invoice_id LIKE " + parameter + "LIMIT 50";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {

				InvoiceDetailDTO invoiceDetail = new InvoiceDetailDTO();
				setUpInvoiceDetail(rs, invoiceDetail);
				resultSetList.add(invoiceDetail);
			}

		} catch (SQLException sqe) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return Optional.of(resultSetList);

	}
	}


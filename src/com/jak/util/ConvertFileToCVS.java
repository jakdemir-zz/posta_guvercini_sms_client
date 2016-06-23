package com.jak.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.jak.pojo.Customer;

public class ConvertFileToCVS {

	private List<Customer> formatCustomerList(List<Customer> customerList) {
		for (Customer customer : customerList) {
			validateAndFixName(customer);
			validatePhoneList(customer);
		}
		return customerList;
	}

	private void validateAndFixName(Customer customer) {
		if (customer.getName().length() > 10) {
			customer.setName(customer.getName().substring(0, 10));
		}
	}

	private boolean validatePhoneList(Customer customer) {
		List<String> phoneNoList = customer.getPhoneNoList();
		for (String phone : phoneNoList) {
			if (phone.length() != 10) {
				System.out.println("Wrong Phone No: " + customer.getName() + ", " + phone);
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private void printCustomerList(List<Customer> customerList) {
		for (Customer customer : customerList) {
			System.out.print(customer.getId() + ", " + customer.getName());
			List<String> phoneList = customer.getPhoneNoList();
			for (String phone : phoneList) {
				System.out.print(", " + phone);
			}
			System.out.println("\n");
		}
	}

	public String readFile(String filePath) throws FileNotFoundException, IOException {
		FileInputStream fis = new  FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis,Charset.forName("ISO-8859-9"));
		StringBuffer sb = new StringBuffer();
		int b = 0;
		while ((b = isr.read()) != -1) {
			sb.append((char) b);
		}
		isr.close();
		fis.close();
		return sb.toString();
	}


	private void generateCustomerListFile(List<Customer> customerList, String fileName) throws IOException {
		FileWriter fw = new FileWriter(fileName);
		fw.append("Name,PhoneNumber,E-Mail\r\n");
		for (Customer customer : customerList) {
			fw.append(customer.getName());
			List<String> phoneList = customer.getPhoneNoList();
			if ((phoneList != null) && (phoneList.size() > 0)) {
				fw.append("," + phoneList.get(0));
			}
			// for (String phone : phoneList) {
			// fw.append("," + phone);
			// }
			fw.append(",\r\n");
		}
		fw.flush();
		fw.close();

	}
	public List<Customer> parseFile(String filePath) throws IOException {
		System.out.println("Reading File ...");
		String fileData = readFile(filePath);
		System.out.println("Parsing File ...");
		String[] lineArray = fileData.split("\n");
		List<Customer> customerList = new ArrayList<Customer>();
		String no ="";
		for (int i = 0; i < lineArray.length; i++) {
			String[] splitLineArray = lineArray[i].split(":");
			Customer customer = new Customer();
			List<String> phoneList = new ArrayList<String>();

			for (int j = 0; j < splitLineArray.length; j++) {
				if (j == 0) {
					// Know that id's started from 0
					customer.setId(i);
					customer.setName(splitLineArray[j].trim());
				} else {
					no = splitLineArray[j].trim();
					if (no.length() == 11 ) {
						phoneList.add(no.substring(1,11));	
					}else {
						System.out.println("Customer Name - No: "+customer.getName()+" - " + no);
					}
					
				}
			}
			if ((phoneList != null) && (phoneList.size() != 0)) {
				customer.setPhoneNoList(phoneList);
				customerList.add(customer);
			}else {
				System.out.println("No Phone Number: Customer Name - No: "+customer.getName()+" - " + no);
			}

		}
		formatCustomerList(customerList);
		return customerList;
	}


	public static void main(String[] args) {
		ConvertFileToCVS convertFileToCVS = new ConvertFileToCVS();
		String draftFilePath = "/home/jak/workspace_mine/posta_guvercini_sms_client/documents/tugce.txt";
		String newFilePath = "/home/jak/workspace_mine/posta_guvercini_sms_client/documents/tugceF.txt";
		try {
			List<Customer> customerList = convertFileToCVS.parseFile(draftFilePath);
			System.out.println("Generating New File ...");
			convertFileToCVS.generateCustomerListFile(customerList, newFilePath);
			System.out.println("File Generated ...");
			// convertFileToCVS.printCustomerList(customerList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

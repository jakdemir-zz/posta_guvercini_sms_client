package com.jak.sms.post;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.jak.pojo.Customer;
import com.jak.util.ConvertFileToCVS;

public class SmsSenderWithPost {

	private HttpMethod postRequestToUrlAndReturnMethodWithResponse(String url, String requestContent) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestBody(requestContent);

		method.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-9");

		client.executeMethod(method);
		method.getResponseBodyAsString();
		return method;
	}

	private String sendSms(String url, String requestContent) throws HttpException, IOException {
		StringBuffer sb = new StringBuffer();
		String log = "Sending SMSs \n";
		System.out.println(log);
		sb.append(log);
		PostMethod method = (PostMethod) postRequestToUrlAndReturnMethodWithResponse(url, requestContent);
		log = "SMSs are sent \n";
		System.out.println(log);
		sb.append(log);

		log = "StatusCode : " + method.getStatusCode() + "\n";
		System.out.println(log);
		sb.append(log);

		log = "StatusText : " + method.getStatusText() + "\n";
		System.out.println(log);
		sb.append(log);

		log = "ResponseBody : " + method.getResponseBodyAsString() + "\n";
		System.out.println(log);
		sb.append(log);
		return sb.toString();
	}

	private void writeToFile(String filePath, String fileContent) throws IOException {
		System.out.println("Started to write file : " + filePath);
		FileWriter fw = new FileWriter(filePath);
		fw.write(fileContent);
		fw.flush();
		fw.close();
		System.out.println("File written!");
	}

	private String generateRequestContent(String phoneListPath, String templatePath, String url) throws FileNotFoundException, IOException {
		ConvertFileToCVS converter = new ConvertFileToCVS();
		List<Customer> customerList = converter.parseFile(phoneListPath);
		String templateFileContent = converter.readFile(templatePath);
		StringBuffer body = new StringBuffer();
		for (Customer customer : customerList) {
			body.append("<TO>" + customer.getPhoneNoList().get(0) + "</TO>\n");
		}
		templateFileContent = templateFileContent.replace("{0}", body.toString());
		System.out.println(templateFileContent);
		writeToFile(templatePath + "_withPhoneNumbers", templateFileContent);
		return templateFileContent;
	}

	private void generateRequestAndSendSms(String phoneListPath, String templatePath, String logPath, String url) {
		try {
			String content = generateRequestContent(phoneListPath, templatePath, url);
			String response = sendSms(url, content);
			writeToFile(logPath, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SmsSenderWithPost smsSenderWithPost = new SmsSenderWithPost();
		String phoneListPath = "/home/jak/workspace_mine/posta_guvercini_sms_client/documents/testPhoneList.txt";//fihrist.txt";
		String logPath = "/home/jak/workspace_mine/posta_guvercini_sms_client/documents/logs.txt";
		String templatePath = "/home/jak/workspace_mine/posta_guvercini_sms_client/documents/template.xml";
		String url = "http://www.postaguvercini.com/api_xml/Sms_insreq.asp";

		smsSenderWithPost.generateRequestAndSendSms(phoneListPath, templatePath, logPath, url);
	}
}

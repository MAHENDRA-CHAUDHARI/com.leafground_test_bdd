package com.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class Jcraft extends Base{
	static Session session = null;
	
	public static void transferReportToServer(String reportPath) {
		String pathArr[] = reportPath.split("/");
		String reportName = pathArr[pathArr.length-1];
		String reportPathInit = fetchProperty("initialise_report_path");
		String propReport = System.getProperty("maahi.automationReportPath");
		
		if(propReport!=null) {
			reportPathInit = propReport;
		}
		
		String ipInit = fetchProperty("nfs_IP");
		String propIP = fetchProperty("maahi.buildIp");
		if(propIP!=null) {
			ipInit = propIP;
		}
		
		createJcraftConnection(ipInit+":"+fetchProperty("nfs_port"), fetchProperty("nfs_username"), fetchProperty("nfs_password"));
		createDirectory (reportPathInit+pathArr[pathArr.length-1]);
		createDirectory (reportPathInit+pathArr[pathArr.length-1]+"/screenshotes");
		createDirectory (reportPathInit+pathArr[pathArr.length-1]+"files");
		transferFilesToServer(reportName, new File(reportPath));
		String scriptPath = fetchProperty("nfs_shellScript");
		
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(fetchProperty("nfs_username"), fetchProperty("nfs_IP"),8080);
			session.setPassword("password");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			ChannelExec  channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand("sh "+scriptPath);    hardPause(5);
			channel.setInputStream(null);
			channel.setErrStream(System.err);
			channel.connect();    hardPause(40);
			channel.disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		stopConnection();
		logReport(Status.INFO, "Channel Disconnect Successfully");
	}
	
	public static Session createJcraftConnection(String serverIPAndPort, String userName, String password) {
		try {
			session =new JSch().getSession(userName, serverIPAndPort.split(":")[0], Integer.parseInt(serverIPAndPort.split(":")[1]));
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return session;
	}
	
	private static void transferFilesToServer(String reportName, File folder) {
		for(File fileEntry: folder.listFiles()) {
			if(fileEntry.isDirectory()) {
			transferFilesToServer (reportName, fileEntry);
			} else {
				String filePathForCopy = fileEntry.getPath();
				String pathDirs[] = filePathForCopy.split("\\\\");
				String reportPathInit = fetchProperty("initialise_report_path");
				String propReport = System.getProperty("maahi.automationReportPath");
				if(propReport!=null) {
					reportPathInit = propReport;
				}
					String folderName = pathDirs [pathDirs.length-2];
				if(folderName.equals("screenshots")) {
					transferFileToServer (filePathForCopy, reportPathInit+reportName+"/screenshots/"+fileEntry.getName());
				} else if (folderName.equals("files")){ 
					transferFileToServer (filePathForCopy, reportPathInit+reportName+"/files/"+fileEntry.getName());
				} else {
					transferFileToServer (filePathForCopy, reportPathInit+reportName+"/"+fileEntry.getName());
				}
			}	
		}
	}	
	
	public static void createDirectory(String folderPath) {
		try {
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			sftpChannel.mkdir(folderPath);
			sftpChannel.exit();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	private static void transferFileToServer(String srcPath, String destPath) {
		ChannelSftp sftpChannel = null;
		try {
		Channel channel = session.openChannel("sftp");
		channel.connect();
		sftpChannel = (ChannelSftp) channel;
		} catch(JSchException e) {
			e.printStackTrace();
		}
		
		InputStream fis = null;
		try {
			fis = new FileInputStream(new File(srcPath));
			
			try {
				sftpChannel.put(fis, destPath);
				System.out.println("File Transfered to ["+srcPath+"]");
				logReport(Status. PASS, "File Transfered ["+srcPath+"]");
			} catch (SftpException e) {
				System.err.println("Unable to transfer file ["+srcPath+"] to server");
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e1) {
			System.err.println("Unable to find file ["+srcPath+"] to server");
			e1.printStackTrace();
		}
		sftpChannel.exit();
}
	
	public static ArrayList <String> executeCommand(String command){
		ChannelExec channel = null;
		try {
			channel = (ChannelExec) session.openChannel("exec");
		} catch (JSchException e) {
			logReport(Status.FAIL, "Unable to Connect Server: "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		channel.setCommand(command);
		
		InputStream inputErrorStream = null;
		try {
			inputErrorStream = ((ChannelExec) channel).getErrStream();
		} catch (IOException e) {
			logReport(Status. FAIL, "Input Stream Error "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(inputErrorStream));
		InputStream inputStream = null;
		try {
			inputStream = channel.getInputStream();
		} catch (IOException e) {
			logReport(Status.FAIL, "Input Stream Error "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(inputStream));
		try {
			channel.connect();
		} catch (JSchException e) {
			logReport(Status. FAIL, "Unable to connect to given channel on server: "+e.getLocalizedMessage()); e.printStackTrace();
		}
		StringBuilder total = new StringBuilder();
		String line;
		
		ArrayList<String> serverResults = new ArrayList<>();
		boolean errorLines = false;
		try {
			while ((line = errorBuffer.readLine()) != null) {
				total.append(line);
				errorLines = true;
			}
		} catch (IOException e) {
			logReport(Status.FAIL, "Error while reading lines from console: "+e.getLocalizedMessage());
			e.printStackTrace();
		} try {
			while ((line = inputBuffer.readLine()) != null) {
				total.append(line+"<br>");
				serverResults.add(line);
			}
		} catch (IOException e) {
			logReport(Status.FAIL, "Error while reading lines from console: "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		channel.disconnect();
		session.disconnect();
		if(errorLines) {
			logReport(Status. FAIL, "There are error lines returned from server: "+total);
			Assert.fail("There are error lines returned from server: "+total);
		}
		logReport(Status.INFO, "Command ["+command+"] Return Result: "+ total.toString());
		return serverResults;
	}
	
	public static void stopConnection() {
		session.disconnect();
	}
	
	public static void SSHShellScriptExecutor() {
		String host = fetchProperty("nfs_IP");
		int port = Integer.parseInt(fetchProperty("nfs_IP"));
		String username = fetchProperty("nfs_username");
		String password = fetchProperty("nfs_password");
		String scriptPath = fetchProperty("nfs_shellScript");
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(username, host, port);
			session.setPassword (password);
			session.setConfig("StrictHostKey Checking", "no");
			session.connect();
			ChannelExec channel = (ChannelExec) 
			session.openChannel("exec");
			channel.setCommand("sh " + scriptPath);
			channel.setInputStream(null);
			channel.setErrStream (System.err);
			channel.connect();
			channel.disconnect();
			session.disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
}

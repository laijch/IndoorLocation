package com.indoorloc.ShopSignMatching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * MatlabClient¼àÌýÏß³Ì
 * @author Mo Haoran
 */
public class ClientListenThread extends Thread {
	Socket hostSocket;
	
	public ClientListenThread(Socket _hostSocket) {
		hostSocket = _hostSocket;
	}
	
	public void run() {
		String serverMsg = "";
		try {
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
			serverMsg = inFromServer.readLine();
			
			System.out.println("Receive msg from server: " + serverMsg);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Socket Closed.");
		}
		
		try {
			MatlabClient.getMatlabServerMsgFromThread(serverMsg);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("ClientListenThread ended.");
	}
}


package Codigo;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread {
	
	@Override
	public void run(){
		conexaoCliente();
		System.out.println("Servidor Online.");
	}
	
	private void conexaoCliente(){
		try{
			ServerSocket welcomeSocket = new ServerSocket(6789);
			while (true) {
				
				Socket connectionSocket = welcomeSocket.accept();
				new ThreadServidor(connectionSocket).start();	
				
			}
			
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
	}

}

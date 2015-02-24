package Codigo;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends Thread {
	
	@Override
	public void run(){
		System.out.println("Servidor Online.");
		conexaoCliente();
	}
	
	private void conexaoCliente(){
		try{
			ListaCliente.listaClientes = new ArrayList<Cliente>();
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

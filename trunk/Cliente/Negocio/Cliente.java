package Negocio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;

import Codigo.ThreadServidor;

public class Cliente {

	private String IPCliente = "127.0.0.1";
	private int PORTACliente = 6666;
		
	public Cliente(){
		new ThreadRecebeListaClientesOnline().start();
	}
	
	public void conexaoServidor(int tipoConexao, String nomeCliente){
		try{
			
			Socket clientSocket = new Socket("127.0.0.1", 6789);

			DataOutputStream outToServer =
					new DataOutputStream(clientSocket.getOutputStream()); 
			
			BufferedReader inFromServer =
					new BufferedReader(new
					InputStreamReader(clientSocket.getInputStream()));
			
			outToServer.writeBytes(tipoConexao+","+nomeCliente+","+IPCliente+","+PORTACliente+"\n");
			
			String entrada = inFromServer.readLine();
			
			System.out.println(entrada);
			
			clientSocket.close();
			
			System.out.println("Pedido de conexao enviado");

			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
}

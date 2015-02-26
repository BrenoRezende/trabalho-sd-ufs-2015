package Negocio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Interface.PrincipalUI;

public class ThreadEnviaArquivos extends Thread{

	@Override
	public void run(){
		
		ServerSocket welcomeSocket;
		try {
			welcomeSocket = new ServerSocket(5454);
			while (true) {
				
				Socket connectionSocket = welcomeSocket.accept();
				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));
				// Aguarda nome do arquivo enviado pelo cliente.
				try {
					connectionSocket.setSoTimeout(60000);
				} catch (Exception e) {
					connectionSocket.close();
					e.printStackTrace();
					return;
				}
				String nmArquivo = "teste.txt";
				System.out.println("Recebeu pedido de arquivo");

				// Verifica se o arquivo existe e caso exista envia para o cliente
				byte[] arquivo = lerbytes(nmArquivo);
				DataOutputStream outToClient = new DataOutputStream(
						connectionSocket.getOutputStream());
				outToClient.write(arquivo);
				System.out.println("Enviou arquivo");
				outToClient.close();


			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	public byte[] lerbytes(String nomeArq) throws IOException {
		System.out.println("Lendo do diretorio do servidor");
		Path path = Paths.get("C:/arquivos/", new String[] { nomeArq});
		return Files.readAllBytes(path);
	}
}

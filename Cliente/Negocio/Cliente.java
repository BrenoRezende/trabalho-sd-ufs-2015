package Negocio;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Cliente {

	private String nome;
	private String IPCliente;
	private int PORTACliente = 6666;

	public Cliente(){

		new ThreadRecebeListaClientesOnline().start();
		new ThreadRecebeMensagensChat().start();
		new ThreadEnviaArquivos().start();

		try {
			//Pegando IP interno do cliente
			InetAddress i = InetAddress.getLocalHost();
			this.IPCliente = i.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	public void conexaoServidor(int tipoConexao, String nomeCliente){
		try{

			this.nome = nomeCliente;
			//Editar IP do servidor
			Socket clientSocket = new Socket("192.168.1.105", 6789);

			DataOutputStream outToServer =
					new DataOutputStream(clientSocket.getOutputStream()); 

			BufferedReader inFromServer =
					new BufferedReader(new
							InputStreamReader(clientSocket.getInputStream()));

			outToServer.writeBytes(tipoConexao+","+nomeCliente+","+IPCliente+","+PORTACliente+"\n");

			String confirmacaoConexao = inFromServer.readLine();

			System.out.println(confirmacaoConexao);

			clientSocket.close();

		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void chatEnviaMensagemIndividual(TipoCliente clienteChat, String msg) throws IOException{

		try {

			DatagramSocket socket = new DatagramSocket();

			String mensagem = this.nome +","+ msg +"\n";

			byte [] data = mensagem.getBytes() ;

			InetAddress address = InetAddress.getByName(clienteChat.IP);
			DatagramPacket packet = new DatagramPacket( data, data.length, address, clienteChat.PORTA ) ;

			//Enviando mensagem
			socket.send( packet ) ;

			socket.close();

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void recebeArquivo(TipoCliente clienteArquivo){
		
	
		try{
			Socket clientSocket = new Socket(clienteArquivo.IP, 5454);
			DataOutputStream outToServer =
					new DataOutputStream(clientSocket.getOutputStream());

			outToServer.writeBytes("Ok" + '\n');

			try{
				System.out.println("Setando timeout do Cliente.");
				clientSocket.setSoTimeout(60000);
			}catch(Exception e){
				clientSocket.close();
				e.printStackTrace();
				return;
			}

			BufferedInputStream inputS = new BufferedInputStream(clientSocket.getInputStream());
			byte[] buffer = new byte[1024];    //If you handle larger data use a bigger buffer size
			int read;
			
			File f = new File("C:/arquivosBaixados/");
			if (!f.exists())
				f.mkdir();
			
			int i = 1;
			
			FileOutputStream fw = new FileOutputStream(f.toString()+"/Cliente_"+i);
			System.out.println("Aguardando dados do arquivo");
			while((read = inputS.read(buffer)) != -1) {
				fw.write(buffer, 0, read);
				System.out.println("Recebendo do servidor: "+i);
			}
			System.out.println("Arquivo Cliente_"+i+" salvo no diretório /arquivosBaixados/");
			fw.close();			
			clientSocket.close();

		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

}

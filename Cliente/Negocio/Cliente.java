package Negocio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
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
			Socket clientSocket = new Socket("192.168.1.103", 6789);

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

			try{
				clientSocket.setSoTimeout(60000);
			}catch(Exception e){
				clientSocket.close();
				e.printStackTrace();
				return;
			}

			String dirPath = "C:\\arquivosBaixados\\";

			BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream());
			DataInputStream dis = new DataInputStream(bis);

			int filesCount = dis.readInt();
			File[] files = new File[filesCount];

			for(int i = 0; i < filesCount; i++)
			{
				long fileLength = dis.readLong();
				String fileName = dis.readUTF();

				files[i] = new File(dirPath + "/" + fileName);

				FileOutputStream fos = new FileOutputStream(files[i]);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				for(int j = 0; j < fileLength; j++) bos.write(bis.read());

				bos.close();
			}

			dis.close();

			clientSocket.close();

		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

}

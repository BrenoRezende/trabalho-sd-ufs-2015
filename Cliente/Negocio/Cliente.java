package Negocio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
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
	public ThreadRecebeListaClientesOnline lClientes;
	public ThreadRecebeMensagensChat recebeMsg;
	
	public Cliente(){
		
		File f = new File("ClienteOnline.txt");
		if (!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			//Pegando IP interno do cliente
			InetAddress i = InetAddress.getLocalHost();
			this.IPCliente = i.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		lClientes = new ThreadRecebeListaClientesOnline();
		lClientes.start();
		recebeMsg = new ThreadRecebeMensagensChat();
		recebeMsg.start();
	}

	public void conexaoServidor(int tipoConexao, String nomeCliente){
		try{

			this.nome = nomeCliente;
			//Editar IP do servidor
			Socket clientSocket = new Socket("127.0.0.1", 6789);

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
	
	/*public void chatEnviaMensagemTodos(String msg) throws IOException{
		try {

			DatagramSocket socket = new DatagramSocket();
			
			String mensagem = this.nome +","+ msg +"\n";
			
			byte [] data = mensagem.getBytes() ;
			
			for (int i = 0; i < lClientes.listaClientes.size(); i++) {
				TipoCliente tc = lClientes.listaClientes.get(i);
				InetAddress address = InetAddress.getByName(tc.IP);
				DatagramPacket packet = new DatagramPacket( data, data.length, address, tc.PORTA ) ;
				
				//Enviando mensagem
				socket.send( packet ) ;
			}

			socket.close();

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}*/
}

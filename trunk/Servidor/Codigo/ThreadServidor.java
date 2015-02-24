package Codigo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.List;
import java.util.Scanner;

public class ThreadServidor extends Thread{

	  private Socket servidorSocket;
	  
	  public ThreadServidor(Socket servidorSocket) {
	    this.servidorSocket = servidorSocket; 
	  }

	  @Override
	  public void run(){
		  try{
			  
			  BufferedReader inFromClient =
					  new BufferedReader(new InputStreamReader(servidorSocket.getInputStream()));
			  
			  DataOutputStream outToClient =
					  new DataOutputStream(servidorSocket.getOutputStream());
			  
			  //Recebendo informações do cliente
			  String [] entrada = inFromClient.readLine().split(","); 
			  int opcao = Integer.parseInt(entrada[0]);
			  String nomeCliente = entrada[1];
			  String ipCliente = entrada[2];
			  int portaCliente = Integer.parseInt(entrada[3]);
			  
			  // Conectando cliente, se opcao == 1
			  if (opcao == 1){
				//Inserindo cliente na lista de clientes online
				  Cliente cliente = new Cliente();
				  cliente.nome = nomeCliente;
				  cliente.IP = ipCliente;
				  cliente.PORTA = portaCliente;
				  ListaCliente.listaClientes.add(cliente);
				  
				  //Respondendo ao cliente que a conexao foi efetuada com sucesso.
				  outToClient.writeBytes("Ok" + "\n");

				  System.out.println("Cliente conectado: "+ nomeCliente);
				  
			  // Desconectando cliente, se opcao == 0
			  } else if(opcao == 0){
				  for (int i = 0; i < ListaCliente.listaClientes.size(); i++) {
					  Cliente c = ListaCliente.listaClientes.get(i);
					  if (c.nome.equalsIgnoreCase(nomeCliente) && c.IP.equals(ipCliente)){
						  ListaCliente.listaClientes.remove(i);
						  System.out.println("Cliente desconectado: "+ nomeCliente);
						  break;
					  }
				  }
			  }
			  
			  // Envia lista de clientes online via multicast, para todos os clientes.
			  enviaListaClientesOnline();			  

		  }catch(Exception e){
			  System.out.println(e.getMessage());
		  }
	  }
	  
	  private void enviaListaClientesOnline(){
		  final int PORT = 8888;
		    
		    try {
		    	DatagramSocket socket = new DatagramSocket();
		    	
		    	String msg = "";
		    	
		    	//pegando lista de todos os clientes online
		    	for (int i = 0; i < ListaCliente.listaClientes.size(); i++) {
		    		Cliente c = ListaCliente.listaClientes.get(i);
		    		msg = msg + c.nome + "," + c.IP + "," + c.PORTA + ";";
				}
		    	
		        byte [] outBuf = msg.getBytes();
		 
		        //Enviando multicast
		        InetAddress address = InetAddress.getByName("224.2.2.3");
		        DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
		 
		        socket.send(outPacket);

		    } catch (IOException ioe) {
		      System.out.println(ioe);
		    }
	  }
}


package Negocio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class ThreadRecebeListaClientesOnline extends Thread{

	public ArrayList<TipoCliente> listaClientes;

	public ThreadRecebeListaClientesOnline() {
		listaClientes = new ArrayList<TipoCliente>();
	}

	@Override
	public void run(){
		byte[] inBuf = new byte[1024];    
		try {
			//Entrando no grupo multicast
			MulticastSocket socket = new MulticastSocket(8888);
			InetAddress address = InetAddress.getByName("224.2.2.3");
			socket.joinGroup(address);

			while(true){
				DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);

				//Como chegou uma nova lista de clientes, então a anterior é excluida
				listaClientes.clear();

				
				//Recebe lista de clientes no formato "nome,ip,porta;nome,ip,porta;..."
				String msg = new String(inBuf, 0, inPacket.getLength());
				String [] entrada = msg.split(";");

				for (int i = 0; i < entrada.length; i++) {
					String dados = entrada[i];
					String [] linha = dados.split(",");

					TipoCliente c = new TipoCliente();
					c.nome = linha[0];
					c.IP = linha[1];
					c.PORTA = Integer.parseInt(linha[2]);

					//Adiciona clientes a lista
					listaClientes.add(c);
				}

				for (int i = 0; i < listaClientes.size(); i++) {
					System.out.println(listaClientes.get(i).nome);
					System.out.println(listaClientes.get(i).IP);
				}
			}

		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

}

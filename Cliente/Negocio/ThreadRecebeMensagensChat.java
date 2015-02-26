package Negocio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Interface.PrincipalUI;

public class ThreadRecebeMensagensChat extends Thread {

	@Override
	public void run(){
	
			byte[] inBuf = new byte[1024];  
			DatagramSocket socket;
			try {
				socket = new DatagramSocket( 6666 );
				while(true){

					DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
		
					try {
						socket.receive(inPacket);
					} catch (IOException e) {
						e.printStackTrace();
					}


					//Recebe lista de clientes no formato "nome,ip,porta;nome,ip,porta;..."
					String msg = new String(inBuf, 0, inPacket.getLength());
					String [] entrada = msg.split(",");

					String nomeClienteEnviou = entrada[0];
					String mensagem = entrada[1];


					PrincipalUI.areaTexto.setText(PrincipalUI.areaTexto.getText() + "Mensagem rebida de "+nomeClienteEnviou+": "+mensagem+" \n");

				}
			} catch (SocketException e) {
				e.printStackTrace();
			}

		
	}
}

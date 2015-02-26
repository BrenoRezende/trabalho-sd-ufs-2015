package Negocio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

import Interface.PrincipalUI;

public class ThreadRecebeListaClientesOnline extends Thread{

	@Override
	public void run(){
		byte[] inBuf = new byte[1024];    
		try {
			//Entrando no grupo multicast
			MulticastSocket socket = new MulticastSocket(8888);
			InetAddress address = InetAddress.getByName("239.0.0.123");
			socket.joinGroup(address);
			
			File f = new File("ClienteOnline.txt");
			if (f.exists()){
				f.delete();
			}
			f.createNewFile();

			while(true){
				DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);

				
				//Recebe lista de clientes no formato "nome,ip,porta;nome,ip,porta;..."
				String msg = new String(inBuf, 0, inPacket.getLength());
				String [] entrada = msg.split(";");

				
				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				System.out.println("Gravando arquivo.");
				for (int i = 0; i < entrada.length; i++) {
					String dados = entrada[i];
					String [] linha = dados.split(",");
					System.out.println("Gravou: "+dados);
					//Grava clientes no arquivo
					bw.write(dados);
					bw.newLine();
				}
				
				bw.close();
				fw.close();
				
				//Atualiza clientes online no ComboBox
				PrincipalUI.atualizaComboBox();

			}

		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

}

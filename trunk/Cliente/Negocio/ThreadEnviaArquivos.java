package Negocio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

				try {
					connectionSocket.setSoTimeout(60000);
				} catch (Exception e) {
					connectionSocket.close();
					e.printStackTrace();
					return;
				}

				String directory = "C:\\arquivos";

				File[] files = new File(directory).listFiles();

				BufferedOutputStream bos = new BufferedOutputStream(connectionSocket.getOutputStream());
				DataOutputStream dos = new DataOutputStream(bos);

				dos.writeInt(files.length);

				for(File file : files)
				{
					long length = file.length();
					dos.writeLong(length);

					String name = file.getName();
					dos.writeUTF(name);

					FileInputStream fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis);

					int theByte = 0;
					while((theByte = bis.read()) != -1) bos.write(theByte);

					bis.close();
				}

				dos.close();

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}

}

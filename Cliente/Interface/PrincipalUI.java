package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Codigo.ListaCliente;
import Negocio.*;

public class PrincipalUI extends JFrame {

	static Vector comboBoxItems = new Vector();

	private JButton btObterArquivos, btOn, btOff, btEnviar;

	private JTextField tfTextoEnviar, tfNome;

	private JLabel lbNome, lbClientesOn;

	private JComboBox comboListaClienteOn;

	public static JTextArea areaTexto;

	private JScrollPane scroll;

	Cliente c;

	public PrincipalUI() {

		super("Chat Plus");

		comboBoxItems.add("Todos");
		c = new Cliente();

		try {
			atualizaComboBox();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setLayout(null);

		btOn = new JButton("ON");
		btOn.setBounds(650, 20, 100, 30);
		add(btOn);

		btOff = new JButton("OFF");
		btOff.setBounds(770, 20, 100, 30);
		btOff.setEnabled(false);
		add(btOff);

		lbNome = new JLabel("Nome:");
		lbNome.setBounds(20, 20, 100, 30);
		add(lbNome);

		tfNome = new JTextField();
		tfNome.setBounds(120, 20, 500, 30);
		add(tfNome);

		lbClientesOn = new JLabel("Pessoas On:");
		lbClientesOn.setBounds(20, 80, 100, 30);
		add(lbClientesOn);

		final DefaultComboBoxModel model = new DefaultComboBoxModel(comboBoxItems);
		comboListaClienteOn = new JComboBox(model);
		comboListaClienteOn.setBounds(120, 80, 500, 30);
		add(comboListaClienteOn);

		btObterArquivos = new JButton("Obter Arquivos");
		btObterArquivos.setBounds(650, 80, 220, 30);
		add(btObterArquivos);

		areaTexto = new JTextArea("", 0, 0);
		areaTexto.setLineWrap(true); 
		areaTexto.setEditable(false); 

		scroll = new JScrollPane(areaTexto);
		scroll.setBounds(20, 140, 850, 300);
		add(scroll);

		tfTextoEnviar = new JTextField();
		tfTextoEnviar.setBounds(20, 470, 740, 30);
		add(tfTextoEnviar);

		btEnviar = new JButton("Enviar");
		btEnviar.setBounds(770, 470, 100, 30);
		add(btEnviar);

		/*
		tfPalavra = new JTextField("");
		tfPalavra.setBounds(347, 425, 150, 30);
		add(tfPalavra);

		btTestarPalavra = new JButton("Validar");
		btTestarPalavra.setBounds(507, 425, 100, 30);
		btTestarPalavra.setForeground(Color.black);
		add(btTestarPalavra);

		lbTitulo = new JLabel("Simulador de Autômato Finito Não-Determinístico");
		lbTitulo.setBounds(19, 8, 1000, 35);
		lbTitulo.setFont(new java.awt.Font("Tahoma", 0, 20));
		lbTitulo.setForeground(Color.WHITE);
		add(lbTitulo);

		//btSair = new JButton(new ImageIcon(getClass().getResource("/img/btSair1.png")));	
		btSair = new JButton("Sair");
		//btSair.setContentAreaFilled(false);
		btSair.setBounds(805, 8, 50, 35);
		add(btSair);

		lbQtEstados = new JLabel("Quantidade de Estados");
		lbQtEstados.setBounds(30, 80, 160, 35);
		lbQtEstados.setFont(new java.awt.Font("Tahoma", 0, 15));
		lbQtEstados.setForeground(Color.WHITE);
		add(lbQtEstados);

		tfQntEstados = new JTextField("3");
		tfQntEstados.setBounds(190, 82, 90, 30);
		add(tfQntEstados);

		lbEstadoIni = new JLabel("Estado Inicial");
		lbEstadoIni.setBounds(342, 80, 160, 35);
		lbEstadoIni.setFont(new java.awt.Font("Tahoma", 0, 15));
		lbEstadoIni.setForeground(Color.WHITE);
		add(lbEstadoIni);

		tfEstadoIni = new JTextField("q0");
		tfEstadoIni.setBounds(436, 82, 90, 30);
		add(tfEstadoIni);

		lbAlfabeto = new JLabel("Letras do Alfabeto");
		lbAlfabeto.setBounds(62, 115, 160, 35);
		lbAlfabeto.setFont(new java.awt.Font("Tahoma", 0, 15));
		lbAlfabeto.setForeground(Color.WHITE);
		add(lbAlfabeto); */

		acoesBotoes();

		setSize(900, 555);
		setResizable(false);
		//setUndecorated(true);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void acoesBotoes() {

		btOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btOn.setEnabled(false);
				btOff.setEnabled(true);

				c.conexaoServidor(1, tfNome.getText());
			}
		});

		btOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btOn.setEnabled(true);
				btOff.setEnabled(false);

				c.conexaoServidor(0, tfNome.getText());
			}
		});

		btEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				File f = new File("ClienteOnline.txt");
				FileReader fr;

				String forWho = comboListaClienteOn.getSelectedItem().toString();
				String msg = tfTextoEnviar.getText();

				if (forWho.equalsIgnoreCase("Todos")) {

					try {
						fr = new FileReader(f);
						BufferedReader br = new BufferedReader(fr);
						while(br.ready()){
							String[]linha = br.readLine().split(",");

							TipoCliente tc = new TipoCliente();
							tc.nome = linha[0];
							tc.IP = linha[1];
							tc.PORTA = Integer.parseInt(linha[2]);

							try {
								c.chatEnviaMensagemIndividual(tc, msg);
							} catch (IOException e1) {
								e1.printStackTrace();
							}							
						}


					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}else{					

					try {
						c.chatEnviaMensagemIndividual(buscaCliente(forWho), msg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		});

		btObterArquivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String cliente = comboListaClienteOn.getSelectedItem().toString();

				if (cliente.equalsIgnoreCase("Todos")) {
					try {
						File f = new File("ClienteOnline.txt");
						FileReader fr = new FileReader(f);
						BufferedReader br = new BufferedReader(fr);
						while(br.ready()){
							String[]linha = br.readLine().split(",");

							TipoCliente tc = new TipoCliente();
							tc.nome = linha[0];
							tc.IP = linha[1];
							tc.PORTA = Integer.parseInt(linha[2]);

							c.recebeArquivo(tc);

						}


					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}else{
					c.recebeArquivo(buscaCliente(cliente));
				}
			}
		});
	}


	public TipoCliente buscaCliente(String nomeCliente){
		File f = new File("ClienteOnline.txt");
		FileReader fr;

		TipoCliente tc = new TipoCliente();
		try {
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			while(br.ready()){
				String[]linha = br.readLine().split(",");
				if(nomeCliente.equalsIgnoreCase(linha[0])){

					tc.nome = linha[0];
					tc.IP = linha[1];
					tc.PORTA = Integer.parseInt(linha[2]);

					break;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return tc;
	}

	public static void atualizaComboBox() throws IOException{
		File f = new File("ClienteOnline.txt");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);

		comboBoxItems.clear();
		comboBoxItems.add("Todos");

		while(br.ready()){
			String[]linha = br.readLine().split(",");
			comboBoxItems.add(linha[0]);
		}

	}

	public static void main(String[] args) {
		File f = new File("ClienteOnline.txt");
		if (f.exists()){
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		new PrincipalUI();
	}

}

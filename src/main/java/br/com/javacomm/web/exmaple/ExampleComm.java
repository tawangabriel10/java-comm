package br.com.javacomm.web.exmaple;

import java.util.Enumeration;
import java.util.HashMap;

import javax.comm.CommIdentifier;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

public class ExampleComm {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static void getPortSerialSpecific() {
		int timeout = 1000; // Tempo de espera.
		String tipoPorta = "/dev/ttyS1"; //Porta serial no linux Fedora
		CommIdentifier cp = CommPortIdentifier.getPortIdentifier(tipoPorta);
		SerialPort porta = (SerialPort)cp.open(“titulo”, timeout);
	}
	
	private static void getPortsList() {
		// captura a lista de portas disponíveis,
		// pelo método estético em CommPortIdentifier.
		Enumeration<E> pList = CommPortIdentifier.getPortIdentifiers();
		// Um mapping de nomes para CommPortIdentifiers.
		HashMap<String, CommPortIdentifier> map = new HashMap();
		// Procura pela porta desejada
		while (pList.hasMoreElements()) {
		        CommPortIdentifier cpi = (CommPortIdentifier)pList.nextElement();
		        map.put(cpi.getName(), cpi);
		        if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
		                // fazer alguma tarefa
		        }
		}
	}
}

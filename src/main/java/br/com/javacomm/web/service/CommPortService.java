package br.com.javacomm.web.service;

import org.springframework.stereotype.Service;

import javax.comm.CommIdentifier;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

@Service
public class CommPortService {
	
	private int comando;

	
	// Abre a porta para comunicação

	public void abrePortaCom() {
		/*
		* Para a porta ser aberta precisa ter um identificador do tipo da porta, para isso usa-se o método getPortIdentifier("Qual é a porta? COM1, LPT1 ...") da classe CommPortIdentifier.
		* 
		* Tendo feito isso há a necessidade, agora, de instanciar o tipo da porta que foi escolhida, nesse caso a porta serial a qual recebe o identificador juntamente com o método open() que recebe como parâmetro um nome identificador (um título) e o tempo de espera de alguma resposta (timeout).
		* 
		* Estando a porta aberta, configura-se os parâmetros de comunicação serial. 
		*/
	
		try{
			CommIdentifier idPorta = CommPortIdentifier.getPortIdentifier(tipoPorta);
			SerialPort portaSerial = (SerialPort)idPorta.open("PlacaSensoriamento",timeout);
			JOptionPane.showMessageDialog(null, "Configurando Porta Serial");
			portaSerial.setSerialPortParams(baudrate, portaSerial.DATABITS_8, portaSerial.STOPBITS_1, portaSerial.PARITY_NONE);
		}
	
		catch (NoSuchPortException nspe){
		   System.err.println("Posta não existe! : " + tipoPorta);
		}
	
		catch (PortInUseException piu){
		   System.err.println("Porta ja esta aberta!");
		}
	
		catch (UnsupportedCommOperationException uscoe){
		   System.err.println("Configuração dos parametros da porta não suportada!");
		}
	}
	
	/*
	* Ler dados é um pouco mais complicado! passos a serem seguidos
	* 
	* - Criar um fluxo de entrada.
	* - Adicionar um gerenciador de eventos para dados na porta serial.
	* - Instanciar uma Thread para aguardar os eventos.
	* - Tratar os eventos e receber os dados.
	*/

	public void iniciaLeitura(){
	   try{
	      portaEntrada = portaSerial.getInputStream();
	      portaSerial.addEventListener(this);
	      // Agora tem-se uma notificação de dados disponível

	      portaSerial.notifyOnDataAvailable(true);
	      // Threads responsíveis por aguardar os eventos

	      thread = new Thread(this);
	      thread.start();
	   }

	    catch (IOException ioe){
	       System.err.println("Erro de comunicação com a porta!");
	    }

	    catch (TooManyListenersException tmle){
	       System.err.println("Erro. Muitos métodos ouvintes na porta!");
	    }

	    catch (Exception e){
	        System.out.println("Erro ao iniciar leitura com a thread!");
	    }

	}

	// Método que envia um bit para a porta serial
	public void EnviarUmaString(int comando){

	   this.comando = comando;
	   try {
	      saida = portaSerial.getOutputStream();
	      saida.write(comando);
	      Thread.sleep(100);
	      saida.flush();
	   } catch (IOException ioe){
	      System.out.println("Não foi possivel abrir/enviar_comando na porta serial");
	     System.out.println("Erro! STATUS: " + ioe );
	   } catch (InterruptedException ie){
	      System.out.println("Problema com as Threads");
	      System.out.println("Erro: Status: " + ie);
	   }
	}
	
	// Gerenciador de eventos na porta serial

	public void serialEvent(SerialPortEvent spe){
	   switch( spe.getEventType()){
	      case SerialPortEvent.BI:
	      case SerialPortEvent.OE:
	      case SerialPortEvent.FE:
	      case SerialPortEvent.PE:
	      case SerialPortEvent.CD:
	      case SerialPortEvent.CTS:
	      case SerialPortEvent.DSR:
	      case SerialPortEvent.RI:
	      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
	      break;
	      case SerialPortEvent.DATA_AVAILABLE:

	   try{
	      while ( portaEntrada.available() > 0 ) {
	         tmp = Integer.toHexString(portaEntrada.read());
	         st = st + tmp;
	      }
	      System.out.println(" > " + st);
	   }

	   catch(Exception ioe){
	      System.err.println("Erro de Leitura!");
	      ioe.printStackTrace();
	   }
}

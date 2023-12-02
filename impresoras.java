package Impresora;
import java.util.concurrent.Semaphore; //Libreria para el uso de semaforos

public class impresoras{
	static Semaphore impresora = new Semaphore(0); //Declaracion del semaforo impresora
	static Semaphore documento = new Semaphore(0); //Declaracion del semaforo documento 
	static Semaphore mutex = new Semaphore(1);   //Declaracion del semaforo mutex

	//Declaración de variables enteras
	static int documentosEnCola = 3; //docuentos en la cola
	static int documentosRestantes = 3;  //docuentos disponibles
	static int documentoEsperando = 0; //documentos en espera

	//Se define la función main(), y aqui es donde se van a crear e iniciar los hilos de los barberos y clientes

	public static void main(String[] args) throws InterruptedException{

		Thread ImpresoraThread1 = new Thread(new Impresora());
		Thread DocumentoThread1 = new Thread(new Documento());
		Thread DocumentoThread2 = new Thread(new Documento());
		Thread DocumentoThread3 = new Thread(new Documento());
		Thread DocumentoThread4 = new Thread(new Documento());

		ImpresoraThread1.start();
		DocumentoThread1.start();
		DocumentoThread2.start();
		DocumentoThread3.start();
		DocumentoThread4.start();
	}

	// Se define una clase Cliente para implementar la lógica de los cliente que llegan 
	static class Documento implements Runnable{
		@Override
	public void run(){
		try{
			/*acquire bloquea la ejecución del hilo en curso y queda a la espera de
			que otro hilo llame a release() en este caso se encuentran más abajode código esperando
			documento y mutrx para que despues el impresora bloque nuevamente*/

			mutex.acquire();
			System.out.println("Documentos Restantes: "+documentosRestantes);
			documentosRestantes--;
			if (documentoEsperando < documentosEnCola) {
				documentoEsperando++;
				System.out.println("Docuento en cola"); //Mensaje que indica que un documento esta esperando su turno
				documento.release();
				mutex.release();
				impresora.acquire();
				imprimir();
			}else{
				mutex.release();
				System.out.println("No hay espacio en la cola para el documento");
			}
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void imprimir() throws InterruptedException{
		System.out.println("Documento se esta imprimiendo"); //Mensaje de que un documento esta en ejecución de su corte
		}
	}

	//Se define la clase Impresora que implementa la logica del barbero y que este se quede dormido hasta que llegue un nuevo documento
	static class Impresora implements Runnable{
		@Override
		public void run(){
			while(true){
				try{
					documento.acquire();
					mutex.acquire();
					documentoEsperando--;
					impresora.release();
					mutex.release();
					imprimir();
				} catch (InterruptedException e){
			}
		}
	}

	private void imprimir() throws InterruptedException{
		System.out.println("La impresora se encuentra imprimiendo");
		}	
	}
}

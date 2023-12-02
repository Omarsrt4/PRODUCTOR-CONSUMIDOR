package Barberos;
import java.util.concurrent.Semaphore; //Libreria para el uso de semaforos

public class barberoDormilon{
	static Semaphore barbero = new Semaphore(0); //Declaracion del semaforo barbero
	static Semaphore cliente = new Semaphore(0); //Declaracion del semaforo cliente
	static Semaphore mutex = new Semaphore(1);   //Declaracion del semaforo mutex

	//Declaración de variables enteras
	static int sillaEspera = 3; //Sillas en espera
	static int sillasDis = 3;  //Sillas disponibles
	static int clientesEsperando = 0; //Clientes en espera

	//Se define la función main(), y aqui es donde se van a crear e iniciar los hilos de los barberos y clientes

	public static void main(String[] args) throws InterruptedException{

		Thread barberoThread1 = new Thread(new Barbero());
		//Thread barberoThread2 = new Thread(new Barbero()); → se pueden tener mpas barberos
		Thread clienteThread1 = new Thread(new Cliente());
		Thread clienteThread2 = new Thread(new Cliente());
		Thread clienteThread3 = new Thread(new Cliente());
		Thread clienteThread4 = new Thread(new Cliente());

		barberoThread1.start();

		clienteThread1.start();
		clienteThread2.start();
		clienteThread3.start();
		clienteThread4.start();
	}

	// Se define una clase Cliente para implementar la lógica de los cliente que llegan 
	static class Cliente implements Runnable{
		@Override
	public void run(){
		try{
			/*acquire bloquea la ejecución del hilo en curso y queda a la espera de
			que otro hilo llame a release() en este caso se encuentran más abajode código esperando
			cliente y mutrx para que despues el barbero bloque nuevamente*/

			mutex.acquire();
			System.out.println("Sillas Disponibles: "+sillasDis);
			sillasDis--;
			if (clientesEsperando < sillaEspera) {
				clientesEsperando++;
				System.out.println("Cliente esperando"); //Mensaje que indica que un cliente esta esperando su turno
				cliente.release();
				mutex.release();
				barbero.acquire();
				cortarPelo();
			}else{
				mutex.release();
				System.out.println("No hay espacio en las sillas para el cliente");
			}
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void cortarPelo() throws InterruptedException{
		System.out.println("Cliente recibiendo corte"); //Mensaje de que un cliente esta en ejecución de su corte
		}
	}

	//Se define la clase Barbero que implementa la logica del barbero y que este se quede dormido hasta que llegue un nuevo cliente
	static class Barbero implements Runnable{
		@Override
		public void run(){
			while(true){
				try{
					cliente.acquire();
					mutex.acquire();
					clientesEsperando--;
					barbero.release();
					mutex.release();
					cortarPelo();
				} catch (InterruptedException e){
			}
		}
	}

	private void cortarPelo() throws InterruptedException{
		System.out.println("Barbero cortando el pelo al cliente");
		}	
	}
}
#include <stdio.h>
#include <pthread.h>
#include <unistd.h>

#define NUM_HILOS 10  //Cantidad de hilos que se van a usar

//Se declara un mutex y una variable de condicion, ambos se inicializan
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

//Se declaran dos contadores, uno para lectores y otro general, inicializando en 0
int contador = 0;
int lectores = 0;

//Esta es la función que se ejecutará en los hilos lectores
void *lector(void *arg){
	//Adquiere el mutex
	pthread_mutex_lock(&mutex);
	//Incrementa en 1 el contador de lectores
	lectores++;
	//Libera al mutex usado anteriormente
	pthread_mutex_unlock(&mutex);

	//Imprime el mensaje
	printf("Leyendo\n");
	//sleep(1);

	//Adquiere el mutex
	pthread_mutex_lock(&mutex);
	//Decrementa en 1 el contador de lectores
	lectores--;

	//Señala cualquier hilo en espera en la variable de condición
	pthread_cond_signal(&cond);
	//Libera el mutex
	pthread_mutex_unlock(&mutex);
}

//Esta es la función que se ejecutará en los hilos escirtores
void *escritor(void *arg){
	//Adquiere el mutex
	pthread_mutex_lock(&mutex);

	//El while se encargará de que mientras haya lectores, esta se espera en la variable de condición
	while (lectores > 0){
	pthread_cond_wait(&cond, &mutex);
	}
	printf("Escribiendo\n");

//	sleep(1);
	//Libera el mutex
	pthread_mutex_unlock(&mutex);
}

int main() {
	//Se declara una matriz de hilos
	pthread_t hilos[NUM_HILOS];

	//Crea y ejecuta cada hilo
	for (int i = 0; i < NUM_HILOS; i++){
		if (i % 2 == 0){
			//Si i es par se va a crear un hilo lector
			pthread_create(&hilos[i], NULL, lector, NULL);
		}else{
			//Si i es par se va a crear un hilo escritor
			pthread_create(&hilos[i], NULL, escritor, NULL);
		}
	}
	//Espera a que todos los hilos terminen 
	for (int i = 0; i < NUM_HILOS; i++){
		//Funcion que espera a que un hilo en especifico termine
		pthread_join(hilos[i], NULL);
	}

	return 0;
}
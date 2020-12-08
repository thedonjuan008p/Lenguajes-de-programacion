#include <stdlib.h>
#include <stddef.h>
#include <stdio.h>
#include <string.h>
#include "graph.h"


int main(int argc,char** argv){
    FILE *archivo = NULL;
    char* name = malloc(sizeof(char)*100);
    char* web = malloc(sizeof(char)*100);
    const char* r_name = argv[2];
    const char* t_name = argv[3];

    int N, cVecinos, iN, ne = 0; //N° de nodos, N° de vecinos, id del nodo que apunta, N° de arco
    float weight;                //todos menos N son asignaciones relativas (van cambiando)


    archivo = fopen(argv[1], "r+");
    fscanf(archivo, "%d\n",&N);
    edge* E = malloc(sizeof(edge)*N*(N-1)); //N*(N-1) cantidad de arcos maxima en un grafo dirigido
    graph* G = new_graph(N);
    for (int i = 0; i < N; i++){
        fscanf(archivo, "%s %s %f %d\n",name,web,&weight,&cVecinos); 
        addNodeGraph(G,i,name,web,weight);   //Añade al grafo el nodo i con sus atributos
        for (int j = 0; j < cVecinos ; j++){ //Este recorrido depende del N° de vecinos del nodo
            fscanf(archivo, "%d %f\n", &iN, &weight); 
            new_edge(i,iN,weight,E,&ne); //args: inicio, destino, peso, Arcos, N° de arco
        }
    }
    addEdgesGraph(G,E,ne);               //Añade los arcos al grafo --args: Grafo, Arcos, N° Arcos Totales
    printGraph(G);
    int r = findId(G,r_name);            //Esta función busca el id en el grafo, debido a que si lo busca dentro de la funcion
    int t = findId(G,t_name);            //Probability lo hara varias veces y nos ayuda en computar las excepciones.
    int flag = (r == -1 || t == -1); 
    if (flag) printf("Es imposible calcular la probabilidad\n");
    else{
        float prob = Probability(G,r,t)*100;
        printf("La probabilidad de visitar %s, iniciando la navegación en %s, es igual a %3.2f%s\n",t_name,r_name,prob,"%");
    }
    

    free(name);
    free(web);
    free(E);
    freeGraph(G);
    fclose(archivo);
    return 0;
}

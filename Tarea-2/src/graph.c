#include <stdlib.h>
#include <stddef.h>
#include <stdio.h>
#include <string.h>
#include "graph.h"


float Probability(graph* G, int r,int t){

    if (r == t){
        return 1;
    }
    int N = G->N;

    G->visited[r] = 1;
    float prob = 0;

    for (int n = 0; n < N; n++){
        if (!G->visited[n] && r != n && G->head[r]->next[n] != NULL){  //Las ultimas dos condiciones equivalen a que son los nodos siguientes de r
            float p = G->weights[r][n];
            float prob_out = G->head[r]->prob;
            prob = prob + prob_out*p*Probability(G, n, t);
        }
    }
    
    G->visited[r] = 0;
    return prob;
}

int findId(graph* G,const char* nodo_name){
    int N = G->N, r = -1;
    
    // buscar id de los nodos.
    for (int i = 0; i < N; i++){
        const char* name = G->head[i]->name;
        if (strcmp(name , nodo_name)==0){
            r = i;
        }
    }

    if (r == -1){
        printf("El sitio %s no está dentro del archivo\n",nodo_name);
    }

    return r;
}

graph *new_graph(int N){
    graph *G = (graph *)malloc(sizeof(graph));
    G->N = N;

    G->visited = malloc(sizeof(int)*N);
    for (int i = 0; i < N; i++){
        G->visited[i] = 0;
    }

    G->weights = malloc(N*sizeof(float*));
    for (int i = 0; i < N;i++){
        G->weights[i] = malloc(N*sizeof(float));
    }

    G->head = malloc(sizeof(node)*N);
    for (int i = 0; i < N; i++){
        G->head[i] = NULL;
        }
        
    return G;
}

void addEdgesGraph(graph* G, edge *Edge, int n){
    for (int i = 0; i < n; i++){
        int src = Edge[i].src;
        int dest = Edge[i].dest;
        float weight = Edge[i].weight;
        G->weights[src][dest] = weight;
        G->head[src]->next[dest] = G->head[dest];
        }
}
void addNodeGraph(graph* G, int i, char* name, char* web, float weight){
    int N = G->N;
    node* newNode = malloc(sizeof(node));
    newNode->name = copy(name);
    newNode->web = copy(web);
    newNode->prob = weight;
    newNode->next = malloc(sizeof(node)*N-1);
    for (int i = 0; i < N; i++){
        newNode->next[i] = NULL;
    }
    G->head[i] = newNode;
}

void printGraph(graph* G)
{
    int N = G->N;
    int i;
    for (i = 0; i < N; i++)
    {
        printf("name: %s\nweb: %s\n",G->head[i]->name,G->head[i]->web);
        for (int j = 0; j < N; j++){
            if (j!=i && G->head[i]->next[j] != NULL){
                printf("--> %s\n", G->head[j]->name);
            }
        }
        printf("\n");
    }
}

void new_edge(int src, int dest, float weight, edge* E,int* n){
    edge temp_edge;
    temp_edge.dest = dest;
    temp_edge.src = src;
    temp_edge.weight = weight;
    E[*n] = temp_edge;
    *n=*n+1;
}

void freeGraph(graph* G){
    int N = G->N;
    free(G->visited);
    for (int i = 0; i < N; i++){
        free(G->head[i]->name);
        free(G->head[i]->web);
        free(G->head[i]->next);
        free(G->head[i]);
        free(G->weights[i]);
    }
    free(G->head);
    free(G->weights);
    free(G);
}

char* copy(char *s)
{
    char *a = malloc(strlen(s)+1);
    if (a == NULL)
    {
        perror( "malloc failed" );
        return NULL;
    }

    char *c = a;
    while( *s != '\0' )
    {
        *c = *s;
        s++;
        c++;
    }

    *c = '\0';
    return a;
}
#ifndef GRAPH_H
#define GRAPH_H

typedef struct Nodo{
    char* name;
    char* web;
    float prob;
    struct Nodo** next;
} node;

typedef struct Grafo
{
    int N;
    int* visited;
    node** head;
    float** weights;
} graph;

typedef struct Edge
{
    int src;
    int dest;
    float weight;
}edge;

char* copy(char *);
void new_edge(int , int, float, edge*, int*);
graph *new_graph(int );
void addNodeGraph(graph* , int , char* , char* , float);
void addEdgesGraph(graph*, edge *, int );
void printGraph(graph*);
void freeGraph(graph*);
float Probability(graph*, int,int);
int findId(graph*, const char*);

#endif
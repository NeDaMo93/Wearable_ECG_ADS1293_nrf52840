#ifndef QUEUE_H
#define QUEUE_H

#include <stdint.h>

typedef struct Node {
    uint32_t data;
    struct Node* next;
} Node;

typedef struct Queue {
    Node *front, *rear;
    int size;
} Queue;

Queue* createQueue();
void enQueue(Queue* q, uint32_t k);
uint32_t deQueue(Queue* q);
uint32_t calculateMean(Queue* q);
void emptyQueue(Queue* q);
uint32_t sizeQueue(Queue* q);

#endif // QUEUE_H

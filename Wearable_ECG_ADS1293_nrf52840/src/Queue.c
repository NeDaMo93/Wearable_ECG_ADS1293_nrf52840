#include <stdlib.h>
#include "Queue.h"


Queue* createQueue() {
    Queue* q = (Queue*)malloc(sizeof(Queue));
    q->front = q->rear = NULL;
    q->size = 0;
    return q;
}

static Node* newNode(uint32_t k) {
    Node* temp = (Node*)malloc(sizeof(Node));
    temp->data = k;
    temp->next = NULL;
    return temp;
}

void enQueue(Queue* q, uint32_t k) {
    Node* temp = newNode(k);
    if (q->rear == NULL) {
        q->front = q->rear = temp;
        q->size = 1;
        return;
    }
    q->rear->next = temp;
    q->rear = temp;
    q->size++;
}

uint32_t deQueue(Queue* q) {
    if (q->front == NULL)
        return UINT32_MAX;

    Node* temp = q->front;
    uint32_t item = temp->data;

    q->front = q->front->next;
    if (q->front == NULL)
        q->rear = NULL;

    free(temp);
    q->size--;

    return item;
}
void emptyQueue(Queue* q) {
    while (q->size > 1) {
        deQueue(q);
    }
    //q->size = 0;
}

uint32_t calculateMean(Queue* q) {
    if (q->size == 0)
        return 0; // return 0 if queue is empty

    Node* temp = q->front;
    uint64_t sum = 0; // using uint64_t to avoid overflow

    while (temp != NULL) {
        sum += temp->data;
        temp = temp->next;
    }

    return sum / q->size; // Division by size will floor the mean value
}
uint32_t sizeQueue(Queue* q) {
    return q->size;
}


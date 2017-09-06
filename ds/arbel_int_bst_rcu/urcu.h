/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   urcu.h
 * Author: Maya Arbel-Raviv
 *
 * Created on June 10, 2017, 12:07 PM
 */

#ifndef URCU_H
#define URCU_H

typedef struct rcu_node_t {
    char p1[128];
    volatile unsigned long time; 
    volatile uint64_t val1;
    volatile uint64_t val2;
    char p2[128-sizeof(time)- 2*sizeof(uint64_t)];
} rcu_node;

typedef struct predicate_info_t * predicate_info;
typedef bool (*predicate)(predicate_info, uint64_t, uint64_t);

namespace urcu {

void init(const int numThreads);
void deinit(const int numThreads);
void readLock(uint64_t val1, uint64_t val2);
void readUnlock();
void synchronize(predicate pred, predicate_info pred_info); 
void registerThread(int id);
void unregisterThread();

}

#endif /* URCU_H */


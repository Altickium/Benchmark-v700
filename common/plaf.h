/**
 * C++ record manager implementation (PODC 2015) by Trevor Brown.
 * 
 * Copyright (C) 2015 Trevor Brown
 *
 */

#ifndef MACHINECONSTANTS_H
#define	MACHINECONSTANTS_H

#ifndef MAX_THREADS_POW2
    #define MAX_THREADS_POW2 128 // MUST BE A POWER OF TWO, since this is used for some bitwise operations
#endif
#ifndef LOGICAL_PROCESSORS
    #define LOGICAL_PROCESSORS MAX_THREADS_POW2
#endif

// the following definition is only used to pad data to avoid false sharing.
// although the number of words per cache line is actually 8, we inflate this
// figure to counteract the effects of prefetching multiple adjacent cache lines.
#define PREFETCH_SIZE_WORDS 24
#define PREFETCH_SIZE_BYTES 192
#define BYTES_IN_CACHE_LINE 64

#define CAT2(x, y) x##y
#define CAT(x, y) CAT2(x, y)

#define PAD64 volatile char CAT(___padding, __COUNTER__)[64]
#define PAD volatile char CAT(___padding, __COUNTER__)[128]

#endif	/* MACHINECONSTANTS_H */

#ifndef HIST_TREE_ADAPTER_H
#define HIST_TREE_ADAPTER_H

#include <iostream>
#include "errors.h"
#include "record_manager.h"
#ifdef USE_TREE_STATS
#   include "tree_stats.h"
#endif

#include "CHT/include/cht/cht.h"
#include "CHT/include/cht/builder.h"

#define RECORD_MANAGER_T record_manager<Reclaim, Alloc, Pool, int>
#define DATA_STRUCTURE_T cht::CompactHistTree<K>

template <typename K, typename V, class Reclaim = reclaimer_debra<K>, class Alloc = allocator_new<K>, class Pool = pool_none<K>>
class ds_adapter {
private:
    const V NO_VALUE;
    DATA_STRUCTURE_T * const ds;
public:
    ds_adapter(const int NUM_THREADS,
               const K& KEY_MIN,
               const K& KEY_MAX,
               const V& VALUE_RESERVED,
               Random64 * const unused2)
            : NO_VALUE(VALUE_RESERVED)
            , ds(new DATA_STRUCTURE_T())
    {
        std::vector<KeyType> keys(1e6);
        generate(keys.begin(), keys.end(), rand);
        keys.push_back(424242);
        std::sort(keys.begin(), keys.end());

        const unsigned numBins = 64; // each node will have 64 separate bins
        const unsigned maxError = 4; // the error of the index
        cht::Builder<KeyType> chtb(KEY_MIN, KEY_MAX, numBins, maxError, false, useCache);
        for (const auto& key : keys) chtb.AddKey(key);
        ds = &chtb.Finalize();
    }

    ~ds_adapter() {
        delete ds;
    }

    V getNoValue() {
        return NO_VALUE;
    }

    void initThread(const int tid) {
        // do nothing
    }

    void deinitThread(const int tid) {
        // do nothing
    }

    bool contains(const int tid, const K& key) {
        auto it = ds->find(key);
        if (it != ds->end()) {
            return false;
        }
        return true;
    }

    V insert(const int tid, const K& key, const V& val) {
        return NO_VALUE; // not supported
    }

    V insertIfAbsent(const int tid, const K& key, const V& val) {
        return NO_VALUE; // not supported
    }

    V erase(const int tid, const K& key) {
        return NO_VALUE; // not supported
    }

    V find(const int tid, const K& key) {
        auto it = ds->find(key);
        if (it != ds->end()) {
            return (*it).second;
        }
        return NO_VALUE; // fail;
    }

    int rangeQuery(const int tid, const K& lo, const K& hi, K * const resultKeys, V * const resultValues) {
        for (auto it = ds->lower_bound(lo); it != ds->lower_bound(hi); it++) {
            //something
        }
        return 0;
    }

    void printSummary() {
        auto stats = ds->get_stats();
        std::cout << "Final num keys: " << stats.num_keys
                << std::endl;  // expected: 199
        std::cout << "Num inserts: " << stats.num_inserts
                << std::endl;  // expected: 109
    }

    bool validateStructure() {
        return true;
    }

    void printObjectSizes() {
    }

    void debugGCSingleThreaded() {}

#ifdef USE_TREE_STATS
    class NodeHandler {
    public:
        typedef int * NodePtrType;

        NodeHandler(const K& _minKey, const K& _maxKey) {}

        class ChildIterator {
        public:
            ChildIterator(NodePtrType _node) {}
            bool hasNext() {
                return false;
            }
            NodePtrType next() {
                return NULL;
            }
        };

        bool isLeaf(NodePtrType node) {
            return false;
        }
        size_t getNumChildren(NodePtrType node) {
            return 0;
        }
        size_t getNumKeys(NodePtrType node) {
            return 0;
        }
        size_t getSumOfKeys(NodePtrType node) {
            return 0;
        }
        ChildIterator getChildIterator(NodePtrType node) {
            return ChildIterator(node);
        }
    };
    TreeStats<NodeHandler> * createTreeStats(const K& _minKey, const K& _maxKey) {
        return new TreeStats<NodeHandler>(new NodeHandler(_minKey, _maxKey), NULL, true);
    }
#endif
};

#endif

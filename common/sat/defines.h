#pragma once

#define ADAPTER_NODE_HANDLER class NodeHandler {                                           \
   public:                                                                                 \
       using MyNodeHandler = typename DATA_STRUCTURE_T::NodeHandler;                       \
       using NodePtrType = typename MyNodeHandler::NodePtrType;                            \
                                                                                           \
    private:                                                                               \
       MyNodeHandler* my_node_handler;                                                     \
                                                                                           \
    public:                                                                                \
                                                                                           \
       NodeHandler(MyNodeHandler* my_node_handler) : my_node_handler(my_node_handler) {    \
       }                                                                                   \
                                                                                           \
       ~NodeHandler() {                                                                    \
            delete my_node_handler;                                                        \
       }                                                                                   \
                                                                                           \
       class ChildIterator {                                                               \
       private:                                                                            \
           using MyChildIterator = typename MyNodeHandler::ChildIterator;                  \
                                                                                           \
           MyChildIterator my_child_iterator;                                              \
       public:                                                                             \
           ChildIterator(MyChildIterator my_child_iterator): my_child_iterator(my_child_iterator) {  \
           }                                                                               \
                                                                                           \
           bool hasNext() {                                                                \
               return my_child_iterator.HasNext();                                         \
           }                                                                               \
                                                                                           \
           NodePtrType next() {                                                            \
               return my_child_iterator.Next();                                            \
           }                                                                               \
       };                                                                                  \
                                                                                           \
       size_t getNumChildren(NodePtrType node) {                                           \
           return my_node_handler->GetNumChildren(node);                                   \
       }                                                                                   \
                                                                                           \
       bool isLeaf(NodePtrType node) {                                                     \
           return my_node_handler->IsLeaf(node);                                           \
       }                                                                                   \
                                                                                           \
       size_t getNumKeys(NodePtrType node) {                                               \
           return my_node_handler->GetNumKeys(node);                                       \
       }                                                                                   \
                                                                                           \
       size_t getSumOfKeys(NodePtrType node) {                                             \
           return my_node_handler->GetSumKeys(node);                                       \
       }                                                                                   \
                                                                                           \
       ChildIterator getChildIterator(NodePtrType node) {                                  \
           return ChildIterator(my_node_handler->GetChildIterator(node));                  \
       }                                                                                   \
   };                                                                                      \
                                                                                           \
   TreeStats<NodeHandler> * createTreeStats(const K& _minKey, const K& _maxKey) {          \
       return new TreeStats<NodeHandler>(new NodeHandler(ds->GetNodeHandler()), ds->GetRoot(), false); \
   }                                                                                       \

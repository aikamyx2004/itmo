#pragma once

#include <vector>

struct Node
{
    int key;
    double prior;
    Node *left, *right;

    Node(int value);
    ~Node();
};

class Treap
{
    std::size_t tree_size = 0;
    Node * root = nullptr;

public:
    bool contains(int value) const;
    bool insert(int value);
    bool remove(int value);

    std::size_t size() const;
    bool empty() const;

    std::vector<int> values() const;

    ~Treap();
};

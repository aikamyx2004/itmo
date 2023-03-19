#include "Treap.h"

#include "RandomGenerator.h"

Node::Node(int value)
    : key(value)
    , prior(get_random_number())
    , left(nullptr)
    , right(nullptr)
{
}

Node::~Node()
{
    delete left;
    delete right;
}

std::pair<Node *, Node *> split(Node * t, int key)
{
    if (t == nullptr) {
        return {nullptr, nullptr};
    }
    else if (t->key > key) {
        auto [left, right] = split(t->left, key);
        t->left = right;
        return {left, t};
    }
    else {
        auto [left, right] = split(t->right, key);
        t->right = left;
        return {t, right};
    }
}

Node * merge(Node * left, Node * right)
{
    if (left == nullptr || right == nullptr) {
        return left ? left : right;
    }
    else if (left->prior > right->prior) {
        left->right = merge(left->right, right);
        return left;
    }
    else {
        right->left = merge(left, right->left);
        return right;
    }
}

bool Treap::contains(int value) const
{
    Node * now = root;
    while (now != nullptr) {
        if (now->key == value) {
            return true;
        }
        now = now->key > value ? now->left : now->right;
    }
    return false;
}

bool Treap::insert(int value)
{
    if (contains(value))
        return false;
    auto [left, right] = split(root, value);
    root = merge(merge(left, new Node(value)), right);
    tree_size++;
    return true;
}

bool Treap::remove(int value)
{
    if (!contains(value))
        return false;
    auto [temp, right] = split(root, value);
    auto [left, mid] = split(temp, value - 1);
    delete mid;
    root = merge(left, right);
    tree_size--;
    return true;
}

std::size_t Treap::size() const
{
    return tree_size;
}

bool Treap::empty() const
{
    return tree_size == 0;
}

void fill_values(const Node * t, std::vector<int> & result)
{
    if (t == nullptr) {
        return;
    }
    if (t->left) {
        fill_values(t->left, result);
    }
    result.push_back(t->key);
    if (t->right) {
        fill_values(t->right, result);
    }
}

std::vector<int> Treap::values() const
{
    std::vector<int> result;
    result.reserve(tree_size);
    fill_values(root, result);
    return result;
}

Treap::~Treap()
{
    delete root;
}

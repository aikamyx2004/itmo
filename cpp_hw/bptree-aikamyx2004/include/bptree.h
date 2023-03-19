#pragma once

#include <cstddef>
#include <functional>
#include <stdexcept>
#include <utility>
#include <vector>

namespace tree_details_for_iterator {

template <class T>
constexpr bool IsConst = false;
template <template <bool> class T>
constexpr bool IsConst<T<true>> = true;

} // namespace tree_details_for_iterator

template <class Key, class Value, std::size_t BlockSize = 4096, class Less = std::less<Key>>
class BPTree
{
    using size_type = std::size_t;
    using key_type = Key;
    using mapped_type = Value;
    using value_type = std::pair<Key, Value>;
    using reference = value_type &;
    using const_reference = const value_type &;
    using pointer = value_type *;
    using const_pointer = const value_type *;

    // BlockSize = inner_max_size * sizeof(void *) + 2 * sizeof(void*)
    static const std::size_t inner_max_size = std::max((BlockSize) / sizeof(void *) - 2, static_cast<std::size_t>(3));
    // BlockSize = leaf_max_size * sizeof(pair<Key, Value>) + sizeof(void*)
    static const std::size_t leaf_max_size = std::max((BlockSize - sizeof(void *)) / (sizeof(value_type)), static_cast<std::size_t>(3));
    static const std::size_t inner_min_size = inner_max_size / 2;
    static const std::size_t leaf_min_size = leaf_max_size / 2;

    static constexpr Less m_less{};

    struct Node;
    struct LeafNode;
    struct InnerNode;

    struct Info
    {
        std::size_t index = 0;
        LeafNode * place = nullptr;
        bool contains = false;
        Info(LeafNode * _place = nullptr, std::size_t _index = 0, bool _contains = false)
            : index(_index)
            , place(_place)
            , contains(_contains)
        {
        }
    };

    struct Node
    {
        virtual ~Node() = default;
        virtual LeafNode * left() = 0;           // return left leaf of node
        virtual LeafNode * right() = 0;          // return right leaf of node
        virtual const Key & max_key() const = 0; // return max key of node

        virtual Info lower_bound(const Key &) = 0;

        virtual std::pair<Node *, Node *> insert_impl(const Key &, Value &&, Info &) = 0;

        virtual std::pair<bool, Node *> need_change_root() = 0; // return node, if root has 1 child, so we can change root to this child
        virtual Info erase_impl(const Key &, InnerNode * parent = nullptr, std::size_t node_index = 0) = 0;
    };

    struct LeafNode : public Node
    {
        std::vector<value_type> data;
        LeafNode * next_leaf = nullptr;

        LeafNode * left() override
        {
            return this;
        }

        LeafNode * right() override
        {
            return this;
        }

        std::pair<bool, Node *> need_change_root() override
        {
            return {false, nullptr};
        }

        const Key & max_key() const override
        {
            return data.back().first;
        }

        std::size_t find_index(const Key & key) const // return index where I can put this key
        {
            return std::distance(
                    data.begin(),
                    std::lower_bound(data.begin(), data.end(), key, [](const value_type & lhs, const Key & rhs) {
                        return m_less(lhs.first, rhs);
                    }));
        }

        Info lower_bound(const Key & key) override
        {
            std::size_t index = find_index(key);
            if (index == data.size()) {
                return {nullptr, 0, false};
            }
            return {this, index, !m_less(key, data[index].first)}; // !m_less => equal
        }

        static Info good_info(Info & info) // return next leaf, if info.index point to the end of data
        {
            if (info.index == info.place->data.size()) {
                info.place = info.place->next_leaf;
                info.index = 0;
            }
            return info;
        }

        std::pair<Node *, Node *> insert_impl(const Key &, Value &&, Info &) override;
        Info erase_impl(const Key &, InnerNode * parent = nullptr, std::size_t node_index = 0) override;
    };
    // I don't keep all keys between children, but evey node knows her left and
    // right leaf, so max key in node is the last key in node's right leaf,
    // so I spend 2 more pointers instead of all keys
    struct InnerNode : public Node
    {
        std::vector<Node *> children;
        LeafNode * left_leaf = nullptr;
        LeafNode * right_leaf = nullptr;

        InnerNode() = default;

        InnerNode(Node * first, Node * second)
            : children({first, second})
            , left_leaf(first->left())
            , right_leaf(second->right())
        {
        }

        ~InnerNode()
        {
            for (auto child : children) {
                delete child;
            }
        }

        LeafNode * left() override
        {
            return left_leaf;
        }

        LeafNode * right() override
        {
            return right_leaf;
        }

        std::pair<bool, Node *> need_change_root() override
        {
            if (children.size() == 1) {
                children.clear();
                return {true, children.front()};
            }
            return {false, nullptr};
        }

        const Key & max_key() const override
        {
            return right_leaf->data.back().first;
        }

        std::size_t find_index(const Key & key) const
        {
            auto position = std::lower_bound(children.begin(), children.end(), key, [&](const Node * lhs, const Key & rhs) {
                return m_less(lhs->max_key(), rhs);
            });
            return std::min(children.size() - 1, static_cast<std::size_t>(std::distance(children.begin(), position)));
        }

        Info lower_bound(const Key & key) override
        {
            std::size_t index = find_index(key);
            return children[index]->lower_bound(key);
        }

        std::pair<Node *, Node *> insert_impl(const Key &, Value &&, Info &) override;
        Info erase_impl(const Key &, InnerNode * parent = nullptr, std::size_t node_index = 0) override;
    };

private:
    template <bool is_const>
    class Iterator;

    LeafNode * left_leaf() const
    {
        auto node = root->left();
        return node->data.empty() ? nullptr : node;
    }

public:
    using iterator = Iterator<false>;
    using const_iterator = Iterator<true>;
    iterator begin() { return {left_leaf(), 0, this}; }
    iterator end() { return this; }
    const_iterator cbegin() const { return {left_leaf(), 0, this}; }
    const_iterator cend() const { return this; }
    const_iterator begin() const { return cbegin(); }
    const_iterator end() const { return cend(); }

public:
    bool empty() const
    {
        return size_ == 0;
    };

    std::size_t size() const
    {
        return size_;
    }

    void clear()
    {
        *this = BPTree();
    }

public:
    iterator lower_bound(const Key & key)
    {
        auto info = root->lower_bound(key);
        return {info.place, info.index, this};
    }

    const_iterator lower_bound(const Key & key) const
    {
        auto info = root->lower_bound(key);
        return {info.place, info.index, this};
    }

private:
    iterator upper_bound_impl(const Key & key) const
    {
        auto info = root->lower_bound(key);
        auto it = iterator(info.place, info.index, this);
        if (it == end() || m_less(key, it->first))
            return it;
        return ++it; // it->first == key, so we need to return next
    }

public:
    iterator upper_bound(const Key & key)
    {
        return upper_bound_impl(key);
    }

    const_iterator upper_bound(const Key & key) const
    {
        return upper_bound_impl(key);
    }

private:
    iterator find_impl(const Key & key) const
    {
        auto info = root->lower_bound(key);
        if (!info.contains)
            return {nullptr, 0, this};
        return {info.place, info.index, this};
    }

public:
    iterator find(const Key & key)
    {
        return find_impl(key);
    }

    const_iterator find(const Key & key) const
    {
        return find_impl(key);
    }

    bool contains(const Key & key) const
    {
        auto info = root->lower_bound(key);
        return info.contains;
    }

    std::size_t count(const Key & key) const
    {
        return contains(key);
    }

    std::pair<iterator, iterator> equal_range(const Key & key)
    {
        return {lower_bound(key), upper_bound(key)};
    }

    std::pair<const_iterator, const_iterator> equal_range(const Key & key) const
    {
        return {lower_bound(key), upper_bound(key)};
    }

public:
    Value & at(const Key & key)
    {
        auto it = find(key);
        if (it == end()) {
            throw std::out_of_range("no such key");
        }
        return it->second;
    }

    const Value & at(const Key & key) const
    {
        auto it = find(key);
        if (it == end()) {
            throw std::out_of_range("no such key");
        }
        return it->second;
    }

public:
    std::pair<iterator, bool> insert(const Key & key, Value && value)
    {
        Info info;
        auto [node, sibling] = root->insert_impl(key, std::forward<Value>(value), info);
        if (node != nullptr && sibling != nullptr) { // need to create new node, if root split
            root = new InnerNode(node, sibling);
        }
        size_ += info.contains;
        return {iterator(info.place, info.index, this), info.contains};
    }

    std::pair<iterator, bool> insert(const Key & key, const Value & value)
    {
        return insert(key, Value(value));
    }

    template <class ForwardIt>
    void insert(ForwardIt begin, ForwardIt end)
    {
        for (auto it = begin; it != end; ++it) {
            insert(it->first, it->second);
        }
    }

    void insert(std::initializer_list<value_type> list)
    {
        for (const auto & [key, value] : list) {
            insert(key, value);
        }
    }

    Value & operator[](const Key & key)
    {
        auto place = find(key);
        if (place == end()) {
            return insert(key, std::move(Value())).first->second;
        }
        return place->second;
    }

private:
    Info erase_impl(const Key & key)
    {
        Info info = root->erase_impl(key);
        size_ -= info.contains;
        auto [need_change, new_root] = root->need_change_root();
        if (need_change) {
            delete root;
            root = new_root;
        }
        return info;
    }

public:
    size_type erase(const Key & key)
    {
        Info info = erase_impl(key);
        return info.contains;
    }

    iterator erase(const_iterator it)
    {
        Info info = erase_impl(it->first);
        return {info.place, info.index, this};
    }

    iterator erase(const_iterator first, const_iterator last)
    {
        std::size_t cnt = std::distance(first, last);
        for (std::size_t i = 0; i < cnt; ++i) {
            first = erase(first);
        }
        return {first.m_current, first.m_index, first.m_tree};
    }

public:
    BPTree() = default;

    BPTree(std::initializer_list<std::pair<Key, Value>> list)
    {
        insert(list);
    }

    BPTree(const BPTree & other)
    {
        *this = other;
    }

    BPTree(BPTree && other)
    {
        *this = std::move(other);
    }

    BPTree & operator=(BPTree && other)
    {
        std::swap(root, other.root);
        std::swap(size_, other.size_);
        return *this;
    }

    BPTree & operator=(const BPTree & other)
    {
        *this = BPTree();
        insert(other.begin(), other.end());
        return *this;
    }

    ~BPTree()
    {
        delete root;
    }

private:
    Node * root = new LeafNode();
    std::size_t size_ = 0;
};

template <class Key, class Value, std::size_t BlockSize, class Less>
template <bool is_const>
class BPTree<Key, Value, BlockSize, Less>::Iterator
{
public:
    friend class BPTree;

    using difference_type = std::ptrdiff_t;
    using value_type = std::conditional_t<is_const, const std::pair<Key, Value>, std::pair<Key, Value>>;
    using pointer = value_type *;
    using reference = value_type &;
    using iterator_category = std::forward_iterator_tag;

    Iterator() = default;

    template <class R = Iterator, std::enable_if_t<tree_details_for_iterator::IsConst<R>, int> = 0>
    Iterator(const Iterator<false> & other)
        : m_current(other.m_current)
        , m_index(other.m_index)
        , m_tree(other.m_tree)
    {
    }

    friend bool operator==(const Iterator & lhs, const Iterator & rhs)
    {
        return lhs.m_index == rhs.m_index && lhs.m_tree == rhs.m_tree && lhs.m_current == rhs.m_current;
    }

    friend bool operator!=(const Iterator & lhs, const Iterator & rhs)
    {
        return !(lhs == rhs);
    }

    reference operator*() const
    {
        return m_current->data[m_index];
    }

    pointer operator->() const
    {
        return &m_current->data[m_index];
    }

    Iterator & operator++()
    {
        if (m_index + 1 >= m_current->data.size()) {
            m_current = m_current->next_leaf;
            m_index = 0;
        }
        else {
            ++m_index;
        }
        return *this;
    }

    Iterator operator++(int)
    {
        auto tmp = *this;
        operator++();
        return tmp;
    }

private:
    Iterator(const BPTree * tree)
        : m_tree(tree)
    {
    }

    Iterator(LeafNode * node, std::size_t index, const BPTree * tree)
        : m_current(node)
        , m_index(index)
        , m_tree(tree)
    {
    }

    LeafNode * m_current = nullptr;
    std::size_t m_index = 0;
    const BPTree * m_tree = nullptr;
};

// LeafNode insert
template <class Key, class Value, std::size_t BlockSize, class Less>
auto BPTree<Key, Value, BlockSize, Less>::LeafNode::insert_impl(const Key & key, Value && value, BPTree::Info & info)
        -> std::pair<Node *, Node *>
{
    std::size_t index = find_index(key);
    if (index == data.size() || m_less(key, data[index].first)) { // add key and value if it wasn't
        info.contains = true;
        data.template emplace(data.begin() + index);
        data[index].first = key;
        data[index].second = std::move(value);
    }

    info.index = index;
    info.place = this;
    if (data.size() <= leaf_max_size) { // node is not full, so we don't split
        return {nullptr, nullptr};
    }
    // split node, half of these data goes to sibling
    LeafNode * sibling = new LeafNode();
    sibling->next_leaf = next_leaf;
    next_leaf = sibling;

    std::size_t values_to_move = data.size() / 2;
    sibling->data.insert(sibling->data.begin(), std::move_iterator(data.begin() + values_to_move), std::move_iterator(data.end()));
    data.erase(data.begin() + values_to_move, data.end());

    if (info.index >= values_to_move) { // change info, if key is in sibling
        info.index -= values_to_move;
        info.place = sibling;
    }
    return {this, sibling};
}

// InnerNode insert
template <class Key, class Value, std::size_t BlockSize, class Less>
auto BPTree<Key, Value, BlockSize, Less>::InnerNode::insert_impl(const Key & key, Value && value, BPTree::Info & info)
        -> std::pair<Node *, Node *>
{
    std::size_t child_index = find_index(key);
    auto [child, next] = children[child_index]->insert_impl(key, std::forward<Value>(value), info);

    if (child != nullptr) { // add sibling
        children.insert(children.begin() + child_index + 1, next);
        left_leaf = children.front()->left();
        right_leaf = children.back()->right();
    }

    if (children.size() <= inner_max_size) { // node is not full, so we don't split
        return {nullptr, nullptr};
    }
    // split node, half of these children goes to sibling
    InnerNode * sibling = new InnerNode();

    std::size_t child_to_move = children.size() / 2;
    sibling->children.insert(sibling->children.begin(), children.begin() + child_to_move, children.end());
    children.erase(children.begin() + child_to_move, children.end());
    right_leaf = children.back()->right();
    left_leaf = children.front()->left();

    sibling->right_leaf = sibling->children.back()->right();
    sibling->left_leaf = sibling->children.front()->left();
    return {this, sibling};
}

// LeafNode erase
template <class Key, class Value, std::size_t BlockSize, class Less>
auto BPTree<Key, Value, BlockSize, Less>::LeafNode::erase_impl(const Key & key, InnerNode * parent, std::size_t node_index) -> Info
{
    std::size_t index = find_index(key);
    Info info(this, index);
    if (index == data.size() || m_less(key, data[index].first)) { // there no such key
        return info;
    }
    info.contains = true;
    data.erase(data.begin() + index);
    if (parent == nullptr || data.size() >= leaf_min_size) {
        // node is root or half full, so we don't take data from other nodes
        return good_info(info);
    }

    if (node_index > 0) { // there is left node
        LeafNode * left = dynamic_cast<LeafNode *>(parent->children[node_index - 1]);
        if (left->data.size() == leaf_min_size) {
            // merge left node and this, and delete this.
            // I couldn't delete left node, because
            // it was hard to keep pointers to next leaves
            info = Info(left, index + left->data.size());
            left->data.insert(left->data.end(), std::move_iterator(data.begin()), std::move_iterator(data.end()));
            left->next_leaf = next_leaf;

            delete this;
            parent->children.erase(parent->children.begin() + node_index);
        }
        else {
            data.insert(data.begin(), std::move(left->data.back()));
            left->data.pop_back();
            info.index++; // added node to the beginning of data, so index changed
        }
    }
    else {
        LeafNode * right = dynamic_cast<LeafNode *>(parent->children[node_index + 1]);
        if (right->data.size() == leaf_min_size) {
            // merge right node and this, and delete right
            data.insert(data.end(), std::move_iterator(right->data.begin()), std::move_iterator(right->data.end()));
            next_leaf = right->next_leaf;
            delete right;
            parent->children.erase(parent->children.begin() + node_index + 1);
        }
        else {
            data.push_back(std::move(right->data.front()));
            right->data.erase(right->data.begin());
        }
    }
    return good_info(info); // info.index could point to the end of array, so I change info is needed
}

// InnerNode erase
template <class Key, class Value, std::size_t BlockSize, class Less>
auto BPTree<Key, Value, BlockSize, Less>::InnerNode::erase_impl(const Key & key, InnerNode * parent, std::size_t node_index)
        -> Info
{
    std::size_t index = find_index(key);
    Info info = children[index]->erase_impl(key, this, index);
    if (parent == nullptr || children.size() >= inner_min_size) {
        left_leaf = children.front()->left();
        right_leaf = children.back()->right();
        return info;
    }
    if (node_index > 0) {
        InnerNode * left = dynamic_cast<InnerNode *>(parent->children[node_index - 1]);
        if (left->children.size() <= inner_min_size) {
            // merge this node and left, delete left
            children.insert(children.begin(), left->children.begin(), left->children.end());
            left->children.clear();

            delete left;
            parent->children.erase(parent->children.begin() + node_index - 1); // erase left node from parent's children
        }
        else {
            children.insert(children.begin(), left->children.back());
            left->children.pop_back();
        }
        left_leaf = children.front()->left(); // left_leaf changed
    }
    else {
        InnerNode * right = dynamic_cast<InnerNode *>(parent->children[node_index + 1]);
        if (right->children.size() == inner_min_size) {
            children.insert(children.end(), right->children.begin(), right->children.end());
            right->children.clear();

            delete right;
            parent->children.erase(parent->children.begin() + node_index + 1); // erase right node from parent's children
        }
        else {
            children.push_back(right->children.front());
            right->children.erase(right->children.begin());
        }
        right_leaf = children.back()->right(); // right_leaf changed
    }
    return info;
}

#pragma once

#include <algorithm>
#include <cstddef>
#include <list>
#include <new>
#include <ostream>

template <class Key, class KeyProvider, class Allocator>
class Cache
{
public:
    template <class... AllocArgs>
    Cache(const std::size_t cache_size, AllocArgs &&... alloc_args)
        : m_max_top_size(cache_size)
        , m_max_low_size(cache_size)
        , m_alloc(std::forward<AllocArgs>(alloc_args)...)
    {
    }

    std::size_t size() const
    {
        return recently.size() + queue.size();
    }

    bool empty() const
    {
        return size() == 0;
    }

    template <class T>
    T & get(const Key & key);

    std::ostream & print(std::ostream & strm) const;

    friend std::ostream & operator<<(std::ostream & strm, const Cache & cache)
    {
        return cache.print(strm);
    }

private:
    const std::size_t m_max_top_size;
    const std::size_t m_max_low_size;
    Allocator m_alloc;
    std::list<KeyProvider *> recently;
    std::list<KeyProvider *> queue;
};

template <class Key, class KeyProvider, class Allocator>
template <class T>
inline T & Cache<Key, KeyProvider, Allocator>::get(const Key & key)
{
    auto check = [&key](auto element) {
        return *element == key;
    };
    auto it = std::find_if(recently.begin(), recently.end(), check);
    if (it != recently.end()) {
        recently.splice(recently.begin(), recently, it);
        return *static_cast<T *>(*it);
    }

    it = std::find_if(queue.begin(), queue.end(), check);
    if (it != queue.end()) {
        recently.splice(recently.begin(), queue, it);
        if (recently.size() > m_max_top_size) {
            queue.splice(queue.begin(), recently, std::prev(recently.end()));
        }
        return *static_cast<T *>(*it);
    }

    queue.push_front(m_alloc.template create<T>(key));
    if (queue.size() > m_max_low_size) {
        m_alloc.template destroy<KeyProvider>(queue.back());
        queue.pop_back();
    }
    return *static_cast<T *>(queue.front());
}

template <class Key, class KeyProvider, class Allocator>
inline std::ostream & Cache<Key, KeyProvider, Allocator>::print(std::ostream & strm) const
{
    bool first = true;
    for (auto elem : recently) {
        if (first) {
            first = false;
        }
        else {
            strm << ' ';
        }
        strm << *elem;
    }

    for (auto elem : queue) {
        if (first) {
            first = false;
        }
        else {
            strm << ' ';
        }
        strm << *elem;
    }
    return strm;
}

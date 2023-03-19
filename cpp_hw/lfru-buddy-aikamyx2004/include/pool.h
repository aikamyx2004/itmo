#pragma once

#include <map>
#include <new>
#include <unordered_set>
#include <vector>

class PoolAllocator
{
    std::size_t find_free_segment(std::size_t power);
    std::size_t segment_length(std::size_t power) const;
    bool check_free_segment(const std::unordered_set<std::size_t> & level, std::size_t segment_start);
    void merge_free_segment(std::size_t level, std::size_t first_segment_start, std::size_t second_segment_start);

    const std::size_t min_size;
    std::vector<std::unordered_set<std::size_t>> free_segment;
    std::map<std::size_t, std::size_t> segment_level;
    std::vector<std::byte> storage;

public:
    PoolAllocator(const unsigned min_p, const unsigned max_p);

    void * allocate(const std::size_t n);

    void deallocate(const void * ptr);
};

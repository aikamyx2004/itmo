#include "pool.h"

namespace {
std::size_t log(std::size_t n)
{
    std::size_t result = 0;
    while ((1ul << result) < n) {
        ++result;
    }
    return result;
}
} // namespace

std::size_t PoolAllocator::segment_length(std::size_t power) const
{
    return static_cast<std::size_t>(1ll << (power + min_size));
}

PoolAllocator::PoolAllocator(const unsigned int min_p, const unsigned int max_p)
    : min_size(min_p)
    , free_segment(max_p - min_p + 1)
    , storage(1 << max_p)
{
    free_segment.back().insert(0);
}

void * PoolAllocator::allocate(const std::size_t n)
{
    std::size_t power = std::max(log(n), min_size) - min_size;
    std::size_t segment_start = find_free_segment(power);
    segment_level[segment_start] = power;
    free_segment[power].erase(segment_start);
    return storage.data() + segment_start;
}

std::size_t PoolAllocator::find_free_segment(std::size_t power)
{
    std::size_t level = power;
    for (; level < free_segment.size(); ++level) {
        if (!free_segment[level].empty()) {
            break;
        }
    }
    if (level >= free_segment.size()) {
        throw std::bad_alloc{};
    }

    std::size_t segment_start = *free_segment[level].begin();
    for (; level > power; --level) {
        std::size_t mid = segment_start + segment_length(level - 1);
        free_segment[level - 1].insert(mid);
        free_segment[level].erase(segment_start);
    }
    return segment_start;
}

void PoolAllocator::deallocate(const void * ptr)
{
    auto byte_ptr = static_cast<const std::byte *>(ptr);
    std::size_t segment_start = byte_ptr - storage.data();
    std::size_t level = segment_level[segment_start];
    segment_level.erase(segment_start);
    std::size_t other_segment;

    for (; level + 1 < free_segment.size(); ++level) {
        other_segment = segment_start + segment_length(level);
        if (check_free_segment(free_segment[level], other_segment)) {
            merge_free_segment(level, segment_start, other_segment);
            continue;
        }

        if (segment_start >= segment_length(level)) {
            other_segment = segment_start - segment_length(level);
            if (check_free_segment(free_segment[level], other_segment)) {
                merge_free_segment(level, other_segment, segment_start);
                segment_start = other_segment;
                continue;
            }
        }
        break;
    }
    free_segment[level].insert(segment_start);
}

bool PoolAllocator::check_free_segment(const std::unordered_set<std::size_t> & level, std::size_t segment_start)
{
    return level.find(segment_start) != level.end();
}

void PoolAllocator::merge_free_segment(std::size_t level, std::size_t first_segment_start, std::size_t second_segment_start)
{
    free_segment[level].erase(first_segment_start);
    free_segment[level].erase(second_segment_start);
    free_segment[level + 1].insert(first_segment_start);
}

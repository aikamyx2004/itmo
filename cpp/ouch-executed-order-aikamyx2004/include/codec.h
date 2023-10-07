#pragma once

#include <cstring>
#include <string>

inline unsigned encode_integer(const unsigned char *& start)
{
    constexpr size_t integer_size = 4;
    unsigned result = 0;
    for (size_t i = 0; i < integer_size; ++i) {
        result *= 256;
        result += *start++;
    }
    return result;
}

inline void encode_integer(const unsigned char *& start, unsigned & result)
{
    result = encode_integer(start);
}

inline void encode_price(const unsigned char *& start, double & result)
{
    result = encode_integer(start) / 1e4;
}

inline void encode_text(const unsigned char *& start, std::string & result, size_t length)
{
    std::string_view text = std::string_view(reinterpret_cast<const char *>(start), length);
    result = text.substr(0, text.find_last_not_of(' ') + 1);
    start += length;
}

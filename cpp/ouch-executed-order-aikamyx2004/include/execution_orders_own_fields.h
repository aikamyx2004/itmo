#include "codec.h"

#include <bitset>

inline void encode_bit(const unsigned char *& start, bool & result, size_t index)
{
    std::bitset<8> binary(*start);
    result = binary[index];
}

inline void encode_liquidity_indicator(const unsigned char *& start, LiquidityIndicator & result)
{
    std::bitset<8> binary(*start);
    result = LiquidityIndicator::None;
    if (!binary[4]) {
        result = binary[3] ? LiquidityIndicator::Removed : LiquidityIndicator::Added;
    }
}

#define FIELD(name, protocol_type, ctype)                                   \
    inline void encode_##name(const unsigned char *& start, ctype & result) \
    {                                                                       \
        encode_##protocol_type(start, result);                              \
    }

#define FIELD_1_ARGS(name, protocol_type, ctype, arg1)                      \
    inline void encode_##name(const unsigned char *& start, ctype & result) \
    {                                                                       \
        encode_##protocol_type(start, result, arg1);                        \
    }
#include "execution_orders_own_fields.inl"

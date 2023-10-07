#include "codec.h"

inline void encode_mmt(const unsigned char *& start, char (&mmt)[15])
{
    std::fill(std::begin(mmt), std::end(mmt), '-');
    mmt[0] = '1';
    mmt[1] = *start++;
    mmt[2] = *start++;
    mmt[9] = 'P';
    mmt[10] = *start++;
    mmt[14] = '\0';
}

#define FIELD_1_ARGS(name, protocol_type, ctype, arg1)                      \
    inline void encode_##name(const unsigned char *& start, ctype & result) \
    {                                                                       \
        encode_##protocol_type(start, result, arg1);                        \
    }

#define FIELD(name, protocol_type, ctype)                                   \
    inline void encode_##name(const unsigned char *& start, ctype & result) \
    {                                                                       \
        encode_##protocol_type(start, result);                              \
    }
#include "mutual_trade_fields.inl"

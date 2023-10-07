#include "requests.h"

inline BreakReason encode_reason(const unsigned char *& start)
{
    switch (*start++) {
    case 'E':
        return BreakReason::Erroneous;
    case 'C':
        return BreakReason::Consent;
    case 'S':
        return BreakReason::Supervisory;
    case 'X':
        return BreakReason::External;
    default:
        return BreakReason::Unknown;
    }
}

inline void encode_reason(const unsigned char *& start, BreakReason & result)
{
    result = encode_reason(start);
}

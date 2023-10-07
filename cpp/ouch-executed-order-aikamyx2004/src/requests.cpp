#include "requests.h"

#include "broken_trades_own_fields.h"
#include "execution_orders_own_fields.h"
#include "mutual_trade_fields.h"

#define FIELD(name) encode_##name(start, details.name);
#define SKIP(n) start += n;

ExecutionDetails decode_executed_order(const std::vector<unsigned char> & message)
{
    ExecutionDetails details;
    const unsigned char * start = message.data();
#include "execution_orders_all_fields.inl"
    return details;
}

BrokenTradeDetails decode_broken_trade(const std::vector<unsigned char> & message)
{
    BrokenTradeDetails details;
    const unsigned char * start = message.data();
#include "broken_trades_all_fields.inl"
    return details;
}

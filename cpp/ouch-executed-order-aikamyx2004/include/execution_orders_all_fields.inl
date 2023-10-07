#if !defined(SKIP) || !defined(FIELD)
#error you have to define SKIP, FIELD
#else
SKIP(9)
FIELD(cl_ord_id)
FIELD(filled_volume)
FIELD(price)
SKIP(1)
FIELD(match_number)
FIELD(counterpart)
FIELD(mmt)
FIELD(liquidity_indicator)
FIELD(internalized)
FIELD(self_trade)
#endif

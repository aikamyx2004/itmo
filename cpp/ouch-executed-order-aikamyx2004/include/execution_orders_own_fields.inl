#if !defined(FIELD_1_ARGS) || !defined(FIELD)
#error you have to define FIELD_1_ARGS and FIELD
#else
FIELD(filled_volume, integer, unsigned)
FIELD_1_ARGS(counterpart, text, std::string, 4)
FIELD_1_ARGS(internalized, bit, bool, 5)
FIELD_1_ARGS(self_trade, bit, bool, 7)

#undef FIELD_1_ARGS
#undef FIELD

#endif

#if !defined(FIELD_1_ARGS) || !defined(FIELD)
#error you have to define FIELD_1_ARGS and FIELD
#else
FIELD_1_ARGS(cl_ord_id, text, std::string, 14)
FIELD(match_number, integer, unsigned)

#undef FIELD_1_ARGS
#undef FIELD

#endif

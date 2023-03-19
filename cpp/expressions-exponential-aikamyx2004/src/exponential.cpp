#include "exponential.h"

std::string Exponential::str() const
{
    if (_exponent == 0)
        return std::to_string(_significand);
    return std::to_string(_significand) + 'e' + std::to_string(_exponent);
}

std::ostream & operator<<(std::ostream & strm, Exponential exponential)
{
    return strm << exponential.str();
}

#include "expression.h"

std::ostream & operator<<(std::ostream & strm, const Expression & expression)
{
    return strm << expression.str();
}

// Const
Expression * Const::clone() const
{
    return new Const(*this);
}

std::string Const::str() const
{
    return value.str();
}

// Variable
Variable::Variable(const std::string & _variable)
    : variable(_variable)
{
}

Variable::Variable(const Variable & other) = default;

Exponential Variable::eval(const std::map<std::string, Exponential> & values) const
{
    return values.at(variable);
}

Expression * Variable::clone() const
{
    return new Variable(*this);
}

std::string Variable::str() const
{
    return variable;
}

// Negate
Negate::Negate(const Expression & expr)
    : expression(expr.clone())
{
}

Negate::Negate(const Negate & other) = default;

Exponential Negate::eval(const std::map<std::string, Exponential> & values) const
{
    return -expression->eval(values);
}

Expression * Negate::clone() const
{
    return new Negate(*this);
}

std::string Negate::str() const
{
    return "-(" + expression->str() + ")";
}

// Operation
Operation::Operation(const Expression & left, const Expression & right)
    : lhs(left.clone())
    , rhs(right.clone())
{
}

Exponential Operation::eval(const std::map<std::string, Exponential> & values) const
{
    return function(lhs->eval(values), rhs->eval(values));
}

std::string Operation::str() const
{
    return '(' + lhs->str() + ' ' + operation() + ' ' + rhs->str() + ')';
}

// Add
Add::Add(const Expression & lhs, const Expression & rhs)
    : Operation(lhs, rhs)
{
}

Add::Add(const Add & other) = default;

Expression * Add::clone() const
{
    return new Add(*this);
}

// Subtract
Subtract::Subtract(const Expression & lhs, const Expression & rhs)
    : Operation(lhs, rhs)
{
}

Subtract::Subtract(const Subtract & other) = default;

Expression * Subtract::clone() const
{
    return new Subtract(*this);
}

// Multiply
Multiply::Multiply(const Expression & lhs, const Expression & rhs)
    : Operation(lhs, rhs)
{
}

Multiply::Multiply(const Multiply & other) = default;

Expression * Multiply::clone() const
{
    return new Multiply(*this);
}

// Divide
Divide::Divide(const Expression & lhs, const Expression & rhs)
    : Operation(lhs, rhs)
{
}

Divide::Divide(const Divide & other) = default;

Expression * Divide::clone() const
{
    return new Divide(*this);
}

// operators
Negate operator-(const Expression & expression)
{
    return Negate(expression);
}

Add operator+(const Expression & lhs, const Expression & rhs)
{
    return Add(lhs, rhs);
}

Subtract operator-(const Expression & lhs, const Expression & rhs)
{
    return Subtract(lhs, rhs);
}

Multiply operator*(const Expression & lhs, const Expression & rhs)
{
    return Multiply(lhs, rhs);
}

Divide operator/(const Expression & lhs, const Expression & rhs)
{
    return Divide(lhs, rhs);
}

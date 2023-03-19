#pragma once

#include "exponential.h"

#include <map>
#include <memory>
#include <ostream>

struct Expression
{
    virtual Exponential eval(std::map<std::string, Exponential> const & values = {}) const = 0;

    virtual Expression * clone() const = 0;

    virtual std::string str() const = 0;

    friend std::ostream & operator<<(std::ostream & strm, const Expression & expression);

    virtual ~Expression() = default;
};

struct Const : public Expression
{
    constexpr Const(Exponential _value) noexcept
        : value(_value)
    {
    }

    constexpr Const(const Const & other) noexcept = default;

    constexpr Exponential eval(const std::map<std::string, Exponential> & = {}) const noexcept override
    {
        return value;
    }

    Expression * clone() const override;

    std::string str() const override;

private:
    Exponential value;
};

struct Variable : public Expression
{
    Variable(const std::string & _variable);

    Variable(const Variable & other);

    Exponential eval(const std::map<std::string, Exponential> & values = {}) const override;

    Expression * clone() const override;

    std::string str() const override;

private:
    std::string variable;
};

struct Negate : Expression
{
    Negate(const Expression & expr);

    Negate(const Negate & other);

    Exponential eval(const std::map<std::string, Exponential> & values = {}) const override;

    Expression * clone() const override;

    std::string str() const override;

private:
    std::shared_ptr<Expression> expression;
};

struct Operation : public Expression
{
    Operation(const Expression & left, const Expression & right);

    Exponential eval(const std::map<std::string, Exponential> & values = {}) const override;

    std::string str() const override;

private:
    std::shared_ptr<Expression> lhs;
    std::shared_ptr<Expression> rhs;

    virtual Exponential function(Exponential, Exponential) const noexcept = 0;
    virtual char operation() const noexcept = 0;
};

struct Add : public Operation
{
    Add(const Expression & lhs, const Expression & rhs);

    Add(const Add & other);

    Expression * clone() const override;

    constexpr Exponential function(Exponential lhs, Exponential rhs) const noexcept override
    {
        return lhs + rhs;
    }

    constexpr char operation() const noexcept override
    {
        return '+';
    }
};

struct Subtract : public Operation
{
    Subtract(const Expression & lhs, const Expression & rhs);

    Subtract(const Subtract & other);

    Expression * clone() const override;

    constexpr Exponential function(Exponential lhs, Exponential rhs) const noexcept override
    {
        return lhs - rhs;
    }

    constexpr char operation() const noexcept override
    {
        return '-';
    }
};

struct Multiply : public Operation
{
    Multiply(const Expression & lhs, const Expression & rhs);

    Multiply(const Multiply & other);

    Expression * clone() const override;

    constexpr Exponential function(Exponential lhs, Exponential rhs) const noexcept override
    {
        return lhs * rhs;
    }

    constexpr char operation() const noexcept override
    {
        return '*';
    }
};

struct Divide : public Operation
{
    Divide(const Expression & lhs, const Expression & rhs);

    Divide(const Divide & other);

    Expression * clone() const override;

    constexpr Exponential function(Exponential lhs, Exponential rhs) const noexcept override
    {
        return lhs / rhs;
    }

    constexpr char operation() const noexcept override
    {
        return '/';
    }
};

Negate operator-(const Expression & expression);

Add operator+(const Expression & lhs, const Expression & rhs);

Subtract operator-(const Expression & lhs, const Expression & rhs);

Multiply operator*(const Expression & lhs, const Expression & rhs);

Divide operator/(const Expression & lhs, const Expression & rhs);
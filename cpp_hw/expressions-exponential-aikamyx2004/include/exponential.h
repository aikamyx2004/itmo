#pragma once

#include <cmath>
#include <numeric>
#include <string>

struct Exponential
{
    constexpr Exponential() noexcept = default;

    constexpr Exponential(int64_t significand) noexcept
        : Exponential(significand, 0)
    {
    }

    constexpr Exponential(int64_t significand, int64_t exponent) noexcept
        : _significand(significand)
        , _exponent(exponent)
    {
        normalize();
    }

    constexpr int64_t exponent() const noexcept { return _exponent; };

    constexpr int64_t significand() const noexcept { return _significand; };

    constexpr explicit operator double() const noexcept
    {
        return _significand * std::pow(10, _exponent);
    }

    std::string str() const;

    constexpr Exponential & operator+=(Exponential rhs) noexcept
    {
        if (rhs == 0) {
            return *this;
        }
        if (_significand == 0) {
            return (*this = rhs);
        }
        reduce_exponent(*this, rhs);
        _significand += rhs._significand;
        normalize();
        return *this;
    }

    constexpr Exponential & operator-=(Exponential rhs) noexcept
    {
        return *this += -rhs;
    }

    constexpr Exponential & operator*=(Exponential rhs) noexcept
    {
        int64_t ten_power = 0;
        while (_significand != 0 && rhs._significand != 0 && _significand % 5 == 0 && rhs._significand % 2 == 0) {
            _significand /= 5;
            rhs._significand /= 2;
            ++ten_power;
        }
        while (_significand != 0 && rhs._significand != 0 && _significand % 2 == 0 && rhs._significand % 5 == 0) {
            _significand /= 2;
            rhs._significand /= 5;
            ++ten_power;
        }

        _significand *= rhs._significand;
        _exponent += rhs._exponent + ten_power;
        normalize();
        return *this;
    }

    constexpr Exponential & operator/=(Exponential rhs) noexcept
    {
        int64_t gcd = std::gcd(_significand, rhs._significand);
        _significand /= gcd;
        rhs._significand /= gcd;
        while (rhs._significand != 0 && rhs._significand % 5 == 0 && !multiply_overflow(_significand, 2)) {
            rhs._significand /= 5;
            _significand *= 2;
            --_exponent;
        }
        while (rhs._significand != 0 && rhs._significand % 2 == 0 && !multiply_overflow(_significand, 5)) {
            rhs._significand /= 2;
            _significand *= 5;
            --_exponent;
        }
        increase_numerator();
        rhs.decrease_denominator();
        _significand /= rhs._significand;
        _exponent -= rhs._exponent;
        normalize();
        return *this;
    }

    constexpr friend Exponential operator+(Exponential lhs, Exponential rhs) noexcept { return lhs += rhs; }
    constexpr friend Exponential operator-(Exponential lhs, Exponential rhs) noexcept { return lhs -= rhs; }
    constexpr friend Exponential operator*(Exponential lhs, Exponential rhs) noexcept { return lhs *= rhs; }
    constexpr friend Exponential operator/(Exponential lhs, Exponential rhs) noexcept { return lhs /= rhs; }

    constexpr Exponential operator-() const noexcept { return {-_significand, _exponent}; }

    constexpr friend bool operator==(Exponential lhs, Exponential rhs) noexcept
    {
        return lhs._significand == rhs._significand && lhs._exponent == rhs._exponent;
    }

    constexpr friend bool operator!=(Exponential lhs, Exponential rhs) noexcept { return !(lhs == rhs); }

    friend std::ostream & operator<<(std::ostream &, Exponential);

private:
    int64_t _significand = 0;
    int64_t _exponent = 0;

    constexpr static void reduce_exponent(Exponential & lhs, Exponential & rhs) noexcept
    {
        if (lhs._exponent > rhs._exponent) {
            reduce_exponent_impl(lhs, rhs);
        }
        else {
            reduce_exponent_impl(rhs, lhs);
        }
    }

    constexpr static void reduce_exponent_impl(Exponential & lhs, const Exponential & rhs) noexcept
    {
        lhs._significand *= ten_pow(lhs._exponent - rhs._exponent);
        lhs._exponent = rhs._exponent;
    }

    constexpr static int64_t ten_pow(int64_t power) noexcept
    {
        if (power == 0) {
            return 1;
        }
        int64_t value = ten_pow(power / 2);
        value *= value;
        if (power % 2 == 1) {
            value *= 10;
        }
        return value;
    }

    constexpr void increase_numerator() noexcept
    {
        while (_significand != 0 && !multiply_overflow(_significand, 10)) {
            _significand *= 10;
            --_exponent;
        }
    }
    constexpr void decrease_denominator() noexcept
    {
        if (_significand > 0) {
            constexpr int32_t int32_max = std::numeric_limits<int32_t>::max();
            while (_significand > int32_max) {
                _significand /= 10;
                _exponent++;
            }
        }
        if (_significand > 0) {
            constexpr int32_t int32_min = std::numeric_limits<int32_t>::min();
            while (_significand < int32_min) {
                _significand /= 10;
                _exponent++;
            }
        }
    }

    constexpr static bool multiply_overflow(int64_t lhs, int64_t rhs) noexcept
    {
        if (lhs == 0 || rhs == 0) {
            return false;
        }
        constexpr int64_t int64_max = std::numeric_limits<int64_t>::max();
        constexpr int64_t int64_min = std::numeric_limits<int64_t>::min();
        if (lhs > 0) {
            return lhs > int64_max / rhs || rhs > int64_max / lhs;
        }
        return lhs < int64_min / rhs || (lhs != -1 && rhs > int64_min / lhs);
    }

    constexpr void normalize() noexcept
    {
        if (_significand == 0) {
            _exponent = 0;
            return;
        }
        while (_significand % 10 == 0) {
            _significand /= 10;
            ++_exponent;
        }
    }
};

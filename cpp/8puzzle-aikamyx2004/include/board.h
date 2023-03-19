#pragma once

#include <string>
#include <vector>

class Board
{
    std::vector<std::vector<unsigned>> board;
    std::pair<std::size_t, std::size_t> empty_place;

public:
    static Board create_goal(unsigned size);

    static Board create_random(unsigned size);

    Board() = default;

    explicit Board(const std::vector<std::vector<unsigned>> & data);

    std::size_t size() const;

    bool is_goal() const;

    unsigned hamming() const;

    unsigned manhattan() const;

    std::string to_string() const;

    bool is_solvable() const;

    const std::vector<unsigned> & operator[](std::size_t) const;

    const std::pair<std::size_t, std::size_t> & get_empty_place() const;

    const std::vector<std::vector<unsigned>> & get_board() const;

    std::vector<Board> make_move();

    friend bool operator<(const Board & lhs, const Board & rhs) // for map<Board, ...>
    {
        return lhs.board < rhs.board;
    }

    friend bool operator==(const Board & lhs, const Board & rhs)
    {
        return lhs.board == rhs.board;
    }

    friend bool operator!=(const Board & lhs, const Board & rhs)
    {
        return !(lhs == rhs);
    }

    friend std::ostream & operator<<(std::ostream & out, const Board & _board)
    {
        return out << _board.to_string();
    }
};

#include "board.h"

#include <algorithm>
#include <numeric>
#include <random>

namespace {
unsigned absu(unsigned lhs, unsigned rhs)
{
    return lhs < rhs ? rhs - lhs : lhs - rhs;
}
} // namespace

Board Board::create_goal(const unsigned _size)
{
    std::vector<std::vector<unsigned>> board(_size, std::vector<unsigned>(_size));
    unsigned value = 0;
    for (std::size_t i = 0; i < board.size(); ++i) {
        for (std::size_t j = 0; j < board.size(); ++j) {
            board[i][j] = ++value;
        }
    }
    if (_size != 0) {
        board.back().back() = 0;
    }
    return Board(board);
}

Board Board::create_random(const unsigned _size)
{
    std::vector<unsigned> values(_size * _size);
    std::iota(values.begin(), values.end(), 0);

    std::shuffle(values.begin(), values.end(), std::mt19937(std::random_device{}()));
    std::vector<std::vector<unsigned>> board(_size, std::vector<unsigned>(_size));
    std::size_t pos = 0;
    for (auto & row : board) {
        for (auto & element : row) {
            element = values[pos++];
        }
    }
    return Board(board);
}

Board::Board(const std::vector<std::vector<unsigned>> & data)
    : board(data)
{
    for (std::size_t i = 0; i < data.size(); ++i) {
        for (std::size_t j = 0; j < data.size(); ++j) {
            if (data[i][j] == 0) {
                empty_place = {i, j};
                return;
            }
        }
    }
}

std::size_t Board::size() const
{
    return board.size();
}

bool Board::is_goal() const
{
    return hamming() == 0;
}

unsigned Board::hamming() const
{
    unsigned count = 0, value = 0;
    for (std::size_t i = 0; i < board.size(); ++i) {
        for (std::size_t j = 0; j < board.size(); ++j) {
            if (i + 1 == board.size() && j + 1 == board.size()) {
                if (board[i][j] != 0) {
                    ++count;
                }
            }
            else {
                if (board[i][j] != ++value) {
                    ++count;
                }
            }
        }
    }
    return count;
}

unsigned Board::manhattan() const
{
    unsigned count = 0;
    for (std::size_t i = 0; i < board.size(); ++i) {
        for (std::size_t j = 0; j < board.size(); ++j) {
            if (board[i][j] == 0) {
                continue;
            }
            unsigned value_i = (board[i][j] - 1) / board.size(); // (value_i, value_j) is the position
            unsigned value_j = (board[i][j] - 1) % board.size(); // of board[i][j] in the goal board
            count += absu(i, value_i) + absu(j, value_j);
        }
    }
    return count;
}

std::string Board::to_string() const
{
    std::string result;
    for (auto & row : board) {
        bool first = true;
        for (auto & element : row) {
            if (first) {
                first = false;
            }
            else {
                result += ' ';
            }
            result += (element == 0) ? "_" : std::to_string(element);
        }
        result += '\n';
    }
    return result;
}

bool Board::is_solvable() const
{
    bool parity = true; // parity of inversions
    for (std::size_t i1 = 0; i1 < board.size(); ++i1) {
        for (std::size_t j1 = 0; j1 < board.size(); ++j1) {
            if (board[i1][j1] == 0) {
                continue;
            }
            for (std::size_t i2 = i1; i2 < board.size(); ++i2) {
                for (std::size_t j2 = (i1 == i2 ? j1 : 0); j2 < board.size(); ++j2) {
                    if (board[i2][j2] == 0) {
                        continue;
                    }
                    if (board[i1][j1] > board[i2][j2]) {
                        parity = !parity;
                    }
                }
            }
        }
    }

    if (board.empty() || board.size() % 2 == 1 || (board.size() - empty_place.first) % 2 == 1)
        return parity;
    return !parity;
}

const std::vector<unsigned int> & Board::operator[](std::size_t index) const
{
    return board[index];
}

const std::pair<std::size_t, std::size_t> & Board::get_empty_place() const
{
    return empty_place;
}

const std::vector<std::vector<unsigned int>> & Board::get_board() const
{
    return board;
}

std::vector<Board> Board::make_move()
{
    static constexpr int dx[4] = {0, 0, 1, -1};
    static constexpr int dy[4] = {1, -1, 0, 0};
    auto [x, y] = empty_place;
    std::vector<Board> result;
    for (std::size_t i = 0; i < 4; ++i) {
        int nx = x + dx[i];
        int ny = y + dy[i];
        if (nx >= 0 && ny >= 0 && static_cast<std::size_t>(nx) < board.size() && static_cast<std::size_t>(ny) < board.size()) {
            std::swap(board[x][y], board[nx][ny]);
            result.emplace_back(board);
            std::swap(board[x][y], board[nx][ny]);
        }
    }
    return result;
}
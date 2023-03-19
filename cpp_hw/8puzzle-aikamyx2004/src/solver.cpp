#include "solver.h"

#include <algorithm>
#include <cmath>
#include <iostream>
#include <map>
#include <queue>
namespace {

struct NodeInfo
{
    std::size_t previous_x = 0, previous_y = 0;
    unsigned distance = std::numeric_limits<unsigned>::max();

    NodeInfo() = default;

    NodeInfo(std::pair<std::size_t, std::size_t> coordinate)
        : previous_x(coordinate.first)
        , previous_y(coordinate.second)
    {
    }
};

struct Node
{
    unsigned moves;
    unsigned heuristic;
    Board board;

    friend bool operator<(const Node & lhs, const Node & rhs) // less by priority
    {
        return rhs.heuristic == 0 || lhs.moves + lhs.heuristic > rhs.moves + rhs.heuristic ||
                (lhs.moves + lhs.heuristic == rhs.moves + rhs.heuristic && lhs.moves < rhs.moves);
    }

    Node() = default;

    Node(unsigned _moves, const Board & _board)
        : moves(_moves)
        , heuristic(find_heuristic(_board))
        , board(_board)
    {
    }

    static unsigned find_heuristic(const Board & board)
    {
        unsigned hamming = board.hamming();
        unsigned heuristic = board.manhattan();
        std::size_t half_board = board.size() * board.size() / 2;
        if (board.size() <= 4) { // can't overestimate heuristic here, because we need the shortest solution
            if (hamming > half_board) {
                heuristic *= 2;
            }
            else if (hamming > 3 * board.size() / 2) {
                heuristic *= 1 + static_cast<double>(hamming) / (board.size() * board.size());
            }
            return heuristic;
        }
        // here we overestimate heuristic to make A* faster,
        // but solutions doesn't have to be the shortest

        // magic constants found by testing
        double arctg = atan(hamming);
        if (hamming >= half_board) {
            heuristic *= 2 * board.size();
        }
        else if (hamming >= 3 * board.size() / 2) {
            heuristic *= sqrt(hamming);
            heuristic *= std::max(board.size() / 2.0, 2.5 * arctg);
        }
        return heuristic * std::max(1.0, arctg);
    }
};
} // namespace

Solver::Solution Solver::solve(const Board & initial)
{
    if (initial.size() <= 1 || initial.is_goal()) {
        return Solution({initial});
    }

    if (!initial.is_solvable()) {
        return {};
    }
    // A* algorithm
    std::priority_queue<Node> queue;
    queue.emplace(0, initial);

    std::map<Board, NodeInfo> node_info;
    node_info[initial] = {initial.get_empty_place()};

    while (!queue.empty()) {
        auto [moves, heuristic, board] = queue.top();
        if (heuristic == 0) {
            break;
        }
        queue.pop();
        for (const auto & other_board : board.make_move()) {
            auto & [nx, ny, other_dist] = node_info[other_board];
            if (other_dist > moves + 1) {
                other_dist = moves + 1;
                nx = board.get_empty_place().first;
                ny = board.get_empty_place().second;
                queue.emplace(moves + 1, other_board);
            }
        }
    }
    // finding path
    // step by step go from the goal to the initial
    // board_now is 2d array that show board condition
    // in each step
    std::vector<Board> result;
    auto [moves, _, board] = queue.top();
    auto board_now(board.get_board()); //
    std::size_t x = board.size() - 1;
    std::size_t y = board.size() - 1;
    for (unsigned i = 0; i <= moves; ++i) { // go from the goal step by step to the initial
        Board temp(board_now);
        auto [nx, ny, node_dist] = node_info[temp];
        result.push_back(std::move(temp));
        std::swap(board_now[x][y], board_now[nx][ny]);
        x = nx, y = ny;
    }
    std::reverse(result.begin(), result.end());
    return Solution(std::move(result));
}

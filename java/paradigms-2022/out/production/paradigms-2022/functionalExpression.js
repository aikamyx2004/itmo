"use strict";

const cnst = a => () => a;
const variables = {x: 0, y: 1, z: 2};
// :NOTE: variables[letter]
const variable = letter => (...args) => args[variables[letter]];

const nFold = f => (...elements) =>
    (...args) => f(...elements.map(a => a(...args)));
const negate = nFold(a => -a);
const add = nFold((a, b) => a + b);
const subtract = nFold((a, b) => a - b);
const multiply = nFold((a, b) => a * b);
const divide = nFold((a, b) => a / b);
const abs = nFold(Math.abs);
const iff = nFold((a, b, c) => a >= 0 ? b : c);

const pi = cnst(Math.PI);
const e = cnst(Math.E);

const constMap = {'pi': pi, 'e': e};
const operationMap = {
    // :NOTE: F.length
    "negate": [negate, 1],
    "abs": [abs, 1],
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "iff": [iff, 3],
};

function parse(expression) {
    return expression.split(' ').filter(token => token !== '').reduce((stack, token) => {
        if (token in variables) {
            stack.push(variable(token));
        } else if (token in constMap) {
            stack.push(constMap[token]);
        } else if (token in operationMap) {
            let operation = operationMap[token][0];
            let length = operationMap[token][1];
            stack.push(operation(...stack.splice(-length)));
        } else {
            stack.push(cnst(parseInt(token)));
        }
        return stack;
    }, []).pop();
}

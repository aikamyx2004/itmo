"use strict";

class Const {
    constructor(number) {
        this.number = number;
    }

    evaluate() {
        return this.number;
    }

    diff() {
        return Const.ZERO;
    }

    toString() {
        return this.number.toString();
    }
}

Const.ZERO = new Const(0);
Const.ONE = new Const(1);

const variables = {'x': 0, 'y': 1, 'z': 2}

class Variable {
    constructor(variable) {
        this.variable = variable;
        this.index = variables[variable];
    }

    evaluate(...args) {
        return args[this.index];
    }

    diff(variable) {
        return this.variable === variable ? Const.ONE : Const.ZERO;
    }

    toString() {
        return this.variable;
    }
}

class AbstractOperation {
    // :NOTE: -> Прототип
    constructor(operation, operator, ...elements) {
        this.elements = elements;
        this.operation = operation;
        this.operator = operator;
    }

    // :NOTE: -> Прототип
    evaluate(...args) {
        return this.operation(...this.elements.map(element => element.evaluate(...args)));
    }

    toString() {
        return this.elements.map(element => element.toString()).join(' ') + ' ' + this.operator;
    }
}

class EasyDiffOperation extends AbstractOperation {
    constructor(operation, operator, ...elements) {
        super(operation, operator, ...elements);
    }

    diff(variable) {
        return new this.constructor(...this.elements.map(a => a.diff(variable)));
    }
}

class Min3 extends EasyDiffOperation {
    constructor(...elements) {
        super((...args) => Math.min(...args), 'min3', ...elements);
    }
}

class Max5 extends EasyDiffOperation {
    constructor(...elements) {
        super((...args) => Math.max(...args), 'max5', ...elements);
    }
}

class Add extends EasyDiffOperation {
    constructor(first, second) {
        super((a, b) => a + b, '+', first, second);
    }
}


class Subtract extends EasyDiffOperation {
    constructor(first, second) {
        super((a, b) => a - b, '-', first, second);
    }
}

class Negate extends EasyDiffOperation {
    constructor(first) {
        super(a => -a, 'negate', first);
    }
}

class Multiply extends AbstractOperation {
    constructor(first, second) {
        // :NOTE: .length
        super((a, b) => a * b, '*', first, second);
    }

    diff(variable) {
        // :NOTE: (a, b, da, db)
        return ((a, b) =>
                new Add(
                    new Multiply(a, b.diff(variable)),
                    new Multiply(b, a.diff(variable))
                )
        )(...this.elements);
    }
}

class Divide extends AbstractOperation {
    constructor(first, second) {
        super((a, b) => a / b, '/', first, second);
    }

    diff(variable) {
        return ((a, b) =>
                new Divide(
                    new Subtract(
                        new Multiply(a.diff(variable), b),
                        new Multiply(b.diff(variable), a)
                    ),
                    new Multiply(b, b))
        )(...this.elements);
    }
}


class Ln extends AbstractOperation {
    constructor(arg) {
        super(a => Math.log(Math.abs(a)), 'ln', arg);
    }

    diff(variable) {
        return (a => new Divide(a.diff(variable), a))(...this.elements);
    }
}

class Pow extends AbstractOperation {
    constructor(first, second) {
        super(Math.pow, "pow", first, second);
    }

    diff(variable) {
        return ((a, b) => new Multiply(
                new Pow(a, b),
                new Multiply(b, new Ln(a)).diff(variable))
        )(...this.elements);
    }
}

class Log extends AbstractOperation {
    constructor(first, second) {
        super((a, b) => Math.log(Math.abs(b)) / Math.log(Math.abs(a)), "log", first, second);
    }

    diff(variable) {
        return ((a, b) =>
            new Divide(new Ln(b), new Ln(a)).diff(variable))(...this.elements);
    }
}

const operationMap = {
    "negate": [Negate, 1],
    "+": [Add, 2],
    "-": [Subtract, 2],
    "*": [Multiply, 2],
    "/": [Divide, 2],
    "max5": [Max5, 5],
    "min3": [Min3, 3],
    "pow": [Pow, 2],
    "log": [Log, 2],
};

const parse = expression =>
    expression.split(' ').filter(token => token !== '').reduce((stack, token) => {
        if (token in variables) {
            stack.push(new Variable(token));
        } else if (token in operationMap) {
            let operation = operationMap[token][0];
            let length = operationMap[token][1];
            stack.push(new operation(...stack.splice(-length)));
        } else {
            stack.push(new Const(parseInt(token)));
        }
        return stack;
    }, []).pop();

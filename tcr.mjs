import * as assert from "node:assert/strict";

function rpn(s) {
  let tokens = s.split(/ /);
  let args = [];

  for (let token of tokens) {
    switch (token) {
    case "+":
      return args.reduce((a,b) => a+b, 0);

    default:
      args.push(parseInt(token));
    }
  }
}

assert.deepEqual(rpn("1 2 +"), 3);

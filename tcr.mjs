import * as assert from "node:assert/strict";

function rpn(s) {
  let tokens = s.split(/ /);
  let args = [];

  for (let token of tokens) {
    switch (token) {
    case "+":
      args = [args.reduce((a,b) => a+b, 0)];
      break;

    default:
      args.push(parseInt(token));
    }
  }
  return args[0];
}

assert.deepEqual(rpn("1 2 +"), 3);
assert.deepEqual(rpn("1 2 + 3 +"), 6);

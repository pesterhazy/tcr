import * as assert from "node:assert/strict";

function rpn(s) {
  let tokens = s.split(/ /);
  let args = [];

  for (let token of tokens) {
    switch (token) {
    case "+":
      args = [args.reduce((a,b) => a+b, 0)];
      break;

    case "-":
      if (args.length === 2) {
        args = [args[0]-args[1]];
      }
      else if (args.length === 1) {
        args = [-args[0]];
      }
      else {
        throw new Error("Must be binary");
      }

      break;

    case "MAX":
      args = [args.reduce((a,b) => Math.max(a,b), 0)];
      break;

    default:
      args.push(parseInt(token));
    }
  }
  return args[0];
}

assert.deepEqual(rpn("1 2 +"), 3);
assert.deepEqual(rpn("1 2 + 3 +"), 6);
assert.deepEqual(rpn("2 1 -"), 1);
assert.deepEqual(rpn("1 -"), -1);
//assert.deepEqual(rpn("5 3 4 2 9 1 MAX"), 9);

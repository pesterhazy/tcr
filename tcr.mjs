import * as assert from "node:assert/strict";

function rpn(s) {
  let tokens = s.split(/ /);
  let args = [];

  args[0] = parseInt(tokens[0]);
  args[1] = parseInt(tokens[1]);
  return args[0] + args[1];
}

assert.deepEqual(rpn("1 2 +"), 3);

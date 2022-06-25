import * as assert from "node:assert/strict";

function splitWithEOL(s) {
  let r = /((?!\n).*)\n/;
  return s.split(/\n/);
}

assert.deepEqual([""], splitWithEOL(""));
assert.deepEqual(["a"], splitWithEOL("a"));
//assert.deepEqual(["a\n", "b"], splitWithEOL("a\nb"));
console.log("ok");

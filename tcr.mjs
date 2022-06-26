import * as assert from "node:assert/strict";

function splitWithEOL(s) {
  return s.split(/\n/);
}

function find(s) {
  let r = /((?!\n).*)\n/y;
  let m = r.exec(s);
  let match = m[0];

  return [match,s.slice(r.lastIndex)];
}

assert.deepEqual(["a\n","b"],find("a\nb"));


assert.deepEqual([""], splitWithEOL(""));
assert.deepEqual(["a"], splitWithEOL("a"));
//assert.deepEqual(["a\n", "b"], splitWithEOL("a\nb"));
console.log("ok");

import * as assert from "node:assert/strict";

function splitWithEOL(s) {
  return s.split(/\n/);
}

function find(s) {
  let r = /((?!\n).*)\n/;
  let m = r.exec(s);
  return m[0];
}

assert.deepEqual("a\n",find("a\nb"));


assert.deepEqual([""], splitWithEOL(""));
assert.deepEqual(["a"], splitWithEOL("a"));
//assert.deepEqual(["a\n", "b"], splitWithEOL("a\nb"));
console.log("ok");

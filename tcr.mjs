import * as assert from "node:assert/strict";

function splitWithEOL(s) {
  return s.split(/\n/);
}

function find(s) {
  let r = /((?!\n).*)\n/y;
  let result = [];
  let last=0;

  do {
    let m = r.exec(s);
    if ( !m )
      break;

    last = r.lastIndex;
    let match = m[0];
    result.push(match);
  } while (1);

  if (last < s.length)
    result.push(s.slice(last));

  return result;
}

assert.deepEqual(["a\n","b"],find("a\nb"));


assert.deepEqual([""], splitWithEOL(""));
assert.deepEqual(["a"], splitWithEOL("a"));
//assert.deepEqual(["a\n", "b"], splitWithEOL("a\nb"));
console.log("ok");

import * as assert from "node:assert/strict";

function splitWithEOL(s) {
  let r = /((?!\r?\n).*)\r?\n/y;
  let result = [];
  let last=0;

  if ( s.length === 0 )
    return [""];

  do {
    let m = r.exec(s);
    if ( !m )
      break;

    last = r.lastIndex;
    let match = m[0];
    result.push(m[0]);
  } while (1);

  if (last < s.length)
    result.push(s.slice(last));

  return result;
}

assert.deepEqual([""], splitWithEOL(""));
assert.deepEqual(["a"], splitWithEOL("a"));
assert.deepEqual(["a\n", "b"], splitWithEOL("a\nb"));
assert.deepEqual(["a\r\n", "b"], splitWithEOL("a\r\nb"));
assert.deepEqual(["one\n","two\r\n","three\r\n"], splitWithEOL("one\ntwo\r\nthree\r\n"));
console.log("ok");

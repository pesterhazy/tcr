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
    result.push(match);
  } while (1);

  if (last < s.length)
    result.push(s.slice(last));

  return result;
}

assert.deepEqual([""], splitWithEOL(""));
assert.deepEqual(["a"], splitWithEOL("a"));
assert.deepEqual(["a\r\n", "b"], splitWithEOL("a\r\nb"));
console.log("ok");

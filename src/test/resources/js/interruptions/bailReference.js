// even though converting to a destructured assignment is legal here by hoisting `b` up,
// we bail on references in order to preserve converter complexity
const arr = [];
const a = arr[0];
const x = a;
const b = arr[1];
// We cannot trust any methods interrupting a group of destructors:
// we do not know if arr is modified in the middle of a group without an advanced analysis
const arr = [0, 0];
function unsureIfModifiesArr() { arr[1] = 5; }
const x = arr[0];
unsureIfModifiesArr();
const y = arr[1];
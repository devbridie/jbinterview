const arr = [];
function unsureIfModifiesArr() { arr[1] = 5; }
const
    x = arr[0],
    y = unsureIfModifiesArr(),
    z = arr[1];
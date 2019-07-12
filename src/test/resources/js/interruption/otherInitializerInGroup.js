const arr = [];
function unsureIfModifiesArr() { arr[1] = 5; }
const
    x = arr[0],
    z = arr[1],
    y = unsureIfModifiesArr();
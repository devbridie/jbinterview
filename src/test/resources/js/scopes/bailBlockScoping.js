// inlining to destructure changes the semantics of the program, so we bail

const arr = [];
const x = arr[0];
{
    const y = arr[1];
}
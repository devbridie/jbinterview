## Interview test problem

In this repository, I have written a command line tool which takes a JavaScript file as input parameter. 
This file will be transformed, finding usages where a destructuring assignment can be used instead of an assignment using an array indexing expression.

The result of the transformation is printed to standard output. 

The transformation looks for chains of assignments of the form `<name> = <array>[<index>]`, and will group those that have the same keyword (`const/var/let`), and that make use of the same array together.

When creating this transformation, I elected to consider the following situations as out of scope:
* Lexical scope changes: `const x = arr[0]; { const y = arr[1]; }` Moving `y` to a destructuring declaration together with `x` changes the semantics of the program.
* Differing keywords: `const x = arr[0]; var y = arr[1];` Grouping a destructuring assignment changes the semantics of the program. However, this can be solved with a more complicated analysis where we check usages of mutability.
* Chain interruptions: `const x = arr[10]; possiblyMutateArr(); const y = arr[1];`. In this scenario, we cannot be sure that any expressions that interrupt the chain do not modify the array. In this case, hoisting `y` above this interruption could change the functionality of the program. However, an advanced analysis of side-effects could solve these cases.
* Non-trivial array expressions: `const x = calc()[0]; const y = calc()[1];` Because we cannot determine if `calc` is side-effect free, we cannot inline the call to `calc()` to a destructuring array, as this changes the semantics of the program.

The test files I have created can be found in `./src/test/resources/js/`.


### Quickstart

* `./gradlew run --quiet --args <input file>`
* Example: `./gradlew run --quiet --args src/test/resources/js/reorder.js`

### Alternative (build then run)

 * `./gradlew build` will build an executable jar in `./build/libs/jbinterview-1.0-SNAPSHOT.jar`.
* `java -jar build/libs/jbinterview-1.0-SNAPSHOT.jar src/test/resources/js/reorder.js`

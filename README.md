## `CharacterBuffer` an alternative to Java's `StringBuilder` and `StringBuffer`

Instances of this class are created using the *builder design pattern* through the static method `CharacterBuffer.with(int)` that receives the memory page size as parameter.

##Example:

Instantiating a `CharacterBuffer` with memory pages of `16KB` in 4 different ways: 

    CharacterBuffer a = CharacterBuffer.with(16384).bytes();
    CharacterBuffer b = CharacterBuffer.with(8192).chars();
    CharacterBuffer c = CharacterBuffer.with(16).kilo().bytes();
    CharacterBuffer d = CharacterBuffer.with(8).kilo().chars();
 
##CharacterBuffer's memory allocation difference:

 - When `StringBuilder` and `StringBuffer` reach the current capacity, they **double** the size of the internal `char` array.

 - When `CharacterBuffer` reach the current capacity, it allocates a **new memory page** that is a **fixed size** `char` array.

With this strategy the `CharacterBuffer` prevents `OutOfMemoryError`s when dealing with huge `String` concatenations.

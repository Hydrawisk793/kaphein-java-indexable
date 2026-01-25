# kaphein-java-indexable

Defines the contract of `Indexable`.

## What is `IndexableComplient` and `Indexable`?

`IndexableComplient` is an annotation for a contract to represent POJOs with `java.util.Map`-like capabilities.

`Indexable` is an interface to represent POJOs who compiles `IndexableComplient` and base abstract classes to implement `IndexableComplient` POJO classes.

## Why this contract is introduced?

Java classes does not have flexibilities compared to JavaScript objects because of strongly-typed language philosophy of Java.  
This is problematic when the schema of a data class can be changed without any noticies. One of examples for this would be response objects of Open API systems. When the systems add new properties, strongly-typed Java POJOs will simply ignore the properties or throw exceptions on runtime deserializations.

## Goals of `Indexable`

1. Represents POJOs that can have extra properties that are not declared in the class definitions.  
2. Supports properties whose names are not Java identifiers including URI-based names.  
3. Supports `java.util.Map`-like accesses on POJOs.  
4. Make POJO classes more be tolerent on sudden schema changes of incoming response data deserializations.  

## License

[MIT](./LICENSE)

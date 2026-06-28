# kaphein-java-indexable

Defines the contract of `Indexable`.

## What is `Indexable`?

`Indexable` is the name of a contract to represent POJOs with `java.util.Map`-like capabilities and it is also a name of Java `interface` to realize that contract.

## Why `Indexable`?

Java classes does not have flexibilities compared to JavaScript objects because of strongly-typed language philosophy of Java.  
This is problematic when the schema of a data class can be changed without any noticies. One of examples for this would be response objects of Open API systems. When the systems add new properties, strongly-typed Java POJOs will simply ignore the properties or throw exceptions on runtime deserializations.

## Goals of `Indexable`

1. Represents POJO classes that can have extra properties that are not declared in the class definitions.  
2. Supports properties whose names are not Java identifiers including URI-based names.  
3. Make POJO classes be tolerent on sudden schema changes of incoming response data.  

## License

[MIT](./LICENSE)

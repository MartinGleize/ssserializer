[![Build Status](https://travis-ci.org/MartinGleize/ssserializer.svg?branch=master)](https://travis-ci.org/MartinGleize/ssserializer)

# ssserializer

ssserializer, the Simple Scala Serializer, provides seamless serialization/deserialization capabilities for many Scala types without the need for any boilerplate code at all, and with a very low memory footprint at runtime.

It currently supports JSON serialization/deserialization of:
* Value types (Int, Long, Double, Boolean...)
* String
* Arrays
* Most sequence types like Seq, Vector, List
* Set, Map
* Standalone case classes
* Option
* Tuples of all arities

## Requirements

sbt 1.2.8

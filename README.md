# HashCounting

Here's a piece of code I wrote to understand hash collision probablities in Java. Because hashcode is an int, uuid to hashcode loses its uniqueness as 2^128 bins becomes 2^32 bins. The default settings show that the probablity  having a collision is 50%, even with as few as 77163 non-deterministically generated UUIDs.


# Toy-Language-Interpreter

Our mini interpreter uses the following main structures:
- Execution Stack (ExeStack): a stack of statements to execute the currrent program
- Table of Symbols (SymTable): a table which keeps the variables values
- Output (Out): that keeps all the mesages printed by the toy program
- File table : FileTable that manages the files opened in our Toy Language - is 
a dictionary mapping Toy Language file descriptor (that is an integer) to the following tuple
(filename, file descriptor from Java language). The Toy Language file descriptor is managed by
th FileTable implementation and is unique. The filename is a string and denotes the path to
the file. The file descriptor from Java language is an instance of the BufferedReader class.
We assume that a file can only be a text file that contains only non-zero positive integers, one
integer per line.
- Heap table : Heap is a dictionary of mappings (address, content) where the
address is an integer (the index of a location in the heap) while the content is an integer value

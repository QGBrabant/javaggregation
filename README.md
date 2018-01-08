# javaggregation

## What is it

This program is a java implementation of algorithms presented in the paper *Interpolation by lattice polynomial functions: a polynomial time algorithm* by Quentin Brabant, Miguel Couceiro and José Figueira (submitted for publication). It implements distributive lattices, lattice polynomial functions, and processes related to it.

## The jar file

It contains a compiled version of the programm, that you can download and launch. The command line for this is
```
java -jar latticepolynomials.jar [task] [options]
```

### Tasks
The task to execute is specified by the first argument:
* ``help``: displays arguments and  option names
* ``interpolation_test``: randomly generates a distributive lattice, a lattice polynomial, and data. From the data, computes the constraints for interpolating lattice polynomials, and display the time spent on this last task. For more details, see *Interpolation by lattice polynomial functions: a polynomial time algorithm*. Note that this task is executed for each combination of the options ``-n``, ``-m``, ``-k``, ``-p``, ``-g``, ``-nbrep``.
* ``lattice_gen``: Generates random distributive lattices and displays them. One lattice is generated for each combination of the options ``-k``,``-p``.

### Options
* ``-n`` arities, default: 1, 2, 3, 4, 5, 6, 10, 15, 20, 30
* ``-m`` number of instances in the data, default: 10, 100, 1000, 10000, 50000
* ``-k`` height of the lattice, default: 1, 2, 3, 5, 7, 10, 15, 20, 30
* ``-p`` rho parameter for lattice generation, default: 0.6
* ``-g`` maximal number of focal set of the lattice polynomial, default: 100
* ``-nbrep``, default: 1

### Example
```java -jar latticepolynomials.jar lattice_gen -k 1,2,3,4,5,7,10 -p 0.5```


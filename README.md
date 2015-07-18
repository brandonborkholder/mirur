# Mirur Eclipse Plugin

Mirur is an Eclipse plugin (http://mirur.io/) to visualize your arrays the way they were meant to be.  No more using Arrays.toString()! The existing visual debugger in Eclipse does a .toString() on any object you select in the Variables view. For large arrays, this might mean hanging your JVM. Besides the fact that looking at a long list of values is generally useless to understand what's happening.

Mirur can plot numerical arrays of any size. View 1-dimensional arrays using a line graph or a bar chart. View 2-dimensional data using a heatmap. Or even just view a histogram of all values in the array.

You can view anything that can be interpreted as a collection of numbers, including List<Number> or AtomicLong[].

![Screenshot](../gh-pages/images/mirur-example.jpg)


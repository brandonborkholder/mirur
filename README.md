# Mirur Eclipse Plugin

Mirur is an Eclipse plugin (http://mirur.io/) to visualize your arrays the way
they were meant to be.  No more using Arrays.toString()! The existing visual
debugger in Eclipse does a .toString() on any object you select in the
Variables view. For large arrays, this might mean hanging your JVM. Besides
the fact that looking at a long list of values is generally useless to
understand what's happening.

Mirur can plot numerical arrays of any size. View 1-dimensional arrays using a
line graph or a bar chart. View 2-dimensional data using a heatmap. Or even
just view a histogram of all values in the array.

You can view anything that can be interpreted as a collection of numbers,
including List<Number> or AtomicLong[].

![Screenshot](../gh-pages/images/mirur-example.jpg)

<a href="http://with-eclipse.github.io/" target="_blank">
<img alt="with-Eclipse logo" src="http://with-eclipse.github.io/with-eclipse-0.jpg" /></a>

## Building

The update-site branch contains the website and the update-site folder for
releases. Both the development branch (e.g. master) and update-site need to be
checked out alongside each other build. The update-site branch is in a folder
named mirur-update-site on the same level as the mirur repository for
development.

```
cd mirur
mvn clean package install
# this is a workaround for an issue in Tycho
mvn package install
```

Now the new build is in ../mirur-update-site/update-site/


# Mirur Eclipse Plugin

Mirur is an Eclipse plugin (http://mirur.io/) to visualize your arrays the way
they were meant to be.  No more using `Arrays.toString()`! The existing visual
debugger in Eclipse does a `.toString()` on any object you select in the
Variables view. For large arrays, this might mean hanging your JVM. Besides
the fact that looking at a long list of values is generally useless to
understand what's happening.

Mirur can plot numerical arrays of any size. View 1-dimensional arrays using a
line graph or a bar chart. View 2-dimensional data using a heatmap. Or even
just view a histogram of all values in the array.

You can view anything that can be interpreted as a collection of numbers,
including `List<Number>` or `AtomicLong[]`.

![Screenshot](../gh-pages/images/mirur-example.jpg)

![with-Eclipse logo](http://with-eclipse.github.io/with-eclipse-0.jpg)

## Building

Mirur resolves Eclipse plug-in APIs through the pinned target definition in
`releng/target-platform/mirur.target`. See `docs/development.md` for the selected
Eclipse release train, supported Eclipse matrix, and target-platform update
process.

Build and verify the project with Maven:

```
mvn clean verify
```

The p2 update-site repository is produced at
`mirur-repository/target/repository`. A normal `mvn install` or `mvn verify` does
not copy that repository into a sibling checkout. Release automation should
archive and publish the repository artifact from that target directory.

See `docs/release.md` for the release checklist, version/tag steps, signing and
checksum guidance, rollback procedure, and how to keep the existing public p2
update-site URL stable for current installs.

## Testing

You can run `mirur.mirur-ui` as an Eclipse application to test. However, you must add
the following JVM arguments to your debug configuration:

```
--add-exports java.base/java.lang=ALL-UNNAMED
--add-exports java.desktop/sun.awt=ALL-UNNAMED
--add-exports java.desktop/sun.java2d=ALL-UNNAMED
```

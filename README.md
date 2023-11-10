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

The `gh-pages` branch contains the website and the update-site folder for
releases. Both the development branch (e.g. `master`) and `gh-pages` need to be
checked out alongside each other build. The build assumes `gh-pages` is checked
out in a folder called `mirur-update-site` at the same level as the main code.
For example, `mirur` and `mirur-update-site` are sibling folders.

```
cd mirur
mvn versions:set -DnewVersion=X-SNAPSHOT && mvn tycho-versions:set-version -DnewVersion=X.qualifier
git commit -a -m "Version to X" && git tag X
mvn clean install
```

Now the new build is in `../mirur-update-site/update-site/`

```
cd ../mirur-update-site/update-site/
unzip content.jar && unzip artifacts.jar
java -jar ../eclipse/plugins/org.eclipse.equinox.p2.jarprocessor_1.*.jar \
  -processAll -pack -verbose -outputDir plugins plugins/mirur.mirur-ui_*
```

## Testing

You can run `mirur.mirur-ui` as an Eclipse application to test. However, you must add
the following JVM arguments to your debug configuration:

```
--add-exports java.base/java.lang=ALL-UNNAMED
--add-exports java.desktop/sun.awt=ALL-UNNAMED
--add-exports java.desktop/sun.java2d=ALL-UNNAMED
```

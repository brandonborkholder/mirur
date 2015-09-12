-injars '../lib/antlr-2.7.7.jar'
-outjars '../lib/antlr-2.7.7-repackaged.jar'
-injars '../lib/antlr-runtime-3.4.jar'
-outjars '../lib/antlr-runtime-3.4-repackaged.jar'
-injars '../lib/commons-lang3-3.3.2.jar'
-outjars '../lib/commons-lang3-3.3.2-repackaged.jar'
-injars '../lib/fastutil-6.5.12.jar'
-outjars '../lib/fastutil-6.5.12-repackaged.jar'
-injars '../lib/guava-15.0.jar'
-outjars '../lib/guava-15.0-repackaged.jar'
-injars '../lib/miglayout-core-4.2.jar'
-outjars '../lib/miglayout-core-4.2-repackaged.jar'
-injars '../lib/stringtemplate-3.2.1.jar'
-outjars '../lib/stringtemplate-3.2.1-repackaged.jar'
-injars '../lib/glg2d-0.4-SNAPSHOT.jar'
-outjars '../lib/glg2d-0.4-SNAPSHOT-repackaged.jar'

-injars '../target/classes/'

-libraryjars ../lib/(mirur.jdk-agent-*.jar;)

# something wrong with how it processes enums as part of a class in glimpse, f$*#s it up
-libraryjars '../lib/glimpse-core-2.2.0.jar'
-libraryjars '../lib/glimpse-extras-swt-2.2.0.jar'
-libraryjars '../lib/glimpse-util-2.2.0.jar'
-libraryjars '../lib/gluegen-rt-2.2.0.jar'
-libraryjars '../lib/gluegen-rt-main-2.2.0.jar'
-libraryjars '../lib/jogl-all-2.2.0.jar'
-libraryjars '../lib/jogl-all-main-2.2.0.jar'

-libraryjars '<java.home>/lib/rt.jar'
-libraryjars ../lib/(org.eclipse.*.jar;)
-libraryjars ../lib/(com.ibm.icu.*.jar;)
-libraryjars ../lib/(jdi.jar;)
-libraryjars ../lib/(jdimodel.jar;)

-target 1.7
-verbose

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn com.jogamp.plugin.**
-dontwarn com.jogamp.openal.**
-dontwarn com.metsci.glimpse.**

-printmapping mapping.log

-keepclassmembers class * { *; }

-optimizations !class/unboxing/enum

-keep,allowshrinking class ** { *; }


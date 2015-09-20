-injars '../target/classes/'
-outjars '../target/mirur-repackaged/'

-injars '../lib/commons-lang3-3.3.2.jar'
-injars '../lib/fastutil-6.5.12.jar'
-injars '../lib/guava-15.0.jar'
-injars '../lib/stringtemplate-3.2.1.jar'
-injars '../lib/glg2d-0.4-SNAPSHOT.jar'
-injars '../lib/antlr-2.7.7.jar'
-injars '../lib/antlr-runtime-3.4.jar'
-injars '../lib/miglayout-core-4.2.jar'
-injars '../lib/glimpse-core-2.2.0.jar'
-injars '../lib/glimpse-extras-swt-2.2.0.jar'
-injars '../lib/glimpse-util-2.2.0.jar'
-injars '../lib/gluegen-rt-2.2.0.jar'
-injars '../lib/gluegen-rt-main-2.2.0.jar'
-injars '../lib/jogl-all-2.2.0.jar'
-injars '../lib/jogl-all-main-2.2.0.jar'

-outjars ../lib/repackaged/

-libraryjars ../lib/(mirur.jdk-agent-*.jar;)

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

-dontobfuscate
-dontoptimize

-keep class mirur.** { *; }
-keep class com.metsci.glimpse.axis.tagged.Tag { *; }
-keep class * implements java.lang.Enum { *; }

-keep class com.jogamp.** { *; }
-keep class javax.media.** { *; }
-keep class jogamp.** { *; }
-keep class jogl.** { *; }
-keep class newt.** { *; }

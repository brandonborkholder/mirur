-injars ..\lib\fastutil-6.5.12.jar
-injars ..\lib\guava-15.0.jar
-outjars ..\lib\repackaged
-injars ..\lib\glimpse-core-2.2.0.jar
-injars ..\lib\glimpse-extras-swt-2.2.0.jar
-injars ..\lib\glimpse-util-2.2.0.jar
-injars ..\target\classes

-libraryjars ..\lib\commons-lang3-3.3.2.jar
-libraryjars ..\lib\stringtemplate-3.2.1.jar
-libraryjars ..\lib\glg2d-0.4-SNAPSHOT.jar
-libraryjars ..\lib\antlr-2.7.7.jar
-libraryjars ..\lib\antlr-runtime-3.4.jar
-libraryjars ..\lib\miglayout-core-4.2.jar
-libraryjars ..\lib\gluegen-rt-2.2.0.jar
-libraryjars ..\lib\gluegen-rt-main-2.2.0.jar
-libraryjars ..\lib\jogl-all-2.2.0.jar
-libraryjars ..\lib\jogl-all-main-2.2.0.jar
-libraryjars ..\lib(mirur.jdk-agent-*.jar;)
-libraryjars 'C:\Program Files\Java\jre1.8.0_60\lib\rt.jar'
-libraryjars ..\lib(org.eclipse.*.jar;)
-libraryjars ..\lib(com.ibm.icu.*.jar;)
-libraryjars ..\lib(jdi.jar;)
-libraryjars ..\lib(jdimodel.jar;)

-target 1.7
-dontoptimize
-dontobfuscate
-verbose
-dontwarn javax.annotation.**,javax.inject.**,com.jogamp.plugin.**,com.jogamp.openal.**


-keep class * extends java.lang.Enum {
    <fields>;
    <methods>;
}

-keep class mirur.** {
    <fields>;
    <methods>;
}

-keep class com.metsci.glimpse.** {
    <fields>;
    <methods>;
}

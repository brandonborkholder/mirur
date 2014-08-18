-injars '../target/classes'
-outjars '../target/obfuscated-classes'

-libraryjars ../lib
-libraryjars '../../mirur-agent/bin'
-libraryjars '<java.home>/lib/rt.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.ui_3.105.0.v20130522-1122.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.swt_3.102.1.v20140206-1334.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.jface_3.9.1.v20130725-1141.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.core.commands_3.6.100.v20130515-1857.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.ui.workbench_3.105.2.v20140211-1711.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.e4.ui.workbench3_0.12.0.v20130515-1857.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.core.runtime_3.9.100.v20131218-1515.jar'
-libraryjars '<eclipse.home>/plugins/javax.annotation_1.1.0.v201209060031.jar'
-libraryjars '<eclipse.home>/plugins/javax.inject_1.0.0.v20091030.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.osgi_3.9.1.v20140110-1610.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.equinox.common_3.6.200.v20130402-1505.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.core.jobs_3.5.300.v20130429-1813.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.core.runtime.compatibility.registry_3.5.200.v20130514-1256/runtime_registry_compatibility.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.equinox.registry_3.5.301.v20130717-1549.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.equinox.preferences_3.5.100.v20130422-1538.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.core.contenttype_3.4.200.v20130326-1255.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.equinox.app_1.3.100.v20130327-1442.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.debug.core_3.8.0.v20130514-0954.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.core.resources_3.8.101.v20130717-0806.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.debug.ui_3.9.0.v20130516-1713.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.jdt.debug_3.8.50.v20140317-1921/jdi.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.jdt.debug_3.8.50.v20140317-1921/jdimodel.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.ui.workbench.texteditor_3.8.101.v20130729-1318.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.jdt.debug.ui_3.6.250.v20140317-1921.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.jdt.core_3.9.50.v20140317-1741.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.jdt.compiler.apt_1.0.650.v20140316-1836.jar'
-libraryjars '<eclipse.home>/plugins/org.eclipse.jdt.compiler.tool_1.0.250.v20140316-1836.jar'

-target 1.7
-printmapping mapping.log
-verbose

-keep,allowoptimization class * {
   public <methods>;
   protected <methods>;
}

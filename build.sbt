import de.johoop.jacoco4sbt._


autoCompilerPlugins := true

jacoco.settings

jacoco.reportFormats in jacoco.Config := Seq(XMLReport("utf-8"), HTMLReport("utf-8"))

jacoco.excludes in jacoco.Config := Seq("**/minicp/examples/**/*.class")

lazy val root = (project in file(".")).settings(
    name := "minicp",
    libraryDependencies += "junit" % "junit" % "4.12" % "test",
    libraryDependencies ++= Seq("com.novocode" % "junit-interface" % "0.10" % "test"),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    javacOptions in doc := Seq("-source", "1.8"),
    javaOptions in run += "-Xmx2G"
  )



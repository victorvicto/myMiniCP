
autoCompilerPlugins := true

lazy val root = (project in file(".")).settings(
    name := "minicp",
    libraryDependencies += "junit" % "junit" % "4.12" % "test",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
    javacOptions ++= Seq("-source", "8", "-target", "8"),
    javacOptions in doc := Seq("-source", "1.8"),
    javaOptions in run += "-Xmx2G"
  )




    
lazy val root = (project in file(".")).
  settings(
    name := "minicp",
    libraryDependencies += "junit" % "junit" % "4.12" % "test",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
    javaOptions in run += "-Xmx2G"
    
  )





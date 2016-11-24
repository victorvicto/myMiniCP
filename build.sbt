
    
lazy val root = (project in file(".")).
  settings(
    name := "minicp",
    libraryDependencies += "junit" % "junit" % "4.12" % "test",
    scalaVersion := "2.12.0",
    javaOptions in run += "-Xmx2G"
    
  )





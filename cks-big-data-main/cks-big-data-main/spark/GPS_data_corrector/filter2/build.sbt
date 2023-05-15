name := "Filter2"

version := "0.1"

scalaVersion := "2.11.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.7"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.7"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.7"
libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.5.0"
libraryDependencies += "com.crealytics" %% "spark-excel" % "0.13.1"

assemblyJarName in assembly := s"${name.value}.jar"
mainClass := Some("Filter")
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}


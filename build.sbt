name := """Filtering"""
scalaVersion := "2.13.8"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.+" % Test
)
// https://mvnrepository.com/artifact/org.sangria-graphql/sangria
libraryDependencies += "org.sangria-graphql" %% "sangria" % "3.+"
libraryDependencies += "org.sangria-graphql" %% "sangria-play-json" % "2.+"
//https://mvnrepository.com/artifact/com.typesafe.slick/slick
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.+"
//https://mvnrepository.com/artifact/com.typesafe.play/play-slick-evolutions
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.+",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.+"
)
// https://mvnrepository.com/artifact/mysql/mysql-connector-java
libraryDependencies += "mysql" % "mysql-connector-java" % "8.+"
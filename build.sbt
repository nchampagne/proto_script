val projectName = "proto_script"

val settings = Seq(
  organization  := "com.sharecare",
  name          := projectName,
  version       := "1.0.0-SNAPSHOT",
  description   := "Server-side JS executor",
  scalaVersion  := "2.12.4",

  resolvers ++= Resolver.jcenterRepo +: Resolver.bintrayRepo("scalaz", "releases") +: {
    Seq(
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    )
  },

  libraryDependencies ++= ws +: guice +: Seq(
    "com.typesafe.play" %% "play-json" % "2.6.7"
  )
)

val root = (project in file(".")).enablePlugins(PlayScala)
  .enablePlugins(PlayScala)
  .settings(settings)
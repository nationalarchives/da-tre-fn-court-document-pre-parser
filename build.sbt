import Dependencies._

ThisBuild / scalaVersion := "2.13.12"
ThisBuild / version := "0.1.0"

val awsVersion = "2.21.38"

lazy val root = (project in file("."))
  .settings(
    name := "da-tre-fn-court-document-pre-parser",
    libraryDependencies ++= Seq(
      lambdaRuntimeInterfaceClient
    ),
    assembly / assemblyOutputPath := file("target/function.jar")
  )

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _                        => MergeStrategy.first
}

libraryDependencies ++= Seq(
  "io.cucumber" %% "cucumber-scala" % "8.19.0" % Test,
  "io.cucumber" % "cucumber-junit" % "7.14.1" % Test,
  "io.cucumber" % "cucumber-core" % "7.11.1" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "org.scalatest" %% "scalatest" % "3.2.17" % Test,
  "org.scalatestplus" %% "mockito-4-11" % "3.2.17.0" % Test,
  "uk.gov.nationalarchives" % "da-transform-schemas" % "2.5",
  "com.amazonaws" % "aws-lambda-java-events" % "3.11.1",
  "com.typesafe.play" %% "play-json" % "2.10.3",
  "software.amazon.awssdk" % "s3" % awsVersion,
  "software.amazon.awssdk" % "sso" % awsVersion,
  "software.amazon.awssdk" % "ssooidc" % awsVersion,
  "com.typesafe.play" %% "play-json" % "2.10.3",
  "com.jayway.jsonpath" % "json-path" % "2.6.0"
)

val circeVersion = "0.14.3"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic-extras"
).map(_ % circeVersion)

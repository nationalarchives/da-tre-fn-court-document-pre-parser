import Dependencies._

ThisBuild / scalaVersion := "2.13.12"
ThisBuild / version := "0.1.0"

val awsVersion = "2.23.3"

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
  "io.cucumber" % "cucumber-core" % "7.15.0" % Test,
  "io.cucumber" % "cucumber-junit" % "7.15.0" % Test,
  "io.cucumber" %% "cucumber-scala" % "8.20.0" % Test,
  "io.cucumber" % "cucumber-core" % "7.11.2" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "org.scalatest" %% "scalatest" % "3.2.17" % Test,
  "org.scalatestplus" %% "mockito-4-11" % "3.2.17.0" % Test,
  "uk.gov.nationalarchives" % "da-transform-schemas" % "2.5",
  "com.amazonaws" % "aws-lambda-java-events" % "3.11.4",
  "org.playframework" %% "play-json" % "3.0.1",
  "software.amazon.awssdk" % "s3" % awsVersion,
  "software.amazon.awssdk" % "sso" % awsVersion,
  "software.amazon.awssdk" % "ssooidc" % awsVersion,
  "org.playframework" %% "play-json" % "3.0.1",
  "com.jayway.jsonpath" % "json-path" % "2.8.0",
  "io.circe" %% "circe-generic-extras" % "0.14.3"
)

val circeVersion = "0.14.6"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

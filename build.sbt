import Dependencies._

ThisBuild / scalaVersion := "2.13.14"
ThisBuild / version := "0.1.0"

val awsVersion = "2.27.24"

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
  "io.cucumber" % "cucumber-core" % "7.18.1" % Test,
  "io.cucumber" % "cucumber-junit" % "7.18.1" % Test,
  "io.cucumber" %% "cucumber-scala" % "8.23.1" % Test,
  "io.cucumber" % "cucumber-core" % "7.11.2" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  "org.scalatestplus" %% "mockito-4-11" % "3.2.18.0" % Test,
  "uk.gov.nationalarchives" % "da-transform-schemas" % "2.8",
  "com.amazonaws" % "aws-lambda-java-events" % "3.13.0",
  "org.playframework" %% "play-json" % "3.0.1",
  "software.amazon.awssdk" % "s3" % awsVersion,
  "software.amazon.awssdk" % "sso" % awsVersion,
  "software.amazon.awssdk" % "ssooidc" % awsVersion,
  "org.playframework" %% "play-json" % "3.0.4",
  "com.jayway.jsonpath" % "json-path" % "2.9.0",
  "io.circe" %% "circe-generic-extras" % "0.14.4"
)

val circeVersion = "0.14.9"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

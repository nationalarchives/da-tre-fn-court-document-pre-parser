FROM  sbtscala/scala-sbt:eclipse-temurin-21.0.6_7_1.10.10_3.6.3 AS builder
COPY . /lambda/src/
WORKDIR /lambda/src/
RUN sbt assembly

FROM public.ecr.aws/lambda/java:21
RUN dnf upgrade -y rpm rpm-libs --releasever=2023.12.20261112 && dnf clean all
COPY --from=builder /lambda/src/target/function.jar ${LAMBDA_TASK_ROOT}/lib/
CMD ["uk.gov.nationalarchives.tre.Lambda::handleRequest"]

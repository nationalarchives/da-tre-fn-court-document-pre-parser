package uk.gov.nationalarchives.tre

import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{ListObjectsV2Request, ListObjectsV2Response}

import scala.jdk.CollectionConverters._

class S3Utils(s3Client: S3Client) {

  def getFileNames(bucketName: String, directory: String): Seq[String] = {
    val listObjectsRequest = ListObjectsV2Request.builder()
      .bucket(bucketName)
      .prefix(directory)
      .build()

    val listObjectsResponse: ListObjectsV2Response = s3Client.listObjectsV2(listObjectsRequest)

    listObjectsResponse.contents().asScala.map(_.key()).toSeq
      .map(_.replace(s"$directory/", ""))
  }
}

package uk.gov.nationalarchives.tre

import com.amazonaws.services.lambda.runtime.events.SNSEvent.{SNS, SNSRecord}
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import software.amazon.awssdk.services.s3.model.{ListObjectsV2Request, ListObjectsV2Response, S3Object}
import uk.gov.nationalarchives.common.messages.Producer.TDR
import uk.gov.nationalarchives.common.messages.Properties
import uk.gov.nationalarchives.tre.messages.bag.validate.ConsignmentType.COURT_DOCUMENT
import uk.gov.nationalarchives.tre.messages.bag.validate.{BagValidate, Parameters}

import java.time.Instant
import scala.jdk.CollectionConverters.{MapHasAsJava, SeqHasAsJava}

object TestHelpers {
  val treWorkingBucket = "tre-common-data"
  case class TDRRequest(reference: String, executionId: String, filesInUnpackedBag: Seq[String]) {
    val bagContentsLocation = s"$reference/$executionId/$reference"

    val bagValidateMessage: BagValidate = BagValidate(
      properties = Properties(
        messageType = "uk.gov.nationalarchives.tre.messages.bag.validate.BagValidate",
        timestamp = Instant.now().toString,
        function = "da-tre-fn-vb-bag-validation",
        producer = TDR,
        executionId = executionId,
        parentExecutionId = None
      ),
      parameters = Parameters(
        reference = reference,
        consignmentType = COURT_DOCUMENT,
        originator = Some(TDR.toString),
        s3Bucket = treWorkingBucket,
        s3ObjectRoot = bagContentsLocation
      )
    )
  }

  def bagContentsRequest(request: TDRRequest): ListObjectsV2Request = ListObjectsV2Request.builder()
    .bucket(treWorkingBucket)
    .prefix(request.bagContentsLocation)
    .build()

  def mockObjectListing(fileLocations: Seq[String], objectRoot: String): ListObjectsV2Response = {
    val objects = fileLocations.map(location => S3Object.builder().key(s"$objectRoot/$location").build())
    ListObjectsV2Response.builder().contents(objects.asJava).build()
  }
}

package uk.gov.nationalarchives.tre

import io.circe.generic.semiauto.deriveEncoder
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, parser}
import uk.gov.nationalarchives.common.messages.Producer.TRE
import uk.gov.nationalarchives.common.messages.{Producer, Properties}
import uk.gov.nationalarchives.da.messages.request.courtdocument.parse.{Parameters, ParserInstructions, RequestCourtDocumentParse}
import uk.gov.nationalarchives.tre.messages.bag.validate.{BagValidate, ConsignmentType}

import java.time.Instant
import java.util.UUID

object MessageParsingUtils {
  implicit val propertiesEncoder: Encoder[Properties] = deriveEncoder[Properties]
  implicit val producerEncoder: Encoder[Producer.Value] = Encoder.encodeEnumeration(Producer)
  implicit val producerDecoder: Decoder[Producer.Value] = Decoder.decodeEnumeration(Producer)
  implicit val consignmentTypeEncoder: Encoder[ConsignmentType.Value] = Encoder.encodeEnumeration(ConsignmentType)
  implicit val consignmentTypeDecoder: Decoder[ConsignmentType.Value] = Decoder.decodeEnumeration(ConsignmentType)
  implicit val courtDocumentPackagePrepareEncoder: Encoder[RequestCourtDocumentParse] = deriveEncoder[RequestCourtDocumentParse]

  def parseBagValidateMessage(message: String): BagValidate =
    parser.decode[BagValidate](message).fold(error => throw new RuntimeException(error), identity)

  def requestCourtDocumentParseJsonString(
    reference: String,
    originator: Option[String],
    s3Bucket: String,
    inputDocumentPath: String,
    bagValidateExecutionId: String,
    uuid:  String = UUID.randomUUID().toString,
    timestamp: String = Instant.now().toString
  ): String = {
    RequestCourtDocumentParse(
      properties = Properties(
        messageType = "uk.gov.nationalarchives.tre.messages.request.courtdocument.parse.RequestCourtDocumentParse",
        timestamp = timestamp,
        function = "da-tre-fn-court-document-pre-parser",
        producer = TRE,
        executionId = uuid,
        parentExecutionId = Some(bagValidateExecutionId)
      ),
      parameters = Parameters(
        s3Bucket = s3Bucket,
        s3Key = inputDocumentPath,
        reference = reference,
        originator = originator,
        parserInstructions = ParserInstructions(documentType = "")
      )
    ).asJson.toString
  }
}

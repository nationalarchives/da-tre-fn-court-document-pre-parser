package uk.gov.nationalarchives.tre

import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import uk.gov.nationalarchives.tre.MessageParsingUtils.{parseBagValidateMessage, requestCourtDocumentParseJsonString}
import uk.gov.nationalarchives.tre.messages.bag.validate.BagValidate

import scala.jdk.CollectionConverters.CollectionHasAsScala

class Lambda extends RequestHandler[SNSEvent, String] {
  val s3Utils = new S3Utils(S3Client.builder().region(Region.EU_WEST_2).build())

  override def handleRequest(event: SNSEvent, context: Context): String = {
    event.getRecords.asScala.toList match {
      case snsRecord :: Nil =>
        context.getLogger.log(s"Received SNS message: ${snsRecord.getSNS.getMessage}\n")
        val bagValidateMessage = parseBagValidateMessage(snsRecord.getSNS.getMessage)
        context.getLogger.log(s"Successfully parsed incoming message as BagValidate\n")
        val messageHandler = new BagValidateMessageHandler(s3Utils)
        val requestCourtDocumentParse = messageHandler.handleBagValidate(bagValidateMessage)
        context.getLogger.log(s"Returning request court document parse message: $requestCourtDocumentParse\n")
        requestCourtDocumentParse
      case _ => throw new RuntimeException("Single record expected; zero or multiple received")
    }
  }
}

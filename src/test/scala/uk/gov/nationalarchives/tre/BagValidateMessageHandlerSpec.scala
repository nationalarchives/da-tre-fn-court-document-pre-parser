package uk.gov.nationalarchives.tre

import com.jayway.jsonpath.JsonPath
import org.mockito.Mockito.when
import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers._
import org.scalatestplus.mockito.MockitoSugar
import software.amazon.awssdk.services.s3.S3Client
import uk.gov.nationalarchives.tre.TestHelpers.{TDRRequest, bagContentsRequest, mockObjectListing}

class BagValidateMessageHandlerSpec extends AnyFlatSpec with MockitoSugar {

  val unpackedBagOutputFiles: Seq[String] = Seq(
    "bag-info.txt",
    "bagit.txt",
    "file-av.csv",
    "file-ffid.csv",
    "file-metadata.csv",
    "manifest-sha256.txt",
    "tagmanifest-sha256.txt"
  )

  val unpackedBagInputFile = "data/test.docx"

  val requestWithInputData: TDRRequest = TDRRequest(
    reference = "TDR-2023-X1",
    executionId = "001",
    filesInUnpackedBag = unpackedBagOutputFiles :+ unpackedBagInputFile
  )

  val requestWithMissingInput: TDRRequest = TDRRequest(
    reference = "TDR-2023-X2",
    executionId = "002",
    filesInUnpackedBag = unpackedBagOutputFiles
  )

  val s3Client: S3Client = mock[S3Client]

  when(s3Client.listObjectsV2(bagContentsRequest(requestWithInputData)))
    .thenReturn(mockObjectListing(requestWithInputData.filesInUnpackedBag, requestWithInputData.bagContentsLocation))

  when(s3Client.listObjectsV2(bagContentsRequest(requestWithMissingInput)))
    .thenReturn(mockObjectListing(requestWithMissingInput.filesInUnpackedBag, requestWithMissingInput.bagContentsLocation))

  val s3Utils = new S3Utils(s3Client)

  val messageHandler = new BagValidateMessageHandler(s3Utils = s3Utils)

  "BagValidateMessageHandler" should "return a RequestCourtDocumentParse message with the correct input file location" in {
    val requestCourtDocumentParseString = messageHandler.handleBagValidate(requestWithInputData.bagValidateMessage)
    JsonPath.read[String](requestCourtDocumentParseString, "$.properties.messageType") should be ("uk.gov.nationalarchives.tre.messages.courtdocument.parse.RequestCourtDocumentParse")
    JsonPath.read[String](requestCourtDocumentParseString, "$.parameters.s3Bucket") should be ("tre-common-data")
    JsonPath.read[String](requestCourtDocumentParseString, "$.parameters.s3Key") should be ("TDR-2023-X1/001/TDR-2023-X1/data/test.docx")
  }

  it should "throw a runtime exception if no input file can be found" in {
    val exception = the [RuntimeException] thrownBy messageHandler.handleBagValidate(requestWithMissingInput.bagValidateMessage)
    exception.getMessage should be ("Unable to find expected court document input")
  }
}

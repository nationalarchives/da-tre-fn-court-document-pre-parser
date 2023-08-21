package uk.gov.nationalarchives.tre

import uk.gov.nationalarchives.tre.MessageParsingUtils.requestCourtDocumentParseJsonString
import uk.gov.nationalarchives.tre.messages.bag.validate.BagValidate

class BagValidateMessageHandler(s3Utils: S3Utils, environmentPrefix: String) {

  def handleBagValidate(bagValidate: BagValidate): String = {
    getInputDocumentPath(bagValidate) match {
      case Some(inputDocumentPath) =>
        requestCourtDocumentParseJsonString(
          environmentPrefix = environmentPrefix,
          reference = bagValidate.parameters.reference,
          originator = bagValidate.parameters.originator,
          inputDocumentPath = inputDocumentPath,
          bagValidateExecutionId = bagValidate.properties.executionId
        )
      case None =>
        throw new RuntimeException("Unable to find expected court document input")
    }
  }

  private def getInputDocumentPath(bagValidate: BagValidate): Option[String] = {
    s3Utils.getFileNames(bagValidate.parameters.s3Bucket, bagValidate.parameters.s3ObjectRoot)
      .find(name => name.startsWith("data/") && name.endsWith(".docx"))
      .map(filename => s"${bagValidate.parameters.s3ObjectRoot}/$filename")
  }
}

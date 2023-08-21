# da-tre-fn-court-document-pre-parser

A simple lambda that receives a `BagValidate` message and returns a `RequestCourtDocumentParse`.

The lambda looks for a file ending with `.docx` in the data directory of the object root is has been supplied. If it is there, it passes that on as the s3Key for the `RequestCourtDocumentParse`- if not, it throws an exception .

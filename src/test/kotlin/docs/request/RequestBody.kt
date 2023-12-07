package docs.request

import doc.RestDocumentBuilder
import doc.RestDocumentConsumer
import docs.RestDocument
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.RequestFieldsSnippet

open class RestDocumentRequestBody(
    override val consumer: RestDocumentConsumer<*>,
    override val builder: RestDocumentBuilder
) : RestDocument

fun RestDocumentRequest.requestBody(vararg descriptors: FieldDescriptor): RequestFieldsSnippet {
    return PayloadDocumentation.requestFields(listOf(*descriptors))
}

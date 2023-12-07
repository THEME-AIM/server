package docs

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.ResponseFieldsSnippet

open class ResponseBody(
    override val consumer: RestDocumentConsumer<*>,
    override val builder: RestDocumentBuilder
) : RestDocument

fun RestDocumentResponse.responseBody(vararg descriptors: FieldDescriptor): ResponseFieldsSnippet {
    return PayloadDocumentation.responseFields(listOf(*descriptors))
}
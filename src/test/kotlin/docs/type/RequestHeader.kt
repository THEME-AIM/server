package docs.type

import docs.request.RestDocumentRequest
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.RequestHeadersSnippet

fun RestDocumentRequest.requestHeaders(vararg descriptors: HeaderDescriptor): RequestHeadersSnippet {
    return HeaderDocumentation.requestHeaders(listOf(*descriptors))
}

infix fun String.meanHeader(description: String): HeaderDescriptor {
    return HeaderDocumentation.headerWithName(this).description(description)
}
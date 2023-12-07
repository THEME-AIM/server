package docs.type

import docs.response.RestDocumentResponse
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.ResponseHeadersSnippet


fun RestDocumentResponse.responseHeaders(vararg descriptors: HeaderDescriptor): ResponseHeadersSnippet {
    return HeaderDocumentation.responseHeaders(listOf(*descriptors))
}
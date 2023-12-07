package docs.response

import doc.RestDocumentBuilder
import doc.RestDocumentConsumer
import doc.visit
import docs.CustomDocument
import docs.RestDocument
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.headers.ResponseHeadersSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Snippet

open class RestDocumentResponse(
    var headers: ResponseHeadersSnippet? = null,
    var responseBody: ResponseFieldsSnippet? = null,
    override val consumer: RestDocumentConsumer<*>,
    override val builder: RestDocumentBuilder
) : RestDocument

fun CustomDocument.response(block: RestDocumentResponse.() -> Unit): Unit = RestDocumentResponse(
    consumer = consumer,
    builder = ResponseBuilder(
        requestSpecification = this.requestSpecification,
        snippets = this.builder.snippets
    )
).visit(block)


open class ResponseBuilder(
    override val requestSpecification: RequestSpecification,
    override val snippets: MutableList<Snippet>
) : RestDocumentBuilder {
    override fun addSnippets(document: RestDocument) {
        val response = document as RestDocumentResponse

        response.headers?.let { snippets.add(it) }
        response.responseBody?.let { snippets.add(it) }
    }

    override fun build(document: RestDocument): Unit = Unit
}
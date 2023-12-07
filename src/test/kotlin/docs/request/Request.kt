package docs.request

import doc.RestDocumentBuilder
import doc.RestDocumentConsumer
import doc.visit
import docs.CustomDocument
import docs.RestDocument
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.headers.RequestHeadersSnippet
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.snippet.Snippet

open class RestDocumentRequest(
    var pathVariable: PathParametersSnippet? = null,
    var headers: RequestHeadersSnippet? = null,
    var requestBody: RequestFieldsSnippet? = null,
    override val consumer: RestDocumentConsumer<*>,
    override val builder: RestDocumentBuilder
) : RestDocument

fun CustomDocument.request(block: RestDocumentRequest.() -> Unit): Unit = RestDocumentRequest(
    consumer = consumer,
    builder = RequestBuilder(
        requestSpecification = this.requestSpecification,
        snippets = this.builder.snippets
    )
).visit(block)

class RequestBuilder(
    override val requestSpecification: RequestSpecification,
    override val snippets: MutableList<Snippet>

) : RestDocumentBuilder {
    override fun addSnippets(document: RestDocument) {
        val request = document as RestDocumentRequest
        request.pathVariable?.let { snippets.add(it) }
        request.headers?.let { snippets.add(it) }
        request.requestBody?.let { snippets.add(it) }
    }

    override fun build(document: RestDocument): Unit = Unit
}
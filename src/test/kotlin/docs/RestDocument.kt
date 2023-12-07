package docs

import io.restassured.RestAssured
import io.restassured.http.Method
import io.restassured.specification.RequestSpecification
import org.springframework.http.HttpStatus
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation
import org.springframework.restdocs.snippet.Snippet

interface RestDocument {
    val consumer: RestDocumentConsumer<*>
    val builder: RestDocumentBuilder
}

open class CustomDocument(
    var statusCode: HttpStatus = HttpStatus.OK,
    var url: String? = null,
    var method: Method? = null,
    var requestBodyValue: String? = null,
    var pathVariables: List<String> = emptyList(),
    val identifier: String,
    val requestSpecification: RequestSpecification,
    override val consumer: RestDocumentConsumer<*>,
    override val builder: RestDocumentBuilder,
) : RestDocument

open class CustomDocumentBuilder(
    override val requestSpecification: RequestSpecification,
    override val snippets: MutableList<Snippet>
) : RestDocumentBuilder {
    override fun addSnippet(document: RestDocument): Unit = Unit

    override fun build(document: RestDocument) {
        val doc = document as CustomDocument
        RestAssured.given(requestSpecification)
            .contentType("application/json")
            .accept("application/json")
            .apply {
                if (doc.requestBodyValue != null) {
                    this.body(doc.requestBodyValue)
                }
            }
            .filter(
                RestAssuredRestDocumentation.document(
                    doc.identifier,
                    *snippets.toTypedArray()
                )
            )
            .`when`()
            .request(
                doc.method, doc.url, *doc.pathVariables.toTypedArray()
            )
            .then()
            .assertThat()
            .statusCode(doc.statusCode.value())
    }
}

fun RequestSpecification.makeDocument(identifier: String, block: CustomDocument.() -> Unit): RequestSpecification {
    val consumer = RestDocumentStreamBuilder(this)
    return CustomDocument(
        identifier = identifier,
        requestSpecification = this,
        consumer = consumer,
        builder = CustomDocumentBuilder(requestSpecification = request(), snippets = mutableListOf()),
    ).visitAndFinalize(consumer, block)
}
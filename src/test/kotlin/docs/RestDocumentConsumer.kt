package docs

import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.snippet.Snippet

interface RestDocumentBuilder {
    val requestSpecification: RequestSpecification
    val snippets: MutableList<Snippet>

    fun addSnippet(document: RestDocument)
    fun build(document: RestDocument)
}

interface RestDocumentConsumer<R> {
    val requestSpecification: RequestSpecification

    fun onDocumentStart(restDocs: RestDocument)
    fun onDocumentEnd(restDocs: RestDocument)
    fun onDocumentError(restDocs: RestDocument, exception: Throwable): Unit = throw exception
    fun finish(): R
}

fun <T : RestDocument> T.visit(block: T.() -> Unit) {
    consumer.onDocumentStart(this)
    try {
        this.block()
    } catch (error: Throwable) {
        consumer.onDocumentError(this, error)
    } finally {
        consumer.onDocumentEnd(this)
    }
}

fun <T : RestDocument, R> T.visitAndFinalize(consumer: RestDocumentConsumer<R>, block: T.() -> Unit): R {
    if (this.consumer !== consumer) {
        throw IllegalArgumentException("consumer is not matched")
    }
    visit(block)
    
    builder.build(this)
    return consumer.finish()
}

class RestDocumentStreamBuilder<O : RequestSpecification>(
    override val requestSpecification: O,
) : RestDocumentConsumer<O> {
    override fun onDocumentStart(restDocs: RestDocument) {
        restDocs.builder.addSnippet(restDocs)
    }

    override fun onDocumentEnd(restDocs: RestDocument) {
        restDocs.builder.addSnippet(restDocs)
    }

    override fun finish(): O = requestSpecification

}
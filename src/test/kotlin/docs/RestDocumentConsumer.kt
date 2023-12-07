package doc

import docs.RestDocument
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.snippet.Snippet

interface RestDocumentBuilder {
    val requestSpecification: RequestSpecification
    val snippets: MutableList<Snippet>

    fun addSnippets(document: RestDocument)
    fun build(document: RestDocument)
}

interface RestDocumentConsumer<out R> {
    val requestSpecification: RequestSpecification

    fun onDocumentStart(restDocs: RestDocument)
    fun onDocumentEnd(restDocs: RestDocument)
    fun onDocumentError(restDocs: RestDocument, exception: Throwable): Unit = throw exception
    fun finish(): R
}

@Suppress("TooGenericExceptionCaught")
fun <T : RestDocument> T.visit(block: T.() -> Unit) {
    consumer.onDocumentStart(this)
    try {
        this.block()
    } catch (err: Throwable) {
        consumer.onDocumentError(this, err)
    } finally {
        consumer.onDocumentEnd(this)
    }
}

fun <T : RestDocument, R> T.visitAndFinalize(consumer: RestDocumentConsumer<R>, block: T.() -> Unit): R {
    if (this.consumer !== consumer) {
        throw IllegalArgumentException("Wrong exception")
    }

    visit(block)
    return consumer.finish()
}

class RestDocumentStreamBuilder<O : RequestSpecification>(
    override val requestSpecification: O
) : RestDocumentConsumer<O> {

    override fun onDocumentStart(restDocs: RestDocument) {
        restDocs.builder.addSnippets(restDocs)
    }

    override fun onDocumentEnd(restDocs: RestDocument) {
        restDocs.builder.build(restDocs)
    }

    override fun finish(): O = requestSpecification
}
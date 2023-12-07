package base

import com.google.gson.Gson
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration


abstract class AbstractControllerTest(
    private val port: Int
) {
    protected lateinit var spec: RequestSpecification

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider?) {
        this.spec = RequestSpecBuilder()
            .setPort(port)
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(
                        modifyHeaders()
                            .remove("Pragma").remove("Expires").remove("Cache-Control").remove("Connection")
                            .remove("X-Content-Type-Options").remove("X-XSS-Protection").remove("Date"),
                        prettyPrint()
                    )
            )
            .build()
    }

    fun <T> T.toJson(): String = Gson().toJson(this)
}
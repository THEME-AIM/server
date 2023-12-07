import com.aim.server.domain.admin.const.ConfigConsts
import com.aim.server.domain.admin.dto.AdminConfigData
import com.google.gson.Gson
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.TestPropertySource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension::class)
@TestPropertySource("classpath:application-test.properties")
abstract class AbstractControllerTest(
    private val port: Int
) {
    protected lateinit var spec: RequestSpecification

    protected fun getSessionId(): String {
        val body = AdminConfigData.SignInRequest(
            ConfigConsts.DEFAULT_ADMIN_USERNAME_VALUE,
            ConfigConsts.DEFAULT_ADMIN_PASSWORD_VALUE
        ).toJson()

        return RestAssured.given(this.spec)
            .contentType("application/json")
            .body(body)
            .`when`()
            .post("/api/admin/sign-in")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .response()
            .sessionId
    }

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
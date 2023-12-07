package docs.response

import doc.RestDocumentBuilder
import doc.RestDocumentConsumer
import docs.RestDocument
import docs.type.*
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

fun RestDocumentResponse.baseResponseBody(vararg descriptor: FieldDescriptor): ResponseFieldsSnippet {
    val baseResponse = listOf(
        "isSuccess" type BOOLEAN means "성공여부",
        "code" type NUMBER means "상태 코드",
        "message" type STRING means "메시지",
    )
    return PayloadDocumentation.responseFields(listOf(*baseResponse.toTypedArray(), *descriptor))
}

fun RestDocumentResponse.errorResponseBody(): ResponseFieldsSnippet {
    val errorResponse = listOf(
        "isSuccess" type BOOLEAN means "성공여부",
        "code" type NUMBER means "상태 코드",
        "message" type STRING means "메시지",
        "errors" type ARRAY means "에러 목록",
        "errors[].field" type STRING means "에러 필드",
        "errors[].value" type STRING means "에러 값",
        "errors[].code" type STRING means "에러 코드",
        "errors[].reason" type STRING means "에러 이유",
    )
    return PayloadDocumentation.responseFields(listOf(*errorResponse.toTypedArray()))
}

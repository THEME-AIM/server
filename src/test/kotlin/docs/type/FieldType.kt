package docs.type

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.AbstractDescriptor
import org.springframework.restdocs.snippet.Attributes

sealed class RestDocumentFieldType(
    val type: JsonFieldType
)

object ARRAY : RestDocumentFieldType(JsonFieldType.ARRAY)
object BOOLEAN : RestDocumentFieldType(JsonFieldType.BOOLEAN)
object NUMBER : RestDocumentFieldType(JsonFieldType.NUMBER)
object OBJECT : RestDocumentFieldType(JsonFieldType.OBJECT)
object STRING : RestDocumentFieldType(JsonFieldType.STRING)
object NULL : RestDocumentFieldType(JsonFieldType.NULL)
object ANY : RestDocumentFieldType(JsonFieldType.VARIES)
object FILE : RestDocumentFieldType(JsonFieldType.VARIES)
object DATE : RestDocumentFieldType(JsonFieldType.STRING)
object DATETIME : RestDocumentFieldType(JsonFieldType.STRING)
object LIST : RestDocumentFieldType(JsonFieldType.ARRAY)

infix fun FieldDescriptor.means(description: String): FieldDescriptor {
    return this.description(description)
}

infix fun String.type(
    docsFieldType: RestDocumentFieldType
): FieldDescriptor {
    return PayloadDocumentation.fieldWithPath(this).type(docsFieldType.type)
}

infix fun FieldDescriptor.require(
    isRequired: Boolean
): FieldDescriptor {
    return if (isRequired) this else this.optional()
}

fun <T : AbstractDescriptor<T>> AbstractDescriptor<T>.defaultValue(value: Any?): T {
    return this.attributes(Attributes.Attribute("default", value ?: "null"))
}

infix fun FieldDescriptor.default(
    value: Any?
): FieldDescriptor {
    return this.defaultValue(value)
}
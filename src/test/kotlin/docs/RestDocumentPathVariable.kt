package docs

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.request.RequestDocumentation

fun RestDocumentRequest.pathVariables(vararg descriptors: ParameterDescriptor): PathParametersSnippet {
    return RequestDocumentation.pathParameters(listOf(*descriptors))
}

infix fun String.meanPath(description: String): ParameterDescriptor {
    return RequestDocumentation.parameterWithName(this).description(description)
}
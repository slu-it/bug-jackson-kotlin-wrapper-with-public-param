package example

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class IssueTests {

    private val objectMapper = jacksonObjectMapper()
    private val json = """{"foo":42.123}"""

    private val dtoWithPublicProperty = DtoWithPublicPropery(WrapperWithPublicValue(BigDecimal("42.123")))
    private val dtoWithPrivateProperty = DtoWithPrivateProperty(WrapperWithPrivateValue(BigDecimal("42.123")))

    @Test
    fun `deserialization - fails if it is a public property`() {
        val actual = objectMapper.readValue<DtoWithPublicPropery>(json)
        assertEquals(dtoWithPublicProperty, actual)
    }

    @Test
    fun `serialization - works if it is a public property`() {
        val actual = objectMapper.writeValueAsString(dtoWithPublicProperty)
        assertEquals(json, actual)
    }

    @Test
    fun `deserialization - works when it is a private property`() {
        val actual = objectMapper.readValue<DtoWithPrivateProperty>(json)
        assertEquals(dtoWithPrivateProperty, actual)
    }

    @Test
    fun `serialization - works if it is a private property`() {
        val actual = objectMapper.writeValueAsString(dtoWithPrivateProperty)
        assertEquals(json, actual)
    }

}

data class DtoWithPublicPropery(
    val foo: WrapperWithPublicValue
    // more properties
)

data class DtoWithPrivateProperty(
    val foo: WrapperWithPrivateValue
    // more properties
)

data class WrapperWithPublicValue(@JsonValue val value: BigDecimal) {
    init {
        // validation like upper and lower bounds etc.
    }

    fun toBigDecimal() = value
}

data class WrapperWithPrivateValue(@JsonValue private val value: BigDecimal) {
    init {
        // validation like upper and lower bounds etc.
    }

    fun toBigDecimal() = value
}

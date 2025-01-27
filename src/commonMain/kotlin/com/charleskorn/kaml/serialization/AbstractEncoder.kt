package com.charleskorn.kaml.serialization


import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder

@ExperimentalSerializationApi
public abstract class AbstractEncoder : Encoder, CompositeEncoder {
    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = this

    override fun endStructure(descriptor: SerialDescriptor) {}

    /**
     * Invoked before writing an element that is part of the structure to determine whether it should be encoded.
     * Element information can be obtained from the [descriptor] by the given [index].
     *
     * @return `true` if the value should be encoded, false otherwise
     */
    public open fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean = true

    /**
     * Invoked to encode a value when specialized `encode*` method was not overridden.
     */
    public open fun encodeValue(value: Any): Unit =
        throw SerializationException("Non-serializable ${value::class} is not supported by ${this::class} encoder")

    override fun encodeNull() {
        throw SerializationException("'null' is not supported by default")
    }

    override fun encodeBoolean(value: Boolean): Unit = encodeValue(value)
    override fun encodeByte(value: Byte): Unit = encodeValue(value)
    override fun encodeShort(value: Short): Unit = encodeValue(value)
    override fun encodeInt(value: Int): Unit = encodeValue(value)
    override fun encodeLong(value: Long): Unit = encodeValue(value)
    override fun encodeFloat(value: Float): Unit = encodeValue(value)
    override fun encodeDouble(value: Double): Unit = encodeValue(value)
    override fun encodeChar(value: Char): Unit = encodeValue(value)
    override fun encodeString(value: String): Unit = encodeValue(value)
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int): Unit = encodeValue(index)

    override fun encodeInline(descriptor: SerialDescriptor): Encoder = this

    // Delegating implementation of CompositeEncoder
    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) { if (encodeElement(descriptor, index)) encodeBoolean(value) }
    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) { if (encodeElement(descriptor, index)) encodeByte(value) }
    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) { if (encodeElement(descriptor, index)) encodeShort(value) }
    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) { if (encodeElement(descriptor, index)) encodeInt(value) }
    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) { if (encodeElement(descriptor, index)) encodeLong(value) }
    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) { if (encodeElement(descriptor, index)) encodeFloat(value) }
    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) { if (encodeElement(descriptor, index)) encodeDouble(value) }
    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) { if (encodeElement(descriptor, index)) encodeChar(value) }
    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) { if (encodeElement(descriptor, index)) encodeString(value) }

    final override fun encodeInlineElement(
        descriptor: SerialDescriptor,
        index: Int
    ): Encoder =
        if (encodeElement(descriptor, index)) encodeInline(descriptor.getElementDescriptor(index)) else NoOpEncoder

    override fun <T : Any?> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        if (encodeElement(descriptor, index))
            encodeSerializableValue(serializer, value)
    }

    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        if (encodeElement(descriptor, index))
            encodeNullableSerializableValue(serializer, value)
    }

}

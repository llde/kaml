/*

   Copyright 2018-2023 Charles Korn.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package com.charleskorn.kaml

/**
 * Configuration options for parsing YAML to objects and serialising objects to YAML.
 *
 * * [encodeDefaults]: set to false to not write default property values to YAML (defaults to `true`)
 * * [strictMode]: set to true to throw an exception when reading an object that has an unknown property, or false to ignore unknown properties (defaults to `true`)
 * * [extensionDefinitionPrefix]: prefix used on root-level keys (where document root is an object) to define extensions that can later be merged (defaults to `null`, which disables extensions altogether). See https://batect.dev/docs/reference/config#anchors-aliases-extensions-and-merging for example.
 * * [polymorphismStyle]: how to read or write the type of a polymorphic object:
 *    * [PolymorphismStyle.Tag]: use a YAML tag (eg. `!<typeOfThing> { property: value }`)
 *    * [PolymorphismStyle.Property]: use a property (eg. `{ type: typeOfThing, property: value }`)
 * * [polymorphismPropertyName]: property name to use when [polymorphismStyle] is [PolymorphismStyle.Property]
 * * [encodingIndentationSize]: number of spaces to use as indentation when encoding objects as YAML
 * * [breakScalarsAt]: maximum length of scalars when encoding objects as YAML (scalars exceeding this length will be split into multiple lines)
 * * [sequenceStyle]: how sequences (aka lists and arrays) should be formatted. See [SequenceStyle] for an example of each
 * * [ambiguousQuoteStyle]: how strings should be escaped when [singleLineStringStyle] is [SingleLineStringStyle.PlainExceptAmbiguous] and the value is ambiguous
 * * [sequenceBlockIndent]: number of spaces to use as indentation for sequences, if [sequenceStyle] set to [SequenceStyle.Block]
 * * [allowAnchorsAndAliases]: set to true to allow anchors and aliases when decoding YAML (defaults to `false`)
 */
public data class YamlConfiguration constructor(
    var encodeDefaults: Boolean = true,
    var strictMode: Boolean = true,
    var extensionDefinitionPrefix: String? = null,
    var polymorphismStyle: PolymorphismStyle = PolymorphismStyle.Property,
    var polymorphismPropertyName: String = "type",
    var encodingIndentationSize: Int = 2,
    var breakScalarsAt: Int = 80,
    var sequenceStyle: SequenceStyle = SequenceStyle.Block,
    var singleLineStringStyle: SingleLineStringStyle = SingleLineStringStyle.DoubleQuoted,
    var multiLineStringStyle: MultiLineStringStyle = singleLineStringStyle.multiLineStringStyle,
    var ambiguousQuoteStyle: AmbiguousQuoteStyle = AmbiguousQuoteStyle.DoubleQuoted,
    var enumQuoteStyle: SingleLineStringStyle = SingleLineStringStyle.DoubleQuoted,
    var sequenceBlockIndent: Int = 0,
    var allowAnchorsAndAliases: Boolean = false,
    var yamlNamingStrategy: YamlNamingStrategy? = null,
)

public enum class PolymorphismStyle {
    Tag,
    Property,
    None,
}

public enum class SequenceStyle {
    /**
     * The block form, eg
     * ```yaml
     * - 1
     * - 2
     * - 3
     * ```
     */
    Block,

    /**
     * The flow form, eg
     * ```yaml
     * [1, 2, 3]
     * ```
     */
    Flow,
}

public enum class MultiLineStringStyle {
    Literal,
    DoubleQuoted,
    SingleQuoted,
    Plain,
}

public enum class SingleLineStringStyle {
    DoubleQuoted,
    SingleQuoted,
    Plain,

    /**
     * This is the same as [SingleLineStringStyle.Plain], except strings that could be misinterpreted as other types
     * will be quoted with the escape style defined in [AmbiguousQuoteStyle].
     *
     * For example, the strings "True", "0xAB", "1" and "1.2" would all be quoted,
     * while "1.2.3" and "abc" would not be quoted.
     */
    PlainExceptAmbiguous,
    ;

    public val multiLineStringStyle: MultiLineStringStyle
        get() = when (this) {
            DoubleQuoted -> MultiLineStringStyle.DoubleQuoted
            SingleQuoted -> MultiLineStringStyle.SingleQuoted
            Plain -> MultiLineStringStyle.Plain
            PlainExceptAmbiguous -> MultiLineStringStyle.Plain
        }
}

public enum class AmbiguousQuoteStyle {
    DoubleQuoted,
    SingleQuoted,
}

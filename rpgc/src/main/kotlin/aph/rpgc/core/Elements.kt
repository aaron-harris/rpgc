package aph.rpgc.core

interface RpgElement

typealias Label = String
typealias Paragraph = String

data class Block(
    val label: Label,
    val paragraphs: List<Paragraph>
)

data class GenericElement(
    val name: Label,
    val description: List<Paragraph> = emptyList(),
    val blocks: List<Block> = emptyList()
) : RpgElement

package com.example.tcgdex_app.data

import com.example.tcgdex_app.data.remote.dto.*
import junit.framework.TestCase.*
import org.junit.Test

class MappersTest {

    @Test
    fun `CardDto maps id and name correctly`() {
        val dto = CardDto(id = "xy1-1", localId = "1", name = "Bulbasaur", image = null)

        val result = dto.toDomainModel()

        assertEquals("xy1-1", result.id)
        assertEquals("Bulbasaur", result.name)
    }

    @Test
    fun `CardDto with image appends low quality suffix`() {
        val dto = CardDto(
            id = "xy1-1",
            localId = "1",
            name = "Bulbasaur",
            image = "https://example.com/xy1/1"
        )

        val result = dto.toDomainModel()

        assertEquals("https://example.com/xy1/1/low.jpg", result.imageUrl)
    }

    @Test
    fun `CardDto without image returns empty imageUrl`() {
        val dto = CardDto(id = "xy1-1", localId = "1", name = "Bulbasaur", image = null)

        val result = dto.toDomainModel()

        assertTrue(result.imageUrl.isEmpty())
    }

    @Test
    fun `CardDetailsDto with image appends high quality suffix`() {
        val dto = buildCardDetailsDto(image = "https://example.com/xy1/1")

        val result = dto.toDomainModel()

        assertEquals("https://example.com/xy1/1/high.jpg", result.imageUrl)
    }

    @Test
    fun `CardDetailsDto without image returns empty imageUrl`() {
        val dto = buildCardDetailsDto(image = null)

        val result = dto.toDomainModel()

        assertTrue(result.imageUrl.isEmpty())
    }

    @Test
    fun `CardDetailsDto maps set name correctly`() {
        val dto = buildCardDetailsDto(set = SetDetailsDto(id = "xy1", name = "XY Base Set"))

        val result = dto.toDomainModel()

        assertEquals("XY Base Set", result.set)
    }

    @Test
    fun `CardDetailsDto without set returns empty string`() {
        val dto = buildCardDetailsDto(set = null)

        val result = dto.toDomainModel()

        assertTrue(result.set.isEmpty())
    }

    @Test
    fun `CardDetailsDto filters weaknesses without type`() {
        val dto = buildCardDetailsDto(
            weaknesses = listOf(
                WeaknessDto(type = "Fire", value = "×2"),
                WeaknessDto(type = null, value = "×2"),
            )
        )

        val result = dto.toDomainModel()

        assertEquals(1, result.weaknesses.size)
        assertEquals("Fire", result.weaknesses[0].type)
    }

    @Test
    fun `CardDetailsDto filters abilities without name or type`() {
        val dto = buildCardDetailsDto(
            abilities = listOf(
                AbilityDto(type = "Ability", name = "Overgrow", effect = "Powers up Grass moves"),
                AbilityDto(type = null, name = "Invalid", effect = null),
                AbilityDto(type = "Ability", name = null, effect = null),
            )
        )

        val result = dto.toDomainModel()

        assertEquals(1, result.abilities.size)
        assertEquals("Overgrow", result.abilities[0].name)
    }

    @Test
    fun `CardDetailsDto maps attack damage correctly`() {
        val dto = buildCardDetailsDto(
            attacks = listOf(
                AttackDto(name = "Tackle", cost = listOf("Colorless"), damage = "10"),
                AttackDto(name = "Razor Leaf", cost = listOf("Grass", "Grass"), damage = null),
            )
        )

        val result = dto.toDomainModel()

        assertEquals("10", result.attacks[0].damage)
        assertTrue(result.attacks[1].damage.isEmpty())
    }

    @Test
    fun `CardDetailsDto maps nullable fields correctly`() {
        val dto = buildCardDetailsDto(
            illustrator = null,
            rarity = null,
            evolveFrom = null,
            description = null,
            stage = null,
        )

        val result = dto.toDomainModel()

        assertTrue(result.illustrator.isEmpty())
        assertTrue(result.rarity.isEmpty())
        assertNull(result.evolveFrom)
        assertNull(result.description)
        assertNull(result.stage)
    }

    private fun buildCardDetailsDto(
        id: String = "xy1-1",
        name: String = "Bulbasaur",
        image: String? = "https://example.com/xy1/1",
        illustrator: String? = "Ken Sugimori",
        rarity: String? = "Common",
        set: SetDetailsDto? = SetDetailsDto(id = "xy1", name = "XY Base Set"),
        hp: Int? = 60,
        types: List<String> = listOf("Grass"),
        evolveFrom: String? = null,
        description: String? = null,
        stage: String? = "Basic",
        abilities: List<AbilityDto> = emptyList(),
        attacks: List<AttackDto> = emptyList(),
        weaknesses: List<WeaknessDto> = emptyList(),
        retreat: Int? = 1,
    ) = CardDetailsDto(
        id = id,
        name = name,
        image = image,
        illustrator = illustrator,
        rarity = rarity,
        set = set,
        hp = hp,
        types = types,
        evolveFrom = evolveFrom,
        description = description,
        stage = stage,
        abilities = abilities,
        attacks = attacks,
        weaknesses = weaknesses,
        retreat = retreat,
    )
}
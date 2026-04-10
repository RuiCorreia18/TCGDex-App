package com.example.tcgdex_app.data

import com.example.tcgdex_app.data.remote.dto.*
import com.example.tcgdex_app.domain.model.*

fun CardDto.toDomainModel(): Card {
    return Card(
        id = id,
        name = name,
        imageUrl = image?.let { "$it/low.jpg" }.orEmpty(),
    )
}

fun CardDetailsDto.toDomainModel(): CardDetails {
    return CardDetails(
        id = id,
        name = name,
        imageUrl = image?.let { "$it/high.jpg" }.orEmpty(),
        category = category,
        illustrator = illustrator.orEmpty(),
        rarity = rarity.orEmpty(),
        set = this.set?.name.orEmpty(),
        hp = hp,
        types = types,
        evolveFrom = evolveFrom,
        description = description,
        stage = stage,
        abilities = abilities.mapNotNull { it.toDomainModel() },
        attacks = attacks.map { it.toDomainModel() },
        weaknesses = weaknesses.mapNotNull { it.toDomainModel() },
        retreat = retreat,
    )
}

private fun AbilityDto.toDomainModel(): Ability? {
    val name = name ?: return null
    val type = type ?: return null

    return Ability(
        type = type,
        name = name,
        effect = effect,
    )
}

private fun AttackDto.toDomainModel(): Attack {
    return Attack(
        name = name,
        cost = cost,
        damage = damage.orEmpty(),
        effect = effect,
    )
}

private fun WeaknessDto.toDomainModel(): Weakness? {
    val type = type ?: return null
    return Weakness(
        type = type,
        value = value.orEmpty(),
    )
}
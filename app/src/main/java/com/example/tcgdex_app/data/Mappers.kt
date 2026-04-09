package com.example.tcgdex_app.data

import com.example.tcgdex_app.data.remote.dto.AttackDto
import com.example.tcgdex_app.data.remote.dto.CardDetailsDto
import com.example.tcgdex_app.data.remote.dto.CardDto
import com.example.tcgdex_app.data.remote.dto.WeaknessDto
import com.example.tcgdex_app.domain.model.Attack
import com.example.tcgdex_app.domain.model.Card
import com.example.tcgdex_app.domain.model.CardDetails
import com.example.tcgdex_app.domain.model.Weakness

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
        attacks = attacks.map { it.toDomainModel() },
        weaknesses = weaknesses.mapNotNull { it.toDomainModel() },
        retreat = retreat,

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
package com.example.tcgdex_app.presentation.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tcgdex_app.domain.model.*
import com.example.tcgdex_app.ui.theme.TCGDexAppTheme


@Composable
fun CardDetailsRoute(
    onBack: () -> Unit,
    viewModel: CardDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CardDetailsScreen(
        uiState = uiState,
        onBack = onBack,
        onRetry = viewModel::retry,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailsScreen(
    uiState: CardDetailsUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.data?.name ?: "Card Details",
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    ErrorContent(
                        message = uiState.error,
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                uiState.data != null -> {
                    CardDetailsContent(card = uiState.data)
                }
            }
        }
    }
}

@Composable
private fun CardDetailsContent(
    card: CardDetails,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        item {
            CardImageHeader(card = card)
        }

        // Core info chips (type, category, rarity)
        item {
            CardChipsRow(card = card)
        }

        // Stats section (HP, retreat, stage, evolveFrom)
        item {
            CardStatsSection(card = card)
        }

        if (!card.description.isNullOrBlank()) {
            item {
                DescriptionSection(description = card.description)
            }
        }

        if (card.attacks.isNotEmpty()) {
            item {
                AttacksSection(attacks = card.attacks)
            }
        }

        if (card.weaknesses.isNotEmpty()) {
            item {
                WeaknessesSection(weaknesses = card.weaknesses)
            }
        }

        // Set & illustrator footer
        item {
            CardFooter(card = card)
        }
    }
}

@Composable
private fun CardImageHeader(card: CardDetails) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp),
    ) {
        AsyncImage(
            model = card.imageUrl,
            contentDescription = card.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun CardChipsRow(card: CardDetails) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(card.types) { type ->
            TypeChip(label = type)
        }

        item {
            InfoChip(label = card.category)
        }

        item {
            InfoChip(label = card.rarity)
        }
    }
}

@Composable
private fun TypeChip(label: String) {
    SuggestionChip(
        onClick = {},
        label = { Text(label, fontWeight = FontWeight.SemiBold) },
    )
}

@Composable
private fun InfoChip(label: String) {
    SuggestionChip(
        onClick = {},
        label = { Text(label) },
    )
}

@Composable
private fun CardStatsSection(card: CardDetails) {
    SectionCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            if (card.hp != null) {
                StatItem(label = "HP", value = card.hp.toString())
            }
            if (card.retreat != null) {
                StatItem(label = "Retreat", value = card.retreat.toString())
            }
            if (!card.stage.isNullOrBlank()) {
                StatItem(label = "Stage", value = card.stage)
            }
        }

        if (!card.evolveFrom.isNullOrBlank()) {
            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Evolves from",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(100.dp),
                )
                Text(
                    text = card.evolveFrom,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DescriptionSection(description: String) {
    SectionCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        SectionTitle(text = "Description")
        Spacer(Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AttacksSection(attacks: List<Attack>) {
    SectionCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        SectionTitle(text = "Attacks")
        Spacer(Modifier.height(8.dp))
        attacks.forEachIndexed { index, attack ->
            AttackItem(attack = attack)
            if (index < attacks.lastIndex) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
private fun AttackItem(attack: Attack) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = attack.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                if (attack.cost.isNotEmpty()) {
                    Text(
                        text = "Cost: ${attack.cost.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(
                text = attack.damage,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        if (!attack.effect.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = attack.effect,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun WeaknessesSection(weaknesses: List<Weakness>) {
    SectionCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        SectionTitle(text = "Weaknesses")
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            weaknesses.forEach { weakness ->
                WeaknessChip(weakness = weakness)
            }
        }
    }
}

@Composable
private fun WeaknessChip(weakness: Weakness) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = weakness.type,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )

            if (weakness.value.isNotBlank()) {
                Text(
                    text = weakness.value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}

@Composable
private fun CardFooter(card: CardDetails) {
    SectionCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        FooterRow(label = "Set", value = card.set)
        if (card.illustrator.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            FooterRow(label = "Illustrator", value = card.illustrator)
        }
    }
}

@Composable
private fun FooterRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(90.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}


@Composable
private fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        content = {
            Column(modifier = Modifier.padding(16.dp), content = content)
        },
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}


// ─── Preview ────────────────────────────────────────────────────────────

private val previewCard = CardDetails(
    id = "swsh1-1",
    name = "Charizard VMAX",
    imageUrl = "",
    category = "Pokemon",
    illustrator = "5ban Graphics",
    rarity = "Rare Holo VMAX",
    set = "Sword & Shield",
    hp = 330,
    types = listOf("Fire"),
    evolveFrom = "Charizard V",
    description = "This Pokémon produces flames so hot that they melt anything. It has a brutal nature that won't let it rest until it wins.",
    stage = "VMAX",
    attacks = listOf(
        Attack(
            name = "Max Flare",
            cost = listOf("Fire", "Fire", "Colorless"),
            damage = "300",
            effect = "This attack also does 30 damage to each of your opponent's Benched Pokémon.",
        ),
        Attack(
            name = "Crimson Storm",
            cost = listOf("Fire", "Colorless"),
            damage = "120",
            effect = null,
        ),
    ),
    weaknesses = listOf(
        Weakness(type = "Water", value = "×2"),
    ),
    retreat = 3,
)

@Preview(name = "Details — Content (Light)", showBackground = true, showSystemUi = true)
@Composable
private fun CardDetailsScreenPreview() {
    TCGDexAppTheme(darkTheme = false, dynamicColor = false) {
        CardDetailsScreen(
            uiState = CardDetailsUiState(data = previewCard),
            onBack = {},
            onRetry = {},
        )
    }
}

@Preview(name = "Details — Loading", showBackground = true, showSystemUi = true)
@Composable
private fun CardDetailsScreenLoadingPreview() {
    TCGDexAppTheme(darkTheme = false, dynamicColor = false) {
        CardDetailsScreen(
            uiState = CardDetailsUiState(isLoading = true),
            onBack = {},
            onRetry = {},
        )
    }
}

@Preview(name = "Details — Error", showBackground = true, showSystemUi = true)
@Composable
private fun CardDetailsScreenErrorPreview() {
    TCGDexAppTheme(darkTheme = false, dynamicColor = false) {
        CardDetailsScreen(
            uiState = CardDetailsUiState(error = "Failed to load card. Check your connection."),
            onBack = {},
            onRetry = {},
        )
    }
}

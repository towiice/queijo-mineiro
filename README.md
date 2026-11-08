# Queijo Mineiro (Fabric 1.21.4)

Mod de Minecraft sobre a produção de **queijo minas** e **pão de queijo**.

## Cadeia de produção

```
Grama baixa (5%) ──► Mandioca ──plantar em terra arada──► Plantação (8 estágios)
Mandioca ──bancada──► Mandioca Ralada ──fornalha (secar)──► Polvilho
Mandioca ──fornalha/defumador/fogueira──► Mandioca Cozida

Vaca/Mooshroom (35%) ──► Coalho
Balde de Leite + Coalho ──bancada──► Massa de Queijo   (o balde volta)
Massa de Queijo ──clique na Forma de Queijo──► Fresco ─► Meia-cura ─► Curado
                                           (clique com a mão vazia para desenformar)

2 Polvilho + Queijo (qualquer) + Ovo + Balde de Leite ──► 6 Massa de Pão de Queijo
Massa de Pão de Queijo ──fornalha / defumador (2x mais rápido) / fogueira──► Pão de Queijo
```

**Forma de Queijo**: `S S` / `SSS` com lajes de madeira (qualquer tipo).
- A cura acontece por *random ticks* (~3–4 min por estágio no padrão).
- **Não cura tomando chuva** — deixe sob um teto.
- Comparador lê o estágio (0 / 5 / 10 / 15) — dá pra automatizar.
- Quebrar a forma devolve a forma + o queijo do estágio atual.

## Comidas

| Item | Fome | Saturação | Bônus |
|---|---|---|---|
| Mandioca Cozida | 6 | 0.7 | — |
| Queijo Minas Frescal | 3 | 0.6 | come rápido (1.2 s) |
| Queijo Minas Meia-Cura | 5 | 0.8 | Regeneração I (5 s) |
| Queijo Minas Curado | 6 | 1.0 | Absorção I (30 s) |
| **Pão de Queijo** | 5 | 1.0 | come rápido (1 s), Velocidade I (30 s) + Regeneração I (4 s) |

(Pão vanilla = 5 / 0.6.)

## Compilar e testar

Requer **Java 21** (Temurin). Na pasta do projeto:

```powershell
.\gradlew.bat build        # gera build\libs\queijo-mineiro-1.0.0.jar
.\gradlew.bat runClient    # abre o Minecraft de desenvolvimento com o mod
```

Para jogar: copie o `.jar` de `build/libs` (o que **não** termina em `-sources`) para a pasta
`mods` de uma instalação **Fabric Loader 1.21.4** junto com o **Fabric API** 1.21.4.

## Texturas

Todas as texturas são pixel art 16×16 geradas por [`tools/gerar_texturas.py`](tools/gerar_texturas.py)
(Python puro, sem dependências). Edite paletas/formas e rode:

```powershell
python tools\gerar_texturas.py
```

Uma prévia de todas fica em `tools/preview/todas_as_texturas.png`.
Os JSONs (modelos, receitas, loot tables, traduções) são gerados por `tools/gerar_recursos.py`.

## Estrutura

```
src/main/java/com/queijomineiro/
  QueijoMineiro.java          inicialização (registros, compostagem)
  QueijoMineiroClient.java    render layers (cutout)
  block/  ModBlocks, MandiocaCropBlock, FormaDeQueijoBlock
  item/   ModItems, ModFoods, ModItemGroups
  loot/   ModLootTableModifiers (coalho das vacas, mandioca da grama)
src/main/resources/
  assets/queijo_mineiro/  blockstates, items, models, textures, lang (pt_br, en_us)
  data/queijo_mineiro/    recipe, loot_table, tags
```

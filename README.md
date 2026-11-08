# Queijo Mineiro (Fabric 1.21.x e 26.x)

Mod de Minecraft sobre o ciclo de produção tradicional do **queijo minas** e do **pão de queijo**.

---

## Índice
1. [Versões Suportadas e Branches](#versões-suportadas-e-branches)
2. [Requisitos](#requisitos)
3. [Cadeia de Produção e Fluxograma](#cadeia-de-produção-e-fluxograma)
4. [Itens e Blocos](#itens-e-blocos)
5. [Mecânicas Detalhadas](#mecânicas-detalhadas)
6. [Tabela de Alimentos e Efeitos](#tabela-de-alimentos-e-efeitos)
7. [Todas as Receitas de Crafting e Forno](#todas-as-receitas-de-crafting-e-forno)
8. [Guia de Instalação](#guia-de-instalação)
9. [Como Compilar e Testar Localmente](#como-compilar-e-testar-localmente)

---

## Versões Suportadas e Branches

O repositório está organizado em três branches para cobrir as versões da linha **1.21.x** (a partir da 1.21.4) e toda a linha **26.x**:

| Versões do Minecraft | Branch no Git | Java | Arquivo `.jar` correspondente |
|---|---|---|---|
| **1.21.4 – 1.21.5** | `1.21.4-1.21.5` | Java 21 | `queijo-mineiro-1.0.4+1.21.4-1.21.5.jar` |
| **1.21.6 – 1.21.11** | `1.21.6-1.21.11` | Java 21 | `queijo-mineiro-1.0.4+1.21.6-1.21.11.jar` |
| **26.x** *(26.1, 26.2, 26.3+)* | `26.x` | Java 25 | `queijo-mineiro-1.0.4+26.x.jar` |

---

## Requisitos

- **Minecraft:** `1.21.4` até `1.21.11` ou `26.x` (veja a tabela acima para escolher o `.jar` da sua versão)
- **Mod Loader:** Fabric Loader (`>= 0.16.10` para `1.21.x` / `>= 0.19.2` para `26.x`)
- **API:** Fabric API (compatível com a sua versão do Minecraft)
- **Java:** Java 21 (para `1.21.x`) ou Java 25 (para `26.x`)

---

## Cadeia de Produção e Fluxograma

```
1. Mandioca:
   Grama Baixa (5%) -> Mandioca -> Plantio em Terra Arada -> Colheita (multiplica)
   Mandioca (Bancada) -> Mandioca Ralada -> Forno/Defumador (Secagem) -> Polvilho
   Mandioca -> Forno / Defumador / Fogueira -> Mandioca Cozida

2. Coalho (Dois Caminhos):
   Animal: Vaca/Mooshroom (35%) -> Coagulador Bovino + Frasco de Vidro / Garrafa com Água -> Frasco de Coalho
   Vegetal: Flores Silvestres (Cornflower, Dandelion, etc.) + Frasco de Vidro / Garrafa com Água -> Frasco de Coalho

3. Queijo Minas e Cura:
   Balde de Leite + Frasco de Coalho -> Massa de Queijo (Balde vazio retorna)
   Massa de Queijo na Forma -> Frescal (1) -> Meia-Cura (2) -> Curado (3)
   * Clique com a mão vazia para desenformar o queijo.
   * Clique com o queijo na forma vazia para reenformar.

4. Pão de Queijo:
   2x Polvilho + 1x Queijo (qualquer tipo) + 1x Ovo + 1x Balde de Leite -> 6x Massa de Pão de Queijo
   Massa de Pão de Queijo -> Forno / Defumador (2x rápido) / Fogueira -> Pão de Queijo
```

---

## Itens e Blocos

### Agricultura e Ingredientes
- **Mandioca (`Cassava`):** Raiz obtida da grama ou colheita. Pode ser comida crua, cozida ou plantada.
- **Mandioca Ralada (`Grated Cassava`):** Produto da mandioca triturada na bancada.
- **Polvilho (`Cassava Starch`):** Amido seco obtido após assar a mandioca ralada. Ingrediente-chave do pão de queijo.
- **Coagulador Bovino (`Bovine Abomasum`):** Órgão bovino cru com enzimas naturais de coagulação (drop de vaca).
- **Coalho (`Rennet`):** Frasco enzimático usado para talhar o leite.
- **Massa de Queijo (`Cheese Curd`):** Coalhada fresca escorrida pronta para enformar.
- **Massa de Pão de Queijo (`Cheese Bread Dough`):** Bolinha crua temperada com queijo, ovo, leite e polvilho.

### Blocos
- **Plantação de Mandioca (`Cassava Crop`):** Planta de 8 estágios de crescimento (0 a 7). Suporta farinha de osso (Bone Meal) e encantamento Fortuna na colheita.
- **Forma de Queijo (`Cheese Mold`):** Bloco funcional feito com 5 lajes de madeira. Cura queijo através de random ticks sob abrigo. Emite sinal de comparador redstone.

### Alimentos Prontos
- **Mandioca Cozida (`Boiled Cassava`):** Alimento básico.
- **Queijo Minas Frescal (`Fresh Minas Cheese`):** Queijo leve e de consumo rápido.
- **Queijo Minas Meia-Cura (`Half-Cured Minas Cheese`):** Queijo com tempo intermediário de maturação.
- **Queijo Minas Curado (`Cured Minas Cheese`):** Queijo maturado de casca firme e alta saciedade.
- **Pão de Queijo (`Pão de Queijo`):** Alimento tradicional de alto nível com consumo acelerado e concessão de Pressa I.

---

## Mecânicas Detalhadas

### 1. Cultivo da Mandioca
- Plante a raiz de `Cassava` em terra arada próxima à água.
- A planta cresce em 8 estágios.
- Quando madura, quebre para coletar múltiplas raízes. O encantamento Fortuna aumenta a quantidade obtida.
- Mandioca e mandioca ralada podem ser usadas no Composteiro (65% e 50% de chance, respectivamente).

### 2. Produção de Coalho (Realismo e Opções)
- **Coalho Animal:** Vacas e Mooshrooms possuem 35% de chance de dropar `Coagulador Bovino` (aumenta com saque/looting). Junte com frasco de vidro ou garrafa de água na bancada.
- **Coalho Vegetal:** Coleta pacífica usando flores ricas em enzimas coagulantes (Cornflower/Cardo, Dandelion, Poppy, Allium, Oxeye Daisy) combinadas com frasco de vidro ou garrafa de água.

### 3. Maturação e Forma de Queijo
- Coloque a `Forma de Queijo` no chão, preferencialmente coberta por um teto.
- Clique com o botão direito segurando a `Massa de Queijo` para colocar o queijo no molde (Estágio 1 - Fresco).
- **Chuva:** O queijo interrompe a cura caso esteja exposto diretamente à chuva.
- **Random Ticks:** A cada intervalo de tempo, o queijo avança de estágio emitindo partículas verdes:
  - Estágio 1: Frescal
  - Estágio 2: Meia-Cura
  - Estágio 3: Curado
- **Interação:** Clique com a mão vazia para desenformar a qualquer momento.
- **Reenformar:** É possível recolocar o queijo (Frescal, Meia-Cura ou Curado) de volta em uma forma vazia.
- **Redstone:** Um Comparador ligado à forma emite sinais de força:
  - Vazia: sinal 0
  - Frescal: sinal 5
  - Meia-Cura: sinal 10
  - Curado: sinal 15

---

## Tabela de Alimentos e Efeitos

| Alimento | Nutrição | Saturação | Tempo de Consumo | Efeito Concedido | Regra Especial |
|---|---|---|---|---|---|
| **Mandioca Cozida** | 6 (3 pernas) | 0.7 | 1.6 s (normal) | Nenhum | - |
| **Queijo Minas Frescal** | 3 (1.5 pernas) | 0.6 | 1.2 s (rápido) | Nenhum | - |
| **Queijo Minas Meia-Cura** | 5 (2.5 pernas) | 0.8 | 1.6 s (normal) | **Regeneração I** (5 s) | - |
| **Queijo Minas Curado** | 6 (3 pernas) | 1.0 | 1.6 s (normal) | **Absorção I** (30 s - 2 corações amarelos) | - |
| **Pão de Queijo** | 6 (3 pernas) | 1.2 (elite) | 1.0 s (muito rápido) | **Pressa I / Haste** (20 s) | **Sempre comível** (mesmo de barriga cheia) |

---

## Todas as Receitas de Crafting e Forno

### Bancada de Trabalho (`Crafting Table` - Sem forma fixa / Shapeless)
1. **Mandioca Ralada:**
   `1x Mandioca`
2. **Coalho Animal:**
   `1x Coagulador Bovino` + `1x Frasco de Vidro ou Garrafa com Água`
3. **Coalho Vegetal:**
   `1x Flor (Cornflower, Dandelion, Poppy, Allium, Oxeye Daisy)` + `1x Frasco de Vidro ou Garrafa com Água`
4. **Massa de Queijo:**
   `1x Balde de Leite` + `1x Frasco de Coalho` *(o balde de ferro vazio retorna)*
5. **Massa de Pão de Queijo (Rende 6):**
   `2x Polvilho` + `1x Queijo (Frescal, Meia-Cura ou Curado)` + `1x Ovo` + `1x Balde de Leite` *(o balde de ferro vazio retorna)*

### Bancada de Trabalho (`Crafting Table` - Com formato / Shaped)
1. **Forma de Queijo:**
   - Padrão em "U" usando 5 lajes de qualquer madeira:
     `[ Laje ] [      ] [ Laje ]`
     `[ Laje ] [ Laje ] [ Laje ]`

### Fornos (Fornalha, Defumador e Fogueira)
- **Polvilho:** Secar `Mandioca Ralada` na fornalha ou defumador.
- **Mandioca Cozida:** Cozinhar `Mandioca` na fornalha, defumador ou fogueira.
- **Pão de Queijo:** Assar `Massa de Pão de Queijo`:
  - Na Fornalha convencional (tempo padrão).
  - No Defumador (tempo reduzido pela metade).
  - Na Fogueira (assamento lento sem consumo de combustível).

---

## Guia de Instalação

1. Baixe e instale o [Fabric Loader](https://fabricmc.net/use/installer/) para a sua versão do Minecraft (`1.21.x` ou `26.x`).
2. Baixe o [Fabric API](https://curseforge.com/minecraft/mc-mods/fabric-api) correspondente à sua versão e insira na pasta `.minecraft/mods`.
3. Baixe o arquivo `.jar` correspondente à sua versão na aba [Releases](https://github.com/towiice/queijo-mineiro/releases):
   - Para **1.21.4 – 1.21.5**: `queijo-mineiro-1.0.4+1.21.4-1.21.5.jar`
   - Para **1.21.6 – 1.21.11**: `queijo-mineiro-1.0.4+1.21.6-1.21.11.jar`
   - Para **26.x**: `queijo-mineiro-1.0.4+26.x.jar`
4. Coloque o arquivo na pasta `mods` e inicie o jogo no perfil Fabric.

---

## Como Compilar e Testar Localmente

O projeto inclui o wrapper do Gradle e um executável de testes rápido.

```powershell
# Executar o jogo de desenvolvimento com o mod:
.\testar_mod.bat

# Ou via comando terminal direto (1.21.x usa JDK 21 / 26.x usa JDK 25):
.\gradlew.bat runClient

# Compilar um novo arquivo .jar:
.\gradlew.bat build
# O arquivo gerado estará em: build/libs/queijo-mineiro-X.X.X.jar
```

"""Gera os JSONs de recursos/dados do mod (executado uma vez; os arquivos ficam no projeto)."""
import json, os

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "src", "main", "resources")
NS = "queijo_mineiro"
A = os.path.join(ROOT, "assets", NS)
D = os.path.join(ROOT, "data", NS)


def w(path, obj):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8", newline="\n") as f:
        json.dump(obj, f, ensure_ascii=False, indent=2)
        f.write("\n")


def ns(x):
    return f"{NS}:{x}"


# ---------------- Itens simples (sprite 2D) ----------------
ITENS_2D = ["mandioca", "mandioca_ralada", "mandioca_cozida", "polvilho", "coagulador_bovino", "coalho", "massa_de_queijo",
            "queijo_minas_frescal", "queijo_meia_cura", "queijo_curado", "massa_de_pao_de_queijo", "pao_de_queijo"]
for it in ITENS_2D:
    w(f"{A}/models/item/{it}.json", {"parent": "minecraft:item/generated", "textures": {"layer0": ns(f"item/{it}")}})
    w(f"{A}/items/{it}.json", {"model": {"type": "minecraft:model", "model": ns(f"item/{it}")}})

# ---------------- Plantação de mandioca ----------------
for s in range(4):
    w(f"{A}/models/block/mandioca_stage{s}.json",
      {"parent": "minecraft:block/crop", "textures": {"crop": ns(f"block/mandioca_stage{s}")}})
idade_para_estagio = [0, 0, 1, 1, 2, 2, 2, 3]
w(f"{A}/blockstates/plantacao_de_mandioca.json", {"variants": {
    f"age={i}": {"model": ns(f"block/mandioca_stage{e}")} for i, e in enumerate(idade_para_estagio)}})

# ---------------- Forma de queijo ----------------
lados = ["north", "south", "west", "east"]


def faces(tex, incluir=("up", "north", "south", "west", "east")):
    return {f: {"texture": tex} for f in incluir}


paredes = [
    {"from": [2, 1, 2], "to": [14, 5, 3], "faces": faces("#lado")},
    {"from": [2, 1, 13], "to": [14, 5, 14], "faces": faces("#lado")},
    {"from": [2, 1, 3], "to": [3, 5, 13], "faces": faces("#lado")},
    {"from": [13, 1, 3], "to": [14, 5, 13], "faces": faces("#lado")},
]
fundo = {"from": [2, 0, 2], "to": [14, 1, 14], "faces": {
    "down": {"texture": "#fundo", "cullface": "down"}, "up": {"texture": "#fundo"},
    **{l: {"texture": "#lado"} for l in lados}}}
queijo = {"from": [3, 1, 3], "to": [13, 7, 13], "faces": {
    "up": {"texture": "#queijo_topo"}, **{l: {"texture": "#queijo_lado"} for l in lados}}}

w(f"{A}/models/block/template_forma_de_queijo.json", {
    "parent": "minecraft:block/block", "textures": {"particle": "#lado"}, "elements": [fundo] + paredes})
w(f"{A}/models/block/template_forma_de_queijo_cheia.json", {
    "parent": "minecraft:block/block", "textures": {"particle": "#lado"}, "elements": [fundo, queijo] + paredes})

madeira = {"lado": ns("block/forma_de_queijo_lado"), "fundo": ns("block/forma_de_queijo_fundo")}
w(f"{A}/models/block/forma_de_queijo.json",
  {"parent": ns("block/template_forma_de_queijo"), "textures": madeira})
estagios = {1: "fresco", 2: "meia_cura", 3: "curado"}
for n, e in estagios.items():
    w(f"{A}/models/block/forma_de_queijo_{e}.json", {
        "parent": ns("block/template_forma_de_queijo_cheia"),
        "textures": {**madeira, "queijo_topo": ns(f"block/queijo_{e}_topo"), "queijo_lado": ns(f"block/queijo_{e}_lado")}})
w(f"{A}/blockstates/forma_de_queijo.json", {"variants": {
    "cura=0": {"model": ns("block/forma_de_queijo")},
    **{f"cura={n}": {"model": ns(f"block/forma_de_queijo_{e}")} for n, e in estagios.items()}}})
w(f"{A}/items/forma_de_queijo.json", {"model": {"type": "minecraft:model", "model": ns("block/forma_de_queijo")}})

# ---------------- Traduções ----------------
pt = {
    "item.queijo_mineiro.mandioca": "Mandioca",
    "item.queijo_mineiro.mandioca_ralada": "Mandioca Ralada",
    "item.queijo_mineiro.mandioca_cozida": "Mandioca Cozida",
    "item.queijo_mineiro.polvilho": "Polvilho",
    "item.queijo_mineiro.coagulador_bovino": "Coagulador Bovino",
    "item.queijo_mineiro.coalho": "Coalho",
    "item.queijo_mineiro.massa_de_queijo": "Massa de Queijo",
    "item.queijo_mineiro.queijo_minas_frescal": "Queijo Minas Frescal",
    "item.queijo_mineiro.queijo_meia_cura": "Queijo Minas Meia-Cura",
    "item.queijo_mineiro.queijo_curado": "Queijo Minas Curado",
    "item.queijo_mineiro.massa_de_pao_de_queijo": "Massa de Pão de Queijo",
    "item.queijo_mineiro.pao_de_queijo": "Pão de Queijo",
    "block.queijo_mineiro.forma_de_queijo": "Forma de Queijo",
    "block.queijo_mineiro.plantacao_de_mandioca": "Plantação de Mandioca",
    "itemGroup.queijo_mineiro.principal": "Queijo Mineiro",
    "tag.item.queijo_mineiro.queijos": "Queijos",
}
en = {
    "item.queijo_mineiro.mandioca": "Cassava",
    "item.queijo_mineiro.mandioca_ralada": "Grated Cassava",
    "item.queijo_mineiro.mandioca_cozida": "Boiled Cassava",
    "item.queijo_mineiro.polvilho": "Cassava Starch",
    "item.queijo_mineiro.coagulador_bovino": "Bovine Abomasum",
    "item.queijo_mineiro.coalho": "Rennet",
    "item.queijo_mineiro.massa_de_queijo": "Cheese Curd",
    "item.queijo_mineiro.queijo_minas_frescal": "Fresh Minas Cheese",
    "item.queijo_mineiro.queijo_meia_cura": "Half-Cured Minas Cheese",
    "item.queijo_mineiro.queijo_curado": "Cured Minas Cheese",
    "item.queijo_mineiro.massa_de_pao_de_queijo": "Cheese Bread Dough",
    "item.queijo_mineiro.pao_de_queijo": "Pão de Queijo",
    "block.queijo_mineiro.forma_de_queijo": "Cheese Mold",
    "block.queijo_mineiro.plantacao_de_mandioca": "Cassava Crop",
    "itemGroup.queijo_mineiro.principal": "Minas Cheese",
    "tag.item.queijo_mineiro.queijos": "Cheeses",
}
w(f"{A}/lang/pt_br.json", pt)
w(f"{A}/lang/en_us.json", en)

# ---------------- Tags ----------------
w(f"{D}/tags/item/queijos.json", {"replace": False, "values": [
    ns("queijo_minas_frescal"), ns("queijo_meia_cura"), ns("queijo_curado")]})

w(f"{D}/tags/item/flores_de_coalho.json", {"replace": False, "values": [
    "minecraft:cornflower",
    "minecraft:dandelion",
    "minecraft:poppy",
    "minecraft:allium",
    "minecraft:oxeye_daisy"
]})

# ---------------- Receitas ----------------
R = f"{D}/recipe"


def resultado(item, n=1):
    return {"id": ns(item), "count": n}


w(f"{R}/mandioca_ralada.json", {"type": "minecraft:crafting_shapeless", "category": "misc",
                                "ingredients": [ns("mandioca")], "result": resultado("mandioca_ralada")})

# Coalho Animal: Coagulador Bovino + Frasco de Água (ou Frasco de Vidro)
w(f"{R}/coalho_animal.json", {"type": "minecraft:crafting_shapeless", "category": "misc",
                              "ingredients": [ns("coagulador_bovino"), "minecraft:glass_bottle"],
                              "result": resultado("coalho")})

# Coalho Vegetal: Flor de Cardo/Silvestre + Frasco de Vidro
w(f"{R}/coalho_vegetal.json", {"type": "minecraft:crafting_shapeless", "category": "misc",
                               "ingredients": ["#queijo_mineiro:flores_de_coalho", "minecraft:glass_bottle"],
                               "result": resultado("coalho")})

w(f"{R}/massa_de_queijo.json", {"type": "minecraft:crafting_shapeless", "category": "misc",
                                "ingredients": ["minecraft:milk_bucket", ns("coalho")],
                                "result": resultado("massa_de_queijo")})
w(f"{R}/forma_de_queijo.json", {"type": "minecraft:crafting_shaped", "category": "misc",
                                "pattern": ["S S", "SSS"], "key": {"S": "#minecraft:wooden_slabs"},
                                "result": resultado("forma_de_queijo")})
w(f"{R}/massa_de_pao_de_queijo.json", {"type": "minecraft:crafting_shapeless", "category": "misc",
                                       "ingredients": [ns("polvilho"), ns("polvilho"), "#queijo_mineiro:queijos",
                                                       "minecraft:egg", "minecraft:milk_bucket"],
                                       "result": resultado("massa_de_pao_de_queijo", 6)})


def cozimento(nome, tipo, entrada, saida, tempo, xp, categoria="food"):
    w(f"{R}/{nome}.json", {"type": f"minecraft:{tipo}", "category": categoria, "ingredient": ns(entrada),
                           "result": {"id": ns(saida)}, "experience": xp, "cookingtime": tempo})


# Polvilho: secar a mandioca ralada na fornalha.
cozimento("polvilho", "smelting", "mandioca_ralada", "polvilho", 200, 0.1, "misc")
# Pão de queijo: fornalha, defumador (mais rápido) ou fogueira.
cozimento("pao_de_queijo", "smelting", "massa_de_pao_de_queijo", "pao_de_queijo", 200, 0.35)
cozimento("pao_de_queijo_no_defumador", "smoking", "massa_de_pao_de_queijo", "pao_de_queijo", 100, 0.35)
cozimento("pao_de_queijo_na_fogueira", "campfire_cooking", "massa_de_pao_de_queijo", "pao_de_queijo", 600, 0.35)
# Mandioca cozida.
cozimento("mandioca_cozida", "smelting", "mandioca", "mandioca_cozida", 200, 0.35)
cozimento("mandioca_cozida_no_defumador", "smoking", "mandioca", "mandioca_cozida", 100, 0.35)
cozimento("mandioca_cozida_na_fogueira", "campfire_cooking", "mandioca", "mandioca_cozida", 600, 0.35)

# ---------------- Loot tables ----------------
L = f"{D}/loot_table/blocks"
w(f"{L}/plantacao_de_mandioca.json", {
    "type": "minecraft:block",
    "pools": [
        {"rolls": 1, "entries": [{"type": "minecraft:item", "name": ns("mandioca")}]},
        {"rolls": 1,
         "conditions": [{"condition": "minecraft:block_state_property", "block": ns("plantacao_de_mandioca"),
                         "properties": {"age": "7"}}],
         "entries": [{"type": "minecraft:item", "name": ns("mandioca"), "functions": [
             {"function": "minecraft:apply_bonus", "enchantment": "minecraft:fortune",
              "formula": "minecraft:binomial_with_bonus_count",
              "parameters": {"extra": 3, "probability": 0.5714286}}]}]},
    ],
    "functions": [{"function": "minecraft:explosion_decay"}],
    "random_sequence": ns("blocks/plantacao_de_mandioca"),
})

pools_forma = [{"rolls": 1, "conditions": [{"condition": "minecraft:survives_explosion"}],
                "entries": [{"type": "minecraft:item", "name": ns("forma_de_queijo")}]}]
for n, item in {1: "queijo_minas_frescal", 2: "queijo_meia_cura", 3: "queijo_curado"}.items():
    pools_forma.append({"rolls": 1,
                        "conditions": [{"condition": "minecraft:block_state_property", "block": ns("forma_de_queijo"),
                                        "properties": {"cura": str(n)}}],
                        "entries": [{"type": "minecraft:item", "name": ns(item)}]})
w(f"{L}/forma_de_queijo.json", {"type": "minecraft:block", "pools": pools_forma,
                                "random_sequence": ns("blocks/forma_de_queijo")})

# Ferramenta certa para quebrar a forma (machado).
w(os.path.join(ROOT, "data", "minecraft", "tags", "block", "mineable", "axe.json"),
  {"replace": False, "values": [ns("forma_de_queijo")]})

print("Recursos gerados em", os.path.normpath(ROOT))

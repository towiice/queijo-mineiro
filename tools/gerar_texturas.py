"""
Gerador das texturas pixel art (16x16) do mod Queijo Mineiro.
Sem dependências externas: escreve PNG direto com zlib.

    python tools/gerar_texturas.py

Pode editar paletas/formas aqui e rodar de novo para regerar tudo.
"""
import math
import os
import random
import struct
import zlib

BASE = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "src", "main", "resources",
                    "assets", "queijo_mineiro")
TEX = os.path.join(BASE, "textures")
PREVIEW = os.path.join(os.path.dirname(os.path.abspath(__file__)), "preview")

TRANSP = (0, 0, 0, 0)
LUZ = (-0.55, -0.65, 0.53)  # luz vindo de cima/esquerda (x direita, y para baixo, z para fora)
_n = math.sqrt(sum(c * c for c in LUZ))
LUZ = tuple(c / _n for c in LUZ)


def cor(h, a=255):
    h = h.lstrip("#")
    return (int(h[0:2], 16), int(h[2:4], 16), int(h[4:6], 16), a)


def rampa(*hexes):
    return [cor(h) for h in hexes]


def escurecer(c, f=0.6):
    return (int(c[0] * f), int(c[1] * f), int(c[2] * f), 255)


# ------------------------------------------------------------------ imagem / PNG
class Img:
    def __init__(self, w=16, h=16):
        self.w, self.h = w, h
        self.p = [[TRANSP] * w for _ in range(h)]

    def dentro(self, x, y):
        return 0 <= x < self.w and 0 <= y < self.h

    def set(self, x, y, c):
        if self.dentro(x, y):
            self.p[y][x] = c

    def get(self, x, y):
        return self.p[y][x] if self.dentro(x, y) else TRANSP

    def cheio(self, x, y):
        return self.get(x, y)[3] > 0

    def escalar(self, f):
        out = Img(self.w * f, self.h * f)
        for y in range(out.h):
            for x in range(out.w):
                out.p[y][x] = self.p[y // f][x // f]
        return out

    def salvar(self, caminho):
        os.makedirs(os.path.dirname(caminho), exist_ok=True)
        raw = b"".join(b"\x00" + bytes(v for px in linha for v in px) for linha in self.p)

        def chunk(t, d):
            return struct.pack(">I", len(d)) + t + d + struct.pack(">I", zlib.crc32(t + d) & 0xFFFFFFFF)

        png = (b"\x89PNG\r\n\x1a\n"
               + chunk(b"IHDR", struct.pack(">IIBBBBB", self.w, self.h, 8, 6, 0, 0, 0))
               + chunk(b"IDAT", zlib.compress(raw, 9))
               + chunk(b"IEND", b""))
        with open(caminho, "wb") as f:
            f.write(png)


# ------------------------------------------------------------------ helpers de desenho
def tom(r, intensidade, rng, dither=0.12):
    i = intensidade + (rng.random() - 0.5) * dither
    return r[max(0, min(len(r) - 1, int(i * len(r))))]


def luz_normal(nx, ny, nz):
    d = nx * LUZ[0] + ny * LUZ[1] + nz * LUZ[2]
    return max(0.0, min(0.999, d * 0.9 + 0.25))


def elipse(img, cx, cy, rx, ry, r, rng, dither=0.12):
    """Esfera/elipsoide sombreado."""
    for y in range(img.h):
        for x in range(img.w):
            nx = (x + 0.5 - cx) / rx
            ny = (y + 0.5 - cy) / ry
            d = nx * nx + ny * ny
            if d <= 1.0:
                img.set(x, y, tom(r, luz_normal(nx, ny, math.sqrt(1 - d)), rng, dither))


def capsula(img, p0, p1, r0, r1, r, rng, dither=0.1, aneis=0, ponta_reta=False):
    """Cilindro afunilado de p0 (raio r0) a p1 (raio r1). Retorna t de cada pixel pintado."""
    (x0, y0), (x1, y1) = p0, p1
    dx, dy = x1 - x0, y1 - y0
    L2 = dx * dx + dy * dy
    ts = {}
    for y in range(img.h):
        for x in range(img.w):
            px, py = x + 0.5, y + 0.5
            t = ((px - x0) * dx + (py - y0) * dy) / L2
            if ponta_reta and t > 1.0:
                continue
            tc = max(0.0, min(1.0, t))
            cxp, cyp = x0 + dx * tc, y0 + dy * tc
            raio = r0 + (r1 - r0) * tc
            vx, vy = px - cxp, py - cyp
            dist = math.hypot(vx, vy)
            if dist <= raio:
                nx, ny = vx / raio, vy / raio
                nz = math.sqrt(max(0.0, 1 - nx * nx - ny * ny))
                i = luz_normal(nx, ny, nz)
                if aneis and (t * aneis) % 1.0 < 0.16:
                    i -= 0.25
                img.set(x, y, tom(r, i, rng, dither))
                ts[(x, y)] = t
    return ts


def contorno(img, fator=0.45, diagonal=False):
    """Contorno escuro "seletivo": cada borda usa uma versão escura do vizinho."""
    viz = [(1, 0), (-1, 0), (0, 1), (0, -1)]
    if diagonal:
        viz += [(1, 1), (-1, -1), (1, -1), (-1, 1)]
    novos = {}
    for y in range(img.h):
        for x in range(img.w):
            if img.cheio(x, y):
                continue
            for ddx, ddy in viz:
                if img.cheio(x + ddx, y + ddy):
                    novos[(x, y)] = escurecer(img.get(x + ddx, y + ddy), fator)
                    break
    for (x, y), c in novos.items():
        img.set(x, y, c)


def pontilhar(img, rng, prob, cores, filtro=None):
    for y in range(img.h):
        for x in range(img.w):
            if img.cheio(x, y) and (filtro is None or filtro(x, y)) and rng.random() < prob:
                img.set(x, y, rng.choice(cores))


def linha(img, x0, y0, x1, y1, c):
    n = int(max(abs(x1 - x0), abs(y1 - y0))) + 1
    for i in range(n + 1):
        t = i / max(1, n)
        img.set(int(round(x0 + (x1 - x0) * t)), int(round(y0 + (y1 - y0) * t)), c)


def grade(linhas, paleta):
    img = Img()
    assert len(linhas) == 16, "sprite precisa de 16 linhas"
    for y, ln in enumerate(linhas):
        assert len(ln) == 16, f"linha {y} tem {len(ln)} colunas: {ln!r}"
        for x, ch in enumerate(ln):
            if ch != ".":
                img.set(x, y, paleta[ch])
    return img


# ------------------------------------------------------------------ ITENS
def item_mandioca():
    rng = random.Random(1)
    img = Img()
    casca = rampa("#3e2412", "#5e3a1c", "#7e5028", "#9c6a38", "#b88650")
    ts = capsula(img, (2.6, 13.4), (11.6, 4.4), 0.7, 2.8, casca, rng, aneis=6)
    # ponta cortada mostrando a polpa branca
    polpa = rampa("#d6c9a8", "#ece2c8", "#fbf6e8")
    for (x, y), t in ts.items():
        if t > 0.86 and math.hypot(x + 0.5 - 11.9, y + 0.5 - 4.1) <= 1.9:
            img.set(x, y, tom(polpa, 0.75 - (x + y - 15) * 0.12, rng, 0.2))
    img.set(12, 4, cor("#e8dcb8"))  # miolo
    contorno(img)
    return img


def item_mandioca_ralada():
    rng = random.Random(2)
    img = Img()
    r = rampa("#a08f5c", "#c4b37e", "#ddd09c", "#ede4bd", "#faf5dd")
    # monte (meia elipse) com textura grossa de fiapos
    cx, base, rx, ry = 8.0, 13.6, 6.6, 7.5
    for y in range(16):
        for x in range(16):
            nx = (x + 0.5 - cx) / rx
            ny = (y + 0.5 - base) / ry
            if y + 0.5 <= base and nx * nx + ny * ny <= 1:
                nz = math.sqrt(max(0, 1 - nx * nx - ny * ny))
                img.set(x, y, tom(r, luz_normal(nx, ny, nz), rng, 0.45))
    # fiapos claros
    for _ in range(9):
        x, y = rng.randint(3, 12), rng.randint(8, 12)
        if img.cheio(x, y) and img.cheio(x + 1, y - 1):
            img.set(x, y, r[-1])
            img.set(x + 1, y - 1, r[-2])
    contorno(img)
    return img


def item_mandioca_cozida():
    rng = random.Random(3)
    img = Img()
    amarelo = rampa("#9c7428", "#c89a40", "#e2bc5e", "#f2d684", "#fdeebc")
    capsula(img, (3.0, 6.0), (10.5, 10.5), 2.5, 2.5, amarelo, rng, 0.15)
    ts = capsula(img, (6.0, 12.8), (13.2, 8.0), 2.4, 2.4, amarelo, rng, 0.15)
    # pontas com a "fibra" central (mais escura)
    for (fx, fy) in [(13, 8), (3, 6)]:
        img.set(fx, fy, cor("#b58a3a"))
    # rachaduras de cozimento
    for (x, y) in [(6, 7), (7, 8), (9, 11), (10, 10)]:
        if img.cheio(x, y):
            img.set(x, y, amarelo[1])
    contorno(img)
    return img


def item_polvilho():
    rng = random.Random(4)
    img = Img()
    juta = rampa("#5f4626", "#7d6236", "#9c7f4c", "#b89b64", "#ccb37c")
    # corpo do saquinho
    elipse(img, 8.0, 10.6, 6.0, 4.6, juta, rng, 0.1)
    for y in range(5, 11):
        for x in range(3, 13):
            nx = (x + 0.5 - 8) / 5.2
            img.set(x, y, tom(juta, 0.62 - nx * 0.4, rng, 0.12))
    # trama da juta
    for y in range(16):
        for x in range(16):
            if img.cheio(x, y) and (x + 2 * y) % 4 == 0:
                c = img.get(x, y)
                img.set(x, y, escurecer(c, 0.85))
    # barbante amarrado
    for x in range(3, 13):
        img.set(x, 7, cor("#e2d2a4") if x % 2 else cor("#bfa977"))
    # boca aberta com polvilho branquinho
    branco = rampa("#bdb8ac", "#dad6cc", "#efece6", "#ffffff")
    elipse(img, 8.0, 4.4, 4.6, 2.4, branco, rng, 0.2)
    elipse(img, 8.0, 3.6, 3.0, 1.9, branco, rng, 0.2)
    contorno(img)
    return img


def item_coalho():
    # Frasquinho de coalho líquido, desenhado à mão.
    pal = {
        "O": cor("#2b1d12"), "k": cor("#8a5a32"), "K": cor("#b07a48"),
        "w": cor("#ffffff", 220), "g": cor("#c7e3e8", 170),
        "L": cor("#e8c070"), "l": cor("#c89848"), "d": cor("#946a2c"),
        "e": cor("#f4efe2"), "r": cor("#b8322a"),
    }
    return grade([
        "................",
        "......OOOO......",
        "......OkKO......",
        "......OkkO......",
        ".....OOOOOO.....",
        "......OwgO......",
        "......OwgO......",
        ".....OwgggO.....",
        "....OwgggggO....",
        "....OLlllldO....",
        "....OeeeeeeO....",
        "....OerrreeO....",
        "....OeeeeeeO....",
        "....OLlllldO....",
        "....OllllddO....",
        ".....OOOOOO.....",
    ], pal)


def item_massa_de_queijo():
    rng = random.Random(5)
    img = Img()
    branco = rampa("#a6a28c", "#cbc7b0", "#e4e1cf", "#f3f1e4", "#ffffff")
    # coalhada: vários grumos
    grumos = [(5.0, 6.5, 2.6), (10.0, 6.0, 2.5), (7.5, 9.0, 2.8), (4.5, 11.5, 2.6),
              (11.2, 10.6, 2.7), (8.2, 12.6, 2.4)]
    for gx, gy, gr in grumos:
        elipse(img, gx, gy, gr, gr * 0.92, branco, rng, 0.15)
    contorno(img, 0.55)
    # soro amarelinho escorrendo embaixo
    for (x, y) in [(6, 15), (7, 15), (9, 15)]:
        if not img.cheio(x, y):
            img.set(x, y, cor("#e8e0a8", 200))
    return img


def roda_de_queijo(topo, lado, seed, sulcos=False, manchas=None):
    rng = random.Random(seed)
    img = Img()
    cx, rx = 8.0, 6.6
    yt, yb, ry = 5.6, 10.4, 3.0
    # lateral (corpo + elipse de baixo)
    for y in range(16):
        for x in range(16):
            px, py = x + 0.5, y + 0.5
            nx = (px - cx) / rx
            if abs(nx) > 1:
                continue
            na_base = nx * nx + ((py - yb) / ry) ** 2 <= 1
            if yt <= py <= yb or na_base:
                i = 0.5 - nx * 0.42
                if sulcos and y % 2 == 0:
                    i -= 0.22
                img.set(x, y, tom(lado, i, rng, 0.1))
    # topo
    for y in range(16):
        for x in range(16):
            nx = (x + 0.5 - cx) / rx
            ny = (y + 0.5 - yt) / ry
            d = nx * nx + ny * ny
            if d <= 1:
                i = 0.85 - (nx + ny) * 0.1
                if d > 0.6 and ny > 0.2:
                    img.set(x, y, topo[0])  # quina frontal do topo (separa topo da lateral)
                    continue
                if d > 0.72:
                    i -= 0.3  # borda/casca do topo
                img.set(x, y, tom(topo, i, rng, 0.1))
    if manchas:
        pontilhar(img, rng, manchas[0], manchas[1], filtro=lambda x, y: y > 6)
    contorno(img, 0.5)
    return img


def item_queijo_minas_frescal():
    return roda_de_queijo(rampa("#cfc8b0", "#e8e3d0", "#f6f3e8", "#ffffff"),
                          rampa("#a9a28a", "#cbc4aa", "#e2dcc6", "#f2eee0"), 6, sulcos=True)


def item_queijo_meia_cura():
    return roda_de_queijo(rampa("#d4bb6e", "#ead292", "#f5e4ae", "#fff4cf"),
                          rampa("#a2803a", "#c49e52", "#d9b66a", "#e9cc88"), 7, sulcos=True)


def item_queijo_curado():
    return roda_de_queijo(rampa("#cfa036", "#e4bc52", "#f0d072", "#fae29a"),
                          rampa("#6e4a14", "#8f6220", "#ad7e30", "#c79a44"), 8,
                          manchas=(0.12, [cor("#5e3e10"), cor("#7a5418")]))


def item_massa_de_pao_de_queijo():
    rng = random.Random(9)
    img = Img()
    massa = rampa("#9a8a6a", "#bdae8e", "#d8cbad", "#eae1ca", "#f6f1e2", "#fffcf4")
    # três bolinhas cruas
    elipse(img, 8.0, 5.4, 3.5, 3.2, massa, rng)
    elipse(img, 4.6, 10.6, 3.6, 3.3, massa, rng)
    elipse(img, 11.4, 10.6, 3.6, 3.3, massa, rng)
    # pontinhos de queijo na massa
    pontilhar(img, rng, 0.07, [cor("#f2dc8a"), cor("#e8cc70")])
    contorno(img, 0.5)
    return img


def item_pao_de_queijo():
    rng = random.Random(10)
    img = Img()
    dourado = rampa("#7a4814", "#a6662a", "#cc9240", "#e6b65e", "#f4d68c", "#fff0c4")
    elipse(img, 8.0, 8.9, 6.6, 5.7, dourado, rng, 0.14)
    # casquinha craquelada
    pontilhar(img, rng, 0.10, [dourado[1], dourado[2]], filtro=lambda x, y: 5 < y < 13)
    # queijinho derretido aparecendo
    pontilhar(img, rng, 0.05, [cor("#fff4cc"), cor("#ffe9a0")], filtro=lambda x, y: y < 9)
    contorno(img, 0.45)
    return img


# ------------------------------------------------------------------ BLOCOS
FOLHA = rampa("#24501a", "#346e24", "#4c9232", "#6cb446")
CAULE = rampa("#4e2a1e", "#70402e", "#925a40")


def folha_palmada(img, x, y, tam, rng):
    """Folha de mandioca: lobos em leque saindo de (x, y)."""
    angs = [-160, -125, -90, -55, -20] if tam >= 2.5 else [-135, -90, -45]
    for a in angs:
        rad = math.radians(a)
        comp = tam * (1.0 if a == -90 else 0.85)
        for i in range(1, int(round(comp)) + 1):
            px = int(round(x + math.cos(rad) * i))
            py = int(round(y + math.sin(rad) * i))
            img.set(px, py, FOLHA[2] if i < comp - 0.5 else FOLHA[3])
            if tam >= 3 and 1 < i < comp:
                # engrossa o lobo do lado de baixo
                img.set(px + (1 if math.cos(rad) < 0 else 0), py + 1, FOLHA[1])
    img.set(int(x), int(y), FOLHA[0])


def pe_de_mandioca(img, x, altura, folha, laterais, rng, raiz=False):
    topo = 15 - altura
    for y in range(topo, 16):
        img.set(x, y, CAULE[1] if (y % 3) else CAULE[0])  # nós do caule
        if altura >= 12:
            img.set(x + 1, y, CAULE[2] if (y % 3) else CAULE[1])
    for (ly, lado, tam) in laterais:
        bx = x + 3 * lado
        linha(img, x, ly, bx, ly - 2, cor("#a0503a"))  # pecíolo avermelhado
        folha_palmada(img, bx, ly - 2, tam, rng)
    folha_palmada(img, x, topo, folha, rng)
    if raiz:
        for (rx, ry) in [(x - 2, 15), (x - 1, 14), (x + 2, 15), (x + 3, 14), (x - 1, 15), (x + 2, 14)]:
            img.set(rx, ry, cor("#7e5028") if ry == 15 else cor("#9c6a38"))


def bloco_mandioca(estagio):
    rng = random.Random(20 + estagio)
    img = Img()
    cfg = {
        0: dict(altura=2, folha=1.5, laterais=[]),
        1: dict(altura=5, folha=2.5, laterais=[]),
        2: dict(altura=9, folha=3.2, laterais=[(11, -1, 2)]),
        3: dict(altura=12, folha=4.0, laterais=[(10, -1, 2.6), (7, 1, 2.6)]),
    }[estagio]
    pe_de_mandioca(img, 4, cfg["altura"], cfg["folha"], cfg["laterais"], rng, raiz=estagio == 3)
    lat2 = [(ly, -l, t) for (ly, l, t) in cfg["laterais"]]
    pe_de_mandioca(img, 11, cfg["altura"] - (1 if estagio else 0), cfg["folha"], lat2, rng, raiz=estagio == 3)
    return img


MADEIRA = rampa("#8a6236", "#a87c48", "#c29a62", "#d6b47c")


def madeira(seed, furos=False):
    rng = random.Random(seed)
    img = Img()
    for y in range(16):
        desloc = rng.random()
        for x in range(16):
            i = 0.6 + 0.15 * math.sin((x + desloc * 9) * 0.7 + y * 0.3)
            if rng.random() < 0.12:
                i -= 0.3  # veios
            img.set(x, y, tom(MADEIRA, i, rng, 0.1))
    for x in range(16):  # bordas das tábuas
        img.set(x, 0, MADEIRA[0])
        img.set(x, 8, MADEIRA[1])
    if furos:  # furinhos de dessorar (na faixa visível das paredes)
        for y, offs in [(11, 0), (13, 2)]:
            for x in range(1 + offs, 16, 4):
                img.set(x, y, cor("#3a2414"))
                img.set(x, y - 1, MADEIRA[0])
    return img


def queijo_topo(r, seed, manchas=None):
    rng = random.Random(seed)
    img = Img()
    for y in range(16):
        for x in range(16):
            borda = min(x, y, 15 - x, 15 - y)
            i = 0.72 if borda > 3 else 0.5
            img.set(x, y, tom(r, i, rng, 0.2))
    if manchas:
        pontilhar(img, rng, manchas[0], manchas[1])
    return img


def queijo_lado(r, seed, sulcos=False, manchas=None):
    rng = random.Random(seed)
    img = Img()
    for y in range(16):
        for x in range(16):
            i = 0.6
            if sulcos and y % 2 == 0:
                i -= 0.25
            img.set(x, y, tom(r, i, rng, 0.15))
    if manchas:
        pontilhar(img, rng, manchas[0], manchas[1])
    return img


# ------------------------------------------------------------------ main
def main():
    itens = {
        "mandioca": item_mandioca,
        "mandioca_ralada": item_mandioca_ralada,
        "mandioca_cozida": item_mandioca_cozida,
        "polvilho": item_polvilho,
        "coalho": item_coalho,
        "massa_de_queijo": item_massa_de_queijo,
        "queijo_minas_frescal": item_queijo_minas_frescal,
        "queijo_meia_cura": item_queijo_meia_cura,
        "queijo_curado": item_queijo_curado,
        "massa_de_pao_de_queijo": item_massa_de_pao_de_queijo,
        "pao_de_queijo": item_pao_de_queijo,
    }
    blocos = {f"mandioca_stage{i}": (lambda i=i: bloco_mandioca(i)) for i in range(4)}
    blocos.update({
        "forma_de_queijo_lado": lambda: madeira(30, furos=True),
        "forma_de_queijo_fundo": lambda: madeira(31),
        "queijo_fresco_topo": lambda: queijo_topo(rampa("#d8d2bc", "#ebe7d6", "#f7f5ec", "#ffffff"), 40),
        "queijo_fresco_lado": lambda: queijo_lado(rampa("#b4ad94", "#d2ccb4", "#e6e1cc", "#f4f0e2"), 41, sulcos=True),
        "queijo_meia_cura_topo": lambda: queijo_topo(rampa("#dcc47a", "#ecd698", "#f6e6b2", "#fff3cc"), 42),
        "queijo_meia_cura_lado": lambda: queijo_lado(rampa("#a8863e", "#c6a256", "#dab86c", "#eacd8a"), 43, sulcos=True),
        "queijo_curado_topo": lambda: queijo_topo(rampa("#d4a63c", "#e6c056", "#f0d074", "#f9e098"), 44,
                                                  manchas=(0.06, [cor("#c08e2c")])),
        "queijo_curado_lado": lambda: queijo_lado(rampa("#6e4a14", "#8f6220", "#ad7e30", "#c79a44"), 45,
                                                  manchas=(0.12, [cor("#5e3e10"), cor("#7a5418")])),
    })

    todas = []
    for nome, f in itens.items():
        img = f()
        img.salvar(os.path.join(TEX, "item", nome + ".png"))
        todas.append((nome, img))
    for nome, f in blocos.items():
        img = f()
        img.salvar(os.path.join(TEX, "block", nome + ".png"))
        todas.append((nome, img))

    # Ícone do mod (pão de queijo ampliado 8x)
    item_pao_de_queijo().escalar(8).salvar(os.path.join(BASE, "icon.png"))

    # Folha de contato para conferir tudo de uma vez (não vai pro jar)
    cols, esc, pad = 8, 6, 4
    linhas_n = (len(todas) + cols - 1) // cols
    folha = Img(cols * (16 * esc + pad) + pad, linhas_n * (16 * esc + pad) + pad)
    for y in range(folha.h):
        for x in range(folha.w):
            folha.p[y][x] = cor("#3c3c46") if ((x // 8 + y // 8) % 2) else cor("#46464f")
    for idx, (_, img) in enumerate(todas):
        ox = pad + (idx % cols) * (16 * esc + pad)
        oy = pad + (idx // cols) * (16 * esc + pad)
        for y in range(16 * esc):
            for x in range(16 * esc):
                c = img.p[y // esc][x // esc]
                if c[3] > 0:
                    a = c[3] / 255
                    b = folha.p[oy + y][ox + x]
                    folha.p[oy + y][ox + x] = tuple(int(c[k] * a + b[k] * (1 - a)) for k in range(3)) + (255,)
    folha.salvar(os.path.join(PREVIEW, "todas_as_texturas.png"))
    print(f"{len(todas)} texturas geradas. Prévia: {os.path.normpath(PREVIEW)}")


if __name__ == "__main__":
    main()

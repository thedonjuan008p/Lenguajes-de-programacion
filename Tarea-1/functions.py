import re

patHex = re.compile(r"&\w{3}(\w{2})(\w{2})(\w{2})")
patSty = re.compile(r"^\w+: (.+),[\w ]+,\d+,(&\w+)(?:,&\w+){3}.+$", flags=re.MULTILINE)
patDia = re.compile(r"^\w+: \d,(\d+:\d+:\d+.\d+),(\d+:\d+:\d+.\d+),(.*),[\w+]*,(?:\d+,){3}[\w !#$%&()*+,\-./:;<=>?@\^_`{|}~]*,(.+)$",
                    flags=re.MULTILINE)
patTime = re.compile(r"(\d+)*:*(\d{2}):(\d{2}).(\d{2,3})")
# He puesto 2 o 3 digitos en los ms porque los .ass tiene 2 d. y los srt(y el intervalo) 3 d.


def styles_pkg(text):  # Convierte el color de los estilos de hex a rgb en una lista->[(nombre,color),...]
    dicStyles = {}  # guardandolos en un diccionario {nombre:color,...}
    for nombre, color in patSty.findall(text):
        color = patHex.sub(r"#\3\2\1", color)
        dicStyles[nombre] = color
    return dicStyles


def format_time(text): # Este solo es ocupado cuando no hay intervalo.
    cap = patTime.findall(text)
    if len(cap[0][0]) == 1:
        return patTime.sub(r"0\1:\2:\3,\4", text) + "0"  # me gustaria saber como poner un 0 adelante del \4
    elif len(cap[0][0]) == 0:
        return patTime.sub(r"00:\2:\3,\4", text)
    return patTime.sub(r"\1:\2:\3,\4", text)


def tupla_int(t):  # Convierte una tupla str "int" a tipo int.
    if not t[0]:
        return 0, int(t[1]), int(t[2]), int(t[3])*10
    return int(t[0]), int(t[1]), int(t[2]), int(t[3])*10


def tupla_resta(t1, t2):  # Resta de tiempos.
    return abs(t1[0] - t2[0]), abs(t1[1] - t2[1]), abs(t1[2] - t2[2]), abs(t1[3] - t2[3])


def add_zeros(t):
    t0 = str(t[0])
    t1 = str(t[1])
    t2 = str(t[2])
    t3 = str(t[3])
    if t[0] < 10:
        t0 = "0" + t0
    if t[1] < 10:
        t1 = "0" + t1
    if t[2] < 10:
        t2 = "0" + t2
    if t[3] < 10:
        t3 = "0" + t3
    if t[3] < 100:
        t3 = t3 + "0"
    return f"{t0}:{t1}:{t2},{t3}"


def adjust_time(t, t2, ts):  # recibira una tupla y reajustara la hora
    t = tupla_resta(t, ts)
    t2 = tupla_resta(t2, ts)
    return add_zeros(t), add_zeros(t2)  # Formatea los 0 necesarios.


def obtain_dial(text):
    return patDia.findall(text)


def obtain_time(text):
    return patTime.findall(text)


def printear_time(time, i=0):
    contexto = "Desde"
    if i:
        contexto = "Hasta"
    if time[0]:
        return f"{contexto} {time[0]}[h], {time[1]}[min], {time[2]}[s], {time[3]}[ms]"
    return f"{contexto} {time[1]}[min], {time[2]}[s], {time[3]}[ms]"

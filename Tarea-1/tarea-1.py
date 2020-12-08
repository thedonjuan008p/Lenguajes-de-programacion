import sys
from functions import *


args = sys.argv
nameInput = args[1]
nameOutput = args[2]
with open(nameInput, encoding="utf8") as ifile:
    text = ifile.read()
    ifile.close()


styles = styles_pkg(text)  # Crea un diccionario de estilo con formato nombre_estilo:color
dialogues = obtain_dial(text)  # Extrae la informacion de los dialogos del texto.

print(f"Traduciendo {nameInput} a {nameOutput}.srt")
output = ""
c = 1
if len(args) == 4:  # con intervalo
    interval = args[3]
    initial, final = obtain_time(interval)
    initial = tupla_int(initial)
    final = tupla_int(final)
    print(printear_time(initial))
    print(printear_time(final, 1)) # Esta funcion distingue si esta printeando final o inicio. nada para inicio 1 para final.
    for start, end, style, text in dialogues:
        tStart = obtain_time(start)[0]
        tEnd = obtain_time(end)[0]
        tStart = tupla_int(tStart)
        tEnd = tupla_int(tEnd)
        if (tStart > initial) and (tEnd < final):
            startA, endA = adjust_time(tStart, tEnd, initial)
            color = styles[style]
            output += f'{c}\n{startA} --> {endA}\n<font color="{color}">{text}</font>\n\n'
            c += 1

else:  # sin intervalo
    for start, end, style, text in dialogues:
        start = format_time(start)
        end = format_time(end)
        color = styles[style]
        output += f'{c}\n{start} --> {end}\n<font color="{color}">{text}</font>\n\n'
        c += 1

with open(f"{nameOutput}.srt", "w", encoding="utf8") as ofile:
    ofile.write(output)
    ofile.close()

print("Proceso finalizado.")
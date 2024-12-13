# **BOMBERMAN**

>Bomberman is a strategic, maze-based computer and video game franchise originally developed by Hudson Soft.
>
>Goal of the game is to be the last one standing by eliminating all other players.
>
>Players can be eliminated by strategically placing bombs that explode in multiple directions after certain amount of time.
>
>Bombs can also destroy terrain blocks.
>Destroying terrain blocks can sometimes reveal power-ups.
>
>There are multiple power-ups.
>
>For example:
> - larger explosions
> - drop more bombs at once
> - faster movement speed
>
>Game can be played with up to 4 players.
>
>AI element can be implemented by programming a computer that tries to compete against players.
>

<img src="https://upload.wikimedia.org/wikipedia/en/b/ba/SNES_Super_Bomberman_%28Battle_Mode%29.png">
<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRomI6uVI_8CdkAfkqaV169d3Iutm5ihRZYyw&usqp=CAU" width="335">
<img src="https://upload.wikimedia.org/wikipedia/en/thumb/3/3a/SNES_Super_Bomberman_%28Normal_Game%29.png/200px-SNES_Super_Bomberman_%28Normal_Game%29.png" width="255">


Soovime maksimaalse hinde peale välja minna, et võtta kursuselt kõik, mida pakutakse. Mängu funktsionaalsused ja omadused on välja toodud Wikis, Feature Listis.

Plaanime kasutada Libgdx raamistikku ning kliendi ja serveri suhtluse plaanime realiseerida Kryonet teegi abil.

# Bomberman dokumentatsioon

Bomberman on strateegiline battle royale mäng kus mängija eesmärk on jääda viimasena ellu.
Selleks tuleb mängijal labürindis teised mängijad pommidega õhku lasta ning
seinu õhata kust võib saada uuendusi mängija võimetele. Uuendusteks võivad olla näiteks liikumiskiiruse suurendus, suurem pommide arv mida korraga maha saab panna ning plahvatuse raadiuse suurenemine.

## Mängu installeerimise juhend:

- Pulli projekt
- Ava IntelliJ-s fail "build.gradle"
- Vali "Open as Project"
- Tõmba gradle kõik vajalikud dependency'd
- Rohkem infot: https://libgdx.com/wiki/start/import-and-running
- Navigeeri IntelliJ-s kaustas: bomberman > desktop > src > ee.taltech.iti0301.game
- Käivita fail DesktopLauncher

## Tehniline dokumentatsioon:

Mängu jaoks kasutatakse libGDX libraryt ning serveri jaoks kasutatakse kryonet libraryt.  

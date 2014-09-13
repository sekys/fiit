//Pomenovanie registrov
//#1 cislo
//#2 mocnina
//#3 maxrad
//#4 rad
//#5 cifra
//#6 sucet
//#7 castcisla
//#8 i
//#9 vysledok
//#10 tmp
//#11 tmp1
//#12 tmp2
//#13 tmp3
//#14 tmp4
//#15 tmp5
//#16 tmp6
//#17 tmp7
//#18 tmp8
//#19 tmp9
//#20 tmp10
//#21 tmp11
//#22 tmp12
//#23 tmp13
//#24 tmp14
//#25 tmp15
//#26 tmp16
//#27 tmp17
//#28 tmp18
//#29 tmp19
//#30 tmp20
//#31 tmp21

//Priradenie premennej cislo  
READ 1
LOAD 1
STORE 1
//Priradenie premennej maxrad 
LOAD = 1
STORE 3
//Priradenie premennej mocnina 
LOAD = 0
STORE 2
//While-cyklus
start: 
LOAD 1
SUB 3
JGZERO true
LOAD =0
JUMP res
true: LOAD =1
res: STORE 10

LOAD 10
JZERO end
//Priradenie premennej maxrad 
LOAD 3
MULT =10
STORE 11

LOAD 11
STORE 3
//Priradenie premennej mocnina 
LOAD 2
ADD =1
STORE 12

LOAD 12
STORE 2

JUMP start
end:
//Priradenie premennej maxrad 
LOAD 3
DIV =10
STORE 13

LOAD 13
STORE 3
//Priradenie premennej sucet 
LOAD = 0
STORE 6
//Priradenie premennej castcisla 

LOAD 1
STORE 7
//Priradenie premennej rad 

LOAD 3
STORE 4
//While-cyklus
start1: 
LOAD 7
SUB =0
JGZERO true1
LOAD =0
JUMP res1
true1: LOAD =1
res1: STORE 14

LOAD 14
JZERO end1
//Priradenie premennej cifra 
LOAD 7
DIV 4
STORE 15

LOAD 15
STORE 5
//Podmienka
LOAD 5
SUB =1
JZERO true2
LOAD =0
JUMP res2
true2: LOAD =1
res2: STORE 16

LOAD 16
JZERO end2
//Podmienka splnená
//Priradenie premennej sucet 
LOAD 6
ADD =1
STORE 17

LOAD 17
STORE 6

end2:
//Podmienka
LOAD 5
SUB =1
JGZERO true3
LOAD =0
JUMP res3
true3: LOAD =1
res3: STORE 18

LOAD 18
JZERO end3
//Podmienka splnená
//Priradenie premennej vysledok 

LOAD 5
STORE 9
//Priradenie premennej i
LOAD =1
STORE 8
//While-cyklus
start2: 
LOAD 2
SUB 8
JGTZ true4
LOAD =0
JUMP res4
true4: LOAD =1
res4: STORE 19

LOAD 19
JZERO end4
//Priradenie premennej vysledok 
LOAD 9
MULT 5
STORE 20

LOAD 20
STORE 9
//Priradenie premennej i 
LOAD 8
ADD =1
STORE 21

LOAD 21
STORE 8

JUMP start2
end4:
//Priradenie premennej sucet 
LOAD 6
ADD 9
STORE 22

LOAD 22
STORE 6

end3:
//Priradenie premennej castcisla 
LOAD 5
MULT 4
STORE 23
LOAD 7
SUB 23
STORE 24

LOAD 24
STORE 7
//Priradenie premennej rad 
LOAD 4
DIV =10
STORE 25

LOAD 25
STORE 4

JUMP start1
end1:
//Podmienka
LOAD 6
SUB 1
JZERO true5
LOAD =0
JUMP res5
true5: LOAD =1
res5: STORE 26

LOAD 26
JZERO else
//Podmienka splnená
//Priradenie premennej castcisla 

LOAD 1
STORE 7
//Priradenie premennej rad 

LOAD 1 // vypis cislo
WRITE 0

LOAD 3
STORE 4
//While-cyklus
start3: 
LOAD 7
SUB =0
JGZERO true6
LOAD =0
JUMP res6
true6: LOAD =1
res6: STORE 27

LOAD 27
JZERO end6
//Priradenie premennej cifra 
LOAD 7
DIV 4
STORE 28

LOAD 28
STORE 5
//Priradenie premennej castcisla 
LOAD 5
MULT 4
STORE 29
LOAD 7
SUB 29
STORE 30

LOAD 30
STORE 7
//Priradenie premennej rad 
LOAD 4
DIV =10
STORE 31

LOAD 31
STORE 4

// Vypisanie vysledku
LOAD 5
WRITE 0
LOAD 2 
WRITE 0

JUMP start3
end6:
ACCEPT

JUMP end5
//Podmienka  nesplnená
else:

LOAD 1
WRITE 0
LOAD = 0
WRITE 0
REJECT

end5:
HALT

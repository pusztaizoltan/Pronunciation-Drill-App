# Pronunciation-Drill-App
Vizsga Project

Pronunciation-Drill-App angol (vagy akármilyen) nyelvü szavak kielytésének
gyakorlására generál leckéket, azonos kezdetű (prefix) vagy végű (suffix) (alphabetic és phonetic)
szavak csoportokba gyűjtésével, formázásával, online megjelenítésével.

Megjelenítés után bármilyen online szövegfelolvasó program használata mellett
a csoportosított szavak kiejtése gyakorolható.

Használat előtti követelmény: adatokkal való feltöltés.
Ez idegen nyelvű szavak alphabetikus és fonetikus alakjainak bevitelét jelenti
mint egyetlen szükséges követelmény.

Prefixek és suffixek hozzáadása törlése szavakhoz rendelése egyenként manuálisan is elvégezhető.

- Szó-adatbázis kiinduló - feltöltése flyway migrációval történik.

- Prefix-suffix adatbázisok automatikus generálása a
        http://localhost:8080/generatedata/prefix
        http://localhost:8080/generatedata/suffix
  vonatkozó végpontok használatával történik.
  
- Prefix-suffix alapú gyakorlatok automatikus randomizált generálása a
        http://localhost:8080/generatedata/prefix/drill
        http://localhost:8080/generatedata/suffix/drill
  vonatkozó végpontokon érhető el a már legenerált
  vagy elemenként megadott prefix-suffix adatbázisok alapján.

- Autogenerálás végpontjaira példák:
        src/test/java/com/codelool/pronunciationdrillapp/requesttests/DataGeneratorTest.http
        src/test/java/com/codelool/pronunciationdrillapp/requesttests/DrillGeneratorTest.http

- Szó-adatbázis manuális hozzáférése
        http://localhost:8080/dataio/word/findall
        http://localhost:8080/dataio/word/findone/{id}
        http://localhost:8080/dataio/word/addone
        http://localhost:8080/dataio/word/deleteone/{id}
        http://localhost:8080/dataio/word/updateone/{id}
  végpontokon kezelhető.

- Prefix-adatbázis manuális hozzáférése  
        http://localhost:8080/dataio/prefix/findall
        http://localhost:8080/dataio/prefix/findone/{id}
        http://localhost:8080/dataio/prefix/addone
        http://localhost:8080/dataio/prefix/deleteone/{id}
        http://localhost:8080/dataio/prefix/{id}/words
        http://localhost:8080/dataio/prefix/{id}/addword
        http://localhost:8080/dataio/prefix/{id}/delword
  végpontokon kezelhető.
  
- Suffix-adatbázis manuális hozzáférése
        http://localhost:8080/dataio/suffix/findall
        http://localhost:8080/dataio/suffix/findone/{id}
        http://localhost:8080/dataio/suffix/addone
        http://localhost:8080/dataio/suffix/deleteone/{id}
        http://localhost:8080/dataio/suffix/{id}/words
        http://localhost:8080/dataio/suffix/{id}/addword
        http://localhost:8080/dataio/suffix/{id}/delword
  végpontokon kezelhető.

- Manuális hozzáférés végpontjaira példák:
        src/test/java/com/codelool/pronunciationdrillapp/requesttests/PrefixControllerTest.http
        src/test/java/com/codelool/pronunciationdrillapp/requesttests/SufffixControllerTest.http
        src/test/java/com/codelool/pronunciationdrillapp/requesttests/WordControllerTest.http

- Docker build és run a start.sh -val indítható.



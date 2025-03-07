# cms_project

### Konfiguracja
1. Utworzyć lokalną bazę danych PostgreSQL
2. Skopiować [application.properties.sample](cms%2Fsrc%2Fmain%2Fresources%2Fapplication.properties.sample) i zapisać jako
`application.properties` w tym samym katalogu.
3. Podmienić wymagane pola w `spring.datasource` i `spring.mail`.

### Linki
- Swagger: http://localhost:8080/swagger-ui/index.html
  - pozwala przeprowadzać operacje na zasobach ([User](cms%2Fsrc%2Fmain%2Fjava%2Ffantastic%2Fcms%2Fmodels%2FUser.java), [News](cms%2Fsrc%2Fmain%2Fjava%2Ffantastic%2Fcms%2Fmodels%2FNews.java), itd.)
- Strona główna: http://localhost:8080/home
- Rejestracja: http://localhost:8080/register
- Logowanie: http://localhost:8080/login
- Panel po zalogowaniu: http://localhost:8080
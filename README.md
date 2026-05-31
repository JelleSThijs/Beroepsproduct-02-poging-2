# Implementatieplan – Beroepsproduct 02
**Project:** Fysieke Media Leen Applicatie  
**Ontwikkelaars:** Jelle & Thijs  
**Technologieën:** Java, JavaFX, MySQL/MariaDB

---

## 1. Projectbeschrijving

De applicatie is een platform waarop gebruikers hun fysieke media (films en boeken) kunnen aanbieden om uit te lenen aan anderen. Gebruikers kunnen hun eigen collectie beheren, items van anderen bekijken en via de contactgegevens van de eigenaar afspraken maken om media te lenen. Admins hebben extra rechten om gebruikers en items te beheren.

---

## 2. Functionaliteiten

### 2.1 Gebruikersbeheer
- Registreren met gebruikersnaam, wachtwoord, naam en optioneel e-mail, telefoonnummer en woonplaats
- Inloggen en uitloggen
- Profielpagina met eigen uitleenbare items
- Admin kan andere gebruikers verwijderen

### 2.2 Itembeheer
- Nieuw item toevoegen (film of boek) met titel, samenvatting, afbeelding, maker, jaar en tags
- Item bewerken via het EditScreen
- Item verwijderen (alleen eigenaar of admin)
- Items worden gekoppeld aan de eigenaar (ingelogde gebruiker)

### 2.3 Contact opnemen met eigenaar
- Op de detailpagina van een item zijn de contactgegevens van de eigenaar zichtbaar
- Via een dialoogvenster kunnen het e-mailadres en telefoonnummer gekopieerd worden
- Zo kunnen gebruikers buiten de app afspraken maken om het item te lenen

### 2.4 Zoeken en filteren
- Zoeken op naam via een zoekveld
- Filteren op genre (tag), releasejaar en type (film/boek)
- Combinaties van filters zijn mogelijk

### 2.5 Homepagina
- Toont de 6 meest recent toegevoegde boeken
- Toont de 6 meest recent toegevoegde films

---

## 3. Technische Architectuur

### 3.1 Lagen
| Laag | Verantwoordelijkheid |
|---|---|
| **Screens** | JavaFX schermen voor de gebruikersinterface |
| **Models** | Dataklassen: `Item`, `User`, `Tags` |
| **Database** | Alle SQL-queries via `PreparedStatement` |
| **Application** | Globale state, navigatie en stylesheet |

### 3.2 Databasestructuur
| Tabel | Omschrijving |
|---|---|
| `users` | Gebruikersaccounts met optionele contactgegevens |
| `items` | Fysieke media (films/boeken) aangeboden door gebruikers |
| `tags` | Beschikbare genre-tags |
| `itemtags` | Koppeltabel tussen items en tags |

---

## 4. Schermen

| Scherm | Omschrijving |
|---|---|
| `HomeScreen` | Startpagina met recent toegevoegde uitleenbare items |
| `SearchScreen` | Zoekresultaten op basis van filters |
| `ItemScreen` | Detailpagina van een item met contactgegevens van de eigenaar |
| `ProfileScreen` | Profielpagina met de collectie van een gebruiker |
| `NewScreen` | Formulier voor het toevoegen van een nieuw item aan de collectie |
| `EditScreen` | Formulier voor het bewerken van een bestaand item |
| `LoginScreen` | Inlogformulier |
| `RegisterScreen` | Registratieformulier |

---

## 5. Database Queries

De applicatie maakt gebruik van de volgende CRUD-operaties en query-clausules:

### 5.1 CRUD
| Operatie | Methode |
|---|---|
| **Create** | `addItem()`, `addUser()` |
| **Read** | `getItem()`, `getItems()`, `getSearchResults()`, `loginUser()`, etc. |
| **Update** | `updateItem()`, `setItemTags()` |
| **Delete** | `deleteItem()`, `deleteUser()` |

### 5.2 Gebruikte query-clausules
| Clausule | Waar gebruikt |
|---|---|
| `WHERE` | Bijna alle queries |
| `ORDER BY` | `getItems()` – sorteren op ID aflopend |
| `LIMIT` | `getItems()` – maximaal aantal resultaten |
| `LIKE` | `getSearchResults()` – zoeken op naam |
| `AND` | `getSearchResults()`, `loginUser()` |
| `IN (SELECT ...)` | `getSearchResults()` – filteren op tag via subquery |

---

## 6. Beveiliging

- Alle queries gebruiken `PreparedStatement` om SQL-injectie te voorkomen
- Databasegegevens worden opgeslagen in een extern `databaseCredentials.properties` bestand dat niet in versiebeheer wordt opgenomen
- Een `databaseCredentialsTemplate.properties` bestand is beschikbaar als referentie

---

## 7. Validatie

- Het releasejaar accepteert alleen cijfers en maximaal 4 tekens (via `TextFormatter`)
- Het telefoonnummerveld accepteert alleen cijfers (via `TextFormatter`)
- Gebruikersnaam wordt gecontroleerd op duplicaten voor registratie

---

## 8. Unit Tests

Unit tests zijn geschreven voor de modelklassen en de databaseklasse:

| Testklasse | Wat wordt getest |
|---|---|
| `ItemTest` | Getters, `setId()`, `setData()`, standaardwaarden |
| `UserTest` | Getters, standaard admin-waarde, optionele velden |
| `TagsTest` | `getNameById()`, `getIdByName()`, `getName()`, foutafhandeling |
| `DatabaseTest` | Alle CRUD-methoden tegen de lokale database |

---

## 9. Installatie

1. Kloon de repository
2. Kopieer `databaseCredentialsTemplate.properties` naar `databaseCredentials.properties`
3. Vul de databasegegevens in
4. Importeer het meegeleverde SQL-bestand in MySQL/MariaDB
5. Start de applicatie via IntelliJ of met Maven

---

## 10. Taakverdeling

| Taak | Verantwoordelijke |
|---|---|
| Database klasse & queries | Jelle & Thijs |
| Schermen (UI) | Jelle & Thijs |
| Modelklassen | Jelle & Thijs |
| Unit tests | Jelle & Thijs |
| Documentatie | Jelle & Thijs |

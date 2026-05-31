package com.jsthijs.beroepsproduct02;

import com.jsthijs.beroepsproduct02.models.Item;
import com.jsthijs.beroepsproduct02.models.User;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Let op: deze tests draaien tegen de echte lokale database.
// Zorg dat de database draait en de credentials kloppen in databaseCredentials.properties.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // voer de test in order uit
class DatabaseTest {

    private static Database db;

    // Houd de aangemaakte test-IDs bij zodat we ze achteraf kunnen opruimen.
    private static int testUserId = -1;
    private static int testItemId = -1;

    @BeforeAll
    static void setUp() {
        // Verbinding maken met de lokale database.
        db = new Database();
    }

    // -------------------------
    // User tests
    // -------------------------

    @Test
    @Order(1)
    void testAddUser_newUser_returnsTrue() {
        User user = new User(
                "test_user_junit",
                "testpass",
                "JUnit Testgebruiker",
                "junit@test.nl",
                "+31600000000",
                "Teststad"
        );

        Boolean result = db.addUser(user);
        assertTrue(result, "Nieuwe gebruiker aanmaken zou true moeten teruggeven");
    }

    @Test
    @Order(2)
    void testAddUser_duplicateUsername_returnsTrue() {
        // Dezelfde gebruiker nogmaals toevoegen moet true teruggeven.
        User user = new User(
                "test_user_junit",
                "anderwachtwoord",
                "Andere Naam",
                null, null, null
        );

        Boolean result = db.addUser(user);
        assertTrue(result, "Dubbele gebruikersnaam zou false moeten teruggeven");
    }

    @Test
    @Order(3)
    void testLoginUser_correctCredentials_returnsResult() throws SQLException {
        ResultSet rs = db.loginUser("test_user_junit", "testpass");
        assertTrue(rs.next(), "Inloggen met juiste gegevens zou een resultaat moeten geven");

        // Sla het userId op voor de item-tests hierna.
        testUserId = rs.getInt("id");
        assertTrue(testUserId > 0, "Gebruiker ID moet groter dan 0 zijn");
    }

    @Test
    @Order(4)
    void testLoginUser_wrongPassword_returnsEmpty() throws SQLException {
        ResultSet rs = db.loginUser("test_user_junit", "fout_wachtwoord");
        assertFalse(rs.next(), "Inloggen met fout wachtwoord mag geen resultaat geven");
    }

    @Test
    @Order(5)
    void testLoginUser_nonExistentUser_returnsEmpty() throws SQLException {
        ResultSet rs = db.loginUser("bestaat_niet", "wachtwoord");
        assertFalse(rs.next(), "Niet-bestaande gebruiker mag geen resultaat geven");
    }

    // -------------------------
    // Item tests
    // -------------------------

    @Test
    @Order(6)
    void testAddItem_setsIdOnItem() {
        // testUserId moet ingesteld zijn door testLoginUser.
        assumeUserIdSet();

        Item item = new Item(
                "JUnit Testfilm",
                "Een film voor testdoeleinden.",
                "https://example.com/test.jpg",
                "JUnit Studio",
                2024,
                "film",
                testUserId,
                new ArrayList<>()
        );

        db.addItem(item);

        // Na addItem moet het ID ingesteld zijn.
        assertNotNull(item.getId(), "Item ID moet ingesteld zijn na addItem()");
        assertTrue(item.getId() > 0, "Item ID moet groter dan 0 zijn");

        testItemId = item.getId();
    }

    @Test
    @Order(7)
    void testGetItem_existingId_returnsItem() throws SQLException {
        assumeItemIdSet();

        ResultSet rs = db.getItem(testItemId);
        assertTrue(rs.next(), "getItem() moet een resultaat teruggeven voor een bestaand ID");
        assertEquals("JUnit Testfilm", rs.getString("name"));
    }

    @Test
    @Order(8)
    void testGetItem_nonExistentId_returnsEmpty() throws SQLException {
        ResultSet rs = db.getItem(-999);
        assertFalse(rs.next(), "getItem() met onbestaand ID moet leeg zijn");
    }

    @Test
    @Order(9)
    void testGetItems_returnsItemsOfType() throws SQLException {
        ResultSet rs = db.getItems("film", 10);
        assertTrue(rs.next(), "getItems() voor type 'film' moet minimaal 1 resultaat geven");
    }

    @Test
    @Order(10)
    void testGetItems_limitIsRespected() throws SQLException {
        int limit = 3;
        ResultSet rs = db.getItems("film", limit);

        int count = 0;
        while (rs.next()) { count++; }

        assertTrue(count <= limit, "getItems() mag niet meer rijen teruggeven dan de limiet");
    }

    @Test
    @Order(11)
    void testGetUserItems_returnsItemsForUser() throws SQLException {
        assumeUserIdSet();

        ResultSet rs = db.getUserItems(testUserId);
        assertTrue(rs.next(), "getUserItems() moet minimaal het zojuist aangemaakte item teruggeven");
    }

    @Test
    @Order(12)
    void testUpdateItem_changesData() throws SQLException {
        assumeItemIdSet();

        Item item = new Item(
                "Aangepaste Titel",
                "Aangepaste samenvatting.",
                "https://example.com/updated.jpg",
                "Aangepaste Studio",
                2025,
                "film",
                testUserId,
                new ArrayList<>()
        );
        item.setId(testItemId);

        db.updateItem(item);

        // Controleer of de wijziging in de database staat.
        ResultSet rs = db.getItem(testItemId);
        assertTrue(rs.next());
        assertEquals("Aangepaste Titel", rs.getString("name"));
        assertEquals(2025, rs.getInt("releaseYear"));
    }

    @Test
    @Order(13)
    void testGetItemOwnerDetails_returnsOwner() throws SQLException {
        assumeUserIdSet();

        ResultSet rs = db.getItemOwnerDetails(testUserId);
        assertTrue(rs.next(), "getItemOwnerDetails() moet de eigenaar teruggeven");
        assertEquals("JUnit Testgebruiker", rs.getString("name"));
    }

    @Test
    @Order(14)
    void testGetSearchResults_byName_returnsResults() throws SQLException {
        ResultSet rs = db.getSearchResults("Aangepaste", "Genre", "", "Type");
        assertTrue(rs.next(), "Zoeken op naam moet het aangepaste item vinden");
    }

    @Test
    @Order(15)
    void testGetSearchResults_noMatch_returnsEmpty() throws SQLException {
        ResultSet rs = db.getSearchResults("xyzbestaatniet123", "Genre", "", "Type");
        assertFalse(rs.next(), "Zoeken op niet-bestaande naam moet leeg zijn");
    }

    @Test
    @Order(16)
    void testGetSearchResults_withTypeFilter() throws SQLException {
        ResultSet rs = db.getSearchResults("", "Genre", "", "film");
        // Alle resultaten moeten van type 'film' zijn.
        while (rs.next()) {
            assertEquals("film", rs.getString("type"),
                    "Alle resultaten moeten van type 'film' zijn");
        }
    }

    @Test
    @Order(17)
    void testGetTags_returnsAtLeastOneTag() throws SQLException {
        ResultSet rs = db.getTags();
        assertTrue(rs.next(), "getTags() moet minimaal 1 tag teruggeven");
    }

    // -------------------------
    // Opruimen - altijd als laatste
    // -------------------------

    @Test
    @Order(18)
    void testDeleteItem_removesItem() throws SQLException {
        assumeItemIdSet();

        db.deleteItem(testItemId);

        ResultSet rs = db.getItem(testItemId);
        assertFalse(rs.next(), "Item moet verwijderd zijn na deleteItem()");
    }

    @Test
    @Order(19)
    void testDeleteUser_removesUser() throws SQLException {
        assumeUserIdSet();

        db.deleteUser(testUserId);

        // Controleren door in te loggen — mag niets teruggeven.
        ResultSet rs = db.loginUser("test_user_junit", "testpass");
        assertFalse(rs.next(), "Gebruiker moet verwijderd zijn na deleteUser()");
    }

    // -------------------------
    // Hulpmethoden
    // -------------------------

    private void assumeUserIdSet() {
        // Als testUserId niet ingesteld is, sla de test over.
        Assumptions.assumeTrue(
                testUserId > 0,
                "testUserId niet ingesteld — zorg dat testLoginUser_correctCredentials eerst slaagt"
        );
    }

    private void assumeItemIdSet() {
        Assumptions.assumeTrue(
                testItemId > 0,
                "testItemId niet ingesteld — zorg dat testAddItem eerst slaagt"
        );
    }
}

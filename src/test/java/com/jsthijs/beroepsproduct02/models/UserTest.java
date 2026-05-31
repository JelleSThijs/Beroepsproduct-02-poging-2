package com.jsthijs.beroepsproduct02.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private User adminUser;

    @BeforeEach
    void setUp() {
        user = new User(
                "john_doe",
                "pass1234",
                "John Doe",
                "john@example.com",
                "+31612345678",
                "Amsterdam"
        );

        adminUser = new User(
                "admin",
                "adminpass",
                "Admin User",
                "admin@example.com",
                "+31611111111",
                "Utrecht",
                1
        );
    }

    @Test
    void testGetUsername() {
        assertEquals("john_doe", user.getUsername());
        assertEquals("admin", adminUser.getUsername());
    }

    @Test
    void testGetPassword() {
        assertEquals("pass1234", user.getPassword());
        assertEquals("adminpass", adminUser.getPassword());
    }

    @Test
    void testGetName() {
        assertEquals("John Doe", user.getName());
        assertEquals("Admin User", adminUser.getName());
    }

    @Test
    void testGetEmail() {
        assertEquals("john@example.com", user.getEmail());
        assertEquals("admin@example.com", adminUser.getEmail());
    }

    @Test
    void testGetPhoneNumber() {
        assertEquals("+31612345678", user.getPhoneNumber());
        assertEquals("+31611111111", adminUser.getPhoneNumber());

    }

    @Test
    void testGetCity() {
        assertEquals("Amsterdam", user.getCity());
        assertEquals("Utrecht", adminUser.getCity());
    }

    @Test
    void testAdminCredentials() {
        // Nieuwe gebruikers zijn standaard geen admin.
        assertEquals(0, user.getIsAdmin());
        assertEquals(1, adminUser.getIsAdmin());
    }

    @Test
    void testOptionalFieldsCanBeNull() {
        // Optionele velden mogen null zijn bij registratie.
        User userWithNulls = new User(
                "jane",
                "secret",
                "Jane",
                null,
                null,
                null
        );

        assertNull(userWithNulls.getEmail());
        assertNull(userWithNulls.getPhoneNumber());
        assertNull(userWithNulls.getCity());
    }

    @Test
    void testOptionalFieldsCanBeEmpty() {
        // Optionele velden mogen ook leeg zijn.
        User userWithEmpty = new User(
                "jane",
                "secret",
                "Jane",
                "",
                "",
                ""
        );

        assertEquals("", userWithEmpty.getEmail());
        assertEquals("", userWithEmpty.getPhoneNumber());
        assertEquals("", userWithEmpty.getCity());
    }
}

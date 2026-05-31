package com.jsthijs.beroepsproduct02.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Tags laadt normaal gesproken uit de database, dus we testen de logica
// door de interne lijsten via reflectie te vullen, zonder een DB-verbinding.
class TagsTest {

    private Tags tags;

    @BeforeEach
    void setUp() throws Exception {
        // Tags aanmaken zonder de constructor (die DB-connectie nodig heeft).
        tags = new Tags(
                new ArrayList<Integer>(List.of(1,2,3)),
                new ArrayList<String>(List.of("Action", "Comedy", "Drama"))
        );
    }

    @Test
    void testGetNameById() {
        assertEquals("Action", tags.getNameById(1));
        assertEquals("Comedy", tags.getNameById(2));
        assertEquals("Drama", tags.getNameById(3));
    }

    @Test
    void testGetIdByName() {
        assertEquals(1, tags.getIdByName("Action"));
        assertEquals(2, tags.getIdByName("Comedy"));
        assertEquals(3, tags.getIdByName("Drama"));
    }

    @Test
    void testGetName() {
        ArrayList<String> names = tags.getName();
        assertEquals(3, names.size());
        assertTrue(names.contains("Action"));
        assertTrue(names.contains("Comedy"));
        assertTrue(names.contains("Drama"));
    }

    @Test
    void testGetNameByIdUnknown() {
        // Onbekend ID geeft een IndexOutOfBoundsException.
        assertThrows(IndexOutOfBoundsException.class, () -> tags.getNameById(99));
    }

    @Test
    void testGetIdByNameUnknown() {
        // Onbekende naam geeft -1 terug via indexOf, wat tot een fout leidt.
        assertThrows(IndexOutOfBoundsException.class, () -> tags.getIdByName("Unknown"));
    }
}

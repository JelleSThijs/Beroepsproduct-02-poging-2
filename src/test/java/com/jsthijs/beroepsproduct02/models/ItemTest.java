package com.jsthijs.beroepsproduct02.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item item;
    private ArrayList<String> tags;

    @BeforeEach
    void setUp() {
        tags = new ArrayList<>();
        tags.add("Action");
        tags.add("Sci-Fi");

        item = new Item(
                "Inception",
                "A thief enters dreams.",
                "https://example.com/inception.jpg",
                "Christopher Nolan",
                2010,
                "film",
                1,
                tags
        );
    }

    @Test
    void testGetName() {
        assertEquals("Inception", item.getName());
    }

    @Test
    void testGetSummary() {
        assertEquals("A thief enters dreams.", item.getSummary());
    }

    @Test
    void testGetImage() {
        assertEquals("https://example.com/inception.jpg", item.getImage());
    }

    @Test
    void testGetMaker() {
        assertEquals("Christopher Nolan", item.getMaker());
    }

    @Test
    void testGetReleaseYear() {
        assertEquals(2010, item.getReleaseYear());
    }

    @Test
    void testGetType() {
        assertEquals("film", item.getType());
    }

    @Test
    void testGetUserId() {
        assertEquals(1, item.getUserId());
    }

    @Test
    void testGetTags() {
        assertEquals(2, item.getTags().size());
        assertTrue(item.getTags().contains("Action"));
        assertTrue(item.getTags().contains("Sci-Fi"));
    }

    @Test
    void testSetId() {
        item.setId(42);
        assertEquals(42, item.getId());
    }

    @Test
    void testIdIsNullByDefault() {
        // Een nieuw item heeft nog geen ID totdat de database er een toekent.
        assertNull(item.getId());
    }

    @Test
    void testSetData() {
        ArrayList<String> newTags = new ArrayList<>();
        newTags.add("Drama");

        item.setData(
                "Dune",
                "A desert planet.",
                "https://example.com/dune.jpg",
                "Denis Villeneuve",
                2021,
                "film",
                2,
                newTags
        );

        assertEquals("Dune", item.getName());
        assertEquals("A desert planet.", item.getSummary());
        assertEquals("https://example.com/dune.jpg", item.getImage());
        assertEquals("Denis Villeneuve", item.getMaker());
        assertEquals(2021, item.getReleaseYear());
        assertEquals("film", item.getType());
        assertEquals(2, item.getUserId());
        assertEquals(1, item.getTags().size());
        assertTrue(item.getTags().contains("Drama"));
    }
}

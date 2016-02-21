package IFO.Tests;

import IFO.Ifofile;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class IfofileTest {

    Ifofile file;// = new Ifofile("C:\\Users\\Stanlezz\\Desktop\\asd");

    @Test
    public void descriptionTest () {
        assertEquals("", file.getDescription());

        file.setDescription("Toto je dummy description.");
        assertEquals("Toto je dummy description.", file.getDescription());

        file.setDescription("");
        assertEquals("", file.getDescription());
    }

    @Test
    public void popularityTest () {
        assertSame(0, file.getPopularity());

        for (int i = 0; i < 100; i++) file.incrementPopularity();
        assertSame(100, file.getPopularity());

        file.zeroPopularity();
        assertSame(0, file.getPopularity());

        for (int i = 0; i < 6000; i++) {
            file.setRawPopularity(i);
            assertEquals(Integer.valueOf(i), file.getPopularity());
        }
    }

    @Test
    public void categoriesTest () {
        assertEquals(new HashSet<String>(){}, file.getCategories());

        file.addCategory("Kamaráti");
        assertEquals(new HashSet<>(Arrays.asList("Kamaráti")), file.getCategories());

        file.removeCategory("Kamaráti");
        assertEquals(new HashSet<String>(){}, file.getCategories());

        file.changeCategoryName("Kamaráti", "Priatelia");
        assertEquals(new HashSet<>(), file.getCategories());

        file.addCategory("Mesto");
        file.changeCategoryName("Mesto", "Obec");
        assertEquals(new HashSet<>(Arrays.asList("Obec")), file.getCategories());

        file.removeCategory("Obec");
        file.removeCategory("Obec");
        assertEquals(new HashSet<String>(){}, file.getCategories());

        assertTrue(file.addCategory("Nová kategória"));
        assertFalse(file.addCategory("Nová kategória"));

        assertTrue(file.changeCategoryName("Nová kategória", "Kratšia"));
        file.removeCategory("Kratšia");
        assertFalse(file.changeCategoryName("Kratšia", "Nová"));
    }

    @Test
    public void tagsTest () {
        file.addCategory("Kamaráti");
        assertEquals(new HashSet<String>(){}, file.getTagsFromCategory("Kamaráti"));

        assertTrue(file.addTag("Kamaráti", "Jurko"));
        assertEquals(new HashSet<>(Arrays.asList("Jurko")), file.getTagsFromCategory("Kamaráti"));

        assertTrue(file.addTags("Kamaráti", new HashSet<>(Arrays.asList("Jurko","Stanko","Neviem"))));
        assertEquals(new HashSet<>(Arrays.asList("Jurko", "Stanko", "Neviem")), file.getTagsFromCategory("Kamaráti"));

        file.removeTag("Kamaráti", "Neviem");
        assertEquals(new HashSet<>(Arrays.asList("Jurko", "Stanko")), file.getTagsFromCategory("Kamaráti"));

        file.removeTag("Kamaráti", "stanko");
        assertEquals(new HashSet<>(Arrays.asList("Jurko", "Stanko")), file.getTagsFromCategory("Kamaráti"));

        assertTrue(file.changeTagName("Kamaráti", "Jurko", "Janko"));
        assertEquals(new HashSet<>(Arrays.asList("Janko", "Stanko")), file.getTagsFromCategory("Kamaráti"));

        assertFalse(file.addTag("Kamaráti", "Janko"));

        file.addTag("Kamaráti", "Emil");
        file.addTag("Kamaráti", "Stanko");
        file.addTag("Kamaráti", "Zuzana");
        file.addTag("Peter", "Kamaráti");
        file.addTag("Kamaráti", "");
        assertTrue(file.addTag("Mesto", "Bratislava"));
        assertEquals(new HashSet<>(Arrays.asList("Janko", "Stanko", "", "Emil", "Zuzana")), file.getTagsFromCategory("Kamaráti"));
        assertEquals(new HashSet<>(Arrays.asList("Janko", "Stanko", "", "Emil", "Zuzana", "Bratislava", "Kamaráti")),
                file.getAllTags());
    }

}
package systems;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrencySystemTest {

    @Test
    void testInitialization() {
        CurrencySystem cs = new CurrencySystem(500);
        assertEquals(500, cs.getBalance());
    }

    @Test
    void testEarn() {
        CurrencySystem cs = new CurrencySystem(100);
        cs.earn(50);
        assertEquals(150, cs.getBalance());

        cs.earn(-20); // Should ignore negative
        assertEquals(150, cs.getBalance());
    }

    @Test
    void testSpend() {
        CurrencySystem cs = new CurrencySystem(100);

        assertTrue(cs.spend(50));
        assertEquals(50, cs.getBalance());

        assertFalse(cs.spend(60), "Should fail if not enough balance");
        assertEquals(50, cs.getBalance(), "Balance should not change on failure");

        assertFalse(cs.spend(-10), "Should ignore negative spend");
    }

    @Test
    void testCanAfford() {
        CurrencySystem cs = new CurrencySystem(100);
        assertTrue(cs.canAfford(100));
        assertTrue(cs.canAfford(50));
        assertFalse(cs.canAfford(101));
    }
}
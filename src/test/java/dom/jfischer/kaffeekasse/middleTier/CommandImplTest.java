/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

import backend.fake.DAOImpl;
import dom.jfischer.kaffeekasse.backend.BackendError;
import dom.jfischer.kaffeekasse.backend.DAO;
import dom.jfischer.kaffeekasse.backend.ar.AccountEntry;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import frontend.fake.Slave;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * unit test.
 *
 * @author jfischer
 */
public class CommandImplTest {

    /**
     * frontend.
     */
    private Command frontend;

    /**
     * channel for transmitting error information from backend to frontend.
     */
    private Slave slave;

    /**
     * constructor.
     */
    public CommandImplTest() {
    }

    /**
     * not used.
     */
    @BeforeAll
    public static void setUpClass() {
    }

    /**
     * not used.
     */
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * constructs items.
     */
    @BeforeEach
    public void setUp() {
        this.slave = new Slave();
        DAO dao = new DAOImpl(this.slave);
        TallyPDF tallyPDF = new TallyPDFImpl(dao, this.slave);
        this.frontend = new CommandImpl(dao, this.slave, tallyPDF);
    }

    /**
     * not really used.
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of addParticipant method, of class CommandImpl.
     */
    @Test
    public void testAddParticipant() {
        setUp();

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(0, jArray.size());
        }

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 0);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
        }

        {
            assertTrue(this.slave.isErrorMode());
            assertTrue(
                    this.slave.getState()
                    == BackendError.PARTICIPANT_ALREADY_EXISTS);
        }

        this.slave.resetState();

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 0);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        tearDown();
    }

    /**
     * Test of clear method, of class CommandImpl.
     */
    @Test
    public void testClear() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.setPrice(7);
            this.frontend.coffeeIn(name, 500);
            this.frontend.coffeeOut(name, 3);
            this.frontend.clear();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 479);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.listPeriods();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(2, jArray.size());

            {
                JsonObject jObject = (JsonObject) jArray.get(0);
                Integer value = (Integer) jObject.get("price");
                assertTrue(value == 7);
            }
            {
                JsonObject jObject = (JsonObject) jArray.get(1);
                Integer value = (Integer) jObject.get("price");
                assertTrue(value == 7);
            }
        }

        tearDown();
    }

    /**
     * Test of coffeeIn method, of class CommandImpl.
     */
    @Test
    public void testCoffeeIn() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            int amount = 500;
            this.frontend.coffeeIn(name, amount);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 500);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.listEntries();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("amount");
                assertTrue(value == 500);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("participant");
                assertEquals("Mickey Mouse", value);
            }
        }

        tearDown();
    }

    /**
     * Test of coffeeOut method, of class CommandImpl.
     */
    @Test
    public void testCoffeeOut() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            int nrCups = 3;
            this.frontend.coffeeOut(name, nrCups);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 0);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 3);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.listEntries();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("amount");
                assertTrue(value == 0);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 3);
            }
            {
                String value = (String) jObject.get("participant");
                assertEquals("Mickey Mouse", value);
            }
        }

        tearDown();
    }

    /**
     * Test of deleteEntry method, of class CommandImpl.
     */
    @Test
    public void testDeleteEntry() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            int nrCups = 3;
            this.frontend.coffeeOut(name, nrCups);
        }

        this.frontend.listEntries();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            String id;

            {
                JSONObject jsonObject = this.slave.getJSONObject();
                assertTrue(jsonObject.getJsonObject() == null);

                JsonArray jArray = jsonObject.getJsonArray();
                assertEquals(1, jArray.size());

                JsonObject jObject = (JsonObject) jArray.get(0);
                id = (String) jObject.get("id");
            }

            this.frontend.deleteEntry(id);

            {
                assertFalse(this.slave.isErrorMode());
                assertTrue(this.slave.getState() == BackendError.OK);
            }

            this.frontend.listEntries();

            {
                assertFalse(this.slave.isErrorMode());
                assertTrue(this.slave.getState() == BackendError.OK);
            }

            {
                JSONObject jsonObject = this.slave.getJSONObject();
                assertTrue(jsonObject.getJsonObject() == null);

                JsonArray jArray = jsonObject.getJsonArray();
                assertEquals(0, jArray.size());
            }

            this.frontend.deleteEntry(id);
        }

        {
            assertTrue(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.ENTRY_NOT_FOUND);
        }

        tearDown();
    }

    /**
     * Test of getBankDeposit method, of class CommandImpl.
     */
    @Test
    public void testGetBankDeposit() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.pay(name, 3000);
            this.frontend.getBankDeposit();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 3000);
        }

        tearDown();
    }

    /**
     * Test of getDeposit method, of class CommandImpl.
     */
    @Test
    public void testGetDeposit() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.coffeeIn(name, 500);
            this.frontend.getDeposit(name);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 500);
        }

        tearDown();
    }

    /**
     * Test of getNrCups method, of class CommandImpl.
     */
    @Test
    public void testGetNrCups() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.coffeeOut(name, 3);
            this.frontend.getNrCups(name);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 3);
        }

        tearDown();
    }

    /**
     * Test of getPrice method, of class CommandImpl.
     */
    @Test
    public void testGetPrice() {
        setUp();

        {
            this.frontend.setPrice(7);
            this.frontend.getPrice();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 7);
        }

        tearDown();
    }

    /**
     * Test of inactivateParticipant method, of class CommandImpl.
     */
    @Test
    public void testInactivateParticipant() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.inactivateParticipant(name);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(0, jArray.size());
        }

        tearDown();
    }

    /**
     * Test of listEntries method, of class CommandImpl.
     */
    @Test
    public void testListEntries() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.coffeeOut(name, 3);
            this.frontend.listEntries();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 3);
            }
            {
                String value = (String) jObject.get("participant");
                assertEquals("Mickey Mouse", value);
            }
        }

        tearDown();
    }

    /**
     * Test of listParticipants method, of class CommandImpl.
     */
    @Test
    public void testListParticipants() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.coffeeOut(name, 3);
            this.frontend.listParticipants();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 3);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        tearDown();
    }

    /**
     * Test of listPeriods method, of class CommandImpl.
     */
    @Test
    public void testListPeriods() {
        setUp();

        this.frontend.listPeriods();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());
        }

        tearDown();
    }

    /**
     * Test of pay method, of class CommandImpl.
     */
    @Test
    public void testPay() {
        setUp();

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            int amount = 3000;
            this.frontend.pay(name, amount);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 3000);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.listEntries();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("amount");
                assertTrue(value == 3000);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("participant");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.getBankDeposit();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 3000);
        }

        tearDown();

    }

    /**
     * Test of setPrice method, of class CommandImpl.
     */
    @Test
    public void testSetPrice() {
        setUp();

        this.frontend.setPrice(7);

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        this.frontend.getPrice();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }
        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 7);
        }

        this.frontend.setPrice(0);

        {
            assertTrue(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.ZERO_PRICE);
        }

        this.slave.resetState();

        this.frontend.getPrice();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendError.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 7);
        }

        tearDown();
    }

}

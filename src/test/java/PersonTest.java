import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class PersonTest{
  Person testPerson;
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Before
  public void setUp(){
    testPerson = new Person("Henry", "henry@henry.com");
  }

  @Test
  public void person_instantiatesCorrectly_true() {
    assertTrue(testPerson instanceof Person);
  }

  @Test
  public void getName_personInstantiatesWithName_Henry() {
    assertEquals("Henry", testPerson.getName());
  }

  @Test
  public void getName_personInstantiatesWithEmail_String() {
    assertEquals("henry@henry.com", testPerson.getEmail());
  }

  @Test
  public void equals_returnsTrueIfPropertiesAreSame_true(){
    Person testPerson2 = new Person("Henry", "henry@henry.com");
    assertTrue(testPerson.equals(testPerson2));
  }

  @Test
  public void save_insertsObjectIntoDatabase_Person() {
    testPerson.save();
    Person testPerson2 = null;
    try(Connection con = DB.sql2o.open()){
      testPerson2 = con.createQuery("SELECT * FROM persons WHERE name='Henry'")
      .executeAndFetchFirst(Person.class);
    }
    assertTrue(testPerson2.equals(testPerson));
  }

  @Test
  public void all_returnsAllInstancesOfPerson_true() {
    testPerson.save();
    Person testPerson2 = new Person("Harriet", "harriet@harriet.com");
    testPerson2.save();
    assertEquals(true, Person.all().get(0).equals(testPerson));
    assertEquals(true, Person.all().get(1).equals(testPerson2));
  }

  @Test
  public void save_assignsIdToObject() {
    testPerson.save();
    Person testPerson2 = Person.all().get(0);
    assertEquals(testPerson.getId(), testPerson2.getId());
  }

  @Test
  public void find_returnsPersonWithSameId_secondPerson() {
    testPerson.save();
    Person testPerson2 = new Person("Harriet", "harriet@harriet.com");
    testPerson2.save();
    assertEquals(Person.find(testPerson2.getId()), testPerson2);
  }
}

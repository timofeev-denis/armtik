/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.voskhod.tik;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timofeev
 */
public class PersonsCollectionTest {
    
    public PersonsCollectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getLabels method, of class PersonsCollection.
     */
    @org.junit.Test
    public void testGetLabelsOnePerson() {
        System.out.println("getLabels");
        PersonsCollection instance = new PersonsCollection();
        instance.addPerson(new Person("Васильев", "Вася", "Васильевич", 42));
        String expResult = "\"   Васильев В.В.    \"";
        String result = instance.getLabels(0, 0);
        assertEquals(expResult, result);
    }

    @org.junit.Test
    public void testGetLabelsTwoPersons() {
        System.out.println("getLabels");
        PersonsCollection instance = new PersonsCollection();
        instance.addPerson(new Person("Васильев", "Иван", "Аганесович", 42));
        instance.addPerson(new Person("Петров", "Карен", "Назарович", 21));
        String expResult = "\"   Васильев И.А.    \", \"    Петров К.Н.     \"";
        String result = instance.getLabels(0, 1);
        assertEquals(expResult, result);
    }

    /**
     * Test of getVotes method, of class PersonsCollection.
     */
    @org.junit.Test
    public void testGetVotesOnePerson() {
        System.out.println("getVotes");
        PersonsCollection instance = new PersonsCollection();
        instance.addPerson(new Person("Вася", "", "", 42));
        String expResult = "100";
        String result = instance.getVotes(0, 0);
        assertEquals(expResult, result);
    }

    /**
     * Test of getVotes method, of class PersonsCollection.
     */
    @org.junit.Test
    public void testGetVotesTwoPersons() {
        System.out.println("getVotes");
        PersonsCollection instance = new PersonsCollection();
        instance.addPerson(new Person("Вася", "", "", 42));
        instance.addPerson(new Person("Петя", "", "", 21));
        String expResult = "66.7, 33.3";
        String result = instance.getVotes(0, 1);
        assertEquals(expResult, result);
    }
    
    @org.junit.Test
    public void testGetBarMax() {
        System.out.println("getBarMax");
        PersonsCollection instance = new PersonsCollection();
        instance.addPerson(new Person("Вася", "", "", 42));
        instance.addPerson(new Person("Петя", "", "", 21));
        String expResult = "66.7";
        String result = instance.getBarMax();
        assertEquals(expResult, result);
    }
    
}

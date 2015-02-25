/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buttso.demo.weblogic.rest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sbutton
 */
public class AccountBeanTest {

    public AccountBeanTest() {
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
     * Test of getName method, of class AccountBean.
     */
    @Test
    public void testGetName() {
        String expResult = "Jack";
        AccountBean instance = new AccountBean(expResult, 100.00F);
        String result = instance.getName();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getAmount method, of class AccountBean.
     */
    @Test
    public void testGetAmount() {
        Float expResult = 56.67F;
        AccountBean instance = new AccountBean("Jack", expResult);
        Float result = instance.getAmount();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of deposit method, of class AccountBean.
     */
    @Test
    public void testDeposit() {
        AccountBean instance = new AccountBean("Jack", 0.0F);
        Float deposit = 60.99F;
        instance.deposit(deposit);
        Assert.assertEquals(deposit, instance.getAmount());
        instance.deposit(deposit);
        Assert.assertEquals((Float)(deposit*2.F), instance.getAmount());
    }

    /**
     * Test of withdrawal method, of class AccountBean.
     */
    @Test
    public void testWithdrawal() {
        Float withdrawal = 34.78F;
        AccountBean instance = new AccountBean("Jack", withdrawal*2);
        instance.withdrawal(withdrawal);
        Assert.assertEquals(withdrawal, instance.getAmount());
        instance.withdrawal(withdrawal);
        Assert.assertEquals((Float)0F, instance.getAmount());
    }

}

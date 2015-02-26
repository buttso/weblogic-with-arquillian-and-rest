/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buttso.demo.weblogic.rest;

import com.sun.jersey.api.client.ClientResponse;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author sbutton
 */
@RunWith(Arquillian.class)
public class AccountServiceTest {

    @Inject
    AccountService accountService;

    @ArquillianResource
    URL DEPLOYMENT_URL;

    URL resturl;
    
    private static final boolean DEBUG = false;

    public AccountServiceTest() {
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClass(AccountBean.class)
                .addClass(AccountService.class)
                .addClass(AccountApplication.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        if(DEBUG) {
            System.out.printf("\n\t%s\n", war.toString(true));
        }
        
        return war;
    }

    @Before
    public void before() {
        //resturl = new URL(url.toExternalForm() + "rest/account/create/Jack");        
    }

    /**
     * Test of getAmountForAccount method, of class AccountService.
     */
    //@Test
    public void testGetAmountForAccount() throws Exception {
    }

    /**
     * Test of createAccount method, of class AccountService.
     */
    @Test
    public void testCreateAccount() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(createRestTestURI(DEPLOYMENT_URL, "create", "Sam"));

        Form form = new Form().param("amount", "100.00");
        Response response = target.request().post(Entity.form(form));

        Assert.assertEquals(200, response.getStatus());
        String data = response.readEntity(String.class);
        Assert.assertTrue(data.contains("$100.00"));
    }

    @Test
    public void testDuplicateAccountCreateFails() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(createRestTestURI(DEPLOYMENT_URL, "create", "Jet"));

        Form form = new Form().param("amount", "100.00");
        Response response = target.request().post(Entity.form(form));

        Assert.assertEquals(200, response.getStatus());
        response = target.request().post(Entity.form(form));
        Assert.assertEquals(409, response.getStatus());

    }

    /**
     * Test of makeDepositForAccount method, of class AccountService.
     */
    @Test
    public void testMakeDepositForAccount() throws Exception {
        Client client = null;
        WebTarget target = null;
        Form form = null;
        Response response = null;

        client = ClientBuilder.newClient();
        target = client.target(createRestTestURI(DEPLOYMENT_URL, "create", "Jack"));
        form = new Form().param("amount", "0.00");
        response = target.request().post(Entity.form(form));
        Assert.assertEquals(200, response.getStatus());

        target = client.target(createRestTestURI(DEPLOYMENT_URL, "deposit", "Jack"));

        int deposits = 5;
        Float deposit = 100F;

        for (int i = 0; i < deposits; i++) {
            form = new Form().param("amount", String.valueOf(deposit));
            response = target.request().post(Entity.form(form));
            Assert.assertEquals(200, response.getStatus());
        }

        target = client.target(createRestTestURI(DEPLOYMENT_URL, "amount", "Jack"));
        response = target.request().get();
        Assert.assertEquals(200, response.getStatus());
        String total = response.readEntity(String.class);
        String expect = NumberFormat.getCurrencyInstance().format(deposits * deposit);
        Assert.assertTrue(expect.equals(total));

    }

    /**
     * Test of makeWithdrawalForAccount method, of class AccountService.
     */
    @Test
    public void testMakeWithdrawalForAccount() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(createRestTestURI(DEPLOYMENT_URL, "create", "Patto"));

        Form form = new Form().param("amount", "100.00");
        Response response = target.request().post(Entity.form(form));
        Assert.assertEquals(200, response.getStatus());
        
        target = client.target(createRestTestURI(DEPLOYMENT_URL, "withdraw", "Patto"));
        form = new Form().param("amount", "50.00");
        response = target.request().post(Entity.form(form));
        Assert.assertEquals(200, response.getStatus());
        
        target = client.target(createRestTestURI(DEPLOYMENT_URL, "amount", "Patto"));
        response = target.request().get();
        Assert.assertEquals(200, response.getStatus());
        String total = response.readEntity(String.class);
        Assert.assertEquals("$50.00", total);
        

        target = client.target(createRestTestURI(DEPLOYMENT_URL, "withdraw", "Patto"));
        form = new Form().param("amount", "50.00");
        response = target.request().post(Entity.form(form));
        Assert.assertEquals(200, response.getStatus());
        
        
        target = client.target(createRestTestURI(DEPLOYMENT_URL, "amount", "Patto"));
        response = target.request().get();
        Assert.assertEquals(200, response.getStatus());
        total = response.readEntity(String.class);
        Assert.assertEquals("$0.00", total);
    }

    // There is a much better way to this using the pattern/template model
    // todo: get that to work
    private URI createRestTestURI(URL baseURL, String action, String param) throws MalformedURLException, URISyntaxException {
        return createRestTestURL(baseURL, action, param).toURI();
    }
    
    private URL createRestTestURL(URL baseURL, String action, String param) throws MalformedURLException {
        String restapp = "rest/account/";
        StringBuilder resturl = new StringBuilder(baseURL.toExternalForm());
        resturl.append(restapp);

        switch (action) {
            case "create":
                resturl.append("create/")
                        .append(param);
                break;
            case "deposit":
                resturl.append("deposit/")
                        .append(param);
                break;
            case "withdraw":
                resturl.append("withdraw/")
                        .append(param);
                break;
            default:
                resturl.append(param);
        }
        
        if(DEBUG) {
            System.out.println(" --> " + resturl.toString());
        }
        
        return new URL(resturl.toString());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buttso.demo.weblogic.rest;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.Singleton;
import javax.ws.rs.*;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.*;

/**
 *
 * @author sbutton
 */
@Singleton
@Path("/account")
public class AccountService {

    private Map<String, AccountBean> accounts = new ConcurrentHashMap<>(5);

    /**
     * Get amount for account
     */
    @GET
    @Path("/{name}")
    public Response getAmountForAccount(@PathParam("name") String name) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        if (name == null || "".equals(name)) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        AccountBean account = accounts.get(name);
        if (account == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(formatCurrency(account.getAmount())).build();
    }

    /**
     * Create a new account
     *
     * @param name - unique name
     * @return Response
     */
    @POST
    @Path("/create/{name}")
    //@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createAccount(
            @PathParam("name") String name,
            @FormParam("amount")Float amount) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        
        if(name == null || "".equals(name)) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if(amount == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (accounts.containsKey(name)) {
            return Response.status(Status.CONFLICT).build();
        }
        AccountBean account = new AccountBean(name, amount);
        accounts.put(name, account);
        return Response.ok(account.getName() + " " + formatCurrency(account.getAmount())).build();
    }

    /**
     * Make a deposit for a named account
     *
     * @param name
     * @param amount
     * @return
     */
    @POST
    @Path("/deposit/{name}")
    public Response makeDepositForAccount(
            @PathParam("name") String name,
            @FormParam("amount") Float amount) {
        
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());

        if (name == null || "".equals(name)) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (amount == null || amount == 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        AccountBean account = accounts.get(name);
        if (account == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        account.deposit(amount);
        return Response.ok(formatCurrency(account.getAmount())).build();
    }
    
    /**
     * Make a withdrawal for a named account
     *
     * @param name
     * @param amount
     * @return
     */
    @POST
    @Path("/withdraw/{name}")
    public Response makeWithdrawalForAccount(
            @PathParam("name") String name,
            @FormParam("amount") Float amount) {
        
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());

        if (name == null || "".equals(name)) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (amount == null || amount == 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        AccountBean account = accounts.get(name);
        if (account == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        account.withdrawal(amount);
        return Response.ok(formatCurrency(account.getAmount())).build();
    }
    

    private String formatCurrency(Float amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }
}

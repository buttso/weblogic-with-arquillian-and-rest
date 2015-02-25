/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buttso.demo.weblogic.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sbutton
 */
@XmlRootElement(name = "account")
public class AccountBean {

    @XmlElement
    private String name;
    @XmlElement
    private Float amount = 0.0F;

    public AccountBean() {
        super();
    }

    public AccountBean(String name) {
        super();
        this.name = name;
    }

    protected AccountBean(String name, Float amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public Float getAmount() {
        return amount;
    }

    public void deposit(Float deposit) {
        this.amount += deposit;
    }

    public void withdrawal(Float withdrawal) {
        this.amount -= withdrawal;
    }

}

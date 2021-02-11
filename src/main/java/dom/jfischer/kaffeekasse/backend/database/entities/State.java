/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.database.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * State class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
@Entity
@Table(name = "STATE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "State.findAll", query = "SELECT s FROM State s"),
    @NamedQuery(name = "State.findById", query = "SELECT s FROM State s WHERE s.id = :id"),
    @NamedQuery(name = "State.findByBankDeposit", query = "SELECT s FROM State s WHERE s.bankDeposit = :bankDeposit")})
public class State implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "bankDeposit")
    private Integer bankDeposit;
    @JoinColumn(name = "accountPeriod_id", referencedColumnName = "id")
    @ManyToOne
    private AccountPeriod accountPeriodid;

    /**
     * <p>
     * Constructor for State.</p>
     */
    public State() {
    }

    /**
     * <p>
     * Constructor for State.</p>
     *
     * @param id a {@link java.lang.Integer} object.
     */
    public State(Integer id) {
        this.id = id;
    }

    /**
     * <p>
     * Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getId() {
        return id;
    }

    /**
     * <p>
     * Setter for the field <code>id</code>.</p>
     *
     * @param id a {@link java.lang.Integer} object.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * <p>
     * Getter for the field <code>bankDeposit</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getBankDeposit() {
        return bankDeposit;
    }

    /**
     * <p>
     * Setter for the field <code>bankDeposit</code>.</p>
     *
     * @param bankDeposit a {@link java.lang.Integer} object.
     */
    public void setBankDeposit(Integer bankDeposit) {
        this.bankDeposit = bankDeposit;
    }

    /**
     * <p>
     * Getter for the field <code>accountPeriodid</code>.</p>
     *
     * @return a
     * {@link dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod}
     * object.
     */
    public AccountPeriod getAccountPeriodid() {
        return accountPeriodid;
    }

    /**
     * <p>
     * Setter for the field <code>accountPeriodid</code>.</p>
     *
     * @param accountPeriodid a
     * {@link dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod}
     * object.
     */
    public void setAccountPeriodid(AccountPeriod accountPeriodid) {
        this.accountPeriodid = accountPeriodid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof State)) {
            return false;
        }
        State other = (State) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "dom.jfischer.kaffeekasse.backend.database.entities.State[ id=" + id + " ]";
    }

}

/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.database.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <p>
 * AccountPeriod class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
@Entity
@Table(name = "ACCOUNT_PERIOD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountPeriod.findAll", query = "SELECT a FROM AccountPeriod a"),
    @NamedQuery(name = "AccountPeriod.findById", query = "SELECT a FROM AccountPeriod a WHERE a.id = :id"),
    @NamedQuery(name = "AccountPeriod.findByTimestamp", query = "SELECT a FROM AccountPeriod a WHERE a.timestamp = :timestamp"),
    @NamedQuery(name = "AccountPeriod.findByPrice", query = "SELECT a FROM AccountPeriod a WHERE a.price = :price")})
public class AccountPeriod implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Basic(optional = false)
    @Column(name = "price")
    private int price;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountPeriodid")
    private List<AccountEntry> accountEntryList;
    @OneToMany(mappedBy = "accountPeriodid")
    private List<State> stateList;

    /**
     * <p>
     * Constructor for AccountPeriod.</p>
     */
    public AccountPeriod() {
    }

    /**
     * <p>
     * Constructor for AccountPeriod.</p>
     *
     * @param id a {@link java.lang.Integer} object.
     */
    public AccountPeriod(Integer id) {
        this.id = id;
    }

    /**
     * <p>
     * Constructor for AccountPeriod.</p>
     *
     * @param id a {@link java.lang.Integer} object.
     * @param timestamp a {@link java.util.Date} object.
     * @param price a int.
     */
    public AccountPeriod(Integer id, Date timestamp, int price) {
        this.id = id;
        this.timestamp = timestamp;
        this.price = price;
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
     * Getter for the field <code>timestamp</code>.</p>
     *
     * @return a {@link java.util.Date} object.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * <p>
     * Setter for the field <code>timestamp</code>.</p>
     *
     * @param timestamp a {@link java.util.Date} object.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * <p>
     * Getter for the field <code>price</code>.</p>
     *
     * @return a int.
     */
    public int getPrice() {
        return price;
    }

    /**
     * <p>
     * Setter for the field <code>price</code>.</p>
     *
     * @param price a int.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * <p>
     * Getter for the field <code>accountEntryList</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    @XmlTransient
    public List<AccountEntry> getAccountEntryList() {
        return accountEntryList;
    }

    /**
     * <p>
     * Setter for the field <code>accountEntryList</code>.</p>
     *
     * @param accountEntryList a {@link java.util.List} object.
     */
    public void setAccountEntryList(List<AccountEntry> accountEntryList) {
        this.accountEntryList = accountEntryList;
    }

    /**
     * <p>
     * Getter for the field <code>stateList</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    @XmlTransient
    public List<State> getStateList() {
        return stateList;
    }

    /**
     * <p>
     * Setter for the field <code>stateList</code>.</p>
     *
     * @param stateList a {@link java.util.List} object.
     */
    public void setStateList(List<State> stateList) {
        this.stateList = stateList;
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
        if (!(object instanceof AccountPeriod)) {
            return false;
        }
        AccountPeriod other = (AccountPeriod) object;
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
        return "dom.jfischer.kaffeekasse.backend.database.entities.AccountPeriod[ id=" + id + " ]";
    }

}

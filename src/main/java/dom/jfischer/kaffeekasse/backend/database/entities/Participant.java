/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.database.entities;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <p>
 * Participant class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
@Entity
@Table(name = "PARTICIPANT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Participant.findAll", query = "SELECT p FROM Participant p"),
    @NamedQuery(name = "Participant.findById", query = "SELECT p FROM Participant p WHERE p.id = :id"),
    @NamedQuery(name = "Participant.findByName", query = "SELECT p FROM Participant p WHERE p.name = :name"),
    @NamedQuery(name = "Participant.findByDeposit", query = "SELECT p FROM Participant p WHERE p.deposit = :deposit"),
    @NamedQuery(name = "Participant.findByNrCups", query = "SELECT p FROM Participant p WHERE p.nrCups = :nrCups"),
    @NamedQuery(name = "Participant.findByIsActive", query = "SELECT p FROM Participant p WHERE p.isActive = :isActive")})
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "deposit")
    private int deposit;
    @Basic(optional = false)
    @Column(name = "nrCups")
    private int nrCups;
    @Basic(optional = false)
    @Column(name = "isActive")
    private boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participantId")
    private List<AccountEntry> accountEntryList;

    /**
     * <p>
     * Constructor for Participant.</p>
     */
    public Participant() {
    }

    /**
     * <p>
     * Constructor for Participant.</p>
     *
     * @param id a {@link java.lang.Integer} object.
     */
    public Participant(Integer id) {
        this.id = id;
    }

    /**
     * <p>
     * Constructor for Participant.</p>
     *
     * @param id a {@link java.lang.Integer} object.
     * @param name a {@link java.lang.String} object.
     * @param deposit a int.
     * @param nrCups a int.
     * @param isActive a boolean.
     */
    public Participant(Integer id, String name, int deposit, int nrCups, boolean isActive) {
        this.id = id;
        this.name = name;
        this.deposit = deposit;
        this.nrCups = nrCups;
        this.isActive = isActive;
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
     * Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>
     * Getter for the field <code>deposit</code>.</p>
     *
     * @return a int.
     */
    public int getDeposit() {
        return deposit;
    }

    /**
     * <p>
     * Setter for the field <code>deposit</code>.</p>
     *
     * @param deposit a int.
     */
    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    /**
     * <p>
     * Getter for the field <code>nrCups</code>.</p>
     *
     * @return a int.
     */
    public int getNrCups() {
        return nrCups;
    }

    /**
     * <p>
     * Setter for the field <code>nrCups</code>.</p>
     *
     * @param nrCups a int.
     */
    public void setNrCups(int nrCups) {
        this.nrCups = nrCups;
    }

    /**
     * <p>
     * Getter for the field <code>isActive</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * <p>
     * Setter for the field <code>isActive</code>.</p>
     *
     * @param isActive a boolean.
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
        if (!(object instanceof Participant)) {
            return false;
        }
        Participant other = (Participant) object;
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
        return "dom.jfischer.kaffeekasse.backend.database.entities.Participant[ id=" + id + " ]";
    }

}

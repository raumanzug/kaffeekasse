/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.backend.database.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * AccountEntry class.</p>
 *
 * @author jfischer
 * @version $Id: $Id
 */
@Entity
@Table(name = "ACCOUNT_ENTRY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountEntry.findAll", query = "SELECT a FROM AccountEntry a"),
    @NamedQuery(name = "AccountEntry.findById", query = "SELECT a FROM AccountEntry a WHERE a.id = :id"),
    @NamedQuery(name = "AccountEntry.findByTimestamp", query = "SELECT a FROM AccountEntry a WHERE a.timestamp = :timestamp"),
    @NamedQuery(name = "AccountEntry.findByAmount", query = "SELECT a FROM AccountEntry a WHERE a.amount = :amount"),
    @NamedQuery(name = "AccountEntry.findByNrCups", query = "SELECT a FROM AccountEntry a WHERE a.nrCups = :nrCups"),
    @NamedQuery(name = "AccountEntry.findByPostingText", query = "SELECT a FROM AccountEntry a WHERE a.postingText = :postingText")})
public class AccountEntry implements Serializable {

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
    @Column(name = "amount")
    private int amount;
    @Basic(optional = false)
    @Column(name = "nrCups")
    private int nrCups;
    @Basic(optional = false)
    @Column(name = "postingText")
    private String postingText;
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Participant participantId;
    @JoinColumn(name = "accountPeriod_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccountPeriod accountPeriodid;

    /**
     * <p>
     * Constructor for AccountEntry.</p>
     */
    public AccountEntry() {
    }

    /**
     * <p>
     * Constructor for AccountEntry.</p>
     *
     * @param id a {@link java.lang.Integer} object.
     */
    public AccountEntry(Integer id) {
        this.id = id;
    }

    /**
     * <p>
     * Constructor for AccountEntry.</p>
     *
     * @param id a {@link java.lang.Integer} object.
     * @param timestamp a {@link java.util.Date} object.
     * @param amount a int.
     * @param nrCups a int.
     * @param postingText a {@link java.lang.String} object.
     */
    public AccountEntry(Integer id, Date timestamp, int amount, int nrCups, String postingText) {
        this.id = id;
        this.timestamp = timestamp;
        this.amount = amount;
        this.nrCups = nrCups;
        this.postingText = postingText;
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
     * Getter for the field <code>amount</code>.</p>
     *
     * @return a int.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * <p>
     * Setter for the field <code>amount</code>.</p>
     *
     * @param amount a int.
     */
    public void setAmount(int amount) {
        this.amount = amount;
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
     * Getter for the field <code>postingText</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPostingText() {
        return postingText;
    }

    /**
     * <p>
     * Setter for the field <code>postingText</code>.</p>
     *
     * @param postingText a {@link java.lang.String} object.
     */
    public void setPostingText(String postingText) {
        this.postingText = postingText;
    }

    /**
     * <p>
     * Getter for the field <code>participantId</code>.</p>
     *
     * @return a
     * {@link dom.jfischer.kaffeekasse.backend.database.entities.Participant}
     * object.
     */
    public Participant getParticipantId() {
        return participantId;
    }

    /**
     * <p>
     * Setter for the field <code>participantId</code>.</p>
     *
     * @param participantId a
     * {@link dom.jfischer.kaffeekasse.backend.database.entities.Participant}
     * object.
     */
    public void setParticipantId(Participant participantId) {
        this.participantId = participantId;
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
        if (!(object instanceof AccountEntry)) {
            return false;
        }
        AccountEntry other = (AccountEntry) object;
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
        return "dom.jfischer.kaffeekasse.backend.database.entities.AccountEntry[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraker
 */
@Entity
@Table(name = "Phrase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Phrase.findAll", query = "SELECT p FROM Phrase p")
    , @NamedQuery(name = "Phrase.findById", query = "SELECT p FROM Phrase p WHERE p.id = :id")
    , @NamedQuery(name = "Phrase.findByCreationDate", query = "SELECT p FROM Phrase p WHERE p.creationDate = :creationDate")
    , @NamedQuery(name = "Phrase.findByUserName", query = "SELECT p FROM Phrase p WHERE p.userName = :userName")})
public class Phrase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 45)
    @Column(name = "CreationDate")
    private String creationDate;
    @Size(max = 45)
    @Column(name = "UserName")
    private String userName;
    @JoinTable(name = "Phrase_has_Recurse", joinColumns = {
        @JoinColumn(name = "Phrase_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "Recurse_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Recurse> recurseList;
    @ManyToMany(mappedBy = "phraseList")
    private List<Groups> groupsList;
    @JoinColumn(name = "Account_Subject", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Account accountSubject;
    @JoinColumn(name = "Account_Receiver", referencedColumnName = "ID")
    @ManyToOne
    private Account accountReceiver;
    @JoinColumn(name = "Action_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Action actionID;
    @JoinColumn(name = "Artefact_ID", referencedColumnName = "ID")
    @ManyToOne
    private Artefact artefactID;

    public Phrase() {
    }

    public Phrase(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @XmlTransient
    public List<Recurse> getRecurseList() {
        return recurseList;
    }

    public void setRecurseList(List<Recurse> recurseList) {
        this.recurseList = recurseList;
    }

    @XmlTransient
    public List<Groups> getGroupsList() {
        return groupsList;
    }

    public void setGroupsList(List<Groups> groupsList) {
        this.groupsList = groupsList;
    }

    public Account getAccountSubject() {
        return accountSubject;
    }

    public void setAccountSubject(Account accountSubject) {
        this.accountSubject = accountSubject;
    }

    public Account getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(Account accountReceiver) {
        this.accountReceiver = accountReceiver;
    }

    public Action getActionID() {
        return actionID;
    }

    public void setActionID(Action actionID) {
        this.actionID = actionID;
    }

    public Artefact getArtefactID() {
        return artefactID;
    }

    public void setArtefactID(Artefact artefactID) {
        this.artefactID = artefactID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Phrase)) {
            return false;
        }
        Phrase other = (Phrase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String result = accountSubject + " " + actionID.getName();
        if(accountReceiver != null)
            result+= " " + accountReceiver;
        if(artefactID != null)
            result+= " " + artefactID.getName();
        return result;
    }
    
}

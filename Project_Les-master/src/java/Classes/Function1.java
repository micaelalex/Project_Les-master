/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraker
 */
@Entity
@Table(name = "Function")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Function1.findAll", query = "SELECT f FROM Function1 f")
    , @NamedQuery(name = "Function1.findById", query = "SELECT f FROM Function1 f WHERE f.id = :id")
    , @NamedQuery(name = "Function1.findByName", query = "SELECT f FROM Function1 f WHERE f.name = :name")
    , @NamedQuery(name = "Function1.findByDescription", query = "SELECT f FROM Function1 f WHERE f.description = :description")
    , @NamedQuery(name = "Function1.findByCreationDate", query = "SELECT f FROM Function1 f WHERE f.creationDate = :creationDate")})
public class Function1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 45)
    @Column(name = "Name")
    private String name;
    @Size(max = 512)
    @Column(name = "Description")
    private String description;
    @Column(name = "CreationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @ManyToMany(mappedBy = "function1List")
    private List<Activity> activityList;
    @JoinColumn(name = "Account_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Account accountID;

    public Function1() {
    }

    public Function1(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @XmlTransient
    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
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
        if (!(object instanceof Function1)) {
            return false;
        }
        Function1 other = (Function1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + "";
    }
    
}

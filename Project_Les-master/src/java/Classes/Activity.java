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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "Activity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activity.findAll", query = "SELECT a FROM Activity a")
    , @NamedQuery(name = "Activity.findById", query = "SELECT a FROM Activity a WHERE a.id = :id")
    , @NamedQuery(name = "Activity.findByName", query = "SELECT a FROM Activity a WHERE a.name = :name")
    , @NamedQuery(name = "Activity.findByDescription", query = "SELECT a FROM Activity a WHERE a.description = :description")
    , @NamedQuery(name = "Activity.findByCreationDate", query = "SELECT a FROM Activity a WHERE a.creationDate = :creationDate")})
public class Activity implements Serializable {

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
    @JoinTable(name = "Activity_has_Pattern", joinColumns = {
        @JoinColumn(name = "Activity_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "Pattern_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Pattern> patternList;
    @JoinTable(name = "Activity_has_Function", joinColumns = {
        @JoinColumn(name = "Activity_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "Function_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Function1> function1List;
    @JoinTable(name = "Activity_has_Product", joinColumns = {
        @JoinColumn(name = "Activity_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "Product_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Product> productList;
    @JoinColumn(name = "Account_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Account accountID;
    @OneToMany(mappedBy = "activityID")
    private List<Activity> activityList;
    @JoinColumn(name = "Activity_ID", referencedColumnName = "ID")
    @ManyToOne
    private Activity activityID;
    @JoinColumn(name = "Process_ID", referencedColumnName = "ID")
    @ManyToOne
    private Process processID;

    public Activity() {
    }

    public Activity(Integer id) {
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
    public List<Pattern> getPatternList() {
        return patternList;
    }

    public void setPatternList(List<Pattern> patternList) {
        this.patternList = patternList;
    }

    @XmlTransient
    public List<Function1> getFunction1List() {
        return function1List;
    }

    public void setFunction1List(List<Function1> function1List) {
        this.function1List = function1List;
    }

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
    }

    @XmlTransient
    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public Activity getActivityID() {
        return activityID;
    }

    public void setActivityID(Activity activityID) {
        this.activityID = activityID;
    }

    public Process getProcessID() {
        return processID;
    }

    public void setProcessID(Process processID) {
        this.processID = processID;
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
        if (!(object instanceof Activity)) {
            return false;
        }
        Activity other = (Activity) object;
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

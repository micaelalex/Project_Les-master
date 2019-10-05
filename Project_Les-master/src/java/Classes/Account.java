/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author diogo
 */
@Entity
@Table(name = "Account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
    , @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id")
    , @NamedQuery(name = "Account.findByUserName", query = "SELECT a FROM Account a WHERE a.userName = :userName")
    , @NamedQuery(name = "Account.findByCc", query = "SELECT a FROM Account a WHERE a.cc = :cc")
    , @NamedQuery(name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password")
    , @NamedQuery(name = "Account.findByAccountName", query = "SELECT a FROM Account a WHERE a.accountName = :accountName")
    , @NamedQuery(name = "Account.findByBirthday", query = "SELECT a FROM Account a WHERE a.birthday = :birthday")
    , @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email")
    , @NamedQuery(name = "Account.findByAddress", query = "SELECT a FROM Account a WHERE a.address = :address")
    , @NamedQuery(name = "Account.findBySalary", query = "SELECT a FROM Account a WHERE a.salary = :salary")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UserName")
    private String userName;
    @Size(max = 45)
    @Column(name = "CC")
    private String cc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "Password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "AccountName")
    private String accountName;
    @Column(name = "Birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Email")
    private String email;
    @Size(max = 256)
    @Column(name = "Address")
    private String address;
    @Column(name = "Salary")
    private Integer salary;
    @JoinTable(name = "Organization_has_Account", joinColumns = {
        @JoinColumn(name = "Account_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "Organization_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<Organization> organizationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private Collection<Pattern> patternCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private Collection<Action> actionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private Collection<Activity> activityCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private Collection<Product> productCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private Collection<Process> processCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountSubject")
    private Collection<Phrase> phraseCollection;
    @OneToMany(mappedBy = "accountReceiver")
    private Collection<Phrase> phraseCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private Collection<Function1> function1Collection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private Collection<Groups> groupsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private Collection<UserType> userTypeCollection;

    public Account() {
    }

    public Account(Integer id) {
        this.id = id;
    }

    public Account(Integer id, String userName, String password, String accountName, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.accountName = accountName;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @XmlTransient
    public Collection<Organization> getOrganizationCollection() {
        return organizationCollection;
    }

    public void setOrganizationCollection(Collection<Organization> organizationCollection) {
        this.organizationCollection = organizationCollection;
    }

    @XmlTransient
    public Collection<Pattern> getPatternCollection() {
        return patternCollection;
    }

    public void setPatternCollection(Collection<Pattern> patternCollection) {
        this.patternCollection = patternCollection;
    }

    @XmlTransient
    public Collection<Action> getActionCollection() {
        return actionCollection;
    }

    public void setActionCollection(Collection<Action> actionCollection) {
        this.actionCollection = actionCollection;
    }

    @XmlTransient
    public Collection<Activity> getActivityCollection() {
        return activityCollection;
    }

    public void setActivityCollection(Collection<Activity> activityCollection) {
        this.activityCollection = activityCollection;
    }

    @XmlTransient
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }

    @XmlTransient
    public Collection<Process> getProcessCollection() {
        return processCollection;
    }

    public void setProcessCollection(Collection<Process> processCollection) {
        this.processCollection = processCollection;
    }

    @XmlTransient
    public Collection<Phrase> getPhraseCollection() {
        return phraseCollection;
    }

    public void setPhraseCollection(Collection<Phrase> phraseCollection) {
        this.phraseCollection = phraseCollection;
    }

    @XmlTransient
    public Collection<Phrase> getPhraseCollection1() {
        return phraseCollection1;
    }

    public void setPhraseCollection1(Collection<Phrase> phraseCollection1) {
        this.phraseCollection1 = phraseCollection1;
    }

    @XmlTransient
    public Collection<Function1> getFunction1Collection() {
        return function1Collection;
    }

    public void setFunction1Collection(Collection<Function1> function1Collection) {
        this.function1Collection = function1Collection;
    }

    @XmlTransient
    public Collection<Groups> getGroupsCollection() {
        return groupsCollection;
    }

    public void setGroupsCollection(Collection<Groups> groupsCollection) {
        this.groupsCollection = groupsCollection;
    }

    @XmlTransient
    public Collection<UserType> getUserTypeCollection() {
        return userTypeCollection;
    }

    public void setUserTypeCollection(Collection<UserType> userTypeCollection) {
        this.userTypeCollection = userTypeCollection;
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
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id= " + id + ", username= " +userName;
    }
    
}

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraker
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
public class Account_old implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 45)
    @Column(name = "UserName")
    private String userName;
    @Size(max = 45)
    @Column(name = "CC")
    private String cc;
    @Size(max = 128)
    @Column(name = "Password")
    private String password;
    @Size(max = 45)
    @Column(name = "AccountName")
    private String accountName;
    @Column(name = "Birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 45)
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
    private List<Organization> organizationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private List<Pattern> patternList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private List<Action> actionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private List<Activity> activityList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private List<Product> productList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private List<Process> processList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountSubject")
    private List<Phrase> phraseList;
    @OneToMany(mappedBy = "accountReceiver")
    private List<Phrase> phraseList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private List<Function1> function1List;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private List<Groups> groupsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountID")
    private List<UserType> userTypeList;

    public Account_old() {
    }

    public Account_old(Integer id) {
        this.id = id;
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
    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    @XmlTransient
    public List<Pattern> getPatternList() {
        return patternList;
    }

    public void setPatternList(List<Pattern> patternList) {
        this.patternList = patternList;
    }

    @XmlTransient
    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    @XmlTransient
    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @XmlTransient
    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    @XmlTransient
    public List<Phrase> getPhraseList() {
        return phraseList;
    }

    public void setPhraseList(List<Phrase> phraseList) {
        this.phraseList = phraseList;
    }

    @XmlTransient
    public List<Phrase> getPhraseList1() {
        return phraseList1;
    }

    public void setPhraseList1(List<Phrase> phraseList1) {
        this.phraseList1 = phraseList1;
    }

    @XmlTransient
    public List<Function1> getFunction1List() {
        return function1List;
    }

    public void setFunction1List(List<Function1> function1List) {
        this.function1List = function1List;
    }

    @XmlTransient
    public List<Groups> getGroupsList() {
        return groupsList;
    }

    public void setGroupsList(List<Groups> groupsList) {
        this.groupsList = groupsList;
    }

    @XmlTransient
    public List<UserType> getUserTypeList() {
        return userTypeList;
    }

    public void setUserTypeList(List<UserType> userTypeList) {
        this.userTypeList = userTypeList;
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
        if (!(object instanceof Account_old)) {
            return false;
        }
        Account_old other = (Account_old) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return userName + "";
    }
    
}

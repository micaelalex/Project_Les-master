package Controlers;

import Classes.Account;
import Controlers.util.JsfUtil;
import Controlers.util.PaginationHelper;
import Models.AccountFacade;

import java.util.*;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("accountController")
@SessionScoped
public class AccountController implements Serializable {
    
    private List<Account> selectedList = new ArrayList<>();
    private Account current;
    private DataModel items = null;
    @EJB
    private Models.AccountFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    public AccountController() {
    }
    
    public String editDisable(){
        if(selectedList.size()==1)
            return "false";
        else
            return "true";
    }
    
        public String deleteDisable(){
        if(selectedList.size()>=1)
            return "false";
        else
            return "true";
    }
    
    public Account getSelected() {
        if (current == null) {
            current = new Account();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public int selectedSize(){
        return selectedList.size();
    }
    
    public boolean selectedContains(Account a){
        if(selectedList.isEmpty()) return false;
        for(Account acc: selectedList){
            if(acc.equals(a)){
                return true;
            }
            
        }
        return false;
    }
    
    public void select(){
        current = (Account) getItems().getRowData();
        System.out.println(current.getId());
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        selectedList.add(current);
    }
    
    public String destroyAll(){
        List<Integer> ids_deleted = new ArrayList<>();
        List<Integer> ids_not = new ArrayList<>();
        
        for(Account acc: selectedList){
            int id = acc.getId();
            try {
                getFacade().remove(acc);
                ids_deleted.add(id);
                
            } catch (Exception e) {            
                ids_not.add(id);
            }

        }
        
        String notDel ="";
        for(Integer i: ids_not){
            notDel+= i +"; ";
        }
        if(ids_not.size()>0)
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("AccountDeletedError") +" "+notDel);
        
        String del ="";
        for(Integer i: ids_deleted){
            del+= i +"; ";
        }
        if(ids_deleted.size()>0)
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AccountDeletedConfirm")+" "+ del);
        
        selectedList.clear();
        recreatePagination();
        recreateModel();
        current= null;
        return "List";
    }
    
    public void unselect(){
        Account toRemove = null;
        Account value = (Account) getItems().getRowData();
        for(Account acc: selectedList){
            if(acc.equals(value)){
                toRemove = acc;
                break;
            }
            
        }
        
        if(toRemove != null)
            selectedList.remove(toRemove);
        
        if(selectedList.isEmpty()){
            current = new Account();
            selectedItemIndex =-1;
        }
        else
            this.updateCurrentItem();
        
    }
    
    private AccountFacade getFacade() {
        return ejbFacade;
    }
    
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(100000) {
                
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }
                
                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public String prepareView() {
        current = (Account) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public void prepareCreate() {
        current = new Account();
        selectedItemIndex = -1;
        //return "Create";
    }
    

    
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AccountCreated"));
            prepareCreate();
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public void validateByEmail(FacesContext context,
            UIComponent toValidate,
            Object value) {
        
        String email = (String) value;
        if (!getFacade().findByEmail(email).isEmpty()) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("CreateAccountAlreadyExistEmail"));
            context.addMessage(toValidate.getClientId(context), message);
        } else
            ((UIInput) toValidate).setValid(true);
    }
    
    public String prepareEdit() {
        current = (Account) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AccountUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String destroy() {
        current = (Account) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }
    
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AccountDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }
    
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }
    
    private void recreateModel() {
        items = null;
    }
    
    private void recreatePagination() {
        pagination = null;
    }
    
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }
    
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }
    
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    
    public Account getAccount(java.lang.Integer id) {
        return ejbFacade.find(id);
    }
    
    @FacesConverter(forClass = Account.class)
    public static class AccountControllerConverter implements Converter {
        
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AccountController controller = (AccountController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "accountController");
            return controller.getAccount(getKey(value));
        }
        
        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }
        
        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Account) {
                Account o = (Account) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Account.class.getName());
            }
        }
        
    }
    
}

package Controlers;

import Classes.Phrase;
import Controlers.util.JsfUtil;
import Controlers.util.PaginationHelper;
import Models.PhraseFacade;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("phraseController")
@SessionScoped
public class PhraseController implements Serializable {

    private List<Phrase> selectedList = new ArrayList<>();
    private Phrase current;
    private Phrase toCreate;
    private DataModel items = null;
    @EJB
    private Models.PhraseFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex = -1;
    
    public PhraseController(){
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
    
    public Phrase getCurrent(){
        return current;
    }
    
    public int selectedSize(){
        return selectedList.size();
    }
    
    public boolean selectedContains(Phrase p){
        if(selectedList.isEmpty()) return false;
        for(Phrase phr: selectedList){
            if(phr.equals(p)){
                return true;
            }
                
        }
        return false;
    }
    
    public void select(){
        try{
            current = (Phrase) getItems().getRowData();
            System.out.println(current);
            selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
            selectedList.add(current);
        }
        catch(Exception e){
            return;
        }
        //return "nextpage?faces-redirect=true";
    }
    
    public void desselect(){
        try {
            Phrase toRemove = null;

            System.out.println("unselect");
            Phrase value = (Phrase) getItems().getRowData();

            for(Phrase phr: selectedList){
                if(phr.equals(value)){
                    toRemove = phr;
                    break;
                }
            }

            if(toRemove != null)
                selectedList.remove(toRemove);

            if(selectedList.isEmpty())
                current = new Phrase();
            else if(current == toRemove)
                current = selectedList.get(0);
        }
        catch(Exception e){
            return;
        }
        ///return "nextpage?faces-redirect=true";
    }
    
    public void unselect(Phrase p){
        Phrase toRemove = null;
        
        //Phrase value = (Phrase) getItems().getRowData();
         
        for(Phrase phr: selectedList){
            if(phr.equals(p)){
                toRemove = phr;
                break;
            }
        }
        
        if(toRemove != null)
            selectedList.remove(toRemove);
        
        if(selectedList.isEmpty())
            current = new Phrase();
        else if(current == toRemove)
            current = selectedList.get(0);
    }
    
    public void unselectAll(){
        current = new Phrase();
        selectedList.clear();
    }
    
    public int getIndex(){
        return selectedItemIndex;
    }


    public Phrase getSelected() {
        if (current == null) {
            current = new Phrase();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public Phrase getToCreate(){
        if (toCreate == null) {
            toCreate = new Phrase();
        }
        return toCreate;
    }

    private PhraseFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(1000) {
            //pagination = new PaginationHelper(10) {

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
        current = (Phrase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        toCreate = new Phrase();
        selectedItemIndex = -1;
        return prepareList();
    }

    public String create() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        toCreate.setCreationDate(dateFormat.format(date));
        try {
            getFacade().create(toCreate);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PhraseCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Phrase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PhraseUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Phrase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public void destroyBetter(){
        boolean suc = false;
        for(Phrase phr: selectedList){
            
            try {
                
                getFacade().remove(phr);
                suc = true;
                
            } catch (Exception e) {
                
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                break;
            }
        }
        if(suc == true)
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PhraseDeleted"));
        
        selectedList.clear();
        recreatePagination();
        recreateModel();
        prepareList();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PhraseDeleted"));
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

    public Phrase getPhrase(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Phrase.class)
    public static class PhraseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PhraseController controller = (PhraseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "phraseController");
            return controller.getPhrase(getKey(value));
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
            if (object instanceof Phrase) {
                Phrase o = (Phrase) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Phrase.class.getName());
            }
        }

    }

}
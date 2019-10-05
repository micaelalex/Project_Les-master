package Controlers;

import Classes.Pattern;
import Controlers.util.JsfUtil;
import Controlers.util.PaginationHelper;
import Models.PatternFacade;

import java.io.Serializable;
import java.util.*;
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

@Named("patternController")
@SessionScoped
public class PatternController implements Serializable {

    private Pattern current;
    private DataModel items = null;
    @EJB
    private Models.PatternFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private ArrayList<Pattern> selectedPatterns = new ArrayList<Pattern>();

    public PatternController() {
    }
    
    public void selectPattern(){
        current = (Pattern) getItems().getRowData();
        System.out.println("Select " + current.getId());
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        selectedPatterns.add(current);
    }
    
    public void unselectPattern(){
        Pattern tmp = (Pattern) getItems().getRowData();
        System.out.println("Unselect " + tmp.getId());
        for(Pattern pt: selectedPatterns){
            if(pt.equals(tmp)){
                selectedPatterns.remove(pt);
                return;
            }
        }
    }

    public Pattern getSelected() {
        if (current == null) {
            current = new Pattern();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PatternFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(1000) {

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
        current = (Pattern) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Pattern();
        selectedItemIndex = -1;
        return prepareList();
    }

    public String create() {
        Date date = new Date();
        current.setCreationDate(date);
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PatternCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Pattern) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PatternUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Pattern) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String destroyAll(){
        int allSelectedPatterns = selectedPatterns.size();
        int increment = 0;
        for(Pattern pat: selectedPatterns){
            try {
                getFacade().remove(pat);
                increment++;
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured! The pattern with id " + pat.getId() + " could not be deleted!"));
            }
        }
        
        if(increment == allSelectedPatterns){
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PatternDeleted"));
        }
        selectedPatterns.clear();
        recreatePagination();
        recreateModel();
        current= null;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PatternDeleted"));
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
        System.out.print("Hey");
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Pattern getPattern(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Pattern.class)
    public static class PatternControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PatternController controller = (PatternController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "patternController");
            return controller.getPattern(getKey(value));
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
            if (object instanceof Pattern) {
                Pattern o = (Pattern) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Pattern.class.getName());
            }
        }

    }

}

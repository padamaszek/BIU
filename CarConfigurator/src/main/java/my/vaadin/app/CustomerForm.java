package my.vaadin.app;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.TextField;

public class CustomerForm extends CustomerFormDesign {

	CustomerService service = CustomerService.getInstance();
	private Customer customer;
	private MyUI myUI;

	public CustomerForm(MyUI myUI) {
		this.myUI = myUI;
		status.addItems(CustomerStatus.values());
		save.setClickShortcut(KeyCode.ENTER);
		save.addClickListener(e -> this.save());
		delete.addClickListener(e -> this.delete());
		
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		final BeanFieldGroup<Customer> binder = BeanFieldGroup.bindFieldsUnbuffered(customer, this);
		year.addValidator(new BeanValidator(Customer.class, "year"));
		
		
		/*final BeanFieldGroup<Customer> binder =
		        new BeanFieldGroup<Customer>(Customer.class);
		binder.setItemDataSource(customer);
		binder.buildAndBind("First", "name");
		binder.buildAndBind("Year", "year");
		binder.setBuffered(true);*/
		
		//BeanItem<Customer> item = new BeanItem<Customer> (customer);
		//year("Year",item.getItemProperty("year"));
		
		//binder.setBuffered(true);
       //		 year.addValidator(new BeanValidator(Customer.class, "year"));
        //		 year.setImmediate(true);
        	//	 addComponent(year);
       // x.setItemDataSource(customer);
       // x.buildAndBind("Year","year");
        
		//BeanFieldGroup.bindFieldsUnbuffered(customer, this.year).buildAndBind("Year", "year").setBuffered(true);
        		
		// Show delete button for only customers already in the database
		delete.setVisible(customer.isPersisted());
		setVisible(true);
		name.selectAll();
	}

	private void delete() {
		service.delete(customer);
		myUI.updateList();
		setVisible(false);
	}

	private void save() {
		service.save(customer);
		myUI.updateList();
		setVisible(true);
		}
	
}

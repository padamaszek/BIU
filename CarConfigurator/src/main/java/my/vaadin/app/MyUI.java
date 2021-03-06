package my.vaadin.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.google.gwt.dom.client.Style.Clear;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 */
@Theme("mytheme")
@Widgetset("my.vaadin.app.MyAppWidgetset")
public class MyUI extends UI {

	private CustomerService service = CustomerService.getInstance();
	private Grid grid = new Grid();
	private TextField filterText = new TextField();
	CustomerForm form = new CustomerForm(this);
	static Boolean isLogged = false;
	static ArrayList<Customer> car = new ArrayList<>();
	static Label label = new Label("WELCOME!");

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		filterText.setInputPrompt("filter by Car...");
		filterText.addTextChangeListener(e -> {
			grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.findAll(e.getText())));
		});
		filterText.setVisible(false);

		Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
		clearFilterTextBtn.setDescription("Clear the current filter");
		clearFilterTextBtn.addClickListener(e -> {
			filterText.clear();
			updateList();
		});
		clearFilterTextBtn.setVisible(false);

		CssLayout filtering = new CssLayout();
		filtering.addComponents(filterText, clearFilterTextBtn);
		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		Button addCustomerBtn = new Button("Add new product");
		addCustomerBtn.addClickListener(e -> {
			grid.select(null);
			form.setCustomer(new Customer());
		});
		addCustomerBtn.setVisible(false);

		Button buttonBack = new Button("Back");
		buttonBack.addClickListener(e -> {
			showBack();
		});

		Button startConfigure = new Button("Start Configuring Car");
		startConfigure.setDescription("Clear the current filter");
		startConfigure.addClickListener(e -> {
			label.setValue("Configure Car");
			isLogged = false;
			filterText.clear();
			grid.setVisible(true);
			filterText.setVisible(true);
			clearFilterTextBtn.setVisible(false);
			buttonBack.setVisible(true);
			startConfigure.setVisible(false);
			form.setVisible(false);
			updateList();
		});
		// ------------------------------------------
		Button order = new Button("Orders");
		order.addClickListener(e -> {
			label.setValue("ORDERS");
			showOrders();
		});

		Button signIn = new Button("Sign In");
		signIn.addClickListener(e -> {
			if (isLogged) {
				
				grid.setVisible(true);
				form.setVisible(true);
				updateList();
				
			} else {
				
			
			label.setValue("Sign In");
			signIn.setVisible(false);
			TextField login = new TextField();
			TextField pwd = new TextField();
			layout.addComponents(login, pwd);
			startConfigure.setVisible(false);
			Button confirmButton = new Button("Confirm");
			confirmButton.addClickListener(a -> {

				if (login.getValue().equals("admin") && pwd.getValue().equals("admin")) {
					label.setValue("Logged Sucessfully");
					grid.setVisible(true);
					form.setVisible(true);
					isLogged = true;
					login.setVisible(false);
					pwd.setVisible(false);
					confirmButton.setVisible(false);
					order.setVisible(true);
					buttonBack.setVisible(false);
					addCustomerBtn.setVisible(true);
					clearFilterTextBtn.setVisible(true);
					updateList();
				}
			
			});
			layout.addComponent(confirmButton);
			}
		});

		HorizontalLayout toolbar = new HorizontalLayout(filtering, addCustomerBtn, startConfigure, signIn, order,
				buttonBack);
		toolbar.setSpacing(true);
		order.setVisible(false);
		buttonBack.setVisible(false);
		addCustomerBtn.setVisible(false);
		clearFilterTextBtn.setVisible(false);

		grid.setColumns("name", "model", "status");

		HorizontalLayout main = new HorizontalLayout(grid, form);
		main.setSpacing(true);
		main.setSizeFull();
		grid.setSizeFull();
		main.setExpandRatio(grid, 1);

		layout.addComponents(label, toolbar, main);

		updateList();

		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);
		
			form.setVisible(false);
		
		grid.setVisible(false);

		grid.addSelectionListener(event -> {
			if (event.getSelected().isEmpty()) {
				form.setVisible(true);

			} else {

				Customer customer = (Customer) event.getSelected().iterator().next();
				form.setCustomer(customer);

				if (!isLogged) {
					grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.getCar(customer)));
				}
			}

		});

	}

	public void updateList() {
		// fetch list of Customers from service and assign it to Grid
		List<Customer> customers = service.findAll(filterText.getValue());
		if (!isLogged) {
			grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.findAllItems("Car")));
		} else {
			grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.findAllLogged()));
		}
		car.clear();
	}

	public void showOrders() {
		// fetch list of Customers from service and assign it to Grid
		List<Customer> customers = service.findAll(filterText.getValue());
		grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.getOrder()));
		car.clear();
	}

	public void showBack() {
		// fetch list of Customers from service and assign it to Grid
		List<Customer> customers = service.findAll(filterText.getValue());
		grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.getBack()));
		car.clear();
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}

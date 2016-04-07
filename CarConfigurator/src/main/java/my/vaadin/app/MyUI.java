package my.vaadin.app;

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


	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		Label label = new Label("asdf");
		
		filterText.setInputPrompt("filter by name...");
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

		Button addCustomerBtn = new Button("Add new customer");
		addCustomerBtn.addClickListener(e -> {
			grid.select(null);
			form.setCustomer(new Customer());
		});
		addCustomerBtn.setVisible(false);

		Button startConfigure = new Button("Start Configuring Car");
		startConfigure.setDescription("Clear the current filter");
		startConfigure.addClickListener(e -> {
			filterText.clear();
			grid.setVisible(true);
			filterText.setVisible(true);
			addCustomerBtn.setVisible(true);
			clearFilterTextBtn.setVisible(true);

			updateList();
		});
		
		HorizontalLayout toolbar = new HorizontalLayout(filtering, addCustomerBtn,startConfigure);
		toolbar.setSpacing(true);

		grid.setColumns("name", "model","type");

		HorizontalLayout main = new HorizontalLayout(grid, form);
		main.setSpacing(true);
		main.setSizeFull();
		grid.setSizeFull();
		main.setExpandRatio(grid, 1);

		layout.addComponents(label,toolbar, main);

		updateList();

		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);

		form.setVisible(false);

		//
		grid.setVisible(false);
		
		grid.addSelectionListener(event -> {
			if (event.getSelected().isEmpty()) {
				form.setVisible(false);
				
				
			} else {
				Customer customer = (Customer) event.getSelected().iterator().next();
				form.setCustomer(customer);
				grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.getModel(customer)));
			}
		});

	}

	public void updateList() {
		// fetch list of Customers from service and assign it to Grid
		List<Customer> customers = service.findAll(filterText.getValue());
		grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.findAllItems("Car")));
		CustomerService.car.clear();
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}

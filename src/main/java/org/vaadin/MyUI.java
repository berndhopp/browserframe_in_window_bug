package org.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.io.IOException;
import java.net.URL;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);

        Window window = new Window();

        StreamResource streamResource = new StreamResource((StreamResource.StreamSource) () -> {
            try {
                return new URL("https://upload.wikimedia.org/wikipedia/commons/e/e4/Chess.pdf").openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "test.pdf");

        streamResource.setMIMEType("application/pdf");

        BrowserFrame browserFrame = new BrowserFrame("test_pdf", streamResource);

        browserFrame.setSizeFull();

        window.setDraggable(true);
        window.setContent(browserFrame);

        window.center();

        window.setWidth("350px");
        window.setHeight("400px");

        Button button = new Button("toggle", (Button.ClickListener) clickEvent -> {
            if(!getWindows().contains(window)){
                addWindow(window);
            } else {
                removeWindow(window);
            }
        });

        layout.addComponent(button);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}

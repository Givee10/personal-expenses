package com.givee.application.views;

import com.givee.application.entity.UserInfo;
import com.givee.application.security.AuthenticatedUser;
import com.givee.application.views.checkform.CheckFormView;
import com.givee.application.views.exchangeform.ExchangeFormView;
import com.givee.application.views.exchanges.ExchangesView;
import com.givee.application.views.expenses.ExpensesView;
import com.givee.application.views.person.PersonView;
import com.givee.application.views.personform.PersonFormView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Personal Expenses");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(ExpensesView.class)) {
            nav.addItem(new SideNavItem("Expenses", ExpensesView.class, LineAwesomeIcon.FILTER_SOLID.create()));
        }
        if (accessChecker.hasAccess(PersonView.class)) {
            nav.addItem(new SideNavItem("Person View", PersonView.class, LineAwesomeIcon.USER_FRIENDS_SOLID.create()));
        }
        if (accessChecker.hasAccess(CheckFormView.class)) {
            nav.addItem(new SideNavItem("Check Form", CheckFormView.class, LineAwesomeIcon.PEN_ALT_SOLID.create()));
        }
        if (accessChecker.hasAccess(PersonFormView.class)) {
            nav.addItem(new SideNavItem("Person Form", PersonFormView.class, LineAwesomeIcon.USER.create()));
        }
        if (accessChecker.hasAccess(ExchangesView.class)) {
            nav.addItem(new SideNavItem("Exchanges", ExchangesView.class, LineAwesomeIcon.MONEY_BILL_WAVE_SOLID.create()));
        }
        if (accessChecker.hasAccess(ExchangeFormView.class)) {
            nav.addItem(new SideNavItem("Exchange Form", ExchangeFormView.class, LineAwesomeIcon.MONEY_BILL_SOLID.create()));
        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<UserInfo> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            UserInfo user = maybeUser.get();

            Avatar avatar = new Avatar(user.getUsername());
//            StreamResource resource = new StreamResource("profile-pic",
//                    () -> new ByteArrayInputStream(user.getProfilePicture()));
//            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getUsername());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}

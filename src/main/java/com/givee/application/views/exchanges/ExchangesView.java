package com.givee.application.views.exchanges;

import com.givee.application.entity.Currency;
import com.givee.application.entity.CurrencyExchangeRate;
import com.givee.application.repository.CurrencyExchangeRateRepository;
import com.givee.application.repository.CurrencyRepository;
import com.givee.application.utils.SpecificationUtils;
import com.givee.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Exchanges")
@Route(value = "exchanges", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class ExchangesView extends Div {

    private Grid<CurrencyExchangeRate> grid;

    private Filters filters;
    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;

    public ExchangesView(CurrencyExchangeRateRepository currencyExchangeRateRepository, CurrencyRepository currencyRepository) {
        this.currencyExchangeRateRepository = currencyExchangeRateRepository;
        setSizeFull();
        addClassNames("exchanges-view");

        filters = new Filters(currencyRepository, this::refreshGrid);
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private HorizontalLayout createMobileFilters() {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }

    public static class Filters extends Div implements Specification<CurrencyExchangeRate> {
        private final DatePicker startDate = new DatePicker("Date");
        private final DatePicker endDate = new DatePicker();
        private final MultiSelectComboBox<Currency> currencies = new MultiSelectComboBox<>("Currencies");

        public Filters(CurrencyRepository currencyRepository, Runnable onSearch) {
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            currencies.setItems(currencyRepository.findAll());
            currencies.setItemLabelGenerator(Currency::getCode);

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                startDate.clear();
                endDate.clear();
                currencies.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(createDateRangeFilter(), currencies, actions);
        }

        private Component createDateRangeFilter() {
            startDate.setPlaceholder("From");

            endDate.setPlaceholder("To");

            // For screen readers
            startDate.setAriaLabel("From date");
            endDate.setAriaLabel("To date");
            startDate.addValueChangeListener(valueChangeEvent -> endDate.setMin(valueChangeEvent.getValue()));
            endDate.addValueChangeListener(valueChangeEvent -> startDate.setMax(valueChangeEvent.getValue()));

            FlexLayout dateRangeComponent = new FlexLayout(startDate, new Text(" – "), endDate);
            dateRangeComponent.setAlignItems(FlexComponent.Alignment.BASELINE);
            dateRangeComponent.addClassName(LumoUtility.Gap.XSMALL);

            return dateRangeComponent;
        }

        @Override
        public Predicate toPredicate(Root<CurrencyExchangeRate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<String> currencyList = new ArrayList<>();
            for (Currency currency : currencies.getValue()) {
                currencyList.add(currency.getCode());
            }
            return SpecificationUtils.filterCurrencyExchangeRates("USD", currencyList, startDate.getValue(), endDate.getValue())
                    .toPredicate(root, query, criteriaBuilder);
        }
    }

    private Component createGrid() {
        grid = new Grid<>(CurrencyExchangeRate.class, false);
        grid.addColumn("exchangeDate").setAutoWidth(true);
        grid.addColumn("fromCurrency.code").setAutoWidth(true);
        grid.addColumn("toCurrency.code").setAutoWidth(true);
        grid.addColumn("rate").setAutoWidth(true);

        grid.setItems(query -> currencyExchangeRateRepository.findAll(filters,
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query))
        ).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}

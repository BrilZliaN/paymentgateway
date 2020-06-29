package ru.ifmo.practice.gateway.controller.ui.component;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExpirationDateField extends CustomField<String> {

    private static final ZoneId LAST_TIME_ZONE = ZoneId.ofOffset("UTC", ZoneOffset.MIN);
    private static final int SUPPORT_YEARS = 20;

    private final Select<String> monthSelect = new Select<>();
    private final Select<Integer> yearSelect = new Select<>();
    private final LocalDate currentDate = LocalDate.now(LAST_TIME_ZONE);
    private final int currentYear = currentDate.getYear();
    private final int currentMonth = currentDate.getMonthValue();

    private List<Integer> years = Collections.emptyList();

    public ExpirationDateField() {
        setLabel("Действительна до");
        initData();
        var layout = new HorizontalLayout();
        var separator = new Label("/");
        layout.add(monthSelect, separator, yearSelect);
        add(layout);
    }

    private void initData() {
        yearSelect.setPlaceholder("ГГ");
        monthSelect.setPlaceholder("ММ");
        yearSelect.setItems(generateYears());
        monthSelect.setItems(generateMonths());
        yearSelect.addValueChangeListener(e -> {
            var saved = monthSelect.getValue();
            var generatedMonths = generateMonths();
            monthSelect.setItems(generatedMonths);
            if (generatedMonths.contains(saved)) {
                monthSelect.setValue(saved);
            } else {
                monthSelect.setValue("");
            }
        });
    }

    @Override
    protected String generateModelValue() {
        var month = monthSelect.getValue();
        var year = yearSelect.getValue().toString().substring(2);
        return month + "/" + year;
    }

    @Override
    protected void setPresentationValue(String s) {
        String month = s.substring(0, 2);
        String yearShortString = s.substring(3, 5);
        int yearEnd = parseYear(yearShortString);
        int year = years
                .stream()
                .filter(x -> yearEnd == x % 100)
                .findAny()
                .orElse(yearEnd);
        monthSelect.setValue(month);
        yearSelect.setValue(year);
    }

    private int parseYear(String year) {
        try {
            return Integer.parseInt(year);
        } catch (NumberFormatException ignored) {
            return currentYear % 100;
        }
    }

    private List<Integer> generateYears() {
        return years = IntStream
                .range(currentYear, currentYear + SUPPORT_YEARS)
                .boxed()
                .collect(Collectors.toList());
    }

    private List<String> generateMonths() {
        var selected = yearSelect.getValue();
        boolean isCurrentYear = selected != null && selected.equals(currentYear);
        return IntStream
                .rangeClosed(isCurrentYear ? currentMonth : 1, 12)
                .mapToObj(Integer::toString)
                .map(string -> string.length() == 1 ? "0" + string : string)
                .collect(Collectors.toList());
    }
}

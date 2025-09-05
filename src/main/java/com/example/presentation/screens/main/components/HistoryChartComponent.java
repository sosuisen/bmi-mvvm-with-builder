package com.example.presentation.screens.main.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import com.example.domain.service.BmiRecordWithDiff;
import com.example.presentation.helpers.I18n;
import com.example.presentation.screens.main.MainViewModel;

import io.github.sosuisen.jfxbuilder.controls.CategoryAxisBuilder;
import io.github.sosuisen.jfxbuilder.controls.LineChartBuilder;
import io.github.sosuisen.jfxbuilder.controls.NumberAxisBuilder;
import io.github.sosuisen.jfxbuilder.controls.XYChartSeriesBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class HistoryChartComponent {
    public static LineChart<String, Number> getRoot(MainViewModel viewModel) {
        var chartData =
            FXCollections.observableArrayList(new ArrayList<XYChart.Data<String, Number>>());
        chartData.addAll(
            viewModel.getBmiList().stream().map(HistoryChartComponent::bmiToChartData).toList()
        );
        FXCollections.sort(chartData, Comparator.comparing(XYChart.Data::getXValue));

        viewModel.getBmiList().addListener((ListChangeListener<BmiRecordWithDiff>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    for (var record : change.getRemoved()) {
                        chartData
                            .removeIf(data -> data.getXValue().equals(dateToXValue(record.date())));
                    }
                }
                if (change.wasAdded()) {
                    chartData.addAll(
                        change.getAddedSubList().stream().map(HistoryChartComponent::bmiToChartData)
                            .toList()
                    );
                    FXCollections.sort(chartData, Comparator.comparing(XYChart.Data::getXValue));
                }
            }
        });

        return LineChartBuilder
            .withData(XYChartSeriesBuilder.<String, Number>create().data(chartData).build())
            .xAxis(
                CategoryAxisBuilder.create()
                    .labelPropertyApply(prop -> prop.bind(I18n.textProperty("history.chart.xaxis")))
                    .build()
            )
            .yAxis(
                NumberAxisBuilder.create()
                    .labelPropertyApply(prop -> prop.bind(I18n.textProperty("history.chart.yaxis")))
                    .build()
            )
            .title(I18n.text("history.chart.title"))
            .legendVisible(false)
            .animated(false)
            .prefWidth(300)
            .minWidth(200)
            .build();
    }

    private static String dateToXValue(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM/dd"));
    }

    private static XYChart.Data<String, Number> bmiToChartData(BmiRecordWithDiff bmiRecord) {
        return new XYChart.Data<>(dateToXValue(bmiRecord.date()), bmiRecord.bmi());
    }

}

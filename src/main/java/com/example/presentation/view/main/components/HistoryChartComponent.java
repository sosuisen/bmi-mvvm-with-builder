package com.example.presentation.view.main.components;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.example.domain.service.BmiRecordWithDiff;
import com.example.presentation.utils.I18n;
import com.example.presentation.view.main.MainViewModel;

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
        var chartData = FXCollections.observableArrayList(new ArrayList<XYChart.Data<String, Number>>());
        chartData.addAll(viewModel.getBmiList().stream().map(HistoryChartComponent::bmiToChartData).toList());
        viewModel.getBmiList().addListener((ListChangeListener<BmiRecordWithDiff>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    chartData.addAll(
                            change.getAddedSubList().stream().map(HistoryChartComponent::bmiToChartData).toList());
                } else if (change.wasRemoved()) {
                    chartData.subList(change.getFrom(), change.getTo() + 1).clear();
                }
            }
        });

        return LineChartBuilder
                .<String, Number>create(
                        CategoryAxisBuilder.create()
                                .labelPropertyApply(
                                        prop -> prop.bind(I18n.textProperty("history.chart.xaxis")))
                                .build(),
                        NumberAxisBuilder.create()
                                .labelPropertyApply(
                                        prop -> prop.bind(I18n.textProperty("history.chart.yaxis")))
                                .build())
                .addData(
                        XYChartSeriesBuilder.<String, Number>create()
                                .data(chartData)
                                .build())
                .title(I18n.text("history.chart.title"))
                .legendVisible(false)
                .prefWidth(300)
                .minWidth(200)
                .build();
    }

    private static XYChart.Data<String, Number> bmiToChartData(BmiRecordWithDiff bmiRecord) {
        return new XYChart.Data<>(bmiRecord.date().format(DateTimeFormatter.ofPattern("M/d")),
                bmiRecord.bmi());
    }

}

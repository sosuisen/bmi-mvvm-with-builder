package com.example.presentation.view.main.components;

import com.example.presentation.view.common.I18n;
import com.example.presentation.view.main.MainViewModel;

import io.github.sosuisen.jfxbuilder.controls.TabBuilder;
import io.github.sosuisen.jfxbuilder.controls.TabPaneBuilder;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;

public class HistoryComponent {

    public static TabPane getRoot(MainViewModel viewModel) {
        return TabPaneBuilder.create()
                .observableTabs(
                        TabBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.list.tab")))
                                .content(HistoryListComponent.getRoot(viewModel))
                                .closable(false)
                                .build(),
                        TabBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.table.tab")))
                                .content(HistoryTableComponent.getRoot(viewModel))
                                .closable(false)
                                .build(),
                        TabBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.chart.tab")))
                                .content(HistoryChartComponent.getRoot(viewModel))
                                .closable(false)
                                .build())
                .maxWidth(Double.MAX_VALUE)
                .hGrowInHBox(Priority.ALWAYS)
                .build();
    }

}

package com.example.presentation.screens.main.components;

import com.example.presentation.appmodel.BmiCommonAppModel;
import com.example.presentation.helpers.I18n;
import com.example.presentation.screens.main.MainViewModel;

import io.github.sosuisen.jfxbuilder.controls.ComboBoxBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.TabBuilder;
import io.github.sosuisen.jfxbuilder.controls.TabPaneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.HBoxBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HistoryComponent {

    public static VBox getRoot(MainViewModel viewModel) {
        return VBoxBuilder
            .withChildren(
                HBoxBuilder
                    .withChildren(
                        LabelBuilder.create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.limit"))
                            )
                            .alignment(Pos.CENTER)
                            .marginInHBox(new Insets(3))
                            .build(),
                        ComboBoxBuilder
                            .withItems(
                                BmiCommonAppModel.HISTORY_LIMIT
                            )
                            .valuePropertyApply(
                                prop -> prop
                                    .bindBidirectional(viewModel.historyLimitProperty().asObject())
                            )
                            .build()
                    )
                    .build(),
                TabPaneBuilder
                    .withTabs(
                        TabBuilder.create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.list.tab"))
                            )
                            .content(HistoryListComponent.getRoot(viewModel))
                            .closable(false)
                            .build(),
                        TabBuilder.create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.table.tab"))
                            )
                            .content(HistoryTableComponent.getRoot(viewModel))
                            .closable(false)
                            .build(),
                        TabBuilder.create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.chart.tab"))
                            )
                            .content(HistoryChartComponent.getRoot(viewModel))
                            .closable(false)
                            .build()
                    )
                    .maxWidth(Double.MAX_VALUE)
                    .hGrowInHBox(Priority.ALWAYS)
                    .build()
            )
            .build();
    }

}

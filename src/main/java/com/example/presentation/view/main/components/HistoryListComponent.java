package com.example.presentation.view.main.components;

import java.time.format.DateTimeFormatter;

import com.example.domain.model.BmiRecord;
import com.example.presentation.view.common.I18n;
import com.example.presentation.view.main.MainViewModel;

import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.ListViewBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HistoryListComponent {
    public static VBox getRoot(MainViewModel viewModel) {
        return VBoxBuilder
                .withChildren(
                        LabelBuilder.create()
                                .textPropertyApply(
                                        prop -> prop.bind(I18n.textProperty("history.list.title")))
                                .build(),
                        ListViewBuilder.<BmiRecord>create()
                                .items(viewModel.getBmiList())
                                .cellFactory(HistoryListComponent::recordsCellFactory)
                                .vGrowInVBox(Priority.ALWAYS)
                                .build())
                .padding(new Insets(3))
                .build();

    }

    private static ListCell<BmiRecord> recordsCellFactory(ListView<BmiRecord> listView) {
        return new ListCell<BmiRecord>() {
            @Override
            protected void updateItem(BmiRecord item, boolean empty) {
                super.updateItem(item, empty);

                textProperty().unbind();

                if (empty || item == null) {
                    setText(null);
                } else {
                    textProperty().bind(
                            I18n.textProperty("main.obesity.category." + item.getObesity().toResourceString())
                                    .map(obesity -> String.format("[%s] %.1f (%s)",
                                            item.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                            item.getBmi(),
                                            obesity)));
                }
            }
        };
    }

}

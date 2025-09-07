module com.example.app {
    requires javafx.controls;
    requires io.github.sosuisen.api.jfxbuilder.controls;
    requires io.github.sosuisen.api.jfxbuilder.graphics;

    requires org.jooq;

    exports com.example.repository.jooq.tables.records to org.jooq;
    exports com.example.domain.model to org.jooq;

    opens com.example.presentation to javafx.graphics;
    opens com.example.main to javafx.graphics;
}

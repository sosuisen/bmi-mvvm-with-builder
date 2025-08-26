CREATE TABLE bmi_history (
    id INTEGER NOT NULL,
    height_meter REAL NOT NULL,
    weight_kg REAL NOT NULL,
    date TEXT NOT NULL UNIQUE,
    CONSTRAINT bmi_pk PRIMARY KEY (id)
);
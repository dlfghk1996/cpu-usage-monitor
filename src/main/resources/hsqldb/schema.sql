DROP TABLE cpu_usage IF EXISTS;

CREATE TABLE cpu_usage
(
    id bigint AUTO_INCREMENT NOT NULL,
    measurement_value  double,
    measurement_time DATETIME,
    PRIMARY KEY (id)
);


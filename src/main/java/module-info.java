module com.covoiturage {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires jbcrypt;
    requires static lombok;
    requires org.slf4j;
    requires java.sql;
    requires java.naming;
    requires com.jfoenix;
    requires java.mail;

    opens com.covoiturage to javafx.fxml;
    opens com.covoiturage.controller to javafx.fxml;
    opens com.covoiturage.model to org.hibernate.orm.core;

    exports com.covoiturage;
    exports com.covoiturage.controller;
    exports com.covoiturage.model;
    exports com.covoiturage.service;
    exports com.covoiturage.dao;
}
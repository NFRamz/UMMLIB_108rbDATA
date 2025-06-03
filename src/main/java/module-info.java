module Tugas_6_JavaFX{

        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.web;
        requires javafx.media;
        requires org.controlsfx.controls;
        requires com.dlsc.formsfx;
        requires net.synedra.validatorfx;
        requires org.kordamp.ikonli.javafx;
        requires org.kordamp.bootstrapfx.core;
        requires eu.hansolo.tilesfx;
        requires com.almasb.fxgl.all;
        requires java.mail;
        requires activation;
        requires java.sql;
        requires sqlite.jdbc;
        requires dotenv.java;
        requires org.json;
        requires com.zaxxer.hikari;

        opens books to javafx.base;
        exports books;

        opens sound to javafx.media;
        exports sound;

        opens Main to javafx.fxml;
        exports Main;
        exports data;
        opens data to javafx.fxml;
    exports Testing;
    opens Testing to javafx.fxml;

        exports Testing.datatest; // ⬅️ Tambahkan baris ini
        exports Testing.tampilan; // ⬅️ Tambahkan baris ini

        opens Testing.datatest to javafx.graphics; // ⬅️ Ini juga penting agar JavaFX bisa mengaksesnya
        opens Testing.tampilan to javafx.graphics; // ⬅️ Ini juga penting agar JavaFX bisa mengaksesnya
}


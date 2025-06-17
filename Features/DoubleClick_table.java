package Features;

import books.Book;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class DoubleClick_table {

    public static void setupDragAndDrop(TableColumn<Book, String> column, TextField bookIdField) {
        column.setCellFactory(param -> new TableCell<>() {

            {
                setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {
                        bookIdField.setText(getItem());
                    }
                });
            }

            @Override
           public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
            }
        });
    }
}

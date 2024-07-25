import com.mongodb.client.*;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.*;
import org.bson.Document;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("celltechhub");
            MongoCollection<Document> collection = database.getCollection("Usuarios");
            // Crear índice único
            IndexOptions indexOptions = new IndexOptions().unique(true);
            collection.createIndex(new Document("username", 1), indexOptions);
            //Ingresar Administrador
            Document documento = new Document("username", "Anderson")
                    .append("password", "123456");

            try {
                collection.insertOne(documento);
                System.out.println("Administrador insertado con éxito");
            } catch (Exception e) {
                System.out.println("El Administrador ya esta en la base de datos");
            }
        }
        JFrame frame = new JFrame("Cell Tech Hub");
        frame.setContentPane(new Login().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setSize(1000, 630);
        frame.setVisible(true);
    }
}

import com.mongodb.client.*;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

public class UsuarioAdmi {
    public void insertar(String username, String password) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("celltechhub");
            MongoCollection<Document> collection = database.getCollection("Usuarios");
            // Crear índice único
            IndexOptions indexOptions = new IndexOptions().unique(true);
            collection.createIndex(new Document("username", 1), indexOptions);
            // Ingresar Administrador
            Document documento = new Document("username", username)
                    .append("password", password);

            try {
                collection.insertOne(documento);
                System.out.println("Administrador insertado con éxito");
            } catch (Exception e) {
                System.out.println("El Administrador ya está en la base de datos");
            }
        }
    }
}

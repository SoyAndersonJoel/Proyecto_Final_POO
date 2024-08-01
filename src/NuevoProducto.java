import com.mongodb.client.*;
import org.bson.Document;

public class NuevoProducto{

    private  MongoClient mongoClient;
    private  MongoDatabase database;
    private  MongoCollection<Document> collection;

    public NuevoProducto() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("celltechhub");
        collection = database.getCollection("Productos");
    }

    public Productos obtenerProducto() {
        try {
            FindIterable<Document> iterable = collection.find().sort(new Document("_id", -1)).limit(1);
            Document ProductoDoc = iterable.first();

            if (ProductoDoc != null) {
                Productos newProducto = new Productos(
                        ProductoDoc.getString("nombre"),
                        ProductoDoc.getString("descripcion"),
                        ProductoDoc.getInteger("cantidad"),
                        ProductoDoc.getDouble("precio"),
                        ProductoDoc.getString("foto")
                );
                newProducto.setId(ProductoDoc.getObjectId("_id"));
                return newProducto;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            mongoClient.close();
        }
    }

    public void cerrarConexion() {
        mongoClient.close();
    }
}

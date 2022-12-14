import spark.Spark;

public class Main {
    public static void main(String[] args) {
        Spark.get("/hello", (request, response) -> "Hello world");
    }


}
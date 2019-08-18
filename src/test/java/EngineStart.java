import com.seomse.api.ApiRequest;

public class EngineStart {

    public static void main(String[] args) {

        ApiRequest request = new ApiRequest("127.0.0.1", 33316);
        if (request.connect()) {
            System.out.println("conn..");
            request.setPackageName("com.seomse.system.server.api");
            System.out.println(request.sendToReceiveMessage("EngineRunApi", "E1"));
        }

    }
}

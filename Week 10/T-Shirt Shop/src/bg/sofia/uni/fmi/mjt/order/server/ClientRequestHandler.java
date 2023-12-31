package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {

    private Socket socket;
    private MJTOrderRepository mjtOrderRepository;
    private static final int SIZE_FIELD = 1;
    private static final int COLOR_FIELD = 2;
    private static final int DESTINATION_FIELD = 3;

    public ClientRequestHandler(Socket socket, MJTOrderRepository mjtOrderRepository) {
        this.socket = socket;
        this.mjtOrderRepository = mjtOrderRepository;
    }

    private Response commandGet(String... tokens) {
        if (tokens[1].equals("all")) {
            return mjtOrderRepository.getAllOrders();
        } else if (tokens[1].equals("all-successfuly")) {
            return mjtOrderRepository.getAllSuccessfulOrders();
        } else if (tokens[1].equals("my-order")) {
            String[] idTokens = tokens[2].split("=");
            return mjtOrderRepository.getOrderById(Integer.parseInt(idTokens[1]));
        }
        return null;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] tokens = inputLine.split("\s");
                Response response = null;
                switch (tokens[0]) {
                    case "request" -> {
                        String[] sizeTokens = tokens[SIZE_FIELD].split("=");
                        String[] colorTokens = tokens[COLOR_FIELD].split("=");
                        String[] destinationTokens = tokens[DESTINATION_FIELD].split("=");
                        response = mjtOrderRepository.request(sizeTokens[1], colorTokens[1], destinationTokens[1]);
                    }
                    case "get" -> commandGet(tokens);
                }
                out.println(response.toString());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
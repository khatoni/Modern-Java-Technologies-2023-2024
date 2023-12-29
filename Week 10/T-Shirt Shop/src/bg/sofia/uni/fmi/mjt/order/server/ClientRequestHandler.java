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

    public ClientRequestHandler(Socket socket, MJTOrderRepository mjtOrderRepository) {
        this.socket = socket;
        this.mjtOrderRepository = mjtOrderRepository;
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
                        if (tokens.length != 4) {

                        } else {
                            String[] sizeTokens = tokens[1].split("=");
                            String[] colorTokens = tokens[2].split("=");
                            String[] destinationTokens = tokens[3].split("=");
                            response = mjtOrderRepository.request(sizeTokens[1], colorTokens[1], destinationTokens[1]);
                        }
                    }
                    case "get" -> {
                        if (tokens[1].equals("all")) {
                            response = mjtOrderRepository.getAllOrders();
                        } else if (tokens[1].equals("all-successfuly")) {
                            response = mjtOrderRepository.getAllSuccessfulOrders();
                        } else if (tokens[1].equals("my-order")) {
                            String[] idTokens = tokens[2].split("=");
                            response = mjtOrderRepository.getOrderById(Integer.parseInt(idTokens[1]));
                        }
                    }
                }

                out.println(response.toText());
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
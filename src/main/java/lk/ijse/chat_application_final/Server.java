package lk.ijse.chat_application_final;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ClientHandler> clients;

    public Server(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = null;
                    clients = new ArrayList<>();
            System.out.println("Server started on port " + port);
            while (true) {
                try {
                    socket = serverSocket.accept();
                    System.out.println("New client connected: " + socket);
                    ClientHandler clientHandler = new ClientHandler(socket, this);
                    clients.add(clientHandler);
                    clientHandler.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                } finally {
                    System.out.println("Socket is closed");
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(ClientHandler clientHandler, String message) {
        for (ClientHandler client : clients) {
            if (client != clientHandler) {
                client.sendMessage(message);
            }
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(1234);
    }
}
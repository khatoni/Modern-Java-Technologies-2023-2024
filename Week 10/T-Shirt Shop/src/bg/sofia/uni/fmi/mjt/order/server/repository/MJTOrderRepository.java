package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MJTOrderRepository implements OrderRepository {

    private static int id = 1;
    BlockingQueue<Order> orders;

    public MJTOrderRepository() {
        orders = new ArrayBlockingQueue<>(100);
    }

    public Response request(String size, String color, String destination) {
        if (size == null || color == null || destination == null) {
            throw new IllegalArgumentException("size, color or destination is null");
        }
        String invalidInfo = "invalid: ";
        Size shirtSize;
        try {
            shirtSize = Size.valueOf(size);
        } catch (IllegalArgumentException e) {
            shirtSize = Size.UNKNOWN;
            invalidInfo += "size";
        }
        Color shirtColor;
        try {
            shirtColor = Color.valueOf(color);
        } catch (IllegalArgumentException e) {
            shirtColor = Color.UNKNOWN;
            invalidInfo += ",color";
        }
        Destination shirtDestination;
        try {
            shirtDestination = Destination.valueOf(destination);
        } catch (IllegalArgumentException e) {
            shirtDestination = Destination.UNKNOWN;
            invalidInfo += ",destination";
        }
        if ("invalid: ".equals(invalidInfo)) {
            orders.add(new Order(id, new TShirt(shirtSize, shirtColor), shirtDestination));
            return Response.create(id);
            id++;
        } else {
            orders.add(new Order(-1, new TShirt(shirtSize, shirtColor), shirtDestination));
            return Response.decline(invalidInfo);
        }
    }

    public Response getOrderById(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("id is negative");
        }
        Order found = null;
        for (Order order : orders) {
            if (order.id() == id) {
                found = order;
            }
        }
        if (found == null) {
            return Response.notFound(id);
        } else {
            Collection<Order> answer = new ArrayList<>();
            answer.add(found);
            return Response.ok(answer);
        }
    }

    public Response getAllOrders() {
        return Response.ok(orders);
    }

    public Response getAllSuccessfulOrders() {
        Collection<Order> answer = new ArrayList<>();
        for (Order order : orders) {
            if (order.id() != -1) {
                answer.add(order);
            }
        }
        return Response.ok(answer);
    }
}

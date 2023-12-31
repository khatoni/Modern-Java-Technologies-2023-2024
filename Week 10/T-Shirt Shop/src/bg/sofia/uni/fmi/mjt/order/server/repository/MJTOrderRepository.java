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
import java.util.concurrent.atomic.AtomicInteger;

public class MJTOrderRepository implements OrderRepository {

    private static final int MAX_CAPACITY = 100000;
    private AtomicInteger id = new AtomicInteger();
    BlockingQueue<Order> orders;

    public MJTOrderRepository() {
        orders = new ArrayBlockingQueue<>(MAX_CAPACITY);
    }

    public Response request(String size, String color, String destination) {
        if (size == null || color == null || destination == null) {
            throw new IllegalArgumentException("size, color or destination is null");
        }
        Size shirtSize = validateSize(size);
        Color shirtColor = validateColor(color);
        Destination shirtDestination = validateDestination(destination);
        String invalidInfo = getInvalidInfo(shirtSize, shirtColor, shirtDestination);
        if ("invalid: ".equals(invalidInfo)) {

            orders.add(new Order(id.addAndGet(1), new TShirt(shirtSize, shirtColor), shirtDestination));
            return Response.create(id.get());
        } else {
            orders.add(new Order(-1, new TShirt(shirtSize, shirtColor), shirtDestination));
            return Response.decline(invalidInfo);
        }
    }

    private Size validateSize(String size) {
        Size shirtSize;
        try {
            shirtSize = Size.valueOf(size);
        } catch (IllegalArgumentException e) {
            shirtSize = Size.UNKNOWN;
        }
        return shirtSize;
    }

    private Color validateColor(String color) {
        Color shirtColor;
        try {
            shirtColor = Color.valueOf(color);
        } catch (IllegalArgumentException e) {
            shirtColor = Color.UNKNOWN;

        }
        return shirtColor;
    }

    private Destination validateDestination(String destination) {
        Destination shirtDestination;
        try {
            shirtDestination = Destination.valueOf(destination);
        } catch (IllegalArgumentException e) {
            shirtDestination = Destination.UNKNOWN;
        }
        return shirtDestination;
    }

    private String getInvalidInfo(Size size, Color color, Destination destination) {
        String invalidInfo = "invalid: ";
        if (size == Size.UNKNOWN) {
            invalidInfo += "size";
        }
        if (color == Color.UNKNOWN) {
            if (invalidInfo.contains("size")) {
                invalidInfo += ",color";
            } else {
                invalidInfo += "color";
            }
        }

        if (destination == Destination.UNKNOWN) {
            if (invalidInfo.contains("size") || invalidInfo.contains("color")) {
                invalidInfo += ",destination";
            } else {
                invalidInfo += "destination";
            }
        }
        return invalidInfo;
    }

    public Response getOrderById(int id) {
        if (id <= 0) {
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

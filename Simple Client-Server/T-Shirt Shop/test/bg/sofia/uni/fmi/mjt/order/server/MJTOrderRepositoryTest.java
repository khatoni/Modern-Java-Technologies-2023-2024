package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MJTOrderRepositoryTest {

    @Test
    void testRequestNullArguments() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.request(null, "BLACK", "EUROPE"),
            "IllegalArgumentException was expected");
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.request("L", null, "EUROPE"),
            "IllegalArgumentException was expected");
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.request("L", "BLACK", null),
            "IllegalArgumentException was expected");
    }

    @Test
    void testRequestDeclinedOrderInvalidSize() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Response expectedResponse = Response.decline("invalid: size");
        Response toTest = repository.request("K", "RED", "EUROPE");
        Assertions.assertEquals(expectedResponse, toTest, "The retured response is not the same as the expected one");
    }

    @Test
    void testRequestDeclinedOrderInvalidColor() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Response expectedResponse = Response.decline("invalid: color");
        Response toTest = repository.request("M", "BLUE", "EUROPE");
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

    @Test
    void testRequestDeclinedOrderInvalidDestination() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Response expectedResponse = Response.decline("invalid: destination");
        Response toTest = repository.request("M", "WHITE", "BULGARIA");
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

    @Test
    void testRequestDeclinedOrderInvalidSizeColor() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Response expectedResponse = Response.decline("invalid: size,color");
        Response toTest = repository.request("K", "ORANGE", "EUROPE");
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

    @Test
    void testRequestDeclinedOrderInvalidSizeDestination() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Response expectedResponse = Response.decline("invalid: size,destination");
        Response toTest = repository.request("K", "BLACK", "Bulgaria");
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

    @Test
    void testRequestDeclinedOrderInvalidColorDestination() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Response expectedResponse = Response.decline("invalid: color,destination");
        Response toTest = repository.request("L", "GREEN", "Bulgaria");
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

    @Test
    void testRequestDeclinedOrderInvalidSizeColorDestination() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Response expectedResponse = Response.decline("invalid: size,color,destination");
        Response toTest = repository.request("K", "BLUE", "SERBIA");
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

    @Test
    void testRequestSuccessfully() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Response expectedResponse = Response.create(1);
        Response toTest = repository.request("M", "WHITE", "EUROPE");
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

    @Test
    void testGetOrderByIdInvalidId() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.getOrderById(0),
            "IllegalArgumentException was expected");
    }

    @Test
    void testGetOrderByIDNotFound() {
        MJTOrderRepository repository = new MJTOrderRepository();
        Response expectedResponse = Response.notFound(2);
        Response toTest = repository.getOrderById(2);
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

    @Test
    void testGetOrderByIDSuccessfullyFound() {
        MJTOrderRepository repository = new MJTOrderRepository();
        repository.request("L", "WHITE", "EUROPE");
        repository.request("M", "BLACK", "EUROPE");
        Collection<Order> expectedOrder = new ArrayList<>();
        expectedOrder.add(new Order(2, new TShirt(Size.M, Color.BLACK), Destination.EUROPE));
        Response expectedResponse = Response.ok(expectedOrder);
        Response toTest = repository.getOrderById(2);
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

    @Test
    void testGetAllOrders() {
        MJTOrderRepository repository = new MJTOrderRepository();
        repository.request("L", "WHITE", "EUROPE");
        BlockingQueue<Order> expectedOrder = new ArrayBlockingQueue<>(10);
        expectedOrder.add(new Order(1, new TShirt(Size.L, Color.WHITE), Destination.EUROPE));
        Response expectedResponse = Response.ok(expectedOrder);
        Response toTest = repository.getAllOrders();

    }

    @Test
    void testGetAllSuccessfullyOrders() {
        MJTOrderRepository repository = new MJTOrderRepository();
        repository.request("L", "WHITE", "EUROPE");
        repository.request("M", "BLACK", "EUROPE");
        Collection<Order> expectedOrder = new ArrayList<>();
        expectedOrder.add(new Order(1, new TShirt(Size.L, Color.WHITE), Destination.EUROPE));
        expectedOrder.add(new Order(2, new TShirt(Size.M, Color.BLACK), Destination.EUROPE));
        Response expectedResponse = Response.ok(expectedOrder);
        Response toTest = repository.getAllSuccessfulOrders();
        Assertions.assertEquals(expectedResponse, toTest, "The returned response is not the same as the expected one");
    }

}

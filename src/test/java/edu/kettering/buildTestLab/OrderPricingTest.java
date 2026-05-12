package edu.kettering.buildTestLab;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderPricingTest {

    private final OrderPricing pricing = new OrderPricing();


    @Test
    void emptyCartReturnsZero() {
        int total = pricing.totalCents(List.of());
        assertEquals(0, total);
    }


    @Test
    void subtotalAtOrAboveFreeShippingGetsFreeShipping() {
        // Subtotal = 5000 exactly
        List<OrderPricing.LineItem> items = List.of(
                new OrderPricing.LineItem("LAPTOP", 1, 5000)
        );

        int total = pricing.totalCents(items);

        assertEquals(5000, total);
    }


    // -------------------- Boundary / edge cases --------------------

    @Test
    void bulkQuantityAcrossMultipleItemsGetsDiscount() {
        List<OrderPricing.LineItem> items = List.of(
                new OrderPricing.LineItem("PEN", 5, 200),
                new OrderPricing.LineItem("NOTEBOOK", 5, 300)
        );

        int total = pricing.totalCents(items);

        // Subtotal 2500 + shipping 799 - bulk discount 300
        assertEquals(2999, total);
    }

    @Test
    void subtotalBelowFreeShippingAddsShipping() {
        List<OrderPricing.LineItem> items = List.of(
                new OrderPricing.LineItem("NOTEBOOK", 1, 4999)
        );

        int total = pricing.totalCents(items);

        assertEquals(5798, total);
    }


    // -------------------- Validation tests --------------------

    @Test
    void nullItemListThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> pricing.totalCents(null));
    }

    @Test
    void invalidLineItemThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPricing.LineItem(null, 1, 100));
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPricing.LineItem("", 1, 100));
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPricing.LineItem("PEN", 0, 100));
        assertThrows(IllegalArgumentException.class,
                () -> new OrderPricing.LineItem("PEN", 1, -1));
    }

    @Test
    void nullLineItemThrowsException() {
        assertThrows(NullPointerException.class,
                () -> pricing.totalCents(List.of((OrderPricing.LineItem) null)));
    }


    
}


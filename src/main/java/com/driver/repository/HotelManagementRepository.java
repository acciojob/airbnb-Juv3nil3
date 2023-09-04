package com.driver.repository;

import com.driver.model.Booking;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class HotelManagementRepository {
    private Map<String, Hotel> hotelDb = new HashMap<>();
    private Map<Integer, User> userDb = new HashMap<>();
    private Map<String, Booking> bookingDb = new HashMap<>();
    
    public Hotel findHotelByName(String hotelName) {
        return hotelDb.get(hotelName);
    }

    public void addHotel(Hotel hotel) {
        hotelDb.put(hotel.getHotelName(), hotel);
    }

    public void addUser(User user) {
        userDb.put(user.getaadharCardNo(), user);
    }

    public List<Hotel> getAllHotels() {
        return new ArrayList<>(hotelDb.values());
    }

    public void saveBooking(Booking booking) {
        String bookingId = UUID.randomUUID().toString();
        booking.setBookingId(bookingId);
        bookingDb.put(bookingId, booking);
    }

    public List<Booking> getBookingsByAadharCard(Integer aadharCard) {
        return bookingDb.values().stream()
                .filter(booking-> booking.getBookingAadharCard() == aadharCard)
                .collect(Collectors.toList());
    }
}

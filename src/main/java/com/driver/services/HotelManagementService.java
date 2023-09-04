package com.driver.services;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.repository.HotelManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelManagementService {

    private HotelManagementRepository hotelManagementRepository;

    public HotelManagementService(HotelManagementRepository hotelManagementRepository) {
        this.hotelManagementRepository = hotelManagementRepository;
    }

    public String addHotel(Hotel hotel) {
        if (hotel == null || (hotel.getHotelName() == null)) {
            return "FAILURE";
        } else if ( hotelManagementRepository.findHotelByName(hotel.getHotelName()) != null) {
            return "FAILURE";
        }
        hotelManagementRepository.addHotel(hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        hotelManagementRepository.addUser(user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        List<Hotel> hotels = hotelManagementRepository.getAllHotels();

        Optional<Hotel> hotelWithMostFacilities = hotels.stream()
                .filter(hotel1 -> !hotel1.getFacilities().isEmpty())
                .max((hotel1, hotel2)-> {
                     int facilityCount1 = hotel1.getFacilities().size();
                     int facilityCount2 = hotel2.getFacilities().size();

                     if(facilityCount1 != facilityCount2) {
                         return Integer.compare(facilityCount1,facilityCount2);
                     } else {
                         return hotel1.getHotelName().compareTo(hotel2.getHotelName());
                     }
                });
        return hotelWithMostFacilities.map(Hotel:: getHotelName).orElse("");
    }

    public int bookARoom(Booking booking) {
        Hotel hotel = hotelManagementRepository.findHotelByName(booking.getHotelName());

        if(hotel == null || hotel.getAvailableRooms() < booking.getNoOfRooms()) {
            return -1;
        }
        int totalAmount = booking.getNoOfRooms() * hotel.getPricePerNight();

        hotel.setAvailableRooms(hotel.getAvailableRooms() - booking.getNoOfRooms());

        hotelManagementRepository.saveBooking(booking);

        return totalAmount;

    }

    public int getBookings(Integer aadharCard) {
        List<Booking> bookings = hotelManagementRepository.getBookingsByAadharCard(aadharCard);
        return bookings.size();
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelManagementRepository.findHotelByName(hotelName);

        if(hotel != null){
            List<Facility> currentFacilities = hotel.getFacilities();
            for(Facility facility : newFacilities) {
                if(!currentFacilities.contains(facility)) {
                    currentFacilities.add(facility);
                }
            }
        }
        return hotel;
    }
}

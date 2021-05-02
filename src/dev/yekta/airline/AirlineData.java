/*
 * Copyright © 2021 Ali Khaleqi Yekta, All Rights Reserved.
 *
 * Author: Ali Khaleqi Yekta [YektaDev]
 * Website: https://Yekta.Dev
 * Email: Me@Yekta.Dev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.yekta.airline;

import java.util.ArrayList;

public class AirlineData {
    private static AirlineData airlineData;

    public static AirlineData getInstance() {
        if (airlineData == null)
            airlineData = new AirlineData();

        return airlineData;
    }

    private final ArrayList<User> users;
    private final ArrayList<Flight> flights;
    private int currentSessionUserId = -1;

    public AirlineData() {
        users = new ArrayList<>();
        flights = new ArrayList<>();
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void addUser(String username, String password) {
        users.add(new User(username, password));
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public boolean hasUser(String username) {
        for (User u : users)
            if (u.getUsername().equals(username))
                return true;

        return false;
    }

    public User currentUser() {
        return users.get(currentSessionUserId);
    }

    public boolean login(String username, String password) {
        for (User u : users)
            if (u.getUsername().equals(username) && u.getPassword().equals(User.hashPassword(password))) {
                currentSessionUserId = u.getId();
                return true;
            }

        return false;
    }

    public void logout() {
        currentSessionUserId = -1;
    }

    public ArrayList<Flight> findFlights(String from, String to, String weekDay, int hour) {
        ArrayList<Flight> result = new ArrayList<>();

        flights.stream().filter(f ->
                f.getFrom().equalsIgnoreCase(from)
                        && f.getTo().equalsIgnoreCase(to)
                        && f.getWeekDay().equalsIgnoreCase(weekDay)
                        && f.getHour() == hour
        ).forEach(result::add);

        return result;
    }

    public ArrayList<Flight> findFlights(String from, String to, String weekDay) {
        ArrayList<Flight> result = new ArrayList<>();

        flights.stream().filter(f ->
                f.getFrom().equalsIgnoreCase(from)
                        && f.getTo().equalsIgnoreCase(to)
                        && f.getWeekDay().equalsIgnoreCase(weekDay)
        ).forEach(result::add);

        return result;
    }

    public ArrayList<Flight> findFlights(String from, String to) {
        ArrayList<Flight> result = new ArrayList<>();

        flights.stream().filter(f ->
                f.getFrom().equalsIgnoreCase(from)
                        && f.getTo().equalsIgnoreCase(to)
        ).forEach(result::add);

        return result;
    }

    public ArrayList<Flight> findFlights(String from) {
        ArrayList<Flight> result = new ArrayList<>();
        flights.stream().filter(f -> f.getFrom().equalsIgnoreCase(from)).forEach(result::add);
        return result;
    }
}

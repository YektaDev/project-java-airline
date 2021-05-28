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
    final static String usersFileDir = "users.dat";
    final static String flightsFileDir = "flights.dat";

    private static AirlineData airlineData;


    public static AirlineData getInstance() {
        if (airlineData == null)
            airlineData = new AirlineData();

        return airlineData;
    }

    private final FileManager<Flight> flightFileManager;
    private final FileManager<User> userFileManager;
    private int currentSessionUserId = -1;

    public AirlineData() {
        userFileManager = new FileManager<>(User::new, usersFileDir);
        flightFileManager = new FileManager<>(Flight::new, flightsFileDir);
    }

    public FileManager<Flight> getFlightsFile() {
        return flightFileManager;
    }

    public FileManager<User> getUsersFile() {
        return userFileManager;
    }

    public void addUser(String username, String password) {
        userFileManager
                .open()
                .appendRecord(new User(username, password))
                .close();
    }

    public void addFlight(Flight flight) {
        flightFileManager
                .open()
                .appendRecord(flight)
                .close();
    }

    public boolean hasUser(String username) {
        userFileManager.open();

        while (userFileManager.pointerPos() < userFileManager.length()) {
            User u = userFileManager.readRecord();
            if (u.getUsername().equals(username)) {
                userFileManager.close();
                return true;
            }
        }

        userFileManager.close();
        return false;
    }

    public User currentUser() {
        userFileManager.open();
        String u = userFileManager.seekEndOfLine(currentSessionUserId + 1).getKey();

        userFileManager.close();
        return new User().deserialize(u);
    }

    public boolean login(String username, String password) {
        userFileManager.open();

        while (userFileManager.pointerPos() < userFileManager.length()) {
            User u = userFileManager.readRecord();
            if (u.getUsername().equals(username) && new User().cleanup(u.getPassword()).equals(User.hashPassword(password))) {
                currentSessionUserId = u.getId();
                userFileManager.close();
                return true;
            }
        }

        userFileManager.close();
        return false;
    }

    public void logout() {
        currentSessionUserId = -1;
    }

    public ArrayList<Flight> findFlights(String from, String to, String weekDay, int hour) {
        flightFileManager.open();
        ArrayList<Flight> result = new ArrayList<>();

        while (flightFileManager.pointerPos() < flightFileManager.length()) {
            Flight f = flightFileManager.readRecord();
            if (f.getFrom().equalsIgnoreCase(from)
                    && f.getTo().equalsIgnoreCase(to)
                    && f.getWeekDay().equalsIgnoreCase(weekDay)
                    && f.getHour() == hour) {
                result.add(f);
            }
        }

        flightFileManager.close();
        return result;
    }

    public ArrayList<Flight> findFlights(String from, String to, String weekDay) {
        flightFileManager.open();
        ArrayList<Flight> result = new ArrayList<>();

        while (flightFileManager.pointerPos() < flightFileManager.length()) {
            Flight f = flightFileManager.readRecord();
            if (f.getFrom().equalsIgnoreCase(from)
                    && f.getTo().equalsIgnoreCase(to)
                    && f.getWeekDay().equalsIgnoreCase(weekDay)) {
                result.add(f);
            }
        }

        flightFileManager.close();
        return result;
    }

    public ArrayList<Flight> findFlights(String from, String to) {
        flightFileManager.open();
        ArrayList<Flight> result = new ArrayList<>();

        while (flightFileManager.pointerPos() < flightFileManager.length()) {
            Flight f = flightFileManager.readRecord();
            if (f.getFrom().equalsIgnoreCase(from)
                    && f.getTo().equalsIgnoreCase(to)) {
                result.add(f);
            }
        }

        flightFileManager.close();
        return result;
    }

    public ArrayList<Flight> findFlights(String from) {
        flightFileManager.open();
        ArrayList<Flight> result = new ArrayList<>();

        while (flightFileManager.pointerPos() < flightFileManager.length()) {
            Flight f = flightFileManager.readRecord();
            if (f.getFrom().equalsIgnoreCase(from)) {
                result.add(f);
            }
        }

        flightFileManager.close();
        return result;
    }

    int countRealLines() {
        userFileManager.open();
        int lines = 0;
        String line;
        while ((line = userFileManager.readLine()) != null && !line.isEmpty())
            lines++;
        return lines;
    }

}

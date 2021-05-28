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

public class User implements Serializable<User>, Cloneable {
    private static int lastId = -1;
    private final int id;
    private final String username;
    private final String password;
    private ArrayList<Flight> flights;

    private int getLastId() {
        if (lastId == -1)
            return AirlineData.getInstance().countRealLines() - 1;
        return lastId;
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.flights = new ArrayList<>();
    }

    public User() {
        this.id = -1;
        this.username = this.password = null;
        this.flights = null;
    }

    public User(String username, String password) {
        lastId = getLastId();
        this.id = ++lastId;
        this.username = username;
        this.password = hashPassword(password);
        this.flights = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public static String hashPassword(String password) {
        return Hash.sha256(password);
    }

    public User updateFlights() {
        FileManager<User> file = AirlineData.getInstance().getUsersFile();
        file.open();

        User current;
        while (file.pointerPos() < file.length()) {
            current = file.readRecord();
            if (current.getId() == id) {
                this.flights.clear();
                this.flights.addAll(current.getFlights());
                file.close();
                return this;
            }
        }

        file.close();
        return null;
    }

    @Override
    public String serialize() {
        StringBuilder flightsString = new StringBuilder();

        for (int i = 0; i < flights.size(); i++) {
            flightsString.append(flights.get(i).serialize());

            if (i < flights.size() - 1)
                flightsString.append(SEP_W);
        }

        return id + SEP_W + username + SEP_W + password + ((flights.size() > 0) ? (SEP_W + flightsString) : "");
    }

    @Override
    public User deserialize(String data) {
        String[] userData = data.split(SEP_R);
        ArrayList<Flight> userFlights = new ArrayList<>();

        for (int i = 3; i <= userData.length - 2; i += 5) {
            userFlights.add(
                    new Flight(
                            cleanup(userData[i]),
                            cleanup(userData[i + 1]),
                            cleanup(userData[i + 2]),
                            cleanup(userData[i + 3]),
                            Integer.parseInt(cleanup(userData[i + 4]))
                    )
            );
        }

        User u = new User(
                Integer.parseInt(cleanup(userData[0])),
                cleanup(userData[1]),
                cleanup(userData[2])
        ) {{
            flights = new ArrayList<>();
        }};

        u.getFlights().addAll(userFlights);

        return u;
    }

    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

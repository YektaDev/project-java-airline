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

import static dev.yekta.airline.Util.waitFor;

public class PageManager {
    public static final ConsoleOption back = new ConsoleOption('0', "Go Back", PageManager::toMain);

    public static void toLanding() {
        new ConsolePage("Welcome to YektaAir") {{
            ConsoleOption register = new ConsoleOption('1', "Register", PageManager::toRegister);
            ConsoleOption login = new ConsoleOption('2', "Login", PageManager::toLogin);
            ConsoleOption exit = new ConsoleOption('0', "Exit", () -> clearContent().info("Have a Good Day!"));

            addContent("Please login or register to continue: ");
            addContent(register);
            addContent(login);
            addContent(exit);
            updateView();
            handleChar(false);
        }};
    }

    public static void toRegister() {
        ConsolePage page = new ConsolePage("Register");

        String username = "", password;
        while (true) {
            if (username.length() == 0)
                page.updateView();

            username = page
                    .addContent("Username: ")
                    .getStr(true, "Enter Your Username: ");

            if (AirlineData.getInstance().hasUser(username))
                page.error("This username is already occupied!");
            else if (username.length() > 0)
                break;

            page.dropLastContent();
        }

        do {
            password = page.updateView().getStr(false, "Enter Your Password: ");
        } while (password.length() == 0);

        AirlineData.getInstance().addUser(username, password);
        AirlineData.getInstance().login(username, password);
        page.dropLastContent();
        page.info("Register Completed. Welcome to YektaAir!");
        waitFor(2000);

        toMain();
    }

    public static void toLogin() {
        ConsolePage page = new ConsolePage("Login");
        String username = null, password;

        do {
            do {
                if (username != null && username.length() != 0)
                    page.error("Incorrect Username or Password.");
                else
                    page.updateView().addContent("Username: ");

                username = page.getStr(true, "Enter Your Username: ");
                page.dropLastContent();
                page.dropLastContent();
            }
            while (username.length() == 0);

            do
                password = page.updateView().getStr(false, "Enter Your Password: ");
            while (password.length() == 0);
        }
        while (!AirlineData.getInstance().login(username, password));

        page.dropLastContent();
        page.info(String.format("Hello %s, Welcome Back!", username));
        waitFor(2000);

        toMain();
    }

    public static void toMain() {
        ConsoleOption allFlights = new ConsoleOption('1', "View All Flights", PageManager::toAllFlights);
        ConsoleOption searchFlights = new ConsoleOption('2', "Search Flights", PageManager::toSearchFlights);
        ConsoleOption myFlights = new ConsoleOption('3', "View My Flights", PageManager::toMyFlights);
        ConsoleOption logout = new ConsoleOption('0', "Logout", PageManager::logout);

        new ConsolePage("YektaAir Main Panel") {{
            addContent(allFlights);
            addContent(searchFlights);
            addContent(myFlights);
            addContent(logout);
            updateView();
            handleChar(false);
        }};
    }

    public static void toAllFlights() {
        FlightTable table = new FlightTable(AirlineData.getInstance().getFlights());

        new ConsolePage("All Available Flights") {{
            addContent(table.toString());
            addContent(back);
            updateView();
            handleChar(false);
        }};
    }

    public static void toSearchFlights() {
        new ConsolePage("Search Flights") {{
            Flight result = askForFlightDetails(this);

            if (result != null) {
                addContent(new FlightTable(new ArrayList<Flight>() {{
                    add(result);
                }}).toString());
                addContent(new ConsoleOption('9', "Add the Flight Ticket", () -> PageManager.addTicket(result)));
                addContent(back);
                updateView();
            } else
                addContent(back).updateView().info("No Flights are Available With the Given Information.");

            handleChar(false);
        }};
    }

    public static void toMyFlights() {
        FlightTable table = new FlightTable(AirlineData.getInstance().currentUser().getFlights());

        new ConsolePage("My Flights") {{
            if (table.getFlights().size() > 0)
                addContent(table.toString())
                        .addContent(back)
                        .updateView()
                        .handleChar(false);
            else
                addContent(back)
                        .info("You don't have any flight tickets yet!")
                        .handleChar(false);
        }};
    }

    private static void logout() {
        AirlineData.getInstance().logout();
        toLanding();
    }

    private static Flight askForFlightDetails(ConsolePage page) {
        String from, to, day;
        int hour;
        ArrayList<Flight> result;

        page.addContent("Please enter the information of the flight you're looking for.");

        from = page.updateView().getStr(true, "From: ");

        result = AirlineData.getInstance().findFlights(from);
        if (result.size() <= 1)
            return result.size() > 0 ? result.get(0) : null;

        page.addContent(new FlightTable(result).toString());

        to = page.updateView().getStr(true, "To: ");
        page.dropLastContent();

        result = AirlineData.getInstance().findFlights(from, to);
        if (result.size() <= 1)
            return result.size() > 0 ? result.get(0) : null;

        page.addContent(new FlightTable(result).toString());

        day = page.updateView().getStr(true, "Day of Departure: ");
        page.dropLastContent();

        result = AirlineData.getInstance().findFlights(from, to, day);
        if (result.size() <= 1)
            return result.size() > 0 ? result.get(0) : null;

        page.addContent(new FlightTable(result).toString());

        hour = Integer.parseInt(page.updateView().getStr(true, "Time of Departure: "));
        page.dropLastContent();
        page.dropLastContent();

        result = AirlineData.getInstance().findFlights(from, to, day, hour);
        return result.size() > 0 ? result.get(0) : null;
    }

    private static void addTicket(Flight flight) {
        AirlineData.getInstance().currentUser().addFlight(flight);
        toMain();
    }
}

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

import static dev.yekta.airline.ConsoleFormat.*;

public class FlightTable {
    private ArrayList<Flight> flights;

    public FlightTable(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    @Override
    public String toString() {
        final String INDENT = "\t\t\t\t ";
        final StringBuilder flightsInfo = new StringBuilder("\n"
                + INDENT
                + BG_YELLOW_BRIGHT
                + BLACK_BOLD
                + " Number    From      To        Day       Time "
                + RESET
                + "\n"
        );

        for (Flight f : flights)
            flightsInfo.append(INDENT).append(f).append("\n");

        return flightsInfo.toString();
    }
}

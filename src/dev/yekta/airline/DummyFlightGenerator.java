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

import static dev.yekta.airline.Flight.*;
import static dev.yekta.airline.Util.*;

public class DummyFlightGenerator {
    // Due to uniqueness of flights, counts bigger than 32 will result in creation of 32 flights only.
    public void fillWithRandomFlights(AirlineData data, int count) {
        ArrayList<Integer> id = new ArrayList<>();

        loop:
        for (int i = 0; i < count; i++) {
            int flightId;
            String flightFrom, flightTo;

            id.add(-1);

            do {
                flightId = randomInt(10, 10000);
                id.set(i, flightId);
            } while (id.indexOf(flightId) != i);

            flightFrom = randomStringElement(FLIGHT_LOCATIONS);
            do {
                flightTo = randomStringElement(FLIGHT_LOCATIONS);
            } while (flightFrom.equals(flightTo));

            int dataSize = data.getFlights().size();
            if (dataSize >= 1)
                for (int j = 0; j < dataSize; j++)
                    for (int k = j - 1; k >= 0; k--)
                        if (data.getFlights().get(j).hashCode() == data.getFlights().get(k).hashCode())
                            continue loop;

            data.addFlight(
                    new Flight(
                            String.format("%4s", flightId).replace(' ', '0'),
                            flightFrom,
                            flightTo,
                            randomStringElement(FLIGHT_WEEKDAYS),
                            randomIntElement(FLIGHT_HOURS)
                    )
            );
        }
    }
}

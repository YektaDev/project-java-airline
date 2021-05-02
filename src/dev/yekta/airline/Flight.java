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

import java.util.Objects;

import static dev.yekta.airline.ConsoleFormat.*;

public class Flight {
    public static final String[] FLIGHT_WEEKDAYS = new String[]{"Saturday", "Sunday", "Monday", "Tuesday"};
    public static final int[] FLIGHT_HOURS = new int[]{8, 10, 18, 20};
    public static final String[] FLIGHT_LOCATIONS = new String[]{"Tehran", "Isfahan", "Yazd", "Mashhad", "Shiraz", "Kerman"};

    private final String id;
    private final String from;
    private final String to;
    private final String weekDay;
    private final int hour;

    public Flight(String id, String from, String to, String weekDay, int hour) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.weekDay = weekDay;
        this.hour = hour;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public int getHour() {
        return hour;
    }

    @Override
    public String toString() {
        return String.format("%s  %-10s%-10s%-10s%s",
                BG_WHITE_BRIGHT + BLACK_BOLD + " " + id + "    " + BG_WHITE,
                from,
                to,
                weekDay,
                hour + (hour < 10 ? " " : "") + "   " + RESET
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return hour == flight.hour && from.equals(flight.from) && to.equals(flight.to) && weekDay.equals(flight.weekDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, weekDay, hour);
    }
}

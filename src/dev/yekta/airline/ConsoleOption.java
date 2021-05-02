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

import static dev.yekta.airline.ConsoleFormat.*;

public class ConsoleOption {
    private final char charCode;
    private final String optionTitle;
    private final OptionAction action;

    public ConsoleOption(char charCode, String optionTitle, OptionAction action) {
        this.charCode = charCode;
        this.optionTitle = optionTitle;
        this.action = action;
    }

    public char getCharCode() {
        return charCode;
    }

    public void run() {
        action.run();
    }

    @Override
    public String toString() {
        return String.format("  %s[%s]%s > %s",
                BG_BLACK + RED_BOLD_BRIGHT,
                YELLOW_BOLD_BRIGHT + charCode + RED_BOLD_BRIGHT,
                RESET + WHITE,
                WHITE_BOLD_BRIGHT + optionTitle + RESET
        );
    }
}
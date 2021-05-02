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

public class ConsolePageActionHandler {
    private final ArrayList<ConsoleOption> options;

    public ConsolePageActionHandler(ArrayList<ConsoleOption> options) {
        this.options = options;
    }

    public ConsolePageActionHandler() {
        this(new ArrayList<>());
    }

    public void add(ConsoleOption option) {
        options.add(option);
    }

    public boolean handleActionFor(char charCode) {
        for (ConsoleOption opt : options)
            if (opt.getCharCode() == charCode) {
                opt.run();
                return true;
            }

        return false;
    }
}

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
import java.util.Scanner;

import static dev.yekta.airline.ConsoleFormat.*;

public class ConsolePage {
    private final String title;
    private final ArrayList<String> content;
    private final Scanner sc;
    private final ConsolePageActionHandler handler;

    public ConsolePage(ConsolePageActionHandler handler, String title, ArrayList<String> content) {
        this.handler = handler;
        this.title = title;
        this.content = content;
        sc = new Scanner(System.in);
    }

    public ConsolePage(String title) {
        this(new ConsolePageActionHandler(), title, new ArrayList<>());
    }

    public void error(String message) {
        updateView();
        System.out.println('\n' + BG_BLACK + RED_BOLD_BRIGHT + "[!]" + RESET + " " + RED_BOLD_BRIGHT + message + RESET + '\n');
    }

    public ConsolePage info(String message) {
        updateView();
        System.out.println('\n' + BG_BLACK + GREEN_BOLD_BRIGHT + "[*]" + RESET + " " + GREEN_BOLD_BRIGHT + message + RESET + '\n');
        return this;
    }

    public ConsolePage addContent(String content) {
        this.content.add(content);
        return this;
    }

    public ConsolePage addContent(ConsoleOption option) {
        this.handler.add(option);
        this.content.add(option.toString());
        return this;
    }

    public ConsolePage clearContent() {
        this.content.clear();
        return this;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(RESET);

        str.append(String.format("%s<-=-=-=-=-=-=-=-=-=-=-=-=-%s<*[%s %s %s]*>%s-=-=-=-=-=-=-=-=-=-=-=-=->%s\n", CYAN_BOLD, RED_BOLD_BRIGHT, BG_BLACK, WHITE_BOLD_BRIGHT + title, RESET + RED_BOLD_BRIGHT, CYAN_BOLD, RESET));

        for (String s : content)
            str.append(s).append('\n');

        return str.toString();
    }

    public ConsolePage updateView() {
        clear();
        System.out.print(this);
        return this;
    }

    public String getStr(boolean addToContent, String tempContent) {
        if (tempContent == null)
            System.out.print('\n' + CYAN + "--> " + WHITE_BOLD_BRIGHT);
        else
            System.out.print(tempContent);

        String originalInput = sc.nextLine();

        if (addToContent) {
            final int lastIndex = content.size() - 1;
            final String lastContent = lastIndex > -1 ? content.get(lastIndex) : "";

            if (lastIndex > -1)
                content.set(lastIndex, lastContent.concat(originalInput));
            else
                content.add(lastContent.concat(originalInput));
        }

        return originalInput.trim();
    }

    public char getChar(boolean addToContent, String tempContent) {
        String str = getStr(addToContent, tempContent);
        return str.length() > 0 ? str.charAt(0) : '\u0000';
    }

    public char getChar(boolean addToContent) {
        return getChar(addToContent, null);
    }

    public void dropLastContent() {
        if (content.size() > 0)
            content.remove(content.size() - 1);
    }

    public void clear() {
        System.out.print("\n" + BG_BLACK + YELLOW_BOLD_BRIGHT
                + " [WARNING]: This CLI is not suitable for YektaAir, please use another CLI. "
                + RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
                + "\033[H\033[2J");
        System.out.flush();

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception ignored) {
        }
    }

    public void handleChar(boolean addToContent) {
        while (!handler.handleActionFor(getChar(addToContent))) {
            this.updateView();
        }
    }
}

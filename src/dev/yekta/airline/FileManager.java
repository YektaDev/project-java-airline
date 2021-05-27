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

import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.function.Supplier;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue", "unchecked"})
public class FileManager<T extends Serializable<T>> {
    private RandomAccessFile file;
    private final Supplier<T> supplier;
    private final String filename;

    FileManager(Supplier<T> supplier, String filename) {
        this.filename = filename;
        this.supplier = supplier;
    }

    FileManager<T> open() {
        RandomAccessFile f;
        try {
            f = new RandomAccessFile(filename, "rw");
        } catch (FileNotFoundException ignored) {
            return null;
        }

        this.file = f;
        return this;
    }

    FileManager<T> close() {
        try {
            file.close();
        } catch (IOException e) {
            return null;
        }
        return this;
    }

    private FileManager<T> writeLine(String data) {
        try {
            file.writeChars(data + "\n");
        } catch (IOException ignored) {
            return null;
        }

        return this;
    }

    String readLine() {
        String line;
        try {
            line = file.readLine();
        } catch (IOException e) {
            line = null;
        }

        return line;
    }

    FileManager<T> writeRecord(T data) {
        return writeLine(data.serialize().replaceAll("\n", ""));
    }

    FileManager<T> appendRecord(T data) {
        seekEnd();
        return writeRecord(data);
    }

    T readRecord() {
        return (T) supplier.get().deserialize(readLine());
    }

    long pointerPos() {
        try {
            return file.getFilePointer();
        } catch (IOException e) {
            return -1;
        }
    }

    FileManager<T> seek(long pos) {
        try {
            file.seek(pos);
        } catch (IOException e) {
            return null;
        }
        return this;
    }

    FileManager<T> seekEnd() {
        return seek(length());
    }

    long length() {
        try {
            return file.length();
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * Moves the pointer to a line
     *
     * @param lineNumber Number of the line, starting from 1
     */
    Pair<String, Long> seekLine(long lineNumber, long startFromPos, boolean seekEndOfLine) {
        long currentLine = 0;
        long currentPos = 0;
        String lineData;
        try {
            file.seek(startFromPos);
            do {
                currentLine++;
                lineData = file.readLine();
            } while (currentLine < lineNumber);

            if (!seekEndOfLine) {
                currentPos -= lineData.length();
                file.seek(currentPos);
            }

        } catch (IOException e) {
            return null;
        }

        return new Pair<>(lineData, currentPos);
    }

    Pair<String, Long> seekEndOfLine(long lineNumber) {
        return seekLine(lineNumber, 0, true);
    }
}

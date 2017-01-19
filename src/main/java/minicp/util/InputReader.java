/*
 * This file is part of mini-cp.
 *
 * mini-cp is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mini-cp.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2016 L. Michel, P. Schaus, P. Van Hentenryck
 */


package minicp.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class InputReader {

    private BufferedReader in;
    private StringTokenizer tokenizer;

    public InputReader(String file) {
        try {

            FileInputStream istream = new FileInputStream(file);
            in = new BufferedReader(new InputStreamReader(istream));
            tokenizer = new StringTokenizer("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getInt() throws RuntimeException {
        if (!tokenizer.hasMoreTokens()) {
            try {
                String line;
                do {
                    line = in.readLine();
                    if (line == null) {
                        System.out.println("No more line to read");
                        throw new RuntimeException("End of file");
                    }
                    tokenizer = new StringTokenizer(line);
                } while (line != null && !tokenizer.hasMoreTokens());

            } catch (IOException e) {
                throw new RuntimeException(e.toString());
            }
        }
        return Integer.parseInt(tokenizer.nextToken());
    }

    public String getString() throws RuntimeException {
        if (!tokenizer.hasMoreTokens()) {
            try {
                String line;
                do {
                    line = in.readLine();
                    if (line == null) {
                        System.out.println("No more line to read");
                        throw new RuntimeException("End of file");
                    }
                    tokenizer = new StringTokenizer(in.readLine());
                } while (line != null && !tokenizer.hasMoreTokens());

            } catch (IOException e) {
                throw new RuntimeException(e.toString());
            }
        }
        return tokenizer.nextToken();
    }

}
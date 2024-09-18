/*
 * MIT License
 *
 * Copyright (c) 2023-2024 Volodya Lombrozo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.lombrozo.jsmith.antlr.view;

import com.github.lombrozo.jsmith.antlr.rules.Rule;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * It is an informative output of the generated text.
 * It is useful for debugging purposes when we need to investigate the output path.
 * @since 0.1
 */
public final class TextTree implements Text {

    /**
     * Original output.
     */
    private final Text original;

    /**
     * Constructor.
     * @param original Original output.
     */
    public TextTree(final Text original) {
        this.original = original;
    }

    @Override
    public Rule writer() {
        return this.original.writer();
    }

    @Override
    public List<Text> children() {
        return this.original.children();
    }

    @Override
    public String output() {
        final Columns all = new Columns();
        this.travers(all, this.original, 0);
        return all.toString();
    }


    private void travers(final Columns all, final Text current, final int deep) {
        if (current.children().isEmpty()) {
            final Column column = all.get(deep);
            column.write(current.writer().name());
            final Column res = all.resulting();
            res.write(current.output());
        } else {
            final Column column = all.get(deep);
            column.write(current.writer().name());
            final int width = this.width(current) - 1;
            Stream.generate(() -> " ").limit(width).forEach(column::write);
            for (final Text child : current.children()) {
                this.travers(all, child, deep + 1);
            }
        }
    }

    private int width(final Text text) {
        if (text.children().isEmpty()) {
            return 1;
        }
        return text.children().stream()
            .mapToInt(this::width)
            .sum();
    }

    private final class Columns {

        private final List<Column> all;
        private final Column res;

        public Columns() {
            this(new ArrayList<>(0));
        }

        public Columns(final List<Column> all) {
            this.all = all;
            this.res = new Column();
        }

        public Column next() {
            final Column column = new Column();
            this.all.add(column);
            return column;
        }

        public Column get(final int deep) {
            if (this.all.size() <= deep) {
                this.all.add(new Column());
            }
            return this.all.get(deep);
        }

        public Column resulting() {
            return this.res;
        }

        @Override
        public String toString() {
            final int size = this.rows();
            String res = "";
            for (int row = 0; row < size; row++) {
                String line = "";
                for (int col = 0; col < this.all.size(); col++) {
                    final Column column = this.all.get(col);
                    final String pretty = column.pretty(row, column.maxLength());
                    line += pretty;
                }
                line += this.res.pretty(row, this.res.maxLength());
                res += line + "\n";
            }
            return res;
        }

        public int rows() {
            return this.all.stream()
                .mapToInt(col -> col.paths.size())
                .max()
                .orElse(0);
        }
    }

    private final class Column {

        private List<String> paths;

        public Column() {
            this(new ArrayList<>(0));
        }

        public Column(final List<String> paths) {
            this.paths = paths;
        }

        public void write(final String path) {
            this.paths.add(path);
        }

        String pretty(final int row, final int maxLength) {
            if (row >= this.paths.size()) {
                return Stream.generate(() -> " ")
                    .limit(maxLength)
                    .collect(Collectors.joining());
            }
            final String original = this.paths.get(row);
            final String offset = Stream.generate(() -> " ").limit(maxLength - original.length())
                .collect(Collectors.joining());
            return original + offset;
        }

        int maxLength() {
            return this.paths.stream()
                .map(String::length)
                .max(Integer::compareTo)
                .orElse(0) + 2;
        }
    }

}

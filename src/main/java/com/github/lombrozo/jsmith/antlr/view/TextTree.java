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
import java.util.Arrays;
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
        final Table table = new Table(new ArrayList<>(0));
        this.travers(table, this.original, 0, 0);
        return table.toString();


    }

    private void travers(final Table table, final Text current, final int x, final int y) {
        if (current.children().isEmpty()) {
            final Cell writer = new Cell(y, x, current.writer().name());
            final Cell result = Cell.result(y, current.output());
            table.put(writer);
            table.put(result);
        } else {
            final Cell cell = new Cell(y, x, current.writer().name());
            table.put(cell);
            final List<Text> children = current.children();
            final int size = children.size();
            for (int index = 0; index < size; ++index) {
                final Text child = children.get(index);
//                final int width = this.width(child);
                this.travers(table, child, x + 1, y + index);
            }
        }
    }

    private final class Table {

        private final List<Cell> cells;

        public Table(final List<Cell> cells) {
            this.cells = cells;
        }

        public void put(final Cell writer) {
            this.cells.add(writer);
        }

        @Override
        public String toString() {
            final int numRows = this.numRows() + 1;
            final int numColumns = this.numColumns() + 1;
            String[][] map = new String[numRows][numColumns];
            for (int i = 0; i < numRows; ++i) {
                for (int j = 0; j < numColumns; ++j) {
                    map[i][j] = Stream.generate(() -> " ")
                        .limit(this.columnWidth(j))
                        .collect(
                            Collectors.joining());
                }
            }
            for (final Cell cell : this.cells) {
                final int width = this.columnWidth(cell.column());
                map[cell.row()][cell.column()] = cell.pretty(width);
            }

            for (int i = 0; i < numRows; ++i) {
                for (int j = 0; j < numColumns; ++j) {
                    System.out.print(map[i][j]);
                }
                System.out.println();
            }

            return Arrays.deepToString(map);
        }

        private int numRows() {
            return this.cells.stream()
                .mapToInt(Cell::row)
                .max()
                .orElse(0);
        }

        private int numColumns() {
            return this.cells.stream()
                .mapToInt(Cell::column)
                .max()
                .orElse(0);
        }

        private int columnWidth(final int column) {
            return this.cells.stream()
                .filter(cell -> cell.column() == column)
                .mapToInt(Cell::length)
                .max()
                .orElse(0) + 2;
        }


    }

    private static final class Cell {

        private final int row;
        private final int column;

        private final String text;

        public Cell(
            final int row,
            final int column,
            final String text
        ) {
            this.row = row;
            this.column = column;
            this.text = text;
        }

        public int row() {
            return this.row;
        }

        public int column() {
            return this.column;
        }

        public int length() {
            return this.text.length();
        }

        public String pretty(final int maxLength) {
            final String original = this.text;
            final String offset = Stream.generate(() -> " ").limit(maxLength - original.length())
                .collect(Collectors.joining());
            return original + offset;
        }

        public static Cell result(final int row, final String text) {
            return new Cell(row, 10, text);
        }

    }
}

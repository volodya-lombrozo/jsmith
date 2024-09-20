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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * It is an informative output of the generated text.
 * It is useful for debugging purposes when we need to investigate the output path.
 * @since 0.1
 * @todo #1:90min Beautify {@link TextTree} output.
 *  Currently we have rather ugly {@link TextTree} output.
 *  We can understand what is going on, but it would be much better
 *  to see a more readable output.
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
        final Table table = new Table(TextTree.width(this.original));
        this.fill(table, this.original, 0, 0);
        return table.toString();
    }

    /**
     * Calculate the width of the subtree.
     * Example:
     *         a
     *       /  \
     *      b    c
     *    /  \  / \
     *    d  e f   g
     * Width of the subtree is 4.
     * @param subtree Subtree.
     * @return Width of the subtree.
     */
    private static int width(final Text subtree) {
        final int result;
        if (subtree.children().isEmpty()) {
            result = 1;
        } else {
            result = subtree.children().stream().mapToInt(TextTree::width).sum();
        }
        return result;
    }

    /**
     * Fill the table with the subtree.
     * @param table Table to fill.
     * @param current Current node.
     * @param column X coordinate.
     * @param row Y coordinate.
     */
    private void fill(final Table table, final Text current, final int column, final int row) {
        if (current.children().isEmpty()) {
            table.put(new Cell(row, column, current.writer().name()));
            table.put(new Output(row, current.output()));
        } else {
            final Cell cell = new Cell(row, column, current.writer().name());
            table.put(cell);
            final List<Text> children = current.children();
            final int size = children.size();
            int prev = 0;
            for (int index = 0; index < size; ++index) {
                final Text child = children.get(index);
                this.fill(table, child, column + 1, row + prev);
                prev += TextTree.width(child);
            }
        }
    }

    /**
     * Table.
     * @since 0.1
     */
    private final class Table {

        /**
         * All table cells.
         */
        private final List<Cell> cells;

        /**
         * All rules outputs.
         */
        private final List<Output> outputs;

        /**
         * Constructor.
         * @param size Number of rows.
         */
        private Table(int size) {
            this(
                new ArrayList<>(0),
                new ArrayList<>(Collections.nCopies(size, new Output(0, "")))
            );
        }

        /**
         * Constructor.
         * @param cells Cells.
         * @param outputs Outputs.
         */
        private Table(final List<Cell> cells, final List<Output> outputs) {
            this.cells = cells;
            this.outputs = outputs;
        }

        /**
         * Put the cell.
         * @param cell Cell.
         */
        public void put(final Cell cell) {
            this.cells.add(cell);
        }

        /**
         * Put the output.
         * @param output Output.
         */
        public void put(final Output output) {
            this.outputs.set(output.row, output);
        }

        @Override
        public String toString() {
            final int rows = this.rows() + 1;
            final int columns = this.columns() + 1;
            final String[][] map = new String[rows][columns];
            this.fillByDashes(map);
            this.fillByCellValues(map);
            this.clearLeftCells(map);
            return String.join("\n", this.toLines(map));
        }

        /**
         * Convert the table to the list of lines.
         * @param map Table.
         * @return List of lines.
         */
        private List<String> toLines(final String[][] map) {
            return IntStream.range(0, map.length)
                .mapToObj(
                    index -> String.format(
                        "%s --> %s",
                        String.join(" ", map[index]),
                        this.outputs.get(index).text()
                    )
                ).collect(Collectors.toList());
        }

        /**
         * Clear left cells before the first cell with the text.
         * @param map Table.
         */
        private void clearLeftCells(final String[][] map) {
            for (int row = 0; row < map.length; row++) {
                for (int column = 0; column < map[row].length; column++) {
                    final int length = map[row][column].length();
                    if (map[row][column].replace("-", "").isEmpty()) {
                        map[row][column] = Stream.generate(() -> " ")
                            .limit(length - 2)
                            .collect(Collectors.joining("", " ", "|"));
                    } else {
                        break;
                    }
                }
            }
        }

        /**
         * Fill the table by cell values.
         * @param map Table.
         */
        private void fillByCellValues(final String[][] map) {
            for (final Cell cell : this.cells) {
                map[cell.row()][cell.column()] = cell.pretty(this.width(cell.column()));
            }
        }

        /**
         * Fill the table cells by dashes.
         * @param map Table.
         */
        private void fillByDashes(final String[][] map) {
            for (int row = 0; row < map.length; ++row) {
                for (int column = 0; column < map[row].length; ++column) {
                    map[row][column] = Stream.generate(() -> "-")
                        .limit(this.width(column))
                        .collect(Collectors.joining());
                }
            }
        }

        /**
         * Number of rows.
         * @return Number of rows.
         */
        private int rows() {
            return this.cells.stream()
                .mapToInt(Cell::row)
                .max()
                .orElse(0);
        }

        /**
         * Number of columns.
         * @return Number of columns.
         */
        private int columns() {
            return this.cells.stream()
                .mapToInt(Cell::column)
                .max()
                .orElse(0);
        }

        /**
         * Max column width.
         * Calculates the max length of the cell in the column.
         * @param column Column.
         * @return Max column width.
         */
        private int width(final int column) {
            return this.cells.stream()
                .filter(cell -> cell.column() == column)
                .mapToInt(Cell::length)
                .max()
                .orElse(0);
        }
    }

    /**
     * Final output of some branch.
     * {@link Output} always is the last element in the row.
     * It's why we need to store the row number
     * and don't need to store the column number.
     * @since 0.1
     */
    private static final class Output {
        /**
         * Row.
         */
        private final int row;

        /**
         * Text.
         */
        private final String text;

        /**
         * Constructor.
         * @param row Row.
         * @param text Text.
         */
        private Output(final int row, final String text) {
            this.row = row;
            this.text = text;
        }

        /**
         * Get the resulting text.
         * @return Text.
         */
        String text() {
            return this.text;
        }
    }

    /**
     * Cell in the table.
     */
    private static final class Cell {

        /**
         * X coordinate.
         */
        private final int row;

        /**
         * Y coordinate.
         */
        private final int column;

        /**
         * Text.
         */
        private final String text;

        /**
         * Constructor.
         * @param row Row.
         * @param column Column.
         * @param text Text.
         */
        Cell(
            final int row,
            final int column,
            final String text
        ) {
            this.row = row;
            this.column = column;
            this.text = text;
        }

        /**
         * Get the row.
         * @return Row.
         */
        int row() {
            return this.row;
        }

        /**
         * Get the column.
         * @return Column.
         */
        int column() {
            return this.column;
        }

        /**
         * Get the text length.
         * @return Text length.
         */
        int length() {
            return this.text.length();
        }

        /**
         * Pretty print.
         * If the text is shorter than the column width, fill the rest with '-'.
         * @param max column with.
         * @return Pretty text.
         */
        String pretty(final int max) {
            return String.format(
                "%s%s",
                this.text,
                Stream.generate(() -> "-")
                    .limit(max - this.text.length())
                    .collect(Collectors.joining())
            );
        }
    }
}

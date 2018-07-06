/*
 * matrix-java-sdk - Matrix Client SDK for Java
 * Copyright (C) 2018 Kamax Sarl
 *
 * https://www.kamax.io/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.kamax.matrix.room;

import io.kamax.matrix.event._MatrixEvent;

import java.util.List;

public class MatrixRoomMessageChunk implements _MatrixRoomMessageChunk {

    private String startToken;
    private String endToken;
    private List<_MatrixEvent> events;

    public MatrixRoomMessageChunk(String startToken, String endToken, List<_MatrixEvent> events) {
        this.startToken = startToken;
        this.endToken = endToken;
        this.events = events;
    }

    @Override
    public String getStartToken() {
        return startToken;
    }

    @Override
    public String getEndToken() {
        return endToken;
    }

    @Override
    public List<_MatrixEvent> getEvents() {
        return events;
    }

}

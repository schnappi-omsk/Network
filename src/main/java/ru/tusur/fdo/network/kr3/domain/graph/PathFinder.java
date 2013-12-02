package ru.tusur.fdo.network.kr3.domain.graph;

import java.util.List;

/**
 * User: oleg
 * Date: 02.12.13
 * Time: 21:12
 */
public interface PathFinder {

    List<Vertex> pathTo(Vertex target);

}

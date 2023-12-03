package com.github.lombrozo.jsmith.nodes.types;

import com.github.lombrozo.jsmith.nodes.Node;


/**
 * Interface for unannotated types.
 */
public interface IUnannType extends Node {
    /**
     * @return The of the object.
     */
    String getType();
}

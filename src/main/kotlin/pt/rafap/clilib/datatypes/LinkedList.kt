package pt.rafap.clilib.datatypes

import pt.rafap.clilib.tools.joinToString

internal class LinkedList<T> {

    var head: Node<T>? = null
    private var tail: Node<T>? = null

    data class Node<T>(var value: T, var next: Node<T>? = null, var prev: Node<T>? = null)

    fun add(value: T) {
        val newNode = Node(value)
        if (head == null) {
            head = newNode
            tail = newNode
        } else {
            tail?.next = newNode
            tail = newNode
        }
    }

    fun remove(value: T): Boolean {
        var current = head
        var previous: Node<T>? = null

        while (current != null) {
            if (current.value == value) {
                if (previous == null) {
                    head = current.next
                } else {
                    previous.next = current.next
                }
                if (current.next == null) {
                    tail = previous
                }
                return true
            }
            previous = current
            current = current.next
        }
        return false
    }

    fun contains(value: T): Boolean {
        var current = head
        while (current != null) {
            if (current.value == value) return true
            current = current.next
        }
        return false
    }

    fun isEmpty(): Boolean = head == null

    fun clear() {
        head = null
        tail = null
    }

    override fun toString(): String {
        val values = mutableListOf<String>()
        var current = head
        while (current != null) {
            values.add(current.value.joinToString())
            current = current.next
        }
        return values.joinToString(" -> ")
    }
}
package pt.rafap.clilib.datatypes

interface Operator<T> {
    operator fun invoke(left: T?, right: T?): T?
}